import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.awt.Frame;

public class GameController {
    private Player player;
    private Map map;
    private final Inventory inventory;
    private Time time;
    private Weather weather;
    private Hunting hunting;
    private Perils perils;
    private Job job;

    // Trail setup
    private String trail;

    // Game state
    private boolean gameStarted = false;
    private boolean isGameRunning = true;

    // Event tracking for the initial journey
    private final ArrayList<String> initialJourneyEvents = new ArrayList<>();
    private final ArrayList<Landmark> initialLandmarksPassed = new ArrayList<>();
    // Fields to track consumption during initial journey
    private int initialFoodConsumed = 0;
    private int initialPartsUsed = 0;
    private int initialMedicineUsed = 0;
    private int initialAmmoUsed = 0;

    //Food types, and spoil rates
    private static final String[] FOOD_TYPES = {
            "Flour", "Bacon", "Dried Beans", "Rice", "Coffee", "Sugar", "Dried Fruit", "Hardtack"
    };
    private static final double[] FOOD_SPOILRATE = {
            .05, .15, .02, .03, .005, .04, .1, .01
    };

    //Wagon Parts
    private static final String[] WAGON_PARTS = {
            "Wheel", "Bow", "Tongue", "Axle"
    };

    // Listeners
    private final ArrayList<Consumer<String>> messageListeners = new ArrayList<>();
    private final ArrayList<Runnable> gameStateListeners = new ArrayList<>();

    /**
     * Constructor: Initializes default game state components.
     * Actual game setup (player, trail, month) happens via GUI dialogs.
     */
    public GameController() {
        // Initialize with defaults, will be overwritten by dialogs
        player = new Player("Player", "Male", job); // Default player
        inventory = new Inventory();
        time = new Time(1848, 3); // Default to March 1848
        map = new Map(1); // Default to Oregon Trail
        weather = new Weather(time.getMonth(), map.getStartingLocation());
        hunting = new Hunting(inventory); // Needs inventory
        perils = new Perils(player, inventory, weather, time); // Needs player, inventory, weather, time

        // Set the message listener for Perils class right away
        perils.setMessageListener(this::notifyListeners);
    }

    /**
     * Starts a new game - called after initial dialogs are complete.
     * Initializes game state based on user choices.
     */
    public void startNewGame() {
        // This method assumes player, map, time, inventory, etc. have been
        // properly initialized based on user selections from dialogs.
        gameStarted = true;
        isGameRunning = true;

        // Re-initialize components that depend on player choices if needed
        // (e.g., Perils might depend on player gender or family size indirectly)
        if (player != null && inventory != null && weather != null) {
            perils = new Perils(player, inventory, weather, time);
            perils.setMessageListener(this::notifyListeners);
        } else {
            System.err.println("Error in startNewGame: Player, Inventory, or Weather is null.");
            // Handle error - maybe show message and exit?
            notifyListeners("ERROR: Failed to initialize game components fully.");
            isGameRunning = false;
        }
        if (inventory != null) {
            hunting = new Hunting(inventory);
        } else {
            System.err.println("Error in startNewGame: Inventory is null.");
            notifyListeners("ERROR: Failed to initialize inventory.");
            isGameRunning = false;
        }

        // No introductory messages sent from here anymore.
    }

    // --- Listener Management ---

    /** Adds a message listener (typically the GUI's output area). */
    public void addMessageListener(Consumer<String> listener) {
        messageListeners.add(listener);
    }

    /** Adds a listener for game state changes (typically the GUI's update method). */
    public void addGameStateListener(Runnable listener) {
        gameStateListeners.add(listener);
    }

    /** Sends a message to all registered message listeners (ensures runs on EDT). */
    private void notifyListeners(String message) {
        SwingUtilities.invokeLater(() -> {
            for (Consumer<String> listener : messageListeners) {
                listener.accept(message);
            }
        });
    }

    // --- Game Setup Methods (Called by Dialogs) ---

