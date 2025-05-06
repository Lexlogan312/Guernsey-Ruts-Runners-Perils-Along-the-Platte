/**
 * CompletionDialog Class of the Perils Along the Platte Game
 * A dialog that appears when the player successfully completes their journey west.
 * Displays:
 * - Journey completion message
 * - Travel statistics (days, distance, arrival date)
 * - Historical context about life after the journey
 *
 * @author Alex Randall and Chase McCluskey
 * @version 1.0
 * @date 05/06/2025
 * @file CompletionDialog.java
 */

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class CompletionDialog extends JDialog {
    
    // UI Colors for western theme
    private final Color BACKGROUND_COLOR = new Color(240, 220, 180); // Parchment/sepia background
    private final Color PANEL_COLOR = new Color(200, 170, 130);      // Darker parchment for panels
    private final Color TEXT_COLOR = new Color(80, 30, 0);           // Dark brown for text
    private final Color ACCENT_COLOR = new Color(160, 100, 40);      // Light brown for borders
    private final Color SUCCESS_COLOR = new Color(0, 100, 0);        // Dark green for success messages
    
    // Path to custom victory image
    private static final String CUSTOM_IMAGE_PATH = "images/Completion Screen.png";
    
    /**
     * Constructs a new CompletionDialog to show journey completion details.
     * Initializes the dialog with a fixed size and modal behavior.
     * 
     * @param owner The parent frame for modal dialog positioning
     * @param destination The final destination reached by the player
     * @param days The number of days the journey took
     * @param distance The total distance traveled in miles
     * @param finalDate The date when the journey was completed
     */
    public CompletionDialog(Frame owner, String destination, int days, int distance, String finalDate) {
        super(owner, "Journey Complete", true);
        
        initUI(destination, days, distance, finalDate);
        pack();
        setLocationRelativeTo(owner);
        setSize(700, 750);
        setResizable(false);
    }
    
    /**
     * Initializes the dialog's user interface components.
     * Creates and arranges panels for:
     * - Title display with success message
     * - Journey statistics (days, distance, date)
     * - Historical context about pioneer life
     *
     * @param destination The final destination reached
     * @param days The number of days traveled
     * @param distance The distance covered in miles
     * @param finalDate The arrival date
     */
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
        
        // Create the text panel first
        JPanel textPanel = new JPanel(new BorderLayout(0, 15));
        textPanel.setBackground(PANEL_COLOR);
        
        // Success message
        JLabel successLabel = new JLabel("You have reached " + destination + "!", JLabel.CENTER);
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
        
        // Historical context for the arrival
        JTextArea congratsArea = new JTextArea(
            "A new life awaits you and your family in this land of opportunity.\n\n" +
            "Historical Note: Those who completed the journey west often faced new challenges as they " +
            "established homes and communities in unfamiliar territories. Many emigrants became farmers, " +
            "miners, or merchants. Women played crucial roles in establishing schools, churches, and " +
            "social institutions that helped transform the frontier into settled communities."
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
        
        // Add text panel to the content panel
        contentPanel.add(textPanel, BorderLayout.CENTER);
        
        // Try to load custom image, fallback to drawn wagon if not available
        // Now this will go at the bottom instead of the left
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(PANEL_COLOR);

        // Try to load custom image using ResourceLoader
        try {
            ImageIcon customIcon = ResourceLoader.loadImage(CUSTOM_IMAGE_PATH);
            if (customIcon != null && customIcon.getIconWidth() > 0) {
                // Calculate available height to prevent overlap with text
                int availableHeight = 250; // Adjusted height to fit well in the panel
                
                // Scale the image to fit available space while preserving aspect ratio
                double ratio = (double) customIcon.getIconWidth() / customIcon.getIconHeight();
                int targetHeight = availableHeight;
                int targetWidth = (int) (targetHeight * ratio);
                
                Image scaledImage = customIcon.getImage().getScaledInstance(
                        targetWidth, targetHeight, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                imageLabel.setHorizontalAlignment(JLabel.CENTER); // Center the image
                imageLabel.setBorder(null); // Remove any borders on the image label
                imagePanel.add(imageLabel, BorderLayout.CENTER);
            }
        } catch (Exception e) {
            System.err.println("Error loading custom completion image: " + e.getMessage());
        }
        
        // Only add the image panel if an image was loaded
        contentPanel.add(imagePanel, BorderLayout.SOUTH);

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