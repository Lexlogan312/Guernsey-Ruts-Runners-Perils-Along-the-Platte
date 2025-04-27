import java.util.ArrayList;

public class Inventory {
    private int food;
    private String foodName;
    private int oxen;
    private int wheels;
    private int axles;
    private int tongues;
    private int wagonBows;
    private int medicine;
    private int ammunition;
    private int oxenHealth;
    private final ArrayList<Item> items;
    
    private static final int MAX_WEIGHT_CAPACITY = 1500;
    
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
        this.items = new ArrayList<>();
    }

    public int getFood() {
        return food;
    }

    public String getFoodName(String name){
        return this.foodName;
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
    
    public void addWagonParts(int amount) {
        int remaining = amount;
        
        int halfRemaining = remaining / 2;
        addWheels(halfRemaining);
        remaining -= halfRemaining;
        
        addAxles(halfRemaining);
        remaining -= halfRemaining;
        
        if (remaining > 0) {
            addTongues(remaining);
        }
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

    public void addItem(Item item) {
        items.add(item);
    }

    public ArrayList<Item> getItems() {
        return items;
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
            Item item = items.get(i); // Grab the Item from the list
            if (item.getName().equalsIgnoreCase(name)) { // Compare name properly
                return item.getSpoilRate(); // Return the spoil rate
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

    public void displayInventory() {
        System.out.println("\n=== INVENTORY ===");
        System.out.println("Load: " + getCurrentWeight() + "/" + MAX_WEIGHT_CAPACITY + " pounds");
        System.out.println("Food: " + food + " pounds");
        System.out.println("Oxen: " + oxen + " (Health: " + oxenHealth + "%)");
        
        System.out.println("Wagon parts:");
        System.out.println("- Wheels: " + wheels);
        System.out.println("- Axles: " + axles);
        System.out.println("- Tongues: " + tongues);
        System.out.println("- Wagon Bows: " + wagonBows);
        
        System.out.println("Medicine kits: " + medicine);
        System.out.println("Ammunition: " + ammunition + " rounds");

        if (!items.isEmpty()) {
            System.out.println("\nOther items:");
            for (Item item : items) {
                System.out.println("- " + item.getName() + ": " + item.getDescription());
            }
        }
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
        // Base chance of part breaking
        double breakChance = 0.05; // 5% chance per travel day
        
        // Apply Blacksmith job bonus if applicable
        double jobBonus = gameController.getJobBonus("part_breakage");
        breakChance += jobBonus; // Will reduce breakage for blacksmiths
        
        // Ensure break chance doesn't go negative
        if (breakChance < 0.01) breakChance = 0.01; // Minimum 1% chance
        
        // Check if a part breaks
        if (Math.random() < breakChance) {
            // Determine which part breaks
            int partIndex = (int)(Math.random() * WAGON_PARTS.length);
            String partName = WAGON_PARTS[partIndex];
            
            // Apply the breakage
            switch (partIndex) {
                case 0: // Wheel
                    if (wheels > 0) {
                        wheels--;
                        return "A wagon wheel broke";
                    }
                    break;
                case 1: // Axle
                    if (axles > 0) {
                        axles--;
                        return "A wagon axle broke";
                    }
                    break;
                case 2: // Tongue
                    if (tongues > 0) {
                        tongues--;
                        return "The wagon tongue broke";
                    }
                    break;
                case 3: // Bow
                    if (wagonBows > 0) {
                        wagonBows--;
                        return "A wagon bow broke";
                    }
                    break;
            }
        }
        
        return null; // Nothing broke
    }
}