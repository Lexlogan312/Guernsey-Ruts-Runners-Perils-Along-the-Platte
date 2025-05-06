/**
 * GameController Class of the Perils Along the Platte Game
 * Central controller class that manages game state, player actions, and game progression.
 * Handles all major game mechanics including:
 * - Travel and journey management
 * - Resource consumption and management
 * - Event generation and handling
 * - Player health and morale
 * - Weather and environmental effects
 * - Game state tracking and updates
 * - Landmark and river crossing events
 * - Hunting and resting mechanics
 * - Market interactions
 * - Trail-specific gameplay variations
 *
 * @author Alex Randall and Chase McCluskey
 * @version 1.0
 * @date 05/06/2025
 * @file GameController.java
 */

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.awt.Frame;

public class GameController {
    // Tracks player stats, family, and health
    private Player player;

    // Manages locations, landmarks, and travel progress
    private Map map;

    // Handles supplies, items, and resource management
    private final Inventory inventory;

    // Tracks date, season, and day/night cycles
    private Time time;

    // Manages environmental conditions and effects
    private Weather weather;

    // Handles random events and challenges
    private Perils perils;

    // Tracks player's occupation and associated benefits
    private Job job;

    //The selected trail for the journey.
    private String trail;

    private HistoricalData historicalDataManager;

    private TrailLogManager trailLog;

    private HistoricalDisplayManager historicalDisplayManager;

    private boolean gameStarted = false;
    private boolean isGameRunning = true;

    private final ArrayList<String> initialJourneyEvents = new ArrayList<>();
    private final ArrayList<Landmark> initialLandmarksPassed = new ArrayList<>();
    private int initialFoodConsumed = 0;
    private int initialPartsUsed = 0;
    private int initialMedicineUsed = 0;
    private int initialAmmoUsed = 0;
    private static double currentSpeedMultiplier = 1.0; //Full Speed

    // List of available food items
    private static final String[] FOOD_TYPES = {
            "Flour", "Bacon", "Dried Beans", "Rice", "Coffee", "Sugar", "Dried Fruit", "Hardtack"
    };

    // Corresponding spoilage rates (0.0 to 1.0)
    private static final double[] FOOD_SPOILRATE = {
            .05, .15, .02, .03, .005, .04, .1, .01
    };

    // Available wagon parts that can break or be replaced.
    private static final String[] WAGON_PARTS = {
            "Wheel", "Bow", "Tongue", "Axle"
    };

    // Event listeners for game updates and state changes.
    private final ArrayList<Consumer<String>> messageListeners = new ArrayList<>();
    private final ArrayList<Runnable> gameStateListeners = new ArrayList<>();

    private boolean isTraveling = false;
    private String currentSpeed = "Normal";

    /**
     * Constructs a new GameController with default game state.
     * Initializes core game components with default values that will be
     * updated during game setup. Sets up the initial game environment
     * before player customization.
     */
    public GameController() {
        // Initialize game components
        player = new Player("Player", "Male", job);
        inventory = new Inventory();
        time = new Time(1848, 3);
        map = new Map(1);
        weather = new Weather(time.getMonth(), map.getStartingLocation());
        perils = new Perils(player, inventory, weather, time);
        perils.setMessageListener(this::notifyListeners);
        historicalDataManager = new HistoricalData(time);
        trailLog = new TrailLogManager(time);
        historicalDisplayManager = new HistoricalDisplayManager(historicalDataManager, trailLog);
    }

    /**
     * Starts a new game after initial setup is complete.
     * Initializes game state based on player choices and validates
     * all required components are properly initialized.
     * Sets up the initial journey conditions and game state.
     */
    public void startNewGame() {
        // Start the game
        gameStarted = true;
        isGameRunning = true;

        if (player != null && inventory != null && weather != null) {
            perils = new Perils(player, inventory, weather, time);
            perils.setMessageListener(this::notifyListeners);
        } else {
            String error = "Error in startNewGame: Player, Inventory, or Weather is null.";
            System.err.println(error);
            notifyListeners("ERROR: Failed to initialize game components fully.");
            isGameRunning = false;
        }
    }

    /**
     * Adds a message listener to receive game event notifications.
     * Listeners will receive updates about game events, status changes,
     * and important messages. Messages are delivered on the EDT.
     * 
     * @param listener The Consumer<String> that will receive game messages
     */
    public void addMessageListener(Consumer<String> listener) {
        messageListeners.add(listener);
    }

    /**
     * Adds a listener for game state changes.
     * These listeners are notified when significant game state changes occur,
     * such as completing a journey or reaching a landmark.
     * State changes are delivered on the EDT.
     * 
     * @param listener The Runnable to be called when game state changes
     */
    public void addGameStateListener(Runnable listener) {
        gameStateListeners.add(listener);
    }

    /**
     * Notifies all registered message listeners of a new message.
     * Ensures the notification happens on the Event Dispatch Thread
     * to maintain thread safety with Swing components.
     * 
     * @param message The message to send to all listeners
     */
    private void notifyListeners(String message) {
        SwingUtilities.invokeLater(() -> {
            for (Consumer<String> listener : messageListeners) {
                listener.accept(message);
            }
        });
    }

    /**
     * Sets up the player with basic information and family members.
     * Initializes the player character with their chosen attributes
     * and sets up their family group for the journey.
     * 
     * @param name The player's name
     * @param gender The player's gender
     * @param familyMembers Array of family member names
     * @param job The player's occupation
     */
    public void playerSetup(String name, String gender, String[] familyMembers, Job job) {
        player = new Player(name, gender, job);
        player.setFamilyMembers(familyMembers);
        if (inventory != null && weather != null) {
            perils = new Perils(player, inventory, weather, time);
            perils.setMessageListener(this::notifyListeners);
        }
    }

    /**
     * Sets the trail choice and initializes the map accordingly.
     * Configures the game for the selected trail route, including
     * starting location and available landmarks.
     * 
     * @param trailChoice The selected trail (1=Oregon, 2=California, 3=Mormon)
     */
    public void selectTrail(int trailChoice) {
        // Set the trail
        switch (trailChoice) {
            case 1:
                trail = "Oregon";
                break;
            case 2:
                trail = "California";
                break;
            case 3:
                trail = "Mormon";
                break;
        }
        map = new Map(trailChoice);
        if (time != null) {
            weather = new Weather(time.getMonth(), map.getStartingLocation());
            if (player != null && inventory != null) {
                perils = new Perils(player, inventory, weather, time);
                perils.setMessageListener(this::notifyListeners);
            }
        }
    }

