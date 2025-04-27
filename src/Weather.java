public class Weather {
    private String currentWeather;
    private int severity; // 1-5 scale, 1 = best, 5 = worst

    public Weather(int month, String location) {
        generateWeather(month, location != null ? location : "");
    }

    private void generateWeather(int month, String location) {
        // Base probability factors for different weather types based on month
        double rainProbability = 0.0;
        double snowProbability = 0.0;
        double clearProbability = 0.0;
        double cloudyProbability = 0.0;
        double fogProbability = 0.0;

        // Adjust probabilities based on month (season)
        switch (month) {
            case 12: case 1: case 2: // Winter
                snowProbability = 0.5;
                rainProbability = 0.1;
                clearProbability = 0.2;
                cloudyProbability = 0.15;
                fogProbability = 0.05;
                break;
            case 3: case 4: case 5: // Spring
                rainProbability = 0.4;
                snowProbability = 0.1;
                clearProbability = 0.3;
                cloudyProbability = 0.15;
                fogProbability = 0.05;
                break;
            case 6: case 7: case 8: // Summer
                clearProbability = 0.6;
                rainProbability = 0.2;
                cloudyProbability = 0.15;
                snowProbability = 0.0;
                fogProbability = 0.05;
                break;
            case 9: case 10: case 11: // Fall
                rainProbability = 0.3;
                clearProbability = 0.3;
                cloudyProbability = 0.2;
                snowProbability = 0.1;
                fogProbability = 0.1;
                break;
            default: // Default to spring-like weather for invalid months
                rainProbability = 0.4;
                snowProbability = 0.1;
                clearProbability = 0.3;
                cloudyProbability = 0.15;
                fogProbability = 0.05;
                break;
        }

        // Adjust probabilities further based on specific locations
        if (location != null) {
            if (location.contains("Mountains")) {
                snowProbability += 0.2;
                rainProbability += 0.1;
                fogProbability += 0.1;
                clearProbability -= 0.2;
            } else if (location.contains("River")) {
                fogProbability += 0.15;
                rainProbability += 0.1;
            } else if (location.contains("Desert") || location.contains("Pass")) {
                clearProbability += 0.2;
                rainProbability -= 0.1;
                if (rainProbability < 0) rainProbability = 0;
            }
        }

        // Determine the weather
        double random = Math.random();
        double total = 0.0;

        total += rainProbability;
        if (random < total) {
            if (Math.random() < 0.3) {
                currentWeather = "Heavy rain";
                severity = 4;
            } else {
                currentWeather = "Light rain";
                severity = 2;
            }
            return;
        }

        total += snowProbability;
        if (random < total) {
            if (Math.random() < 0.3) {
                currentWeather = "Blizzard";
                severity = 5;
            } else {
                currentWeather = "Snow";
                severity = 3;
            }
            return;
        }

        total += clearProbability;
        if (random < total) {
            currentWeather = "Clear";
            severity = 1;
            return;
        }

        total += cloudyProbability;
        if (random < total) {
            currentWeather = "Cloudy";
            severity = 2;
            return;
        }

        // Fog or other
        currentWeather = "Foggy";
        severity = 3;
    }

    public String getCurrentWeather() {
        return currentWeather;
    }

    public int getSeverity() {
        return severity;
    }

    public int adjustTravelDistance(int baseMiles) {
        double factor = 1.0;

        switch (severity) {
            case 1: // Perfect weather
                factor = 1.2; // 20% bonus
                break;
            case 2: // Minor impediment
                factor = 1.0; // No change
                break;
            case 3: // Moderate challenge
                factor = 0.8; // 20% reduction
                break;
            case 4: // Significant challenge
                factor = 0.6; // 40% reduction
                break;
            case 5: // Severe weather
                factor = 0.3; // 70% reduction
                break;
        }

        return (int)(baseMiles * factor);
    }
}