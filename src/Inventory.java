/**
 * Inventory Class of the Perils Along the Platte Game
 * Manages all player resources including food, oxen, wagon parts, medicine, and ammunition.
 * Handles resource tracking, consumption, and maintenance of wagon parts and oxen health.
 *
 * @author Alex Randall and Chase McCluskey
 * @version 1.0
 * @date 05/06/2025
 * @file Inventory.java
 */

import java.util.ArrayList;

public class Inventory {
    // Resource quantities
    private int food;
    private int oxen;
    private int wheels;
    private int axles;
    private int tongues;
    private int wagonBows;
    private int medicine;
    private int ammunition;

    // Oxen condition
    private int oxenHealth = 100;
    private int oxenFatigue;

    //List of items
    private final ArrayList<Item> items;
    
    // Weight and capacity constants
    private static final int MAX_WEIGHT_CAPACITY = 1500;
    private static final int MAX_OXEN_FATIGUE = 100;
    private static final int BASE_FATIGUE_RATE = 2;
    private static final int WEIGHT_FATIGUE_FACTOR = 1;
    
    // Item weight constants
    private static final int MEDICINE_WEIGHT = 5;
    private static final int AMMO_BOX_WEIGHT = 3;
    
    // Wagon part weight constants
    private static final int WHEEL_WEIGHT = 50;
    private static final int AXLE_WEIGHT = 40;
    private static final int TONGUE_WEIGHT = 30;
    private static final int WAGON_BOW_WEIGHT = 10;

    // Wagon part tracking arrays
    private final int[] WAGON_PARTS_BREAKPERCENTAGE = new int[]{100, 100, 100, 100};
    private final String[] WAGON_PARTS = new String[]{"Wheel", "Axle", "Tongue", "Bow"};
    private final boolean[] WAGON_PARTS_BROKEN = new boolean[]{false, false, false, false};
    private final int[] WAGON_PARTS_WEAR_RATES = new int[]{2, 1, 1, 1};  // Wheels wear out faster
    private final String[] WAGON_PARTS_HISTORICAL_NOTES = new String[]{
            "The rough terrain and constant movement took its toll on the wagon wheels, a common issue on the Oregon Trail.",
            "The wooden axles, bearing the weight of the wagon and its contents, finally gave way under the strain.",
            "The wagon tongue, essential for steering and connecting the oxen, succumbed to the rigors of the journey.",
            "The wagon bows, supporting the canvas cover, weakened from exposure to the elements and constant movement."
    };

    /**
     * Constructs a new Inventory with all resources initialized to zero.
     * Sets up the initial state for a new journey, including:
     * - Zero quantities for all resources
     * - Full health for oxen (100%)
     * - No fatigue (0%)
     * - Empty items collection
     * - Perfect condition for all wagon parts (100%)
     * 
     * This represents the starting state before any supplies are purchased.
     */
    public Inventory() {
        this.food = 0;
        this.oxen = 0;
        this.wheels = 0;
        this.axles = 0;
        this.tongues = 0;
        this.wagonBows = 0;
        this.medicine = 0;
        this.ammunition = 0;
        this.oxenFatigue = 0;
        this.items = new ArrayList<>();
    }

    /**
     * Gets the current amount of food in pounds.
     * This value represents the total edible food available,
     * before any spoilage calculations.
     * 
     * @return The amount of food in pounds
     */
    public int getFood() {
        return food;
    }

    /**
     * Adds food to the inventory.
     * Increases the total food supply by the specified amount.
     * No weight capacity check is performed here - that should be
     * done before calling this method.
     * 
     * @param amount The amount of food to add in pounds
     */
    public void addFood(int amount) {
        this.food += amount;
    }

    /**
     * Consumes food from the inventory.
     * Reduces the food supply by the specified amount, ensuring it never goes below zero.
     * This method is called during daily food consumption and special events.
     * 
     * @param amount The amount of food to consume in pounds
     */
    public void consumeFood(int amount) {
        this.food = Math.max(0, this.food - amount);
    }

    /**
     * Gets the current number of oxen in the team.
     * This value represents the total number of oxen available
     * for pulling the wagon, regardless of their condition.
     * 
     * @return The number of oxen
     */
    public int getOxen() {
        return oxen;
    }

    /**
     * Adds oxen to the inventory.
     * Increases the oxen team size by the specified amount.
     * New oxen start with full health and no fatigue.
     * 
     * @param amount The number of oxen to add
     */
    public void addOxen(int amount) {
        this.oxen += amount;
    }
    
