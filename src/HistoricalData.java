// First, let's create a class to store our historical data
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HistoricalData {
    private ArrayList<String> pioneerFacts;
    private ArrayList<String> trailFacts;
    private ArrayList<String> survivalTips;
    private ArrayList<String> landmarkDescriptions;
    private final Random random;

    // Journal tracking structures
    private ArrayList<JournalEntry> journalEntries;
    private Map<String, Boolean> viewedPioneerFacts;
    private Map<String, Boolean> viewedTrailFacts;
    private Map<String, Boolean> viewedSurvivalTips;
    private Map<String, Boolean> viewedLandmarkDescriptions;

    public final Time time;

    public HistoricalData(Time time) {
        if (time == null) {
            throw new IllegalArgumentException("Time cannot be null");
        }
        this.time = time;
        random = new Random();
        initializePioneerFacts();
        initializeTrailFacts();
        initializeSurvivalTips();

        // Initialize tracking structures
        journalEntries = new ArrayList<>();
        viewedPioneerFacts = new HashMap<>();
        viewedTrailFacts = new HashMap<>();
        viewedSurvivalTips = new HashMap<>();
        viewedLandmarkDescriptions = new HashMap<>();
    }

    // Methods to add custom historical data
    public void addPioneerFact(String fact) {
        pioneerFacts.add(fact);
    }

    public void addTrailFact(String fact) {
        trailFacts.add(fact);
    }

    public void addSurvivalTip(String tip) {
        survivalTips.add(tip);
    }

    public void addLandmarkDescription(String description) {
        landmarkDescriptions.add(description);
    }

    private void initializePioneerFacts() {
        pioneerFacts = new ArrayList<>();
        pioneerFacts.add("Many pioneers traveled with only the belongings that could fit in their wagon, often abandoning items along the trail when their oxen became too weak.");
        pioneerFacts.add("A typical wagon could hold about 2,000 pounds of supplies, but the weight had to be carefully balanced.");
        pioneerFacts.add("Children on the Oregon Trail would often walk alongside the wagons to reduce the strain on the oxen.");
        pioneerFacts.add("The journey on the Oregon Trail typically took 4-6 months to complete.");
        pioneerFacts.add("Pioneers would form wagon trains for protection, typically consisting of 20-40 wagons.");
        pioneerFacts.add("Many pioneers marked their journeys by carving their names on landmarks like Independence Rock.");
        pioneerFacts.add("Wagon wheels were often wrapped with iron to prevent wear, but they still needed frequent repairs.");
        pioneerFacts.add("Women on the trail were responsible for cooking, childcare, and often helped with hunting and wagon repairs.");
        pioneerFacts.add("Pioneers would often travel 15-20 miles per day when conditions were favorable.");
        pioneerFacts.add("Many travelers kept detailed journals of their journey west, providing historians with valuable information.");
    }

    private void initializeTrailFacts() {
        trailFacts = new ArrayList<>();
        trailFacts.add("The Oregon Trail stretched approximately 2,170 miles from Independence, Missouri to Oregon City, Oregon.");
        trailFacts.add("The trail followed rivers whenever possible to ensure access to water.");
        trailFacts.add("Disease was the biggest killer on the trail, with cholera being particularly deadly.");
        trailFacts.add("Crossing rivers was one of the most dangerous parts of the journey, with many drownings occurring.");
        trailFacts.add("The trail was marked with ruts from thousands of wagon wheels, some of which are still visible today.");
        trailFacts.add("Fort Laramie was a major resupply point and resting area for weary travelers.");
        trailFacts.add("The Barlow Road, built in 1846, provided the first land route around Mount Hood into the Willamette Valley.");
        trailFacts.add("South Pass in Wyoming was a relatively easy mountain crossing and a crucial part of the trail.");
        trailFacts.add("Native American tribes sometimes offered assistance to pioneers, helping with river crossings or trading supplies.");
        trailFacts.add("The peak years of Oregon Trail migration were between 1843 and 1869, before the transcontinental railroad was completed.");
    }

    private void initializeSurvivalTips() {
        survivalTips = new ArrayList<>();
        survivalTips.add("Pioneer wisdom: When thunder rolls across the plains, seek shelter quickly as lightning strikes are common.");
        survivalTips.add("Experienced travelers know to layer clothing rather than wearing single heavy garments to better adapt to changing temperatures.");
        survivalTips.add("A mixture of buffalo tallow and beeswax was used by pioneers to waterproof leather goods and wagon covers.");
        survivalTips.add("Pioneers would place pebbles in their mouths to generate saliva during times of extreme thirst when water was scarce.");
        survivalTips.add("Wild onions and garlic were collected not just for food but as natural remedies for colds and infections on the trail.");
        survivalTips.add("Trail veterans advised keeping a small bag of salt in a dry place, invaluable for preserving meat and treating wounds.");
        survivalTips.add("Experienced wagon masters knew to cross rivers early in the morning when water levels were typically at their lowest.");
        survivalTips.add("A wise pioneer never travels during midday heat in summer months - morning and evening travel preserves the strength of oxen.");
        survivalTips.add("The oxen's health is your lifeline - check their hooves daily and apply pine tar to cracks before they worsen.");
        survivalTips.add("When food runs low, prairie turnips can be dug from the ground and wild berries gathered along wooded creek beds.");
    }

    // Inner class to represent a journal entry
    public static class JournalEntry {
        private final String content;
        private final String location;
        private final String activity;
        private final String type;
        private final int year;
        private final int month;
        private final int day;
        private final String trailName;

        public JournalEntry(String content, String location, String activity, String type, Time time) {
            this.content = content;
            this.location = location;
            this.activity = activity;
            this.type = type;
            this.year = time.getYear();
            this.month = time.getMonth();
            this.day = time.getDay();
            this.trailName = time.getTrailName();
        }

        public String getContent() { return content; }
        public String getLocation() { return location; }
        public String getActivity() { return activity; }
        public String getType() { return type; }
        
        public String getMonthName() {
            switch (this.month) {
                case 1: return "January";
                case 2: return "February";
                case 3: return "March";
                case 4: return "April";
                case 5: return "May";
                case 6: return "June";
                case 7: return "July";
                case 8: return "August";
                case 9: return "September";
                case 10: return "October";
                case 11: return "November";
                case 12: return "December";
                default: return "Unknown";
            }
        }
        
        public int getYear() { return year; }
        public int getMonth() { return month; }
        public int getDay() { return day; }
        public String getTrailName() { return trailName; }

        @Override
        public String toString() {
            return String.format("%s - %s - %s\n%s", 
                getMonthName(), 
                day, 
                year,
                content);
        }
    }

    // Method to track when a fact is presented to the user
    private void trackFactPresentation(String content, String type, String location, String activity) {
        // Create and add the journal entry with the current time
        JournalEntry entry = new JournalEntry(content, location, activity, type, time);
        journalEntries.add(entry);

        // Mark as viewed in the appropriate map
        switch (type) {
            case "Pioneer Fact":
                viewedPioneerFacts.put(content, true);
                break;
            case "Trail Fact":
                viewedTrailFacts.put(content, true);
                break;
            case "Survival Tip":
                viewedSurvivalTips.put(content, true);
                break;
            case "Landmark Description":
                viewedLandmarkDescriptions.put(content, true);
                break;
        }
    }

    // Modified methods to track facts when they're presented
    public String getRandomPioneerFact(String currentLocation, String currentActivity) {
        String fact = pioneerFacts.get(random.nextInt(pioneerFacts.size()));
        String formattedFact = fact;
        trackFactPresentation(fact, "Pioneer Fact", currentLocation, currentActivity);
        return formattedFact;
    }

    // Overload for backward compatibility
    public String getRandomPioneerFact() {
        return getRandomPioneerFact("Unknown", "Unknown");
    }

    public String getRandomTrailFact(String currentLocation, String currentActivity) {
        String fact = trailFacts.get(random.nextInt(trailFacts.size()));
        String formattedFact = fact;
        trackFactPresentation(fact, "Trail Fact", currentLocation, currentActivity);
        return formattedFact;
    }

    // Overload for backward compatibility
    public String getRandomTrailFact() {
        return getRandomTrailFact("Unknown", "Unknown");
    }

    public String getRandomSurvivalTip(String currentLocation, String currentActivity) {
        String tip = survivalTips.get(random.nextInt(survivalTips.size()));
        String formattedTip = "Survival Tips: \n" + tip;
        trackFactPresentation(tip, "Survival Tip", currentLocation, currentActivity);
        return formattedTip;
    }

    // Overload for backward compatibility
    public String getRandomSurvivalTip() {
        return getRandomSurvivalTip("Unknown", "Unknown");
    }

    // Modified general method with tracking
    public String getRandomHistoricalData(String currentLocation, String currentActivity) {
        int category = random.nextInt(4);
        switch (category) {
            case 0:
                return getRandomPioneerFact(currentLocation, currentActivity);
            case 1:
                return getRandomTrailFact(currentLocation, currentActivity);
            case 2:
                return getRandomSurvivalTip(currentLocation, currentActivity);
            default:
                return null;
        }
    }

    // Overload for backward compatibility
    public String getRandomHistoricalData() {
        return getRandomHistoricalData("Unknown", "Unknown");
    }

    // Modified contextual method with tracking
    public String getContextualHistoricalData(String activity, String currentLocation) {
        switch (activity.toLowerCase()) {
            case "travel":
                // When traveling, focus on trail facts and landmarks
                return random.nextBoolean() ?
                        getRandomTrailFact(currentLocation, activity) :
                        getRandomPioneerFact(currentLocation, activity);
            case "rest":
                // When resting, focus on pioneer facts and survival tips
                return random.nextBoolean() ?
                        getRandomPioneerFact(currentLocation, activity) :
                        getRandomSurvivalTip(currentLocation, activity);
            case "hunt":
                // When hunting, focus on survival tips
                return getRandomSurvivalTip(currentLocation, activity);
            default:
                return getRandomHistoricalData(currentLocation, activity);
        }
    }

    // Modified location-specific method with tracking
    public String getLocationSpecificData(String location) {
        String content;

        // Custom facts for important locations on the trail
        switch (location.toLowerCase()) {
            case "fort laramie":
                content = "Fort Laramie was originally a fur trading post established in 1834. By the 1840s, it became a crucial resupply point for pioneers on the Oregon Trail.";
                break;
            case "independence rock":
                content = "Independence Rock was named because pioneers tried to reach it by July 4th (Independence Day). Arriving later meant risking early snowfall in the mountains.";
                break;
            case "chimney rock":
                content = "Chimney Rock served as a natural lighthouse for pioneers. Many described it as resembling a factory chimney, rising about 300 feet from its base.";
                break;
            case "south pass":
                content = "South Pass was discovered in 1812 and provided a relatively easy crossing through the Rocky Mountains, with a gentle incline rather than steep slopes.";
                break;
            case "fort hall":
                content = "Fort Hall was established in 1834 as a trading post. It later became an important stop for Oregon Trail travelers where they could trade and rest.";
                break;
            case "three island crossing":
                content = "The Three Island Crossing on the Snake River was one of the most dangerous river crossings. Some pioneers chose a longer, safer route rather than risk the crossing.";
                break;
            case "fort boise":
                content = "Fort Boise, established in 1834, offered protection and supplies to weary travelers after the difficult crossing of the Snake River.";
                break;
            case "the dalles":
                content = "The Dalles marked the end of the overland portion of the Oregon Trail for many years. From here, pioneers initially rafted down the Columbia River.";
                break;
            case "barlow road":
                content = "The Barlow Road, opened in 1846, provided an alternative to the dangerous Columbia River route, allowing wagons to cross the Cascade Mountains.";
                break;
            case "oregon city":
                content = "Oregon City, established in 1829, was the end of the Oregon Trail and the first U.S. city west of the Rocky Mountains to be incorporated.";
                break;
            case "river crossing":
                content = "River crossings were among the most dangerous parts of the journey. Pioneers often caulked their wagon beds and floated them across deep rivers.";
                break;
            case "mountain passage":
                content = "Mountain passages slowed travel considerably. Pioneers often had to double-team oxen to pull wagons up steep slopes, and use ropes to lower them down the other side.";
                break;
            case "plains":
                content = "The Great Plains offered relatively easy travel but little shelter from storms. Pioneers learned to watch the horizon carefully for approaching weather.";
                break;
            case "desert":
                content = "Desert crossings were planned carefully, often traveling at night to avoid the heat. Water barrels were filled to capacity before entering arid regions.";
                break;
            default:
                // If no specific location match, just return a random trail fact
                return getRandomTrailFact(location, "Traveling");
        }

        // Track this landmark description
        trackFactPresentation(content, "Landmark Description", location, "Traveling");
        return content;
    }

    // Journal retrieval methods

    // Get all journal entries
    public ArrayList<JournalEntry> getAllJournalEntries() {
        return journalEntries;
    }

    // Get entries by type
    public ArrayList<JournalEntry> getEntriesByType(String type) {
        ArrayList<JournalEntry> filteredEntries = new ArrayList<>();
        for (JournalEntry entry : journalEntries) {
            if (entry.getType().equals(type)) {
                filteredEntries.add(entry);
            }
        }
        return filteredEntries;
    }

    // Get entries by location
    public ArrayList<JournalEntry> getEntriesByLocation(String location) {
        ArrayList<JournalEntry> filteredEntries = new ArrayList<>();
        for (JournalEntry entry : journalEntries) {
            if (entry.getLocation().equalsIgnoreCase(location)) {
                filteredEntries.add(entry);
            }
        }
        return filteredEntries;
    }

    // Get entries by activity
    public ArrayList<JournalEntry> getEntriesByActivity(String activity) {
        ArrayList<JournalEntry> filteredEntries = new ArrayList<>();
        for (JournalEntry entry : journalEntries) {
            if (entry.getActivity().equalsIgnoreCase(activity)) {
                filteredEntries.add(entry);
            }
        }
        return filteredEntries;
    }

    // Get entries by date
    public ArrayList<JournalEntry> getEntriesByDate(Time date) {
        ArrayList<JournalEntry> filteredEntries = new ArrayList<>();
        for (JournalEntry entry : journalEntries) {
            if (entry.getYear() == date.getYear() && 
                entry.getMonth() == date.getMonth() && 
                entry.getDay() == date.getDay()) {
                filteredEntries.add(entry);
            }
        }
        return filteredEntries;
    }

    // Get count of unique facts viewed
    public int getUniqueFactsViewedCount() {
        return viewedPioneerFacts.size() + viewedTrailFacts.size() +
                viewedSurvivalTips.size() + viewedLandmarkDescriptions.size();
    }

    // Get percentage of all facts viewed
    public double getPercentageOfFactsViewed() {
        int totalFacts = pioneerFacts.size() + trailFacts.size() +
                survivalTips.size() + landmarkDescriptions.size();
        int viewedFacts = getUniqueFactsViewedCount();

        if (totalFacts == 0) return 0.0;
        return (double) viewedFacts / totalFacts * 100.0;
    }

    // Export journal to formatted string
    public String exportJournalToString() {
        StringBuilder journalText = new StringBuilder();
        journalText.append(time.getTrailName()).append(" JOURNEY JOURNAL\n");
        journalText.append("Date: ").append(time.getMonthName()).append(" ")
                  .append(time.getDay()).append(", ")
                  .append(time.getYear()).append("\n");
        journalText.append("==========================\n\n");

        for (JournalEntry entry : journalEntries) {
            journalText.append("Date: ").append(entry.getMonthName() + " " + 
                                              entry.getDay() + ", " + 
                                              entry.getYear()).append("\n");
            journalText.append("Location: ").append(entry.getLocation()).append("\n");
            journalText.append("Activity: ").append(entry.getActivity()).append("\n");
            journalText.append("Type: ").append(entry.getType()).append("\n\n");
            journalText.append(entry.getContent()).append("\n\n");
            journalText.append("----------------------------\n\n");
        }

        return journalText.toString();
    }
}