    /**
     * Sets the departure month and initializes related game components.
     * Configures the starting season and initial weather conditions
     * based on the chosen departure month.
     *
     * @param month The selected departure month (1-5, representing March-July)
     */
    public void selectDepartureMonth(int month) {
        int monthChoice = month - 1;
        int monthNumber = monthChoice + 3;
        time = new Time(1848, monthNumber);

        String startLoc = (map != null) ? map.getStartingLocation() : "Start";
        weather = new Weather(monthNumber, startLoc);

        if (player != null && inventory != null) {
            perils = new Perils(player, inventory, weather, time);
            perils.setMessageListener(this::notifyListeners);
        }

        this.historicalDataManager = new HistoricalData(this.time);

        // Initialize or update TrailLogManager with the correct Time object
        if (this.trailLog == null) {
            this.trailLog = new TrailLogManager(this.time);
        } else {
            this.trailLog.updateGameDate(this.time); // Update existing log manager's time
        }

        // Re-initialize HistoricalDisplayManager to link to the updated data managers
        this.historicalDisplayManager = new HistoricalDisplayManager(this.historicalDataManager, this.trailLog);

        // Update the trail name in the time object after the trail has been selected
        if(map != null && this.time != null){
            this.time.setTrailName(map.getTrailName().toUpperCase());
        }
        notifyGameStateChanged();
    }

    /**
     * Simulates the initial journey to Fort Kearny.
     * Tracks events, resource consumption, and shows a summary dialog upon completion.
     * This method handles the first leg of the journey, including:
     * - Daily travel progress
     * - Resource consumption
     * - Random events
     * - Landmark encounters
     * - Health and morale changes
     */
    public void journeyToFortKearny() {
        if (!validateGameComponents()) return;

        initialJourneyEvents.clear();
        initialLandmarksPassed.clear();
        resetInitialConsumption();

        int fortKearnyDistance = findFortKearnyDistance();
        Landmark fortKearnyLandmark = findFortKearnyLandmark();

        if (fortKearnyDistance <= 0 || fortKearnyLandmark == null) {
            notifyListeners("ERROR: Could not find Fort Kearny on the map. Cannot simulate journey.");
            return;
        }

        collectInitialLandmarks(fortKearnyDistance);

        int averageDailyDistance = 15;
        int daysToFortKearny = 0;
        int distanceCovered = 0;

        while (distanceCovered < fortKearnyDistance && !player.isDead()) {
            daysToFortKearny++;
            time.advanceDay();
            weather = new Weather(time.getMonth(), map.getCurrentLocation());

            int dailyDistance = calculateDailyDistance(averageDailyDistance);
            if (distanceCovered + dailyDistance > fortKearnyDistance) {
                dailyDistance = fortKearnyDistance - distanceCovered;
            }
            distanceCovered += dailyDistance;

            consumeDailyFood(daysToFortKearny);
            simulateDailyOxenFatigue();
            simulateInitialJourneyEvent(daysToFortKearny);

            if (player.isDead()) {
                handleInitialJourneyDeath(daysToFortKearny);
                return;
            }
        }

        if (!player.isDead()) {
            map.travel(distanceCovered);
            map.setCurrentLocation(fortKearnyLandmark.getName());
            notifyListeners("\n=== JOURNEY TO FORT KEARNY COMPLETE ===\n" +
                    "Arrived after " + daysToFortKearny + " days.");
        }

        showTravelSummaryDialog(daysToFortKearny, distanceCovered);
    }

    /**
     * Validates that all required game components are properly initialized.
     * 
     * @return true if all components are valid, false otherwise
     */
    private boolean validateGameComponents() {
        if (map == null || time == null || player == null || inventory == null || perils == null) {
            String missing = "";
            if (map == null) missing += "Map ";
            if (time == null) missing += "Time ";
            if (player == null) missing += "Player ";
            if (inventory == null) missing += "Inventory ";
            if (perils == null) missing += "Perils ";
            notifyListeners("ERROR: Game not fully initialized. Missing: " + missing.trim());
            System.err.println("ERROR: Game not fully initialized. Missing: " + missing.trim());
            isGameRunning = false;
            return false;
        }
        return true;
    }

    /**
     * Resets the tracking of initial resource consumption.
     */
    private void resetInitialConsumption() {
        initialFoodConsumed = 0;
        initialPartsUsed = 0;
        initialMedicineUsed = 0;
        initialAmmoUsed = 0;
    }

    /**
     * Finds the distance to Fort Kearny from the starting location.
     * 
     * @return The distance to Fort Kearny in miles
     */
    private int findFortKearnyDistance() {
        if (map == null || map.getLandmarks() == null) return 0;
        for (Landmark lm : map.getLandmarks()) {
            if (lm.getName().contains("Fort Kearny")) {
                return lm.getDistance();
            }
        }
        System.err.println("Error: Fort Kearny distance not found.");
        return 0;
    }

    /**
     * Locates the Fort Kearny landmark in the map.
     * 
     * @return The Fort Kearny landmark object, or null if not found
     */
    private Landmark findFortKearnyLandmark() {
        if (map == null || map.getLandmarks() == null) return null;
        for (Landmark lm : map.getLandmarks()) {
            if (lm.getName().contains("Fort Kearny")) {
                return lm;
            }
        }
        System.err.println("Error: Fort Kearny landmark object not found.");
        return null;
    }

    /**
     * Collects all landmarks between the starting location and Fort Kearny.
     * 
     * @param fortKearnyDist The distance to Fort Kearny
     */
    private void collectInitialLandmarks(int fortKearnyDist) {
        if (map == null || map.getLandmarks() == null) return;
        for (Landmark landmark : map.getLandmarks()) {
            if (landmark.getDistance() < fortKearnyDist) {
                initialLandmarksPassed.add(landmark);
            } else {
                break;
            }
        }
    }

    /**
     * Calculates the daily travel distance based on base speed and conditions.
     * 
     * @param base The base daily travel distance
     * @return The adjusted daily travel distance
     */
    private int calculateDailyDistance(int base) {
        int dailyDistance = base;
        dailyDistance = weather.adjustTravelDistance(dailyDistance);
        dailyDistance = (int)(dailyDistance * Math.max(0.1, inventory.getOxenHealth() / 100.0));
        return Math.max(0, dailyDistance);
    }

