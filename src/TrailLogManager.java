import java.util.ArrayList;
import java.util.List;

/**
 * Manages the trail log entries and historical notes for the game.
 * This class helps keep track of all updates that occur during the journey.
 */
public class TrailLogManager {
    private ArrayList<LogEntry> trailLog;
    private int maxEntries;
    private Time gameTime;

    // Define log entry categories
    public enum LogCategory {
        TRAVEL,
        REST,
        HUNT,
        RIVER_CROSSING,
        LANDMARK,
        HISTORICAL_FACT,
        SURVIVAL_TIP,
        WARNING,
        EVENT,
        GENERAL,
        JOURNAL,
        REPAIR,
        FOOD_SPOILAGE,
        PART_BREAKAGE
    }

    /**
     * Update the current game date
     *
     * @param newTime The new game time
     */
    public void updateGameDate(Time newTime) {
        this.gameTime = newTime;
    }

    /**
     * Get all log entries
     *
     * @return ArrayList of all log entries
     */
    public ArrayList<LogEntry> getAllEntries() {
        return new ArrayList<>(trailLog);
    }

    /**
     * Get recent log entries
     *
     * @param count Number of recent entries to retrieve
     * @return List of recent log entries
     */
    public List<LogEntry> getRecentEntries(int count) {
        int startIndex = Math.max(0, trailLog.size() - count);
        return trailLog.subList(startIndex, trailLog.size());
    }

    /**
     * Get log entries of a specific category
     *
     * @param category The category to filter by
     * @return List of log entries matching the category
     */
    public List<LogEntry> getEntriesByCategory(LogCategory category) {
        ArrayList<LogEntry> filteredEntries = new ArrayList<>();
        for (LogEntry entry : trailLog) {
            if (entry.getCategory() == category) {
                filteredEntries.add(entry);
            }
        }
        return filteredEntries;
    }

    /**
     * Get entries from a specific date range
     *
     * @param startTime Start of date range
     * @param endTime   End of date range
     * @return List of log entries within the date range
     */
    public List<LogEntry> getEntriesInDateRange(Time startTime, Time endTime) {
        ArrayList<LogEntry> filteredEntries = new ArrayList<>();
        for (LogEntry entry : trailLog) {
            Time entryTime = entry.getTime();
            if (entryTime.getTotalDays() >= startTime.getTotalDays() && 
                entryTime.getTotalDays() <= endTime.getTotalDays()) {
                filteredEntries.add(entry);
            }
        }
        return filteredEntries;
    }

    /**
     * Export the trail log as a formatted journal
     *
     * @return Formatted string representation of the trail log
     */
    public String exportTrailJournal() {
        StringBuilder journal = new StringBuilder();
        journal.append("=== ").append(gameTime.getTrailName()).append(" JOURNEY JOURNAL ===\n");
        journal.append("Date: ").append(gameTime.getMonthName()).append(" ")
              .append(gameTime.getDay()).append(", ")
              .append(gameTime.getYear()).append("\n\n");
        
        for (LogEntry entry : trailLog) {
            journal.append(entry.getDate().getMonthName())
                  .append(" ")
                  .append(entry.getDate().getDay())
                  .append(", ")
                  .append(entry.getDate().getYear())
                  .append(" - ")
                  .append(entry.getLocation())
                  .append("\n")
                  .append(entry.getMessage())
                  .append("\n\n");
        }
        
        return journal.toString();
    }

    /**
     * Represents a single log entry in the trail journal
     */
    public static class LogEntry {
        private final Time date;
        private final String message;
        private final String location;
        private final LogCategory category;

        public LogEntry(Time date, String message, String location, LogCategory category) {
            this.date = date;
            this.message = message;
            this.location = location;
            this.category = category;
        }

        public Time getDate() {
            return date;
        }

        public String getMessage() {
            return message;
        }

        public String getLocation() {
            return location;
        }

        public LogCategory getCategory() {
            return category;
        }

        public Time getTime() {
            return date;
        }

        public String getFormattedDate() {
            return date.getMonthName() + " " + date.getDay() + ", " + date.getYear();
        }

        @Override
        public String toString() {
            return getFormattedDate() + " - " + message;
        }
    }

    public TrailLogManager(Time startTime) {
        trailLog = new ArrayList<>();
        maxEntries = 100;
        gameTime = startTime;
    }

    /**
     * Add a new entry to the trail log
     *
     * @param message  The log message
     * @param location The location of the log entry
     * @param category The category of message (regular, historical, warning, etc.)
     */
    public void addLogEntry(String message, String location, LogCategory category) {
        LogEntry entry = new LogEntry(gameTime, message, location, category);
        trailLog.add(entry);

        // Trim log if it exceeds maximum size
        if (trailLog.size() > maxEntries) {
            trailLog.remove(0);
        }
    }
}