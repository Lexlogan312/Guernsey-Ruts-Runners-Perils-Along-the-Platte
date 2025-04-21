import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * EnhancedGUI implements the improved interface for the Oregon Trail game with
 * vintage styling, map-centered layout, and game integration.
 */
public class EnhancedGUI extends JPanel {
    private GameController gameController;
    
    // Map panel components
    private MapPanel mapPanel;
    
    // Status panel components
    private JPanel statusPanel;
    private HashMap<String, JLabel> statusLabels = new HashMap<>();
    private HashMap<String, JLabel> statusIcons = new HashMap<>();
    
    // Control panel components
    private JPanel controlPanel;
    private JButton travelButton;
    private JButton restButton;
    private JButton huntButton;
    private JButton inventoryButton;
    private JButton tradeButton;
    private JButton quitButton;
    
    // Output panel components
    private JPanel outputPanel;
    private JTextArea outputTextArea;
    private JScrollPane outputScrollPane;
    
    // Colors
    private final Color BACKGROUND_COLOR = new Color(240, 220, 180); // Parchment/sepia
    private final Color PANEL_COLOR = new Color(200, 170, 130);      // Darker parchment
    private final Color TEXT_COLOR = new Color(80, 30, 0);           // Dark brown
    private final Color HEADER_COLOR = new Color(120, 60, 0);        // Medium brown
    private final Color ACCENT_COLOR = new Color(160, 100, 40);      // Light brown
    
    /**
     * Constructor
     */
    public EnhancedGUI(GameController controller) {
        this.gameController = controller;
        initializeUI();
        setupEventListeners();
    }
    