    /**
     * Simulates daily food consumption during the initial journey.
     * 
     * @param currentDay The current day of the journey
     */
    private void consumeDailyFood(int currentDay) {
        int foodNeeded = player.getFamilySize() * 2;
        int foodAvailable = inventory.getFood();

        if (foodAvailable >= foodNeeded) {
            inventory.consumeFood(foodNeeded);
            initialFoodConsumed += foodNeeded;
        } else {
            inventory.consumeFood(foodAvailable);
            initialFoodConsumed += foodAvailable;
            
            int healthPenalty = 5 + (foodNeeded - foodAvailable);
            player.decreaseHealth(healthPenalty, "starvation");
            initialJourneyEvents.add("Day " + currentDay + ": Ran low on food! Lost " + healthPenalty + " health.");
            
            if (player.isDead()) {
                initialJourneyEvents.add("Tragically, you starved before reaching Fort Kearny.");
                handleInitialJourneyDeath(currentDay);
            }
        }
    }

    /**
     * Simulates daily oxen fatigue during travel.
     */
    private void simulateDailyOxenFatigue() {
        if (Math.random() < 0.10) {
            inventory.decreaseOxenHealth(2);
        }
    }

    /**
     * Generates and handles random events during the initial journey.
     * 
     * @param currentDay The current day of the journey
     */
    private void simulateInitialJourneyEvent(int currentDay) {
        if (Math.random() < 0.25) {
            List<String> eventDetails = new ArrayList<>();
            Consumer<String> eventCaptureListener = eventText -> {
                String cleaned = eventText.replace("\n", " ").replace("===", "").trim();
                if (!cleaned.isEmpty()) eventDetails.add(cleaned);
            };

            Consumer<String> originalListener = this::notifyListeners;
            perils.setMessageListener(eventCaptureListener);

            int partsBefore = inventory.getWagonParts();
            int medicineBefore = inventory.getMedicine();
            int ammoBefore = inventory.getAmmunition();

            perils.generateRandomEvent();

            initialPartsUsed += Math.max(0, partsBefore - inventory.getWagonParts());
            initialMedicineUsed += Math.max(0, medicineBefore - inventory.getMedicine());
            initialAmmoUsed += Math.max(0, ammoBefore - inventory.getAmmunition());

            perils.setMessageListener(originalListener);

            if (!eventDetails.isEmpty()) {
                initialJourneyEvents.add("Day " + currentDay + ": " + String.join(" ", eventDetails));
            }
        }
    }

    /**
     * Handles player death during the initial journey.
     * 
     * @param days The number of days traveled before death
     */
    private void handleInitialJourneyDeath(int days) {
        isGameRunning = false;
        
        String causeOfDeath = player.getCauseOfDeath();
        if (causeOfDeath == null || causeOfDeath.trim().isEmpty()) {
            causeOfDeath = "poor health";
            player.setCauseOfDeath(causeOfDeath);
        }
        
        String deathMessage = "Died of " + causeOfDeath + " after " + days + " days, before reaching Fort Kearny.";
        initialJourneyEvents.add(deathMessage);
        notifyListeners("\n" + deathMessage);
        showDeathDialog();
    }

    /**
     * Notifies all game state listeners of a state change.
     */
    private void notifyGameStateChanged() {
        SwingUtilities.invokeLater(() -> {
            for (Runnable listener : gameStateListeners) {
                listener.run();
            }
        });
    }

    /**
     * Shows a dialog summarizing the initial journey to Fort Kearny.
     * 
     * @param days The number of days taken
     * @param distance The distance traveled
     */
    private void showTravelSummaryDialog(int days, int distance) {
        final int finalFood = initialFoodConsumed;
        final int finalParts = initialPartsUsed;
        final int finalMeds = initialMedicineUsed;
        final int finalAmmo = initialAmmoUsed;

        SwingUtilities.invokeLater(() -> {
            Frame owner = findVisibleFrame();
            TravelSummaryDialog summaryDialog = new TravelSummaryDialog(
                    owner, initialJourneyEvents, initialLandmarksPassed,
                    days, distance, finalFood, finalParts, finalMeds, finalAmmo
            );
            summaryDialog.setVisible(true);
            notifyGameStateChanged();
        });
    }

    // Main Game Loop Actions (Called by GUI Buttons)

