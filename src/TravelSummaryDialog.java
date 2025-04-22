import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Dialog to show a summary of travel events after Fort Kearny journey
 */
public class TravelSummaryDialog extends JDialog {
    private List<String> events;
    private int days;
    private int distance;
    
    // Colors - match EnhancedGUI
    private final Color BACKGROUND_COLOR = new Color(240, 220, 180); // Parchment/sepia
    private final Color PANEL_COLOR = new Color(200, 170, 130);      // Darker parchment
    private final Color TEXT_COLOR = new Color(80, 30, 0);           // Dark brown
    private final Color HEADER_COLOR = new Color(120, 60, 0);        // Medium brown
    private final Color ACCENT_COLOR = new Color(160, 100, 40);      // Light brown
    
    /**
     * Constructor
     * @param owner The parent frame
     * @param events List of event descriptions that occurred
     * @param days Number of days the journey took
     * @param distance Distance traveled to Fort Kearny
     */
    public TravelSummaryDialog(Frame owner, List<String> events, int days, int distance) {
        super(owner, "Journey to Fort Kearny", true);
        this.events = events;
        this.days = days;
        this.distance = distance;
        
        initUI();
        pack();
        setLocationRelativeTo(owner);
        setResizable(true);
    }
    
    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BACKGROUND_COLOR);
        setSize(1000, 800);
        
        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);
        titlePanel.setBorder(new EmptyBorder(10, 10, 5, 10));
        
        JLabel titleLabel = new JLabel("Journey to Fort Kearny", JLabel.CENTER);
        titleLabel.setFont(FontManager.WESTERN_FONT_TITLE);
        titleLabel.setForeground(TEXT_COLOR);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // Main panel with scrolling capabilities
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(PANEL_COLOR);
        
        // Summary panel
        JPanel summaryPanel = new JPanel(new BorderLayout(10, 10));
        summaryPanel.setBackground(PANEL_COLOR);
        summaryPanel.setBorder(new CompoundBorder(
            new TitledBorder(new LineBorder(ACCENT_COLOR, 2), "Journey Summary", 
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
                FontManager.getBoldWesternFont(16), TEXT_COLOR),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // Journey overview
        JPanel overviewPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        overviewPanel.setBackground(PANEL_COLOR);
        
        JPanel statsPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        statsPanel.setBackground(PANEL_COLOR);
        
        JLabel daysLabel = new JLabel("Days on trail: " + days);
        daysLabel.setFont(FontManager.getBoldWesternFont(18));
        daysLabel.setForeground(TEXT_COLOR);
        
        JLabel distanceLabel = new JLabel("Distance traveled: " + distance + " miles");
        distanceLabel.setFont(FontManager.getBoldWesternFont(18));
        distanceLabel.setForeground(TEXT_COLOR);
        
        statsPanel.add(daysLabel);
        statsPanel.add(distanceLabel);
        
        JTextArea descriptionArea = new JTextArea(
            "Your journey to Fort Kearny marks the first major milestone of your westward trek. " +
            "Fort Kearny served as an important outpost for travelers on the Oregon, California, and Mormon trails. " +
            "The challenges you've faced so far pale in comparison to what lies ahead. " +
            "Prepare yourself for the mountains, deserts, and rivers that await."
        );
        descriptionArea.setFont(FontManager.getWesternFont(16));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setBackground(PANEL_COLOR);
        descriptionArea.setForeground(TEXT_COLOR);
        
        overviewPanel.add(statsPanel);
        overviewPanel.add(descriptionArea);
        
        summaryPanel.add(overviewPanel, BorderLayout.NORTH);
        
        // Create a content panel that will hold all sections
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(PANEL_COLOR);
        
        // Add the summary panel to the content
        contentPanel.add(summaryPanel);
        
        // Landmarks section
        JPanel landmarksPanel = new JPanel(new BorderLayout());
        landmarksPanel.setBackground(PANEL_COLOR);
        landmarksPanel.setBorder(new CompoundBorder(
            new TitledBorder(new LineBorder(ACCENT_COLOR, 2), "Landmarks Passed", 
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
                FontManager.getBoldWesternFont(16), TEXT_COLOR),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JTextArea landmarksArea = new JTextArea();
        landmarksArea.setFont(FontManager.getWesternFont(16));
        landmarksArea.setLineWrap(true);
        landmarksArea.setWrapStyleWord(true);
        landmarksArea.setEditable(false);
        landmarksArea.setBackground(PANEL_COLOR);
        landmarksArea.setForeground(TEXT_COLOR);
        
        // Extract landmark information from events
        StringBuilder landmarksText = new StringBuilder();
        
        // Add a header describing the journey path
        landmarksText.append("Your journey took you along this historic route:\n\n");
        
        // Add the starting point based on common departure points
        String departurePoint = "Independence, Missouri";
        for (String event : events) {
            if (event.contains("Nauvoo, Illinois")) {
                departurePoint = "Nauvoo, Illinois";
                break;
            }
        }
        landmarksText.append("• Starting point: ").append(departurePoint).append("\n\n");
        
        // Track whether we've already seen Fort Kearny
        boolean fortKearnyFound = false;
        
        // List all landmarks
        ArrayList<String> landmarksList = new ArrayList<>();
        for (String event : events) {
            if (event.startsWith("LANDMARK:")) {
                String landmarkInfo = event.substring(9).trim(); // Remove the "LANDMARK: " prefix
                landmarksList.add("• " + landmarkInfo);
                
                if (landmarkInfo.contains("Fort Kearny")) {
                    fortKearnyFound = true;
                }
            }
        }
        
        // If no landmarks were recorded but there's a "Notable landmarks" entry, parse that instead
        if (landmarksList.isEmpty()) {
            for (String event : events) {
                if (event.startsWith("Notable landmarks you passed:")) {
                    String[] landmarks = event.substring("Notable landmarks you passed:".length()).trim().split(",");
                    for (String landmark : landmarks) {
                        landmarksList.add("• " + landmark.trim());
                        if (landmark.contains("Fort Kearny")) {
                            fortKearnyFound = true;
                        }
                    }
                }
            }
        }
        
        // Add common landmarks on this portion of the trail if we don't have enough specific ones
        if (landmarksList.size() < 3) {
            if (departurePoint.equals("Independence, Missouri")) {
                if (!containsLandmark(landmarksList, "Blue River crossing")) {
                    landmarksList.add("• Blue River crossing - An early river that tested your river crossing skills");
                }
                if (!containsLandmark(landmarksList, "Kansas River crossing")) {
                    landmarksList.add("• Kansas River crossing - A substantial river where many travelers faced challenges");
                }
                if (!containsLandmark(landmarksList, "Big Blue River crossing")) {
                    landmarksList.add("• Big Blue River crossing - Another river that tested your wagon's durability");
                }
                if (!containsLandmark(landmarksList, "St. Mary's Mission")) {
                    landmarksList.add("• St. Mary's Mission - A Catholic mission that offered rest and supplies to travelers");
                }
            } else { // Nauvoo route
                if (!containsLandmark(landmarksList, "Sugar Creek")) {
                    landmarksList.add("• Sugar Creek - First major campsite after leaving Nauvoo");
                }
                if (!containsLandmark(landmarksList, "Garden Grove")) {
                    landmarksList.add("• Garden Grove - Important way station where Mormon pioneers established farms");
                }
                if (!containsLandmark(landmarksList, "Mount Pisgah")) {
                    landmarksList.add("• Mount Pisgah - Semipermanent settlement that supported thousands of travelers");
                }
            }
        }
        
        // Add Fort Kearny if it wasn't mentioned
        if (!fortKearnyFound) {
            landmarksList.add("• Fort Kearny - Military post established in 1848 that served as a way station for travelers");
        }
        
        // Add all landmarks to the text
        for (String landmark : landmarksList) {
            landmarksText.append(landmark).append("\n\n");
        }
        
        landmarksArea.setText(landmarksText.toString());
        landmarksPanel.add(new JScrollPane(landmarksArea), BorderLayout.CENTER);
        contentPanel.add(landmarksPanel);
        
        // Events section
        JPanel eventsPanel = new JPanel(new BorderLayout());
        eventsPanel.setBackground(PANEL_COLOR);
        eventsPanel.setBorder(new CompoundBorder(
            new TitledBorder(new LineBorder(ACCENT_COLOR, 2), "Journey Events", 
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
                FontManager.getBoldWesternFont(16), TEXT_COLOR),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JTextArea eventsArea = new JTextArea();
        eventsArea.setFont(FontManager.getWesternFont(16));
        eventsArea.setLineWrap(true);
        eventsArea.setWrapStyleWord(true);
        eventsArea.setEditable(false);
        eventsArea.setBackground(PANEL_COLOR);
        eventsArea.setForeground(TEXT_COLOR);
        
        StringBuilder eventsText = new StringBuilder();
        for (String event : events) {
            // Skip landmark entries as they're shown in their own section
            if (!event.startsWith("LANDMARK:") && !event.startsWith("Notable landmarks you passed:")) {
                eventsText.append("• ").append(event).append("\n\n");
            }
        }
        
        if (eventsText.length() == 0) {
            eventsText.append("Your journey was remarkably uneventful.");
        }
        
        eventsArea.setText(eventsText.toString());
        eventsPanel.add(new JScrollPane(eventsArea), BorderLayout.CENTER);
        contentPanel.add(eventsPanel);
        
        // Add the content panel to a scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(new LineBorder(ACCENT_COLOR, 1));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 15, 0));
        
        JButton continueButton = new JButton("Continue Your Journey");
        continueButton.setFont(FontManager.getBoldWesternFont(16));
        continueButton.setForeground(TEXT_COLOR);
        continueButton.setBackground(PANEL_COLOR);
        continueButton.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(8, 30, 8, 30)
        ));
        
        continueButton.addActionListener(e -> dispose());
        
        buttonPanel.add(continueButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Helper method to check if a landmark is already in our list
     */
    private boolean containsLandmark(ArrayList<String> landmarks, String landmarkName) {
        for (String landmark : landmarks) {
            if (landmark.toLowerCase().contains(landmarkName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
} 