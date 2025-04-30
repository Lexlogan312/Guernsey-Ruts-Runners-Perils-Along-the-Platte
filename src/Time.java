public class Time {
    private int year;
    private int month;
    private int day;
    private int totalDays;

    public Time(int year, int month) {
        this.year = year;
        this.month = month;
        this.day = 1;
        this.totalDays = 0;
    }

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

    public boolean isLeapYear() {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

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

    public int getDay() {
        return day;
    }

    public int getTotalDays() {
        return totalDays;
    }
}