    /** Travel action for one day. */
    public void travel() {
        if (!isGameRunning || !validateGameComponents()) return;

        int baseDistance = 15;
        int adjustedDistance = calculateDailyDistance(baseDistance);

        StringBuilder result = new StringBuilder();

        if (adjustedDistance <= 0) {
            notifyListeners("Cannot travel today (check oxen health).");
            consumeDailyFood(time.getTotalDays() + 1);
            advanceDay(false);
            return;
        }

        // Check for broken parts and apply currentSpeedMultiplier
        if (inventory.hasBrokenParts()) {
            // Apply the current speed multiplier (which would have been set by reduceSpeed)
            adjustedDistance = (int)(adjustedDistance * currentSpeedMultiplier);
            result.append("Travel is slowed due to broken wagon parts.\n");

            // Get the specific broken part to show appropriate message
            String brokenPart = inventory.checkForPartBreakage(this);
            if (brokenPart != null) {
                result.append("Your broken " + brokenPart + " is reducing your travel speed to " +
                        String.format("%.0f", currentSpeedMultiplier * 100) + "% of normal.\n");
            }
        } else {
            // If no parts are broken, ensure speed is restored to normal
            restoreSpeed();
        }

        String currentWeather = weather.getCurrentWeather();
        if (currentWeather.contains("Rain") || currentWeather.contains("Snow")) {
            adjustedDistance = (int)(adjustedDistance * 0.7); // 30% slower in rain/snow
        } else if (currentWeather.contains("Storm")) {
            adjustedDistance = (int)(adjustedDistance * 0.5); // 50% slower in storms
        }

        // Update oxen fatigue based on travel distance and weather
        inventory.updateOxenFatigue(adjustedDistance, currentWeather, false);

        // Adjust travel distance based on oxen health and fatigue
        double oxenHealthFactor = inventory.getOxenHealth() / 100.0;
        double oxenFatigueFactor = 1.0 - (inventory.getOxenFatigue() / 200.0); // Fatigue reduces speed up to 50%
        adjustedDistance = (int)(adjustedDistance * oxenHealthFactor * oxenFatigueFactor);

        // Travel happens first
        map.travel(adjustedDistance);
        addTravelJournalEntry();

        // Check for part breakage and handle it properly
        String breakageResult = inventory.checkForPartBreakage(this);
        if (breakageResult != null) {
            result.append(breakageResult).append("\n");

            // Parse the breakage result to get the part name
            // Assuming breakageResult format is "Your [part] has broken!"
            String partName = null;
            if (breakageResult.contains("Wheel")) partName = "Wheel";
            else if (breakageResult.contains("Axle")) partName = "Axle";
            else if (breakageResult.contains("Tongue")) partName = "Tongue";
            else if (breakageResult.contains("Bow")) partName = "Bow";

            // Apply appropriate speed reduction based on part
            if (partName != null) {
                if (partName.equals("Wheel")) {
                    reduceSpeed(0.5); // Reduce travel speed by 50%
                } else if (partName.equals("Axle")) {
                    reduceSpeed(0.1); // Almost no travel until fixed
                } else if (partName.equals("Tongue")) {
                    reduceSpeed(0.7); // Reduce travel speed by 30%
                } else if (partName.equals("Bow")) {
                    reduceSpeed(0.8); // Reduce travel speed by 20%
                }

                // Call handlePartBreakage to show repair options and update game state
                handlePartBreakage(partName);
            }
        }

        // Apply food spoilage
        inventory.applyFoodSpoilage(weather, this);

        // Fatigue, Health decrease from normal trail wear
        player.decreaseHealth(5); // Base fatigue damage per day of travel

        // Health-based Risk Warning
        if (player.getHealth() <= 30) {
            notifyListeners("Your party is severely weakened. It is recommended to rest soon or risk breakdowns and deaths.");
        }

        // Blacksmith Wagon Part Check
        if (player.getJob() == Job.BLACKSMITH) {
            for (int i = 0; i < WAGON_PARTS.length; i++) {
                int partHealth = inventory.getWagonPartBreakpercentage(WAGON_PARTS[i]);
                if (partHealth < 50) {
                    notifyListeners("Your " + WAGON_PARTS[i] + " is in poor condition. Resting could allow repairs.");
                }
            }
        }

        // Random Injury Chance
        if (Math.random() < 0.1) {
            player.decreaseHealth(2);
            notifyListeners("The rough trail caused some minor injuries and fatigue.");
        }

        // Daily food consumption
        int foodConsumedToday = player.getFamilySize() * 2;
        consumeDailyFood(time.getTotalDays() + 1);

        // Spoilage check on random food item
        int randomFood = (int)(Math.random() * FOOD_TYPES.length);
        String itemName = inventory.getItem(FOOD_TYPES[randomFood]);

        if (itemName != null) {
            double spoilRate = inventory.getSpoilRate(itemName);
            // Farmer Bonus: food spoils slower
            if (player.getJob() == Job.FARMER) {
                spoilRate *= 0.75; // 25% less spoilage
            }
            int spoiledFood = (int)(spoilRate * inventory.getWeight(itemName));
            if (spoiledFood > 0) {
                foodConsumedToday += spoiledFood;
                inventory.consumeFood(spoiledFood);
                notifyListeners(spoiledFood + " pounds of " + itemName + " spoiled during travel.");
            }
        }

        // Display travel and next landmark information
        notifyListeners("You traveled " + adjustedDistance + " miles today.\n" +
                "Food consumed: " + foodConsumedToday + " pounds.");

        // Add information about distance to next landmark if available
        int distanceToNext = map.getDistanceToNextLandmark();
        if (distanceToNext > 0) {
            notifyListeners("Distance to next landmark: " + distanceToNext + " miles.");
        } else if (distanceToNext == 0) {
            notifyListeners("You've reached the next landmark!");
        }

        addTrailUpdate("Traveled " + adjustedDistance + " miles.", TrailLogManager.LogCategory.TRAVEL);

        // Advance Day
        advanceDay(true);

        // Notify listeners of travel results
        if (result.length() > 0) {
            notifyListeners(result.toString());
        }

        // Add trail updates for oxen condition
        if (inventory.getOxenFatigue() >= 80) {
            addTrailUpdate("Your oxen are severely fatigued and struggling to pull the wagon.");
        } else if (inventory.getOxenFatigue() >= 60) {
            addTrailUpdate("Your oxen are showing signs of fatigue.");
        }

        if (inventory.getOxenHealth() <= 30) {
            addTrailUpdate("Your oxen are in poor health and need rest.");
        }

        if(!(map.getDistanceToNextLandmark() < 10)) {
            // Random chance to display historical data (30% chance)
            if (Math.random() < 0.5) {
                String historicalNote = historicalDataManager.getContextualHistoricalData("travel", map.getCurrentLocation());
                addTrailUpdate("TRAIL NOTE: " + historicalNote);
            } else if (Math.random() > 0.6) {
                String travelingHistoricalData = historicalDataManager.getContextualHistoricalData("travel", map.getCurrentLocation());
                notifyListeners("\nTraveling Historical Information: \n" + travelingHistoricalData);
            } else if (Math.random() > 0.5) {
                notifyListeners(historicalDataManager.getRandomHistoricalData(map.getCurrentLocation(), "travel"));
            } else {
                notifyListeners(this.getContextualHistoricalFact());
            }
        }
    }

