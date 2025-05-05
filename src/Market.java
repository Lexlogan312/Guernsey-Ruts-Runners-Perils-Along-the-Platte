/**
 * Market Class of the Perils Along the Platte Game
 * Manages the trading system and resource management interface.
 * Handles the purchase of supplies, including food, oxen, wagon parts, medicine, and ammunition.
 * Provides a graphical user interface for trading and tracks inventory capacity and costs.
 *
 * @author Alex Randall and Chase McCluskey
 * @version 1.0
 * @date 05/06/2025
 * @file Market.java
 */
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

    // Price constants
    private static final int OXEN_PRICE = 40;
    private static final int FOOD_PRICE = 1;
    private static final int WHEEL_PRICE = 20;
    private static final int AXLE_PRICE = 20;
    private static final int TONGUE_PRICE = 15;
    private static final int WAGON_BOW_PRICE = 10;
    private static final int MEDICINE_PRICE = 15;
    private static final int AMMUNITION_PRICE = 10;

    // Weight constants
    private static final int MEDICINE_WEIGHT = 5;
    private static final int AMMO_WEIGHT = 3;
    private static final int WHEEL_WEIGHT = 50;
    private static final int AXLE_WEIGHT = 40;
    private static final int TONGUE_WEIGHT = 30;
    private static final int WAGON_BOW_WEIGHT = 10;

    // Available food types for purchase
    private static final String[] FOOD_TYPES = {
            "Flour", "Bacon", "Dried Beans", "Rice", "Coffee", "Sugar", "Dried Fruit", "Hardtack"
    };
    
    // Weight per unit of each food type in pounds
    private static final int[] FOOD_WEIGHTS = {
            25, 15, 10, 5, 2, 5, 2, 20
    };

    // Spoilage rate for each food type (0.0 to 1.0)
    private static final double[] FOOD_SPOILRATE = {
            .05, .15, .02, .03, .005, .04, .1, .01
    };

    // Label displaying available funds
    private JLabel moneyLabel;

    // Label displaying current and maximum weight capacity
    private JLabel weightCapacityLabel;
    
    // Text fields for entering purchase quantities
    private JTextField[] quantityFields;
    
    // Labels displaying total cost for each item
    private JLabel[] totalCostLabels;
    
    // Labels displaying current inventory for each item
    private JLabel[] currentInventoryLabels;
    
    // Label for displaying insufficient supplies warning
    private JLabel insufficientSuppliesLabel;

    private final Color BACKGROUND_COLOR = new Color(240, 220, 180); // Parchment/sepia background
    private final Color PANEL_COLOR = new Color(200, 170, 130);      // Darker parchment for panels
    private final Color TEXT_COLOR = new Color(80, 30, 0);           // Dark brown for text
    private final Color ACCENT_COLOR = new Color(160, 100, 40);      // Light brown for borders
    private final Color HEADER_COLOR = new Color(120, 60, 0);        // Darker brown for headers

    /**
     * Constructs a new Market with the specified player and inventory.
     * Initializes the trading interface and validates starting conditions.
     * 
     * @param player The player who will be making purchases (must not be null)
     * @param inventory The inventory to be updated with purchases (must not be null)
     */
    public Market(Player player, Inventory inventory) {
        this.player = player;
        this.inventory = inventory;
    }

    /**
     * Checks if the player has the minimum required supplies to start the journey.
     * Validates:
     * - Oxen count
     * - Food quantity
     * - Wagon parts
     * - Medicine kits
     * 
     * @return true if the player has:
     *         - At least 2 oxen
     *         - At least 200 pounds of food
     *         - At least 3 wagon parts
     *         - At least 2 medicine kits
     */
    private boolean canStartJourney() {
        return inventory.getOxen() >= 2 &&
                inventory.getFood() >= 200 &&
                inventory.getWagonParts() >= 3 &&
                inventory.getMedicine() >= 2;
    }

    /**
     * Creates the main market panel with all trading options.
     * Builds the user interface including:
     * - Title and introduction
     * - Available funds and weight capacity
     * - Item purchase interface
     * - Current inventory display
     * - Buy buttons
     * 
     * @return A JPanel containing the complete market interface
     */
    public JPanel createMarketPanel() {
        quantityFields = new JTextField[5];
        totalCostLabels = new JLabel[5];
        currentInventoryLabels = new JLabel[5];

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Market");
        titleLabel.setFont(FontManager.WESTERN_FONT_TITLE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(TEXT_COLOR);
        topPanel.add(titleLabel, BorderLayout.NORTH);

        JTextArea introText = new JTextArea(
                "Before departing, you'll need to purchase supplies for your journey. " +
                        "Choose wisely, as your survival depends on having adequate supplies. " +
                        "You should aim to have at least 2 oxen, 200 pounds of food, 3 wagon parts, and 2 medicine kits, but more is recommended.\n\n" +
                        "When you have purchased enough supplies, click 'Begin Journey' to start your adventure."
        );
        introText.setFont(FontManager.getWesternFont(14));
        introText.setForeground(TEXT_COLOR);
        introText.setBackground(PANEL_COLOR);
        introText.setWrapStyleWord(true);
        introText.setLineWrap(true);
        introText.setEditable(false);
        introText.setMargin(new Insets(5, 5, 5, 5));
        introText.setOpaque(true);
        topPanel.add(introText, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel shoppingPanel = new JPanel(new GridBagLayout());
        shoppingPanel.setBackground(PANEL_COLOR);
        shoppingPanel.setBorder(new CompoundBorder(
                new LineBorder(ACCENT_COLOR, 2),
                new EmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add funds and weight info at the top
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 6;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 5));
        infoPanel.setBackground(PANEL_COLOR);
        
        moneyLabel = new JLabel("Available Funds: $" + player.getMoney(), SwingConstants.CENTER);
        moneyLabel.setFont(FontManager.getBoldWesternFont(16));
        moneyLabel.setForeground(TEXT_COLOR);
        infoPanel.add(moneyLabel);

        weightCapacityLabel = new JLabel("Weight: " + inventory.getCurrentWeight() + "/" + inventory.getMaxWeightCapacity() + " lbs", SwingConstants.CENTER);
        weightCapacityLabel.setFont(FontManager.getBoldWesternFont(16));
        weightCapacityLabel.setForeground(TEXT_COLOR);
        infoPanel.add(weightCapacityLabel);
        
        shoppingPanel.add(infoPanel, gbc);

        // Add column headers
        gbc.gridy = 1;
        gbc.gridwidth = 1;
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

        // Modified items array to consolidate wagon parts
        String[] items = {
                "Food (pounds)",
                "Oxen",
                "Wagon Parts",  // Consolidated wagon parts
                "Medicine Kits",
                "Ammunition (20 rounds)"
        };

        // Modified price display to show range for wagon parts
        String[] priceDisplay = {
                "$" + FOOD_PRICE,
                "$" + OXEN_PRICE,
                "$10-$20",  // Price range for wagon parts
                "$" + MEDICINE_PRICE,
                "$" + AMMUNITION_PRICE
        };

        // Average price for wagon parts calculation
        int wagonPartsAvgPrice = (WHEEL_PRICE + AXLE_PRICE + TONGUE_PRICE + WAGON_BOW_PRICE) / 4;

        for (int i = 0; i < items.length; i++) {
            gbc.gridy = i + 2;

            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.WEST;
            addStyledLabel(shoppingPanel, items[i], gbc, false);

            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.CENTER;
            addStyledLabel(shoppingPanel, priceDisplay[i], gbc, false);

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

            gbc.gridx = 3;
            totalCostLabels[i] = new JLabel("$0", SwingConstants.CENTER);
            totalCostLabels[i].setFont(FontManager.getWesternFont(14));
            totalCostLabels[i].setForeground(TEXT_COLOR);
            shoppingPanel.add(totalCostLabels[i], gbc);

            gbc.gridx = 4;
            currentInventoryLabels[i] = new JLabel(getCurrentInventory(i), SwingConstants.CENTER);
            currentInventoryLabels[i].setFont(FontManager.getWesternFont(14));
            currentInventoryLabels[i].setForeground(TEXT_COLOR);
            shoppingPanel.add(currentInventoryLabels[i], gbc);

            gbc.gridx = 5;
            JButton buyButton = createStyledButton("Buy");
            buyButton.addActionListener(e -> buyItem(itemIndex));

            shoppingPanel.add(buyButton, gbc);
        }

        gbc.gridx = 0;
        gbc.gridy = items.length + 1;
        gbc.gridwidth = 6;
        insufficientSuppliesLabel = new JLabel(
                "You need at least 2 oxen, 200 pounds of food, 3 wagon parts, and 2 medicine kits.",
                SwingConstants.CENTER
        );
        insufficientSuppliesLabel.setFont(FontManager.getBoldWesternFont(14));
        insufficientSuppliesLabel.setVisible(false);
        shoppingPanel.add(insufficientSuppliesLabel, gbc);

        mainPanel.add(new JScrollPane(shoppingPanel), BorderLayout.CENTER);

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

        updatePricesForJob();
        return mainPanel;
    }

    /**
     * Updates the total cost display for a specific item.
     * 
     * @param itemIndex The index of the item to update
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
     * Gets the price of an item based on its index.
     * 
     * @param index The index of the item
     * @return The price of the item in dollars
     */
    private int getItemPrice(int index) {
        switch (index) {
            case 0: return FOOD_PRICE;        // Food
            case 1: return OXEN_PRICE;        // Oxen
            case 2: return (WHEEL_PRICE + AXLE_PRICE + TONGUE_PRICE + WAGON_BOW_PRICE) / 4; // Average wagon part price
            case 3: return MEDICINE_PRICE;    // Medicine
            case 4: return AMMUNITION_PRICE;  // Ammunition
            default: return 0;
        }
    }

    /**
     * Gets the current inventory count for an item.
     * 
     * @param index The index of the item
     * @return A string representation of the current inventory
     */
    private String getCurrentInventory(int index) {
        switch (index) {
            case 0: return String.valueOf(inventory.getFood());
            case 1: return String.valueOf(inventory.getOxen());
            case 2: return String.valueOf(inventory.getWagonParts()); // Total wagon parts
            case 3: return String.valueOf(inventory.getMedicine());
            case 4: return String.valueOf(inventory.getAmmunition());
            default: return "0";
        }
    }

    /**
     * Processes the purchase of an item.
     * 
     * @param itemIndex The index of the item to purchase
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

            // For wagon parts, we don't calculate weight here since the dialog will handle it
            int weight;
            if (itemIndex == 2) { // Wagon parts
                // Use an estimated average weight to check capacity, actual weight will be calculated after parts are selected
                int avgPartWeight = (WHEEL_WEIGHT + AXLE_WEIGHT + TONGUE_WEIGHT + WAGON_BOW_WEIGHT) / 4;
                weight = quantity * avgPartWeight;
            } else {
                weight = calculateItemWeight(itemIndex, quantity);
            }

            if (totalCost > player.getMoney()) {
                new WagonPartsPurchaseDialog(
                    SwingUtilities.getWindowAncestor(moneyLabel),
                    "You don't have enough money for that purchase!",
                    "Insufficient Funds"
                ).setVisible(true);
                return;
            }

            // Check if the wagon has enough capacity for this purchase
            if (!inventory.hasWeightCapacity(weight)) {
                new WagonPartsPurchaseDialog(
                    SwingUtilities.getWindowAncestor(moneyLabel),
                    "Your wagon cannot carry that much additional weight!\n" +
                    "Current load: " + inventory.getCurrentWeight() + "/" + inventory.getMaxWeightCapacity() + " lbs\n" +
                    "Additional weight: " + weight + " lbs",
                    "Wagon Overloaded"
                ).setVisible(true);
                return;
            }

            // If buying food, show a dialog to select food types
            if (itemIndex == 0) { // Food index is 0
                int totalFoodPounds = showFoodSelectionDialog(quantity);
                if (totalFoodPounds <= 0) {
                    return; // User canceled the selection
                }

                // Only deduct money and add food if the user completed the selection
                player.spendMoney(totalCost);
                inventory.addFood(totalFoodPounds);
            }
            // If buying wagon parts, show a dialog to select part types
            else if (itemIndex == 2) { // Wagon parts index is 2
                boolean partsSelected = showWagonPartsSelectionDialog(quantity, totalCost);
                if (!partsSelected) {
                    return; // User canceled the selection
                }
                // Money is deducted in the dialog itself when parts are selected
            }
            else {
                // Deduct money for non-food items
                player.spendMoney(totalCost);

                // Update inventory based on item type
                switch (itemIndex) {
                    case 1: inventory.addOxen(quantity); break;
                    case 3: inventory.addMedicine(quantity); break;
                    case 4: inventory.addAmmunition(quantity * 20); break; // 20 rounds per box
                }
            }

            // Update displays
            moneyLabel.setText("Available Funds: $" + player.getMoney());
            weightCapacityLabel.setText("Weight: " + inventory.getCurrentWeight() + "/" + inventory.getMaxWeightCapacity() + " lbs");
            currentInventoryLabels[itemIndex].setText(getCurrentInventory(itemIndex));
            quantityFields[itemIndex].setText("0");
            totalCostLabels[itemIndex].setText("$0");

            // Check if they now have enough supplies
            if (canStartJourney()) {
                insufficientSuppliesLabel.setVisible(false);
            }

        } catch (NumberFormatException e) {
            new WagonPartsPurchaseDialog(
                SwingUtilities.getWindowAncestor(moneyLabel),
                "Please enter a valid number for quantity.",
                "Invalid Input"
            ).setVisible(true);
        }
    }

    /**
     * Applies job-specific price adjustments to the base price.
     * 
     * @param basePrice The original price before adjustments
     * @return The adjusted price after applying job bonuses
     */
    private int applyJobPriceAdjustment(int basePrice) {
        // Apply Merchant discount if applicable
        if (player.getJob() == Job.MERCHANT) {
            // 5-10% discount for merchants
            double discount = 0.05 + (Math.random() * 0.05);
            return (int)(basePrice * (1.0 - discount));
        }
        return basePrice;
    }
    
    /**
     * Updates all displayed prices based on the player's job.
     */
    private void updatePricesForJob() {
        // Apply merchant discount to all displayed prices if applicable
        if (player.getJob() == Job.MERCHANT) {
            for (JLabel priceLabel : totalCostLabels) {
                if (priceLabel != null && priceLabel.getText().startsWith("$")) {
                    try {
                        String priceText = priceLabel.getText().substring(1);
                        int originalPrice = Integer.parseInt(priceText);
                        int discountedPrice = applyJobPriceAdjustment(originalPrice);
                        priceLabel.setText("$" + discountedPrice);
                    } catch (NumberFormatException e) {
                        // Skip if not a valid number
                    }
                }
            }
        }
    }

    /**
     * Calculates the weight of a specific quantity of an item.
     * 
     * @param itemIndex The index of the item
     * @param quantity The quantity to calculate weight for
     * @return The total weight in pounds
     */
    private int calculateItemWeight(int itemIndex, int quantity) {
        switch (itemIndex) {
            case 0: return quantity; // Food - 1 pound per pound
            case 1: return 0; // Oxen don't count toward weight
            case 2: // Wagon parts - use average weight for initial estimation
                int avgWeight = (WHEEL_WEIGHT + AXLE_WEIGHT + TONGUE_WEIGHT + WAGON_BOW_WEIGHT) / 4;
                return quantity * avgWeight;
            case 3: return quantity * MEDICINE_WEIGHT; // Medicine
            case 4: return quantity * AMMO_WEIGHT; // Ammunition boxes
            default: return 0;
        }
    }

    /**
     * Shows a dialog for selecting food types and quantities.
     * 
     * @param totalPoundsToBuy The total pounds of food to distribute
     * @return The total cost of the food purchase
     */
    private int showFoodSelectionDialog(int totalPoundsToBuy) {
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

        JPanel foodPanel = new JPanel(new GridBagLayout());
        foodPanel.setBackground(PANEL_COLOR);
        foodPanel.setBorder(new CompoundBorder(
                new LineBorder(ACCENT_COLOR, 2),
                new EmptyBorder(15, 15, 15, 15)
        ));

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

        JSpinner[] foodSpinners = new JSpinner[FOOD_TYPES.length];

        for (int i = 0; i < FOOD_TYPES.length; i++) {
            gbc.gridy = i + 2;

            gbc.gridx = 0;
            JLabel foodLabel = new JLabel(FOOD_TYPES[i]);
            foodLabel.setFont(FontManager.getWesternFont(14));
            foodLabel.setForeground(TEXT_COLOR);
            foodPanel.add(foodLabel, gbc);

            gbc.gridx = 1;
            JLabel weightLabel = new JLabel(FOOD_WEIGHTS[i] + " lbs");
            weightLabel.setFont(FontManager.getWesternFont(14));
            weightLabel.setForeground(TEXT_COLOR);
            foodPanel.add(weightLabel, gbc);

            gbc.gridx = 2;
            SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 100, 1);
            foodSpinners[i] = new JSpinner(model);
            foodSpinners[i].setFont(FontManager.getWesternFont(14));
            foodPanel.add(foodSpinners[i], gbc);
        }

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

        gbc.gridx = 0;
        gbc.gridy = FOOD_TYPES.length + 2;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 5, 5, 5);
        foodPanel.add(statusPanel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton confirmButton = createStyledButton("Confirm Purchase");
        JButton cancelButton = createStyledButton("Cancel");

        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);

        for (int i = 0; i < foodSpinners.length; i++) {
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

                confirmButton.setEnabled(totalAllocated > 0 && totalAllocated <= totalPoundsToBuy);
            });
        }

        final int[] result = {0};

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
                    if(player.getJob() == Job.FARMER){
                         spoilRate *= 3;
                    }
                    Item item = new Item(FOOD_TYPES[i], FOOD_WEIGHTS[i], spoilRate);
                    inventory.addItem(item);
                }
            }
          
            int refundPounds = totalPoundsToBuy - totalAllocated;
            if (refundPounds > 0) {
                int refundAmount = refundPounds * FOOD_PRICE;
                player.addMoney(refundAmount);
                purchaseSummary.append("\nRefund for unused pounds: $").append(refundAmount);
            }

            if (totalAllocated <= totalPoundsToBuy && totalAllocated > 0) {
                result[0] = totalAllocated;
                new FoodPurchaseDialog(foodDialog, purchaseSummary.toString(), "Purchase Complete").setVisible(true);
                foodDialog.dispose();
            }
        });

        cancelButton.addActionListener(e -> {
            result[0] = 0;
            foodDialog.dispose();
        });

        confirmButton.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(foodPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(PANEL_COLOR);

        foodDialog.add(scrollPane, BorderLayout.CENTER);
        foodDialog.add(buttonPanel, BorderLayout.SOUTH);

        foodDialog.setSize(500, 600);
        foodDialog.setLocationRelativeTo(parentWindow);
        foodDialog.setVisible(true);

        return result[0];
    }

    /**
     * Shows a dialog for selecting wagon parts.
     * 
     * @param totalPartsToBuy The total number of parts to distribute
     * @param totalCost The total cost of the parts
     * @return true if the purchase was completed, false if cancelled
     */
    private boolean showWagonPartsSelectionDialog(int totalPartsToBuy, int totalCost) {
        Window parentWindow = SwingUtilities.getWindowAncestor(moneyLabel);
        JDialog partsDialog;

        if (parentWindow instanceof Frame) {
            partsDialog = new JDialog((Frame) parentWindow, "Wagon Parts Selection", true);
        } else if (parentWindow instanceof Dialog) {
            partsDialog = new JDialog((Dialog) parentWindow, "Wagon Parts Selection", true);
        } else {
            partsDialog = new JDialog();
            partsDialog.setTitle("Wagon Parts Selection");
            partsDialog.setModal(true);
        }

        partsDialog.setLayout(new BorderLayout(10, 10));
        partsDialog.getContentPane().setBackground(BACKGROUND_COLOR);

        JPanel partsPanel = new JPanel(new GridBagLayout());
        partsPanel.setBackground(PANEL_COLOR);
        partsPanel.setBorder(new CompoundBorder(
                new LineBorder(ACCENT_COLOR, 2),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel descLabel = new JLabel("Select wagon parts to purchase (" + totalPartsToBuy + " parts total):");
        descLabel.setFont(FontManager.getBoldWesternFont(14));
        descLabel.setForeground(TEXT_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(5, 5, 15, 5);
        gbc.anchor = GridBagConstraints.WEST;
        partsPanel.add(descLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        JLabel partTypeHeader = new JLabel("Part Type");
        partTypeHeader.setFont(FontManager.getBoldWesternFont(14));
        partTypeHeader.setForeground(HEADER_COLOR);
        partsPanel.add(partTypeHeader, gbc);

        gbc.gridx = 1;
        JLabel weightHeader = new JLabel("Weight");
        weightHeader.setFont(FontManager.getBoldWesternFont(14));
        weightHeader.setForeground(HEADER_COLOR);
        partsPanel.add(weightHeader, gbc);

        gbc.gridx = 2;
        JLabel priceHeader = new JLabel("Price");
        priceHeader.setFont(FontManager.getBoldWesternFont(14));
        priceHeader.setForeground(HEADER_COLOR);
        partsPanel.add(priceHeader, gbc);

        gbc.gridx = 3;
        JLabel quantityHeader = new JLabel("Quantity");
        quantityHeader.setFont(FontManager.getBoldWesternFont(14));
        quantityHeader.setForeground(HEADER_COLOR);
        partsPanel.add(quantityHeader, gbc);

        String[] partTypes = {"Wheels", "Axles", "Tongues", "Wagon Bows"};
        int[] partWeights = {WHEEL_WEIGHT, AXLE_WEIGHT, TONGUE_WEIGHT, WAGON_BOW_WEIGHT};
        int[] partPrices = {WHEEL_PRICE, AXLE_PRICE, TONGUE_PRICE, WAGON_BOW_PRICE};

        JSpinner[] partSpinners = new JSpinner[partTypes.length];

        for (int i = 0; i < partTypes.length; i++) {
            gbc.gridy = i + 2;

            gbc.gridx = 0;
            JLabel partLabel = new JLabel(partTypes[i]);
            partLabel.setFont(FontManager.getWesternFont(14));
            partLabel.setForeground(TEXT_COLOR);
            partsPanel.add(partLabel, gbc);

            gbc.gridx = 1;
            JLabel weightLabel = new JLabel(partWeights[i] + " lbs");
            weightLabel.setFont(FontManager.getWesternFont(14));
            weightLabel.setForeground(TEXT_COLOR);
            partsPanel.add(weightLabel, gbc);

            gbc.gridx = 2;
            JLabel priceLabel = new JLabel("$" + partPrices[i]);
            priceLabel.setFont(FontManager.getWesternFont(14));
            priceLabel.setForeground(TEXT_COLOR);
            partsPanel.add(priceLabel, gbc);

            gbc.gridx = 3;
            SpinnerNumberModel model = new SpinnerNumberModel(0, 0, totalPartsToBuy, 1);
            partSpinners[i] = new JSpinner(model);
            partSpinners[i].setFont(FontManager.getWesternFont(14));
            partsPanel.add(partSpinners[i], gbc);
        }

        JPanel statusPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        statusPanel.setBackground(PANEL_COLOR);
        statusPanel.setBorder(new CompoundBorder(
                new LineBorder(ACCENT_COLOR, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel allocatedLabel = new JLabel("Allocated: 0 / " + totalPartsToBuy + " parts");
        allocatedLabel.setFont(FontManager.getBoldWesternFont(14));
        allocatedLabel.setForeground(TEXT_COLOR);
        allocatedLabel.setBorder(new EmptyBorder(5, 0, 5, 0));

        JLabel totalCostLabel = new JLabel("Total cost: $0");
        totalCostLabel.setFont(FontManager.getBoldWesternFont(14));
        totalCostLabel.setForeground(TEXT_COLOR);
        totalCostLabel.setBorder(new EmptyBorder(5, 0, 5, 0));

        JLabel remainingLabel = new JLabel("Remaining to allocate: " + totalPartsToBuy + " parts");
        remainingLabel.setFont(FontManager.getBoldWesternFont(14));
        remainingLabel.setForeground(TEXT_COLOR);
        remainingLabel.setBorder(new EmptyBorder(5, 0, 5, 0));

        statusPanel.add(allocatedLabel);
        statusPanel.add(totalCostLabel);
        statusPanel.add(remainingLabel);

        gbc.gridx = 0;
        gbc.gridy = partTypes.length + 2;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 5, 5, 5);
        partsPanel.add(statusPanel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton confirmButton = createStyledButton("Confirm Purchase");
        JButton cancelButton = createStyledButton("Cancel");

        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);

        for (int i = 0; i < partSpinners.length; i++) {
            final int index = i;
            partSpinners[i].addChangeListener(e -> {
                int totalAllocated = 0;
                int actualTotalCost = 0;

                for (int j = 0; j < partSpinners.length; j++) {
                    int spinnerValue = (int) partSpinners[j].getValue();
                    totalAllocated += spinnerValue;
                    actualTotalCost += spinnerValue * partPrices[j]; // Calculate actual cost based on parts selected
                }

                int remaining = totalPartsToBuy - totalAllocated;

                allocatedLabel.setText("Allocated: " + totalAllocated + " / " + totalPartsToBuy + " parts");
                totalCostLabel.setText("Total cost: $" + actualTotalCost);
                remainingLabel.setText("Remaining to allocate: " + remaining + " parts");

                if (remaining < 0) {
                    remainingLabel.setForeground(Color.RED);
                } else {
                    remainingLabel.setForeground(TEXT_COLOR);
                }

                confirmButton.setEnabled(totalAllocated > 0 && totalAllocated <= totalPartsToBuy);
            });
        }

        final boolean[] result = {false};

        confirmButton.addActionListener(e -> {
            int totalAllocated = 0;
            StringBuilder purchaseSummary = new StringBuilder("You purchased:\n");
            int totalWeight = 0;
            int actualTotalCost = 0;

            int wheels = (int) partSpinners[0].getValue();
            int axles = (int) partSpinners[1].getValue();
            int tongues = (int) partSpinners[2].getValue();
            int wagonBows = (int) partSpinners[3].getValue();

            if (wheels > 0) {
                totalAllocated += wheels;
                int partWeight = wheels * WHEEL_WEIGHT;
                int partCost = wheels * WHEEL_PRICE;
                actualTotalCost += partCost;
                totalWeight += partWeight;
                purchaseSummary.append("- ")
                        .append(wheels)
                        .append(" Wheels (")
                        .append(partWeight)
                        .append(" lbs) - $")
                        .append(partCost)
                        .append("\n");
            }

            if (axles > 0) {
                totalAllocated += axles;
                int partWeight = axles * AXLE_WEIGHT;
                int partCost = axles * AXLE_PRICE;
                actualTotalCost += partCost;
                totalWeight += partWeight;
                purchaseSummary.append("- ")
                        .append(axles)
                        .append(" Axles (")
                        .append(partWeight)
                        .append(" lbs) - $")
                        .append(partCost)
                        .append("\n");
            }

            if (tongues > 0) {
                totalAllocated += tongues;
                int partWeight = tongues * TONGUE_WEIGHT;
                int partCost = tongues * TONGUE_PRICE;
                actualTotalCost += partCost;
                totalWeight += partWeight;
                purchaseSummary.append("- ")
                        .append(tongues)
                        .append(" Tongues (")
                        .append(partWeight)
                        .append(" lbs) - $")
                        .append(partCost)
                        .append("\n");
            }

            if (wagonBows > 0) {
                totalAllocated += wagonBows;
                int partWeight = wagonBows * WAGON_BOW_WEIGHT;
                int partCost = wagonBows * WAGON_BOW_PRICE;
                actualTotalCost += partCost;
                totalWeight += partWeight;
                purchaseSummary.append("- ")
                        .append(wagonBows)
                        .append(" Wagon Bows (")
                        .append(partWeight)
                        .append(" lbs) - $")
                        .append(partCost)
                        .append("\n");
            }

            if (totalAllocated <= totalPartsToBuy && totalAllocated > 0) {
                // Deduct money based on actual parts selected
                player.spendMoney(actualTotalCost);

                // Add the parts to inventory
                inventory.addWheels(wheels);
                inventory.addAxles(axles);
                inventory.addTongues(tongues);
                inventory.addWagonBows(wagonBows);

                result[0] = true;

                // Display purchase summary
                purchaseSummary.append("\nTotal cost: $").append(actualTotalCost);
                purchaseSummary.append("\nTotal weight: ").append(totalWeight).append(" lbs");
                new WagonPartsPurchaseDialog(partsDialog, purchaseSummary.toString(), "Purchase Complete").setVisible(true);
                partsDialog.dispose();
            }
        });

        cancelButton.addActionListener(e -> {
            result[0] = false;
            partsDialog.dispose();
        });

        confirmButton.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(partsPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(PANEL_COLOR);

        partsDialog.add(scrollPane, BorderLayout.CENTER);
        partsDialog.add(buttonPanel, BorderLayout.SOUTH);

        partsDialog.setSize(500, 500);
        partsDialog.setLocationRelativeTo(parentWindow);
        partsDialog.setVisible(true);

        return result[0];
    }

    /**
     * Adds a styled label to a panel.
     * 
     * @param panel The panel to add the label to
     * @param text The text to display
     * @param gbc The GridBagConstraints for layout
     * @param isHeader Whether the label is a header
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
     * Creates a styled button with consistent appearance.
     * 
     * @param text The button text
     * @return A styled JButton
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
     * Dialog for purchasing food items.
     */
    private class FoodPurchaseDialog extends JDialog {
        private final boolean isWarning;

        public FoodPurchaseDialog(Window owner, String purchaseSummary, String title) {
            super(owner, title, ModalityType.APPLICATION_MODAL);

            this.isWarning = title.contains("Too Much") || title.contains("No Food");

            initUI(purchaseSummary);
            setSize(450, 400);
            setLocationRelativeTo(owner);
            setResizable(false);
        }

        private void initUI(String purchaseSummary) {
            setLayout(new BorderLayout(10, 10));
            getContentPane().setBackground(BACKGROUND_COLOR);

            JPanel titlePanel = new JPanel(new BorderLayout());
            titlePanel.setBackground(BACKGROUND_COLOR);
            titlePanel.setBorder(new EmptyBorder(10, 10, 5, 10));

            JLabel titleLabel = new JLabel(isWarning ? getTitle() : "Purchase Complete", JLabel.CENTER);
            titleLabel.setFont(FontManager.WESTERN_FONT_TITLE);
            titleLabel.setForeground(isWarning ? new Color(150, 50, 0) : TEXT_COLOR);
            titlePanel.add(titleLabel, BorderLayout.CENTER);

            add(titlePanel, BorderLayout.NORTH);

            JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
            contentPanel.setBackground(PANEL_COLOR);
            contentPanel.setBorder(new CompoundBorder(
                    new LineBorder(ACCENT_COLOR, 2),
                    new EmptyBorder(20, 20, 20, 20)
            ));

            JTextPane summaryPane = new JTextPane();
            summaryPane.setText(purchaseSummary);
            summaryPane.setFont(FontManager.getWesternFont(14));
            summaryPane.setEditable(false);
            summaryPane.setBackground(PANEL_COLOR);
            summaryPane.setForeground(TEXT_COLOR);
            summaryPane.setBorder(new EmptyBorder(10, 10, 10, 10));

            StyledDocument doc = summaryPane.getStyledDocument();
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
            doc.setParagraphAttributes(0, doc.getLength(), center, false);

            JScrollPane scrollPane = new JScrollPane(summaryPane);
            scrollPane.setBorder(null);
            scrollPane.getViewport().setBackground(PANEL_COLOR);

            contentPanel.add(scrollPane, BorderLayout.CENTER);

            add(contentPanel, BorderLayout.CENTER);

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

    /**
     * Dialog for purchasing wagon parts.
     */
    private class WagonPartsPurchaseDialog extends JDialog {
        private final boolean isWarning;

        public WagonPartsPurchaseDialog(Window owner, String purchaseSummary, String title) {
            super(owner, title, ModalityType.APPLICATION_MODAL);

            this.isWarning = title.contains("Warning");

            initUI(purchaseSummary);
            setSize(450, 400);
            setLocationRelativeTo(owner);
            setResizable(false);
        }

        private void initUI(String purchaseSummary) {
            setLayout(new BorderLayout(10, 10));
            getContentPane().setBackground(BACKGROUND_COLOR);

            JPanel titlePanel = new JPanel(new BorderLayout());
            titlePanel.setBackground(BACKGROUND_COLOR);
            titlePanel.setBorder(new EmptyBorder(10, 10, 5, 10));

            JLabel titleLabel = new JLabel(isWarning ? getTitle() : "Purchase Complete", JLabel.CENTER);
            titleLabel.setFont(FontManager.WESTERN_FONT_TITLE);
            titleLabel.setForeground(isWarning ? new Color(150, 50, 0) : TEXT_COLOR);
            titlePanel.add(titleLabel, BorderLayout.CENTER);

            add(titlePanel, BorderLayout.NORTH);

            JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
            contentPanel.setBackground(PANEL_COLOR);
            contentPanel.setBorder(new CompoundBorder(
                    new LineBorder(ACCENT_COLOR, 2),
                    new EmptyBorder(20, 20, 20, 20)
            ));

            JTextPane summaryPane = new JTextPane();
            summaryPane.setText(purchaseSummary);
            summaryPane.setFont(FontManager.getWesternFont(14));
            summaryPane.setEditable(false);
            summaryPane.setBackground(PANEL_COLOR);
            summaryPane.setForeground(TEXT_COLOR);
            summaryPane.setBorder(new EmptyBorder(10, 10, 10, 10));

            StyledDocument doc = summaryPane.getStyledDocument();
            SimpleAttributeSet center = new SimpleAttributeSet();
            StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
            doc.setParagraphAttributes(0, doc.getLength(), center, false);

            JScrollPane scrollPane = new JScrollPane(summaryPane);
            scrollPane.setBorder(null);
            scrollPane.getViewport().setBackground(PANEL_COLOR);

            contentPanel.add(scrollPane, BorderLayout.CENTER);

            add(contentPanel, BorderLayout.CENTER);

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