import java.util.ArrayList;

public class Inventory {
    private int food;
    private int oxen;
    private int wheels;
    private int axles;
    private int tongues;
    private int wagonBows;
    private int medicine;
    private int ammunition;
    private int oxenHealth;
    private int oxenFatigue;
    private final ArrayList<Item> items;
    
    private static final int MAX_WEIGHT_CAPACITY = 1500;
    private static final int MAX_OXEN_FATIGUE = 100;
    private static final int BASE_FATIGUE_RATE = 2;
    private static final int WEIGHT_FATIGUE_FACTOR = 1;
    
    private static final int MEDICINE_WEIGHT = 5;
    private static final int AMMO_BOX_WEIGHT = 3;
    
    private static final int WHEEL_WEIGHT = 50;
    private static final int AXLE_WEIGHT = 40;
    private static final int TONGUE_WEIGHT = 30;
    private static final int WAGON_BOW_WEIGHT = 10;
    private final int[] WAGON_PARTS_BREAKPERCENTAGE = new int[]{
            100, 100, 100, 100
    };
    private final String[] WAGON_PARTS = new String[]{
            "Wheel", "Axle", "Tongue", "Bow"
    };
    private final boolean[] WAGON_PARTS_BROKEN = new boolean[]{
            false, false, false, false
    };
    private final int[] WAGON_PARTS_WEAR_RATES = new int[]{
            2, 1, 1, 1  // Wheels wear out faster than other parts
    };
    private final String[] WAGON_PARTS_HISTORICAL_NOTES = new String[]{
            "The rough terrain and constant movement took its toll on the wagon wheels, a common issue on the Oregon Trail.",
            "The wooden axles, bearing the weight of the wagon and its contents, finally gave way under the strain.",
            "The wagon tongue, essential for steering and connecting the oxen, succumbed to the rigors of the journey.",
            "The wagon bows, supporting the canvas cover, weakened from exposure to the elements and constant movement."
    };

    public Inventory() {
        this.food = 0;
        this.oxen = 0;
        this.wheels = 0;
        this.axles = 0;
        this.tongues = 0;
        this.wagonBows = 0;
        this.medicine = 0;
        this.ammunition = 0;
        this.oxenHealth = 100;
        this.oxenFatigue = 0;
        this.items = new ArrayList<>();
    }

    public int getFood() {
        return food;
    }

    public void addFood(int amount) {
        this.food += amount;
    }

    public void consumeFood(int amount) {
        this.food -= amount;
        if (this.food < 0) {
            this.food = 0;
        }
    }

    public int getOxen() {
        return oxen;
    }

    public void addOxen(int amount) {
        this.oxen += amount;
    }
    
    public int getWheels() {
        return wheels;
    }
    
    public void addWheels(int amount) {
        this.wheels += amount;
    }
    
    public void useWheels(int amount) {
        this.wheels -= amount;
        if (this.wheels < 0) {
            this.wheels = 0;
        }
    }
    
    public int getAxles() {
        return axles;
    }
    
    public void addAxles(int amount) {
        this.axles += amount;
    }
    
    public void useAxles(int amount) {
        this.axles -= amount;
        if (this.axles < 0) {
            this.axles = 0;
        }
    }
    
    public int getTongues() {
        return tongues;
    }
    
    public void addTongues(int amount) {
        this.tongues += amount;
    }
    
    public void useTongues(int amount) {
        this.tongues -= amount;
        if (this.tongues < 0) {
            this.tongues = 0;
        }
    }
    
    public int getWagonBows() {
        return wagonBows;
    }
    
    public void addWagonBows(int amount) {
        this.wagonBows += amount;
    }
    
    public void useWagonBows(int amount) {
        this.wagonBows -= amount;
        if (this.wagonBows < 0) {
            this.wagonBows = 0;
        }
    }
    
    public int getWagonParts() {
        return wheels + axles + tongues + wagonBows;
    }
    
    public void useWagonParts(int amount) {
        int remaining = amount;
        
        int bowsToUse = Math.min(remaining, wagonBows);
        useWagonBows(bowsToUse);
        remaining -= bowsToUse;
        
        if (remaining <= 0) return;
        
        int tonguesToUse = Math.min(remaining, tongues);
        useTongues(tonguesToUse);
        remaining -= tonguesToUse;
        
        if (remaining <= 0) return;
        
        int axlesToUse = Math.min(remaining, axles);
        useAxles(axlesToUse);
        remaining -= axlesToUse;
        
        if (remaining <= 0) return;
        
        int wheelsToUse = Math.min(remaining, wheels);
        useWheels(wheelsToUse);
    }

    public int getWagonPartBreakpercentage(String name){
        switch(name){
            case "Wheel": return WAGON_PARTS_BREAKPERCENTAGE[0];
            case "Axle": return WAGON_PARTS_BREAKPERCENTAGE[1];
            case "Bow": return WAGON_PARTS_BREAKPERCENTAGE[2];
                case "Tongue": return WAGON_PARTS_BREAKPERCENTAGE[3];
            default: return 100;
        }
    }

