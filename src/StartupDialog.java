import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Arrays;

/**
 * Startup dialog to collect player information and game settings
 */
public class StartupDialog extends JDialog {
    private GameController gameController;
    
    // Panel components
    private JTabbedPane tabbedPane;
    private JPanel characterPanel;
    private JPanel familyPanel;
    private JPanel trailPanel;
    private JPanel departurePanel;
    
    // Character fields
    private JTextField nameField;
    private ButtonGroup genderGroup;
    private JRadioButton maleButton;
    private JRadioButton femaleButton;
    
    // Family fields
    private JTextField[] familyMemberFields = new JTextField[3];
    
    // Trail fields
    private ButtonGroup trailGroup;
    private JRadioButton oregonTrailButton;
    private JRadioButton californiaTrailButton;
    private JRadioButton mormonTrailButton;
    
    // Departure fields
    private ButtonGroup monthGroup;
    private JRadioButton[] monthButtons = new JRadioButton[5];
    
    // Colors - match EnhancedGUI
    private final Color BACKGROUND_COLOR = new Color(240, 220, 180); // Parchment/sepia
    private final Color PANEL_COLOR = new Color(200, 170, 130);      // Darker parchment
    private final Color TEXT_COLOR = new Color(80, 30, 0);           // Dark brown
    private final Color HEADER_COLOR = new Color(120, 60, 0);        // Medium brown
    private final Color ACCENT_COLOR = new Color(160, 100, 40);      // Light brown
    
    /**
     * Constructor
     */
    public StartupDialog(Frame owner, GameController controller) {
        super(owner, "Begin Your Journey", true);
        this.gameController = controller;
        
        initializeUI();
        pack();
        setLocationRelativeTo(owner);
        setResizable(false);
    }
    
