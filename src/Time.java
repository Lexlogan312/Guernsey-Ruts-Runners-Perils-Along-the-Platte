/**
 * Time Class of the Perils Along the Platte Game
 * Manages the game's calendar system, tracking days, months, and years.
 * Handles date advancement and provides methods for date-related calculations.
 *
 * @author Alex Randall and Chase McCluskey
 * @version 1.0
 * @date 05/06/2025
 * @file Time.java
 */
public class Time {
    // The current year in the game
    private int year;
    
    // The current month in the game
    private int month;
    
    // The current day of the month
    private int day;
    
    // The total number of days that have passed since the start of the game
    private int totalDays;

    /**
     * Constructs a new Time object starting at the beginning of the specified month.
     * Initializes the time system with:
     * - Specified year and month
     * - Day set to 1
     * - Total days counter set to 0
     * 
     * @param year The starting year (must be a valid year)
     * @param month The starting month (must be between 1 and 12)
     */
    public Time(int year, int month) {
        this.year = year;
        this.month = month;
        this.day = 1;
        this.totalDays = 0;
    }

    /**
     * Advances the current date by one day.
     * Handles month and year transitions automatically.
     * Updates the total days counter.
     * 
     * Month transitions:
     * - 31-day months: January, March, May, July, August, October, December
     * - 30-day months: April, June, September, November
     * - February: 28 days (29 in leap years)
     * 
     * This method is called:
     * - At the end of each game day
     * - During time-based events
     * - When skipping days
     */
    public void advanceDay() {
        this.day++;
        this.totalDays++;

        // Check if we need to advance to next month
        int daysInMonth = getDaysInMonth();
        if (this.day > daysInMonth) {
            this.day = 1;
            this.month++;

            // Check if we need to advance to next year
            if (this.month > 12) {
                this.month = 1;
                this.year++;
            }
        }
    }

    /**
     * Gets the number of days in the current month.
     * Accounts for leap years when calculating February's length.
     * 
     * @return The number of days in the current month:
     *         - 31 days: January, March, May, July, August, October, December
     *         - 30 days: April, June, September, November
     *         - 28/29 days: February (29 in leap years)
     */
    public int getDaysInMonth() {
        switch (this.month) {
            case 2: // February
                return isLeapYear() ? 29 : 28;
            case 4: // April
            case 6: // June
            case 9: // September
            case 11: // November
                return 30;
            default:
                return 31;
        }
    }

    /**
     * Checks if the current year is a leap year.
     * A year is a leap year if:
     * - It is divisible by 4 but not by 100, or
     * - It is divisible by 400
     * 
     * This calculation follows the Gregorian calendar rules
     * established in 1582 and used during the westward migration.
     * 
     * @return true if the current year is a leap year, false otherwise
     */
    public boolean isLeapYear() {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    /**
     * Gets the current year in the game.
     * Returns the historical year of the journey.
     * 
     * @return The current year
     */
    public int getYear() {
        return year;
    }

    /**
     * Gets the current month in the game.
     * Returns the month number (1-12).
     *
     * @return The current month (1-12)
     */
    public int getMonth() {
        return month;
    }

    /**
     * Gets the name of the current month.
     * Returns the full name of the month in English.
     * 
     * @return The name of the current month:
     *         - January, February, March, April, May, June,
     *         - July, August, September, October, November, December
     *         - Returns "Unknown" for invalid month numbers
     */
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

    /**
     * Gets the current day of the month.
     * Returns the day number (1-31).
     * 
     * @return The current day (1-31)
     */
    public int getDay() {
        return day;
    }

    /**
     * Gets the total number of days that have passed since the start of the game.
     * This counter is incremented each time advanceDay() is called.
     * Used for tracking overall game progression and historical timeline.
     * 
     * @return The total number of days since game start
     */
    public int getTotalDays() {
        return totalDays;
    }
}