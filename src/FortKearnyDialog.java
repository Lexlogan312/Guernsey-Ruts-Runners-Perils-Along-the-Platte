import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Dialog to prompt player to begin journey to Fort Kearny
 */
public class FortKearnyDialog extends JDialog {
    private GameController gameController;
    
    // Colors - match EnhancedGUI
    private final Color BACKGROUND_COLOR = new Color(240, 220, 180); // Parchment/sepia
    private final Color PANEL_COLOR = new Color(200, 170, 130);      // Darker parchment
    private final Color TEXT_COLOR = new Color(80, 30, 0);           // Dark brown
    private final Color HEADER_COLOR = new Color(120, 60, 0);        // Medium brown
    private final Color ACCENT_COLOR = new Color(160, 100, 40);      // Light brown
    
    /**
     * Constructor
     */
    public FortKearnyDialog(Frame owner, GameController gameController) {
        super(owner, "Begin Your Journey", true);
        this.gameController = gameController;
        
        initUI();
        pack();
        setLocationRelativeTo(owner);
        setSize(800, 500);
        setResizable(false);
    }
    
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);
        titlePanel.setBorder(new EmptyBorder(10, 10, 5, 10));
        
        JLabel titleLabel = new JLabel("Begin Your Journey West", JLabel.CENTER);
        titleLabel.setFont(FontManager.WESTERN_FONT_TITLE);
        titleLabel.setForeground(TEXT_COLOR);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout(15, 10));
        contentPanel.setBackground(PANEL_COLOR);
        contentPanel.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Add map image
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(PANEL_COLOR);
        try {
            // Try multiple methods to load the map image
            ImageIcon mapIcon = null;
            
            // Method 1: Direct resource path
            try {
                mapIcon = new ImageIcon(getClass().getResource("/resources/images/Oregon Trail Blank Map.png"));
                System.out.println("FortKearnyDialog: Loaded map using resource path");
            } catch (Exception e) {
                System.err.println("FortKearnyDialog: Failed to load map using resource path: " + e);
            }
            
            // Method 2: File path
            if (mapIcon == null || mapIcon.getIconWidth() <= 0) {
                try {
                    mapIcon = new ImageIcon("resources/images/Oregon Trail Blank Map.png");
                    System.out.println("FortKearnyDialog: Loaded map using file path");
                } catch (Exception e) {
                    System.err.println("FortKearnyDialog: Failed to load map using file path: " + e);
                }
            }
            
            // Method 3: Absolute path
            if (mapIcon == null || mapIcon.getIconWidth() <= 0) {
                try {
                    String mapPath = System.getProperty("user.dir") + "/resources/images/Oregon Trail Blank Map.png";
                    mapIcon = new ImageIcon(mapPath);
                    System.out.println("FortKearnyDialog: Loaded map using absolute path: " + mapPath);
                } catch (Exception e) {
                    System.err.println("FortKearnyDialog: Failed to load map using absolute path: " + e);
                }
            }
            
            if (mapIcon != null && mapIcon.getIconWidth() > 0) {
                // Calculate appropriate size while preserving aspect ratio
                int originalWidth = mapIcon.getIconWidth();
                int originalHeight = mapIcon.getIconHeight();
                float aspectRatio = (float)originalWidth / (float)originalHeight;
                
                int displayWidth = 300;
                int displayHeight = (int)(displayWidth / aspectRatio);
                
                Image mapImage = mapIcon.getImage().getScaledInstance(displayWidth, displayHeight, Image.SCALE_SMOOTH);
                JLabel mapLabel = new JLabel(new ImageIcon(mapImage));
                mapLabel.setBorder(new LineBorder(ACCENT_COLOR, 1));
                imagePanel.add(mapLabel, BorderLayout.CENTER);
            } else {
                JLabel noMapLabel = new JLabel("Map image could not be loaded");
                noMapLabel.setFont(FontManager.getBoldWesternFont(14));
                noMapLabel.setForeground(TEXT_COLOR);
                noMapLabel.setHorizontalAlignment(JLabel.CENTER);
                imagePanel.add(noMapLabel, BorderLayout.CENTER);
            }
        } catch (Exception e) {
            System.err.println("Error loading map image: " + e.getMessage());
            e.printStackTrace();
        }
        
        contentPanel.add(imagePanel, BorderLayout.WEST);
        
        // Description text
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setFont(FontManager.getWesternFont(15));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(PANEL_COLOR);
        descriptionArea.setForeground(TEXT_COLOR);
        
        // Get trail and departure point based on trail choice
        String trail = gameController.getTrail();
        String departurePoint = "Independence, Missouri";
        if (trail.equals("Mormon")) {
            departurePoint = "Nauvoo, Illinois";
        }
        
        descriptionArea.setText(
            "You're about to embark on the " + trail + " Trail from " + departurePoint + ".\n\n" +
            "Your first major destination is Fort Kearny, a military post established to protect " +
            "travelers on the trails west. The journey there will take several weeks, " +
            "depending on weather and trail conditions.\n\n" +
            "During this time, you may encounter various challenges, from weather events to " +
            "health problems and wagon breakdowns. Your supplies and decision-making will " +
            "determine how well you fare on this initial leg of your journey.\n\n" +
            "When you're ready, click 'Begin Journey' to fast-forward to Fort Kearny."
        );
        
        // Add some padding around the text area
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBackground(PANEL_COLOR);
        textPanel.setBorder(new EmptyBorder(0, 15, 0, 0));
        textPanel.add(descriptionArea, BorderLayout.CENTER);
        
        contentPanel.add(textPanel, BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        JButton startButton = new JButton("Begin Journey");
        startButton.setFont(FontManager.getBoldWesternFont(16));
        startButton.setForeground(TEXT_COLOR);
        startButton.setBackground(PANEL_COLOR);
        startButton.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(8, 25, 8, 25)
        ));
        
        startButton.addActionListener(e -> {
            dispose(); // Close this dialog
        });
        
        buttonPanel.add(startButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
} 