import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
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
    private final double priceFactor;
    
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
        
        // Top panel with title and info
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel titleLabel = new JLabel("Trading Post", JLabel.CENTER);
        titleLabel.setFont(FontManager.WESTERN_FONT_TITLE);
        titleLabel.setForeground(TEXT_COLOR);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        
        JTextArea infoText = new JTextArea(
            "Trading was an important part of pioneer life on the trail. " +
            "You can buy, sell, or barter with local traders for supplies."
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
        
        JPanel medicinePanel = createTradeButton("Medicine - $" + medicinePrice + " each", 
                                 "Buy Medicine", e -> buyMedicine(medicinePrice));
        
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
        
        JPanel medicinePanel = createTradeButton("Medicine - $" + medicineSellPrice + " each" +
                                 " (Have: " + inventory.getMedicine() + ")", 
                                 "Sell Medicine", e -> sellMedicine(medicineSellPrice));
        
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
        
        JPanel barterPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        barterPanel.setBackground(PANEL_COLOR);
        
        JLabel titleLabel = new JLabel("Barter with Travelers", JLabel.CENTER);
        titleLabel.setFont(FontManager.getBoldWesternFont(16f));
        titleLabel.setForeground(HEADER_COLOR);
        
        // Create barter options
        JPanel option1Panel = createTradeButton("Trade 50 pounds of food for 1 wagon part",
                          "Make Trade", e -> barterFoodForPart());
        
        JPanel option2Panel = createTradeButton("Trade 30 pounds of food for 1 medicine",
                          "Make Trade", e -> barterFoodForMedicine());
        
        JPanel option3Panel = createTradeButton("Trade 2 medicine for 40 rounds of ammunition",
                          "Make Trade", e -> barterMedicineForAmmo());
        
        barterPanel.add(titleLabel);
        barterPanel.add(option1Panel);
        barterPanel.add(option2Panel);
        barterPanel.add(option3Panel);
        
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
        button.addActionListener(action);
        panel.add(button, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Get quantity via dialog
     */
    private int getQuantity(String item, int maxAllowed) {
        String input = JOptionPane.showInputDialog(this, 
                                               "How many " + item + " would you like?",
                                               "Enter Quantity", 
                                               JOptionPane.QUESTION_MESSAGE);
        try {
            int quantity = Integer.parseInt(input);
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, 
                                           "Please enter a positive number.",
                                           "Invalid Input", 
                                           JOptionPane.WARNING_MESSAGE);
                return 0;
            }
            if (maxAllowed > 0 && quantity > maxAllowed) {
                JOptionPane.showMessageDialog(this, 
                                           "You can't trade more than " + maxAllowed + ".",
                                           "Too Many", 
                                           JOptionPane.WARNING_MESSAGE);
                return 0;
            }
            return quantity;
        } catch (NumberFormatException e) {
            return 0;
        }
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
        
        resultArea.setText("You purchased " + quantity + " pounds of food for $" + totalCost);
    }
    
    private void buyWagonParts(int price) {
        int quantity = getQuantity("wagon parts", -1);
        if (quantity <= 0) return;
        
        int totalCost = quantity * price;
        if (totalCost > player.getMoney()) {
            resultArea.setText("You don't have enough money for that purchase!");
            return;
        }
        
        player.spendMoney(totalCost);
        inventory.addWagonParts(quantity);
        updateMoneyDisplay();
        
        resultArea.setText("You purchased " + quantity + " wagon parts for $" + totalCost);
    }
    
    private void buyMedicine(int price) {
        int quantity = getQuantity("medicine", -1);
        if (quantity <= 0) return;
        
        int totalCost = quantity * price;
        if (totalCost > player.getMoney()) {
            resultArea.setText("You don't have enough money for that purchase!");
            return;
        }
        
        player.spendMoney(totalCost);
        inventory.addMedicine(quantity);
        updateMoneyDisplay();
        
        resultArea.setText("You purchased " + quantity + " medicine for $" + totalCost);
    }
    
    private void buyAmmunition(int price) {
        int quantity = getQuantity("boxes of ammunition", -1);
        if (quantity <= 0) return;
        
        int totalCost = quantity * price;
        if (totalCost > player.getMoney()) {
            resultArea.setText("You don't have enough money for that purchase!");
            return;
        }
        
        player.spendMoney(totalCost);
        inventory.addAmmunition(quantity * 20); // 20 rounds per box
        updateMoneyDisplay();
        
        resultArea.setText("You purchased " + quantity + " boxes of ammunition (" + 
                          (quantity * 20) + " rounds) for $" + totalCost);
    }
    
    // Sell methods
    
    private void sellFood(int price) {
        int maxFood = inventory.getFood();
        if (maxFood <= 0) {
            resultArea.setText("You don't have any food to sell!");
            return;
        }
        
        int quantity = getQuantity("pounds of food", maxFood);
        if (quantity <= 0) return;
        
        int totalEarned = quantity * price;
        inventory.consumeFood(quantity);
        player.addMoney(totalEarned);
        updateMoneyDisplay();
        
        resultArea.setText("You sold " + quantity + " pounds of food for $" + totalEarned);
    }
    
    private void sellWagonParts(int price) {
        int maxParts = inventory.getWagonParts();
        if (maxParts <= 0) {
            resultArea.setText("You don't have any wagon parts to sell!");
            return;
        }
        
        int quantity = getQuantity("wagon parts", maxParts);
        if (quantity <= 0) return;
        
        int totalEarned = quantity * price;
        inventory.useWagonParts(quantity);
        player.addMoney(totalEarned);
        updateMoneyDisplay();
        
        resultArea.setText("You sold " + quantity + " wagon parts for $" + totalEarned);
    }
    
    private void sellMedicine(int price) {
        int maxMedicine = inventory.getMedicine();
        if (maxMedicine <= 0) {
            resultArea.setText("You don't have any medicine to sell!");
            return;
        }
        
        int quantity = getQuantity("medicine", maxMedicine);
        if (quantity <= 0) return;
        
        int totalEarned = quantity * price;
        inventory.useMedicine(quantity);
        player.addMoney(totalEarned);
        updateMoneyDisplay();
        
        resultArea.setText("You sold " + quantity + " medicine for $" + totalEarned);
    }
    
    private void sellAmmunition(int price) {
        int maxBoxes = inventory.getAmmunition() / 20;
        if (maxBoxes <= 0) {
            resultArea.setText("You don't have enough ammunition to sell!");
            return;
        }
        
        int quantity = getQuantity("boxes of ammunition", maxBoxes);
        if (quantity <= 0) return;
        
        int totalEarned = quantity * price;
        inventory.useAmmunition(quantity * 20);
        player.addMoney(totalEarned);
        updateMoneyDisplay();
        
        resultArea.setText("You sold " + quantity + " boxes of ammunition (" + 
                          (quantity * 20) + " rounds) for $" + totalEarned);
    }
    
    // Barter methods
    
    private void barterFoodForPart() {
        if (inventory.getFood() < 50) {
            resultArea.setText("You don't have enough food for this trade! You need at least 50 pounds.");
            return;
        }
        
        inventory.consumeFood(50);
        inventory.addWagonParts(1);
        
        resultArea.setText("You traded 50 pounds of food for 1 wagon part.");
    }
    
    private void barterFoodForMedicine() {
        if (inventory.getFood() < 30) {
            resultArea.setText("You don't have enough food for this trade! You need at least 30 pounds.");
            return;
        }
        
        inventory.consumeFood(30);
        inventory.addMedicine(1);
        
        resultArea.setText("You traded 30 pounds of food for 1 medicine.");
    }
    
    private void barterMedicineForAmmo() {
        if (inventory.getMedicine() < 2) {
            resultArea.setText("You don't have enough medicine for this trade! You need at least 2.");
            return;
        }
        
        inventory.useMedicine(2);
        inventory.addAmmunition(40);
        
        resultArea.setText("You traded 2 medicine for 40 rounds of ammunition.");
    }
} 