    /**
     * Gets the current number of spare wagon wheels.
     * This value represents the number of replacement wheels
     * available for when the current wheels break or wear out.
     * 
     * @return The number of wheels
     */
    public int getWheels() {
        return wheels;
    }
    
    /**
     * Adds wagon wheels to the inventory.
     * Increases the number of spare wheels by the specified amount.
     * Each wheel adds 50 pounds to the wagon's total weight.
     * 
     * @param amount The number of wheels to add
     */
    public void addWheels(int amount) {
        this.wheels += amount;
    }
    
    /**
     * Uses wagon wheels from the inventory.
     * Reduces the number of spare wheels by the specified amount,
     * ensuring it never goes below zero. This is called when
     * replacing broken or worn-out wheels.
     * 
     * @param amount The number of wheels to use
     */
    public void useWheels(int amount) {
        this.wheels = Math.max(0, this.wheels - amount);
    }
    
    /**
     * Gets the current number of spare wagon axles.
     * This value represents the number of replacement axles
     * available for when the current axles break or wear out.
     * 
     * @return The number of axles
     */
    public int getAxles() {
        return axles;
    }
    
    /**
     * Adds wagon axles to the inventory.
     * Increases the number of spare axles by the specified amount.
     * Each axle adds 40 pounds to the wagon's total weight.
     * 
     * @param amount The number of axles to add
     */
    public void addAxles(int amount) {
        this.axles += amount;
    }
    
    /**
     * Uses wagon axles from the inventory.
     * Reduces the number of spare axles by the specified amount,
     * ensuring it never goes below zero. This is called when
     * replacing broken or worn-out axles.
     * 
     * @param amount The number of axles to use
     */
    public void useAxles(int amount) {
        this.axles = Math.max(0, this.axles - amount);
    }
    
    /**
     * Gets the current number of spare wagon tongues.
     * This value represents the number of replacement tongues
     * available for when the current tongue breaks or wears out.
     * 
     * @return The number of tongues
     */
    public int getTongues() {
        return tongues;
    }
    
    /**
     * Adds wagon tongues to the inventory.
     * Increases the number of spare tongues by the specified amount.
     * Each tongue adds 30 pounds to the wagon's total weight.
     * 
     * @param amount The number of tongues to add
     */
    public void addTongues(int amount) {
        this.tongues += amount;
    }
    
    /**
     * Uses wagon tongues from the inventory.
     * Reduces the number of spare tongues by the specified amount,
     * ensuring it never goes below zero. This is called when
     * replacing broken or worn-out tongues.
     * 
     * @param amount The number of tongues to use
     */
    public void useTongues(int amount) {
        this.tongues = Math.max(0, this.tongues - amount);
    }
    
    /**
     * Gets the current number of spare wagon bows.
     * This value represents the number of replacement bows
     * available for when the current bows break or wear out.
     * 
     * @return The number of wagon bows
     */
    public int getWagonBows() {
        return wagonBows;
    }
    
    /**
     * Adds wagon bows to the inventory.
     * Increases the number of spare bows by the specified amount.
     * Each bow adds 10 pounds to the wagon's total weight.
     * 
     * @param amount The number of bows to add
     */
    public void addWagonBows(int amount) {
        this.wagonBows += amount;
    }
    
    /**
     * Uses wagon bows from the inventory.
     * Reduces the number of spare bows by the specified amount,
     * ensuring it never goes below zero. This is called when
     * replacing broken or worn-out bows.
     * 
     * @param amount The number of bows to use
     */
    public void useWagonBows(int amount) {
        this.wagonBows = Math.max(0, this.wagonBows - amount);
    }
    
    /**
     * Gets the total number of spare wagon parts.
     * This is the sum of all spare parts (wheels, axles, tongues, and bows).
     * Used for quick checks of overall spare parts availability.
     * 
     * @return The total number of spare wagon parts
     */
    public int getWagonParts() {
        return wheels + axles + tongues + wagonBows;
    }
    