    /** Rest action for one day. */
    public void rest() {
        if (!isGameRunning || !validateGameComponents()) return;

        notifyListeners("You decide to rest for the day.");
        addTrailUpdate("The party rested for the day.", TrailLogManager.LogCategory.REST);

        int healthRecovered = 5 + (int)(Math.random() * 11);
        // Doctor Bonus
        if (player.getJob() == Job.DOCTOR) {
            notifyListeners(player.getName() + " is a doctor, your recovered 25% more health");
            healthRecovered = (int)(healthRecovered * 1.25); // Heal 25% more
        }
        player.increaseHealth(healthRecovered);
        notifyListeners("Health improved by " + healthRecovered + " points.");

        int oxenHealthRecovered = 5 + (int)(Math.random() * 11);
        inventory.increaseOxenHealth(oxenHealthRecovered);
        notifyListeners("Oxen health improved by " + oxenHealthRecovered + " points.");

        int foodConsumedToday = player.getFamilySize() * 2;
        consumeDailyFood(time.getTotalDays() + 1); // Consume food while resting
        notifyListeners("Food consumed: " + foodConsumedToday + " pounds.");

        int moraleHealthRecovered = 5 + (int)(Math.random() * 11 + 2);
        if(player.getJob() == Job.PREACHER){
            notifyListeners(player.getName() + " is a preacher, your morale recovered by 10%.");
            moraleHealthRecovered += 10;
        }

        // Teacher Bonus
        if (player.getJob() == Job.TEACHER) {
            notifyListeners(player.getName() + " is a teacher, your morale recovered by 5%.");
            moraleHealthRecovered += 5;
        }

        player.increaseMorale(moraleHealthRecovered);

        if (Math.random() < 0.2) { // Chance to find food
            int foodFound = 2 + (int)(Math.random() * 9);
            inventory.addFood(foodFound);
            notifyListeners("While resting, your family found " + foodFound + " pounds of edible plants nearby.");
        }

        if (player.getJob() == Job.CARPENTER) {
            notifyListeners(player.getName() + " is a carpender, you have a 40% chance of repairing an wagon part.");
            if (Math.random() < 0.4) { // 40% chance to repair a broken part
                String repairedPart = inventory.repairRandomBrokenPart();
                if (repairedPart != null) {
                    notifyListeners("Your carpenter skills repaired the " + repairedPart + "!");
                }
            }
        }

        // Rest the oxen
        inventory.restOxen();
        addTrailUpdate("Your oxen have rested and recovered some strength.", TrailLogManager.LogCategory.REST);

        // Random chance to display historical data (40% chance, higher when resting)
        if (Math.random() < 0.4) {
            String historicalNote = historicalDataManager.getContextualHistoricalData("rest", map.getCurrentLocation());
            historicalNote = historicalNote.substring(16);
            addTrailUpdate("CAMPFIRE STORY: " + historicalNote);
        }
        else if(Math.random() > 0.7){
            String restingHistoricalData = historicalDataManager.getContextualHistoricalData("rest", map.getCurrentLocation());
            notifyListeners(restingHistoricalData);
        } else if (Math.random() > 0.5){
            notifyListeners(historicalDataManager.getRandomHistoricalData(map.getCurrentLocation(), "rest"));
        }

        advanceDay(true); // Advance time and check for events/crossings
    }

    /** Hunt action for one day. */
    public void hunt() {
        if (!isGameRunning || !validateGameComponents()) return;

        if (inventory.getAmmunition() <= 0) {
            notifyListeners("You don't have any ammunition for hunting!");
            notifyGameStateChanged(); // Update GUI in case button state needs refresh
            return;
        }

        notifyListeners("You set out to hunt for food...");

        int ammoUsed = 1 + (int)(Math.random() * 3);
        if (inventory.getAmmunition() < ammoUsed) {
            ammoUsed = inventory.getAmmunition();
        }
        inventory.useAmmunition(ammoUsed);

        double baseSuccessChance = 0.6;

        // Apply Hunter bonus to success chance
        if (player.getJob() == Job.HUNTER) {
            notifyListeners(player.getName() + " is a hunter, there is 15% higher chance you get a kill shot.");
            baseSuccessChance += 0.15; // 15% bonus to success
        }

        boolean success = Math.random() < baseSuccessChance;

        if (success) {
            double animalChance = Math.random();
            String animal;
            int foodGained;

            if (animalChance < 0.1) {
                animal = "bison";
                foodGained = 250 + (int)(Math.random() * 251);
            } else if (animalChance < 0.3) {
                animal = "deer";
                foodGained = 80 + (int)(Math.random() * 121);
            } else if (animalChance < 0.6) {
                animal = "rabbit";
                foodGained = 5 + (int)(Math.random() * 11);
            } else {
                animal = "squirrel";
                foodGained = 2 + (int)(Math.random() * 4);
            }

            // Apply Hunter bonus to food gained
            if (player.getJob() == Job.HUNTER) {
                notifyListeners(player.getName() + " is a hunter, you gained 15% more food.");
                foodGained = (int)(foodGained * 1.15); // 15% more food
            }

            notifyListeners("Great shot! You got a " + animal + "!\n" +
                    "Gained " + foodGained + " lbs food. Used " + ammoUsed + " ammo.");
            addTrailUpdate("Hunting successful: Shot a " + animal + " for " + foodGained + " pounds of food.",
                    TrailLogManager.LogCategory.HUNT);
            inventory.addFood(foodGained);

            // Random chance to display hunting-related historical data (35% chance when hunting successfully)
            if (Math.random() < 0.35) {
                String historicalNote = historicalDataManager.getRandomSurvivalTip(map.getCurrentLocation(), "hunt");
                addTrailUpdate(historicalNote, TrailLogManager.LogCategory.SURVIVAL_TIP);
            }
        } else {
            notifyListeners("You missed your shot! The animal got away.\n" +
                    "Used " + ammoUsed + " ammo.");
            addTrailUpdate("Hunting unsuccessful. Used " + ammoUsed + " ammunition.",
                    TrailLogManager.LogCategory.HUNT);

            // Small chance to still get a tip even when unsuccessful (15% chance)
            if (Math.random() < 0.15) {
                String historicalNote = historicalDataManager.getRandomSurvivalTip(map.getCurrentLocation(), "hunt");
                addTrailUpdate(historicalNote, TrailLogManager.LogCategory.SURVIVAL_TIP);
            }
        }
        if(Math.random() > 0.6){
            String huntingHistoricalData = historicalDataManager.getContextualHistoricalData("hunt", map.getCurrentLocation());
            huntingHistoricalData = huntingHistoricalData.substring(16);
            notifyListeners("\nHunting history: \n" + huntingHistoricalData);
        }

        advanceDay(true);
    }

