import java.util.List;

/**
 * A utility class to manage the display of historical data in the game UI.
 * This helps with formatting and displaying historical facts in a UI-friendly way.
 * Enhanced to work with the journal tracking system in HistoricalData.
 */
public class HistoricalDisplayManager {
    private final HistoricalData historicalData;
    private final TrailLogManager trailLog;
    private String currentLocation;
    private String currentActivity;

    public HistoricalDisplayManager(HistoricalData historicalData, TrailLogManager trailLog) {
        this.historicalData = historicalData;
        this.trailLog = trailLog;
        this.currentLocation = "Starting Point";
        this.currentActivity = "setup";
    }

    /**
     * Update the current location and activity for contextual historical data
     * @param location Current location on the trail
     * @param activity Current activity (e.g., "travel", "rest", "hunt", etc.)
     */
    public void updateContext(String location, String activity) {
        if (location != null && !location.isEmpty()) {
            this.currentLocation = location;
        }
        
        if (activity != null && !activity.isEmpty()) {
            this.currentActivity = activity;
        }
    }

    /**
     * Update just the current location, keeping the current activity
     * @param location Current location on the trail
     */
    public void updateContext(String location) {
        if (location != null && !location.isEmpty()) {
            this.currentLocation = location;
        }
    }

    /**
     * Get contextual historical information based on current activity
     * @return A formatted contextual historical fact
     */
    public String getContextualHistoricalFact() {
        // Make sure we're using a valid activity value
        String activity = (currentActivity != null && !currentActivity.isEmpty()) ? 
                          currentActivity : "general";
                          
        String fact = historicalData.getContextualHistoricalData(activity, currentLocation);

        // Log the fact in the trail log
        trailLog.addLogEntry(fact, currentLocation, TrailLogManager.LogCategory.HISTORICAL_FACT);

        return fact;
    }

    /**
     * Get the complete journal of historical information encountered during the journey
     * @return A formatted journal string
     */
    public String getCompleteJournal() {
        return historicalData.exportJournalToString();
    }

    /**
     * Get journal entries for the current location
     * @return A formatted string of journal entries for the current location
     */
    public String getLocationJournal() {
        List<HistoricalData.JournalEntry> entries = historicalData.getEntriesByLocation(currentLocation);

        if (entries.isEmpty()) {
            return "No information has been recorded about " + currentLocation + " yet.";
        }

        StringBuilder report = new StringBuilder();
        report.append("==== Journal Entries for ").append(currentLocation).append(" ====\n\n");

        for (HistoricalData.JournalEntry entry : entries) {
            report.append("Date: ").append(entry.getMonthName()).append(" ").append(entry.getDay()).append(", ").append(entry.getYear()).append("\n");
            report.append("Activity: ").append(entry.getActivity()).append("\n");
            report.append("Type: ").append(entry.getType()).append("\n\n");
            report.append(entry.getContent()).append("\n\n");
            report.append("----------------------------\n\n");
        }

        return report.toString();
    }

    /**
     * Get journal entries by type (Pioneer Facts, Trail Facts, Survival Tips, etc.)
     * @param type The type of journal entries to retrieve
     * @return A formatted string of journal entries of the specified type
     */
    public String getJournalByType(String type) {
        List<HistoricalData.JournalEntry> entries = historicalData.getEntriesByType(type);

        if (entries.isEmpty()) {
            return "No information of type '" + type + "' has been recorded yet.";
        }

        StringBuilder report = new StringBuilder();
        report.append("==== Journal Entries: ").append(type).append(" ====\n\n");

        for (HistoricalData.JournalEntry entry : entries) {
            report.append("Date: ").append(entry.getMonthName()).append(" ").append(entry.getDay()).append(", ").append(entry.getYear()).append("\n");
            report.append("Location: ").append(entry.getLocation()).append("\n");
            report.append("Activity: ").append(entry.getActivity()).append("\n\n");
            report.append(entry.getContent()).append("\n\n");
            report.append("----------------------------\n\n");
        }

        return report.toString();
    }

    /**
     * Get journal entries for today's date
     * @return A formatted string of today's journal entries
     */
    public String getTodaysJournal() {
        List<HistoricalData.JournalEntry> entries = historicalData.getEntriesByDate(historicalData.time);

        if (entries.isEmpty()) {
            return "No historical information has been recorded today.";
        }

        StringBuilder report = new StringBuilder();
        report.append("==== Today's Journal Entries ====\n\n");

        for (HistoricalData.JournalEntry entry : entries) {
            report.append("Location: ").append(entry.getLocation()).append("\n");
            report.append("Activity: ").append(entry.getActivity()).append("\n");
            report.append("Type: ").append(entry.getType()).append("\n\n");
            report.append(entry.getContent()).append("\n\n");
            report.append("----------------------------\n\n");
        }

        return report.toString();
    }

    /**
     * Create a formatted summary of historical knowledge gained during the journey
     * @return A formatted string showing historical learning progress
     */
    public String getHistoricalKnowledgeSummary() {
        int totalFacts = historicalData.getUniqueFactsViewedCount();
        double percentageSeen = historicalData.getPercentageOfFactsViewed();

        StringBuilder summary = new StringBuilder();
        summary.append("==== Historical Knowledge Summary ====\n\n");
        summary.append("Unique historical facts discovered: ").append(totalFacts).append("\n");
        summary.append("Percentage of available knowledge: ").append(String.format("%.1f%%", percentageSeen)).append("\n\n");

        // Add information about each type of fact
        List<HistoricalData.JournalEntry> pioneerEntries = historicalData.getEntriesByType("Pioneer Fact");
        List<HistoricalData.JournalEntry> trailEntries = historicalData.getEntriesByType("Trail Fact");
        List<HistoricalData.JournalEntry> survivalEntries = historicalData.getEntriesByType("Survival Tip");
        List<HistoricalData.JournalEntry> landmarkEntries = historicalData.getEntriesByType("Landmark Description");

        summary.append("Pioneer Facts: ").append(pioneerEntries.size()).append("\n");
        summary.append("Trail Facts: ").append(trailEntries.size()).append("\n");
        summary.append("Survival Tips: ").append(survivalEntries.size()).append("\n");
        summary.append("Landmark Descriptions: ").append(landmarkEntries.size()).append("\n\n");

        // Most visited locations
        summary.append("Locations with most historical discoveries:\n");
        // This would require additional processing to count entries by location
        // Simplified version for now
        summary.append("(Complete this section with location counts)\n\n");

        return summary.toString();
    }
}