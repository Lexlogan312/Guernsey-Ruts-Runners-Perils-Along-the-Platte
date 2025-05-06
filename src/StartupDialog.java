import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * StartupDialog Class of the Perils Along the Platte Game
 * A multi-tab dialog that collects player information and game settings at startup.
 * 
 * The dialog consists of five tabs:
 * 1. Introduction: Game overview and welcome message
 * 2. Character: Player name, gender, and occupation selection
 * 3. Family: Family member names (up to 3)
 * 4. Trail: Trail selection (California or Mormon)
 * 5. Departure: Departure month selection
 *
 * @author Alex Randall and Chase McCluskey
 * @version 1.0
 * @date 05/06/2025
 * @file StartupDialog.java
 */
public class StartupDialog extends JDialog {
    // The game controller instance for managing game state
    private final GameController gameController;

    // The main tabbed pane containing all setup panels
    private JTabbedPane tabbedPane;
    
    // The introduction panel with game overview
    private JPanel introPanel;
    
    // The character creation panel
    private JPanel characterPanel;
    
    // The family member setup panel
    private JPanel familyPanel;
    
    // The trail selection panel
    private JPanel trailPanel;
    
    // The departure month selection panel
    private JPanel departurePanel;
    
    // Combo box for occupation selection
    private JComboBox<String> occupationComboBox;

    // Text field for player name input
    private JTextField nameField;
    
    // Radio button for male gender selection
    private JRadioButton maleButton;

    // Text fields for family member names
    private final JTextField[] familyMemberFields = new JTextField[3];

    // Radio buttons for trail selection
    private JRadioButton californiaTrailButton;
    private JRadioButton mormonTrailButton;

    // Radio buttons for departure month selection
    private final JRadioButton[] monthButtons = new JRadioButton[5];

    private final Color BACKGROUND_COLOR = new Color(240, 220, 180); // Parchment/sepia
    private final Color PANEL_COLOR = new Color(200, 170, 130);      // Darker parchment
    private final Color TEXT_COLOR = new Color(80, 30, 0);           // Dark brown
    private final Color HEADER_COLOR = new Color(120, 60, 0);        // Medium brown
    private final Color ACCENT_COLOR = new Color(160, 100, 40);      // Light brown
    
    /**
     * Constructs a new StartupDialog.
     * 
     * @param owner The parent frame
     * @param controller The game controller instance
     */
    public StartupDialog(Frame owner, GameController controller) {
        super(owner, "Begin Your Journey", true);
        this.gameController = controller;
        
        initializeUI();
        pack();
        // Set dialog size back to original size
        setSize(Math.max(getWidth(), 850), Math.max(getHeight(), 400));
        setLocationRelativeTo(owner);
        setResizable(false);
    }
    