    /**
     * Uses wagon parts from the inventory.
     * Attempts to use the specified number of parts, starting with
     * the most commonly needed parts first (wheels, then axles, etc.).
     * This method is called when repairing broken parts.
     * 
     * @param amount The number of parts to use
     * @return true if enough parts were available, false otherwise
     */
    public boolean useWagonParts(int amount) {
        int remaining = amount;
        
        // Use wheels first (most commonly needed)
        int wheelsToUse = Math.min(remaining, wheels);
        useWheels(wheelsToUse);
        remaining -= wheelsToUse;
        
        if (remaining == 0) return true;
        
        // Then axles
        int axlesToUse = Math.min(remaining, axles);
        useAxles(axlesToUse);
        remaining -= axlesToUse;
        
        if (remaining == 0) return true;
        
        // Then tongues
        int tonguesToUse = Math.min(remaining, tongues);
        useTongues(tonguesToUse);
        remaining -= tonguesToUse;
        
        if (remaining == 0) return true;
        
        // Finally bows
        int bowsToUse = Math.min(remaining, wagonBows);
        useWagonBows(bowsToUse);
        remaining -= bowsToUse;
        
        return remaining == 0;
    }
    
    /**
     * Gets the break percentage for a specific wagon part.
     * This value represents the current condition of the part,
     * where 100 is perfect condition and 0 is completely broken.
     * 
     * @param name The name of the part (Wheel, Axle, Tongue, or Bow)
     * @return The break percentage (0-100)
     */
    public int getWagonPartBreakpercentage(String name) {
        for (int i = 0; i < WAGON_PARTS.length; i++) {
            if (WAGON_PARTS[i].equalsIgnoreCase(name)) {
                return WAGON_PARTS_BREAKPERCENTAGE[i];
            }
        }
        return 100;
    }
    
    /**
     * Repairs a random broken wagon part if one exists.
     * This method is called during rest periods or when using
     * repair supplies. It randomly selects a broken part to repair.
     * 
     * @return The name of the repaired part, or null if no parts were broken
     */
    public String repairRandomBrokenPart() {
        ArrayList<Integer> brokenIndices = new ArrayList<>();
        for (int i = 0; i < WAGON_PARTS_BROKEN.length; i++) {
            if (WAGON_PARTS_BROKEN[i]) {
                brokenIndices.add(i);
            }
        }
        
        if (brokenIndices.isEmpty()) {
            return null;
        }
        
        int randomIndex = brokenIndices.get((int)(Math.random() * brokenIndices.size()));
        WAGON_PARTS_BROKEN[randomIndex] = false;
        WAGON_PARTS_BREAKPERCENTAGE[randomIndex] = 100;
        
        return WAGON_PARTS[randomIndex];
    }
    
    /**
     * Gets the current number of medical kits.
     * This value represents the number of complete medical kits
     * available for treating injuries and illnesses.
     * 
     * @return The number of medical kits
     */
    public int getMedicine() {
        return medicine;
    }
    
    /**
     * Adds medical kits to the inventory.
     * Increases the number of medical kits by the specified amount.
     * Each kit adds 5 pounds to the wagon's total weight.
     * 
     * @param amount The number of medical kits to add
     */
    public void addMedicine(int amount) {
        this.medicine += amount;
    }
    
    /**
     * Uses medical kits from the inventory.
     * Reduces the number of medical kits by the specified amount,
     * ensuring it never goes below zero. This is called when
     * treating injuries or illnesses.
     * 
     * @param amount The number of medical kits to use
     */
    public void useMedicine(int amount) {
        this.medicine = Math.max(0, this.medicine - amount);
    }
    
    /**
     * Gets the current number of ammunition boxes.
     * Each box contains 20 rounds of ammunition for hunting.
     * 
     * @return The number of ammunition boxes
     */
    public int getAmmunition() {
        return ammunition;
    }
    
    /**
     * Adds ammunition boxes to the inventory.
     * Increases the number of ammunition boxes by the specified amount.
     * Each box adds 3 pounds to the wagon's total weight.
     * 
     * @param amount The number of ammunition boxes to add
     */
    public void addAmmunition(int amount) {
        this.ammunition += amount;
    }
    
    /**
     * Uses ammunition boxes from the inventory.
     * Reduces the number of ammunition boxes by the specified amount,
     * ensuring it never goes below zero. This is called when
     * hunting or in combat situations.
     * 
     * @param amount The number of ammunition boxes to use
     */
    public void useAmmunition(int amount) {
        this.ammunition = Math.max(0, this.ammunition - amount);
    }
    
    /**
     * Gets the current health percentage of the oxen team.
     * This value ranges from 0 to 100, where:
     * - 100: Perfect health
     * - 50: Moderate health issues
     * - 0: Critical condition
     * 
     * @return The oxen health percentage (0-100)
     */
    public int getOxenHealth() {
        return oxenHealth;
    }
    