    /** Sets up player with name, gender and family members. */
    public void playerSetup(String name, String gender, String[] familyMembers, Job job) {
        player = new Player(name, gender, job);
        player.setFamilyMembers(familyMembers);
        // Re-initialize Perils if it depends on player details
        if (inventory != null && weather != null) {
            perils = new Perils(player, inventory, weather, time);
            perils.setMessageListener(this::notifyListeners);
        }
    }

    /** Sets the trail choice and initializes the map. */
    public void selectTrail(int trailChoice) {
        switch (trailChoice) {
            case 1:
                trail = "Oregon";
                String departureLocation = "Independence, Missouri";
                break;
            case 2:
                trail = "California";
                departureLocation = "Independence, Missouri";
                break;
            case 3:
                trail = "Mormon";
                departureLocation = "Nauvoo, Illinois";
                break;
            default: // Default to Oregon if invalid
                trail = "Oregon";
                departureLocation = "Independence, Missouri";
                trailChoice = 1;
                System.err.println("Warning: Invalid trail choice received. Defaulting to Oregon Trail.");
                break;
        }
        map = new Map(trailChoice); // Initialize map based on choice
        // Weather might depend on starting location, re-init if time already set
        if (time != null) {
            weather = new Weather(time.getMonth(), map.getStartingLocation());
            if (player != null && inventory != null) {
                perils = new Perils(player, inventory, weather, time);
                perils.setMessageListener(this::notifyListeners);
            }
        }
    }

    /** Sets the departure month and initializes time/weather/perils. */
    public void selectDepartureMonth(int month) {
        String[] months = {"March", "April", "May", "June", "July"};
        if (month >= 1 && month <= 5) {
            // 0=March, 1=April, etc.
            int monthChoice = month - 1;
            String departureMonth = months[monthChoice];
            int monthNumber = monthChoice + 3; // March=3, April=4, etc.
            time = new Time(1848, monthNumber);

            // Ensure map is initialized before getting starting location
            String startLoc = (map != null) ? map.getStartingLocation() : "Start";
            weather = new Weather(monthNumber, startLoc);

            // Ensure player and inventory are initialized before Perils
            if (player != null && inventory != null) {
                perils = new Perils(player, inventory, weather, time);
                perils.setMessageListener(this::notifyListeners);
            } else {
                System.err.println("Error initializing Perils in selectDepartureMonth: Player or Inventory is null.");
            }
        } else {
            System.err.println("Invalid departure month selected: " + month + ". Defaulting to March.");
            selectDepartureMonth(1); // Default to March if invalid
        }
    }

    /** Called after the Market dialog closes (mostly for state update). */
    public void visitMarket() {
        // Logic moved to Market.java GUI panel actions.
        // This method remains as a placeholder in the sequence if needed.
        notifyGameStateChanged(); // Update state in case money changed
    }

    // --- Initial Journey Simulation ---

    /** Simulates the journey to Fort Kearny and shows the summary dialog. */
    public void journeyToFortKearny() {
        if (!validateGameComponents()) return; // Check if game is ready

        initialJourneyEvents.clear();
        initialLandmarksPassed.clear();
        resetInitialConsumption(); // Reset counters

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

        // --- Simulation Loop ---
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
            simulateInitialJourneyEvent(daysToFortKearny); // Simulate random events

            if (player.isDead()) {
                handleInitialJourneyDeath(daysToFortKearny);
                return; // Stop simulation
            }
        }
        // --- End Simulation Loop ---

        if (!player.isDead()) {
            map.travel(distanceCovered); // Update map's total distance
            map.setCurrentLocation(fortKearnyLandmark.getName()); // Set location explicitly
            notifyListeners("\n=== JOURNEY TO FORT KEARNY COMPLETE ===\n" +
                    "Arrived after " + daysToFortKearny + " days.");
        }

