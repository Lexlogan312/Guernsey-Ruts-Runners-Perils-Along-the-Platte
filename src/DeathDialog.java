import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Dialog shown when the player dies
 */
public class DeathDialog extends JDialog {
    
    // Colors - match EnhancedGUI
    private final Color BACKGROUND_COLOR = new Color(240, 220, 180); // Parchment/sepia
    private final Color PANEL_COLOR = new Color(200, 170, 130);      // Darker parchment
    private final Color TEXT_COLOR = new Color(80, 30, 0);           // Dark brown
    private final Color HEADER_COLOR = new Color(120, 60, 0);        // Medium brown
    private final Color ACCENT_COLOR = new Color(160, 100, 40);      // Light brown
    
    /**
     * Constructor
     * @param owner The parent frame
     * @param causeOfDeath What killed the player
     * @param days Number of days the journey took
     * @param distance Distance traveled
     * @param lastLocation Last known location
     */
    public DeathDialog(Frame owner, String causeOfDeath, int days, int distance, String lastLocation) {
        super(owner, "Game Over", true);
        
        initUI(causeOfDeath, days, distance, lastLocation);
        pack();
        setLocationRelativeTo(owner);
        setSize(700, 500);
        setResizable(false);
    }
    
    private void initUI(String causeOfDeath, int days, int distance, String lastLocation) {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);
        titlePanel.setBorder(new EmptyBorder(10, 10, 5, 10));
        
        JLabel titleLabel = new JLabel("Your Journey Has Ended", JLabel.CENTER);
        titleLabel.setFont(FontManager.WESTERN_FONT_TITLE);
        titleLabel.setForeground(new Color(139, 0, 0)); // Dark red for death
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(PANEL_COLOR);
        contentPanel.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Death image (tombstone or similar)
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(PANEL_COLOR);
        imagePanel.setPreferredSize(new Dimension(120, 150));
        
        // Create a simple tombstone graphic
        JPanel tombstonePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw tombstone
                int width = getWidth();
                int height = getHeight();
                
                // Tombstone shape
                g2d.setColor(Color.GRAY);
                int stoneWidth = width - 20;
                int stoneHeight = height - 30;
                int arcWidth = 60;
                
                g2d.fillRoundRect(10, 10, stoneWidth, stoneHeight, arcWidth, arcWidth);
                
                // Border
                g2d.setColor(Color.DARK_GRAY);
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawRoundRect(10, 10, stoneWidth, stoneHeight, arcWidth, arcWidth);
                
                // RIP text
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Serif", Font.BOLD, 24));
                FontMetrics fm = g2d.getFontMetrics();
                String rip = "R.I.P.";
                int textWidth = fm.stringWidth(rip);
                g2d.drawString(rip, (width - textWidth) / 2, height / 3);
                
                // Date
                g2d.setFont(new Font("Serif", Font.PLAIN, 14));
                fm = g2d.getFontMetrics();
                String dateText = "1848";
                textWidth = fm.stringWidth(dateText);
                g2d.drawString(dateText, (width - textWidth) / 2, height / 2 + 10);
            }
        };
        tombstonePanel.setBackground(PANEL_COLOR);
        
        imagePanel.add(tombstonePanel, BorderLayout.CENTER);
        contentPanel.add(imagePanel, BorderLayout.WEST);
        
        // Description text
        JPanel textPanel = new JPanel(new BorderLayout(0, 15));
        textPanel.setBackground(PANEL_COLOR);
        
        // Cause of death
        JLabel deathCauseLabel = new JLabel("You have died of " + causeOfDeath + ".");
        deathCauseLabel.setFont(FontManager.getBoldWesternFont(20));
        deathCauseLabel.setForeground(new Color(139, 0, 0)); // Dark red
        
        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        statsPanel.setBackground(PANEL_COLOR);
        
        JLabel daysLabel = new JLabel("Days on trail: " + days);
        daysLabel.setFont(FontManager.getWesternFont(16));
        daysLabel.setForeground(TEXT_COLOR);
        
        JLabel distanceLabel = new JLabel("Distance traveled: " + distance + " miles");
        distanceLabel.setFont(FontManager.getWesternFont(16));
        distanceLabel.setForeground(TEXT_COLOR);
        
        JLabel locationLabel = new JLabel("Last known location: near " + lastLocation);
        locationLabel.setFont(FontManager.getWesternFont(16));
        locationLabel.setForeground(TEXT_COLOR);
        
        statsPanel.add(daysLabel);
        statsPanel.add(distanceLabel);
        statsPanel.add(locationLabel);
        
        // Quote
        JTextArea quoteArea = new JTextArea(
            "\"Remember, always face danger with courage, and never forget those who traveled before you.\"\n\n" +
            "Your journey may have ended, but your legacy lives on."
        );
        quoteArea.setFont(FontManager.getWesternFont(14));
        quoteArea.setLineWrap(true);
        quoteArea.setWrapStyleWord(true);
        quoteArea.setEditable(false);
        quoteArea.setBackground(PANEL_COLOR);
        quoteArea.setForeground(TEXT_COLOR);
        
        textPanel.add(deathCauseLabel, BorderLayout.NORTH);
        textPanel.add(statsPanel, BorderLayout.CENTER);
        textPanel.add(quoteArea, BorderLayout.SOUTH);
        
        contentPanel.add(textPanel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        JButton closeButton = new JButton("Close");
        closeButton.setFont(FontManager.getBoldWesternFont(16));
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