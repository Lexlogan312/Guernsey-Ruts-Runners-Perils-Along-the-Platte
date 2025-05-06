import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Manages the trail log entries and historical notes for the game.
 * This class helps keep track of all updates that occur during the journey.
 */
public class TrailLogManager {
    private ArrayList<LogEntry> trailLog;
    private int maxEntries;
    private Calendar gameCalendar;
    private SimpleDateFormat dateFormat;

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
     * @param newDate The new game date
     */
    public void updateGameDate(Calendar newDate) {
        this.gameCalendar = (Calendar) newDate.clone();
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
     * @param startDate Start of date range
     * @param endDate   End of date range
     * @return List of log entries within the date range
     */
    public List<LogEntry> getEntriesInDateRange(Calendar startDate, Calendar endDate) {
        ArrayList<LogEntry> filteredEntries = new ArrayList<>();
        for (LogEntry entry : trailLog) {
            if (entry.getDate().after(startDate) && entry.getDate().before(endDate)) {
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
        journal.append("======= OREGON TRAIL JOURNAL =======\n\n");

        String currentDate = null;

        for (LogEntry entry : trailLog) {
            String entryDate = entry.getFormattedDate();

            // Add date headers when the date changes
            if (currentDate == null || !currentDate.equals(entryDate)) {
                currentDate = entryDate;
                journal.append("\n--- ").append(currentDate).append(" ---\n\n");
            }

            // Format based on category
            switch (entry.getCategory()) {
                case HISTORICAL_FACT:
                    journal.append("📜 ");
                    break;
                case SURVIVAL_TIP:
                    journal.append("💡 ");
                    break;
                case WARNING:
                    journal.append("⚠️ ");
                    break;
                case LANDMARK:
                    journal.append("🏞️ ");
                    break;
                case RIVER_CROSSING:
                    journal.append("🌊 ");
                    break;
                case EVENT:
                    journal.append("⚡ ");
                    break;
                default:
                    journal.append("• ");
            }

            journal.append(entry.getMessage()).append("\n");
        }

        return journal.toString();
    }

    /**
     * Represents a single log entry in the trail journal
     */
    public class LogEntry {
        private String message;
        private LogCategory category;
        private Calendar date;

        public LogEntry(String message, LogCategory category, Calendar date) {
            this.message = message;
            this.category = category;
            this.date = date;
        }

        public String getMessage() {
            return message;
        }

        public LogCategory getCategory() {
            return category;
        }

        public Calendar getDate() {
            return date;
        }

        public String getFormattedDate() {
            return dateFormat.format(date.getTime());
        }

        @Override
        public String toString() {
            return getFormattedDate() + " - " + message;
        }
    }

    public TrailLogManager(Calendar startDate) {
        trailLog = new ArrayList<>();
        maxEntries = 100;
        gameCalendar = (startDate != null) ? (Calendar) startDate.clone() : Calendar.getInstance(); // Fallback to current date
        dateFormat = new SimpleDateFormat("MMMM d, yyyy");
    }


    /**
     * Add a new entry to the trail log
     *
     * @param message  The log message
     * @param category The category of message (regular, historical, warning, etc.)
     */
    public void addLogEntry(String message, LogCategory category) {
        LogEntry entry = new LogEntry(message, category, (Calendar) gameCalendar.clone());
        trailLog.add(entry);

        // Trim log if it exceeds maximum size
        if (trailLog.size() > maxEntries) {
            trailLog.remove(0);
        }
    }
}