    /**
     * Handles the display of the player's journal when the journal button is clicked.
     * This method compiles information from the historical data tracking system
     * and presents it in a formatted display.
     *
     * @param displayMode The display mode for the journal ("all", "location", "today", "type", "summary")
     * @param factType Optional parameter for when displayMode is "type" (e.g., "Pioneer Fact", "Trail Fact")
     * @return Formatted journal content as a string, ready for display in the UI
     */
    public String displayJournal(String displayMode, String factType) {
        // Allow journal access even after game completion or death
        // Only check if the journal-related components are available
        if (historicalDataManager == null || trailLog == null || historicalDisplayManager == null) {
            return "Journal unavailable: Historical data tracking system not initialized.";
        }

        // Get current location for context - safely handle if map is null
        String currentLocation = (map != null) ? map.getCurrentLocation() : "Unknown";
        
        // Update the historical display manager with current context
        historicalDisplayManager.updateContext(currentLocation);

        // Determine which journal view to display based on mode
        String journalContent;
        switch (displayMode.toLowerCase()) {
            case "all":
                journalContent = historicalDisplayManager.getCompleteJournal();
                break;
            case "location":
                journalContent = historicalDisplayManager.getLocationJournal();
                break;
            case "today":
                journalContent = historicalDisplayManager.getTodaysJournal();
                break;
            case "type":
                // If factType is provided, show that type, otherwise default to pioneer facts
                String type = (factType != null && !factType.isEmpty()) ? factType : "Pioneer Fact";
                journalContent = historicalDisplayManager.getJournalByType(type);
                break;
            case "summary":
                journalContent = historicalDisplayManager.getHistoricalKnowledgeSummary();
                break;
            default:
                // Default to complete journal
                journalContent = historicalDisplayManager.getCompleteJournal();
        }

        // Only log if game is still running (don't modify journal after death/completion)
        if (isGameRunning) {
            // Log this journal viewing in the trail log
            trailLog.addLogEntry("Viewed journal (" + displayMode + ") at " + currentLocation,
                    currentLocation,
                    TrailLogManager.LogCategory.JOURNAL);
        }

        return journalContent;
    }

    /**
     * Helper method to get a random historical fact based on the current context.
     * This can be used for displaying facts during travel, rest, or other activities.
     *
     * @return A formatted historical fact
     */
    public String getContextualHistoricalFact() {
        if (!isGameRunning || !validateGameComponents()) return "";

        // Update the historical display manager with current context
        String currentLocation = map.getCurrentLocation();
        String currentActivity = isTraveling ? "travel" : "rest"; // Default activity based on state
        historicalDisplayManager.updateContext(currentLocation, currentActivity);

        // Get a contextual fact based on current activity
        return historicalDisplayManager.getContextualHistoricalFact();
    }

    /**
     * Method to be called during travel to add historical facts to the journal.
     * This should be called from your travel() method.
     */
    public void addTravelJournalEntry() {
        if (!isGameRunning || !validateGameComponents()) return;

        // Update context
        String currentLocation = map.getCurrentLocation();
        historicalDisplayManager.updateContext(currentLocation, "travel");

        // 15% chance to learn a new fact during travel
        if (Math.random() < 0.15) {

            // Notify the player about the new journal entry
            notifyListeners("You learned something new! Check your journal for details.");
        }
    }

    /**
     * Method to be called when arriving at a landmark to add a location-specific entry.
     * This should be called when the map.travel() method reaches a landmark.
     *
     * @param landmarkName The name of the landmark
     */
    public void addLandmarkJournalEntry(String landmarkName) {
        if (!isGameRunning || !validateGameComponents()) return;

        // Update context to the landmark
        historicalDisplayManager.updateContext(landmarkName, "landmark");

        // Notify the player about the new journal entry
        notifyListeners("New information added to your journal.");
    }
    /**
     * Handles food spoilage events in the game.
     * This method is called when food items spoil based on weather conditions.
     * It updates inventory, creates an event log entry, and notifies the player.
     *
     * @param foodName The name of the food item that spoiled
     */
    public void handleFoodSpoilage(String foodName) {
        // Log the event
        trailLog.addLogEntry(
                "Some of your " + foodName + " has spoiled." + "\n Time: " + time.getDay(),
                map.getCurrentLocation(),
                TrailLogManager.LogCategory.FOOD_SPOILAGE
        );

        // Remove the spoiled food from inventory or reduce quantity
        inventory.consumeFood(1);

        // Notify the player
        notifyListeners(
                "Food Spoilage\n" +
                "Some of your " + foodName + " has spoiled due to weather conditions."
        );

        // Update UI elements if needed
        updateGameState();
    }

    /**
     * Handles wagon part breakage events in the game.
     * This method is called when a wagon part breaks during travel.
     * It updates inventory, creates an event log, notifies the player, and applies gameplay effects.
     *
     * @param partName The name of the wagon part that broke
     */
    public void handlePartBreakage(String partName) {
        // Log the event
        trailLog.addLogEntry(
                "Your wagon's " + partName + " has broken." + "\nTime: " +
                time.getDay(),
                map.getCurrentLocation(),
                TrailLogManager.LogCategory.PART_BREAKAGE
        );

        // Apply gameplay effects
        if (partName.equals("Wheel")) {
            reduceSpeed(0.5); // Reduce travel speed by 50%
        } else if (partName.equals("Axle")) {
            reduceSpeed(0.1); // Cannot travel until fixed
        } else if (partName.equals("Tongue")) {
            reduceSpeed(0.7); // Reduce travel speed by 30%
        } else if (partName.equals("Bow")) {
            reduceSpeed(0.8); // Reduce travel speed by 80%
        }

        // Notify the player
        notifyListeners(
                "Part Breakage: \n" +
                "Your wagon's " + partName + " has broken and needs repair."
        );

        // Update UI
        updateGameState();

        boolean hasSparePart = true;
        // Check if the player has spare parts
        switch(partName){
            case "Wheel": if(inventory.getWheels() == 0){
                hasSparePart = false;
            }
            else {
                hasSparePart = true;
            }
             break;
            case "Bow": if(inventory.getWagonBows() == 0){
                hasSparePart = false;
            }
            else {
                hasSparePart = true;
            }
            break;
            case "Tongue": if(inventory.getTongues() == 0){
                hasSparePart = false;
            }
            else {
                hasSparePart = true;
            }
            break;
            case "Axle": if(inventory.getAxles() == 0){
                hasSparePart = false;
            }
            else {
                hasSparePart = true;
            }
            break;
        }

        // Provide player guidance based on situation
        if (hasSparePart) {
            notifyListeners(
                    "Repair Option: \n" +
                    "You have a spare " + partName + ". Would you like to repair now?"
            );
            showRepairDialog(partName);
        } else {
            notifyListeners(
                    "No Spare Parts: \n" +
                    "You don't have a spare " + partName + ". You should trade for one at the next landmark."
            );
        }
    }

    /**
     * Helper method to show repair dialog
     *
     * @param partName The name of the part that needs repair
     */
    private void showRepairDialog(String partName) {
        // Use HealthDialog's repair functionality instead
        HealthDialog healthDialog = new HealthDialog(findVisibleFrame(), player, inventory);
        healthDialog.setVisible(true);
    }

