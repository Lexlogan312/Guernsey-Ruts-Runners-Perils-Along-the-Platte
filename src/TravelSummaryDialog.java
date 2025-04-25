import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Dialog to show a summary of travel events after Fort Kearny journey
 * CHANGES:
 * - Constructor now accepts consumption totals (food, parts, medicine, ammo).
 * - Added a "Resources Consumed" section to display these totals.
 * - Ensured text area backgrounds match panel color.
 */
public class TravelSummaryDialog extends JDialog {
    private List<String> events;
    private List<Landmark> landmarksPassed;
    private int days;
    private int distance;
    // Added fields for consumption
    private int foodConsumed;
    private int partsUsed;
    private int medicineUsed;
    private int ammoUsed;


    // Colors - match EnhancedGUI
    private final Color BACKGROUND_COLOR = new Color(240, 220, 180); // Parchment/sepia
    private final Color PANEL_COLOR = new Color(200, 170, 130);      // Darker parchment
    private final Color TEXT_COLOR = new Color(80, 30, 0);           // Dark brown
    private final Color HEADER_COLOR = new Color(120, 60, 0);        // Medium brown
    private final Color ACCENT_COLOR = new Color(160, 100, 40);      // Light brown

    /**
     * Constructor
     * @param owner The parent frame
     * @param events List of general event descriptions that occurred
     * @param landmarksPassed List of Landmark objects passed before Fort Kearny
     * @param days Number of days the journey took
     * @param distance Distance traveled to Fort Kearny
     * @param foodConsumed Total food consumed during this leg
     * @param partsUsed Total wagon parts used
     * @param medicineUsed Total medicine used
     * @param ammoUsed Total ammunition used
     */
    public TravelSummaryDialog(Frame owner, List<String> events, List<Landmark> landmarksPassed,
                               int days, int distance, int foodConsumed, int partsUsed,
                               int medicineUsed, int ammoUsed) { // Added consumption params
        super(owner, "Journey to Fort Kearny", true);
        this.events = events;
        this.landmarksPassed = landmarksPassed;
        this.days = days;
        this.distance = distance;
        // Store consumption data
        this.foodConsumed = foodConsumed;
        this.partsUsed = partsUsed;
        this.medicineUsed = medicineUsed;
        this.ammoUsed = ammoUsed;


        initUI();
        pack(); // Pack first
        // Set a reasonable minimum and preferred size
        setMinimumSize(new Dimension(800, 650)); // Increased min height
        setPreferredSize(new Dimension(900, 750)); // Increased preferred height
        setLocationRelativeTo(owner);
        setResizable(true); // Allow resizing
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);
        titlePanel.setBorder(new EmptyBorder(15, 15, 10, 15));

        JLabel titleLabel = new JLabel("Journey to Fort Kearny", JLabel.CENTER);
        titleLabel.setFont(FontManager.WESTERN_FONT_TITLE);
        titleLabel.setForeground(TEXT_COLOR);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        add(titlePanel, BorderLayout.NORTH);

        // Create a content panel that will hold all sections within a scroll pane
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(PANEL_COLOR); // Set background for the container
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- Summary Section ---
        JPanel summaryPanel = createSectionPanel("Journey Summary");
        JPanel overviewPanel = new JPanel(new GridBagLayout());
        overviewPanel.setOpaque(false); // Make transparent to show parent background
        GridBagConstraints gbcSummary = new GridBagConstraints();
        gbcSummary.insets = new Insets(5, 10, 5, 10);
        gbcSummary.anchor = GridBagConstraints.NORTHWEST;
        gbcSummary.fill = GridBagConstraints.HORIZONTAL;

        // Stats (Days and Distance)
        gbcSummary.gridx = 0; gbcSummary.gridy = 0; gbcSummary.weightx = 0.3;
        JLabel daysLabel = new JLabel("Days on trail: " + days);
        daysLabel.setFont(FontManager.getBoldWesternFont(16));
        daysLabel.setForeground(TEXT_COLOR);
        overviewPanel.add(daysLabel, gbcSummary);

        gbcSummary.gridy = 1;
        JLabel distanceLabel = new JLabel("Distance traveled: " + distance + " miles");
        distanceLabel.setFont(FontManager.getBoldWesternFont(16));
        distanceLabel.setForeground(TEXT_COLOR);
        overviewPanel.add(distanceLabel, gbcSummary);

        // Description Text Area
        gbcSummary.gridx = 1; gbcSummary.gridy = 0; gbcSummary.gridheight = 2;
        gbcSummary.weightx = 0.7; gbcSummary.fill = GridBagConstraints.BOTH;
        JTextArea descriptionArea = createStyledTextArea(
                "Your journey to Fort Kearny marks the first major milestone... Prepare yourself for what lies ahead." // Shortened for brevity
        );
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        descScrollPane.setOpaque(false);
        descScrollPane.getViewport().setOpaque(false);
        descScrollPane.setBorder(null); // Remove scrollpane border
        overviewPanel.add(descScrollPane, gbcSummary);

        summaryPanel.add(overviewPanel, BorderLayout.CENTER);
        contentPanel.add(summaryPanel);
        contentPanel.add(Box.createVerticalStrut(15));