    /**
     * Gets the current fatigue level of the oxen team.
     * This value ranges from 0 to 100, where:
     * - 0: Well-rested
     * - 50: Moderately tired
     * - 100: Exhausted
     * 
     * @return The oxen fatigue level (0-100)
     */
    public int getOxenFatigue() {
        return oxenFatigue;
    }
    
    /**
     * Decreases the health of the oxen team.
     * This method is called when the oxen are injured, sick,
     * or affected by harsh conditions. The health cannot go below 0.
     * 
     * @param amount The amount to decrease health by
     */
    public void decreaseOxenHealth(int amount) {
        this.oxenHealth = Math.max(0, this.oxenHealth - amount);
    }
    
    /**
     * Increases the health of the oxen team.
     * This method is called when the oxen are treated, rested,
     * or recover from injuries. The health cannot exceed 100.
     * 
     * @param amount The amount to increase health by
     */
    public void increaseOxenHealth(int amount) {
        this.oxenHealth = Math.min(100, this.oxenHealth + amount);
    }
    
    /**
     * Updates the fatigue level of the oxen team based on travel conditions.
     * This method is called after each day's travel and takes into account:
     * - Distance traveled
     * - Current weather conditions
     * - Whether the team is resting
     * 
     * @param distanceTraveled The distance traveled in miles
     * @param weather The current weather conditions
     * @param isResting Whether the team is resting
     */
    public void updateOxenFatigue(int distanceTraveled, String weather, boolean isResting) {
        if (isResting) {
            // Rest reduces fatigue
            oxenFatigue = Math.max(0, oxenFatigue - 20);
        } else {
            // Travel increases fatigue based on distance and conditions
            int fatigueIncrease = BASE_FATIGUE_RATE * distanceTraveled;
            
            // Weather effects
            if (weather.equals("Rain")) {
                fatigueIncrease *= 1.5;
            } else if (weather.equals("Snow")) {
                fatigueIncrease *= 2;
            }
            
            // Weight effect
            int weightFactor = getCurrentWeight() / 100;
            fatigueIncrease += weightFactor * WEIGHT_FATIGUE_FACTOR;
            
            oxenFatigue = Math.min(MAX_OXEN_FATIGUE, oxenFatigue + fatigueIncrease);
        }
    }
    
    /**
     * Rests the oxen team, reducing their fatigue.
     * This method is called during rest periods and reduces
     * fatigue by 30 points, but cannot go below 0.
     */
    public void restOxen() {
        oxenFatigue = Math.max(0, oxenFatigue - 30);
    }
    
    /**
     * Adds a custom item to the inventory.
     * This method is used for special items collected during
     * the journey that don't fit into standard categories.
     * 
     * @param item The item to add
     */
    public void addItem(Item item) {
        items.add(item);
    }
    
    /**
     * Gets the current total weight of the wagon and its contents.
     * This includes:
     * - Food weight
     * - Wagon parts weight
     * - Medical supplies weight
     * - Ammunition weight
     * - Custom items weight
     * 
     * @return The total weight in pounds
     */
    public int getCurrentWeight() {
        int totalWeight = 0;
        
        // Add food weight
        totalWeight += food;
        
        // Add wagon parts weight
        totalWeight += wheels * WHEEL_WEIGHT;
        totalWeight += axles * AXLE_WEIGHT;
        totalWeight += tongues * TONGUE_WEIGHT;
        totalWeight += wagonBows * WAGON_BOW_WEIGHT;
        
        // Add medical supplies weight
        totalWeight += medicine * MEDICINE_WEIGHT;
        
        // Add ammunition weight
        totalWeight += ammunition * AMMO_BOX_WEIGHT;
        
        // Add custom items weight
        for (Item item : items) {
            totalWeight += item.getWeight();
        }
        
        return totalWeight;
    }
    
    /**
     * Gets the maximum weight capacity of the wagon.
     * This is the total weight the wagon can carry, including
     * all supplies and the wagon itself.
     * 
     * @return The maximum weight capacity in pounds
     */
    public int getMaxWeightCapacity() {
        return MAX_WEIGHT_CAPACITY;
    }
    
    /**
     * Checks if the wagon has enough capacity for additional weight.
     * This method is called before adding new items to ensure
     * the wagon won't be overloaded.
     * 
     * @param additionalWeight The weight to check in pounds
     * @return true if there is enough capacity, false otherwise
     */
    public boolean hasWeightCapacity(int additionalWeight) {
        return getCurrentWeight() + additionalWeight <= MAX_WEIGHT_CAPACITY;
    }
    