    public static void reduceSpeed(double multiplier) {
        // Validate input
        if (multiplier < 0.0) {
            multiplier = 0.0; // Minimum allowed is 0 (no movement)
        } else if (multiplier > 1.0) {
            multiplier = 1.0; // Maximum allowed is 1 (full speed)
        }

        // Apply the multiplier
        currentSpeedMultiplier = multiplier;

        // Log the speed change
        String speedPercentage = String.format("%.0f", multiplier * 100);
    }

    public static void restoreSpeed() {
        currentSpeedMultiplier = 1.0;
    }

    // Daily Update and Event Handling

    /**
     * Advances game time by one day and handles end-of-day events.
     * @param checkEvents If true, check for perils and river crossings.
     */
    private void advanceDay(boolean checkEvents) {
        if (!isGameRunning) return;

        time.advanceDay();
        weather = new Weather(time.getMonth(), map.getCurrentLocation());

        boolean landmarkReachedToday = false;
        if (map.hasReachedLandmark()) {
            handleLandmarkArrival();
            landmarkReachedToday = true;
        }

        if (checkEvents && !landmarkReachedToday) { // Only check events if traveling/resting and not at landmark yet
            if (Math.random() < 0.25) { // Peril chance
                perils.generateRandomEvent(); // This notifies listeners directly
            }
            if (map.checkForRiverCrossing()) {
                handleRiverCrossing(); // This shows a dialog
                // Note: River crossing dialog handles its own outcome notification
            }
        }

        // --- Checks after daily actions/events ---
        checkLowResourcePrompts(); // Check low resources AFTER events might have used some
        checkGameEndConditions(); // Check for death/win AFTER events

        notifyGameStateChanged(); // Update GUI with new date, weather, status, etc.
    }

