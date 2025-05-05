import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

/**
 * RiverCrossingDialog Class of the Perils Along the Platte Game
 * 
 * A dialog that presents river crossing options to the player, including:
 * - Ford the river (wade across)
 * - Caulk the wagon and float across
 * - Use a ferry (if available)
 * - Wait for better conditions
 * 
 * @author Alex Randall and Chase McCluskey
 * @version 1.0
 * @date 05/06/2025
 * @file RiverCrossingDialog.java
 */
public class RiverCrossingDialog extends JDialog {
    private final Player player;
    private final Inventory inventory;
    private final Weather weather;
    private final Random random = new Random();
    private final Consumer<String> notifier; // To send messages back to GameController

    // River name to display in the dialog
    private String riverName = "River Crossing";

    // River characteristics
    private int depth;
    private final int width;

    // GUI components
    private JPanel optionsPanel; // To disable buttons

    // Colors - match EnhancedGUI
    private final Color BACKGROUND_COLOR = new Color(240, 220, 180); // Parchment/sepia
    private final Color PANEL_COLOR = new Color(200, 170, 130);      // Darker parchment
    private final Color TEXT_COLOR = new Color(80, 30, 0);           // Dark brown
    private final Color HEADER_COLOR = new Color(120, 60, 0);        // Medium brown
    private final Color ACCENT_COLOR = new Color(160, 100, 40);      // Light brown

    /**
     * Constructs a new RiverCrossingDialog.
     * Initializes the dialog with:
     * - River characteristics (depth and width)
     * - Weather effects on river conditions
     * - Available crossing options
     * - Risk assessment information
     * 
     * @param owner The parent frame (main game window)
     * @param player The player character
     * @param inventory The game's inventory system
     * @param weather The current weather conditions
     * @param notifier Function to send messages back to GameController
     */
    public RiverCrossingDialog(Frame owner, Player player, Inventory inventory, Weather weather, Consumer<String> notifier) {
        super(owner, "River Crossing", true); // Modal dialog
        this.player = player;
        this.inventory = inventory;
        this.weather = weather;
        this.notifier = notifier;

        // Generate river characteristics
        depth = 2 + random.nextInt(19); // 2-20 feet deep
        width = 50 + random.nextInt(301); // 50-350 feet wide

        // Adjust difficulty based on weather
        if (weather != null && (weather.getCurrentWeather().contains("Rain") ||
                weather.getCurrentWeather().contains("Snow") ||
                weather.getCurrentWeather().contains("Storm"))) {
            depth += (2 + random.nextInt(4)); // Increase depth by 2-5 feet in bad weather
        }

        initUI();
        pack();
        setSize(600, 500);
        setLocationRelativeTo(owner);
        setResizable(false);
    }

