import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * FortKearnyDialog Class of the Perils Along the Platte Game
 * A dialog that introduces the player to their journey and provides historical context
 * about Fort Kearny, the first major destination on the trail.
 *
 *
 * @author Alex Randall and Chase McCluskey
 * @version 1.0
 * @date 05/06/2025
 * @file FortKearnyDialog.java
 */
public class FortKearnyDialog extends JDialog {
    //The game controller instance for managing game state.
    private final GameController gameController;

    private final Color BACKGROUND_COLOR = new Color(240, 220, 180); // Parchment/sepia

    private final Color PANEL_COLOR = new Color(200, 170, 130);      // Darker parchment

    private final Color TEXT_COLOR = new Color(80, 30, 0);           // Dark brown

    private final Color ACCENT_COLOR = new Color(160, 100, 40);      // Light brown

    /**
     * Constructs a new FortKearnyDialog.
     * Initializes the dialog with a fixed size and modal behavior.
     * Sets up the UI components and historical content.
     * 
     * @param owner The parent frame for modal dialog positioning
     * @param gameController The game controller instance for accessing game state
     */
    public FortKearnyDialog(Frame owner, GameController gameController) {
        super(owner, "Begin Your Journey", true);
        this.gameController = gameController;

        initUI();
        pack(); // Pack first to get preferred sizes
        // Set size *after* packing, increasing height
        setSize(800, 750); // Increased height from 600 to 700 to accommodate both images
        setLocationRelativeTo(owner);
        setResizable(false); // Keep this dialog non-resizable for simplicity
    }

    /**
     * Initializes the dialog's user interface components.
     * Creates and arranges panels for:
     * - Title panel with journey title
     * - Content panel with images and description
     * - Trail overlook image (scaled to fit)
     * - Wagon icon (scaled to fit)
     * - Historical description text (scrollable)
     * - Begin Journey button
     */
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

        // Create a panel for both images arranged vertically
        JPanel imagesPanel = new JPanel(new BorderLayout(0, 10));
        imagesPanel.setBackground(PANEL_COLOR);
        
        // Add trail overlook image at the top
        JPanel overlookPanel = new JPanel(new BorderLayout());
        overlookPanel.setBackground(PANEL_COLOR);
        try {
            ImageIcon overlookIcon = ResourceLoader.loadImage("images/Overlook of Trail.png");
            if (overlookIcon != null && overlookIcon.getIconWidth() > 0) {
                // Calculate appropriate size while preserving aspect ratio
                int originalWidth = overlookIcon.getIconWidth();
                int originalHeight = overlookIcon.getIconHeight();
                float aspectRatio = (float)originalWidth / (float)originalHeight;

                int displayWidth = 300;
                int displayHeight = (int)(displayWidth / aspectRatio);

                Image overlookImage = overlookIcon.getImage().getScaledInstance(displayWidth, displayHeight, Image.SCALE_SMOOTH);
                JLabel overlookLabel = new JLabel(new ImageIcon(overlookImage));
                overlookPanel.add(overlookLabel, BorderLayout.CENTER);
                
                System.out.println("FortKearnyDialog: Loaded overlook image using ResourceLoader");
            } else {
                JLabel noOverlookLabel = new JLabel("Overlook image could not be loaded");
                noOverlookLabel.setFont(FontManager.getBoldWesternFont(14));
                noOverlookLabel.setForeground(TEXT_COLOR);
                noOverlookLabel.setHorizontalAlignment(JLabel.CENTER);
                overlookPanel.add(noOverlookLabel, BorderLayout.CENTER);
            }
        } catch (Exception e) {
            System.err.println("Error loading overlook image: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Add wagon image at the bottom
        JPanel wagonPanel = new JPanel(new BorderLayout());
        wagonPanel.setBackground(PANEL_COLOR);
        try {
            ImageIcon wagonIcon = ResourceLoader.loadImage("images/Wagon Icon.png");
            if (wagonIcon != null && wagonIcon.getIconWidth() > 0) {
                // Calculate appropriate size while preserving aspect ratio
                int originalWidth = wagonIcon.getIconWidth();
                int originalHeight = wagonIcon.getIconHeight();
                float aspectRatio = (float)originalWidth / (float)originalHeight;

                int displayWidth = 300;
                int displayHeight = (int)(displayWidth / aspectRatio);

                Image wagonImage = wagonIcon.getImage().getScaledInstance(displayWidth, displayHeight, Image.SCALE_SMOOTH);
                JLabel wagonLabel = new JLabel(new ImageIcon(wagonImage));
                wagonPanel.add(wagonLabel, BorderLayout.CENTER);
                
                System.out.println("FortKearnyDialog: Loaded wagon icon using ResourceLoader");
            } else {
                JLabel noWagonLabel = new JLabel("Wagon image could not be loaded");
                noWagonLabel.setFont(FontManager.getBoldWesternFont(14));
                noWagonLabel.setForeground(TEXT_COLOR);
                noWagonLabel.setHorizontalAlignment(JLabel.CENTER);
                wagonPanel.add(noWagonLabel, BorderLayout.CENTER);
            }
        } catch (Exception e) {
            System.err.println("Error loading wagon image: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Add both image panels to the vertical layout
        imagesPanel.add(overlookPanel, BorderLayout.NORTH);
        imagesPanel.add(wagonPanel, BorderLayout.CENTER);

        contentPanel.add(imagesPanel, BorderLayout.WEST);

        // Description text with historical context in a scrollable pane
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setFont(FontManager.getWesternFont(15));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(PANEL_COLOR);
        descriptionArea.setForeground(TEXT_COLOR);

        // Get trail and departure point based on trail choice
        String trail = gameController.getTrail() != null ? gameController.getTrail() : "Oregon"; // Default if null
        String departurePoint = "Independence, Missouri";
        if ("Mormon".equals(trail)) { // Use equals for string comparison
            departurePoint = "Nauvoo, Illinois";
        }

        descriptionArea.setText(
                "You're about to embark on the " + trail + " Trail from " + departurePoint + ".\n\n" +
                        "Your first major destination is Fort Kearny, a military post established to protect " +
                        "travelers on the trails west. The journey there will take several weeks, " +
                        "depending on weather and trail conditions.\n\n" +
                        
                        "Fort Kearny was established in 1848 at the junction of the Oregon and Mormon Trails " +
                        "along the Platte River in what is now Nebraska. Named after General Stephen Watts Kearny, " +
                        "it was one of the most important outposts on the westward trails.\n\n" +
                        
                        "This initial leg of the journey was crucial - many emigrants who set out unprepared or " +
                        "experienced early difficulties abandoned their journey at Fort Kearny and returned home.\n\n" +
                        
                        "During this time, you may encounter various challenges, from weather events to " +
                        "health problems and wagon breakdowns. Your supplies and decision-making will " +
                        "determine how well you fare on this initial leg of your journey.\n\n" +
                        
                        "When you're ready, click 'Begin Journey' to fast-forward to Fort Kearny."
        );

        // Create a scrollable view for the text
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setBorder(null); // Remove border
        scrollPane.setBackground(PANEL_COLOR);
        scrollPane.getViewport().setBackground(PANEL_COLOR);
        
        // Add some padding around the text area
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBackground(PANEL_COLOR);
        textPanel.setBorder(new EmptyBorder(0, 15, 0, 0));
        textPanel.add(scrollPane, BorderLayout.CENTER);

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