    /**
     * Gets a custom item by name from the inventory.
     * This method searches through the custom items collection
     * for an item with the specified name.
     * 
     * @param name The name of the item to find
     * @return The item if found, null otherwise
     */
    public String getItem(String name) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item.toString();
            }
        }
        return null;
    }
    
    /**
     * Gets the spoilage rate for a specific food item.
     * This value represents how quickly the item spoils,
     * where higher values mean faster spoilage.
     * 
     * @param name The name of the food item
     * @return The spoilage rate (0.0 to 1.0)
     */
    public double getSpoilRate(String name) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item.getSpoilRate();
            }
        }
        return 0.0;
    }
    
    /**
     * Gets the weight of a specific item.
     * This method searches through both standard and custom items
     * to find the weight of the specified item.
     * 
     * @param name The name of the item
     * @return The weight in pounds, or 0 if not found
     */
    public double getWeight(String name) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item.getWeight();
            }
        }
        return 0.0;
    }
    
    /**
     * Applies food spoilage based on current weather conditions.
     * This method is called daily and simulates how different
     * weather conditions affect food preservation.
     * 
     * @param weather The current weather conditions
     * @param gameController The game controller for event handling
     */
    public void applyFoodSpoilage(Weather weather, GameController gameController) {
        double spoilageMultiplier = 1.0;
        
        // Weather effects on spoilage
        if (weather.getCurrentWeather().equals("Rain")) {
            spoilageMultiplier = 1.5;
        } else if (weather.getCurrentWeather().equals("Snow")) {
            spoilageMultiplier = 0.5; // Cold preserves food
        }
        
        // Apply spoilage to each food item
        for (Item item : items) {
            if (item.getSpoilRate() > 0) {
                double spoilage = item.getSpoilRate() * spoilageMultiplier;
                // Handle spoilage effects
                if (Math.random() < spoilage) {
                    gameController.handleFoodSpoilage(item.getName());
                }
            }
        }
    }
    
    /**
     * Checks for wagon part breakage during travel.
     * This method is called after each day's travel and
     * simulates the wear and tear on wagon parts.
     * 
     * @param gameController The game controller for event handling
     * @return The name of the broken part, or null if none broke
     */
    public String checkForPartBreakage(GameController gameController) {
        for (int i = 0; i < WAGON_PARTS.length; i++) {
            if (!WAGON_PARTS_BROKEN[i]) {
                // Increase wear based on the part's wear rate
                WAGON_PARTS_BREAKPERCENTAGE[i] -= WAGON_PARTS_WEAR_RATES[i];
                
                // Check if part breaks
                if (WAGON_PARTS_BREAKPERCENTAGE[i] <= 0) {
                    WAGON_PARTS_BROKEN[i] = true;
                    gameController.handlePartBreakage(WAGON_PARTS[i]);
                    return WAGON_PARTS[i];
                }
            }
        }
        return null;
    }
    
    /**
     * Checks if a specific wagon part is broken.
     * 
     * @param partName The name of the part to check
     * @return true if the part is broken, false otherwise
     */
    public boolean isPartBroken(String partName) {
        for (int i = 0; i < WAGON_PARTS.length; i++) {
            if (WAGON_PARTS[i].equalsIgnoreCase(partName)) {
                return WAGON_PARTS_BROKEN[i];
            }
        }
        return false;
    }
    
    /**
     * Repairs a specific wagon part.
     * This method is called when using spare parts to fix
     * a broken part. The part's condition is restored to 100%.
     * 
     * @param partName The name of the part to repair
     */
    public void repairPart(String partName) {
        for (int i = 0; i < WAGON_PARTS.length; i++) {
            if (WAGON_PARTS[i].equalsIgnoreCase(partName)) {
                WAGON_PARTS_BROKEN[i] = false;
                WAGON_PARTS_BREAKPERCENTAGE[i] = 100;
                return;
            }
        }
    }
    
    /**
     * Checks if any wagon parts are currently broken.
     * This method is used to determine if repairs are needed
     * before continuing the journey.
     * 
     * @return true if any parts are broken, false otherwise
     */
    public boolean hasBrokenParts() {
        for (boolean broken : WAGON_PARTS_BROKEN) {
            if (broken) {
                return true;
            }
        }
        return false;
    }

    public void setOxenHealth(int health) {
        this.oxenHealth = Math.max(0, Math.min(100, health));
    }
}