    /**
     * Initializes the UI components of the dialog.
     * Sets up:
     * - Dialog layout and properties
     * - Tabbed pane with all panels
     * - Title and navigation buttons
     * - Event handlers for navigation
     */
    private void initializeUI() {
        // Set dialog properties
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(FontManager.getBoldWesternFont(14f));
        tabbedPane.setForeground(TEXT_COLOR);
        tabbedPane.setBackground(PANEL_COLOR);
        
        // Create panels
        createIntroPanel();
        createCharacterPanel();
        createFamilyPanel();
        createTrailPanel();
        createDeparturePanel();
        
        // Add panels to tabbed pane
        tabbedPane.addTab("Introduction", introPanel);
        tabbedPane.addTab("Character", characterPanel);
        tabbedPane.addTab("Family", familyPanel);
        tabbedPane.addTab("Trail", trailPanel);
        tabbedPane.addTab("Departure", departurePanel);
        
        // Add title
        JLabel titleLabel = new JLabel("Perils Along the Platte");
        titleLabel.setFont(FontManager.WESTERN_FONT_TITLE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(TEXT_COLOR);
        add(titleLabel, BorderLayout.NORTH);
        
        // Add tabbed pane to center
        add(tabbedPane, BorderLayout.CENTER);
        
        // Add next/back buttons at bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton backButton = createStyledButton("Back");
        JButton nextButton = createStyledButton("Next");
        
        buttonPanel.add(backButton);
        buttonPanel.add(nextButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Set up button actions
        backButton.addActionListener(e -> {
            int index = tabbedPane.getSelectedIndex();
            if (index > 0) {
                tabbedPane.setSelectedIndex(index - 1);
            }
        });
        
        nextButton.addActionListener(e -> {
            int index = tabbedPane.getSelectedIndex();
            if (index < tabbedPane.getTabCount() - 1) {
                // For intro panel, always allow progression to character tab
                if (index == 0 || validateCurrentTab(index)) {
                    tabbedPane.setSelectedIndex(index + 1);
                }
            } else {
                // If on the last tab, validate and save settings
                if (validateAllTabs()) {
                    saveSettings();
                    dispose();
                }
            }
        });
    }
    
    /**
     * Creates the introduction panel with:
     * - Welcome message
     * - Game overview text
     * - Historical context
     * - Navigation guidance
     */
    private void createIntroPanel() {
        introPanel = createStyledPanel();
        introPanel.setLayout(new BorderLayout(10, 10));
        
        // Main title
        JLabel welcomeLabel = new JLabel("Welcome to Perils Along the Platte!", JLabel.CENTER);
        welcomeLabel.setFont(FontManager.getBoldWesternFont(20f));
        welcomeLabel.setForeground(HEADER_COLOR);
        
        // Introduction text
        JTextArea introText = createDescriptionArea(
            "The year is 1848, and you are about to embark on one of the most significant " +
            "journeys in American history. You will travel a small portion of the nearly 2,000 " +
            "mile long trails to reach the American frontier in search of a new life.\n\n" +
            
            "Your goal is to successfully guide your family from Fort Kearny to Independence Rock on " +
            "your chosen trail. Along the way, you'll face numerous challenges including river crossings," +
            "harsh weather, limited supplies, illness, and other hardships that pioneers encountered.\n\n" +
            
            "Between 1841 and 1869, over 400,000 pioneers traveled these trails to reach new homes in " +
            "the west. Their journeys fundamentally shaped American history."
        );
        
        // Add components to the panel - simplified layout without image and tips
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(PANEL_COLOR);
        contentPanel.add(introText, BorderLayout.CENTER);
        
        introPanel.add(welcomeLabel, BorderLayout.NORTH);
        introPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Add guidance text at the bottom
        JTextArea guidanceText = createDescriptionArea(
            "Click 'Next' to begin creating your character and planning your journey west!"
        );
        introPanel.add(guidanceText, BorderLayout.SOUTH);
    }
    
    /**
     * Creates the character creation panel with:
     * - Name input field
     * - Gender selection
     * - Occupation selection with descriptions
     */
    private void createCharacterPanel() {
        characterPanel = createStyledPanel();
        characterPanel.setLayout(new BorderLayout(10, 10));

        // Add description at the top
        JTextArea descriptionArea = createDescriptionArea(
                "Choose your character's name and gender. In the 1800s, men and women " +
                        "faced different challenges on the trail. Your choice will affect " +
                        "some of the situations you encounter."
        );
        characterPanel.add(descriptionArea, BorderLayout.NORTH);

        // Create a center panel for form elements
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        formPanel.setBackground(PANEL_COLOR);

        // Add name field with label
        JPanel namePanel = new JPanel(new BorderLayout(10, 0));
        namePanel.setBackground(PANEL_COLOR);

        JLabel nameLabel = new JLabel("Your Name:");
        nameLabel.setFont(FontManager.getBoldWesternFont(14f));
        nameLabel.setForeground(TEXT_COLOR);

        nameField = new JTextField(20);
        nameField.setFont(FontManager.getWesternFont(14f));
        nameField.setForeground(TEXT_COLOR);

        namePanel.add(nameLabel, BorderLayout.WEST);
        namePanel.add(nameField, BorderLayout.CENTER);
        formPanel.add(namePanel);

        // Add gender selection
        JPanel genderPanel = new JPanel(new BorderLayout(10, 0));
        genderPanel.setBackground(PANEL_COLOR);

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(FontManager.getBoldWesternFont(14f));
        genderLabel.setForeground(TEXT_COLOR);

        maleButton = new JRadioButton("Male");
        maleButton.setFont(FontManager.getWesternFont(14f));
        maleButton.setForeground(TEXT_COLOR);
        maleButton.setBackground(PANEL_COLOR);
        maleButton.setSelected(true);

        JRadioButton femaleButton = new JRadioButton("Female");
        femaleButton.setFont(FontManager.getWesternFont(14f));
        femaleButton.setForeground(TEXT_COLOR);
        femaleButton.setBackground(PANEL_COLOR);

        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);

        JPanel genderButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderButtonsPanel.setBackground(PANEL_COLOR);
        genderButtonsPanel.add(maleButton);
        genderButtonsPanel.add(femaleButton);

        genderPanel.add(genderLabel, BorderLayout.WEST);
        genderPanel.add(genderButtonsPanel, BorderLayout.CENTER);
        formPanel.add(genderPanel);

        // Add occupation section
        // Occupation selection (new actual selection!)
        JPanel occupationPanel = new JPanel(new BorderLayout(10, 0));
        occupationPanel.setBackground(PANEL_COLOR);

        JLabel occupationLabel = new JLabel("Occupation:");
        occupationLabel.setFont(FontManager.getBoldWesternFont(14f));
        occupationLabel.setForeground(TEXT_COLOR);

        // Create occupation options with descriptions
        String[] occupationLabels = {
                "Farmer - Farmers have experience with plants and animals. Benefit: Food spoils more slowly.",
                "Blacksmith - Blacksmiths are skilled with metal and tools. Benefit: Wagon parts break less often.",
                "Carpenter - Carpenters can repair wagons efficiently. Benefit: Better repair skills and faster fixes.",
                "Merchant - Merchants are skilled traders. Benefit: Better prices when buying and selling goods.",
                "Doctor - Doctors can treat illnesses and injuries. Benefit: Party health depletes more slowly.",
                "Hunter - Hunters are expert marksmen. Benefit: Increased travel speed and hunting efficiency.",
                "Teacher - Teachers boost morale through education. Benefit: Party morale decreases more slowly.",
                "Preacher - Preachers provide spiritual guidance. Benefit: Improved healing and morale boost."
        };
        
        // These are the actual values we'll use for job creation
        String[] occupations = {
                "Farmer", "Blacksmith", "Carpenter", "Merchant",
                "Doctor", "Hunter", "Teacher", "Preacher"
        };

        occupationComboBox = new JComboBox<>(occupationLabels);
        occupationComboBox.setFont(FontManager.getWesternFont(14f));
        occupationComboBox.setForeground(TEXT_COLOR);
        occupationComboBox.setBackground(PANEL_COLOR);
        
        // Make the dropdown wide enough for the descriptions
        occupationComboBox.setPreferredSize(new Dimension(400, 30));
        
        occupationPanel.add(occupationLabel, BorderLayout.WEST);
        occupationPanel.add(occupationComboBox, BorderLayout.CENTER);
        formPanel.add(occupationPanel);

        // Add form panel to center
        characterPanel.add(formPanel, BorderLayout.CENTER);
        
        // Add guidance text at the bottom
        JTextArea guidanceText = createDescriptionArea(
            "Click 'Next' to add your family members who will join you on the journey."
        );
        characterPanel.add(guidanceText, BorderLayout.SOUTH);
    }

    /**
     * Creates the family member setup panel with:
     * - Up to 3 family member name fields
     * - Optional family member input
     */
    private void createFamilyPanel() {
        familyPanel = createStyledPanel();
        familyPanel.setLayout(new BorderLayout(10, 10));

        // Add description at the top
        JTextArea descriptionArea = createDescriptionArea(
                "Name three family members traveling with you. Many emigrants traveled " +
                        "in family groups, providing mutual support during the journey."
        );
        familyPanel.add(descriptionArea, BorderLayout.NORTH);

        // Create a panel for family member fields
        JPanel membersPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        membersPanel.setBackground(PANEL_COLOR);

        // Add family member fields
        for (int i = 0; i < 3; i++) {
            JPanel memberRow = new JPanel(new BorderLayout(10, 0));
            memberRow.setBackground(PANEL_COLOR);

            JLabel memberLabel = new JLabel("Family Member " + (i+1) + ":");
            memberLabel.setFont(FontManager.getBoldWesternFont(14f));
            memberLabel.setForeground(TEXT_COLOR);

            familyMemberFields[i] = new JTextField(20);
            familyMemberFields[i].setFont(FontManager.getWesternFont(14f));
            familyMemberFields[i].setForeground(TEXT_COLOR);

            memberRow.add(memberLabel, BorderLayout.WEST);
            memberRow.add(familyMemberFields[i], BorderLayout.CENTER);
            membersPanel.add(memberRow);
        }

        familyPanel.add(membersPanel, BorderLayout.CENTER);

        // Add historical note at the bottom
        JTextArea historicalNote = createDescriptionArea(
                "Historical Note: Extended families often traveled together. Children " +
                        "were expected to help with daily chores, while women managed cooking, " +
                        "laundry, and childcare. Men drove wagons, handled livestock, and made " +
                        "repairs."
        );
        familyPanel.add(historicalNote, BorderLayout.SOUTH);
        
        // Add guidance text at the bottom
        JTextArea guidanceText = createDescriptionArea(
            "Click 'Next' to choose which trail you will follow to the west."
        );
        familyPanel.add(guidanceText, BorderLayout.SOUTH);
    }
    
    /**
     * Creates the trail selection panel with:
     * - California Trail option
     * - Mormon Trail option
     * - Trail descriptions and differences
     */
    private void createTrailPanel() {
        trailPanel = createStyledPanel();
        trailPanel.setLayout(new BorderLayout(0, 15)); // Add vertical gap between components

        // Add description at the top
        JTextArea descriptionArea = createDescriptionArea(
                "Choose which trail you would like to follow. Each route had different destinations, terrain, and challenges."
        );

        // Add padding below the description
        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.setBackground(PANEL_COLOR);
        descriptionPanel.add(descriptionArea, BorderLayout.CENTER);
        descriptionPanel.setBorder(new EmptyBorder(0, 0, 10, 0)); // Add bottom padding
        
        trailPanel.add(descriptionPanel, BorderLayout.NORTH);

        // Create a container panel for trails that uses GridLayout
        JPanel trailsContainer = new JPanel(new GridLayout(1, 3, 10, 0)); // 1 row, 3 columns, 10px horizontal gap
        trailsContainer.setBackground(PANEL_COLOR);
        // Add top padding to push options down further
        trailsContainer.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Create trail buttons
        JRadioButton oregonTrailButton = new JRadioButton("Oregon Trail (2,170 miles)");
        oregonTrailButton.setFont(FontManager.getBoldWesternFont(14f));
        oregonTrailButton.setForeground(TEXT_COLOR);
        oregonTrailButton.setBackground(PANEL_COLOR);
        oregonTrailButton.setSelected(true);

        californiaTrailButton = new JRadioButton("California Trail (1,950 miles)");
        californiaTrailButton.setFont(FontManager.getBoldWesternFont(14f));
        californiaTrailButton.setForeground(TEXT_COLOR);
        californiaTrailButton.setBackground(PANEL_COLOR);

        mormonTrailButton = new JRadioButton("Mormon Trail (1,300 miles)");
        mormonTrailButton.setFont(FontManager.getBoldWesternFont(14f));
        mormonTrailButton.setForeground(TEXT_COLOR);
        mormonTrailButton.setBackground(PANEL_COLOR);

        // Trail fields
        ButtonGroup trailGroup = new ButtonGroup();
        trailGroup.add(oregonTrailButton);
        trailGroup.add(californiaTrailButton);
        trailGroup.add(mormonTrailButton);

        // Oregon Trail panel
        JPanel oregonPanel = new JPanel(new BorderLayout(5, 5));
        oregonPanel.setBackground(PANEL_COLOR);
        oregonPanel.add(oregonTrailButton, BorderLayout.NORTH);

        JTextArea oregonText = createDescriptionArea(
                "Destination: Oregon's Willamette Valley\n" +
                        "Historical Note: Most popular route for farmers seeking fertile land"
        );
        oregonPanel.add(oregonText, BorderLayout.CENTER);

        // California Trail panel
        JPanel californiaPanel = new JPanel(new BorderLayout(5, 5));
        californiaPanel.setBackground(PANEL_COLOR);
        californiaPanel.add(californiaTrailButton, BorderLayout.NORTH);

        JTextArea californiaText = createDescriptionArea(
                "Destination: California's gold fields and farmlands\n" +
                        "Historical Note: Became heavily traveled after the 1848 Gold Rush"
        );
        californiaPanel.add(californiaText, BorderLayout.CENTER);

        // Mormon Trail panel
        JPanel mormonPanel = new JPanel(new BorderLayout(5, 5));
        mormonPanel.setBackground(PANEL_COLOR);
        mormonPanel.add(mormonTrailButton, BorderLayout.NORTH);

        JTextArea mormonText = createDescriptionArea(
                "Destination: Salt Lake Valley, Utah\n" +
                        "Historical Note: Used by Mormon pioneers fleeing religious persecution"
        );
        mormonPanel.add(mormonText, BorderLayout.CENTER);

        // Add trail panels to the container
        trailsContainer.add(oregonPanel);
        trailsContainer.add(californiaPanel);
        trailsContainer.add(mormonPanel);

        // Add the trails container to the main panel
        trailPanel.add(trailsContainer, BorderLayout.CENTER);
        
        // Add guidance text at the bottom
        JTextArea guidanceText = createDescriptionArea(
            "Click 'Next' to select your departure month and prepare for the journey."
        );
        trailPanel.add(guidanceText, BorderLayout.SOUTH);
    }
    
    /**
     * Creates the departure month selection panel with:
     * - 5 month options (April through August)
     * - Month descriptions and recommendations
     */
    private void createDeparturePanel() {
        departurePanel = createStyledPanel();
        departurePanel.setLayout(new BorderLayout());

        // Add description at the top
        JTextArea descriptionArea = createDescriptionArea(
                "Choose when to depart. The timing of your departure was crucial for pioneers.\n\n" +
                        "Most emigrants departed between April and June."
        );

        departurePanel.add(descriptionArea, BorderLayout.NORTH);

        // Create a panel for months that uses FlowLayout or GridLayout
        JPanel monthsContainer = new JPanel(new GridLayout(1, 5, 10, 0)); // 1 row, 5 columns, 10px horizontal gap
        monthsContainer.setBackground(PANEL_COLOR);

        // Create month selection
        // Departure fields
        ButtonGroup monthGroup = new ButtonGroup();
        String[] months = {"March", "April", "May", "June", "July"};
        String[] descriptions = {
                "An early start, but you'll face muddy trails and swollen rivers.",
                "A good balance - the trails are drying out and you'll have plenty of time.",
                "The most popular month for departure - grass for animals is plentiful.",
                "Still a good time to leave, but you'll need to maintain a steady pace.",
                "A late start - you'll need to hurry to cross mountains before winter."
        };

        for (int i = 0; i < months.length; i++) {
            monthButtons[i] = new JRadioButton(months[i]);
            monthButtons[i].setFont(FontManager.getBoldWesternFont(14f));
            monthButtons[i].setForeground(TEXT_COLOR);
            monthButtons[i].setBackground(PANEL_COLOR);

            if (i == 0) { // March is default
                monthButtons[i].setSelected(true);
            }

            monthGroup.add(monthButtons[i]);

            JPanel monthPanel = new JPanel(new BorderLayout(5, 5));
            monthPanel.setBackground(PANEL_COLOR);
            monthPanel.add(monthButtons[i], BorderLayout.NORTH);

            JTextArea monthText = createDescriptionArea(descriptions[i]);
            monthPanel.add(monthText, BorderLayout.CENTER);

            // Add each month panel to the months container
            monthsContainer.add(monthPanel);
        }

        // Add the months container to the main panel
        departurePanel.add(monthsContainer, BorderLayout.CENTER);
        
        // Add guidance text at the bottom
        JTextArea guidanceText = createDescriptionArea(
            "Click 'Next' to visit the market and prepare for your journey."
        );
        departurePanel.add(guidanceText, BorderLayout.SOUTH);
    }
    
    /**
     * Creates a styled text area for descriptions.
     * 
     * @param text The text to display
     * @return A styled JTextArea with the specified text
     */
    private JTextArea createDescriptionArea(String text) {
        JTextArea area = new JTextArea(text);
        area.setFont(FontManager.getWesternFont(12f));
        area.setForeground(TEXT_COLOR);
        area.setBackground(PANEL_COLOR);
        area.setWrapStyleWord(true);
        area.setLineWrap(true);
        area.setEditable(false);
        area.setMargin(new Insets(5, 5, 5, 5));
        area.setOpaque(true);
        return area;
    }
    
    /**
     * Creates a styled panel with consistent appearance.
     * 
     * @return A styled JPanel
     */
    private JPanel createStyledPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(PANEL_COLOR);
        panel.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(10, 10, 10, 10)
        ));
        return panel;
    }
    
    /**
     * Creates a styled button with consistent appearance.
     * 
     * @param text The button text
     * @return A styled JButton
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FontManager.getBoldWesternFont(14f));
        button.setForeground(TEXT_COLOR);
        button.setBackground(PANEL_COLOR);
        button.setFocusPainted(false);
        button.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(5, 25, 5, 25)
        ));
        return button;
    }
    
    /**
     * Validates the current tab's input.
     * 
     * @param tabIndex The index of the tab to validate
     * @return true if the tab's input is valid, false otherwise
     */
    private boolean validateCurrentTab(int tabIndex) {
        switch (tabIndex) {
            case 0: // Introduction
                return true; // Always valid as we have text
            case 1: // Character
                if (nameField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Please enter your character's name.",
                        "Input Required",
                        JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                return true;
                
            case 2: // Family
                for (int i = 0; i < familyMemberFields.length; i++) {
                    if (familyMemberFields[i].getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                            "Please enter all family member names.",
                            "Input Required",
                            JOptionPane.WARNING_MESSAGE);
                        return false;
                    }
                }
                return true;
                
            case 3: // Trail
                return true; // Always valid as we have radio buttons
                
            case 4: // Departure
                return true; // Always valid as we have radio buttons
        }
        return true;
    }
    
    /**
     * Validates all tabs' input before saving settings.
     * 
     * @return true if all input is valid, false otherwise
     */
    private boolean validateAllTabs() {
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (!validateCurrentTab(i)) {
                tabbedPane.setSelectedIndex(i);
                return false;
            }
        }
        return true;
    }
    
    /**
     * Saves all settings to the game controller.
     * Creates:
     * - Player with selected attributes
     * - Family members
     * - Trail selection
     * - Departure time
     */
    private void saveSettings() {
        // Save player info
        String gender = maleButton.isSelected() ? "male" : "female";
        String[] familyMembers = new String[3];
        for (int i = 0; i < 3; i++) {
            familyMembers[i] = familyMemberFields[i].getText().trim();
        }

        String selectedOccupation = (String) occupationComboBox.getSelectedItem();
        // Extract just the job name from the description (takes text before the first hyphen)
        String jobName = selectedOccupation.split(" - ")[0];
        Job job = Job.valueOf(jobName.toUpperCase());
        gameController.playerSetup(nameField.getText().trim(), gender, familyMembers, job);
        
        // Save trail choice
        int trailChoice = 1; // Default to Oregon Trail
        if (californiaTrailButton.isSelected()) {
            trailChoice = 2;
        } else if (mormonTrailButton.isSelected()) {
            trailChoice = 3;
        }
        
        gameController.selectTrail(trailChoice);
        
        // Save departure month
        int monthChoice = 1; // Default to March
        for (int i = 0; i < monthButtons.length; i++) {
            if (monthButtons[i].isSelected()) {
                monthChoice = i + 1;
                break;
            }
        }
        
        gameController.selectDepartureMonth(monthChoice);
    }

    /**
     * Gets the description for a specific job.
     * 
     * @param jobName The name of the job
     * @return The job's description
     */
    private String getJobDescription(String jobName) {
        switch (jobName) {
            case "Farmer":
                return "Farmers have experience with plants and animals. Benefit: Food spoils more slowly.";
            case "Blacksmith":
                return "Blacksmiths are skilled with metal and tools. Benefit: Wagon parts break less often.";
            case "Carpenter":
                return "Carpenters can repair wagons efficiently. Benefit: Better repair skills and faster fixes.";
            case "Merchant":
                return "Merchants are skilled traders. Benefit: Better prices when buying and selling goods.";
            case "Doctor":
                return "Doctors can treat illnesses and injuries. Benefit: Party health depletes more slowly.";
            case "Hunter":
                return "Hunters are expert marksmen. Benefit: Increased travel speed and hunting efficiency.";
            case "Teacher":
                return "Teachers boost morale through education. Benefit: Party morale decreases more slowly.";
            case "Preacher":
                return "Preachers provide spiritual guidance. Benefit: Improved healing and morale boost.";
            default:
                return "Select an occupation for your journey west.";
        }
    }
} 