    /**
     * Initialize UI components
     */
    private void initializeUI() {
        // Set layout and style
        setLayout(new BorderLayout(10, 10));
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Add title at the top
        JLabel titleLabel = new JLabel("Perils Along the Platte");
        titleLabel.setFont(FontManager.WESTERN_FONT_TITLE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(TEXT_COLOR);
        add(titleLabel, BorderLayout.NORTH);
        
        // Create status panel (top)
        createStatusPanel();
        add(statusPanel, BorderLayout.PAGE_START);
        
        // Create map panel (center)
        createMapPanel();
        add(mapPanel, BorderLayout.CENTER);
        
        // Create control panel (right)
        createControlPanel();
        add(controlPanel, BorderLayout.EAST);
        
        // Create output panel (bottom)
        createOutputPanel();
        add(outputPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Creates the status panel with game stats
     */
    private void createStatusPanel() {
        statusPanel = new JPanel(new GridLayout(1, 9, 5, 0));
        statusPanel.setBackground(PANEL_COLOR);
        statusPanel.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(5, 5, 5, 5)
        ));
        
        // Create status categories with icons and labels
        String[] categories = {
            "Date", "Days", "Location", "Distance", 
            "Next", "Weather", "Health", "Food", "Oxen"
        };
        
        for (String category : categories) {
            JPanel itemPanel = new JPanel(new BorderLayout(2, 2));
            itemPanel.setBackground(PANEL_COLOR);
            
            // Create icon (placeholder for now)
            JLabel iconLabel = new JLabel();
            iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            // Create title
            JLabel titleLabel = new JLabel(category);
            titleLabel.setForeground(HEADER_COLOR);
            titleLabel.setFont(FontManager.getBoldWesternFont(12f));
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            // Create value label
            JLabel valueLabel = new JLabel("--");
            valueLabel.setForeground(TEXT_COLOR);
            valueLabel.setFont(FontManager.getWesternFont(12f));
            valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            // Add components to item panel
            itemPanel.add(titleLabel, BorderLayout.NORTH);
            itemPanel.add(iconLabel, BorderLayout.CENTER);
            itemPanel.add(valueLabel, BorderLayout.SOUTH);
            
            // Store references
            statusIcons.put(category, iconLabel);
            statusLabels.put(category, valueLabel);
            
            // Add to status panel
            statusPanel.add(itemPanel);
        }
    }
    
    /**
     * Creates the map panel
     */
    private void createMapPanel() {
        mapPanel = new MapPanel();
        mapPanel.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(5, 5, 5, 5)
        ));
    }
    
    /**
     * Creates the control panel
     */
    private void createControlPanel() {
        controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(6, 1, 0, 10));
        controlPanel.setBackground(BACKGROUND_COLOR);
        controlPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        travelButton = createStyledButton("Travel");
        restButton = createStyledButton("Rest");
        huntButton = createStyledButton("Hunt");
        inventoryButton = createStyledButton("Inventory");
        tradeButton = createStyledButton("Trade");
        quitButton = createStyledButton("Quit");
        
        controlPanel.add(travelButton);
        controlPanel.add(restButton);
        controlPanel.add(huntButton);
        controlPanel.add(inventoryButton);
        controlPanel.add(tradeButton);
        controlPanel.add(quitButton);
    }
    
    /**
     * Creates a styled button with western font and themed colors
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FontManager.WESTERN_FONT_BOLD);
        button.setBackground(PANEL_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(5, 10, 5, 10)
        ));
        return button;
    }
    
    /**
     * Creates the output panel
     */
    private void createOutputPanel() {
        outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBackground(BACKGROUND_COLOR);
        
        // Create titled border
        TitledBorder titledBorder = new TitledBorder("Trail Updates");
        titledBorder.setTitleFont(FontManager.WESTERN_FONT_BOLD);
        titledBorder.setTitleColor(TEXT_COLOR);
        
        // Create output text area with scroll pane
        outputTextArea = new JTextArea(8, 40);
        outputTextArea.setFont(FontManager.getWesternFont(14f));
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        outputTextArea.setEditable(false);
        outputTextArea.setBackground(new Color(250, 240, 220));
        outputTextArea.setForeground(TEXT_COLOR);
        
        // Auto-scroll to bottom
        DefaultCaret caret = (DefaultCaret)outputTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        outputScrollPane = new JScrollPane(outputTextArea);
        outputScrollPane.setBorder(new CompoundBorder(
            titledBorder,
            new EmptyBorder(5, 5, 5, 5)
        ));
        
        outputPanel.add(outputScrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Set up event listeners
     */
    private void setupEventListeners() {
        // Game controller listeners
        gameController.addMessageListener(this::appendToOutput);
        gameController.addGameStateListener(this::updateGameState);
        
        // Button action listeners
        travelButton.addActionListener(e -> gameController.travel());
        restButton.addActionListener(e -> gameController.rest());
        huntButton.addActionListener(e -> gameController.hunt());
        
        // Other buttons could open dialogs
        inventoryButton.addActionListener(e -> showInventoryDialog());
        tradeButton.addActionListener(e -> showTradeDialog());
        quitButton.addActionListener(e -> confirmQuit());
    }
    
    /**
     * Appends text to the output area
     */
    private void appendToOutput(String message) {
        outputTextArea.append(message + "\n\n");
        // Auto-scroll handled by DefaultCaret
    }
    
    /**
     * Updates all game state displays
     */
    private void updateGameState() {
        // Make sure game has been properly initialized
        if (!gameController.isGameStarted() || 
            gameController.getMap() == null || 
            gameController.getTime() == null) {
            return;
        }
        
        // Update status labels
        updateStatusLabels();
        
        // Update map
        mapPanel.updateMap(gameController.getMap());
        
        // Enable/disable buttons based on game state
        boolean gameRunning = gameController.isGameRunning();
        travelButton.setEnabled(gameRunning);
        restButton.setEnabled(gameRunning);
        huntButton.setEnabled(gameRunning && gameController.getInventory().getAmmunition() > 0);
        tradeButton.setEnabled(gameRunning && gameController.getMap().getCurrentLocation().contains("Fort"));
    }
    
    /**
     * Updates status labels with current game values
     */
    private void updateStatusLabels() {
        Time time = gameController.getTime();
        Map map = gameController.getMap();
        Player player = gameController.getPlayer();
        Inventory inventory = gameController.getInventory();
        Weather weather = gameController.getWeather();
        
        statusLabels.get("Date").setText(time.getMonthName() + " " + time.getDay());
        statusLabels.get("Days").setText(String.valueOf(time.getTotalDays()));
        statusLabels.get("Location").setText(shortenText(map.getCurrentLocation(), 15));
        statusLabels.get("Distance").setText(map.getDistanceTraveled() + " mi");
        statusLabels.get("Next").setText(shortenText(map.getNextLandmark(), 12));
        statusLabels.get("Weather").setText(weather.getCurrentWeather());
        statusLabels.get("Health").setText(player.getHealthStatus());
        statusLabels.get("Food").setText(inventory.getFood() + " lbs");
        statusLabels.get("Oxen").setText(inventory.getOxenHealth() + "%");
    }
    
    /**
     * Shortens text for display purposes
     */
    private String shortenText(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }
    
    /**
     * Shows inventory dialog
     */
    private void showInventoryDialog() {
        // This would show a dialog with detailed inventory
        // For now, just print to output
        Inventory inventory = gameController.getInventory();
        StringBuilder sb = new StringBuilder();
        sb.append("=== INVENTORY ===\n");
        sb.append("Food: ").append(inventory.getFood()).append(" pounds\n");
        sb.append("Oxen: ").append(inventory.getOxen()).append(" (Health: ").append(inventory.getOxenHealth()).append("%)\n");
        sb.append("Wagon parts: ").append(inventory.getWagonParts()).append("\n");
        sb.append("Medicine: ").append(inventory.getMedicine()).append("\n");
        sb.append("Ammunition: ").append(inventory.getAmmunition()).append(" rounds");
        
        appendToOutput(sb.toString());
    }
    
    /**
     * Shows trade dialog
     */
    private void showTradeDialog() {
        // This would show a dialog for trading
        // For now, just print to output
        if (gameController.getMap().getCurrentLocation().contains("Fort")) {
            appendToOutput("You could trade here, but trading is not implemented in the GUI yet.");
        } else {
            appendToOutput("You need to be at a fort or trading post to trade.");
        }
    }
    
    /**
     * Shows quit confirmation dialog
     */
    private void confirmQuit() {
        int option = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to quit the game?",
            "Confirm Quit",
            JOptionPane.YES_NO_OPTION
        );
        
        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    /**
     * Inner class for the map panel
     */
    private class MapPanel extends JPanel {
        private Image mapImage;
        private HashMap<String, Point> landmarkPositions = new HashMap<>();
        private String currentLocation = "";
        
        public MapPanel() {
            setBackground(PANEL_COLOR);
            
            // Load map image
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource("/resources/images/Oregon Trail Blank Map.png"));
                mapImage = icon.getImage();
            } catch (Exception e) {
                System.err.println("Error loading map image: " + e.getMessage());
            }
        }
        
        /**
         * Updates the map display with current game state
         */
        public void updateMap(Map gameMap) {
            if (gameMap == null) return;
            
            currentLocation = gameMap.getCurrentLocation();
            
            // Clear existing landmarks
            landmarkPositions.clear();
            
            // Calculate positions for landmarks along the trail
            // These would be approximate positions on the map image
            calculateLandmarkPositions(gameMap);
            
            // Repaint the map
            repaint();
        }
        
        /**
         * Calculates landmark positions on the map
         */
        private void calculateLandmarkPositions(Map gameMap) {
            if (gameMap == null) return;
            
            // Get trail choice to determine path
            int trailChoice = 1; // Default to Oregon Trail
            if (gameController.getTrail() != null) {
                if (gameController.getTrail().equals("California")) {
                    trailChoice = 2;
                } else if (gameController.getTrail().equals("Mormon")) {
                    trailChoice = 3;
                }
            }
            
            // Map dimensions
            int mapWidth = getWidth();
            int mapHeight = getHeight();
            
            // Approximate positions for the landmarks on the trail
            // Would need to be adjusted based on actual map image
            java.util.List<Landmark> landmarks = gameMap.getLandmarks();
            int totalDistance = landmarks.get(landmarks.size()-1).getDistance();
            
            for (Landmark landmark : landmarks) {
                String name = landmark.getName();
                int distance = landmark.getDistance();
                
                // Calculate position based on trail and distance
                float progress = (float)distance / totalDistance;
                
                int x = 0;
                int y = 0;
                
                if (trailChoice == 1) { // Oregon Trail
                    // Simplified trail path approximation
                    x = (int)(100 + progress * (mapWidth - 200));
                    y = (int)(mapHeight/2 - progress * 50);
                } else if (trailChoice == 2) { // California Trail
                    // Simplified California trail path
                    x = (int)(100 + progress * (mapWidth - 200));
                    y = (int)(mapHeight/2 + progress * 30);
                } else { // Mormon Trail
                    // Simplified Mormon trail path
                    x = (int)(150 + progress * (mapWidth - 250));
                    y = (int)(mapHeight/2 - 20 + progress * 10);
                }
                
                landmarkPositions.put(name, new Point(x, y));
            }
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            // Draw the map image
            if (mapImage != null) {
                g.drawImage(mapImage, 0, 0, getWidth(), getHeight(), this);
            }
            
            // Draw landmarks
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw all landmarks
            for (Entry<String, Point> entry : landmarkPositions.entrySet()) {
                String landmarkName = entry.getKey();
                Point position = entry.getValue();
                
                boolean isCurrentLocation = landmarkName.equals(currentLocation);
                
                // Draw landmark
                if (isCurrentLocation) {
                    // Current location - draw wagon icon
                    drawWagonIcon(g2d, position.x, position.y);
                } else {
                    // Other landmarks - draw dots
                    g2d.setColor(new Color(120, 60, 0));
                    g2d.fillOval(position.x - 5, position.y - 5, 10, 10);
                }
                
                // Draw landmark name
                g2d.setColor(TEXT_COLOR);
                g2d.setFont(FontManager.getWesternFont(10f));
                g2d.drawString(landmarkName, position.x - 5, position.y + 20);
            }
        }
        
        /**
         * Draws a small wagon icon at the current location
         */
        private void drawWagonIcon(Graphics2D g, int x, int y) {
            try {
                // Load wagon icon
                ImageIcon icon = new ImageIcon(getClass().getResource("/resources/images/Wagon Icon.png"));
                Image wagonImage = icon.getImage();
                
                // Draw wagon icon
                g.drawImage(wagonImage, x - 15, y - 15, 30, 30, this);
            } catch (Exception e) {
                // Fallback to simple circle if image fails to load
                g.setColor(Color.RED);
                g.fillOval(x - 8, y - 8, 16, 16);
            }
        }
    }
} 