    /** Checks for low food/health and prompts user if needed. */
    private void checkLowResourcePrompts() {
        if (!isGameRunning) return;

        // Low Food Prompt
        if (inventory.getFood() < 100 && inventory.getAmmunition() > 0 && Math.random() < 0.3) {
            notifyListeners("Your food supply is getting low (" + inventory.getFood() + " lbs). " +
                    "Consider hunting soon to replenish your supplies.");
        }

        // Low Health Prompt
        if (player.getHealth() < 30 && inventory.getMedicine() > 0 && Math.random() < 0.4) {
            SwingUtilities.invokeLater(() -> {
                int choice = JOptionPane.showConfirmDialog(findVisibleFrame(),
                        "Health is critical (" + player.getHealth() + "). Use 1 medicine kit?",
                        "Low Health", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (choice == JOptionPane.YES_OPTION) {
                    inventory.useMedicine(1);
                    player.increaseHealth(30);
                    initialMedicineUsed++; // Track usage if needed for summary later too?
                    notifyListeners("Used 1 medicine kit. Health recovered to " + player.getHealth() + ".");
                    notifyGameStateChanged(); // Update GUI immediately
                }
            });
        }
    }

    /** Checks if the player has died or reached the destination. */
    private void checkGameEndConditions() {
        if (!isGameRunning) return;
        if (player.isDead()) {
            isGameRunning = false;
            showDeathDialog();
        } else if (map.hasReachedDestination()) {
            isGameRunning = false;
            showCompletionDialog();
        }
    }

    /** Handles arriving at a landmark (update location, show info/trade). */
    private void handleLandmarkArrival() {
        map.advanceToNextLandmark(); // Advance map state first
        String currentLocation = map.getCurrentLocation(); // Get the CURRENT location after advancing
        Landmark currentLandmark = map.getLandmarks().get(map.getCurrentLandmarkIndex());
        String landmarkName = currentLandmark.getName();

        // Display landmark information
        String message = "\n=== ARRIVED AT " + landmarkName.toUpperCase() + " ===\n" +
                currentLandmark.getDescription();

        try {
            String landmarkHistoricalInformation = historicalDataManager.getLocationSpecificData(landmarkName);
            message += "\n === Historical Information===\n " + landmarkHistoricalInformation + "\n";
        } catch (Exception e) {
            // Just use the basic message without historical info
            message += "\n";
        }

        notifyListeners(message);

        // Add journal entries and trail updates
        if (currentLocation != null && !currentLocation.isEmpty()) {
                addLandmarkJournalEntry(currentLocation);
                addTrailArrivalUpdate(TrailLogManager.LogCategory.LANDMARK);
        }

        // Handle trading posts and forts
        if (landmarkName.contains("Fort") || landmarkName.contains("Trading Post")) {
            SwingUtilities.invokeLater(() -> {
                Frame owner = findVisibleFrame();
                TradingDialog tradingDialog = new TradingDialog(owner, player, inventory);
                tradingDialog.setVisible(true);
                notifyGameStateChanged();
            });
        }
    }

    /** Initiates the river crossing process by showing the dialog. */
    private void handleRiverCrossing() {
        // Get the name and description of the current river crossing
        String riverName = map.getCurrentRiverCrossingName();
        String riverDescription = map.getCurrentRiverCrossingDescription();

        String riverCrossingHistoricalInformation = historicalDataManager.getLocationSpecificData("river crossing");

        notifyListeners("\n=== " + riverName.toUpperCase() + " ===\n" +
                riverDescription + "\n\n" +
                "You must cross the river.");
        notifyListeners("Historical Information: \n" + riverCrossingHistoricalInformation);

        SwingUtilities.invokeLater(() -> { // Show dialog on EDT
            Frame owner = findVisibleFrame();
            RiverCrossingDialog riverDialog = new RiverCrossingDialog(owner, player, inventory, weather, this::notifyListeners);
            // Pass the river name to the dialog
            riverDialog.setRiverName(riverName);
            riverDialog.setVisible(true); // Modal dialog handles its own logic & notification

            // State is updated by the dialog. Notify main GUI AFTER it closes.
            notifyGameStateChanged();
            checkGameEndConditions(); // Check for death immediately after crossing attempt
        });
        map.resetRiverCrossing(); // Reset flag after showing dialog
    }

    /** Shows the completion dialog. */
    private void showCompletionDialog() {
        final String completionMessage = "\nCONGRATULATIONS!\n" +
                "You reached " + map.getDestination() + "!\n" +
                "Journey took " + time.getTotalDays() + " days. Traveled " + map.getDistanceTraveled() + " miles.\n" +
                "Arrival: " + time.getMonthName() + " " + time.getDay() + ", " + time.getYear();
        notifyListeners(completionMessage);
        SwingUtilities.invokeLater(() -> {
            Frame owner = findVisibleFrame();
            CompletionDialog completionDialog = new CompletionDialog(owner, map.getDestination(), time.getTotalDays(), map.getDistanceTraveled(), time.getMonthName() + " " + time.getDay() + ", " + time.getYear());
            completionDialog.setVisible(true);
        });
    }

    /**
     * Shows a dialog when the player dies.
     */
    private void showDeathDialog() {
        if (player == null || time == null || map == null) {
            System.err.println("Cannot show death dialog: Game components not initialized.");
            return;
        }
        
        Frame parentFrame = findVisibleFrame();
        if (parentFrame == null) {
            System.err.println("Cannot show death dialog: No visible parent frame found.");
            return;
        }

        String causeOfDeath = player.getCauseOfDeath();
        if (causeOfDeath == null || causeOfDeath.trim().isEmpty()) {
            causeOfDeath = "poor health";
        }
        
        int days = time.getTotalDays();
        int distance = map.getDistanceTraveled();
        String location = map.getCurrentLocation();
        
        DeathDialog deathDialog = new DeathDialog(parentFrame, causeOfDeath, days, distance, location);
        deathDialog.setVisible(true);
    }

    /**
     * Finds a visible frame to use for dialogs.
     * 
     * @return A visible Frame object, or null if none found
     */
    private Frame findVisibleFrame() {
        for (Frame frame : Frame.getFrames()) {
            if (frame.isVisible() && frame.isActive()) {
                return frame;
            }
        }
        for (Frame frame : Frame.getFrames()) {
            if (frame.isVisible()) {
                return frame;
            }
        }
        return null;
    }

    /**
     * Gets the current player object.
     * 
     * @return The Player object
     */
    public Player getPlayer() { return player; }

    /**
     * Gets the current map object.
     * 
     * @return The Map object
     */
    public Map getMap() { return map; }

    /**
     * Gets the current inventory object.
     * 
     * @return The Inventory object
     */
    public Inventory getInventory() { return inventory; }

    /**
     * Gets the current time object.
     * 
     * @return The Time object
     */
    public Time getTime() { return time; }

    /**
     * Gets the current weather object.
     * 
     * @return The Weather object
     */
    public Weather getWeather() { return weather; }

    /**
     * Checks if the game has been started.
     * 
     * @return true if the game has been started, false otherwise
     */
    public boolean isGameStarted() { return gameStarted; }

    /**
     * Checks if the game is currently running.
     * 
     * @return true if the game is running, false otherwise
     */
    public boolean isGameRunning() {
        return isGameRunning;
    }

    /**
     * Gets the name of the selected trail.
     * 
     * @return The trail name
     */
    public String getTrail() { return trail; }

    /**
     * Updates the game state and notifies listeners.
     */
    public void updateGameState() {
        // Update oxen health based on travel and conditions
        if (isTraveling) {
            // Decrease health based on travel speed and terrain
            int healthDecrease = 0;
            switch (currentSpeed) {
                case "Slow": healthDecrease = 1; break;
                case "Normal": healthDecrease = 2; break;
                case "Fast": healthDecrease = 3; break;
            }
            
            // Additional decrease based on terrain
            if (map != null) {
                String terrain = map.getCurrentLocation();
                if (terrain.contains("Mountain")) {
                    healthDecrease += 2;
                } else if (terrain.contains("River")) {
                    healthDecrease += 1;
                }
            }
            
            // Apply health decrease
            int currentHealth = inventory.getOxenHealth();
            inventory.setOxenHealth(Math.max(0, currentHealth - healthDecrease));
        }

        notifyGameStateChanged();
    }

    /**
     * Gets the bonus effect for a specific job.
     * 
     * @param jobEffect The type of bonus to check
     * @return The bonus value as a percentage
     */
    public double getJobBonus(String jobEffect) {
        Job playerJob = player.getJob();
        if (playerJob == null) return 0.0;
        
        switch (jobEffect) {
            case "food_spoilage":
                return (playerJob == Job.FARMER) ? -0.225 : 0.0;
                
            case "part_breakage":
                return (playerJob == Job.BLACKSMITH) ? -0.20 : 0.0;
                
            case "repair_cost":
                return (playerJob == Job.CARPENTER) ? -0.25 : 0.0;
                
            case "repair_speed":
                return (playerJob == Job.CARPENTER) ? 0.30 : 0.0;
                
            case "repair_success":
                return (playerJob == Job.CARPENTER) ? 0.10 : 0.0;
                
            case "hunting_ammo_threshold":
                return (playerJob == Job.HUNTER) ? 0.25 : 0.0;
                
            case "travel_speed":
                return (playerJob == Job.HUNTER) ? 0.05 : 0.0;
                
            case "health_depletion":
                return (playerJob == Job.DOCTOR) ? -0.10 : 0.0;
                
            case "morale_decrease":
                return (playerJob == Job.TEACHER) ? -0.15 : 0.0;
                
            case "morale_healing":
                return (playerJob == Job.PREACHER) ? 0.25 : 0.0;
                
            case "health_from_morale":
                if (playerJob == Job.PREACHER && player.getMorale() > 75) {
                    return 0.15;
                }
                return 0.0;
                
            case "merchant_discount":
                return (playerJob == Job.MERCHANT) ? -0.075 : 0.0;
                
            default:
                return 0.0;
        }
    }

    /**
     * Adds a trail update message to the game log.
     * 
     * @param update The update message to add
     */
    public void addTrailUpdate(String update) {
        notifyListeners(update);
    }

    private void addTrailUpdate(String message, TrailLogManager.LogCategory category) {
        if (message == null || message.trim().isEmpty()) {
            return; // Skip empty messages
        }
        
        // Get current location
        String location = map != null ? map.getCurrentLocation() : "Unknown Location";
        
        // Add the log entry with location
        trailLog.addLogEntry(message, location, category);
        notifyListeners(message);
    }

    private void addTrailArrivalUpdate(TrailLogManager.LogCategory category) {
        // Get current location
        String location = map != null ? map.getCurrentLocation() : "Unknown Location";

        // Add the log entry with location
        trailLog.addLogEntry("", location, category);
    }

    /**
     * Gets the current trail log manager.
     * 
     * @return The TrailLogManager instance
     */
    public TrailLogManager getTrailLog() {
        return trailLog;
    }
}