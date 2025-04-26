import java.util.Scanner;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class Market {
    private final Player player;
    private final Inventory inventory;
    private static final int OXEN_PRICE = 40;
    private static final int FOOD_PRICE = 1;
    private static final int WAGON_PART_PRICE = 20;
    private static final int MEDICINE_PRICE = 15;
    private static final int AMMUNITION_PRICE = 2;
    
    // Food options with their weights (pounds per unit)
    private static final String[] FOOD_TYPES = {
        "Flour", "Bacon", "Dried Beans", "Rice", "Coffee", "Sugar", "Dried Fruit", "Hardtack"
    };
    private static final int[] FOOD_WEIGHTS = {
        25, 15, 10, 5, 2, 5, 2, 20
    };
    private static final double[] FOOD_SPOILRATE = {
            .05, .15, .02, .03, .005, .04, .1, .01
    };

    // GUI Components
    private JLabel moneyLabel;
    private JTextField[] quantityFields;
    private JLabel[] totalCostLabels;
    private JLabel[] currentInventoryLabels;
    private JLabel insufficientSuppliesLabel;

    // Colors - match EnhancedGUI
    private final Color BACKGROUND_COLOR = new Color(240, 220, 180); // Parchment/sepia
    private final Color PANEL_COLOR = new Color(200, 170, 130);      // Darker parchment
    private final Color TEXT_COLOR = new Color(80, 30, 0);           // Dark brown
    private final Color HEADER_COLOR = new Color(120, 60, 0);        // Medium brown
    private final Color ACCENT_COLOR = new Color(160, 100, 40);      // Light brown

    public Market(Player player, Inventory inventory) {
        this.player = player;
        this.inventory = inventory;
    }

    public void shop(Scanner scanner) {
        boolean isShopping = true;

        while (isShopping) {
            System.out.println("\n=== MARKET ===");
            System.out.println("Money remaining: $" + player.getMoney());
            System.out.println("What would you like to buy?");
            System.out.println("1. Oxen ($40 each)");
            System.out.println("2. Food ($1 per pound)");
            System.out.println("3. Wagon parts ($20 each)");
            System.out.println("4. Medicine ($15 each)");
            System.out.println("5. Ammunition ($2 per box of 20 rounds)");
            System.out.println("6. Review purchases");
            System.out.println("7. Finish shopping");

            int userChoice;
            try {
                userChoice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                userChoice = 0;
            }

            switch (userChoice) {
                case 1:
                    buyOxen(scanner);
                    break;
                case 2:
                    buyFood(scanner);
                    break;
                case 3:
                    buyWagonParts(scanner);
                    break;
                case 4:
                    buyMedicine(scanner);
                    break;
                case 5:
                    buyAmmunition(scanner);
                    break;
                case 6:
                    inventory.displayInventory();
                    break;
                case 7:
                    if (canStartJourney()) {
                        isShopping = false;
                    } else {
                        System.out.println("\nYou don't have enough supplies to start your journey!");
                        System.out.println("You need at least:");
                        System.out.println("- 2 oxen");
                        System.out.println("- 200 pounds of food");
                        System.out.println("- 3 wagon parts");
                        System.out.println("- 2 medicine");
                    }
                    break;
                default:
                    System.out.println("Please enter a number between 1 and 7.");
            }
        }

        System.out.println("\nYou have completed your shopping.");
        System.out.println("Money remaining: $" + player.getMoney());
        System.out.println("Press Enter to begin your journey...");
        scanner.nextLine();
    }

    private void buyOxen(Scanner scanner) {
        System.out.println("\n=== BUY OXEN ===");
        System.out.println("Oxen are needed to pull your wagon. You should have at least 2.");
        System.out.println("Each ox costs $40");
        System.out.println("You currently have: " + inventory.getOxen() + " oxen");
        System.out.println("How many would you like to buy?");
        int oxenAmount = getValidAmount(scanner);
        int totalCost = oxenAmount * OXEN_PRICE;
        if (player.getJob() == Job.MERCHANT) {
            totalCost = (int) Math.round(totalCost * 0.85); // 15% off
        }
        if (totalCost > player.getMoney()) {
            System.out.println("You don't have enough money for that!");
        } else {
            player.spendMoney(totalCost);
            inventory.addOxen(oxenAmount);
            System.out.println("You bought " + oxenAmount + " oxen for $" + totalCost);
        }
    }

    private void buyFood(Scanner scanner) {
        System.out.println("\n=== BUY FOOD ===");
        System.out.println("Food is needed to feed your family. Each person consumes about 2 pounds per day.");
        System.out.println("Food costs $1 per pound.");
        System.out.println("You currently have: " + inventory.getFood() + " pounds of food");
        System.out.println("How many pounds would you like to buy?");
        int foodAmount = getValidAmount(scanner);
        int totalFoodCost = foodAmount * FOOD_PRICE;
        if (player.getJob() == Job.MERCHANT) {
            totalFoodCost = (int) Math.round(totalFoodCost * 0.85); // 15% off
        }
        if (totalFoodCost > player.getMoney()) {
            System.out.println("You don't have enough money for that!");
        } else {
            player.spendMoney(totalFoodCost);
            inventory.addFood(foodAmount);
            System.out.println("You bought " + foodAmount + " pounds of food for $" + totalFoodCost);
        }
    }

    private void buyWagonParts(Scanner scanner) {
        System.out.println("\n=== BUY WAGON PARTS ===");
        System.out.println("Wagon parts are needed for repairs along the trail.");
        System.out.println("Parts cost $20 each.");
        System.out.println("You currently have: " + inventory.getWagonParts() + " parts");
        System.out.println("How many would you like to buy?");
        int partsAmount = getValidAmount(scanner);
        int totalWagonCost = partsAmount * WAGON_PART_PRICE;
        if (player.getJob() == Job.MERCHANT) {
            totalWagonCost = (int) Math.round(totalWagonCost * 0.85); // 15% off
        }
        if (totalWagonCost > player.getMoney()) {
            System.out.println("You don't have enough money for that!");
        } else {
            player.spendMoney(totalWagonCost);
            inventory.addWagonParts(partsAmount);
            System.out.println("You bought " + partsAmount + " wagon parts for $" + totalWagonCost);
        }
    }

    private void buyMedicine(Scanner scanner) {
        System.out.println("\n=== BUY MEDICINE ===");
        System.out.println("Medicine can help cure diseases and injuries along the trail.");
        System.out.println("Medicine costs $15 per unit.");
        System.out.println("You currently have: " + inventory.getMedicine() + " medicine");
        System.out.println("How many would you like to buy?");
        int medicineAmount = getValidAmount(scanner);
        int totalMedicineCost = medicineAmount * MEDICINE_PRICE;
        if (player.getJob() == Job.MERCHANT) {
            totalMedicineCost = (int) Math.round(totalMedicineCost * 0.85); // 15% off
        }
        if (totalMedicineCost > player.getMoney()) {
            System.out.println("You don't have enough money for that!");
        } else {
            player.spendMoney(totalMedicineCost);
            inventory.addMedicine(medicineAmount);
            System.out.println("You bought " + medicineAmount + " medicine for $" + totalMedicineCost);
        }
    }

    private void buyAmmunition(Scanner scanner) {
        System.out.println("\n=== BUY AMMUNITION ===");
        System.out.println("Ammunition is needed for hunting and protection.");
        System.out.println("Ammunition costs $2 per box (20 rounds).");
        System.out.println("You currently have: " + inventory.getAmmunition() + " rounds");
        System.out.println("How many boxes would you like to buy?");
        int boxesAmount = getValidAmount(scanner);
        int totalAmmoCost = boxesAmount * AMMUNITION_PRICE;
        if (player.getJob() == Job.MERCHANT) {
            totalAmmoCost = (int) Math.round(totalAmmoCost * 0.85); // 15% off
        }
        if (totalAmmoCost > player.getMoney()) {
            System.out.println("You don't have enough money for that!");
        } else {
            player.spendMoney(totalAmmoCost);
            inventory.addAmmunition(boxesAmount * 20);
            System.out.println("You bought " + boxesAmount + " boxes of ammunition for $" + totalAmmoCost);
        }
    }

    private int getValidAmount(Scanner scanner) {
        int amount = 0;
        boolean isValid = false;

        while (!isValid) {
            try {
                amount = Integer.parseInt(scanner.nextLine());
                if (amount < 0) {
                    System.out.println("Please enter a positive number.");
                } else {
                    isValid = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        return amount;
    }

    private boolean canStartJourney() {
        return inventory.getOxen() >= 2 && inventory.getFood() >= 200 && inventory.getWagonParts() >= 3 && inventory.getMedicine() >= 2;
    }

    /**
     * Creates a market panel for the GUI
     */
    public JPanel createMarketPanel() {
        // Initialize GUI components
        quantityFields = new JTextField[5];
        totalCostLabels = new JLabel[5];
        currentInventoryLabels = new JLabel[5];
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title and money label
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel titleLabel = new JLabel("General Store - Purchase Supplies");
        titleLabel.setFont(FontManager.WESTERN_FONT_TITLE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(TEXT_COLOR);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        
        moneyLabel = new JLabel("Available Funds: $" + player.getMoney(), SwingConstants.CENTER);
        moneyLabel.setFont(FontManager.getBoldWesternFont(16));
        moneyLabel.setForeground(TEXT_COLOR);
        topPanel.add(moneyLabel, BorderLayout.CENTER);
        
        JTextArea introText = new JTextArea(
            "Before departing, you'll need to purchase supplies for your journey. " +
            "Choose wisely, as your survival depends on having adequate provisions. " +
            "You should aim to have at least 2 oxen, 200 pounds of food, 3 wagon parts, and 2 medicine."
        );
        introText.setFont(FontManager.getWesternFont(14));
        introText.setForeground(TEXT_COLOR);
        introText.setBackground(PANEL_COLOR);
        introText.setLineWrap(true);
        introText.setWrapStyleWord(true);
        introText.setEditable(false);
        introText.setBorder(new EmptyBorder(10, 10, 10, 10));
        topPanel.add(introText, BorderLayout.SOUTH);
        
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Create shopping panel with GridBagLayout for items
        JPanel shoppingPanel = new JPanel(new GridBagLayout());
        shoppingPanel.setBackground(PANEL_COLOR);
        shoppingPanel.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Column headers
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        addStyledLabel(shoppingPanel, "Item", gbc, true);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        addStyledLabel(shoppingPanel, "Price", gbc, true);
        
        gbc.gridx = 2;
        addStyledLabel(shoppingPanel, "Quantity", gbc, true);
        
        gbc.gridx = 3;
        addStyledLabel(shoppingPanel, "Total Cost", gbc, true);
        
        gbc.gridx = 4;
        addStyledLabel(shoppingPanel, "Current", gbc, true);
        
        gbc.gridx = 5;
        addStyledLabel(shoppingPanel, "Buy", gbc, true);
        
        // Add item rows
        String[] items = {"Oxen", "Food (pounds)", "Wagon Parts", "Medicine", "Ammunition (boxes)"};
        int[] prices = {OXEN_PRICE, FOOD_PRICE, WAGON_PART_PRICE, MEDICINE_PRICE, AMMUNITION_PRICE};
        
        for (int i = 0; i < items.length; i++) {
            gbc.gridy = i + 1;
            
            // Item name
            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.WEST;
            addStyledLabel(shoppingPanel, items[i], gbc, false);
            
            // Price
            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.CENTER;
            addStyledLabel(shoppingPanel, "$" + prices[i], gbc, false);
            
            // Quantity field
            gbc.gridx = 2;
            quantityFields[i] = new JTextField("0", 5);
            quantityFields[i].setFont(FontManager.getWesternFont(14));
            quantityFields[i].setHorizontalAlignment(SwingConstants.CENTER);
            
            final int itemIndex = i;
            quantityFields[i].addActionListener(e -> updateTotalCost(itemIndex));
            quantityFields[i].getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    updateTotalCost(itemIndex);
                }
                
                @Override
                public void removeUpdate(DocumentEvent e) {
                    updateTotalCost(itemIndex);
                }
                
                @Override
                public void changedUpdate(DocumentEvent e) {
                    updateTotalCost(itemIndex);
                }
            });
            
            shoppingPanel.add(quantityFields[i], gbc);
            
            // Total cost label
            gbc.gridx = 3;
            totalCostLabels[i] = new JLabel("$0", SwingConstants.CENTER);
            totalCostLabels[i].setFont(FontManager.getWesternFont(14));
            totalCostLabels[i].setForeground(TEXT_COLOR);
            shoppingPanel.add(totalCostLabels[i], gbc);
            
            // Current inventory
            gbc.gridx = 4;
            currentInventoryLabels[i] = new JLabel(getCurrentInventory(i), SwingConstants.CENTER);
            currentInventoryLabels[i].setFont(FontManager.getWesternFont(14));
            currentInventoryLabels[i].setForeground(TEXT_COLOR);
            shoppingPanel.add(currentInventoryLabels[i], gbc);
            
            // Buy button
            gbc.gridx = 5;
            JButton buyButton = createStyledButton("Buy");
            buyButton.addActionListener(e -> buyItem(itemIndex));
            
            shoppingPanel.add(buyButton, gbc);
        }
        
        // Add insufficient supplies warning label (initially invisible)
        gbc.gridx = 0;
        gbc.gridy = items.length + 1;
        gbc.gridwidth = 6;
        insufficientSuppliesLabel = new JLabel(
            "You need at least 2 oxen, 200 pounds of food, 3 wagon parts, and 2 medicine.", 
            SwingConstants.CENTER
        );
        insufficientSuppliesLabel.setFont(FontManager.getBoldWesternFont(14));
        insufficientSuppliesLabel.setForeground(Color.RED);
        insufficientSuppliesLabel.setVisible(false);
        shoppingPanel.add(insufficientSuppliesLabel, gbc);
        
        mainPanel.add(new JScrollPane(shoppingPanel), BorderLayout.CENTER);
        
        // Bottom button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton finishButton = createStyledButton("Begin Journey");
        finishButton.addActionListener(e -> {
            if (canStartJourney()) {
                Window win = SwingUtilities.getWindowAncestor(mainPanel);
                if (win instanceof JDialog) {
                    win.dispose();
                }
            } else {
                insufficientSuppliesLabel.setVisible(true);
            }
        });
        
        buttonPanel.add(finishButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    /**
     * Updates the total cost label when quantity changes
     */
    private void updateTotalCost(int itemIndex) {
        try {
            int quantity = Integer.parseInt(quantityFields[itemIndex].getText());
            int price = getItemPrice(itemIndex);
            int total = quantity * price;
            totalCostLabels[itemIndex].setText("$" + total);
        } catch (NumberFormatException e) {
            totalCostLabels[itemIndex].setText("$0");
        }
    }
    
    /**
     * Gets the price for an item based on index
     */
    private int getItemPrice(int index) {
        switch (index) {
            case 0: return OXEN_PRICE;
            case 1: return FOOD_PRICE;
            case 2: return WAGON_PART_PRICE;
            case 3: return MEDICINE_PRICE;
            case 4: return AMMUNITION_PRICE;
            default: return 0;
        }
    }
    
    /**
     * Gets current inventory amount based on index
     */
    private String getCurrentInventory(int index) {
        switch (index) {
            case 0: return String.valueOf(inventory.getOxen());
            case 1: return String.valueOf(inventory.getFood());
            case 2: return String.valueOf(inventory.getWagonParts());
            case 3: return String.valueOf(inventory.getMedicine());
            case 4: return String.valueOf(inventory.getAmmunition());
            default: return "0";
        }
    }
    
    /**
     * Buy an item and update inventory and money
     */
    private void buyItem(int itemIndex) {
        try {
            int quantity = Integer.parseInt(quantityFields[itemIndex].getText());
            if (quantity <= 0) return;
            
            int price = getItemPrice(itemIndex);

            if (player.getJob() == Job.MERCHANT) {
                price = (int) Math.round(price * 0.85); // 15% discount for buying
            }

            int totalCost = quantity * price;
            
            if (totalCost > player.getMoney()) {
                JOptionPane.showMessageDialog(null, 
                    "You don't have enough money for that purchase!",
                    "Insufficient Funds", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // If buying food, show a dialog to select food types
            if (itemIndex == 1) { // Food index is 1
                int totalFoodPounds = showFoodSelectionDialog(quantity);
                if (totalFoodPounds <= 0) {
                    return; // User canceled the selection
                }
                
                // Only deduct money and add food if the user completed the selection
                player.spendMoney(totalCost);
                inventory.addFood(totalFoodPounds);
            } else {
                // Deduct money for non-food items
                player.spendMoney(totalCost);
                
                // Update inventory based on item type
                switch (itemIndex) {
                    case 0: inventory.addOxen(quantity); break;
                    case 2: inventory.addWagonParts(quantity); break;
                    case 3: inventory.addMedicine(quantity); break;
                    case 4: inventory.addAmmunition(quantity * 20); break; // 20 rounds per box
                }
            }
            
            // Update displays
            moneyLabel.setText("Available Funds: $" + player.getMoney());
            currentInventoryLabels[itemIndex].setText(getCurrentInventory(itemIndex));
            quantityFields[itemIndex].setText("0");
            totalCostLabels[itemIndex].setText("$0");
            
            // Check if they now have enough supplies
            if (canStartJourney()) {
                insufficientSuppliesLabel.setVisible(false);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, 
                "Please enter a valid number for quantity.",
                "Invalid Input", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Shows a dialog for selecting different types of food
     * @param totalPoundsToBuy The total pounds of food to purchase
     * @return The actual total pounds purchased, or 0 if canceled
     */
    private int showFoodSelectionDialog(int totalPoundsToBuy) {
        // Create a dialog for food selection
        Window parentWindow = SwingUtilities.getWindowAncestor(moneyLabel);
        JDialog foodDialog;
        
        if (parentWindow instanceof Frame) {
            foodDialog = new JDialog((Frame) parentWindow, "Food Selection", true);
        } else if (parentWindow instanceof Dialog) {
            foodDialog = new JDialog((Dialog) parentWindow, "Food Selection", true);
        } else {
            foodDialog = new JDialog();
            foodDialog.setTitle("Food Selection");
            foodDialog.setModal(true);
        }
        
        foodDialog.setLayout(new BorderLayout(10, 10));
        foodDialog.getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Create a panel for the food options
        JPanel foodPanel = new JPanel(new GridBagLayout());
        foodPanel.setBackground(PANEL_COLOR);
        foodPanel.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Create a description label
        JLabel descLabel = new JLabel("Select food types to purchase (" + totalPoundsToBuy + " pounds total):");
        descLabel.setFont(FontManager.getBoldWesternFont(14));
        descLabel.setForeground(TEXT_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(5, 5, 15, 5);
        gbc.anchor = GridBagConstraints.WEST;
        foodPanel.add(descLabel, gbc);
        
        // Column headers
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0;
        JLabel foodTypeHeader = new JLabel("Food Type");
        foodTypeHeader.setFont(FontManager.getBoldWesternFont(14));
        foodTypeHeader.setForeground(HEADER_COLOR);
        foodPanel.add(foodTypeHeader, gbc);
        
        gbc.gridx = 1;
        JLabel weightHeader = new JLabel("Weight per Unit");
        weightHeader.setFont(FontManager.getBoldWesternFont(14));
        weightHeader.setForeground(HEADER_COLOR);
        foodPanel.add(weightHeader, gbc);
        
        gbc.gridx = 2;
        JLabel quantityHeader = new JLabel("Quantity");
        quantityHeader.setFont(FontManager.getBoldWesternFont(14));
        quantityHeader.setForeground(HEADER_COLOR);
        foodPanel.add(quantityHeader, gbc);
        
        // Create an array of spinners for each food type
        JSpinner[] foodSpinners = new JSpinner[FOOD_TYPES.length];
        
        for (int i = 0; i < FOOD_TYPES.length; i++) {
            gbc.gridy = i + 2;
            
            // Food type
            gbc.gridx = 0;
            JLabel foodLabel = new JLabel(FOOD_TYPES[i]);
            foodLabel.setFont(FontManager.getWesternFont(14));
            foodLabel.setForeground(TEXT_COLOR);
            foodPanel.add(foodLabel, gbc);
            
            // Weight per unit
            gbc.gridx = 1;
            JLabel weightLabel = new JLabel(FOOD_WEIGHTS[i] + " lbs");
            weightLabel.setFont(FontManager.getWesternFont(14));
            weightLabel.setForeground(TEXT_COLOR);
            foodPanel.add(weightLabel, gbc);
            
            // Quantity spinner
            gbc.gridx = 2;
            SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 100, 1);
            foodSpinners[i] = new JSpinner(model);
            foodSpinners[i].setFont(FontManager.getWesternFont(14));
            foodPanel.add(foodSpinners[i], gbc);
        }
        
        // Create a status panel with bold borders to make it more visible
        JPanel statusPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        statusPanel.setBackground(PANEL_COLOR);
        statusPanel.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel allocatedLabel = new JLabel("Allocated: 0 / " + totalPoundsToBuy + " pounds");
        allocatedLabel.setFont(FontManager.getBoldWesternFont(14));
        allocatedLabel.setForeground(TEXT_COLOR);
        allocatedLabel.setBorder(new EmptyBorder(5, 0, 5, 0));
        
        JLabel remainingLabel = new JLabel("Remaining to allocate: " + totalPoundsToBuy + " pounds");
        remainingLabel.setFont(FontManager.getBoldWesternFont(14));
        remainingLabel.setForeground(TEXT_COLOR);
        remainingLabel.setBorder(new EmptyBorder(5, 0, 5, 0));
        
        statusPanel.add(allocatedLabel);
        statusPanel.add(remainingLabel);
        
        // Add the status panel to the food panel
        gbc.gridx = 0;
        gbc.gridy = FOOD_TYPES.length + 2;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 5, 5, 5);
        foodPanel.add(statusPanel, gbc);
        
        // Create a button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton confirmButton = createStyledButton("Confirm Purchase");
        JButton cancelButton = createStyledButton("Cancel");
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);
        
        // Add a listener to update the total allocated when spinners change
        for (int i = 0; i < foodSpinners.length; i++) {
            final int index = i;
            foodSpinners[i].addChangeListener(e -> {
                int totalAllocated = 0;
                for (int j = 0; j < foodSpinners.length; j++) {
                    int spinnerValue = (int) foodSpinners[j].getValue();
                    totalAllocated += spinnerValue * FOOD_WEIGHTS[j];
                }
                int remaining = totalPoundsToBuy - totalAllocated;
                
                allocatedLabel.setText("Allocated: " + totalAllocated + " / " + totalPoundsToBuy + " pounds");
                remainingLabel.setText("Remaining to allocate: " + remaining + " pounds");
                
                if (remaining < 0) {
                    remainingLabel.setForeground(Color.RED);
                } else {
                    remainingLabel.setForeground(TEXT_COLOR);
                }
                
                // Enable confirm button if the allocation is valid
                // Now we allow fewer pounds than requested, but not more
                confirmButton.setEnabled(totalAllocated > 0 && totalAllocated <= totalPoundsToBuy);
            });
        }
        
        // Add action listeners to buttons
        final int[] result = {0}; // To store the result (total pounds purchased)

        confirmButton.addActionListener(e -> {
            int totalAllocated = 0;
            StringBuilder purchaseSummary = new StringBuilder("You purchased:\n");

            for (int i = 0; i < foodSpinners.length; i++) {
                int quantity = (int) foodSpinners[i].getValue();
                if (quantity > 0) {
                    int pounds = quantity * FOOD_WEIGHTS[i];
                    totalAllocated += pounds;
                    purchaseSummary.append("- ")
                            .append(quantity)
                            .append(" units of ")
                            .append(FOOD_TYPES[i])
                            .append(" (")
                            .append(pounds)
                            .append(" lbs)\n");
                    double spoilRate = FOOD_SPOILRATE[i];
                    if(player.getJob().equals("Farmer")){
                         spoilRate *= 3;
                    }
                    Item item = new Item(FOOD_TYPES[i], pounds, spoilRate);
                    inventory.addItem(item);
                }
            }

            // Optional: show summary popup or continue to next screen
            JOptionPane.showMessageDialog(null, purchaseSummary.toString(), "Purchase Summary", JOptionPane.INFORMATION_MESSAGE);
        });


        cancelButton.addActionListener(e -> {
            result[0] = 0;
            foodDialog.dispose();
        });
        
        // Initially disable the confirm button until a valid amount is allocated
        confirmButton.setEnabled(false);
        
        // Add components to the dialog
        JScrollPane scrollPane = new JScrollPane(foodPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(PANEL_COLOR);
        
        foodDialog.add(scrollPane, BorderLayout.CENTER);
        foodDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Set dialog properties
        foodDialog.setSize(500, 600);
        foodDialog.setLocationRelativeTo(parentWindow);
        foodDialog.setVisible(true);
        
        // Return the total pounds purchased, or 0 if canceled
        return result[0];
    }
    
    /**
     * Add a styled label to the panel
     */
    private void addStyledLabel(JPanel panel, String text, GridBagConstraints gbc, boolean isHeader) {
        JLabel label = new JLabel(text);
        label.setFont(isHeader ? 
            FontManager.getBoldWesternFont(14) : 
            FontManager.getWesternFont(14));
        label.setForeground(isHeader ? HEADER_COLOR : TEXT_COLOR);
        panel.add(label, gbc);
    }
    
    /**
     * Create a styled button
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FontManager.getBoldWesternFont(14));
        button.setForeground(TEXT_COLOR);
        button.setBackground(PANEL_COLOR);
        button.setFocusPainted(false);
        button.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(5, 10, 5, 10)
        ));
        return button;
    }
    
    /**
     * Custom dialog for displaying food purchase information
     */
    private class FoodPurchaseDialog extends JDialog {
        private boolean isWarning;
        
        public FoodPurchaseDialog(Window owner, String purchaseSummary, String title) {
            super(owner, title, ModalityType.APPLICATION_MODAL);
            
            // Determine if this is a warning dialog based on the title
            this.isWarning = title.contains("Too Much") || title.contains("No Food");
            
            initUI(purchaseSummary);
            setSize(450, 350); // Reduced size since we removed the icon
            setLocationRelativeTo(owner);
            setResizable(false);
        }
        
        private void initUI(String purchaseSummary) {
            setLayout(new BorderLayout(10, 10));
            getContentPane().setBackground(BACKGROUND_COLOR);
            
            // Title panel
            JPanel titlePanel = new JPanel(new BorderLayout());
            titlePanel.setBackground(BACKGROUND_COLOR);
            titlePanel.setBorder(new EmptyBorder(10, 10, 5, 10));
            
            JLabel titleLabel = new JLabel(isWarning ? getTitle() : "Purchase Complete", JLabel.CENTER);
            titleLabel.setFont(FontManager.WESTERN_FONT_TITLE);
            titleLabel.setForeground(isWarning ? new Color(150, 50, 0) : TEXT_COLOR);
            titlePanel.add(titleLabel, BorderLayout.CENTER);
            
            add(titlePanel, BorderLayout.NORTH);
            
            // Content panel
            JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
            contentPanel.setBackground(PANEL_COLOR);
            contentPanel.setBorder(new CompoundBorder(
                new LineBorder(ACCENT_COLOR, 2),
                new EmptyBorder(20, 20, 20, 20)
            ));
            
            // Summary text - using JTextPane for center alignment
            JTextPane summaryPane = new JTextPane();
            summaryPane.setText(purchaseSummary);
            summaryPane.setFont(FontManager.getWesternFont(14));
            summaryPane.setEditable(false);
            summaryPane.setBackground(PANEL_COLOR);
            summaryPane.setForeground(TEXT_COLOR);
            summaryPane.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add padding around the text
            
            // Center align the text
            StyledDocument doc = summaryPane.getStyledDocument();
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
            doc.setParagraphAttributes(0, doc.getLength(), center, false);
            
            JScrollPane scrollPane = new JScrollPane(summaryPane);
            scrollPane.setBorder(null);
            scrollPane.getViewport().setBackground(PANEL_COLOR);
            
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            
            add(contentPanel, BorderLayout.CENTER);
            
            // Button panel
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.setBackground(BACKGROUND_COLOR);
            buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
            
            JButton closeButton = new JButton(isWarning ? "Back" : "Continue");
            closeButton.setFont(FontManager.getBoldWesternFont(14));
            closeButton.setForeground(TEXT_COLOR);
            closeButton.setBackground(PANEL_COLOR);
            closeButton.setBorder(new CompoundBorder(
                new LineBorder(ACCENT_COLOR, 2),
                new EmptyBorder(8, 25, 8, 25)
            ));
            
            closeButton.addActionListener(e -> dispose());
            
            buttonPanel.add(closeButton);
            add(buttonPanel, BorderLayout.SOUTH);
        }
    }
}