        showTravelSummaryDialog(daysToFortKearny, distanceCovered);
    }

    /** Helper to validate essential game components before simulation/actions. */
    private boolean validateGameComponents() {
        if (map == null || time == null || player == null || inventory == null || perils == null || hunting == null) {
            String missing = "";
            if (map == null) missing += "Map ";
            if (time == null) missing += "Time ";
            if (player == null) missing += "Player ";
            if (inventory == null) missing += "Inventory ";
            if (perils == null) missing += "Perils ";
            if (hunting == null) missing += "Hunting ";
            notifyListeners("ERROR: Game not fully initialized. Missing: " + missing.trim());
            System.err.println("ERROR: Game not fully initialized. Missing: " + missing.trim());
            isGameRunning = false; // Stop the game if critical components missing
            return false;
        }
        return true;
    }

    /** Resets counters for initial journey consumption. */
    private void resetInitialConsumption() {
        initialFoodConsumed = 0;
        initialPartsUsed = 0;
        initialMedicineUsed = 0;
        initialAmmoUsed = 0;
    }

    /** Finds the distance to Fort Kearny. */
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

    /** Finds the Fort Kearny Landmark object. */
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

    /** Collects landmarks passed before Fort Kearny. */
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

    /** Calculates daily travel distance based on factors. */
    private int calculateDailyDistance(int base) {
        int dailyDistance = base;
        dailyDistance = weather.adjustTravelDistance(dailyDistance);
        dailyDistance = (int)(dailyDistance * Math.max(0.1, inventory.getOxenHealth() / 100.0)); // Min 10% speed even if health low
        return Math.max(0, dailyDistance); // Can be 0 if oxen health is 0
    }

    /** Handles daily food consumption and tracks total. */
    private void consumeDailyFood(int currentDay) {
        int foodNeeded = player.getFamilySize() * 2; // Base 2 lbs per person per day
        int foodAvailable = inventory.getFood();

        if (foodAvailable >= foodNeeded) {
            inventory.consumeFood(foodNeeded);
            initialFoodConsumed += foodNeeded;
        } else {
            // Consume remaining food
            inventory.consumeFood(foodAvailable);
            initialFoodConsumed += foodAvailable;
            
            // Apply health penalty for insufficient food
            int healthPenalty = 5 + (foodNeeded - foodAvailable); // Penalty increases with shortfall
            // Pass "starvation" as the cause when health decreases due to lack of food
            player.decreaseHealth(healthPenalty, "starvation"); 
            initialJourneyEvents.add("Day " + currentDay + ": Ran low on food! Lost " + healthPenalty + " health.");
            
            // Check if player died from starvation during initial journey
            if (player.isDead()) {
                initialJourneyEvents.add("Tragically, you starved before reaching Fort Kearny.");
                handleInitialJourneyDeath(currentDay);
                return; // Stop simulation if player dies
            }
        }
    }

    /** Simulates random oxen fatigue. */
    private void simulateDailyOxenFatigue() {
        if (Math.random() < 0.10) {
            inventory.decreaseOxenHealth(2);
        }
    }

    /** Simulates a random event during the initial journey and tracks resource usage. */
    private void simulateInitialJourneyEvent(int currentDay) {
        if (Math.random() < 0.25) { // Event chance
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

            perils.generateRandomEvent(); // Event happens here

            // Track resources consumed *by the event*
            initialPartsUsed += Math.max(0, partsBefore - inventory.getWagonParts());
            initialMedicineUsed += Math.max(0, medicineBefore - inventory.getMedicine());
            initialAmmoUsed += Math.max(0, ammoBefore - inventory.getAmmunition());

            perils.setMessageListener(originalListener); // Restore

            if (!eventDetails.isEmpty()) {
                initialJourneyEvents.add("Day " + currentDay + ": " + String.join(" ", eventDetails));
            }
        }
    }

    /** Handles player death during the initial journey simulation. */
    private void handleInitialJourneyDeath(int days) {
        isGameRunning = false;
        
        // Get cause of death or use default if somehow not set
        String causeOfDeath = player.getCauseOfDeath();
        if (causeOfDeath == null || causeOfDeath.trim().isEmpty()) {
            causeOfDeath = "poor health";
            player.setCauseOfDeath(causeOfDeath); // Set it for the DeathDialog
        }
        
        String deathMessage = "Died of " + causeOfDeath + " after " + days + " days, before reaching Fort Kearny.";
        initialJourneyEvents.add(deathMessage);
        notifyListeners("\n" + deathMessage);
        showDeathDialog(); // Show death dialog immediately
    }

    /** Shows the travel summary dialog after the initial journey. */
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
            notifyGameStateChanged(); // Update main GUI after summary closes
        });
    }


    // --- Main Game Loop Actions (Called by GUI Buttons) ---

    /** Travel action for one day. */
    public void travel() {
        if (!isGameRunning || !validateGameComponents()) return;

        int baseDistance = 15;
        int adjustedDistance = calculateDailyDistance(baseDistance);
        double speedBonus = getJobBonus("travel_speed");

        StringBuilder result = new StringBuilder();

        if (adjustedDistance <= 0) {
            notifyListeners("Cannot travel today (check oxen health).");
            consumeDailyFood(time.getTotalDays() + 1);
            advanceDay(false);
            return;
        }

        // Check for broken parts
        if (inventory.hasBrokenParts()) {
            // Reduce travel speed by 50% if any parts are broken
            adjustedDistance = (int)(adjustedDistance * 0.5);
            result.append("Travel is slowed due to broken wagon parts.\n");
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

        // 🛠 TRAVEL happens first
        map.travel(adjustedDistance);

        String breakageResult = inventory.checkForPartBreakage(this);
        if (breakageResult != null) {
            result.append(breakageResult).append("\n");
        }
        
        // Apply food spoilage
        inventory.applyFoodSpoilage(weather, this);

        // 🛠 Fatigue, Health decrease from normal trail wear
        player.decreaseHealth(5); // Base fatigue damage per day of travel

        // 🛠 Health-based Risk Warning
        if (player.getHealth() <= 30) {
            notifyListeners("Your party is severely weakened. It is recommended to rest soon or risk breakdowns and deaths.");
        }

        // 🛠 Blacksmith Wagon Part Check
        if (player.getJob() == Job.BLACKSMITH) {
            for (int i = 0; i < WAGON_PARTS.length; i++) {
                int partHealth = inventory.getWagonPartBreakpercentage(WAGON_PARTS[i]);
                if (partHealth < 50) {
                    notifyListeners("Your " + WAGON_PARTS[i] + " is in poor condition. Resting could allow repairs.");
                }
            }
        }

        // 🛠 Random Injury Chance
        if (Math.random() < 0.1) {
            player.decreaseHealth(2);
            notifyListeners("The rough trail caused some minor injuries and fatigue.");
        }

        // 🛠 Daily food consumption
        int foodConsumedToday = player.getFamilySize() * 2;
        consumeDailyFood(time.getTotalDays() + 1);

        // 🛠 Spoilage check on random food item
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

        // 🛠 Small random injury chance
        if (Math.random() < 0.1) {
            player.decreaseHealth(2);
            notifyListeners("The rough trail caused some minor injuries and fatigue.");
        }

        notifyListeners("You traveled " + adjustedDistance + " miles today.\n" +
                "Food consumed: " + foodConsumedToday + " pounds.");

        // 🛠 Advance Day
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
    }

    /** Rest action for one day. */
    public void rest() {
        if (!isGameRunning || !validateGameComponents()) return;

        notifyListeners("You decide to rest for the day.");

        int healthRecovered = 5 + (int)(Math.random() * 11);
        // Doctor Bonus
        if (player.getJob() == Job.DOCTOR) {
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
            moraleHealthRecovered += 10;
        }

        // Teacher Bonus
        if (player.getJob() == Job.TEACHER) {
            moraleHealthRecovered += 5;
        }

        player.increaseMorale(moraleHealthRecovered);

        if (Math.random() < 0.2) { // Chance to find food
            int foodFound = 2 + (int)(Math.random() * 9);
            inventory.addFood(foodFound);
            notifyListeners("While resting, your family found " + foodFound + " pounds of edible plants nearby.");
        }

        if (player.getJob() == Job.CARPENTER) {
            if (Math.random() < 0.4) { // 40% chance to repair a broken part
                String repairedPart = inventory.repairRandomBrokenPart();
                if (repairedPart != null) {
                    notifyListeners("Your carpenter skills repaired the " + repairedPart + "!");
                }
            }
        }

        // Rest the oxen
        inventory.restOxen();
        addTrailUpdate("Your oxen have rested and recovered some strength.");

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

        // 🛠 Apply Hunter bonus to success chance
        if (player.getJob() == Job.HUNTER) {
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

            // 🛠 Apply Hunter bonus to food gained
            if (player.getJob() == Job.HUNTER) {
                foodGained = (int)(foodGained * 1.25); // 25% more food
            }

            notifyListeners("Great shot! You got a " + animal + "!\n" +
                    "Gained " + foodGained + " lbs food. Used " + ammoUsed + " ammo.");
            inventory.addFood(foodGained);
        } else {
            notifyListeners("You missed your shot! The animal got away.\n" +
                    "Used " + ammoUsed + " ammo.");
        }

        advanceDay(true);
    }

    // --- Daily Update and Event Handling ---

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
        map.advanceToNextLandmark(); // Advance map state
        Landmark currentLandmark = map.getLandmarks().get(map.getCurrentLandmarkIndex());
        String landmarkName = currentLandmark.getName();

        notifyListeners("\n=== ARRIVED AT " + landmarkName.toUpperCase() + " ===\n" +
                currentLandmark.getDescription());

        if (landmarkName.contains("Fort") || landmarkName.contains("Trading Post")) {
            SwingUtilities.invokeLater(() -> { // Show dialog on EDT
                Frame owner = findVisibleFrame();
                TradingDialog tradingDialog = new TradingDialog(owner, player, inventory);
                tradingDialog.setVisible(true);
                notifyGameStateChanged(); // Update after trade dialog closes
            });
        }
    }

    /** Initiates the river crossing process by showing the dialog. */
    private void handleRiverCrossing() {
        // Get the name and description of the current river crossing
        String riverName = map.getCurrentRiverCrossingName();
        String riverDescription = map.getCurrentRiverCrossingDescription();
        
        notifyListeners("\n=== " + riverName.toUpperCase() + " ===\n" +
                riverDescription + "\n\n" +
                "You must cross the river.");

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

    /** Shows the death dialog. */
    private void showDeathDialog() {
        if (player == null || time == null || map == null) {
            System.err.println("Cannot show death dialog: Game components not initialized.");
            return;
        }
        
        Frame parentFrame = findVisibleFrame();
        if (parentFrame == null) {
            System.err.println("Cannot show death dialog: No visible parent frame found.");
            // Optionally, create a temporary frame or show a console message
            return;
        }

        // Get the specific cause of death from the player object
        String causeOfDeath = player.getCauseOfDeath();
        // If the cause is still null or empty, default to "unknown causes" as a final fallback
        if (causeOfDeath == null || causeOfDeath.trim().isEmpty()) {
            causeOfDeath = "poor health";
        }
        
        int days = time.getTotalDays();
        int distance = map.getDistanceTraveled();
        String location = map.getCurrentLocation();
        
        // Use the retrieved causeOfDeath when creating the dialog
        DeathDialog deathDialog = new DeathDialog(parentFrame, causeOfDeath, days, distance, location);
        deathDialog.setVisible(true); // Show the dialog
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

    /** Helper to find the main visible frame for dialog ownership. */
    private Frame findVisibleFrame() {
        for (Frame frame : Frame.getFrames()) {
            if (frame.isVisible() && frame.isActive()) { // Prefer active frame
                return frame;
            }
        }
        // Fallback if no active frame found
        for (Frame frame : Frame.getFrames()) {
            if (frame.isVisible()) {
                return frame;
            }
        }
        return null;
    }
    // --- Getters for Game State (Used by GUI) ---

    public Player getPlayer() { return player; }
    public Map getMap() { return map; }
    public Inventory getInventory() { return inventory; }
    public Time getTime() { return time; }
    public Weather getWeather() { return weather; }
    public boolean isGameStarted() { return gameStarted; }
    // Add this method to the GameController class if it doesn't exist
    public boolean isGameRunning() {
        return isGameRunning;
    }
    
    // Make sure this method exists and is properly implemented
    public void notifyGameStateChanged() {
        // Notify all GUI components that the game state has changed
        SwingUtilities.invokeLater(() -> {
            for (Runnable listener : gameStateListeners) {
                listener.run();
            }
        });
    }

    // Add this inner class if it doesn't exist
    public class GameState {
        public final Player player;
        public final Map map;
        public final Inventory inventory;
        public final Time time;
        public final Weather weather;
        public final boolean isRunning;
        
        public GameState(Player player, Map map, Inventory inventory, Time time, Weather weather, boolean isRunning) {
            this.player = player;
            this.map = map;
            this.inventory = inventory;
            this.time = time;
            this.weather = weather;
            this.isRunning = isRunning;
        }
    }
    public String getTrail() { return trail; }

    /** Trigger a game state update manually if needed (e.g., after dialog). */
    public void updateGameState() {
        notifyGameStateChanged();
    }


    /**
     * Applies job-specific bonuses based on player's occupation
     * @param jobEffect The type of effect to check for
     * @return The bonus value (can be positive or negative)
     */
    public double getJobBonus(String jobEffect) {
        Job playerJob = player.getJob();
        if (playerJob == null) return 0.0;
        
        switch (jobEffect) {
            case "food_spoilage":
                // Farmer: Food spoils 20-25% slower
                return (playerJob == Job.FARMER) ? -0.225 : 0.0;
                
            case "part_breakage":
                // Blacksmith: Wagon parts break 20% less often
                return (playerJob == Job.BLACKSMITH) ? -0.20 : 0.0;
                
            case "repair_cost":
                // Carpenter: Cheaper wagon repairs
                return (playerJob == Job.CARPENTER) ? -0.25 : 0.0;
                
            case "repair_speed":
                // Carpenter: Faster wagon repairs
                return (playerJob == Job.CARPENTER) ? 0.30 : 0.0;
                
            case "repair_success":
                // Carpenter: 10% more likely to successfully improvise repairs
                return (playerJob == Job.CARPENTER) ? 0.10 : 0.0;
                
            case "hunting_ammo_threshold":
                // Hunter: Can hunt with less ammo
                return (playerJob == Job.HUNTER) ? 0.25 : 0.0;
                
            case "travel_speed":
                // Hunter: +5% travel speed because better scouting
                return (playerJob == Job.HUNTER) ? 0.05 : 0.0;
                
            case "health_depletion":
                // Doctor: Party health depletes slower by 10%
                return (playerJob == Job.DOCTOR) ? -0.10 : 0.0;
                
            case "morale_decrease":
                // Teacher: Morale decreases slower over time
                return (playerJob == Job.TEACHER) ? -0.15 : 0.0;
                
            case "morale_healing":
                // Preacher: Morale heals faster when resting
                return (playerJob == Job.PREACHER) ? 0.25 : 0.0;
                
            case "health_from_morale":
                // Preacher: Slight boost to healing if morale is high
                if (playerJob == Job.PREACHER && player.getMorale() > 75) {
                    return 0.15;
                }
                return 0.0;
                
            case "merchant_discount":
                // Merchant: Prices in stores are automatically cheaper (5-10% discount)
                return (playerJob == Job.MERCHANT) ? -0.075 : 0.0;
                
            default:
                return 0.0;
        }
    }

    // Add this method to the GameController class if it doesn't exist
    public void addTrailUpdate(String update) {
        // Implementation of addTrailUpdate method
        // This method should add the given update to the trail description
        // and notify listeners of the update
        // Implementation details depend on the specific implementation of the trail
        // and how updates are tracked and notified to listeners.
    }
}
