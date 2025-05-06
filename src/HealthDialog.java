/**
 * HealthDialog Class of the Perils Along the Platte Game
 * Displays a comprehensive health status dialog showing the condition of:
 * - Player health and morale
 * - Family members' health
 * - Wagon parts condition and repair options
 * - Oxen team health and fatigue
 *
 * @author Alex Randall and Chase McCluskey
 * @version 1.0
 * @date 05/06/2025
 * @file HealthDialog.java
 */

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class HealthDialog extends JDialog {
    // UI Colors for western theme
    private final Color BACKGROUND_COLOR = new Color(240, 220, 180); // Parchment/sepia background
    private final Color PANEL_COLOR = new Color(200, 170, 130);      // Darker parchment for panels
    private final Color TEXT_COLOR = new Color(80, 30, 0);           // Dark brown for text
    private final Color ACCENT_COLOR = new Color(160, 100, 40);      // Light brown for borders
    private final Color HEADER_COLOR = new Color(120, 60, 0);        // Medium brown for headers

    private final Inventory inventory;
    private final JFrame parent;
    private final Player player;

    /**
     * Constructs a new HealthDialog.
     * Initializes the dialog with the current health status of:
     * - Player and family
     * - Wagon parts
     * - Oxen team
     * 
     * @param parent The parent frame (main game window)
     * @param player The player character
     * @param inventory The game's inventory system
     */
    public HealthDialog(Frame parent, Player player, Inventory inventory) {
        super(parent, "Health Status", true);
        this.parent = (JFrame) parent;
        this.inventory = inventory;
        this.player = player;

        initUI();
        pack();
        setLocationRelativeTo(parent);
        setSize(600, 500);
        setResizable(false);
    }

    /**
     * Initializes the dialog's user interface.
     * Creates and arranges all UI components including:
     * - Title panel
     * - Health sections
     * - Repair buttons
     * - Status displays
     * - Close button
     */
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);
        titlePanel.setBorder(new EmptyBorder(10, 10, 5, 10));

        JLabel titleLabel = new JLabel("Health Status", JLabel.CENTER);
        titleLabel.setFont(FontManager.WESTERN_FONT_TITLE);
        titleLabel.setForeground(TEXT_COLOR);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        add(titlePanel, BorderLayout.NORTH);

        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(PANEL_COLOR);
        contentPanel.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(20, 20, 20, 20)
        ));

        // Create a panel for all health sections
        JPanel healthSectionsPanel = new JPanel();
        healthSectionsPanel.setLayout(new BoxLayout(healthSectionsPanel, BoxLayout.Y_AXIS));
        healthSectionsPanel.setBackground(PANEL_COLOR);

        // Player Health Section
        JPanel playerHealthPanel = createHealthSection("Player Health", 
            "Health: " + player.getHealth() + "/100\n" +
            "Status: " + player.getHealthStatus() + "\n" +
            "Morale: " + player.getMorale() + "/100"
        );
        healthSectionsPanel.add(playerHealthPanel);
        healthSectionsPanel.add(Box.createVerticalStrut(15));

        // Family Health Section
        StringBuilder familyHealthText = new StringBuilder();
        String[] familyMembers = player.getFamilyMembers();
        if (familyMembers != null && familyMembers.length > 0) {
            for (String member : familyMembers) {
                if (member != null && !member.isEmpty()) {
                    familyHealthText.append(member).append(": Healthy\n");
                }
            }
        } else {
            familyHealthText.append("No family members traveling with you.");
        }
        JPanel familyHealthPanel = createHealthSection("Family Health", familyHealthText.toString());
        healthSectionsPanel.add(familyHealthPanel);
        healthSectionsPanel.add(Box.createVerticalStrut(15));

        // Wagon Health Section
        String wagonHealthText = "Wheels: " + inventory.getWheels() + " (Condition: " + inventory.getWagonPartBreakpercentage("Wheel") + "%)\n" +
                "Axles: " + inventory.getAxles() + " (Condition: " + inventory.getWagonPartBreakpercentage("Axle") + "%)\n" +
                "Tongues: " + inventory.getTongues() + " (Condition: " + inventory.getWagonPartBreakpercentage("Tongue") + "%)\n" +
                "Wagon Bows: " + inventory.getWagonBows() + " (Condition: " + inventory.getWagonPartBreakpercentage("Bow") + "%)\n" +
                "Overall Condition: " + getWagonCondition();
        
        JPanel wagonHealthPanel = createHealthSection("Wagon Health", wagonHealthText);
        
        // Add repair buttons for each part if it's broken and we have spare parts
        JPanel repairButtonsPanel = new JPanel(new GridLayout(1, 4, 5, 0));
        repairButtonsPanel.setBackground(PANEL_COLOR);
        repairButtonsPanel.setBorder(new EmptyBorder(5, 0, 0, 0));
        
        // Wheel repair button
        if (inventory.isPartBroken("Wheel") && inventory.getWheels() > 0) {
            JButton wheelRepairButton = createRepairButton("Repair Wheel", "Wheel");
            repairButtonsPanel.add(wheelRepairButton);
        } else {
            repairButtonsPanel.add(new JLabel("")); // Empty space
        }
        
        // Axle repair button
        if (inventory.isPartBroken("Axle") && inventory.getAxles() > 0) {
            JButton axleRepairButton = createRepairButton("Repair Axle", "Axle");
            repairButtonsPanel.add(axleRepairButton);
        } else {
            repairButtonsPanel.add(new JLabel("")); // Empty space
        }
        
        // Tongue repair button
        if (inventory.isPartBroken("Tongue") && inventory.getTongues() > 0) {
            JButton tongueRepairButton = createRepairButton("Repair Tongue", "Tongue");
            repairButtonsPanel.add(tongueRepairButton);
        } else {
            repairButtonsPanel.add(new JLabel("")); // Empty space
        }
        
        // Bow repair button
        if (inventory.isPartBroken("Bow") && inventory.getWagonBows() > 0) {
            JButton bowRepairButton = createRepairButton("Repair Bow", "Bow");
            repairButtonsPanel.add(bowRepairButton);
        } else {
            repairButtonsPanel.add(new JLabel("")); // Empty space
        }
        
        wagonHealthPanel.add(repairButtonsPanel, BorderLayout.SOUTH);
        healthSectionsPanel.add(wagonHealthPanel);
        healthSectionsPanel.add(Box.createVerticalStrut(15));

        // Oxen Health Section
        JPanel oxenHealthPanel = createHealthSection("Oxen Health", 
            "Health: " + inventory.getOxenHealth() + "/100\n" +
            "Number of Oxen: " + inventory.getOxen() + "\n" +
            "Condition: " + getOxenCondition()
        );
        healthSectionsPanel.add(oxenHealthPanel);

        // Add oxen health and fatigue information
        StringBuilder oxenStatus = new StringBuilder();
        oxenStatus.append("Oxen Health: ").append(inventory.getOxenHealth()).append("%\n");
        oxenStatus.append("Oxen Fatigue: ").append(inventory.getOxenFatigue()).append("%\n");
        
        if (inventory.getOxenFatigue() >= 80) {
            oxenStatus.append("Your oxen are severely fatigued and need rest.\n");
        } else if (inventory.getOxenFatigue() >= 60) {
            oxenStatus.append("Your oxen are showing signs of fatigue.\n");
        }
        
        if (inventory.getOxenHealth() <= 30) {
            oxenStatus.append("Your oxen are in poor health and need rest.\n");
        }

        JTextArea oxenTextArea = new JTextArea(oxenStatus.toString());
        oxenTextArea.setEditable(false);
        oxenTextArea.setBackground(PANEL_COLOR);
        oxenTextArea.setFont(FontManager.getWesternFont(14f));
        oxenTextArea.setForeground(TEXT_COLOR);
        oxenHealthPanel.add(oxenTextArea, BorderLayout.CENTER);

        // Add the health sections panel to a scroll pane
        JScrollPane scrollPane = new JScrollPane(healthSectionsPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(PANEL_COLOR);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        JButton closeButton = new JButton("Close");
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

    /**
     * Creates a health section panel with title and content.
     * Each section displays information about a specific health aspect:
     * - Player health
     * - Family health
     * - Wagon health
     * - Oxen health
     * 
     * @param title The section title
     * @param content The health information to display
     * @return A JPanel containing the health section
     */
    private JPanel createHealthSection(String title, String content) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PANEL_COLOR);
        panel.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FontManager.getBoldWesternFont(14));
        titleLabel.setForeground(HEADER_COLOR);
        panel.add(titleLabel, BorderLayout.NORTH);

        JTextArea contentArea = new JTextArea(content);
        contentArea.setFont(FontManager.getWesternFont(14));
        contentArea.setForeground(TEXT_COLOR);
        contentArea.setBackground(PANEL_COLOR);
        contentArea.setEditable(false);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setBorder(new EmptyBorder(5, 0, 0, 0));
        panel.add(contentArea, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Calculates the overall condition of the wagon.
     * Considers multiple factors:
     * - Average condition of all parts
     * - Number of spare parts available
     * - Presence of broken parts
     * 
     * @return A descriptive string of the wagon's condition
     */
    private String getWagonCondition() {
        // Calculate average condition of all parts
        int totalCondition = inventory.getWagonPartBreakpercentage("Wheel") +
                           inventory.getWagonPartBreakpercentage("Axle") +
                           inventory.getWagonPartBreakpercentage("Tongue") +
                           inventory.getWagonPartBreakpercentage("Bow");
        double avgCondition = totalCondition / 4.0;

        // Count total spare parts
        int totalSpares = inventory.getWheels() + inventory.getAxles() + 
                         inventory.getTongues() + inventory.getWagonBows();

        // Check if any parts are broken
        boolean hasBrokenParts = inventory.isPartBroken("Wheel") || 
                               inventory.isPartBroken("Axle") || 
                               inventory.isPartBroken("Tongue") || 
                               inventory.isPartBroken("Bow");

        // Determine condition based on multiple factors
        if (hasBrokenParts) {
            return "Critical - Parts need repair";
        } else if (avgCondition < 30) {
            return "Poor - Parts are heavily worn";
        } else if (avgCondition < 60) {
            if (totalSpares < 2) {
                return "Fair - Parts are worn and few spares";
            }
            return "Fair - Parts are moderately worn";
        } else if (avgCondition < 80) {
            if (totalSpares < 2) {
                return "Good - Parts in good shape but few spares";
            }
            return "Good - Parts in good shape";
        } else {
            if (totalSpares < 2) {
                return "Excellent - Parts in great shape but few spares";
            }
            return "Excellent - Parts in great shape with good spares";
        }
    }

    /**
     * Calculates the condition of the oxen team.
     * Evaluates based on:
     * - Health percentage
     * - Fatigue level
     * - Number of oxen
     * 
     * @return A descriptive string of the oxen team's condition
     */
    private String getOxenCondition() {
        int health = inventory.getOxenHealth();
        if (health >= 80) return "Excellent";
        if (health >= 60) return "Good";
        if (health >= 40) return "Fair";
        if (health >= 20) return "Poor";
        return "Critical";
    }

    /**
     * Creates a repair button for a specific wagon part.
     * The button is only created if:
     * - The part is broken
     * - Spare parts are available
     * 
     * @param text The button text
     * @param partName The name of the part to repair
     * @return A JButton for repairing the specified part
     */
    private JButton createRepairButton(String text, String partName) {
        JButton button = new JButton(text);
        button.setFont(FontManager.getWesternFont(12));
        button.setForeground(TEXT_COLOR);
        button.setBackground(PANEL_COLOR);
        button.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 1),
            new EmptyBorder(2, 5, 2, 5)
        ));
        
        button.addActionListener(e -> {
            // Consume the spare part first
            switch(partName) {
                case "Wheel": inventory.useWheels(1); break;
                case "Axle": inventory.useAxles(1); break;
                case "Tongue": inventory.useTongues(1); break;
                case "Bow": inventory.useWagonBows(1); break;
            }
            
            // Then repair the part
            inventory.repairPart(partName);
            
            // Update the dialog
            dispose();
            new HealthDialog(parent, player, inventory).setVisible(true);
        });
        
        return button;
    }
} 