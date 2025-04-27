import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * Dialog for trading at landmarks in the GUI
 */
public class TradingDialog extends JDialog {
    private final Player player;
    private final Inventory inventory;

    // GUI components
    private JLabel moneyLabel;
    private JTextArea resultArea;
    private JPanel actionPanel;
    
    // Price modifiers
    private double priceFactor;
    
    // Colors - match EnhancedGUI
    private final Color BACKGROUND_COLOR = new Color(240, 220, 180); // Parchment/sepia
    private final Color PANEL_COLOR = new Color(200, 170, 130);      // Darker parchment
    private final Color TEXT_COLOR = new Color(80, 30, 0);           // Dark brown
    private final Color HEADER_COLOR = new Color(120, 60, 0);        // Medium brown
    private final Color ACCENT_COLOR = new Color(160, 100, 40);      // Light brown

    /**
     * Constructor
     */
    public TradingDialog(Frame owner, Player player, Inventory inventory) {
        super(owner, "Trading Post", true);
        
        this.player = player;
        this.inventory = inventory;
        
        // Random price fluctuations for supply and demand
        Random random = new Random();
        this.priceFactor = 0.8 + (random.nextDouble() * 0.4); // 0.8 to 1.2 multiplier
        
        // Gender price adjustment - women get better prices
        if ("female".equalsIgnoreCase(player.getGender())) {
            // 5-15% better prices for female characters (historically women were often better at bargaining)
            double femaleDiscount = 0.85 + (random.nextDouble() * 0.10);
            this.priceFactor *= femaleDiscount;
        }
        
        initUI();
        pack();
        setLocationRelativeTo(owner);
        setSize(800, 500);
        setResizable(false);
    }
    
    /**
     * Initialize the user interface
     */
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        if (player.getJob() == Job.MERCHANT) {
            // 5-10% additional discount for merchants
            double merchantDiscount = 0.05 + (new Random().nextDouble() * 0.05);
            this.priceFactor *= (1.0 - merchantDiscount);
        }
        
        // Top panel with title and info
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel titleLabel = new JLabel("Trading Post", JLabel.CENTER);
        titleLabel.setFont(FontManager.WESTERN_FONT_TITLE);
        titleLabel.setForeground(TEXT_COLOR);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        
        JTextArea infoText = new JTextArea(
            "Trading was an important part of pioneer life on the trail. " +
            "You can buy, sell, or barter with local traders for supplies." + 
            (player.getGender().equalsIgnoreCase("female") ? 
            " As a woman in this era, your bargaining skills may get you better prices." : "")
        );
        infoText.setFont(FontManager.getWesternFont(14f));
        infoText.setLineWrap(true);
        infoText.setWrapStyleWord(true);
        infoText.setEditable(false);
        infoText.setBackground(PANEL_COLOR);
        infoText.setForeground(TEXT_COLOR);
        infoText.setBorder(new EmptyBorder(10, 10, 10, 10));
        topPanel.add(infoText, BorderLayout.CENTER);
        
