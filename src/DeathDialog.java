import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
    
    // Path to custom death image
    private static final String CUSTOM_IMAGE_PATH = "images/Death Screen.png";
    
    // Map of detailed death messages for different causes
    private static final Map<String, String> DEATH_DETAILS = new HashMap<>();
    
    static {
        // Initialize detailed death messages
        DEATH_DETAILS.put("cholera", "Cholera was one of the most feared diseases on the trail. " +
                "This bacterial infection caused severe diarrhea, vomiting, and dehydration. It spread quickly " +
                "through contaminated water supplies, often claiming several members of a wagon train. " +
                "In 1849-1850, cholera killed as many as 5,000 travelers along the Platte River alone.");
        
        DEATH_DETAILS.put("dysentery", "Dysentery was a common and often fatal disease on the trail, " +
                "causing severe diarrhea with blood and mucus, painful abdominal cramps, and fever. " +
                "Poor sanitation, contaminated water, and close quarters made it spread rapidly among wagon trains. " +
                "Many emigrants' diaries described the suffering from this devastating illness.");
        
        DEATH_DETAILS.put("typhoid", "Typhoid fever caused high fevers, weakness, stomach pain, headache, and rash. " +
                "The disease was spread through contaminated water and food. On the frontier, typhoid " +
                "claimed countless lives with medicine and proper care often unavailable.");
                
        DEATH_DETAILS.put("measles", "Measles was especially deadly to children on the trail. The disease " +
                "caused rash, high fever, cough, and could lead to fatal complications like pneumonia. " +
                "With no vaccines available in the 1800s, measles outbreaks often swept through wagon trains, " +
                "sometimes forcing groups to delay travel while the sick recovered or died.");
                
        DEATH_DETAILS.put("exhaustion", "The grueling pace of travel took a severe physical toll. " +
                "Many emigrants walked beside wagons for thousands of miles, suffering from malnutrition, " +
                "exposure to elements, and constant physical labor. Records show many pioneers simply " +
                "collapsed from the cumulative effects of hunger, dehydration, and overexertion.");
                
        DEATH_DETAILS.put("starvation", "Food supplies often ran dangerously low on the trail. Poor planning, " +
                "spoilage, theft, or accidents could leave emigrants without adequate provisions. " +
                "Historical accounts describe desperate situations where travelers resorted to eating " +
                "seed grain, shoe leather, or even their draft animals before succumbing to starvation.");
                
        DEATH_DETAILS.put("drowning", "River crossings were among the most dangerous parts of the journey. " +
                "Swollen rivers could sweep away wagons, people, and livestock in moments. Historical " +
                "records indicate that hundreds of emigrants drowned at major river crossings along the " +
                "trail, particularly at the Kansas, North Platte, and Columbia Rivers.");
                
        DEATH_DETAILS.put("exposure", "Extreme weather conditions claimed many lives on the trail. " +
                "Blizzards, freezing temperatures, and unexpected storms left travelers vulnerable to " +
                "hypothermia and frostbite. The tragedy of the Donner Party in 1846-47, trapped by early " +
                "snowfall in the Sierra Nevada mountains, represents the most infamous case of death by exposure.");
                
        DEATH_DETAILS.put("accident", "Wagon accidents were a constant hazard. Overturned wagons, " +
                "runaway oxen, handling firearms, and other mishaps led to serious injuries and deaths. " +
                "Without proper medical care, even minor injuries could become infected and fatal. " +
                "Trail diaries frequently mention emigrants being crushed by wagon wheels or injured during river crossings.");
                
        DEATH_DETAILS.put("snakebite", "Venomous snake encounters, particularly with rattlesnakes, " +
                "were a serious danger. Without antivenom, bites often proved fatal, especially for " +
                "children. Journals from the trail describe terrifying encounters with rattlesnakes " +
                "hiding in tall grass, rocky areas, or even inside wagon boxes.");
                
        DEATH_DETAILS.put("childbirth", "Childbirth on the trail was extremely dangerous, with limited medical assistance " +
                "and harsh conditions. Women often had to give birth in or under wagons, with only other " +
                "women to help. Many died from complications like hemorrhage or infection. Despite these " +
                "dangers, many women were pregnant during the journey, and diaries record both tragic " +
                "losses and miraculous survivals along the trail.");
                
        DEATH_DETAILS.put("fever", "Various fevers claimed many lives on the frontier. Without modern " +
                "medicine, even a common fever could prove fatal. Pioneer diaries frequently mention " +
                "entire families falling ill with 'fever' that would sweep through wagon trains. " +
                "The isolation of trail life meant that medical help was often unavailable when needed most.");
                
        DEATH_DETAILS.put("poor health", "The cumulative effects of trail hardships - inadequate nutrition, " +
                "contaminated water, exposure to elements, and constant physical strain - gradually wore down " +
                "even the strongest pioneers. Many succumbed not to a single disease but to the overall " +
                "deterioration of health during the months-long journey west.");
        
        DEATH_DETAILS.put("exposure to cold", "Bitter winter weather was a deadly threat on the trail. " +
                "Inadequate shelter, clothing, and fuel could quickly lead to hypothermia and frostbite. " +
                "Wagon trains caught in early winter storms sometimes lost multiple members to freezing. " +
                "The most infamous example, the Donner Party, suffered devastating losses when trapped " +
                "by snow in the Sierra Nevada mountains during the winter of 1846-1847.");
                
        DEATH_DETAILS.put("heat stroke", "The extreme heat of prairie and desert crossings claimed many lives. " +
                "Water sources were often miles apart, and temperatures could soar above 100°F in summer months. " +
                "Heat stroke and dehydration could kill quickly, especially affecting the young, elderly, and ill. " +
                "Many emigrants described the torment of crossing regions like the 40-Mile Desert, where wagons " +
                "traveled day and night to survive the waterless stretch.");
                
        DEATH_DETAILS.put("lightning strike", "Thunderstorms were both terrifying and deadly on the open plains. " +
                "With little shelter, emigrants were vulnerable to lightning strikes during sudden storms. " +
                "Pioneer diaries record incidents of people and livestock being struck and killed instantly. " +
                "The metal components of wagons could attract lightning, making storms particularly dangerous " +
                "when no natural shelter was available.");
                
        DEATH_DETAILS.put("childbirth complications", "Childbirth on the trail was extremely dangerous. " +
                "With no proper medical care, women often faced severe complications during labor " +
                "in primitive conditions. Many mothers died from blood loss, infection, or other " +
                "complications that would be easily treated today. The journey west claimed the lives " +
                "of many young women as they tried to bring new life into the world under the harshest " +
                "conditions imaginable.");
    }
    
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
        setSize(700, 650);
        setResizable(false);
    }
    
    private void initUI(String causeOfDeath, int days, int distance, String lastLocation) {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Title panel - use the actual death message instead of generic text
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);
        titlePanel.setBorder(new EmptyBorder(10, 10, 5, 10));
        
        // Check if cause of death is properly set
        String deathTitle;
        if (causeOfDeath == null || causeOfDeath.trim().isEmpty()) {
            deathTitle = "You have died on the trail";
        } else {
            deathTitle = "You have died of " + causeOfDeath;
        }
        
        JLabel titleLabel = new JLabel(deathTitle, JLabel.CENTER);
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
        
        // Get normalized cause of death for detail lookup
        String normalizedCause = causeOfDeath.toLowerCase().trim();
        if (normalizedCause.contains("dysentery")) normalizedCause = "dysentery";
        else if (normalizedCause.contains("cholera")) normalizedCause = "cholera";
        else if (normalizedCause.contains("typhoid")) normalizedCause = "typhoid";
        else if (normalizedCause.contains("measles")) normalizedCause = "measles";
        else if (normalizedCause.contains("exhaust")) normalizedCause = "exhaustion";
        else if (normalizedCause.contains("starv")) normalizedCause = "starvation";
        else if (normalizedCause.contains("drown")) normalizedCause = "drowning";
        else if (normalizedCause.contains("expos") && normalizedCause.contains("cold")) normalizedCause = "exposure to cold";
        else if (normalizedCause.contains("expos")) normalizedCause = "exposure"; // Generic exposure
        else if (normalizedCause.contains("heat")) normalizedCause = "heat stroke";
        else if (normalizedCause.contains("lightning")) normalizedCause = "lightning strike";
        else if (normalizedCause.contains("snake")) normalizedCause = "snakebite";
        else if (normalizedCause.contains("accident")) normalizedCause = "accident";
        else if (normalizedCause.contains("childbirth") && normalizedCause.contains("complication")) normalizedCause = "childbirth complications";
        else if (normalizedCause.contains("childbirth")) normalizedCause = "childbirth complications";
        else if (normalizedCause.contains("fever")) normalizedCause = "fever";
        else if (normalizedCause.contains("health")) normalizedCause = "poor health";
        
        // Debug log to see what the normalized cause is
        System.out.println("Debug - Original death cause: '" + causeOfDeath + "', Normalized to: '" + normalizedCause + "'");
        
        // Create the text panel first - moved up from center to top
        JPanel textPanel = new JPanel(new BorderLayout(0, 15));
        textPanel.setBackground(PANEL_COLOR);
        
        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        statsPanel.setBackground(PANEL_COLOR);
        
        JLabel daysLabel = new JLabel("Days on trail: " + days);
        daysLabel.setFont(FontManager.getWesternFont(16));
        daysLabel.setForeground(TEXT_COLOR);
        
        JLabel distanceLabel = new JLabel("Distance traveled: " + distance + " miles");
        distanceLabel.setFont(FontManager.getWesternFont(16));
        distanceLabel.setForeground(TEXT_COLOR);
        
        JLabel locationLabel = new JLabel("Last location: near " + lastLocation);
        locationLabel.setFont(FontManager.getWesternFont(16));
        locationLabel.setForeground(TEXT_COLOR);
        
        statsPanel.add(daysLabel);
        statsPanel.add(distanceLabel);
        statsPanel.add(locationLabel);
        
        // Get detailed death information if available
        String deathDetail = DEATH_DETAILS.getOrDefault(normalizedCause, 
            "Many emigrants died on the journey west. It's estimated that one in ten pioneers " +
            "died on the trail, with disease being the most common cause of death. Those who died " +
            "were often buried in shallow graves along the trail, marked with simple wooden crosses.");
        
        // Historical detail about this type of death
        JTextArea deathDetailArea = new JTextArea(deathDetail);
        deathDetailArea.setFont(FontManager.getWesternFont(14));
        deathDetailArea.setLineWrap(true);
        deathDetailArea.setWrapStyleWord(true);
        deathDetailArea.setEditable(false);
        deathDetailArea.setBackground(PANEL_COLOR);
        deathDetailArea.setForeground(TEXT_COLOR);
        
        // Combine stats and historical details
        textPanel.add(statsPanel, BorderLayout.NORTH);
        textPanel.add(deathDetailArea, BorderLayout.CENTER);
        
        // Add text panel to the content panel
        contentPanel.add(textPanel, BorderLayout.CENTER);
        
        // Try to load custom image, fallback to drawn tombstone if not available
        // Now this will go at the bottom instead of the left
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(PANEL_COLOR);
        // Remove the border to let image fill the space
        
        boolean customImageLoaded = false;
        
        // Load the custom image using ResourceLoader
        try {
            ImageIcon customIcon = ResourceLoader.loadImage(CUSTOM_IMAGE_PATH);
            if (customIcon != null && customIcon.getIconWidth() > 0) {
                // Calculate available height to prevent overlap with text
                int availableHeight = 250; // Adjusted height to prevent overlap
                
                // Scale the image to fit available space while preserving aspect ratio
                double ratio = (double) customIcon.getIconWidth() / customIcon.getIconHeight();
                int targetHeight = availableHeight;
                int targetWidth = (int) (targetHeight * ratio);
                
                Image scaledImage = customIcon.getImage().getScaledInstance(
                        targetWidth, targetHeight, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                imageLabel.setHorizontalAlignment(JLabel.CENTER); // Center the image
                imageLabel.setBorder(null);
                imagePanel.add(imageLabel, BorderLayout.CENTER);
                customImageLoaded = true;
            }
        } catch (Exception e) {
            System.err.println("Error loading custom death image: " + e.getMessage());
        }
        
        // Only add the image panel if a custom image was successfully loaded
        if (customImageLoaded) {
            contentPanel.add(imagePanel, BorderLayout.SOUTH);
        }
        
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