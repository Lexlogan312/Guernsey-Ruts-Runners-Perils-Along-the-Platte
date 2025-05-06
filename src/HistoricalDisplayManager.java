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
        this.currentLocation = "Unknown";
        this.currentActivity = "Unknown";
    }

    /**
     * Update the current location and activity for contextual historical data
     * @param location Current location on the trail
     */
    public void updateContext(String location) {
        this.currentLocation = location;
    }

    /**
     * Get a formatted version of the latest historical fact for display in the UI
     * @return Formatted historical fact as HTML or rich text for UI display
     */
    public String getLatestHistoricalFactForDisplay() {
        // Get the latest historical fact from the trail log
        List<TrailLogManager.LogEntry> historicalEntries =
                trailLog.getEntriesByCategory(TrailLogManager.LogCategory.HISTORICAL_FACT);

        if (historicalEntries.isEmpty()) {
            return ""; // No historical facts logged yet
        }

        TrailLogManager.LogEntry latestEntry = historicalEntries.getLast();

        // Format it for display
        return "<html><div style='width: 300px; padding: 10px; background-color: #f7f3e9; border: 1px solid #d2b48c;'>" +
                "<h3 style='color: #8b4513; margin-top: 0;'>Historical Note</h3>" +
                "<p style='font-style: italic;'>" + latestEntry.getMessage() + "</p>" +
                "<p style='color: #666; text-align: right; margin-bottom: 0;'>" + latestEntry.getFormattedDate() + "</p>" +
                "</div></html>";
    }

    /**
     * Get a random historical fact for a specific category
     * This is useful for displaying information in a help screen or tutorial
     * @param category The category of fact to retrieve
     * @return A formatted historical fact
     */
    public String getRandomFactByType(String category) {
        String fact = switch (category.toLowerCase()) {
            case "pioneer" -> historicalData.getRandomPioneerFact(currentLocation, currentActivity);
            case "trail" -> historicalData.getRandomTrailFact(currentLocation, currentActivity);
            case "survival" -> historicalData.getRandomSurvivalTip(currentLocation, currentActivity);
            default -> historicalData.getRandomHistoricalData(currentLocation, currentActivity);
        };

        // Log the fact in the trail log
        trailLog.addLogEntry(fact, currentLocation,TrailLogManager.LogCategory.HISTORICAL_FACT);

        // Format for display
        return fact;
    }

    /**
     * Get a location-specific historical fact
     * @return A formatted location-specific fact
     */
    public String getLocationSpecificFact() {
        String fact = historicalData.getLocationSpecificData(currentLocation);

        // Log the fact in the trail log
        trailLog.addLogEntry(fact, currentLocation, TrailLogManager.LogCategory.HISTORICAL_FACT);

        return fact;
    }

    /**
     * Get contextual historical information based on current activity
     * @return A formatted contextual historical fact
     */
    public String getContextualHistoricalFact() {
        String fact = historicalData.getContextualHistoricalData(currentActivity, currentLocation);

        // Log the fact in the trail log
        trailLog.addLogEntry(fact, currentLocation, TrailLogManager.LogCategory.HISTORICAL_FACT);

        return fact;
    }

    /**
     * Get all historical facts for a specific category as a formatted report
     * This is useful for the player to browse through all facts they've encountered
     * @param category The category to retrieve
     * @return A formatted report of all facts in the category
     */
    public String getHistoricalReport(TrailLogManager.LogCategory category) {
        List<TrailLogManager.LogEntry> entries = trailLog.getEntriesByCategory(category);

        if (entries.isEmpty()) {
            return "No historical information has been recorded in this category yet.";
        }

        StringBuilder report = new StringBuilder();
        report.append("==== Historical Information ====\n\n");

        for (TrailLogManager.LogEntry entry : entries) {
            report.append(entry.getFormattedDate()).append(":\n");
            report.append(entry.getMessage()).append("\n\n");
        }

        return report.toString();
    }

    /**
     * Create a popup notification for a new historical fact
     * @param fact The historical fact to display
     * @return A formatted notification for display in a popup
     */
    public String createHistoricalFactPopup(String fact) {
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
            Time time = entry.getTimeViewed();
            report.append("Date: ").append(time.getMonthName()).append(" ").append(time.getDay()).append(", ").append(time.getYear()).append("\n");
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
            Time time = entry.getTimeViewed();
            report.append("Date: ").append(time.getMonthName()).append(" ").append(time.getDay()).append(", ").append(time.getYear()).append("\n");
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
     * Get journal entries for a specific date
     * @param date The date to retrieve journal entries for
     * @return A formatted string of journal entries for the specified date
     */
    public String getJournalByDate(Time date) {
        List<HistoricalData.JournalEntry> entries = historicalData.getEntriesByDate(date);

        if (entries.isEmpty()) {
            return "No historical information has been recorded for this date.";
        }

        StringBuilder report = new StringBuilder();
        report.append("==== Journal Entries for ").append(date.getMonthName()).append(" ").append(date.getDay()).append(", ").append(date.getYear()).append(" ====\n\n");

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