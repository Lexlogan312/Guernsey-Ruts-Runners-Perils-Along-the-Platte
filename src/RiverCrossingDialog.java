import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

/**
 * Dialog for river crossing decisions in the GUI
 */
public class RiverCrossingDialog extends JDialog {
    private Player player;
    private Inventory inventory;
    private Weather weather;
    private Random random = new Random();
    
    // River characteristics
    private int depth;
    private int width;
    
    // GUI components
    private JTextArea resultArea;
    
    // Colors - match EnhancedGUI
    private final Color BACKGROUND_COLOR = new Color(240, 220, 180); // Parchment/sepia
    private final Color PANEL_COLOR = new Color(200, 170, 130);      // Darker parchment
    private final Color TEXT_COLOR = new Color(80, 30, 0);           // Dark brown
    private final Color HEADER_COLOR = new Color(120, 60, 0);        // Medium brown
    private final Color ACCENT_COLOR = new Color(160, 100, 40);      // Light brown

    /**
     * Constructor
     */
    public RiverCrossingDialog(Frame owner, Player player, Inventory inventory, Weather weather) {
        super(owner, "River Crossing", true);
        
        this.player = player;
        this.inventory = inventory;
        this.weather = weather;
        
        // Generate river characteristics
        depth = 2 + random.nextInt(19); // 2-20 feet deep
        width = 50 + random.nextInt(301); // 50-350 feet wide
        
        // Adjust difficulty based on weather
        if (weather.getCurrentWeather().contains("Rain") || 
            weather.getCurrentWeather().contains("Snow")) {
            depth += 3;
        }
        
        initUI();
        pack();
        setLocationRelativeTo(owner);
        setResizable(false);
    }
    
    /**
     * Initialize the user interface
     */
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Top panel with title and river information
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel titleLabel = new JLabel("River Crossing", JLabel.CENTER);
        titleLabel.setFont(FontManager.WESTERN_FONT_TITLE);
        titleLabel.setForeground(TEXT_COLOR);
        topPanel.add(titleLabel, BorderLayout.NORTH);
        
