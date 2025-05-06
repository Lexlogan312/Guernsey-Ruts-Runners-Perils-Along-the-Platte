/**
 * Weather Class of the Perils Along the Platte Game
 * Manages weather conditions and their effects on gameplay.
 * Generates weather based on season and location, and provides methods
 * to adjust travel distances and other game mechanics based on current conditions.
 *
 * @author Alex Randall and Chase McCluskey
 * @version 1.0
 * @date 05/06/2025
 * @file Weather.java
 */

public class Weather {
    // The current weather condition description
    private String currentWeather;
    
    // The severity of the current weather condition
    private int severity;

    /**
     * Constructs a new Weather object based on the current month and location.
     * Initializes weather conditions using historical patterns and location-specific factors.
     * 
     * @param month The current month (1-12)
     * @param location The current location on the trail
     */
    public Weather(int month, String location) {
        generateWeather(month, location != null ? location : "");
    }

    /**
     * Generates weather conditions based on season and location.
     * Uses probability distributions to determine weather type and severity.
     * 
     * Weather probabilities are influenced by:
     * - Seasonal patterns
     * - Geographic location
     * - Historical weather data
     * - Random variation
     * 
     * @param month The current month (1-12)
     * @param location The current location on the trail
     */
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
            if (Math.random() < 0.3) {
                currentWeather = "Hot and clear";
                severity = 3;
            } else {
                currentWeather = "Clear";
                severity = 1;
            }
            return;
        }

        total += cloudyProbability;
        if (random < total) {
            currentWeather = "Cloudy";
            severity = 2;
            return;
        }

        total += fogProbability;
        if (random < total) {
            currentWeather = "Foggy";
            severity = 3;
            return;
        }

        // Default to clear weather if no other conditions met
        currentWeather = "Clear";
        severity = 1;
    }

    /**
     * Gets the current weather condition.
     * Returns a descriptive string of the current weather state.
     * 
     * @return The current weather description
     */
    public String getCurrentWeather() {
        return currentWeather;
    }

    /**
     * Gets the severity of the current weather.
     * Returns a value from 1-5 indicating the impact of the weather.
     * 
     * @return The weather severity (1-5, where 1 is best and 5 is worst)
     */
    public int getSeverity() {
        return severity;
    }

    /**
     * Adjusts the base travel distance based on current weather conditions.
     * Applies weather-specific modifiers to the base travel distance.
     * 
     * Weather modifiers:
     * - Clear: 100% of base distance
     * - Cloudy: 90% of base distance
     * - Light rain: 80% of base distance
     * - Heavy rain: 50% of base distance
     * - Snow: 60% of base distance
     * - Blizzard: 30% of base distance
     * - Foggy: 70% of base distance
     * - Hot and clear: 80% of base distance
     * 
     * @param baseMiles The base distance to travel
     * @return The adjusted travel distance
     */
    public int adjustTravelDistance(int baseMiles) {
        double modifier = 1.0;
        
        switch (currentWeather) {
            case "Blizzard":
                modifier = 0.3;
                break;
            case "Heavy rain":
                modifier = 0.5;
                break;
            case "Snow":
                modifier = 0.6;
                break;
            case "Foggy":
                modifier = 0.7;
                break;
            case "Light rain":
                modifier = 0.8;
                break;
            case "Cloudy":
                modifier = 0.9;
                break;
            case "Hot and clear":
                modifier = 0.8;
                break;
            case "Clear":
                modifier = 1.0;
                break;
        }
        
        return (int)(baseMiles * modifier);
    }
}