    public String repairRandomBrokenPart() {
        // Collect list of broken parts first
        ArrayList<Integer> brokenPartsIndexes = new ArrayList<>();

        for (int i = 0; i < WAGON_PARTS_BREAKPERCENTAGE.length; i++) {
            if (WAGON_PARTS_BREAKPERCENTAGE[i] < 100) { // 100% means fully repaired
                brokenPartsIndexes.add(i);
            }
        }

        if (brokenPartsIndexes.isEmpty()) {
            return null; // No broken parts to repair
        }

        // Pick a random broken part
        int randomIndex = brokenPartsIndexes.get((int)(Math.random() * brokenPartsIndexes.size()));

        // Repair the part by restoring 20% health (or whatever you want)
        WAGON_PARTS_BREAKPERCENTAGE[randomIndex] += 20;
        if (WAGON_PARTS_BREAKPERCENTAGE[randomIndex] > 100) {
            WAGON_PARTS_BREAKPERCENTAGE[randomIndex] = 100; // Cap at full health
        }

        return WAGON_PARTS[randomIndex]; // Return the part name that was repaired
    }

    public int getMedicine() {
        return medicine;
    }

    public void addMedicine(int amount) {
        this.medicine += amount;
    }

    public void useMedicine(int amount) {
        this.medicine -= amount;
        if (this.medicine < 0) {
            this.medicine = 0;
        }
    }

    public int getAmmunition() {
        return ammunition;
    }

    public void addAmmunition(int amount) {
        this.ammunition += amount;
    }

    public void useAmmunition(int amount) {
        this.ammunition -= amount;
        if (this.ammunition < 0) {
            this.ammunition = 0;
        }
    }

    public int getOxenHealth() {
        return oxenHealth;
    }

    public int getOxenFatigue() {
        return oxenFatigue;
    }

    public void decreaseOxenHealth(int amount) {
        this.oxenHealth -= amount;
        if (this.oxenHealth < 0) {
            this.oxenHealth = 0;
        }
    }

    public void increaseOxenHealth(int amount) {
        this.oxenHealth += amount;
        if (this.oxenHealth > 100) {
            this.oxenHealth = 100;
        }
    }

    public void updateOxenFatigue(int distanceTraveled, String weather, boolean isResting) {
        if (isResting) {
            oxenFatigue = Math.max(0, oxenFatigue - 10);
            return;
        }

        int fatigueIncrease = BASE_FATIGUE_RATE + (distanceTraveled / 5);

        if (weather.contains("Rain") || weather.contains("Snow")) {
            fatigueIncrease += 2;
        } else if (weather.contains("Storm")) {
            fatigueIncrease += 4;
        } else if (weather.contains("Hot")) {
            fatigueIncrease += 3;
        }

        int weightOverCapacity = Math.max(0, getCurrentWeight() - MAX_WEIGHT_CAPACITY);
        fatigueIncrease += (weightOverCapacity / 100) * WEIGHT_FATIGUE_FACTOR;

        oxenFatigue = Math.min(MAX_OXEN_FATIGUE, oxenFatigue + fatigueIncrease);

        if (oxenFatigue >= 80) {
            decreaseOxenHealth(2);
        } else if (oxenFatigue >= 60) {
            decreaseOxenHealth(1);
        }
    }

    public void restOxen() {
        oxenFatigue = Math.max(0, oxenFatigue - 15);
        if (oxenFatigue < 40) {
            increaseOxenHealth(5);
        }
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public int getCurrentWeight() {
        int totalWeight = 0;
        
        totalWeight += food;
        
        totalWeight += medicine * MEDICINE_WEIGHT;
        
        totalWeight += (ammunition / 20) * AMMO_BOX_WEIGHT;
        if (ammunition % 20 > 0) {
            totalWeight += AMMO_BOX_WEIGHT;
        }
        
        totalWeight += wheels * WHEEL_WEIGHT;
        totalWeight += axles * AXLE_WEIGHT;
        totalWeight += tongues * TONGUE_WEIGHT;
        totalWeight += wagonBows * WAGON_BOW_WEIGHT;
        
        for (Item item : items) {
            totalWeight += item.getWeight();
        }
        
        return totalWeight;
    }
    
    public int getMaxWeightCapacity() {
        return MAX_WEIGHT_CAPACITY;
    }
    
    public boolean hasWeightCapacity(int additionalWeight) {
        return (getCurrentWeight() + additionalWeight) <= MAX_WEIGHT_CAPACITY;
    }

    public String getItem(String name){
        for(int i = 0; i < items.size(); i++){
            Item item = items.get(i);
            if(item.getName().equalsIgnoreCase(name)){
                return item.getName();
            }
        }
        return null;
    }

    public double getSpoilRate(String name) {
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            if (item.getName().equalsIgnoreCase(name)) {
                return item.getSpoilRate();
            }
        }
        return 0;
    }

    public double getWeight(String name){
        for(int i = 0; i < items.size(); i++){
            Item item = items.get(i);
            if (item.getName().equalsIgnoreCase(name)) {
                return item.getWeight();
            }
        }
        return 0;
    }