    /**
     * Initializes the dialog's user interface.
     * Creates and arranges all UI components including:
     * - Title panel with river name
     * - River information display
     * - Crossing options panel
     * - Option buttons with descriptions
     * - Weather effects display
     */
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Title panel with river name
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);
        titlePanel.setBorder(new EmptyBorder(10, 10, 5, 10));

        JLabel titleLabel = new JLabel(riverName, JLabel.CENTER);
        titleLabel.setFont(FontManager.WESTERN_FONT_TITLE);
        titleLabel.setForeground(TEXT_COLOR);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        add(titlePanel, BorderLayout.NORTH);

        // Top panel with title and river information
        JPanel topPanel = new JPanel(new BorderLayout(0, 10));
        topPanel.setBackground(BACKGROUND_COLOR);
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextArea riverInfoText = new JTextArea(
                "You've come to a river that is " + width + " feet wide and " + depth + " feet deep.\n" +
                        (weather != null && (weather.getCurrentWeather().contains("Rain") || weather.getCurrentWeather().contains("Snow")) ?
                                "The recent precipitation has made the river higher and faster than usual.\n" : "") +
                        "You need to decide how to cross."
        );
        riverInfoText.setFont(FontManager.getWesternFont(14f));
        riverInfoText.setLineWrap(true);
        riverInfoText.setWrapStyleWord(true);
        riverInfoText.setEditable(false);
        riverInfoText.setBackground(PANEL_COLOR);
        riverInfoText.setForeground(TEXT_COLOR);
        riverInfoText.setBorder(new CompoundBorder(
                new LineBorder(ACCENT_COLOR, 1),
                new EmptyBorder(10, 10, 10, 10))
        );
        topPanel.add(new JScrollPane(riverInfoText), BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Options panel
        optionsPanel = new JPanel(new GridLayout(4, 1, 10, 15)); // Increased vertical gap from 10 to 15
        optionsPanel.setBackground(PANEL_COLOR);
        optionsPanel.setBorder(new CompoundBorder(
                new LineBorder(ACCENT_COLOR, 2),
                new EmptyBorder(20, 15, 20, 15) // Increased padding from 15 to 20
        ));

        // Option buttons with direct ActionListener implementations
        JButton fordButton = createOptionButton("Ford the river (wade across)",
                "Attempt to walk the wagon through the river. Risky if deep.",
                e -> {
                    fordRiver(); // Performs logic and sends notifications
                    disableOptions();
                    dispose(); // Close dialog after action
                });

        JButton caulkButton = createOptionButton("Caulk the wagon and float across",
                "Seal the wagon and float it. Better for deeper rivers, but takes time.",
                e -> {
                    caulkAndFloat(); // Performs logic and sends notifications
                    disableOptions();
                    dispose(); // Close dialog after action
                });

        JButton ferryButton = createOptionButton("Use a ferry ($10)",
                "Pay for a safe crossing if available and affordable.",
                e -> {
                    useFerry(); // Performs logic, sends notifications, and may dispose or re-enable options
                });

        JButton waitButton = createOptionButton("Wait a day and see if conditions improve",
                "Delay your journey but possibly get better crossing conditions.",
                e -> {
                    waitForBetterConditions(); // Performs logic, sends notifications, and disposes
                });

        optionsPanel.add(fordButton);
        optionsPanel.add(caulkButton);
        optionsPanel.add(ferryButton);
        optionsPanel.add(waitButton);

        add(optionsPanel, BorderLayout.CENTER);
    }

    /**
     * Creates an option button with formatted title and description.
     * 
     * @param title The button's main text
     * @param description The detailed description of the option
     * @param action The action to perform when the button is clicked
     * @return A formatted JButton with the specified properties
     */
    private JButton createOptionButton(String title, String description, ActionListener action) {
        String htmlText = "<html><div style='text-align: center; padding: 5px;'>" +
                "<b style='font-size: 13px;'>" + title + "</b><br>" + // Slightly smaller font
                "<span style='font-size: 11px;'>" + description + "</span>" + // Slightly smaller font
                "</div></html>";

        JButton button = new JButton(htmlText);
        button.setFont(FontManager.getWesternFont(13f));
        button.setBackground(PANEL_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setBorder(new CompoundBorder(
                new LineBorder(ACCENT_COLOR, 2),
                new EmptyBorder(10, 15, 10, 15) // Increased padding from 5,10,5,10 to 10,15,10,15
        ));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(action);
        return button;
    }

    /**
     * Sends a notification message to the GameController.
     * 
     * @param message The message to send
     */
    private void notify(String message) {
        if (notifier != null) {
            notifier.accept(message);
        } else {
            System.out.println("Notification (RiverDialog): " + message); // Fallback
        }
    }

    /**
     * Attempts to ford the river by walking the wagon through.
     * 
     * Success depends on:
     * - River depth (2-20 feet)
     * - Oxen health and number
     * - Weather conditions
     * 
     * Possible outcomes:
     * - Success: Cross safely, possible minor food loss
     * - Failure: Wagon stuck/overturned, significant resource loss
     * - Critical failure: Possible drowning in deep water
     * 
     * Resource impacts:
     * - Food: 10-30 lbs (success) or 50-150 lbs (failure)
     * - Wagon parts: 0-1 parts (failure)
     * - Medicine: 0-1 kits (failure)
     * - Oxen health: 10-25% decrease (failure)
     * - Player health: 10-25% decrease (failure)
     */
    private void fordRiver() {
        List<String> messages = new ArrayList<>(); // Collect messages
        messages.add("Attempting to ford the river...");

        // Calculate successChance
        double successChance;
        if (depth <= 3) successChance = 0.95;
        else if (depth <= 6) successChance = 0.70;
        else if (depth <= 10) successChance = 0.40;
        else successChance = 0.15;
        if (inventory != null) {
            successChance *= Math.max(0.5, inventory.getOxenHealth() / 100.0);
            successChance *= Math.min(1.0, inventory.getOxen() / 3.0);
        }

        if (Math.random() < successChance) {
            messages.add("Success! You safely forded the river.");
            if (Math.random() < 0.15) {
                int foodLost = 10 + random.nextInt(21);
                if (inventory != null) inventory.consumeFood(foodLost);
                messages.add("However, some supplies got wet. Lost " + foodLost + " lbs of food.");
            }
        } else {
            messages.add("Disaster! The wagon got stuck or overturned!");
            int foodLost = 50 + random.nextInt(101);
            int partsLost = random.nextInt(2);
            int medicineLost = random.nextInt(2);
            if (inventory != null) {
                inventory.consumeFood(foodLost);
                inventory.useWagonParts(partsLost);
                inventory.useMedicine(medicineLost);
                inventory.decreaseOxenHealth(10 + random.nextInt(16));
            }
            messages.add("Lost: " + foodLost + " lbs food" +
                    (partsLost > 0 ? ", " + partsLost + " wagon part(s)" : "") +
                    (medicineLost > 0 ? ", " + medicineLost + " medicine kit" + (medicineLost > 1 ? "s" : "") : "") +
                    ". Oxen health decreased.");
            if (player != null && Math.random() < 0.4) {
                int healthLost = 10 + random.nextInt(16);
                player.decreaseHealth(healthLost);
                messages.add("Someone was injured! Lost " + healthLost + " health.");
                
                // Check for drowning death (5% chance in deep water)
                if (depth > 8 && Math.random() < 0.05) {
                    player.decreaseHealth(player.getHealth(), "drowning"); // Direct cause and ensure death
                    messages.add("Tragedy strikes! Someone in your party drowned in the river.");
                }
            }
        }
        // Send collected messages
        notify(String.join("\n", messages));
    }

    /**
     * Attempts to caulk the wagon and float across the river.
     * 
     * Success depends on:
     * - River width (50-350 feet)
     * - Wagon condition
     * - Weather conditions
     * 
     * Possible outcomes:
     * - Success: Cross safely, minor resource loss
     * - Failure: Wagon damaged, significant resource loss
     * 
     * Resource impacts:
     * - Food: 5-15 lbs (success) or 30-80 lbs (failure)
     * - Wagon parts: 0-2 parts (failure)
     * - Medicine: 0-1 kits (failure)
     * - Oxen health: 5-15% decrease (failure)
     */
    private void caulkAndFloat() {
        List<String> messages = new ArrayList<>();
        messages.add("Sealing the wagon with pitch to float across...");

        // ... (calculate successChance as before) ...
        double successChance;
        if (depth > 10) successChance = 0.85;
        else if (depth > 5) successChance = 0.75;
        else successChance = 0.50;
        if (weather != null && (weather.getCurrentWeather().contains("Rain") || weather.getCurrentWeather().contains("Storm"))) {
            successChance -= 0.25;
            messages.add("The rough water makes floating treacherous.");
        }


        if (Math.random() < successChance) {
            messages.add("Success! You floated across without major incident.");
        } else {
            messages.add("The wagon took on water! Some supplies damaged!");
            int foodLost = 30 + random.nextInt(51);
            int ammoLost = 0;
            if (Math.random() < 0.3) {
                ammoLost = 10 + random.nextInt(21);
            }
            if (inventory != null) {
                inventory.consumeFood(foodLost);
                if (ammoLost > 0) inventory.useAmmunition(ammoLost);
                if (Math.random() < 0.1) {
                    inventory.useWagonParts(1);
                    messages.add("The water also damaged a wagon part.");
                }
            }
            messages.add("Lost " + foodLost + " lbs of food" +
                    (ammoLost > 0 ? " and " + ammoLost + " rounds of ammo." : "."));
                    
            // Check for potential drowning (8% chance if stormy, 3% otherwise)
            if (player != null) {
                double drowningChance = (weather != null && 
                    (weather.getCurrentWeather().contains("Rain") || 
                     weather.getCurrentWeather().contains("Storm"))) ? 0.08 : 0.03;
                     
                if (Math.random() < drowningChance) {
                    // Severe health impact with possibility of death
                    int healthLost = 30 + random.nextInt(30); // 30-60 health impact
                    
                    if (player.getHealth() <= healthLost) {
                        // If this damage would kill the player, provide specific cause
                        player.decreaseHealth(healthLost, "drowning");
                        messages.add("Tragedy! Your wagon capsized in the river and someone drowned.");
                    } else {
                        player.decreaseHealth(healthLost);
                        messages.add("Nearly drowned! Lost " + healthLost + " health points.");
                    }
                }
            }
        }
        // Send collected messages
        notify(String.join("\n", messages));
    }

    /**
     * Attempts to use a ferry for crossing the river.
     * 
     * Requirements:
     * - $10 fee
     * - Ferry availability
     * 
     * Outcomes:
     * - Success: Safe crossing with minimal resource loss
     * - Failure: Cannot afford ferry or ferry unavailable
     * 
     * Resource impacts:
     * - Money: $10 fee
     * - Food: 0-5 lbs loss
     */
    private void useFerry() {
        List<String> messages = new ArrayList<>();
        messages.add("Approaching the ferryman...");

        if (player != null && inventory != null && player.getMoney() >= 10) {
            player.spendMoney(10);
            messages.add("Paid the ferryman $10.");
            messages.add("He safely transports you and your wagon across.");
            messages.add("A safe, albeit costly, crossing!");
            notify(String.join("\n", messages)); // Send message
            disableOptions(); // Disable buttons after choice made
            dispose(); // Close dialog after successful ferry use
        } else {
            messages.add("You don't have enough money for the ferry ($10).");
            messages.add("The ferryman turns you away.");
            notify(String.join("\n", messages)); // Send message

            // Disable the ferry button and let user choose again
            for (Component comp : optionsPanel.getComponents()) {
                if (comp instanceof JButton button) {
                    if (button.getText().contains("ferry")) {
                        button.setEnabled(false);
                        button.setText("<html><div style='text-align: center; padding: 5px; color: gray;'>Use a ferry ($10)<br><span style='font-size: 11px;'>Not enough money!</span></div></html>");
                        break;
                    }
                }
            }
            // DO NOT dispose here
        }
    }

    /**
     * Waits for better river crossing conditions.
     * 
     * Effects:
     * - Advances time by one day
     * - May improve weather conditions
     * - Reduces river depth by 1-3 feet
     * - Consumes daily resources
     * 
     * Resource impacts:
     * - Food: Normal daily consumption
     * - Time: 1 day delay
     */
    private void waitForBetterConditions() {
        List<String> messages = new ArrayList<>();
        messages.add("Deciding to wait a day for conditions to improve...");

        // Consume food for waiting
        if (player != null && inventory != null) {
            int foodConsumed = player.getFamilySize() * 2;
            inventory.consumeFood(foodConsumed);
            messages.add("Food consumed while waiting: " + foodConsumed + " lbs.");
            // NOTE: Advancing game time should ideally happen in GameController
            // after this dialog closes and indicates waiting occurred.
        }

        // 50% chance conditions improve
        if (Math.random() < 0.5) {
            messages.add("The river seems lower today!");
            int depthReduction = (int)(depth * (0.3 + (random.nextDouble() * 0.3)));
            depth = Math.max(2, depth - depthReduction);
            messages.add("The river is now only " + depth + " feet deep.");

            notify(String.join("\n", messages)); // Notify about waiting result

            // Automatically choose the best method now
            if (depth <= 5) {
                fordRiver(); // This will send its own notifications
            } else {
                caulkAndFloat(); // This will send its own notifications
            }
        } else {
            messages.add("The river hasn't changed much. Crossing now...");
            notify(String.join("\n", messages)); // Notify about waiting result

            // Randomly choose a non-wait method
            double choice = Math.random();
            if (player != null && player.getMoney() >= 10 && choice < 0.3) { // 30% chance to try ferry if affordable
                useFerry(); // Try ferry first
                // If ferry fails (not enough money), it will re-enable options,
                // but we need to prevent dispose() here if ferry fails.
                // This logic gets tricky. Simpler: just ford/caulk if wait fails.
                // Let's remove the ferry option here for simplicity after waiting.
            } else if (choice < 0.7 || depth > 10) { // Higher chance to ford unless deep
                fordRiver();
            } else {
                caulkAndFloat();
            }
        }

        // Disable buttons and close dialog after the action is taken
        // Note: If useFerry fails, it doesn't dispose, which is a slight issue here.
        // A better design might involve returning a status from the methods.
        disableOptions();
        dispose();
    }

    /**
     * Disables all crossing option buttons.
     * Called after a crossing attempt is made.
     */
    private void disableOptions() {
        if (optionsPanel == null) return;
        for (Component component : optionsPanel.getComponents()) {
            if (component instanceof JButton) {
                component.setEnabled(false);
            }
        }
    }

    /**
     * Sets the name of the river to display in the dialog.
     * 
     * @param riverName The name of the river
=     */
    public void setRiverName(String riverName) {
        this.riverName = riverName;
        // Update the title if the dialog components are already initialized
        if (isDisplayable()) {
            setTitle(riverName);
            
            // Try to update the title label if it exists
            for (Component comp : getContentPane().getComponents()) {
                if (comp instanceof JPanel panel) {
                    if (panel.getLayout() instanceof BorderLayout) {
                        for (Component innerComp : panel.getComponents()) {
                            if (innerComp instanceof JLabel label) {
                                if (label.getFont() == FontManager.WESTERN_FONT_TITLE) {
                                    label.setText(riverName);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
