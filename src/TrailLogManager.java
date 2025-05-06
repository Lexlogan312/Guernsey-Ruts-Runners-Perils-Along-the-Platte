/**
 * TrailLogManager Class of the Perils Along the Platte Game
 * Manages the trail log entries and event records during the journey.
 * 
 * This class handles:
 * - Creation and storage of log entries for all game events
 * - Categorization of events by type (travel, rest, hunt, etc.)
 * - Tracking of player actions and game events with timestamps
 * - Maintenance of a chronological record of the journey
 * - Support for historical and journal features
 *
 * @author Alex Randall and Chase McCluskey
 * @version 1.0
 * @date 05/06/2025
 * @file TrailLogManager.java
 */

import java.util.ArrayList;

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