    /**
     * Applies food spoilage based on weather conditions
     * @param weather Current weather conditions
     * @param gameController Game controller for job bonuses
     */
    public void applyFoodSpoilage(Weather weather, GameController gameController) {
        // Base spoilage rate depends on weather
        double spoilageRate = 0.02; // 2% base spoilage per day
        
        String currentWeather = weather.getCurrentWeather();
        if (currentWeather.contains("Rain") || currentWeather.contains("Hot")) {
            spoilageRate = 0.05; // 5% spoilage in rain or hot weather
        } else if (currentWeather.contains("Storm")) {
            spoilageRate = 0.08; // 8% spoilage in stormy weather
        }
        
        // Apply Farmer job bonus if applicable
        double jobBonus = gameController.getJobBonus("food_spoilage");
        spoilageRate += jobBonus; // Will reduce spoilage for farmers
        
        // Ensure spoilage rate doesn't go negative
        if (spoilageRate < 0.005) spoilageRate = 0.005; // Minimum 0.5% spoilage
        
        // Calculate and apply spoilage
        int spoiledFood = (int)(food * spoilageRate);
        if (spoiledFood > 0) {
            food -= spoiledFood;
            if (food < 0) food = 0;
        }
    }
    
    /**
     * Checks for random wagon part breakage during travel
     * @param gameController Game controller for job bonuses
     * @return String describing what broke, or null if nothing broke
     */
    public String checkForPartBreakage(GameController gameController) {
        StringBuilder result = new StringBuilder();
        
        // Base chance of part breaking
        double breakChance = 0.05; // 5% chance per travel day
        
        // Apply Blacksmith job bonus if applicable
        double jobBonus = gameController.getJobBonus("part_breakage");
        breakChance += jobBonus; // Will reduce breakage for blacksmiths
        
        // Ensure break chance doesn't go negative
        if (breakChance < 0.01) breakChance = 0.01; // Minimum 1% chance
        
        // Apply wear and tear to all parts
        for (int i = 0; i < WAGON_PARTS.length; i++) {
            if (!WAGON_PARTS_BROKEN[i]) {
                // Reduce condition by wear rate
                WAGON_PARTS_BREAKPERCENTAGE[i] -= WAGON_PARTS_WEAR_RATES[i];
                
                // If condition drops to 0 or below, part breaks
                if (WAGON_PARTS_BREAKPERCENTAGE[i] <= 0) {
                    WAGON_PARTS_BREAKPERCENTAGE[i] = 0;
                    WAGON_PARTS_BROKEN[i] = true;
                    result.append("A wagon ").append(WAGON_PARTS[i].toLowerCase()).append(" has broken from wear and tear!\n");
                    result.append(WAGON_PARTS_HISTORICAL_NOTES[i]).append("\n");
                    result.append("Check the health tab to repair it.\n");
                }
            }
        }
        
        // Check for random breakage
        if (Math.random() < breakChance) {
            // Determine which part breaks
            int partIndex = (int)(Math.random() * WAGON_PARTS.length);
            String partName = WAGON_PARTS[partIndex];
            
            // Only break if not already broken
            if (!WAGON_PARTS_BROKEN[partIndex]) {
                WAGON_PARTS_BROKEN[partIndex] = true;
                WAGON_PARTS_BREAKPERCENTAGE[partIndex] = 0;
                result.append("A wagon ").append(partName.toLowerCase()).append(" has broken!\n");
                result.append(WAGON_PARTS_HISTORICAL_NOTES[partIndex]).append("\n");
                result.append("Check the health tab to repair it.\n");
            }
        }
        
        return result.length() > 0 ? result.toString() : null;
    }

    public boolean isPartBroken(String partName) {
        switch(partName) {
            case "Wheel": return WAGON_PARTS_BROKEN[0];
            case "Axle": return WAGON_PARTS_BROKEN[1];
            case "Bow": return WAGON_PARTS_BROKEN[2];
            case "Tongue": return WAGON_PARTS_BROKEN[3];
            default: return false;
        }
    }

    public void repairPart(String partName) {
        switch(partName) {
            case "Wheel": 
                if (wheels > 0) {
                    WAGON_PARTS_BROKEN[0] = false;
                    WAGON_PARTS_BREAKPERCENTAGE[0] = 100;
                    useWheels(1);
                }
                break;
            case "Axle": 
                if (axles > 0) {
                    WAGON_PARTS_BROKEN[1] = false;
                    WAGON_PARTS_BREAKPERCENTAGE[1] = 100;
                    useAxles(1);
                }
                break;
            case "Bow": 
                if (wagonBows > 0) {
                    WAGON_PARTS_BROKEN[2] = false;
                    WAGON_PARTS_BREAKPERCENTAGE[2] = 100;
                    useWagonBows(1);
                }
                break;
            case "Tongue": 
                if (tongues > 0) {
                    WAGON_PARTS_BROKEN[3] = false;
                    WAGON_PARTS_BREAKPERCENTAGE[3] = 100;
                    useTongues(1);
                }
                break;
        }
    }

    public boolean hasBrokenParts() {
        for (boolean broken : WAGON_PARTS_BROKEN) {
            if (broken) return true;
        }
        return false;
    }
}