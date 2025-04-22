import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    //ArrayList
    private final List<Rectangle> drawnLabelBounds = new ArrayList<>(); // Arraylist for Map Spacing Checker

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
        private Map map = gameController.getMap();
        private HashMap<String, Point> landmarkPositions = new HashMap<>();
        private String currentLocation = "";
        private BufferedImage bufferedMap; // For double buffering
        private int distanceTraveled = 0;
        private int totalDistance = 0;
        private boolean showTooltip = false;
        private String tooltipText = "";
        private Point tooltipPosition = new Point(0, 0);
        private final Color TRAIL_COLOR = new Color(139, 69, 19, 180); // Brown semi-transparent
        private final Color COMPLETED_TRAIL_COLOR = new Color(165, 42, 42, 200); // Darker brown for completed path
        private final Font LANDMARK_FONT = FontManager.getBoldWesternFont(12f);
        private final Font MAP_FONT = new Font("SanSerif", Font.BOLD, 12);
        private final Color LANDMARK_TEXT_SHADOW = new Color(0, 0, 0, 160);

        public MapPanel() {
            setBackground(PANEL_COLOR);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(10, 10, 10, 10),
                    BorderFactory.createLineBorder(new Color(101, 67, 33), 2)
            ));
            setPreferredSize(new Dimension(640, 480)); // Set preferred size for map

            // Load map image
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource("/resources/images/Oregon Trail Blank Map.png"));
                mapImage = icon.getImage();
            } catch (Exception e) {
                System.err.println("Error loading map image: " + e.getMessage());
            }

            // Add mouse motion listener for tooltips
            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    checkLandmarkHover(e.getPoint());
                }
            });
        }

        /**
         * Updates the map display with current game state
         */
        public void updateMap(Map gameMap) {
            if (gameMap == null) return;

            currentLocation = gameMap.getCurrentLocation();

            // Get distance traveled - need to find the landmark by looping through them
            List<Landmark> landmarks = gameMap.getLandmarks();
            for (Landmark lm : landmarks) {
                if (lm.getName().equals(currentLocation)) {
                    distanceTraveled = lm.getDistance();
                    break;
                }
            }

            // Clear existing landmarks
            landmarkPositions.clear();

            // Calculate positions for landmarks along the trail
            calculateLandmarkPositions(gameMap);

            // Create the buffered image for double buffering
            if (getWidth() > 0 && getHeight() > 0) {
                bufferedMap = createBufferedMapImage(gameMap);
            }

            // Repaint the map
            repaint();
        }

        /**
         * Creates a buffered image of the map for smoother rendering
         */
        private BufferedImage createBufferedMapImage(Map gameMap) {
            BufferedImage buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = buffer.createGraphics();

            // Enable antialiasing
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // Draw background map
            if (mapImage != null) {
                g2d.drawImage(mapImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                // Fallback if image fails to load
                g2d.setColor(new Color(240, 230, 200)); // Sandy color
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }

            // Draw trail paths
            drawTrailPaths(g2d, gameMap);

            // Draw landmarks
            drawLandmarks(g2d);

            g2d.dispose();
            return buffer;
        }

        /**
         * Draws the trail paths on the map
         */
        private void drawTrailPaths(Graphics2D g2d, Map gameMap) {
            if (gameMap == null || landmarkPositions.isEmpty()) return;

            // Get trail choice
            int trailChoice = 1; // Default to Oregon Trail
            if (gameController.getTrail() != null) {
                if (gameController.getTrail().equals("California")) {
                    trailChoice = 2;
                } else if (gameController.getTrail().equals("Mormon")) {
                    trailChoice = 3;
                }
            }

            // Get ordered list of landmarks
            List<Landmark> landmarks = gameMap.getLandmarks();
            if (landmarks.isEmpty()) return;

            // Set up for trail drawing
            g2d.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            // Draw trail path
            Point prevPoint = null;
            for (Landmark landmark : landmarks) {
                Point currentPoint = landmarkPositions.get(landmark.getName());
                if (currentPoint != null) {
                    if (prevPoint != null) {
                        // Determine if this segment has been traveled
                        boolean segmentCompleted = landmark.getDistance() <= distanceTraveled;
                        g2d.setColor(segmentCompleted ? COMPLETED_TRAIL_COLOR : TRAIL_COLOR);

                        // Draw trail segment
                        g2d.drawLine(prevPoint.x, prevPoint.y, currentPoint.x, currentPoint.y);
                    }
                    prevPoint = currentPoint;
                }
            }

            // Draw progress indicator
            totalDistance = landmarks.get(landmarks.size()-1).getDistance();
            int distanceTraveled = gameMap.getDistanceTraveled();

            float progressRatio = totalDistance > 0 ?  (float) distanceTraveled / (float) totalDistance : 0f;
            progressRatio = Math.min(progressRatio, 1.0f);

            int progressBarWidth = 200;
            int progressBarHeight = 20;
            int x = getWidth() - progressBarWidth - 20;
            int y = getHeight() - progressBarHeight - 20;

            // Draw progress bar background
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.fillRect(x-2, y-2, progressBarWidth+4, progressBarHeight+4);

            // Draw progress bar
            g2d.setColor(new Color(255, 255, 255, 180));
            g2d.fillRect(x, y, progressBarWidth, progressBarHeight);

            // Draw progress
            g2d.setColor(new Color(0, 128, 0, 220));
            g2d.fillRect(x, y, (int)(progressBarWidth * progressRatio), progressBarHeight);

            // Draw progress text
            g2d.setColor(Color.BLACK);
            g2d.setFont(FontManager.getBoldWesternFont(12f));
            String progressText = distanceTraveled + " / " + totalDistance + " miles";
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(progressText,
                    x + (progressBarWidth - fm.stringWidth(progressText))/2,
                    y + progressBarHeight/2 + fm.getAscent()/2);

            // Draw trail name
            String trailName = "";
            switch (trailChoice) {
                case 1: trailName = "Oregon Trail"; break;
                case 2: trailName = "California Trail"; break;
                case 3: trailName = "Mormon Trail"; break;
            }

            g2d.setFont(FontManager.getBoldWesternFont(16f));
            g2d.setColor(new Color(50, 30, 10));
            g2d.drawString(trailName, 20, 30);
        }

        /**
         * Draws landmarks on the map
         */
        private void drawLandmarks(Graphics2D g2d) {
            int index = 0;

            for (Entry<String, Point> entry : landmarkPositions.entrySet()) {
                String landmarkName = entry.getKey();
                Point position = entry.getValue();
                boolean isCurrentLocation = landmarkName.equals(currentLocation);

                // Draw landmark
                if (isCurrentLocation) {
                    drawWagonIcon(g2d, position.x, position.y);
                    g2d.setColor(new Color(255, 215, 0, 150));
                    g2d.fillOval(position.x - 17, position.y - 17, 34, 34);
                } else {
                    g2d.setColor(new Color(120, 60, 0));
                    g2d.fillOval(position.x - 5, position.y - 5, 10, 10);
                    g2d.setColor(new Color(0, 0, 0, 100));
                    g2d.drawOval(position.x - 5, position.y - 5, 10, 10);
                }

                drawLandmarkName(g2d, landmarkName, position, isCurrentLocation, index);
                index++;
            }
        }

        /**
         * Draws landmark name with improved readability
         */
        private void drawLandmarkName(Graphics2D g2d, String landmarkName, Point position, boolean isCurrentLocation, int index) {
            g2d.setFont(MAP_FONT);
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(landmarkName);
            int textHeight = fm.getHeight();
            int ascent = fm.getAscent();

            int textX = position.x - textWidth / 2;

            // Alternate: even = above, odd = below
            int labelOffset = 25;
            int textY = (index % 2 == 0)
                    ? position.y - labelOffset
                    : position.y + labelOffset;

            // Draw dotted line from text to dot
            Stroke oldStroke = g2d.getStroke();
            g2d.setColor(Color.GRAY);
            g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{2}, 0));
            g2d.drawLine(position.x, position.y, position.x, textY - (index % 2 == 0 ? 2 : -textHeight + 2));
            g2d.setStroke(oldStroke);

            // Text shadow
            g2d.setColor(LANDMARK_TEXT_SHADOW);
            g2d.drawString(landmarkName, textX + 1, textY + 1);

            // Main text
            g2d.setColor(isCurrentLocation ? new Color(139, 0, 0) : TEXT_COLOR);
            g2d.drawString(landmarkName, textX, textY);
        }

        /**
         * Draws a wagon icon at the current location
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

            // Padding to avoid edge placement
            int paddingX = mapWidth / 10;
            int paddingY = mapHeight / 10;
            int usableWidth = mapWidth - (2 * paddingX);
            int usableHeight = mapHeight - (2 * paddingY);

            // Get landmarks
            List<Landmark> landmarks = gameMap.getLandmarks();
            if (landmarks.isEmpty()) return;

            totalDistance = landmarks.get(landmarks.size()-1).getDistance();

            // Create control points for each trail path (for more realistic curves)
            List<Point> controlPoints = createTrailControlPoints(trailChoice, paddingX, paddingY, usableWidth, usableHeight);
            int numSegments = controlPoints.size() - 1;

            for (Landmark landmark : landmarks) {
                String name = landmark.getName();
                int distance = landmark.getDistance();

                // Calculate position based on trail and distance
                float progress = (float)distance / totalDistance;

                // Find which segment of the path this landmark falls on
                int segmentIndex = (int)(progress * numSegments);
                if (segmentIndex >= numSegments) segmentIndex = numSegments - 1;

                // Calculate position within segment
                float segmentProgress = (progress * numSegments) - segmentIndex;

                Point start = controlPoints.get(segmentIndex);
                Point end = controlPoints.get(segmentIndex + 1);

                // Linear interpolation between points
                int x = (int)(start.x + (end.x - start.x) * segmentProgress);
                int y = (int)(start.y + (end.y - start.y) * segmentProgress);

                landmarkPositions.put(name, new Point(x, y));
            }
        }

        /**
         * Creates control points for trail paths
         */
        private List<Point> createTrailControlPoints(int trailChoice, int paddingX, int paddingY, int usableWidth, int usableHeight) {
            List<Point> points = new ArrayList<>();
            int mapWidth = getWidth();
            int mapHeight = getHeight();

            // Starting point (Independence, Missouri)
            Point start = new Point(paddingX, mapHeight / 2);
            points.add(start);

            switch (trailChoice) {
                case 1: // Oregon Trail - curves northwest
                    points.add(new Point(paddingX + (usableWidth / 4), mapHeight / 2 - (usableHeight / 12)));
                    points.add(new Point(paddingX + (usableWidth / 2), mapHeight / 2 - (usableHeight / 6)));
                    points.add(new Point(paddingX + (int)(usableWidth * 0.75), mapHeight / 2 - (usableHeight / 4)));
                    points.add(new Point(mapWidth - paddingX, paddingY + (usableHeight / 3))); // Oregon end point
                    break;

                case 2: // California Trail - splits and goes southwest
                    points.add(new Point(paddingX + (usableWidth / 4), mapHeight / 2 - (usableHeight / 12)));
                    points.add(new Point(paddingX + (usableWidth / 2), mapHeight / 2 - (usableHeight / 8)));
                    points.add(new Point(paddingX + (int)(usableWidth * 0.6), mapHeight / 2)); // Split point
                    points.add(new Point(paddingX + (int)(usableWidth * 0.75), mapHeight / 2 + (usableHeight / 6)));
                    points.add(new Point(mapWidth - paddingX, mapHeight - paddingY - (usableHeight / 3))); // California end point
                    break;

                case 3: // Mormon Trail - stays more central
                    points.add(new Point(paddingX + (usableWidth / 4), mapHeight / 2 - (usableHeight / 20)));
                    points.add(new Point(paddingX + (usableWidth / 2), mapHeight / 2));
                    points.add(new Point(paddingX + (int)(usableWidth * 0.75), mapHeight / 2 + (usableHeight / 20)));
                    points.add(new Point(mapWidth - paddingX - (usableWidth / 4), mapHeight / 2)); // Utah end point
                    break;
            }

            return points;
        }

        /**
         * Checks if mouse is hovering over a landmark
         */
        private void checkLandmarkHover(Point mousePoint) {
            showTooltip = false;

            for (Entry<String, Point> entry : landmarkPositions.entrySet()) {
                Point landmarkPos = entry.getValue();
                // Check if mouse is within 15 pixels of landmark
                if (mousePoint.distance(landmarkPos) <= 15) {
                    showTooltip = true;
                    tooltipText = entry.getKey();
                    tooltipPosition = landmarkPos;

                    // Get additional landmark info if available
                    Map gameMap = gameController.getMap();
                    if (gameMap != null) {
                        List<Landmark> landmarks = gameMap.getLandmarks();
                        for (Landmark landmark : landmarks) {
                            if (landmark.getName().equals(entry.getKey())) {
                                tooltipText += " (" + map.getDistanceTraveled() + " miles)";
                                // Add landmark info if it has a description method
                                if (landmark.getDescription() != null && !landmark.getDescription().isEmpty()) {
                                    tooltipText += "\n" + landmark.getDescription();
                                }
                                break;
                            }
                        }
                    }

                    repaint();
                    break;
                }
            }

            if (!showTooltip) {
                repaint();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // Create buffered image if needed
            if (bufferedMap == null || bufferedMap.getWidth() != getWidth() || bufferedMap.getHeight() != getHeight()) {
                if (getWidth() > 0 && getHeight() > 0) {
                    Map gameMap = gameController.getMap();
                    if (gameMap != null) {
                        calculateLandmarkPositions(gameMap);
                        bufferedMap = createBufferedMapImage(gameMap);
                    }
                }
            }

            // Draw the buffered map
            if (bufferedMap != null) {
                g2d.drawImage(bufferedMap, 0, 0, this);
            }

            // Draw tooltip if needed
            if (showTooltip) {
                drawTooltip(g2d, tooltipText, tooltipPosition);
            }
        }

        /**
         * Draws a tooltip with landmark information
         */
        private void drawTooltip(Graphics2D g2d, String text, Point position) {
            // Enable antialiasing
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // Prepare tooltip text
            String[] lines = text.split("\n");
            g2d.setFont(FontManager.getWesternFont(12f));
            FontMetrics fm = g2d.getFontMetrics();

            // Calculate tooltip dimensions
            int maxWidth = 0;
            for (String line : lines) {
                int width = fm.stringWidth(line);
                if (width > maxWidth) maxWidth = width;
            }

            int padding = 8;
            int tooltipWidth = maxWidth + (padding * 2);
            int lineHeight = fm.getHeight();
            int tooltipHeight = (lineHeight * lines.length) + (padding * 2);

            // Calculate tooltip position, ensuring it stays within the map bounds
            int x = position.x + 20;
            int y = position.y - 10;

            // Adjust if tooltip would go off the edge
            if (x + tooltipWidth > getWidth()) {
                x = position.x - tooltipWidth - 10;
            }
            if (y + tooltipHeight > getHeight()) {
                y = position.y - tooltipHeight - 10;
            }

            // Draw tooltip background with shadow
            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.fillRoundRect(x+2, y+2, tooltipWidth, tooltipHeight, 10, 10);

            g2d.setColor(new Color(250, 240, 220, 240));
            g2d.fillRoundRect(x, y, tooltipWidth, tooltipHeight, 10, 10);

            g2d.setColor(new Color(139, 69, 19));
            g2d.drawRoundRect(x, y, tooltipWidth, tooltipHeight, 10, 10);

            // Draw text
            g2d.setColor(Color.BLACK);
            int textY = y + padding + fm.getAscent();

            for (String line : lines) {
                g2d.drawString(line, x + padding, textY);
                textY += lineHeight;
            }
        }
    }
} 