    /**
     * Initialize the UI components
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
        createCharacterPanel();
        createFamilyPanel();
        createTrailPanel();
        createDeparturePanel();
        
        // Add panels to tabbed pane
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
        
        // Add next/back/finish buttons at bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton backButton = createStyledButton("Back");
        JButton nextButton = createStyledButton("Next");
        JButton finishButton = createStyledButton("Begin Journey");
        
        buttonPanel.add(backButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(finishButton);
        
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
                if (validateCurrentTab(index)) {
                    tabbedPane.setSelectedIndex(index + 1);
                }
            }
        });
        
        finishButton.addActionListener(e -> {
            if (validateAllTabs()) {
                saveSettings();
                dispose();
            }
        });
    }
    
    /**
     * Creates the character selection panel
     */
    private void createCharacterPanel() {
        characterPanel = createStyledPanel();
        characterPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Add description
        JTextArea descriptionArea = createDescriptionArea(
            "Choose your character's name and gender. In the 1800s, men and women " +
            "faced different challenges on the trail west. Your choice will affect " +
            "some of the situations you encounter."
        );
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        characterPanel.add(descriptionArea, gbc);
        
        // Add name field
        JLabel nameLabel = new JLabel("Your Name:");
        nameLabel.setFont(FontManager.getBoldWesternFont(14f));
        nameLabel.setForeground(TEXT_COLOR);
        
        nameField = new JTextField(20);
        nameField.setFont(FontManager.getWesternFont(14f));
        nameField.setForeground(TEXT_COLOR);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        characterPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        characterPanel.add(nameField, gbc);
        
        // Add gender selection
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(FontManager.getBoldWesternFont(14f));
        genderLabel.setForeground(TEXT_COLOR);
        
        maleButton = new JRadioButton("Male");
        maleButton.setFont(FontManager.getWesternFont(14f));
        maleButton.setForeground(TEXT_COLOR);
        maleButton.setBackground(PANEL_COLOR);
        maleButton.setSelected(true);
        
        femaleButton = new JRadioButton("Female");
        femaleButton.setFont(FontManager.getWesternFont(14f));
        femaleButton.setForeground(TEXT_COLOR);
        femaleButton.setBackground(PANEL_COLOR);
        
        genderGroup = new ButtonGroup();
        genderGroup.add(maleButton);
        genderGroup.add(femaleButton);
        
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        genderPanel.setBackground(PANEL_COLOR);
        genderPanel.add(maleButton);
        genderPanel.add(femaleButton);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        characterPanel.add(genderLabel, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        characterPanel.add(genderPanel, gbc);
        
        // Add occupation details for historical flavor
        JLabel occupationLabel = new JLabel("Common Occupations:");
        occupationLabel.setFont(FontManager.getBoldWesternFont(14f));
        occupationLabel.setForeground(TEXT_COLOR);
        
        JTextArea occupationArea = createDescriptionArea(
            "Emigrants on the trail were farmers, blacksmiths, carpenters, " +
            "merchants, doctors, and lawyers. Most were farmers seeking fertile " +
            "land in the West."
        );
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        characterPanel.add(occupationLabel, gbc);
        
        gbc.gridy = 4;
        characterPanel.add(occupationArea, gbc);
    }
    
    /**
     * Creates the family members panel
     */
    private void createFamilyPanel() {
        familyPanel = createStyledPanel();
        familyPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Add description
        JTextArea descriptionArea = createDescriptionArea(
            "Name three family members traveling with you. Many emigrants traveled " +
            "in family groups, providing mutual support during the journey."
        );
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        familyPanel.add(descriptionArea, gbc);
        
        // Add family member fields
        for (int i = 0; i < 3; i++) {
            JLabel memberLabel = new JLabel("Family Member " + (i+1) + ":");
            memberLabel.setFont(FontManager.getBoldWesternFont(14f));
            memberLabel.setForeground(TEXT_COLOR);
            
            familyMemberFields[i] = new JTextField(20);
            familyMemberFields[i].setFont(FontManager.getWesternFont(14f));
            familyMemberFields[i].setForeground(TEXT_COLOR);
            
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            gbc.gridwidth = 1;
            familyPanel.add(memberLabel, gbc);
            
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            familyPanel.add(familyMemberFields[i], gbc);
        }
        
        // Add historical note
        JTextArea historicalNote = createDescriptionArea(
            "Historical Note: Extended families often traveled together. Children " +
            "were expected to help with daily chores, while women managed cooking, " +
            "laundry, and childcare. Men drove wagons, handled livestock, and made " +
            "repairs."
        );
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        familyPanel.add(historicalNote, gbc);
    }
    
    /**
     * Creates the trail selection panel
     */
    private void createTrailPanel() {
        trailPanel = createStyledPanel();
        trailPanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Add description
        JTextArea descriptionArea = createDescriptionArea(
            "Choose which trail to follow. Each route had different destinations, " +
            "terrain, and challenges."
        );
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        trailPanel.add(descriptionArea, gbc);
        
        // Create trail buttons
        oregonTrailButton = new JRadioButton("Oregon Trail (2,170 miles)");
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
        
        trailGroup = new ButtonGroup();
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
        
        // Add trail panels
        gbc.gridx = 0;
        gbc.gridy = 1;
        trailPanel.add(oregonPanel, gbc);
        
        gbc.gridy = 2;
        trailPanel.add(californiaPanel, gbc);
        
        gbc.gridy = 3;
        trailPanel.add(mormonPanel, gbc);
    }
    
    /**
     * Creates the departure month panel
     */
    private void createDeparturePanel() {
        departurePanel = createStyledPanel();
        departurePanel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Add description
        JTextArea descriptionArea = createDescriptionArea(
            "Choose when to depart. The timing of your departure was crucial for pioneers.\n" +
            "Leave too early: face mud and flooding from spring rains.\n" +
            "Leave too late: risk being trapped in mountain snow.\n\n" +
            "Most emigrants departed between April and June."
        );
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        departurePanel.add(descriptionArea, gbc);
        
        // Create month selection
        monthGroup = new ButtonGroup();
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
            
            if (i == 1) { // April is default
                monthButtons[i].setSelected(true);
            }
            
            monthGroup.add(monthButtons[i]);
            
            JPanel monthPanel = new JPanel(new BorderLayout(5, 5));
            monthPanel.setBackground(PANEL_COLOR);
            monthPanel.add(monthButtons[i], BorderLayout.NORTH);
            
            JTextArea monthText = createDescriptionArea(descriptions[i]);
            monthPanel.add(monthText, BorderLayout.CENTER);
            
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            departurePanel.add(monthPanel, gbc);
        }
    }
    
    /**
     * Creates a standardized description text area
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
     * Creates a styled panel with borders
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
     * Creates a styled button
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(FontManager.getBoldWesternFont(14f));
        button.setForeground(TEXT_COLOR);
        button.setBackground(PANEL_COLOR);
        button.setFocusPainted(false);
        button.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_COLOR, 2),
            new EmptyBorder(5, 10, 5, 10)
        ));
        return button;
    }
    
    /**
     * Validates the current tab
     */
    private boolean validateCurrentTab(int tabIndex) {
        switch (tabIndex) {
            case 0: // Character
                if (nameField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Please enter your character's name.",
                        "Input Required",
                        JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                return true;
                
            case 1: // Family
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
                
            case 2: // Trail
                return true; // Always valid as we have radio buttons
                
            case 3: // Departure
                return true; // Always valid as we have radio buttons
        }
        return true;
    }
    
    /**
     * Validates all tabs
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
     * Saves all settings to the game controller
     */
    private void saveSettings() {
        // Save player info
        String gender = maleButton.isSelected() ? "male" : "female";
        String[] familyMembers = new String[3];
        for (int i = 0; i < 3; i++) {
            familyMembers[i] = familyMemberFields[i].getText().trim();
        }
        
        gameController.playerSetup(nameField.getText().trim(), gender, familyMembers);
        
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
} 