        moneyLabel = new JLabel("Available Funds: $" + player.getMoney(), JLabel.CENTER);
        moneyLabel.setFont(FontManager.getBoldWesternFont(16f));
        moneyLabel.setForeground(TEXT_COLOR);
        topPanel.add(moneyLabel, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Center panel with trading options
        JPanel optionsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        optionsPanel.setBackground(PANEL_COLOR);
        optionsPanel.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        // Trading option buttons
        JButton buyButton = createOptionButton("Buy Supplies", 
            "Purchase supplies at local prices", 
            e -> showBuyOptions());
        
        JButton sellButton = createOptionButton("Sell Supplies", 
            "Sell your excess supplies for money", 
            e -> showSellOptions());
        
        JButton barterButton = createOptionButton("Barter with Travelers", 
            "Trade items with other travelers (no money involved)", 
            e -> showBarterOptions());
        
        optionsPanel.add(buyButton);
        optionsPanel.add(sellButton);
        optionsPanel.add(barterButton);
        
        add(optionsPanel, BorderLayout.WEST);
        
        // Action panel (will change based on selection)
        actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBackground(PANEL_COLOR);
        actionPanel.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        // Default message
        JLabel defaultLabel = new JLabel("Select an option to begin trading", JLabel.CENTER);
        defaultLabel.setFont(FontManager.getBoldWesternFont(14f));
        defaultLabel.setForeground(TEXT_COLOR);
        actionPanel.add(defaultLabel, BorderLayout.CENTER);
        
        add(actionPanel, BorderLayout.CENTER);
        
        // Result area
        resultArea = new JTextArea(5, 40);
        resultArea.setFont(FontManager.getWesternFont(14f));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setEditable(false);
        resultArea.setBackground(PANEL_COLOR);
        resultArea.setForeground(TEXT_COLOR);
        resultArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(0, 0, 0, 0)
        ));
        
        add(scrollPane, BorderLayout.SOUTH);
        
        // Bottom panel with exit button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton doneButton = new JButton("Continue Journey");
        doneButton.setFont(FontManager.getBoldWesternFont(14f));
        doneButton.setForeground(TEXT_COLOR);
        doneButton.setBackground(PANEL_COLOR);
        doneButton.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(5, 10, 5, 10)
        ));
        doneButton.addActionListener(e -> dispose());
        
        buttonPanel.add(doneButton);
        add(buttonPanel, BorderLayout.PAGE_END);
    }
    
    /**
     * Creates an option button with description
     */
    private JButton createOptionButton(String title, String description, java.awt.event.ActionListener action) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setBackground(PANEL_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FontManager.getBoldWesternFont(14f));
        titleLabel.setForeground(TEXT_COLOR);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(FontManager.getWesternFont(12f));
        descLabel.setForeground(TEXT_COLOR);
        
        button.add(titleLabel, BorderLayout.NORTH);
        button.add(descLabel, BorderLayout.CENTER);
        
        button.addActionListener(action);
        
        return button;
    }
    
    /**
     * Show buy options panel
     */
    private void showBuyOptions() {
        actionPanel.removeAll();
        
        JPanel buyPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        buyPanel.setBackground(PANEL_COLOR);
        
        JLabel titleLabel = new JLabel("Buy Supplies", JLabel.CENTER);
        titleLabel.setFont(FontManager.getBoldWesternFont(16f));
        titleLabel.setForeground(HEADER_COLOR);
        
        // Calculate prices with random variation
        int foodPrice = Math.max(1, (int)Math.round(1.5 * priceFactor));
        int partPrice = Math.max(20, (int)Math.round(25 * priceFactor));
        int medicinePrice = Math.max(15, (int)Math.round(20 * priceFactor));
        int ammoPrice = Math.max(2, (int)Math.round(2.5 * priceFactor));
        
        // Create buy buttons
        JPanel foodPanel = createTradeButton("Food - $" + foodPrice + " per pound", 
                             "Buy Food", e -> buyFood(foodPrice));
        
        JPanel partsPanel = createTradeButton("Wagon Parts - $" + partPrice + " each", 
                              "Buy Parts", e -> buyWagonParts(partPrice));
        
        JPanel medicinePanel = createTradeButton("Medicine Kits - $" + medicinePrice + " each", 
                                 "Buy Kits", e -> buyMedicine(medicinePrice));
        
        JPanel ammoPanel = createTradeButton("Ammunition - $" + ammoPrice + " per box of 20 rounds", 
                             "Buy Ammo", e -> buyAmmunition(ammoPrice));
        
        buyPanel.add(titleLabel);
        buyPanel.add(foodPanel);
        buyPanel.add(partsPanel);
        buyPanel.add(medicinePanel);
        buyPanel.add(ammoPanel);
        
        actionPanel.add(buyPanel, BorderLayout.CENTER);
        actionPanel.revalidate();
        actionPanel.repaint();
    }
    
    /**
     * Show sell options panel
     */
    private void showSellOptions() {
        actionPanel.removeAll();
        
        JPanel sellPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        sellPanel.setBackground(PANEL_COLOR);
        
        JLabel titleLabel = new JLabel("Sell Supplies", JLabel.CENTER);
        titleLabel.setFont(FontManager.getBoldWesternFont(16f));
        titleLabel.setForeground(HEADER_COLOR);
        
        // Calculate selling prices (always less than buying prices)
        int foodSellPrice = Math.max(1, (int)Math.round(0.75 * priceFactor));
        int partSellPrice = Math.max(10, (int)Math.round(12 * priceFactor));
        int medicineSellPrice = Math.max(7, (int)Math.round(10 * priceFactor));
        int ammoSellPrice = Math.max(1, (int)Math.round(1.25 * priceFactor));
        
        // Create sell buttons
        JPanel foodPanel = createTradeButton("Food - $" + foodSellPrice + " per pound" +
                             " (Have: " + inventory.getFood() + " pounds)", 
                             "Sell Food", e -> sellFood(foodSellPrice));
        
        JPanel partsPanel = createTradeButton("Wagon Parts - $" + partSellPrice + " each" +
                              " (Have: " + inventory.getWagonParts() + ")", 
                              "Sell Parts", e -> sellWagonParts(partSellPrice));
        
        JPanel medicinePanel = createTradeButton("Medicine Kits - $" + medicineSellPrice + " each" +
                                 " (Have: " + inventory.getMedicine() + ")", 
                                 "Sell Kits", e -> sellMedicine(medicineSellPrice));
        
        JPanel ammoPanel = createTradeButton("Ammunition - $" + ammoSellPrice + " per box of 20 rounds" +
                             " (Have: " + inventory.getAmmunition() + " rounds)", 
                             "Sell Ammo", e -> sellAmmunition(ammoSellPrice));
        
        sellPanel.add(titleLabel);
        sellPanel.add(foodPanel);
        sellPanel.add(partsPanel);
        sellPanel.add(medicinePanel);
        sellPanel.add(ammoPanel);
        
        actionPanel.add(sellPanel, BorderLayout.CENTER);
        actionPanel.revalidate();
        actionPanel.repaint();
    }
    
    /**
     * Show barter options
     */
    private void showBarterOptions() {
        actionPanel.removeAll();
        
        JPanel barterPanel = new JPanel(new GridLayout(4, 1, 5, 15));
        barterPanel.setBackground(PANEL_COLOR);
        barterPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel titleLabel = new JLabel("Barter with Travelers", JLabel.CENTER);
        titleLabel.setFont(FontManager.getBoldWesternFont(16f));
        titleLabel.setForeground(HEADER_COLOR);
        
        // Create direct barter button panels with individual action listeners
        JPanel option1Panel = new JPanel(new BorderLayout(10, 0));
        option1Panel.setBackground(PANEL_COLOR);
        option1Panel.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        JLabel desc1Label = new JLabel("<html><body width='250'>Trade 50 pounds of food for 1 wagon part</body></html>");
        desc1Label.setFont(FontManager.getWesternFont(12f));
        desc1Label.setForeground(TEXT_COLOR);
        
        JButton button1 = new JButton("Make Trade");
        button1.setFont(FontManager.getBoldWesternFont(12f));
        button1.setForeground(TEXT_COLOR);
        button1.setBackground(PANEL_COLOR);
        button1.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 1),
            new EmptyBorder(3, 8, 3, 8)
        ));
        button1.setPreferredSize(new Dimension(100, 30));
        button1.setFocusPainted(false);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                barterFoodForPart();
            }
        });
        
        option1Panel.add(desc1Label, BorderLayout.CENTER);
        option1Panel.add(button1, BorderLayout.EAST);
        
        // Create second barter option
        JPanel option2Panel = new JPanel(new BorderLayout(10, 0));
        option2Panel.setBackground(PANEL_COLOR);
        option2Panel.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        JLabel desc2Label = new JLabel("<html><body width='250'>Trade 30 pounds of food for 1 medicine kit</body></html>");
        desc2Label.setFont(FontManager.getWesternFont(12f));
        desc2Label.setForeground(TEXT_COLOR);
        
        JButton button2 = new JButton("Make Trade");
        button2.setFont(FontManager.getBoldWesternFont(12f));
        button2.setForeground(TEXT_COLOR);
        button2.setBackground(PANEL_COLOR);
        button2.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 1),
            new EmptyBorder(3, 8, 3, 8)
        ));
        button2.setPreferredSize(new Dimension(100, 30));
        button2.setFocusPainted(false);
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                barterFoodForMedicine();
            }
        });
        
        option2Panel.add(desc2Label, BorderLayout.CENTER);
        option2Panel.add(button2, BorderLayout.EAST);
        
        // Create third barter option
        JPanel option3Panel = new JPanel(new BorderLayout(10, 0));
        option3Panel.setBackground(PANEL_COLOR);
        option3Panel.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        JLabel desc3Label = new JLabel("<html><body width='250'>Trade 2 medicine kits for 40 rounds of ammunition</body></html>");
        desc3Label.setFont(FontManager.getWesternFont(12f));
        desc3Label.setForeground(TEXT_COLOR);
        
        JButton button3 = new JButton("Make Trade");
        button3.setFont(FontManager.getBoldWesternFont(12f));
        button3.setForeground(TEXT_COLOR);
        button3.setBackground(PANEL_COLOR);
        button3.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 1),
            new EmptyBorder(3, 8, 3, 8)
        ));
        button3.setPreferredSize(new Dimension(100, 30));
        button3.setFocusPainted(false);
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                barterMedicineForAmmo();
            }
        });
        
        option3Panel.add(desc3Label, BorderLayout.CENTER);
        option3Panel.add(button3, BorderLayout.EAST);
        
        // Add everything to barterPanel
        barterPanel.add(titleLabel);
        barterPanel.add(option1Panel);
        barterPanel.add(option2Panel);
        barterPanel.add(option3Panel);
        
        // Add to actionPanel
        actionPanel.add(barterPanel, BorderLayout.CENTER);
        actionPanel.revalidate();
        actionPanel.repaint();
    }
    
    /**
     * Create a trade option button with description
     */
    private JPanel createTradeButton(String description, String buttonText, 
                                     java.awt.event.ActionListener action) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(PANEL_COLOR);
        panel.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        JLabel descLabel = new JLabel("<html><body width='250'>" + description + "</body></html>");
        descLabel.setFont(FontManager.getWesternFont(12f));
        descLabel.setForeground(TEXT_COLOR);
        panel.add(descLabel, BorderLayout.CENTER);
        
        JButton button = new JButton(buttonText);
        button.setFont(FontManager.getBoldWesternFont(12f));
        button.setForeground(TEXT_COLOR);
        button.setBackground(PANEL_COLOR);
        button.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 1),
            new EmptyBorder(3, 8, 3, 8)
        ));
        
        // Add action listener directly to the button
        button.addActionListener(action);
        
        // Make the button bigger and more visible
        button.setPreferredSize(new Dimension(100, 30));
        button.setFocusPainted(false); // Remove focus border
        
        panel.add(button, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Get quantity via styled dialog
     */
    private int getQuantity(String item, int maxAllowed) {
        // Determine if we're buying or selling based on the context
        String actionVerb = "";
        if (item.contains("sell")) {
            actionVerb = "to sell";
            // Extract the actual item name from the combined string
            item = item.replace("sell ", "");
        } else {
            actionVerb = "to buy";
        }
        
        // Create a styled quantity dialog similar to other UI elements
        JDialog quantityDialog = new JDialog(this, "Enter Quantity " + actionVerb, true);
        quantityDialog.setLayout(new BorderLayout(10, 10));
        quantityDialog.getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Title - make font smaller to fit better
        JLabel titleLabel = new JLabel("How many " + item + " would you like " + actionVerb + "?", JLabel.CENTER);
        titleLabel.setFont(FontManager.getBoldWesternFont(14f)); // Reduced from 16f to 14f
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setBackground(PANEL_COLOR);
        inputPanel.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Spinner for quantity selection
        SpinnerNumberModel model;
        if (maxAllowed > 0 && !actionVerb.contains("sell")) {
            // Only apply max limit when buying
            model = new SpinnerNumberModel(1, 1, maxAllowed, 1);
        } else {
            model = new SpinnerNumberModel(1, 1, 999, 1);
        }
        
        JSpinner quantitySpinner = new JSpinner(model);
        quantitySpinner.setFont(FontManager.getBoldWesternFont(14f));
        JComponent editor = quantitySpinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JSpinner.DefaultEditor spinnerEditor = (JSpinner.DefaultEditor)editor;
            spinnerEditor.getTextField().setForeground(TEXT_COLOR);
            spinnerEditor.getTextField().setBackground(PANEL_COLOR.brighter());
            spinnerEditor.getTextField().setHorizontalAlignment(JTextField.CENTER);
        }
        
        // Only show max label for buying, not for selling
        JLabel maxLabel = new JLabel("");
        if (maxAllowed > 0 && !actionVerb.contains("sell")) {
            maxLabel.setText("(Maximum: " + maxAllowed + ")");
            maxLabel.setFont(FontManager.getWesternFont(12f));
            maxLabel.setForeground(TEXT_COLOR);
            maxLabel.setHorizontalAlignment(JLabel.CENTER);
        }
        
        // Add components to input panel
        inputPanel.add(quantitySpinner, BorderLayout.CENTER);
        if (maxAllowed > 0 && !actionVerb.contains("sell")) {
            inputPanel.add(maxLabel, BorderLayout.SOUTH);
        }
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton confirmButton = new JButton("Confirm");
        confirmButton.setFont(FontManager.getBoldWesternFont(14f));
        confirmButton.setForeground(TEXT_COLOR);
        confirmButton.setBackground(PANEL_COLOR);
        confirmButton.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(5, 15, 5, 15)
        ));
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(FontManager.getBoldWesternFont(14f));
        cancelButton.setForeground(TEXT_COLOR);
        cancelButton.setBackground(PANEL_COLOR);
        cancelButton.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(5, 15, 5, 15)
        ));
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        // Add all components to dialog
        quantityDialog.add(titleLabel, BorderLayout.NORTH);
        quantityDialog.add(inputPanel, BorderLayout.CENTER);
        quantityDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Final result
        final int[] result = {0};
        
        // Add button actions
        confirmButton.addActionListener(e -> {
            result[0] = (Integer) quantitySpinner.getValue();
            quantityDialog.dispose();
        });
        
        cancelButton.addActionListener(e -> {
            result[0] = 0;
            quantityDialog.dispose();
        });
        
        // Show dialog
        quantityDialog.pack();
        // Make dialog wider to ensure text fits
        int height = 220;
        int width = 515;
        quantityDialog.setSize(width, height);
        quantityDialog.setLocationRelativeTo(this);
        quantityDialog.setResizable(false);
        quantityDialog.setVisible(true);
        
        return result[0];
    }
    
    /**
     * Update money display
     */
    private void updateMoneyDisplay() {
        moneyLabel.setText("Available Funds: $" + player.getMoney());
    }
    
    // Buy methods
    
    private void buyFood(int price) {
        int quantity = getQuantity("pounds of food", -1);
        if (quantity <= 0) return;
        
        int totalCost = quantity * price;
        if (totalCost > player.getMoney()) {
            resultArea.setText("You don't have enough money for that purchase!");
            return;
        }
        
        player.spendMoney(totalCost);
        inventory.addFood(quantity);
        updateMoneyDisplay();
        
        resultArea.setText("You purchased " + quantity + " pounds of food for $" + totalCost + 
                          "\n\nHistorical Note: A typical family of four needed about 600-1000 pounds of flour, " +
                          "plus hundreds of pounds of bacon, sugar, coffee, and other staples for the journey. " +
                          "Many emigrants also gathered wild fruits, hunted game, and fished to supplement their rations.");
    }
    
    private void buyWagonParts(int basePrice) {
        // Create styled part selection dialog instead of using JOptionPane
        JDialog partDialog = new JDialog(this, "Select Wagon Part", true);
        partDialog.setLayout(new BorderLayout(10, 10));
        partDialog.getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Title
        JLabel titleLabel = new JLabel("Select a wagon part to purchase:", JLabel.CENTER);
        titleLabel.setFont(FontManager.getBoldWesternFont(16f));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Options panel
        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 5, 10));
        optionsPanel.setBackground(PANEL_COLOR);
        optionsPanel.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Part options
        JRadioButton wheelOption = new JRadioButton("Wheel - $25 each");
        JRadioButton axleOption = new JRadioButton("Axle - $20 each");
        JRadioButton tongueOption = new JRadioButton("Tongue - $15 each");
        JRadioButton bowOption = new JRadioButton("Wagon Bow - $10 each");
        
        // Style the radio buttons
        wheelOption.setFont(FontManager.getWesternFont(14f));
        axleOption.setFont(FontManager.getWesternFont(14f));
        tongueOption.setFont(FontManager.getWesternFont(14f));
        bowOption.setFont(FontManager.getWesternFont(14f));
        
        wheelOption.setForeground(TEXT_COLOR);
        axleOption.setForeground(TEXT_COLOR);
        tongueOption.setForeground(TEXT_COLOR);
        bowOption.setForeground(TEXT_COLOR);
        
        wheelOption.setBackground(PANEL_COLOR);
        axleOption.setBackground(PANEL_COLOR);
        tongueOption.setBackground(PANEL_COLOR);
        bowOption.setBackground(PANEL_COLOR);
        
        // Group the radio buttons
        ButtonGroup partGroup = new ButtonGroup();
        partGroup.add(wheelOption);
        partGroup.add(axleOption);
        partGroup.add(tongueOption);
        partGroup.add(bowOption);
        wheelOption.setSelected(true); // Default selection
        
        // Add options to panel
        optionsPanel.add(wheelOption);
        optionsPanel.add(axleOption);
        optionsPanel.add(tongueOption);
        optionsPanel.add(bowOption);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton confirmButton = new JButton("Confirm");
        confirmButton.setFont(FontManager.getBoldWesternFont(14f));
        confirmButton.setForeground(TEXT_COLOR);
        confirmButton.setBackground(PANEL_COLOR);
        confirmButton.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(5, 15, 5, 15)
        ));
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(FontManager.getBoldWesternFont(14f));
        cancelButton.setForeground(TEXT_COLOR);
        cancelButton.setBackground(PANEL_COLOR);
        cancelButton.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(5, 15, 5, 15)
        ));
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        // Add components to dialog
        partDialog.add(titleLabel, BorderLayout.NORTH);
        partDialog.add(optionsPanel, BorderLayout.CENTER);
        partDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Store result
        final String[] selectedPart = {null};
        
        // Add button actions
        confirmButton.addActionListener(e -> {
            if (wheelOption.isSelected()) {
                selectedPart[0] = "Wheel";
            } else if (axleOption.isSelected()) {
                selectedPart[0] = "Axle";
            } else if (tongueOption.isSelected()) {
                selectedPart[0] = "Tongue";
            } else if (bowOption.isSelected()) {
                selectedPart[0] = "Wagon Bow";
            }
            partDialog.dispose();
        });
        
        cancelButton.addActionListener(e -> {
            selectedPart[0] = null;
            partDialog.dispose();
        });
        
        // Show dialog
        partDialog.pack();
        partDialog.setSize(400, 350);
        partDialog.setLocationRelativeTo(this);
        partDialog.setResizable(false);
        partDialog.setVisible(true);
        
        // Process result
        if (selectedPart[0] == null) return; // User canceled
        
        if (selectedPart[0].equals("Wheel")) {
            buyWheels(25);
        } else if (selectedPart[0].equals("Axle")) {
            buyAxles(20);
        } else if (selectedPart[0].equals("Tongue")) {
            buyTongues(15);
        } else if (selectedPart[0].equals("Wagon Bow")) {
            buyWagonBows(10);
        }
    }
    
    private void buyWheels(int price) {
        int quantity = getQuantity("wheels", -1);
        if (quantity <= 0) return;
        
        int totalCost = quantity * price;
        int totalWeight = quantity * 50; // 50 lbs per wheel
        
        if (totalCost > player.getMoney()) {
            resultArea.setText("You don't have enough money for that purchase!");
            return;
        }
        
        if (!inventory.hasWeightCapacity(totalWeight)) {
            resultArea.setText("Your wagon cannot carry that much additional weight!\n" +
                               "Current load: " + inventory.getCurrentWeight() + "/" + 
                               inventory.getMaxWeightCapacity() + " pounds\n" +
                               "Additional weight: " + totalWeight + " pounds");
            return;
        }
        
        player.spendMoney(totalCost);
        inventory.addWheels(quantity);
        updateMoneyDisplay();
        
        resultArea.setText("You purchased " + quantity + " wheel" + (quantity > 1 ? "s" : "") + " for $" + totalCost + 
                          "\n\nHistorical Note: Wagon wheels were typically 4 to 5 feet in diameter with iron tires " + 
                          "that could come loose in dry weather. Emigrants often soaked wheels in rivers overnight " +
                          "to make the wood expand and tighten the fit of the tire.");
    }
    
    private void buyAxles(int price) {
        int quantity = getQuantity("axles", -1);
        if (quantity <= 0) return;
        
        int totalCost = quantity * price;
        int totalWeight = quantity * 40; // 40 lbs per axle
        
        if (totalCost > player.getMoney()) {
            resultArea.setText("You don't have enough money for that purchase!");
            return;
        }
        
        if (!inventory.hasWeightCapacity(totalWeight)) {
            resultArea.setText("Your wagon cannot carry that much additional weight!\n" +
                               "Current load: " + inventory.getCurrentWeight() + "/" + 
                               inventory.getMaxWeightCapacity() + " pounds\n" +
                               "Additional weight: " + totalWeight + " pounds");
            return;
        }
        
        player.spendMoney(totalCost);
        inventory.addAxles(quantity);
        updateMoneyDisplay();
        
        resultArea.setText("You purchased " + quantity + " axle" + (quantity > 1 ? "s" : "") + " for $" + totalCost + 
                          "\n\nHistorical Note: Axles were typically made of hardwood like hickory or oak. " +
                          "They were subject to tremendous stress and could snap when crossing rough terrain " +
                          "or when wagons were overloaded. A broken axle could strand travelers for days.");
    }
    
    private void buyTongues(int price) {
        int quantity = getQuantity("tongues", -1);
        if (quantity <= 0) return;
        
        int totalCost = quantity * price;
        int totalWeight = quantity * 30; // 30 lbs per tongue
        
        if (totalCost > player.getMoney()) {
            resultArea.setText("You don't have enough money for that purchase!");
            return;
        }
        
        if (!inventory.hasWeightCapacity(totalWeight)) {
            resultArea.setText("Your wagon cannot carry that much additional weight!\n" +
                               "Current load: " + inventory.getCurrentWeight() + "/" + 
                               inventory.getMaxWeightCapacity() + " pounds\n" +
                               "Additional weight: " + totalWeight + " pounds");
            return;
        }
        
        player.spendMoney(totalCost);
        inventory.addTongues(quantity);
        updateMoneyDisplay();
        
        resultArea.setText("You purchased " + quantity + " tongue" + (quantity > 1 ? "s" : "") + " for $" + totalCost + 
                          "\n\nHistorical Note: The wagon tongue was the long pole that connected the wagon to the " +
                          "oxen's yoke. They could break when the wagon jackknifed or when crossing steep terrain. " +
                          "Many emigrants carried a spare tongue as they were essential for wagon movement.");
    }
    
    private void buyWagonBows(int price) {
        int quantity = getQuantity("wagon bows", -1);
        if (quantity <= 0) return;
        
        int totalCost = quantity * price;
        int totalWeight = quantity * 10; // 10 lbs per wagon bow
        
        if (totalCost > player.getMoney()) {
            resultArea.setText("You don't have enough money for that purchase!");
            return;
        }
        
        if (!inventory.hasWeightCapacity(totalWeight)) {
            resultArea.setText("Your wagon cannot carry that much additional weight!\n" +
                               "Current load: " + inventory.getCurrentWeight() + "/" + 
                               inventory.getMaxWeightCapacity() + " pounds\n" +
                               "Additional weight: " + totalWeight + " pounds");
            return;
        }
        
        player.spendMoney(totalCost);
        inventory.addWagonBows(quantity);
        updateMoneyDisplay();
        
        resultArea.setText("You purchased " + quantity + " wagon bow" + (quantity > 1 ? "s" : "") + " for $" + totalCost + 
                          "\n\nHistorical Note: Wagon bows were the arched wooden slats that supported the canvas " +
                          "cover. Made of flexible wood like ash or hickory, they could be damaged in storms or " +
                          "by low-hanging branches. The canvas cover protected supplies from weather and provided " +
                          "minimal shelter at night.");
    }
    
    private void buyMedicine(int price) {
        int quantity = getQuantity("medicine kits", -1);
        if (quantity <= 0) return;
        
        int totalCost = quantity * price;
        int totalWeight = quantity * 5; // 5 pounds per medicine kit
        
        if (totalCost > player.getMoney()) {
            resultArea.setText("You don't have enough money for that purchase!");
            return;
        }
        
        if (!inventory.hasWeightCapacity(totalWeight)) {
            resultArea.setText("Your wagon cannot carry that much additional weight!\n" +
                              "Current load: " + inventory.getCurrentWeight() + "/" + 
                              inventory.getMaxWeightCapacity() + " pounds\n" +
                              "Additional weight: " + totalWeight + " pounds");
            return;
        }
        
        player.spendMoney(totalCost);
        inventory.addMedicine(quantity);
        updateMoneyDisplay();
        
        resultArea.setText("You purchased " + quantity + " medicine kits for $" + totalCost + 
                          "\n\nHistorical Note: Cholera was the most feared disease on the trail, " +
                          "with outbreaks in 1849, 1850, and 1852 that killed thousands. Other common " +
                          "ailments included dysentery, mountain fever, scurvy, and various injuries.");
    }
    
    private void buyAmmunition(int price) {
        int quantity = getQuantity("boxes of ammunition", -1);
        if (quantity <= 0) return;
        
        int totalCost = quantity * price;
        int totalWeight = quantity * 3; // 3 pounds per box of 20 rounds
        
        if (totalCost > player.getMoney()) {
            resultArea.setText("You don't have enough money for that purchase!");
            return;
        }
        
        if (!inventory.hasWeightCapacity(totalWeight)) {
            resultArea.setText("Your wagon cannot carry that much additional weight!\n" +
                              "Current load: " + inventory.getCurrentWeight() + "/" + 
                              inventory.getMaxWeightCapacity() + " pounds\n" +
                              "Additional weight: " + totalWeight + " pounds");
            return;
        }
        
        player.spendMoney(totalCost);
        inventory.addAmmunition(quantity * 20); // 20 rounds per box
        updateMoneyDisplay();
        
        resultArea.setText("You purchased " + quantity + " boxes of ammunition (" + 
                          (quantity * 20) + " rounds) for $" + totalCost + 
                          "\n\nHistorical Note: While Native American attacks were greatly exaggerated in " +
                          "popular culture, ammunition was crucial for hunting. Buffalo, deer, antelope, and " +
                          "smaller game provided important food sources along certain stretches of the trail.");
    }
    
    // Sell methods
    
    private void sellFood(int price) {
        int maxFood = inventory.getFood();
        if (maxFood <= 0) {
            resultArea.setText("You don't have any food to sell!");
            return;
        }
        
        int quantity = getQuantity("sell food", maxFood);
        if (quantity <= 0) return;
        
        int totalEarned = quantity * price;
        inventory.consumeFood(quantity);
        player.addMoney(totalEarned);
        updateMoneyDisplay();
        
        resultArea.setText("You sold " + quantity + " pounds of food for $" + totalEarned + 
                         "\n\nHistorical Note: Food supplies often fluctuated in value along the trail. " +
                         "At some forts and trading posts, food might be scarce and valuable, while at others " +
                         "there might be a surplus, lowering prices.");
    }
    
    private void sellWagonParts(int basePrice) {
        // Create styled part selection dialog instead of using JOptionPane
        JDialog partDialog = new JDialog(this, "Select Wagon Part", true);
        partDialog.setLayout(new BorderLayout(10, 10));
        partDialog.getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Title
        JLabel titleLabel = new JLabel("Select a wagon part to sell:", JLabel.CENTER);
        titleLabel.setFont(FontManager.getBoldWesternFont(16f));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Options panel
        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 5, 10));
        optionsPanel.setBackground(PANEL_COLOR);
        optionsPanel.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Part options with inventory counts
        JRadioButton wheelOption = new JRadioButton("Wheels - $15 each (Have: " + inventory.getWheels() + ")");
        JRadioButton axleOption = new JRadioButton("Axles - $12 each (Have: " + inventory.getAxles() + ")");
        JRadioButton tongueOption = new JRadioButton("Tongues - $10 each (Have: " + inventory.getTongues() + ")");
        JRadioButton bowOption = new JRadioButton("Wagon Bows - $7 each (Have: " + inventory.getWagonBows() + ")");
        
        // Disable options if none available
        wheelOption.setEnabled(inventory.getWheels() > 0);
        axleOption.setEnabled(inventory.getAxles() > 0);
        tongueOption.setEnabled(inventory.getTongues() > 0);
        bowOption.setEnabled(inventory.getWagonBows() > 0);
        
        // Select the first available option
        if (inventory.getWheels() > 0) {
            wheelOption.setSelected(true);
        } else if (inventory.getAxles() > 0) {
            axleOption.setSelected(true);
        } else if (inventory.getTongues() > 0) {
            tongueOption.setSelected(true);
        } else if (inventory.getWagonBows() > 0) {
            bowOption.setSelected(true);
        }
        
        // Style the radio buttons
        wheelOption.setFont(FontManager.getWesternFont(14f));
        axleOption.setFont(FontManager.getWesternFont(14f));
        tongueOption.setFont(FontManager.getWesternFont(14f));
        bowOption.setFont(FontManager.getWesternFont(14f));
        
        wheelOption.setForeground(TEXT_COLOR);
        axleOption.setForeground(TEXT_COLOR);
        tongueOption.setForeground(TEXT_COLOR);
        bowOption.setForeground(TEXT_COLOR);
        
        wheelOption.setBackground(PANEL_COLOR);
        axleOption.setBackground(PANEL_COLOR);
        tongueOption.setBackground(PANEL_COLOR);
        bowOption.setBackground(PANEL_COLOR);
        
        // Group the radio buttons
        ButtonGroup partGroup = new ButtonGroup();
        partGroup.add(wheelOption);
        partGroup.add(axleOption);
        partGroup.add(tongueOption);
        partGroup.add(bowOption);
        
        // Add options to panel
        optionsPanel.add(wheelOption);
        optionsPanel.add(axleOption);
        optionsPanel.add(tongueOption);
        optionsPanel.add(bowOption);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton confirmButton = new JButton("Confirm");
        confirmButton.setFont(FontManager.getBoldWesternFont(14f));
        confirmButton.setForeground(TEXT_COLOR);
        confirmButton.setBackground(PANEL_COLOR);
        confirmButton.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(5, 15, 5, 15)
        ));
        
        // Disable confirm button if no parts available
        if (inventory.getWagonParts() <= 0) {
            confirmButton.setEnabled(false);
        }
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(FontManager.getBoldWesternFont(14f));
        cancelButton.setForeground(TEXT_COLOR);
        cancelButton.setBackground(PANEL_COLOR);
        cancelButton.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(5, 15, 5, 15)
        ));
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        // Add components to dialog
        partDialog.add(titleLabel, BorderLayout.NORTH);
        partDialog.add(optionsPanel, BorderLayout.CENTER);
        partDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Store result
        final String[] selectedPart = {null};
        
        // Add button actions
        confirmButton.addActionListener(e -> {
            if (wheelOption.isSelected() && wheelOption.isEnabled()) {
                selectedPart[0] = "Wheels";
            } else if (axleOption.isSelected() && axleOption.isEnabled()) {
                selectedPart[0] = "Axles";
            } else if (tongueOption.isSelected() && tongueOption.isEnabled()) {
                selectedPart[0] = "Tongues";
            } else if (bowOption.isSelected() && bowOption.isEnabled()) {
                selectedPart[0] = "Wagon Bows";
            }
            partDialog.dispose();
        });
        
        cancelButton.addActionListener(e -> {
            selectedPart[0] = null;
            partDialog.dispose();
        });
        
        // Show dialog
        partDialog.pack();
        partDialog.setSize(450, 350);
        partDialog.setLocationRelativeTo(this);
        partDialog.setResizable(false);
        partDialog.setVisible(true);
        
        // Process result
        if (selectedPart[0] == null) return; // User canceled
        
        if (selectedPart[0].equals("Wheels")) {
            sellWheels(15);
        } else if (selectedPart[0].equals("Axles")) {
            sellAxles(12);
        } else if (selectedPart[0].equals("Tongues")) {
            sellTongues(10);
        } else if (selectedPart[0].equals("Wagon Bows")) {
            sellWagonBows(7);
        }
    }
    
    private void sellWheels(int price) {
        int maxWheels = inventory.getWheels();
        if (maxWheels <= 0) {
            resultArea.setText("You don't have any wheels to sell!");
            return;
        }
        
        int quantity = getQuantity("sell wheels", maxWheels);
        if (quantity <= 0) return;
        
        int totalEarned = quantity * price;
        inventory.useWheels(quantity);
        player.addMoney(totalEarned);
        updateMoneyDisplay();
        
        resultArea.setText("You sold " + quantity + " wheel" + (quantity > 1 ? "s" : "") + " for $" + totalEarned + 
                         "\n\nHistorical Note: Wheels were the most frequently damaged part of the wagon. " +
                         "The iron tires would often loosen in dry weather and need to be reset, a job " +
                         "that required special skills and equipment.");
    }
    
    private void sellAxles(int price) {
        int maxAxles = inventory.getAxles();
        if (maxAxles <= 0) {
            resultArea.setText("You don't have any axles to sell!");
            return;
        }
        
        int quantity = getQuantity("sell axles", maxAxles);
        if (quantity <= 0) return;
        
        int totalEarned = quantity * price;
        inventory.useAxles(quantity);
        player.addMoney(totalEarned);
        updateMoneyDisplay();
        
        resultArea.setText("You sold " + quantity + " axle" + (quantity > 1 ? "s" : "") + " for $" + totalEarned + 
                         "\n\nHistorical Note: Wagon axles were made of hardwood and could break when " +
                         "wagons were overloaded or when crossing rocky terrain. Emigrants often carried " +
                         "spare axles, as a broken one could halt progress entirely.");
    }
    
    private void sellTongues(int price) {
        int maxTongues = inventory.getTongues();
        if (maxTongues <= 0) {
            resultArea.setText("You don't have any tongues to sell!");
            return;
        }
        
        int quantity = getQuantity("sell tongues", maxTongues);
        if (quantity <= 0) return;
        
        int totalEarned = quantity * price;
        inventory.useTongues(quantity);
        player.addMoney(totalEarned);
        updateMoneyDisplay();
        
        resultArea.setText("You sold " + quantity + " tongue" + (quantity > 1 ? "s" : "") + " for $" + totalEarned + 
                         "\n\nHistorical Note: The wagon tongue connected the wagon to the oxen's yoke. " +
                         "It was subject to immense strain, especially when crossing rivers or steep hills, " +
                         "making it prone to breakage.");
    }
    
    private void sellWagonBows(int price) {
        int maxBows = inventory.getWagonBows();
        if (maxBows <= 0) {
            resultArea.setText("You don't have any wagon bows to sell!");
            return;
        }
        
        int quantity = getQuantity("sell wagon bows", maxBows);
        if (quantity <= 0) return;
        
        int totalEarned = quantity * price;
        inventory.useWagonBows(quantity);
        player.addMoney(totalEarned);
        updateMoneyDisplay();
        
        resultArea.setText("You sold " + quantity + " wagon bow" + (quantity > 1 ? "s" : "") + " for $" + totalEarned + 
                         "\n\nHistorical Note: Wagon bows were the arched wooden supports for the canvas cover. " +
                         "Though less critical than wheels, axles, or tongues, damaged bows meant less protection " +
                         "from the elements and could lead to damaged supplies.");
    }
    
    private void sellMedicine(int price) {
        int maxMedicine = inventory.getMedicine();
        if (maxMedicine <= 0) {
            resultArea.setText("You don't have any medicine kits to sell!");
            return;
        }
        
        int quantity = getQuantity("sell medicine kits", maxMedicine);
        if (quantity <= 0) return;
        
        int totalEarned = quantity * price;
        inventory.useMedicine(quantity);
        player.addMoney(totalEarned);
        updateMoneyDisplay();
        
        resultArea.setText("You sold " + quantity + " medicine kits for $" + totalEarned + 
                         "\n\nHistorical Note: Medical treatments in the 1840s were primitive by modern standards. " +
                         "Common medicines included laudanum (an opium tincture), quinine for malaria, " +
                         "and various herbal remedies. Disease was the leading cause of death on the trail.");
    }
    
    private void sellAmmunition(int price) {
        int maxBoxes = inventory.getAmmunition() / 20;
        if (maxBoxes <= 0) {
            resultArea.setText("You don't have enough ammunition to sell!");
            return;
        }
        
        int quantity = getQuantity("sell boxes of ammunition", maxBoxes);
        if (quantity <= 0) return;
        
        int totalEarned = quantity * price;
        inventory.useAmmunition(quantity * 20);
        player.addMoney(totalEarned);
        updateMoneyDisplay();
        
        resultArea.setText("You sold " + quantity + " boxes of ammunition (" + 
                          (quantity * 20) + " rounds) for $" + totalEarned + 
                         "\n\nHistorical Note: Firearms were essential for hunting and protection on the trail. " +
                         "Most emigrants carried muzzle-loading rifles or muskets. Ammunition had to be carefully " +
                         "conserved, as supplies along the trail were limited.");
    }
    
    // Barter methods
    
    /**
     * Shows a styled confirmation dialog for successful trades
     */
    private void showTradeConfirmation(String title, String message) {
        // Create a styled dialog similar to other UI elements
        JDialog confirmDialog = new JDialog(this, title, true);
        confirmDialog.setLayout(new BorderLayout(10, 10));
        confirmDialog.getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Title
        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(FontManager.getBoldWesternFont(16f));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(new EmptyBorder(10, 10, 5, 10));
        
        // Message panel
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(PANEL_COLOR);
        messagePanel.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        JTextArea messageArea = new JTextArea(message);
        messageArea.setFont(FontManager.getWesternFont(14f));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setEditable(false);
        messageArea.setBackground(PANEL_COLOR);
        messageArea.setForeground(TEXT_COLOR);
        messageArea.setBorder(null);
        
        messagePanel.add(messageArea, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton okButton = new JButton("OK");
        okButton.setFont(FontManager.getBoldWesternFont(14f));
        okButton.setForeground(TEXT_COLOR);
        okButton.setBackground(PANEL_COLOR);
        okButton.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(5, 20, 5, 20)
        ));
        okButton.addActionListener(e -> confirmDialog.dispose());
        
        buttonPanel.add(okButton);
        
        // Add components to dialog
        confirmDialog.add(titleLabel, BorderLayout.NORTH);
        confirmDialog.add(messagePanel, BorderLayout.CENTER);
        confirmDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Show dialog
        confirmDialog.pack();
        confirmDialog.setSize(400, 250);
        confirmDialog.setLocationRelativeTo(this);
        confirmDialog.setResizable(false);
        confirmDialog.setVisible(true);
    }

    private void barterFoodForPart() {
        if (inventory.getFood() < 50) {
            resultArea.setText("You don't have enough food for this trade! You need at least 50 pounds.");
            resultArea.setVisible(true);
            resultArea.repaint();
            return;
        }
        
        // Random part type with equal probabilities
        Random random = new Random();
        int partType = random.nextInt(4); // 0-3 for four part types
        
        String partName;
        inventory.consumeFood(50);
        
        switch (partType) {
            case 0:
                inventory.addWheels(1);
                partName = "wagon wheel";
                break;
            case 1:
                inventory.addAxles(1);
                partName = "wagon axle";
                break;
            case 2:
                inventory.addTongues(1);
                partName = "wagon tongue";
                break;
            default:
                inventory.addWagonBows(1);
                partName = "wagon bow";
                break;
        }
        
        resultArea.setText("You traded 50 pounds of food for 1 " + partName + ".\n\nHistorical Note: Trading between parties on the trail was common. Emigrants who had excess supplies of one type would barter with others who had different goods to offer.");
        resultArea.setVisible(true);
        resultArea.repaint();
        
        // Show confirmation popup
        showTradeConfirmation("Trade Complete", 
            "You successfully traded 50 pounds of food for 1 " + partName + ".\n\n" +
            "Trading was essential for pioneers who needed to acquire specific items they lacked."
        );
    }
    
    private void barterFoodForMedicine() {
        if (inventory.getFood() < 30) {
            resultArea.setText("You don't have enough food for this trade! You need at least 30 pounds.");
            resultArea.setVisible(true);
            resultArea.repaint();
            return;
        }
        
        inventory.consumeFood(30);
        inventory.addMedicine(1);
        
        resultArea.setText("You traded 30 pounds of food for 1 medicine kit.\n\nHistorical Note: Medicine was highly valuable on the trail, as disease was a major cause of death during westward migration.");
        resultArea.setVisible(true);
        resultArea.repaint();
        
        // Show confirmation popup
        showTradeConfirmation("Trade Complete", 
            "You successfully traded 30 pounds of food for 1 medicine kit.\n\n" +
            "Medicine was one of the most valuable resources on the trail, as illness was a common threat."
        );
    }
    
    private void barterMedicineForAmmo() {
        if (inventory.getMedicine() < 2) {
            resultArea.setText("You don't have enough medicine kits for this trade! You need at least 2.");
            resultArea.setVisible(true);
            resultArea.repaint();
            return;
        }
        
        inventory.useMedicine(2);
        inventory.addAmmunition(40);
        
        resultArea.setText("You traded 2 medicine kits for 40 rounds of ammunition.\n\nHistorical Note: Ammunition was essential not just for protection but for hunting, which supplemented the limited food supplies emigrants could carry.");
        resultArea.setVisible(true);
        resultArea.repaint();
        
        // Show confirmation popup
        showTradeConfirmation("Trade Complete", 
            "You successfully traded 2 medicine kits for 40 rounds of ammunition.\n\n" +
            "Hunting was essential for supplementing food supplies on the trail, making ammunition a valuable resource."
        );
    }
} 