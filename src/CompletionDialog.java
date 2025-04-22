import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Dialog shown when the player successfully completes their journey
 */
public class CompletionDialog extends JDialog {
    
    // Colors - match EnhancedGUI
    private final Color BACKGROUND_COLOR = new Color(240, 220, 180); // Parchment/sepia
    private final Color PANEL_COLOR = new Color(200, 170, 130);      // Darker parchment
    private final Color TEXT_COLOR = new Color(80, 30, 0);           // Dark brown
    private final Color HEADER_COLOR = new Color(120, 60, 0);        // Medium brown
    private final Color ACCENT_COLOR = new Color(160, 100, 40);      // Light brown
    private final Color SUCCESS_COLOR = new Color(0, 100, 0);        // Dark green for success
    
    /**
     * Constructor
     * @param owner The parent frame
     * @param destination Final destination reached
     * @param days Number of days the journey took
     * @param distance Distance traveled
     * @param finalDate Final date when journey completed
     */
    public CompletionDialog(Frame owner, String destination, int days, int distance, String finalDate) {
        super(owner, "Journey Complete", true);
        
        initUI(destination, days, distance, finalDate);
        pack();
        setLocationRelativeTo(owner);
        setSize(700, 500);
        setResizable(false);
    }
    
    private void initUI(String destination, int days, int distance, String finalDate) {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);
        titlePanel.setBorder(new EmptyBorder(10, 10, 5, 10));
        
        JLabel titleLabel = new JLabel("Journey Complete!", JLabel.CENTER);
        titleLabel.setFont(FontManager.WESTERN_FONT_TITLE);
        titleLabel.setForeground(SUCCESS_COLOR);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(PANEL_COLOR);
        contentPanel.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Success image - simulated with a panel that draws a wagon
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(PANEL_COLOR);
        imagePanel.setPreferredSize(new Dimension(150, 150));
        
        // Create a sunset scene with wagon
        JPanel successPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                
                // Sky gradient (sunset)
                GradientPaint skyGradient = new GradientPaint(
                    0, 0, new Color(255, 200, 100),
                    0, height/2, new Color(255, 100, 50)
                );
                g2d.setPaint(skyGradient);
                g2d.fillRect(0, 0, width, height/2);
                
                // Ground
                g2d.setColor(new Color(120, 100, 40));
                g2d.fillRect(0, height/2, width, height/2);
                
                // Sun
                g2d.setColor(new Color(255, 200, 0));
                g2d.fillOval(width/2 - 20, height/4 - 20, 40, 40);
                
                // Mountain silhouettes
                g2d.setColor(new Color(80, 60, 30));
                int[] xPoints = {0, width/4, width/2, 3*width/4, width};
                int[] yPoints = {height/2, height/3, height/2 - 20, height/3 + 10, height/2};
                g2d.fillPolygon(xPoints, yPoints, 5);
                
                // Draw a simple wagon
                int wagonWidth = 60;
                int wagonHeight = 30;
                int wagonX = width/2 - wagonWidth/2;
                int wagonY = height/2 - wagonHeight - 5;
                
                // Wagon body
                g2d.setColor(new Color(139, 69, 19));
                g2d.fillRect(wagonX, wagonY, wagonWidth, wagonHeight);
                
                // Wagon cover
                g2d.setColor(new Color(210, 180, 140));
                g2d.fillArc(wagonX - 5, wagonY - 20, wagonWidth + 10, 50, 0, 180);
                
                // Wheels
                g2d.setColor(new Color(101, 67, 33));
                g2d.fillOval(wagonX + 5, wagonY + wagonHeight - 10, 20, 20);
                g2d.fillOval(wagonX + wagonWidth - 25, wagonY + wagonHeight - 10, 20, 20);
                g2d.setColor(Color.BLACK);
                g2d.drawOval(wagonX + 5, wagonY + wagonHeight - 10, 20, 20);
                g2d.drawOval(wagonX + wagonWidth - 25, wagonY + wagonHeight - 10, 20, 20);
            }
        };
        successPanel.setBackground(PANEL_COLOR);
        
        imagePanel.add(successPanel, BorderLayout.CENTER);
        contentPanel.add(imagePanel, BorderLayout.WEST);
        
        // Description text
        JPanel textPanel = new JPanel(new BorderLayout(0, 15));
        textPanel.setBackground(PANEL_COLOR);
        
        // Success message
        JLabel successLabel = new JLabel("You have reached " + destination + "!");
        successLabel.setFont(FontManager.getBoldWesternFont(20));
        successLabel.setForeground(SUCCESS_COLOR);
        
        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        statsPanel.setBackground(PANEL_COLOR);
        
        JLabel daysLabel = new JLabel("Journey length: " + days + " days");
        daysLabel.setFont(FontManager.getWesternFont(16));
        daysLabel.setForeground(TEXT_COLOR);
        
        JLabel distanceLabel = new JLabel("Distance traveled: " + distance + " miles");
        distanceLabel.setFont(FontManager.getWesternFont(16));
        distanceLabel.setForeground(TEXT_COLOR);
        
        JLabel dateLabel = new JLabel("Arrival date: " + finalDate);
        dateLabel.setFont(FontManager.getWesternFont(16));
        dateLabel.setForeground(TEXT_COLOR);
        
        statsPanel.add(daysLabel);
        statsPanel.add(distanceLabel);
        statsPanel.add(dateLabel);
        
        // Congratulatory message
        JTextArea congratsArea = new JTextArea(
            "You've proven yourself worthy of the frontier spirit! Your courage and " +
            "determination have carried you through countless hardships to reach your destination.\n\n" +
            "A new life awaits you and your family in this land of opportunity."
        );
        congratsArea.setFont(FontManager.getWesternFont(14));
        congratsArea.setLineWrap(true);
        congratsArea.setWrapStyleWord(true);
        congratsArea.setEditable(false);
        congratsArea.setBackground(PANEL_COLOR);
        congratsArea.setForeground(TEXT_COLOR);
        
        textPanel.add(successLabel, BorderLayout.NORTH);
        textPanel.add(statsPanel, BorderLayout.CENTER);
        textPanel.add(congratsArea, BorderLayout.SOUTH);
        
        contentPanel.add(textPanel, BorderLayout.CENTER);
        add(contentPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        JButton closeButton = new JButton("Celebrate");
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