        // --- Resources Consumed Section ---
        JPanel consumptionPanel = createSectionPanel("Resources Consumed");
        JPanel consumptionGrid = new JPanel(new GridLayout(2, 2, 15, 8)); // 2x2 grid
        consumptionGrid.setOpaque(false);

        consumptionGrid.add(createConsumptionLabel("Food Consumed:", foodConsumed + " lbs"));
        consumptionGrid.add(createConsumptionLabel("Wagon Parts Used:", String.valueOf(partsUsed)));
        consumptionGrid.add(createConsumptionLabel("Medicine Used:", String.valueOf(medicineUsed)));
        consumptionGrid.add(createConsumptionLabel("Ammunition Used:", ammoUsed + " rounds"));

        consumptionPanel.add(consumptionGrid, BorderLayout.CENTER);
        contentPanel.add(consumptionPanel);
        contentPanel.add(Box.createVerticalStrut(15));


        // --- Landmarks Section ---
        JPanel landmarksPanel = createSectionPanel("Landmarks Passed");
        JTextArea landmarksArea = createStyledTextArea("");
        StringBuilder landmarksText = new StringBuilder();
        // ... (populate landmarksText as before) ...
        if (landmarksPassed != null && !landmarksPassed.isEmpty()) {
            landmarksText.append("Notable landmarks encountered before Fort Kearny:\n\n");
            for (Landmark landmark : landmarksPassed) {
                if (!landmark.getName().contains("Fort Kearny")) {
                    landmarksText.append("• ").append(landmark.getName());
                    if (landmark.getDescription() != null && !landmark.getDescription().isEmpty()) {
                        landmarksText.append(": ").append(landmark.getDescription());
                    }
                    landmarksText.append("\n\n");
                }
            }
        } else {
            landmarksText.append("No specific landmarks were recorded before Fort Kearny.");
        }
        landmarksArea.setText(landmarksText.toString());
        JScrollPane landmarksScrollPane = new JScrollPane(landmarksArea);
        landmarksScrollPane.setOpaque(false);
        landmarksScrollPane.getViewport().setOpaque(false);
        landmarksScrollPane.setBorder(null);
        landmarksPanel.add(landmarksScrollPane, BorderLayout.CENTER);
        contentPanel.add(landmarksPanel);
        contentPanel.add(Box.createVerticalStrut(15));

        // --- Events Section ---
        JPanel eventsPanel = createSectionPanel("Journey Events");
        JTextArea eventsArea = createStyledTextArea("");
        StringBuilder eventsText = new StringBuilder();
        // ... (populate eventsText as before) ...
        if (events != null && !events.isEmpty()) {
            List<String> generalEvents = events.stream()
                    .filter(event -> !event.startsWith("LANDMARK:") && !event.startsWith("Notable landmarks you passed:"))
                    .collect(Collectors.toList());

            if (!generalEvents.isEmpty()) {
                eventsText.append("Significant events during this leg of the journey:\n\n");
                for (String event : generalEvents) {
                    eventsText.append("• ").append(event.trim()).append("\n\n");
                }
            } else {
                eventsText.append("The journey to Fort Kearny was relatively uneventful.");
            }
        } else {
            eventsText.append("No specific events were recorded during this leg of the journey.");
        }
        eventsArea.setText(eventsText.toString());
        JScrollPane eventsScrollPane = new JScrollPane(eventsArea);
        eventsScrollPane.setOpaque(false);
        eventsScrollPane.getViewport().setOpaque(false);
        eventsScrollPane.setBorder(null);
        eventsPanel.add(eventsScrollPane, BorderLayout.CENTER);
        contentPanel.add(eventsPanel);

        // Add the main content panel to a scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(new LineBorder(ACCENT_COLOR, 1));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(PANEL_COLOR); // Match background

        add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(15, 0, 15, 0));

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

    /** Helper method to create a standard section panel */
    private JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false); // Make transparent to show container background
        panel.setBorder(new CompoundBorder(
                new TitledBorder(new LineBorder(ACCENT_COLOR, 1), title, // Thinner border for sections
                        TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                        FontManager.getBoldWesternFont(14), HEADER_COLOR), // Use Header color
                new EmptyBorder(10, 15, 15, 15)
        ));
        return panel;
    }

    /** Helper method to create a styled text area */
    private JTextArea createStyledTextArea(String initialText) {
        JTextArea textArea = new JTextArea(initialText);
        textArea.setFont(FontManager.getWesternFont(14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setOpaque(false); // Make transparent
        // textArea.setBackground(PANEL_COLOR); // Removed explicit background set
        textArea.setForeground(TEXT_COLOR);
        textArea.setMargin(new Insets(5, 5, 5, 5));
        textArea.setBorder(null); // Remove border from text area itself
        return textArea;
    }

    /** Helper method to create consumption labels */
    private JLabel createConsumptionLabel(String labelText, String valueText) {
        JLabel label = new JLabel(labelText + " " + valueText);
        label.setFont(FontManager.getWesternFont(14));
        label.setForeground(TEXT_COLOR);
        return label;
    }
}