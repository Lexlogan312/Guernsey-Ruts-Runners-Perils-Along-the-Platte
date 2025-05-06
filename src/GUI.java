/**
 * GUI Class of the Perils Along the Platte Game
 * Implements the user interface for the Oregon Trail game, providing a visual representation
 * of the journey and interactive controls. The GUI includes:
 * - A map panel showing the trail, landmarks, and player progress
 * - A status panel displaying current game statistics
 * - Control buttons for game actions
 * - An output panel for game messages and updates
 * The interface uses a western-themed design with custom fonts and colors.
 *
 * @author Alex Randall and Chase McCluskey
 * @version 1.0
 * @date 05/06/2025
 * @file GUI.java
 */

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GUI extends JPanel {
    // The game controller that manages game state and logic.
    private final GameController gameController;
    private final TrailLogManager trailLog;

    // Map panel components for displaying the trail and landmarks.
    private MapPanel mapPanel;

    // Status panel components for displaying game statistics.
    private JPanel statusPanel;
    private final HashMap<String, JLabel> statusLabels = new HashMap<>();

    // Control panel components for game actions.
    private JPanel controlPanel;
    private JButton travelButton;
    private JButton restButton;
    private JButton huntButton;
    private JButton healthButton;
    private JButton inventoryButton;
    private JButton tradeButton;
    private JButton quitButton;
    private JButton journalButton;

    // Output panel components for game messages.
    private JPanel outputPanel;
    private JTextArea outputTextArea;

    // UI Colors for western theme
    private final Color BACKGROUND_COLOR = new Color(240, 220, 180); // Parchment/sepia background
    private final Color PANEL_COLOR = new Color(200, 170, 130);      // Darker parchment for panels
    private final Color TEXT_COLOR = new Color(80, 30, 0);           // Dark brown for text
    private final Color ACCENT_COLOR = new Color(160, 100, 40);      // Light brown for borders
    private final Color HEADER_COLOR = new Color(120, 60, 0);        // Medium brown for headers

    /**
     * Constructs a new GUI instance with the specified game controller.
     * Initializes all UI components and sets up event listeners.
     *
     * @param controller The game controller that manages game state and logic
     */
    public GUI(GameController controller) {
        this.gameController = controller;
        this.trailLog = controller.getTrailLog();
        initializeUI();
        setupEventListeners();
    }

    /**
     * Initializes the user interface components and layout.
     * Sets up the main panel with a GridBagLayout and creates all sub-panels:
     * - Title label
     * - Status panel
     * - Map panel
     * - Control panel
     * - Output panel
     */
    private void initializeUI() {
        // Use GridBagLayout for the main panel to handle resizing better
        setLayout(new GridBagLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();

        // Title Label (Spans across top)
        JLabel titleLabel = new JLabel("Perils Along the Platte");
        titleLabel.setFont(FontManager.WESTERN_FONT_TITLE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span status and controls
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 10, 0); // Bottom padding
        add(titleLabel, gbc);

        // Status Panel (Top row, below title)
        createStatusPanel();
        gbc.gridy = 1;
        gbc.gridwidth = 2; // Span map and controls
        gbc.weighty = 0; // No vertical stretch
        add(statusPanel, gbc);

        // Map Panel (Middle row, left column)
        createMapPanel();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0.7; // Takes up more horizontal space
        gbc.weighty = 1.0; // Takes up more vertical space
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 10); // Padding right
        add(mapPanel, gbc);

        // Control Panel (Moved above map)
        createControlPanel();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        add(controlPanel, gbc);

        // Output Panel (Bottom row, spans both columns)
        createOutputPanel();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        gbc.weighty = 1.0; // Less vertical space than map
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0); // No extra padding needed here
        add(outputPanel, gbc);
    }

    /**
     * Creates the status panel that displays current game statistics.
     * Uses GridBagLayout for flexible component arrangement.
     * Displays information including:
     * - Trail name
     * - Current date
     * - Days traveled
     * - Current location
     * - Distance traveled
     * - Next landmark
     * - Current weather
     * - Player health
     * - Food supply
     * - Oxen health
     * - Player's job
     */
    private void createStatusPanel() {
        statusPanel = new JPanel(new GridBagLayout()); // Use GridBagLayout
        statusPanel.setBackground(PANEL_COLOR);
        statusPanel.setBorder(new CompoundBorder(
                new LineBorder(ACCENT_COLOR, 2),
                new EmptyBorder(5, 5, 5, 5)
        ));

        GridBagConstraints gbcStatus = new GridBagConstraints();
        gbcStatus.insets = new Insets(2, 5, 2, 5); // Padding around elements
        gbcStatus.anchor = GridBagConstraints.CENTER;
        gbcStatus.fill = GridBagConstraints.HORIZONTAL; // Allow horizontal fill

        String[] categories = {
                "Trail", "Date", "Days", "Location", "Distance",
                "Next Landmark", "Weather", "Health", "Food", "Oxen Health", "Job"
        };

        // Define weights for columns - give more space to Location/Next Landmark
        double[] weights = {0.8, 0.8, 0.5, 1.5, 0.8, 1.5, 1.0, 0.8, 0.8, 1.0, 1.0};


        for (int i = 0; i < categories.length; i++) {
            JPanel itemPanel = new JPanel(new BorderLayout(2, 2));
            itemPanel.setBackground(PANEL_COLOR);

            // Create title
            JLabel titleLabel = new JLabel(categories[i]);
            titleLabel.setForeground(HEADER_COLOR);
            titleLabel.setFont(FontManager.getBoldWesternFont(11f)); // Slightly smaller font
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

            // Create value label
            JLabel valueLabel = new JLabel("--");
            valueLabel.setForeground(TEXT_COLOR);
            valueLabel.setFont(FontManager.getWesternFont(11f)); // Slightly smaller font
            valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

            // Add components to item panel
            itemPanel.add(titleLabel, BorderLayout.NORTH);
            itemPanel.add(valueLabel, BorderLayout.CENTER); // Value in center now

            // Store references
            statusLabels.put(categories[i], valueLabel);

            // Add itemPanel to statusPanel using GridBagLayout
            gbcStatus.gridx = i;
            gbcStatus.gridy = 0;
            gbcStatus.weightx = (i < weights.length) ? weights[i] : 1.0; // Assign weight
            statusPanel.add(itemPanel, gbcStatus);
        }
    }

    /**
     * Creates the map panel that displays the trail and player progress.
     * The map panel shows:
     * - The trail path
     * - Landmarks and their names
     * - The player's current position
     * - A progress bar indicating journey completion
     */
    private void createMapPanel() {
        mapPanel = new MapPanel();
        // Border is now handled by GridBagConstraints insets
        // mapPanel.setBorder(new CompoundBorder(...));
    }

    /**
     * Creates the control panel containing action buttons.
     * Includes buttons for:
     * - Travel
     * - Rest
     * - Hunt
     * - Health
     * - Inventory
     * - Trade
     * - Quit
     */
    private void createControlPanel() {
        controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        controlPanel.setBackground(BACKGROUND_COLOR);

        travelButton = createStyledButton("Travel");
        restButton = createStyledButton("Rest");
        huntButton = createStyledButton("Hunt");
        healthButton = createStyledButton("Health");
        inventoryButton = createStyledButton("Inventory");
        tradeButton = createStyledButton("Trade");
        quitButton = createStyledButton("Quit");
        journalButton = createStyledButton("Journal");

        // Add buttons horizontally
        controlPanel.add(travelButton);
        controlPanel.add(restButton);
        controlPanel.add(huntButton);
        controlPanel.add(healthButton);
        controlPanel.add(inventoryButton);
        controlPanel.add(tradeButton);
        controlPanel.add(journalButton);
        controlPanel.add(quitButton);
    }

    /**
     * Creates a styled button with western-themed appearance.
     * Applies custom font, colors, and border styling.
     * Adds tooltips for button functionality.
     *
     * @param text The text to display on the button
     * @return A styled JButton with the specified text
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FontManager.WESTERN_FONT_BOLD);
        button.setBackground(PANEL_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorder(new CompoundBorder(
                new LineBorder(ACCENT_COLOR, 2),
                new EmptyBorder(8, 15, 8, 15) // Increased padding
        ));
        // Tooltips for buttons
        switch (text) {
            case "Travel": button.setToolTipText("Advance along the trail for one day."); break;
            case "Rest": button.setToolTipText("Rest for one day to recover health."); break;
            case "Hunt": button.setToolTipText("Spend a day hunting for food (requires ammunition)."); break;
            case "Inventory": button.setToolTipText("View your current supplies."); break;
            case "Trade": button.setToolTipText("Trade supplies (only available at forts/trading posts)."); break;
            case "Quit": button.setToolTipText("Exit the game."); break;
            case "Journal": button.setToolTipText("Exit the journal."); break;
        }
        return button;
    }

    /**
     * Creates the output panel for displaying game messages and updates.
     * Includes a titled border and a text area with custom styling.
     * The text area is non-editable and uses a custom font.
     */
    private void createOutputPanel() {
        outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBackground(BACKGROUND_COLOR);

        // Create titled border
        TitledBorder titledBorder = new TitledBorder(new LineBorder(ACCENT_COLOR, 1), "Trail Updates");
        titledBorder.setTitleFont(FontManager.getBoldWesternFont(14f));
        titledBorder.setTitleColor(TEXT_COLOR);

        // Create text area for game output
        outputTextArea = new JTextArea(12, 30); // Adjust width for side panel
        outputTextArea.setFont(FontManager.getWesternFont(14f));
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        outputTextArea.setEditable(false);
        outputTextArea.setBackground(new Color(250, 240, 220)); // Slightly lighter background for text area
        outputTextArea.setForeground(TEXT_COLOR);
        outputTextArea.setMargin(new Insets(5, 5, 5, 5)); // Internal padding

        // Set caret to always show the latest text
        DefaultCaret caret = (DefaultCaret) outputTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        // Add text area to a scroll pane
        JScrollPane outputScrollPane = new JScrollPane(outputTextArea);
        outputScrollPane.setBorder(titledBorder);

        outputPanel.add(outputScrollPane, BorderLayout.CENTER);
    }

    /**
     * Sets up event listeners for all interactive components.
     * Configures action listeners for buttons and mouse listeners for the map.
     */
    private void setupEventListeners() {
        // Game controller listeners
        gameController.addMessageListener(this::appendToOutput);
        gameController.addGameStateListener(this::updateGameState);

        // Button action listeners
        travelButton.addActionListener(e -> gameController.travel());
        restButton.addActionListener(e -> gameController.rest());
        huntButton.addActionListener(e -> gameController.hunt());
        healthButton.addActionListener(e -> showHealthDialog());
        inventoryButton.addActionListener(e -> showInventoryDialog());
        tradeButton.addActionListener(e -> showTradeDialog());
        quitButton.addActionListener(e -> confirmQuit());
        journalButton.addActionListener(e -> showJournalPopup());
    }

    /**
     * Appends a message to the output text area.
     * Ensures the text area scrolls to show new messages.
     *
     * @param message The message to append to the output
     */
    private void appendToOutput(String message) {
        SwingUtilities.invokeLater(() -> {
            outputTextArea.append(message + "\n\n");
            // Optional: Limit the amount of text to prevent memory issues
            int maxLines = 500;
            if (outputTextArea.getLineCount() > maxLines + 50) { // Prune when significantly over limit
                try {
                    int end = outputTextArea.getLineStartOffset(50); // Keep last ~450 lines
                    outputTextArea.replaceRange("", 0, end);
                } catch (Exception ex) {
                    // Handle exception if line calculation fails
                    outputTextArea.setText(outputTextArea.getText().substring(outputTextArea.getText().length() / 2)); // Fallback: cut text in half
                }
            }
        });
    }

    /**
     * Updates the game state display.
     * Refreshes all status labels and the map to reflect current game conditions.
     */
    public void updateGameState() {
        // Ensure updates run on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Make sure game has been properly initialized
            if (!gameController.isGameStarted() ||
                    gameController.getMap() == null ||
                    gameController.getTime() == null ||
                    gameController.getPlayer() == null || // Added null checks
                    gameController.getInventory() == null ||
                    gameController.getWeather() == null) {
                System.err.println("GUI Update skipped: GameController components not fully initialized.");
                return;
            }

            // Update status labels
            updateStatusLabels();

            // Update map (this will trigger map repaint)
            mapPanel.updateMap(gameController.getMap());

            // Enable/disable buttons based on game state
            boolean gameRunning = gameController.isGameRunning();
            boolean canTrade = gameRunning && mapPanel.isAtTradingPost(gameController.getMap().getCurrentLocation());
            boolean hasAmmo = gameController.getInventory().getAmmunition() > 0;

            travelButton.setEnabled(gameRunning);
            restButton.setEnabled(gameRunning);
            huntButton.setEnabled(gameRunning && hasAmmo);
            healthButton.setEnabled(gameRunning);
            inventoryButton.setEnabled(gameRunning); // Always allow viewing inventory if game is running
            tradeButton.setEnabled(canTrade);
            quitButton.setEnabled(true); // Always allow quitting

            // Update tooltips based on enabled state
            huntButton.setToolTipText(gameRunning ? (hasAmmo ? "Spend a day hunting for food." : "Spend a day hunting (requires ammunition).") : "Game over.");
            tradeButton.setToolTipText(gameRunning ? (canTrade ? "Trade supplies at this location." : "Trade supplies (only available at forts/trading posts).") : "Game over.");

            // Force repaint of components that might have changed visually
            // Revalidate might be needed if component sizes/visibility changed drastically
            statusPanel.revalidate();
            statusPanel.repaint();
            controlPanel.revalidate();
            controlPanel.repaint();
        });
    }

    /**
     * Gets a description of the bonus effects for a specific job.
     *
     * @param job The player's current job
     * @return A string describing the job's special abilities and bonuses
     */
    private String getJobBonusDescription(Job job) {
        switch (job) {
            case FARMER:
                return "reduced food spoilage";
            case BLACKSMITH:
                return "reduced part breakage";
            case CARPENTER:
                return "increased repair skill";
            case HUNTER:
                return "increased travel speed";
            case DOCTOR:
                return "increased health";
            case TEACHER:
                return "increased morale";
            case PREACHER:
                return "increased healing";
            case MERCHANT:
                return "reduced prices";
            default:
                return "";
        }
    }

    /**
     * Updates all status labels with current game information.
     * Retrieves data from the game controller and formats it for display.
     */
    private void updateStatusLabels() {
        Time time = gameController.getTime();
        Map map = gameController.getMap();
        Player player = gameController.getPlayer();
        Inventory inventory = gameController.getInventory();
        Weather weather = gameController.getWeather();

        // Defensive null checks before accessing methods
        if (time == null || map == null || player == null || inventory == null || weather == null) {
            System.err.println("Error updating status labels: Game objects are null.");
            return;
        }

        statusLabels.get("Trail").setText(map.getTrailName());
        statusLabels.get("Date").setText(time.getMonthName() + " " + time.getDay());
        statusLabels.get("Days").setText(String.valueOf(time.getTotalDays()));
        // Increased max length for location and landmark
        statusLabels.get("Location").setText(shortenText(map.getCurrentLocation(), 24));
        statusLabels.get("Distance").setText(map.getDistanceTraveled() + " mi");
        statusLabels.get("Next Landmark").setText(shortenText(map.getNextLandmark(), 24));
        statusLabels.get("Weather").setText(weather.getCurrentWeather());
        statusLabels.get("Health").setText(player.getHealthStatus());
        statusLabels.get("Food").setText(inventory.getFood() + " lbs");
        statusLabels.get("Oxen Health").setText(inventory.getOxenHealth() + "%");

        // Add job information without prefix or bonus descriptions
        Job playerJob = player.getJob();
        if (playerJob != null) {
            // Convert UPPERCASE to Regular Case
            String jobName = playerJob.toString();
            jobName = jobName.charAt(0) + jobName.substring(1).toLowerCase();
            statusLabels.get("Job").setText(jobName);
        } else {
            statusLabels.get("Job").setText("None");
        }
    }

    /**
     * Shortens text to fit within a specified maximum length.
     * Adds ellipsis (...) if the text is too long.
     *
     * @param text The text to shorten
     * @param maxLength The maximum allowed length
     * @return The shortened text with ellipsis if needed
     */
    private String shortenText(String text, int maxLength) {
        if (text == null) {
            return "--"; // Or some other placeholder
        }
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }

    /**
     * Displays a dialog showing the player's current inventory.
     * Lists all items and their quantities with formatted labels.
     */
    private void showInventoryDialog() {
        if (!gameController.isGameRunning()) return;

        Inventory inventory = gameController.getInventory();
        Player player = gameController.getPlayer(); // Get player for money info

        if (inventory == null || player == null) return;

        // Create a custom dialog for better appearance
        JDialog inventoryDialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                "Inventory",
                true
        );
        inventoryDialog.setLayout(new BorderLayout(10, 10));
        inventoryDialog.getContentPane().setBackground(BACKGROUND_COLOR);

        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);
        titlePanel.setBorder(new EmptyBorder(10, 10, 5, 10));

        JLabel titleLabel = new JLabel("Current Inventory", JLabel.CENTER);
        titleLabel.setFont(FontManager.WESTERN_FONT_TITLE);
        titleLabel.setForeground(TEXT_COLOR);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        inventoryDialog.add(titlePanel, BorderLayout.NORTH);

        // Content panel
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(PANEL_COLOR);
        contentPanel.setBorder(new CompoundBorder(
                new LineBorder(ACCENT_COLOR, 2),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // Money info panel
        JPanel moneyPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        moneyPanel.setBackground(PANEL_COLOR);

        JLabel moneyLabel = new JLabel("Money: $" + player.getMoney(), JLabel.CENTER);
        moneyLabel.setFont(FontManager.getBoldWesternFont(16));
        moneyLabel.setForeground(TEXT_COLOR);

        // Add job information with bonus
        Job playerJob = player.getJob();
        String jobDisplayText = "Job: ";
        if (playerJob != null) {
            // Convert UPPERCASE to Regular Case
            String jobName = playerJob.toString();
            jobName = jobName.charAt(0) + jobName.substring(1).toLowerCase();

            // Add job name without special symbols
            String jobBonusText = getJobBonusDescription(playerJob);
            if (jobBonusText != null && !jobBonusText.isEmpty()) {
                jobDisplayText += jobName + " (" + jobBonusText + ")";
            } else {
                jobDisplayText += jobName;
            }
        } else {
            jobDisplayText += "None";
        }

        JLabel jobLabel = new JLabel(jobDisplayText, JLabel.CENTER);
        jobLabel.setFont(FontManager.getBoldWesternFont(14));
        jobLabel.setForeground(TEXT_COLOR);

        moneyPanel.add(moneyLabel);
        moneyPanel.add(jobLabel);

        contentPanel.add(moneyPanel, BorderLayout.NORTH);

        // Inventory items panel (using GridLayout for organization)
        JPanel itemsPanel = new JPanel(new GridLayout(0, 1, 0, 10));
        itemsPanel.setBackground(PANEL_COLOR);

        // Create styled labels for each inventory item
        JLabel foodLabel = createInventoryItemLabel("Food", inventory.getFood() + " pounds");
        JLabel oxenLabel = createInventoryItemLabel("Oxen", inventory.getOxen() + " (Health: " + inventory.getOxenHealth() + "%)");
        JLabel partsLabel = createInventoryItemLabel("Wagon Parts", String.valueOf(inventory.getWagonParts()));
        JLabel medicineLabel = createInventoryItemLabel("Medicine Kits", String.valueOf(inventory.getMedicine()));
        JLabel ammoLabel = createInventoryItemLabel("Ammunition", inventory.getAmmunition() + " rounds");

        itemsPanel.add(foodLabel);
        itemsPanel.add(oxenLabel);
        itemsPanel.add(partsLabel);
        itemsPanel.add(medicineLabel);
        itemsPanel.add(ammoLabel);

        // Remove other items section
        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(PANEL_COLOR);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        inventoryDialog.add(contentPanel, BorderLayout.CENTER);

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
        closeButton.addActionListener(e -> inventoryDialog.dispose());

        buttonPanel.add(closeButton);
        inventoryDialog.add(buttonPanel, BorderLayout.SOUTH);

        // Set dialog size and position
        inventoryDialog.setSize(500, 600); // Increased height from 450 to 600
        inventoryDialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));
        inventoryDialog.setResizable(false);
        inventoryDialog.setVisible(true);
    }

    /**
     * Creates a formatted label for an inventory item.
     *
     * @param name The name of the inventory item
     * @param value The current quantity or value of the item
     * @return A JLabel displaying the item information
     */
    private JLabel createInventoryItemLabel(String name, String value) {
        JLabel label = new JLabel(name + ": " + value);
        label.setFont(FontManager.getWesternFont(14));
        label.setForeground(TEXT_COLOR);
        label.setBorder(new CompoundBorder(
                new LineBorder(new Color(200, 180, 150), 1), // Subtle border
                new EmptyBorder(8, 10, 8, 10)
        ));
        return label;
    }

    private void showJournalPopup() {
        JDialog journalDialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Player's Journal", Dialog.ModalityType.APPLICATION_MODAL);
        journalDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel journalPanel = new JPanel(new BorderLayout(10, 10));
        journalPanel.setBackground(PANEL_COLOR);
        journalPanel.setBorder(new CompoundBorder(
                new LineBorder(ACCENT_COLOR, 2),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel("Journal Entries", SwingConstants.CENTER);
        titleLabel.setFont(FontManager.WESTERN_FONT_BOLD.deriveFont(16f));
        titleLabel.setForeground(TEXT_COLOR);
        journalPanel.add(titleLabel, BorderLayout.NORTH);

        JTextArea journalTextArea = new JTextArea(15, 40);
        journalTextArea.setText(gameController.displayJournal("all", null));
        journalTextArea.setEditable(false);
        journalTextArea.setFont(FontManager.getWesternFont(12f));
        journalTextArea.setForeground(TEXT_COLOR);
        journalTextArea.setBackground(BACKGROUND_COLOR);
        journalTextArea.setLineWrap(true);
        journalTextArea.setWrapStyleWord(true);
        journalTextArea.setCaretPosition(journalTextArea.getDocument().getLength());
        journalTextArea.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(ACCENT_COLOR, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JScrollPane scrollPane = new JScrollPane(journalTextArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        journalPanel.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.setFont(FontManager.WESTERN_FONT_BOLD);
        closeButton.setBackground(PANEL_COLOR);
        closeButton.setForeground(TEXT_COLOR);
        closeButton.setBorder(new CompoundBorder(
                new LineBorder(ACCENT_COLOR, 2),
                new EmptyBorder(5, 20, 5, 20)
        ));
        closeButton.addActionListener(e -> journalDialog.dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(PANEL_COLOR);
        buttonPanel.add(closeButton);
        journalPanel.add(buttonPanel, BorderLayout.SOUTH);

        journalDialog.getContentPane().add(journalPanel);
        journalDialog.pack();
        journalDialog.setLocationRelativeTo(this);
        journalDialog.setVisible(true);
    }

    /**
     * Displays the trading dialog when at a trading post.
     * Shows available items and their prices.
     */
    private void showTradeDialog() {
        if (!gameController.isGameRunning()) return;

        if (mapPanel.isAtTradingPost(gameController.getMap().getCurrentLocation())) {
            // Create and show trading dialog
            TradingDialog tradingDialog = new TradingDialog(
                    (Frame)SwingUtilities.getWindowAncestor(this),
                    gameController.getPlayer(),
                    gameController.getInventory()
            );
            tradingDialog.setVisible(true); // Dialog is modal

            // Update game state AFTER the dialog is closed
            gameController.updateGameState();
        } else {
            appendToOutput("You need to be at a fort or trading post to trade.");
        }
    }

    /**
     * Displays a dialog showing the player's current health status.
     * Includes health level, morale, and any active conditions.
     */
    private void showHealthDialog() {
        HealthDialog healthDialog = new HealthDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                gameController.getPlayer(),
                gameController.getInventory()
        );
        healthDialog.setVisible(true);
    }

    /**
     * Shows a confirmation dialog before quitting the game.
     * Prompts the user to confirm their decision to exit.
     */
    private void confirmQuit() {
        int option = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to quit the game?",
                "Confirm Quit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE // Use question icon
        );

        if (option == JOptionPane.YES_OPTION) {
            System.exit(0); // Exit the application
        }
    }

    // Inner class for the map panel
    private class MapPanel extends JPanel {
        private Image mapImage;
        private Map map; // Reference to the game's map object
        private final HashMap<String, Point> landmarkScreenPositions = new HashMap<>();
        private final List<Rectangle> drawnLabelBounds = new ArrayList<>(); // For collision detection
        private BufferedImage bufferedMap; // For double buffering
        private int distanceTraveled = 0;
        private int totalDistance = 0;
        private boolean showTooltip = false;
        private String tooltipText = "";
        private Point tooltipPosition = new Point(0, 0);
        private int fortKearnyDistance = -1; // Cache Fort Kearny's distance
        private Point wagonScreenPosition = null; // Current calculated screen position for the wagon

        // Map drawing constants
        private final Color TRAIL_COLOR = new Color(139, 69, 19, 180); // Brown semi-transparent
        private final Font LANDMARK_FONT = FontManager.getBoldWesternFont(11f); // Smaller font
        private final Font TOOLTIP_FONT = FontManager.getWesternFont(12f);
        private final BasicStroke TRAIL_STROKE = new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        private final BasicStroke LABEL_LINE_STROKE = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{2}, 0);

        // Original map dimensions for aspect ratio preservation
        private int originalMapWidth = 0;
        private int originalMapHeight = 0;
        private final Rectangle mapDrawArea = new Rectangle(); // Area where map image is drawn

        /**
         * Constructs a new map panel.
         * Initializes the map image and sets up mouse listeners.
         */
        public MapPanel() {
            setBackground(PANEL_COLOR); // Match GUI panel color
            setPreferredSize(new Dimension(800, 500)); // Initial preferred size

            loadMapImage(); // Load the background map

            // Add mouse motion listener for tooltips
            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    if (map != null && !landmarkScreenPositions.isEmpty()) { // Only check if map loaded
                        checkLandmarkHover(e.getPoint());
                    }
                }
            });
        }

        /**
         * Loads the map image from resources.
         * Scales the image to fit the panel while maintaining aspect ratio.
         */
        private void loadMapImage() {
            try {
                // Use ResourceLoader to load the map image
                ImageIcon icon = ResourceLoader.loadImage("images/Pixel Map.png");

                if (icon != null && icon.getImageLoadStatus() == MediaTracker.COMPLETE && icon.getIconWidth() > 0) {
                    mapImage = icon.getImage();
                    originalMapWidth = icon.getIconWidth();
                    originalMapHeight = icon.getIconHeight();
                    System.out.println("Map loaded: " + originalMapWidth + "x" + originalMapHeight);
                } else {
                    System.err.println("Failed to load map image using ResourceLoader");
                    mapImage = null;
                }
            } catch (Exception e) {
                System.err.println("Error loading map image: " + e.getMessage());
                e.printStackTrace();
                mapImage = null;
            }
        }

        /**
         * Updates the map display with current game state.
         *
         * @param gameMap The current game map object
         */
        public void updateMap(Map gameMap) {
            if (gameMap == null) {
                System.err.println("MapPanel update skipped: gameMap is null.");
                return; // Cannot update without map data
            }
            this.map = gameMap; // Store reference to the current map object

            // Update game state variables used for drawing
            String currentLocationName = map.getCurrentLocation();
            distanceTraveled = map.getDistanceTraveled();
            if (map.getLandmarks() != null && !map.getLandmarks().isEmpty()) {
                totalDistance = map.getLandmarks().get(map.getLandmarks().size()-1).getDistance();
                // Find and cache Fort Kearny's distance if not already done
                if (fortKearnyDistance < 0) {
                    fortKearnyDistance = findFortKearnyDistance();
                }
            } else {
                totalDistance = 0;
                fortKearnyDistance = -1; // Reset if map is invalid
                System.err.println("MapPanel update warning: Map landmarks list is empty or null.");
            }


            // Recreate the buffered image for double buffering
            if (getWidth() > 0 && getHeight() > 0) {
                bufferedMap = createBufferedMapImage();
            }


            // Repaint the map panel
            repaint();
        }

        /**
         * Finds the distance to Fort Kearny for progress calculation.
         *
         * @return The distance to Fort Kearny in miles
         */
        private int findFortKearnyDistance() {
            if (map == null || map.getLandmarks() == null) return 0; // Default or error value
            for (Landmark lm : map.getLandmarks()) {
                if (lm.getName().contains("Fort Kearny")) {
                    return lm.getDistance();
                }
            }
            System.err.println("Warning: Fort Kearny landmark not found in map data.");
            return 0; // Return 0 if not found, so all landmarks might show
        }

        /**
         * Creates a buffered image for the map display.
         * Enables smooth rendering and double buffering.
         *
         * @return A BufferedImage containing the map
         */
        private BufferedImage createBufferedMapImage() {
            // Ensure panel has valid dimensions
            int width = getWidth();
            int height = getHeight();
            if (width <= 0 || height <= 0) {
                return null;
            }

            BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = buffer.createGraphics();

            // Enable antialiasing for smoother graphics
            setupGraphics2D(g2d);

            // Fill background
            g2d.setColor(PANEL_COLOR);
            g2d.fillRect(0, 0, width, height);

            // Calculate map drawing area to preserve aspect ratio
            calculateMapDrawArea(width, height);

            // --- Update Screen Positions BEFORE Drawing ---
            updateLandmarkScreenPositions(); // Calculate where landmarks go on screen
            calculateWagonScreenPosition(); // Calculate where the wagon should be

            // Draw background map image
            drawMapBackground(g2d);

            // Draw trail paths using the calculated screen positions (filtered)
            drawTrailPaths(g2d);

            // Draw landmarks (dots and names) using calculated screen positions (filtered)
            drawLandmarks(g2d);

            // Draw the MOVING WAGON ICON separately
            drawMovingWagon(g2d);

            // Draw progress bar
            drawProgressBar(g2d);

            g2d.dispose(); // Release graphics resources
            return buffer;
        }

        /**
         * Sets up the Graphics2D context for drawing.
         * Configures rendering hints for quality and anti-aliasing.
         *
         * @param g2d The Graphics2D context to configure
         */
        private void setupGraphics2D(Graphics2D g2d) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        }

        /**
         * Draws the map background image.
         * Scales and positions the image within the panel.
         *
         * @param g2d The Graphics2D context for drawing
         */
        private void drawMapBackground(Graphics2D g2d) {
            if (mapImage != null) {
                g2d.drawImage(mapImage, mapDrawArea.x, mapDrawArea.y,
                        mapDrawArea.width, mapDrawArea.height, this);
            } else {
                // Fallback if image fails to load
                g2d.setColor(new Color(240, 230, 200)); // Sandy color
                g2d.fillRect(mapDrawArea.x, mapDrawArea.y, mapDrawArea.width, mapDrawArea.height);
                g2d.setColor(TEXT_COLOR);
                g2d.setFont(FontManager.getBoldWesternFont(16));
                String noMapText = "Map image could not be loaded";
                FontMetrics fm = g2d.getFontMetrics();
                int textX = mapDrawArea.x + (mapDrawArea.width - fm.stringWidth(noMapText)) / 2;
                int textY = mapDrawArea.y + (mapDrawArea.height + fm.getAscent()) / 2;
                g2d.drawString(noMapText, textX, textY);
            }
        }

        /**
         * Calculates the area where the map image should be drawn.
         * Maintains aspect ratio while fitting within panel bounds.
         *
         * @param panelWidth The width of the panel
         * @param panelHeight The height of the panel
         */
        private void calculateMapDrawArea(int panelWidth, int panelHeight) {
            if (originalMapWidth <= 0 || originalMapHeight <= 0 || panelWidth <= 0 || panelHeight <= 0) {
                // If we don't have original map dimensions or panel size, use the panel size directly
                mapDrawArea.setBounds(0, 0, panelWidth, panelHeight);
                return;
            }

            // Calculate aspect ratios
            double mapAspect = (double)originalMapWidth / (double)originalMapHeight;
            double panelAspect = (double)panelWidth / (double)panelHeight;

            int drawWidth, drawHeight;
            if (mapAspect > panelAspect) {
                // Map is wider than panel relative to height -> fit width
                drawWidth = panelWidth;
                drawHeight = (int)Math.floor(drawWidth / mapAspect); // Use floor to avoid sub-pixel differences
            } else {
                // Map is taller than panel relative to width (or equal aspect) -> fit height
                drawHeight = panelHeight;
                drawWidth = (int)Math.floor(drawHeight * mapAspect); // Use floor to avoid sub-pixel differences
            }

            // Cache these values once calculated to prevent drift between calls
            if (mapDrawArea.width != drawWidth || mapDrawArea.height != drawHeight) {
                // Center the map drawing area within the panel
                int x = (panelWidth - drawWidth) / 2;
                int y = (panelHeight - drawHeight) / 2;

                // Ensure consistent pixel positioning
                x = Math.max(0, x);
                y = Math.max(0, y);

                mapDrawArea.setBounds(x, y, drawWidth, drawHeight);

                // After calculating a new draw area, ensure we recalculate landmark positions
                updateLandmarkScreenPositions();
            }
        }

        /**
         * Converts map coordinates to screen coordinates.
         *
         * @param imagePoint The point in map coordinates
         * @return The corresponding point in screen coordinates
         */
        private Point scaleImagePointToScreen(Point imagePoint) {
            if (originalMapWidth <= 0 || originalMapHeight <= 0 || mapDrawArea.width <= 0 || mapDrawArea.height <= 0) {
                // Return a default point or the original point if scaling is not possible
                System.err.println("Warning: Cannot scale image point, invalid dimensions.");
                return new Point(mapDrawArea.x, mapDrawArea.y);
            }

            // Calculate scaling factors
            double scaleX = (double) mapDrawArea.width / originalMapWidth;
            double scaleY = (double) mapDrawArea.height / originalMapHeight;

            // Scale the point relative to the image origin (0,0)
            int scaledX = (int) (imagePoint.x * scaleX);
            int scaledY = (int) (imagePoint.y * scaleY);

            // Translate the scaled point to the mapDrawArea's origin on the panel
            int screenX = mapDrawArea.x + scaledX;
            int screenY = mapDrawArea.y + scaledY;

            return new Point(screenX, screenY);
        }

        /**
         * Updates the screen positions of all landmarks.
         * Recalculates positions when the map is resized.
         */
        private void updateLandmarkScreenPositions() {
            landmarkScreenPositions.clear(); // Clear previous screen positions
            if (map == null || map.getLandmarks() == null) return;

            for (Landmark landmark : map.getLandmarks()) {
                // Create a Point object from the landmark's stored image coordinates
                Point imagePoint = new Point(landmark.getImageX(), landmark.getImageY());
                // Scale this image point to the current screen drawing area
                Point screenPoint = scaleImagePointToScreen(imagePoint);
                // Store the calculated screen point in the HashMap
                landmarkScreenPositions.put(landmark.getName(), screenPoint);
            }
        }

        /**
         * Calculates the current screen position of the wagon.
         * Updates based on the player's progress along the trail.
         */
        private void calculateWagonScreenPosition() {
            if (map == null || landmarkScreenPositions.isEmpty() || map.getLandmarks() == null || totalDistance <= 0) {
                wagonScreenPosition = null; // Cannot calculate
                return;
            }

            List<Landmark> landmarks = map.getLandmarks();
            Landmark previousLandmark = null;
            Landmark nextLandmark = null;

            // Find the two landmarks the player is currently between
            for (Landmark currentLandmark : landmarks) {
                if (currentLandmark.getDistance() <= distanceTraveled) {
                    previousLandmark = currentLandmark;
                } else {
                    nextLandmark = currentLandmark;
                    break; // Found the next landmark
                }
            }

            // Handle edge cases: before the first landmark or after the last
            if (previousLandmark == null && !landmarks.isEmpty()) {
                // Before the first landmark (shouldn't happen after Fort Kearny filter, but good practice)
                previousLandmark = landmarks.get(0);
                nextLandmark = landmarks.size() > 1 ? landmarks.get(1) : previousLandmark; // Use first segment
            } else if (nextLandmark == null && previousLandmark != null) {
                // Past the last landmark (at destination)
                wagonScreenPosition = landmarkScreenPositions.get(previousLandmark.getName());
                return;
            } else if (previousLandmark == null) {
                // No landmarks at all?
                wagonScreenPosition = new Point(mapDrawArea.x, mapDrawArea.y); // Default position
                return;
            }


            // Get screen positions for the surrounding landmarks
            Point prevScreenPos = landmarkScreenPositions.get(previousLandmark.getName());
            Point nextScreenPos = landmarkScreenPositions.get(nextLandmark.getName());

            if (prevScreenPos == null || nextScreenPos == null) {
                System.err.println("Warning: Missing screen position for interpolation between " + previousLandmark.getName() + " and " + nextLandmark.getName());
                wagonScreenPosition = prevScreenPos != null ? prevScreenPos : (nextScreenPos != null ? nextScreenPos : new Point(mapDrawArea.x, mapDrawArea.y)); // Fallback
                return;
            }

            // Calculate the distance between these two landmarks
            double segmentActualDistance = nextLandmark.getDistance() - previousLandmark.getDistance();

            // Calculate how far the player has traveled *within this segment*
            double traveledInSegment = distanceTraveled - previousLandmark.getDistance();

            // Calculate the interpolation factor (t)
            double t = 0.0;
            if (segmentActualDistance > 0) {
                t = traveledInSegment / segmentActualDistance;
                t = Math.max(0.0, Math.min(1.0, t)); // Clamp t between 0 and 1
            } else if (distanceTraveled >= nextLandmark.getDistance()) {
                t = 1.0; // If landmarks have same distance, snap to the next one if passed
            }


            // Interpolate the screen coordinates
            int wagonX = (int) (prevScreenPos.x + (nextScreenPos.x - prevScreenPos.x) * t);
            int wagonY = (int) (prevScreenPos.y + (nextScreenPos.y - prevScreenPos.y) * t);

            wagonScreenPosition = new Point(wagonX, wagonY);
        }

        /**
         * Draws the trail paths on the map.
         * Shows the main trail and any alternate routes.
         *
         * @param g2d The Graphics2D context for drawing
         */
        private void drawTrailPaths(Graphics2D g2d) {
            if (map == null || landmarkScreenPositions.isEmpty() || map.getLandmarks() == null || fortKearnyDistance < 0) return;

            g2d.setStroke(TRAIL_STROKE);
            g2d.setColor(TRAIL_COLOR);

            Point prevPoint = null;
            List<Landmark> landmarks = map.getLandmarks();
            boolean foundStart = false; // Flag to indicate if we've found Fort Kearny or later

            for (Landmark landmark : landmarks) {
                // Only start processing from Fort Kearny onwards
                if (!foundStart && landmark.getDistance() >= fortKearnyDistance) {
                    foundStart = true;
                    prevPoint = landmarkScreenPositions.get(landmark.getName());
                    if (prevPoint == null) {
                        System.err.println("Warning: No screen position found for starting landmark: " + landmark.getName());
                        return; // Cannot draw trail without start point
                    }
                }

                if (!foundStart) continue; // Skip landmarks before Fort Kearny

                Point currentPoint = landmarkScreenPositions.get(landmark.getName());
                if (currentPoint == null) {
                    System.err.println("Warning: No screen position found for landmark: " + landmark.getName());
                    continue; // Skip if position is missing
                }

                // Draw segments from Fort Kearny onwards
                if (prevPoint != null) {
                    g2d.drawLine(prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y);
                }
                prevPoint = currentPoint; // Update previous point for the next iteration
            }
        }

        /**
         * Draws the progress bar showing journey completion.
         * Displays a visual indicator of distance traveled.
         *
         * @param g2d The Graphics2D context for drawing
         */
        private void drawProgressBar(Graphics2D g2d) {
            if (totalDistance <= 0) return; // Avoid division by zero

            float progressRatio = (float) distanceTraveled / (float) totalDistance;
            progressRatio = Math.max(0f, Math.min(progressRatio, 1.0f)); // Clamp between 0 and 1

            int progressBarWidth = 200;
            int progressBarHeight = 18;
            int x = getWidth() - progressBarWidth - 20; // Position bottom-right
            int y = getHeight() - progressBarHeight - 20;

            // Draw progress bar background (slightly transparent)
            g2d.setColor(new Color(0, 0, 0, 80));
            g2d.fillRoundRect(x - 2, y - 2, progressBarWidth + 4, progressBarHeight + 4, 5, 5);

            // Draw progress bar track
            g2d.setColor(new Color(255, 255, 255, 180));
            g2d.fillRoundRect(x, y, progressBarWidth, progressBarHeight, 5, 5);

            // Draw progress fill
            g2d.setColor(new Color(0, 128, 0, 220)); // Green progress
            g2d.fillRoundRect(x, y, (int)(progressBarWidth * progressRatio), progressBarHeight, 5, 5);

            // Draw progress text (centered on bar)
            g2d.setColor(Color.BLACK);
            g2d.setFont(FontManager.getBoldWesternFont(11f));
            String progressText = distanceTraveled + " / " + totalDistance + " miles";
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(progressText);
            g2d.drawString(progressText,
                    x + (progressBarWidth - textWidth) / 2,
                    y + progressBarHeight / 2 + fm.getAscent() / 2 - 1); // Adjust vertical alignment
        }

        /**
         * Draws all landmarks on the map.
         * Shows landmark names and current location indicator.
         *
         * @param g2d The Graphics2D context for drawing
         */
        private void drawLandmarks(Graphics2D g2d) {
            if (map == null || landmarkScreenPositions.isEmpty() || map.getLandmarks() == null || fortKearnyDistance < 0) return;

            drawnLabelBounds.clear(); // Clear bounds from previous frame
            int index = 0;
            List<Landmark> landmarks = map.getLandmarks();

            for (Landmark landmark : landmarks) {
                // *** FILTER: Only draw landmarks at or after Fort Kearny ***
                if (landmark.getDistance() < fortKearnyDistance) {
                    index++; // Still increment index for label positioning offset
                    continue;
                }

                String landmarkName = landmark.getName();
                Point position = landmarkScreenPositions.get(landmarkName);

                if (position == null) continue; // Skip if no position calculated

                // Draw landmark dot (Wagon is drawn separately now)
                g2d.setColor(new Color(120, 60, 0)); // Brown dot
                g2d.fillOval(position.x - 4, position.y - 4, 8, 8);
                g2d.setColor(new Color(0, 0, 0, 100)); // Faint border
                g2d.drawOval(position.x - 4, position.y - 4, 8, 8);

                // Draw the landmark name label, attempting to avoid overlap
                // Pass the landmark object to allow access to custom label positioning
                drawLandmarkName(g2d, landmark, position, false, index);
                index++;
            }
        }

        /**
         * Draws the moving wagon icon at the current position.
         *
         * @param g2d The Graphics2D context for drawing
         */
        private void drawMovingWagon(Graphics2D g2d) {
            if (wagonScreenPosition != null) {
                drawWagonIcon(g2d, wagonScreenPosition.x, wagonScreenPosition.y);
            }
        }

        /**
         * Draws a landmark name with connecting line.
         * Handles text positioning and collision avoidance.
         *
         * @param g2d The Graphics2D context for drawing
         * @param landmark The landmark to draw
         * @param position The screen position of the landmark
         * @param isCurrentLocation Whether this is the current location
         * @param index The landmark's index for positioning
         */
        private void drawLandmarkName(Graphics2D g2d, Landmark landmark, Point position, boolean isCurrentLocation, int index) {
            String landmarkName = landmark.getName();
            g2d.setFont(LANDMARK_FONT);
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(landmarkName);
            int textHeight = fm.getHeight();
            int ascent = fm.getAscent();
            int padding = 4; // Padding around text

            int textX, textY;

            // Use custom position if provided
            Point customLabelPos = scaleImagePointToScreen(new Point(landmark.getLabelX(), landmark.getLabelY()));
            textX = customLabelPos.x;
            textY = customLabelPos.y;

            // Draw dotted line from text anchor to landmark dot
            Stroke oldStroke = g2d.getStroke();
            g2d.setColor(new Color(100, 100, 100, 150)); // Grayish line
            g2d.setStroke(LABEL_LINE_STROKE);
            g2d.drawLine(position.x, position.y, textX + textWidth / 2, textY - ascent / 2);
            g2d.setStroke(oldStroke);

            // Calculate bounds for background
            int bgX = textX - padding;
            int bgY = textY - ascent - padding / 2;
            int bgWidth = textWidth + padding * 2;
            int bgHeight = textHeight + padding;

            // Draw text background for readability
            g2d.setColor(new Color(240, 240, 220, 200)); // Semi-transparent background
            g2d.fillRoundRect(bgX, bgY, bgWidth, bgHeight, 8, 8);

            // Main text (adjust baseline for drawing)
            g2d.setColor(TEXT_COLOR);
            g2d.drawString(landmarkName, textX, textY);

            // Store the bounds of this label to avoid future collisions
            drawnLabelBounds.add(new Rectangle(bgX, bgY, bgWidth, bgHeight));
        }

        /**
         * Draws the wagon icon at the specified position.
         *
         * @param g The Graphics context for drawing
         * @param x The x-coordinate
         * @param y The y-coordinate
         */
        private void drawWagonIcon(Graphics g, int x, int y) {
            try {
                // Use ResourceLoader to load the wagon icon
                ImageIcon icon = ResourceLoader.loadImage("images/Wagon Icon.png");

                if (icon != null && icon.getImageLoadStatus() == MediaTracker.COMPLETE && icon.getIconWidth() > 0) {
                    Image wagonImage = icon.getImage();

                    // Scale the wagon icon
                    int wagonSize = 75; // Smaller icon size

                    // Draw wagon icon centered on the location
                    g.drawImage(wagonImage, x - wagonSize/2, y - wagonSize/2, wagonSize, wagonSize, this);
                } else {
                    throw new Exception("Failed to load wagon icon using ResourceLoader");
                }
            } catch (Exception e) {
                System.err.println("Error loading wagon icon: " + e.getMessage());
            }
        }

        /**
         * Checks if the mouse is hovering over a landmark.
         * Updates tooltip display accordingly.
         *
         * @param mousePoint The current mouse position
         */
        private void checkLandmarkHover(Point mousePoint) {
            String newTooltipText = null;
            Point newTooltipPosition = null;

            double minDistance = 15.0; // Hover sensitivity radius

            // Iterate through the *calculated screen positions*
            // *** FILTER: Only check landmarks at or after Fort Kearny ***
            if (map == null || map.getLandmarks() == null || fortKearnyDistance < 0) return;

            for (Landmark landmark : map.getLandmarks()) {
                if (landmark.getDistance() < fortKearnyDistance) continue; // Skip landmarks before Fort Kearny

                Point landmarkScreenPos = landmarkScreenPositions.get(landmark.getName());
                if (landmarkScreenPos == null) continue; // Skip if position missing

                double distance = mousePoint.distance(landmarkScreenPos);

                if (distance <= minDistance) {
                    // Found a landmark being hovered over
                    // Build tooltip text
                    StringBuilder sb = new StringBuilder();
                    sb.append(landmark.getName());
                    sb.append(" (").append(landmark.getDistance()).append(" miles)");
                    if (landmark.getDescription() != null && !landmark.getDescription().isEmpty()) {
                        sb.append("\n").append(landmark.getDescription());
                    }
                    newTooltipText = sb.toString();
                    newTooltipPosition = landmarkScreenPos; // Use screen position for tooltip anchor
                    break; // Stop checking once one is found
                }
            }

            // Update tooltip state if it changed
            if ((newTooltipText != null && !newTooltipText.equals(tooltipText)) || (newTooltipText == null && tooltipText != null)) {
                tooltipText = newTooltipText;
                tooltipPosition = newTooltipPosition;
                showTooltip = (tooltipText != null);
                repaint(); // Repaint needed to show/hide tooltip
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw the pre-rendered map buffer
            if (bufferedMap != null) {
                g.drawImage(bufferedMap, 0, 0, this);
            } else {
                // If buffer failed (e.g., panel size 0), try to create it now
                if (getWidth() > 0 && getHeight() > 0 && map != null) {
                    bufferedMap = createBufferedMapImage();
                    if (bufferedMap != null) {
                        g.drawImage(bufferedMap, 0, 0, this);
                    } else {
                        // Still failed, draw error message
                        g.setColor(Color.RED);
                        g.setFont(FontManager.getBoldWesternFont(16f));
                        g.drawString("Error rendering map buffer.", 20, 40);
                    }
                }
            }


            // Draw tooltip on top if active
            if (showTooltip && tooltipPosition != null) {
                drawTooltip((Graphics2D) g, tooltipText, tooltipPosition);
            }
        }

        /**
         * Draws a tooltip at the specified position.
         * Shows additional information about landmarks on hover.
         *
         * @param g2d The Graphics2D context for drawing
         * @param text The tooltip text to display
         * @param position The screen position for the tooltip
         */
        private void drawTooltip(Graphics2D g2d, String text, Point position) {
            setupGraphics2D(g2d);

            g2d.setFont(TOOLTIP_FONT);
            FontMetrics fm = g2d.getFontMetrics();
            int lineHeight = fm.getHeight();
            int padding = 8;
            int maxTooltipWidth = 250; // Max width before wrapping

            // Split text into lines based on explicit newlines first
            String[] initialLines = text.split("\n");
            List<String> linesToDraw = new ArrayList<>();
            int actualTooltipWidth = 0;

            // Process each initial line for wrapping
            for (String line : initialLines) {
                int lineWidth = fm.stringWidth(line);
                if (lineWidth <= maxTooltipWidth) {
                    // Line fits, add it directly
                    linesToDraw.add(line);
                    actualTooltipWidth = Math.max(actualTooltipWidth, lineWidth);
                } else {
                    // Line needs wrapping
                    StringBuilder currentLine = new StringBuilder();
                    String[] words = line.split("\\s+");
                    for (String word : words) {
                        String testLine = currentLine.length() > 0 ? currentLine + " " + word : word;
                        if (fm.stringWidth(testLine) <= maxTooltipWidth) {
                            if (currentLine.length() > 0) currentLine.append(" ");
                            currentLine.append(word);
                        } else {
                            // Add the completed line
                            linesToDraw.add(currentLine.toString());
                            actualTooltipWidth = Math.max(actualTooltipWidth, fm.stringWidth(currentLine.toString()));
                            // Start new line with the current word
                            currentLine = new StringBuilder(word);
                        }
                    }
                    // Add the last part of the wrapped line
                    if (currentLine.length() > 0) {
                        linesToDraw.add(currentLine.toString());
                        actualTooltipWidth = Math.max(actualTooltipWidth, fm.stringWidth(currentLine.toString()));
                    }
                }
            }

            // Calculate final tooltip dimensions
            int tooltipWidth = actualTooltipWidth + (padding * 2);
            int tooltipHeight = (lineHeight * linesToDraw.size()) + (padding * 2);

            // Calculate tooltip position, ensuring it stays within the *panel* bounds
            int x = position.x + 15; // Default: right of the point
            int y = position.y - 10; // Default: slightly above the point

            // Adjust if tooltip would go off the right edge
            if (x + tooltipWidth > getWidth() - 5) { // Subtract buffer from edge
                x = position.x - tooltipWidth - 15; // Move to the left
            }
            // Adjust if tooltip would go off the left edge
            if (x < 5) {
                x = 5;
            }

            // Adjust if tooltip would go off the bottom edge
            if (y + tooltipHeight > getHeight() - 5) {
                y = position.y - tooltipHeight - 10; // Move fully above
            }
            // Adjust if tooltip would go off the top edge
            if (y < 5) {
                y = 5;
            }


            // Draw tooltip background with shadow
            g2d.setColor(new Color(0, 0, 0, 100)); // Shadow color
            g2d.fillRoundRect(x + 2, y + 2, tooltipWidth, tooltipHeight, 10, 10); // Offset shadow

            // Draw main background
            g2d.setColor(new Color(250, 240, 220, 240)); // Parchment background, slightly transparent
            g2d.fillRoundRect(x, y, tooltipWidth, tooltipHeight, 10, 10);

            // Draw border
            g2d.setColor(new Color(139, 69, 19)); // Brown border
            g2d.drawRoundRect(x, y, tooltipWidth, tooltipHeight, 10, 10);

            // Draw text lines
            g2d.setColor(Color.BLACK);
            int textY = y + padding + fm.getAscent(); // Start position for first line

            for (String line : linesToDraw) {
                g2d.drawString(line, x + padding, textY);
                textY += lineHeight; // Move to next line
            }
        }

        /**
         * Checks if the current location is a trading post.
         *
         * @param locationName The name of the current location
         * @return true if the location is a trading post, false otherwise
         */
        public boolean isAtTradingPost(String locationName) {
            if (locationName == null) return false;
            return locationName.contains("Fort") || locationName.contains("Trading Post");
        }

    }
}