        JTextArea riverInfoText = new JTextArea(
            "You've come to a river that is " + width + " feet wide and " + depth + " feet deep.\n" +
            (weather.getCurrentWeather().contains("Rain") || weather.getCurrentWeather().contains("Snow") ? 
             "The recent precipitation has made the river higher than usual.\n" : "") +
            "You need to decide how to cross."
        );
        riverInfoText.setFont(FontManager.getWesternFont(14f));
        riverInfoText.setLineWrap(true);
        riverInfoText.setWrapStyleWord(true);
        riverInfoText.setEditable(false);
        riverInfoText.setBackground(PANEL_COLOR);
        riverInfoText.setForeground(TEXT_COLOR);
        riverInfoText.setBorder(new EmptyBorder(10, 10, 10, 10));
        topPanel.add(riverInfoText, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Options panel
        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        optionsPanel.setBackground(PANEL_COLOR);
        optionsPanel.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        // Option buttons with direct ActionListener implementations
        JButton fordButton = createOptionButton("Ford the river (wade across)", 
            "Attempt to walk the wagon through the river. Safest for shallow rivers.", 
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fordRiver();
                }
            });
        
        JButton caulkButton = createOptionButton("Caulk the wagon and float across", 
            "Seal the wagon with pitch and float across. Better for deeper rivers.", 
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    caulkAndFloat();
                }
            });
        
        JButton ferryButton = createOptionButton("Use a ferry ($10)", 
            "Pay for a safe crossing if you have enough money.", 
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    useFerry();
                }
            });
        
        JButton waitButton = createOptionButton("Wait a day and see if conditions improve", 
            "Delay your journey but possibly get better crossing conditions.", 
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    waitForBetterConditions();
                }
            });
        
        optionsPanel.add(fordButton);
        optionsPanel.add(caulkButton);
        optionsPanel.add(ferryButton);
        optionsPanel.add(waitButton);
        
        add(optionsPanel, BorderLayout.CENTER);
        
        // Result area
        resultArea = new JTextArea(8, 40);
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
        
        // Continue button (initially invisible)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton continueButton = new JButton("Continue Journey");
        continueButton.setFont(FontManager.getBoldWesternFont(14f));
        continueButton.setForeground(TEXT_COLOR);
        continueButton.setBackground(PANEL_COLOR);
        continueButton.addActionListener(e -> dispose());
        
        buttonPanel.add(continueButton);
        add(buttonPanel, BorderLayout.PAGE_END);
        
        // Set dialog size
        setSize(600, 500);
    }
    
    /**
     * Creates an option button with description
     */
    private JButton createOptionButton(String title, String description, ActionListener action) {
        // Use a regular button with HTML formatting instead of a complex layout
        String htmlText = "<html><div style='text-align: center;'>" +
                          "<b style='font-size: 14px;'>" + title + "</b><br>" +
                          "<span style='font-size: 12px;'>" + description + "</span>" +
                          "</div></html>";
        
        JButton button = new JButton(htmlText);
        button.setBackground(PANEL_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setFont(FontManager.getWesternFont(14f));
        button.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(10, 10, 10, 10)
        ));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setFocusPainted(false);
        
        // Add action listener directly to the button
        button.addActionListener(action);
        
        return button;
    }
    
    /**
     * Ford the river
     */
    private void fordRiver() {
        resultArea.setText("You attempt to ford the river...\n\n");
        
        // Fording is only safe if river isn't too deep
        double successChance;
        
        if (depth <= 3) {
            successChance = 0.9; // Very high chance of success for shallow rivers
        } else if (depth <= 8) {
            successChance = 0.6; // Moderate chance for medium depth
        } else {
            successChance = 0.3; // Low chance for deep rivers
        }
        
        // Modifier based on oxen health and count
        successChance *= (inventory.getOxenHealth() / 100.0);
        successChance *= Math.min(1.0, inventory.getOxen() / 4.0);
        
        if (Math.random() < successChance) {
            resultArea.append("You successfully ford the river!\n");
            
            // Small chance of minor issue even on success
            if (Math.random() < 0.2) {
                int foodLost = 10 + random.nextInt(31); // 10-40 pounds lost
                inventory.consumeFood(foodLost);
                resultArea.append("\nHowever, some of your food got wet and spoiled. " +
                                 "You lost " + foodLost + " pounds of food.");
            }
        } else {
            resultArea.append("Disaster! Your wagon overturns in the river!\n\n");
            
            // Lost supplies
            int foodLost = 50 + random.nextInt(101); // 50-150 pounds lost
            inventory.consumeFood(foodLost);
            
            int partsLost = random.nextInt(2); // 0-1 parts lost
            inventory.useWagonParts(partsLost);
            
            int medicineLost = random.nextInt(2); // 0-1 medicine lost
            inventory.useMedicine(medicineLost);
            
            resultArea.append("You lost:\n");
            resultArea.append("- " + foodLost + " pounds of food\n");
            if (partsLost > 0) resultArea.append("- " + partsLost + " wagon parts\n");
            if (medicineLost > 0) resultArea.append("- " + medicineLost + " medicine\n");
            
            // Potential injury
            if (Math.random() < 0.5) {
                int healthLost = 10 + random.nextInt(21); // 10-30 health lost
                player.decreaseHealth(healthLost);
                resultArea.append("\nYou were injured in the accident and lost " + healthLost + " health.");
            }
        }
        
        // Disable buttons after choice made
        disableOptions();
    }
    
    /**
     * Caulk and float across
     */
    private void caulkAndFloat() {
        resultArea.setText("You seal the wagon with pitch and prepare to float across...\n\n");
        
        double successChance;
        
        // Caulking works better for wider/deeper rivers
        if (depth > 10) {
            successChance = 0.8; // Good for deep rivers
        } else if (depth > 5) {
            successChance = 0.7; // Decent for medium rivers
        } else {
            successChance = 0.5; // Less effective for shallow rivers
        }
        
        // Weather impact
        if (weather.getCurrentWeather().contains("Rain") || 
            weather.getCurrentWeather().contains("Storm")) {
            successChance -= 0.2;
            resultArea.append("The rough water makes this crossing more difficult.\n\n");
        }
        
        if (Math.random() < successChance) {
            resultArea.append("You successfully float across the river!");
        } else {
            resultArea.append("Your wagon leaks and some of your supplies are damaged!\n\n");
            
            int foodLost = 30 + random.nextInt(71); // 30-100 pounds lost
            inventory.consumeFood(foodLost);
            
            resultArea.append("You lost " + foodLost + " pounds of food.");
            
            // Potential loss of other items
            if (Math.random() < 0.3) {
                int ammoLost = 5 + random.nextInt(11); // 5-15 ammunition lost
                inventory.useAmmunition(ammoLost);
                resultArea.append("\nYou also lost " + ammoLost + " rounds of ammunition.");
            }
        }
        
        // Disable buttons after choice made
        disableOptions();
    }
    
    /**
     * Use ferry
     */
    private void useFerry() {
        resultArea.setText("You approach the ferryman...\n\n");
        
        if (player.getMoney() >= 10) {
            player.spendMoney(10);
            resultArea.append("You pay the ferryman $10.\n" +
                             "He safely transports you and your wagon across the river.\n" +
                             "This was a safe choice!");
        } else {
            resultArea.append("You don't have enough money for the ferry ($10).\n" +
                             "The ferryman turns you away.\n\n");
            
            // Choose a random alternative method
            if (Math.random() < 0.5) {
                resultArea.append("You decide to try fording the river instead.\n\n");
                int adjustedDepth = 5 + random.nextInt(6); // Force a moderate depth river for the ford attempt
                
                // Set depth temporarily for this calculation
                int originalDepth = depth;
                depth = adjustedDepth;
                fordRiver();
                depth = originalDepth;
            } else {
                resultArea.append("You decide to caulk the wagon and float across instead.\n\n");
                caulkAndFloat();
            }
        }
        
        // Disable buttons after choice made
        disableOptions();
    }
    
    /**
     * Wait for better conditions
     */
    private void waitForBetterConditions() {
        resultArea.setText("You decide to wait a day for conditions to improve.\n" +
                          "Your party consumes food for the day.\n\n");
        
        // Consume food for waiting
        int foodConsumed = player.getFamilySize() * 2;
        inventory.consumeFood(foodConsumed);
        resultArea.append("Food consumed: " + foodConsumed + " pounds.\n\n");
        
        // 50% chance conditions improve
        if (Math.random() < 0.5) {
            resultArea.append("The river seems lower today. Crossing should be easier.\n\n");
            
            // Reduce depth by 20-50%
            int depthReduction = (int)(depth * (0.2 + (random.nextDouble() * 0.3)));
            depth -= depthReduction;
            
            // Ensure minimum depth
            if (depth < 2) depth = 2;
            
            resultArea.append("The river is now " + depth + " feet deep.\n\n");
            
            // Increase chances for all methods
            if (Math.random() < 0.6) {
                resultArea.append("You decide to ford the river.");
                fordRiver();
            } else {
                resultArea.append("You decide to caulk the wagon and float across.");
                caulkAndFloat();
            }
        } else {
            resultArea.append("The river hasn't changed much. You decide to cross anyway.\n\n");
            
            // Random method
            double choice = Math.random();
            if (choice < 0.4) {
                resultArea.append("You decide to ford the river.");
                fordRiver();
            } else if (choice < 0.8) {
                resultArea.append("You decide to caulk the wagon and float across.");
                caulkAndFloat();
            } else if (player.getMoney() >= 10) {
                resultArea.append("You decide to use the ferry.");
                useFerry();
            } else {
                resultArea.append("You decide to ford the river.");
                fordRiver();
            }
        }
        
        // Disable buttons after choice made
        disableOptions();
    }
    
    /**
     * Disable option buttons after a choice is made
     */
    private void disableOptions() {
        Component[] components = getContentPane().getComponents();
        for (Component component : components) {
            if (component instanceof JPanel && component != getContentPane().getComponent(getContentPane().getComponentCount() - 1)) {
                disableButtons((JPanel)component);
            }
        }
    }
    
    /**
     * Recursively disable buttons in a container
     */
    private void disableButtons(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof JButton) {
                ((JButton)component).setEnabled(false);
            } else if (component instanceof Container) {
                disableButtons((Container)component);
            }
        }
    }
} 