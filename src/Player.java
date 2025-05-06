/**
 * Player Class of the Perils Along the Platte Game
 * Represents the player character in the game, managing their attributes, health, morale,
 * and job-specific bonuses. Handles all player-related state and calculations.
 *
 * @author Alex Randall and Chase McCluskey
 * @version 1.0
 * @date 05/06/2025
 * @file Player.java
 */

public class Player {
    private final String name;

    private final String gender;

    private int health;

    private boolean isDead;

    private String causeOfDeath;

    private final String[] familyMembers;

    private int money;

    private final Job job;

    private int morale;

    /**
     * Constructs a new Player with initial attributes.
     * Initializes the player with:
     * - Full health (100)
     * - Full morale (100)
     * - Starting money (1000)
     * - Three family member slots
     * 
     * @param name The player's name (must not be null or empty)
     * @param gender The player's gender (must not be null or empty)
     * @param job The player's occupation (must not be null)
     */
    public Player(String name, String gender, Job job) {    
        this.name = name;
        this.gender = gender;
        this.morale = 100;
        this.health = 100;
        this.isDead = false;
        this.causeOfDeath = "";
        this.job = job;
        this.money = 1000;
        this.familyMembers = new String[3];
    }

    /**
     * Sets the player's family members.
     * Copies up to 3 family member names into the player's family array.
     * 
     * @param members Array of family member names (maximum 3)
     */
    public void setFamilyMembers(String[] members) {
        if (members.length <= 3) {
            System.arraycopy(members, 0, this.familyMembers, 0, members.length);
        }
    }

    /**
     * Gets the player's occupation.
     * Returns the job enum that provides specific bonuses and abilities.
     * 
     * @return The player's job
     */
    public Job getJob() {
        return job;
    }

    /**
     * Gets the player's current morale level.
     * Returns a value between 0 and 100.
     * 
     * @return The morale value (0-100)
     */
    public int getMorale() {
        return morale;
    }

    /**
     * Gets the player's name.
     * Returns the name set during player creation.
     * 
     * @return The player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the player's gender.
     * Returns the gender set during player creation.
     * 
     * @return The player's gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Gets the player's family members.
     * Returns an array of up to 3 family member names.
     * 
     * @return Array of family member names
     */
    public String[] getFamilyMembers() {
        return familyMembers;
    }

    /**
     * Gets the total size of the player's family including themselves.
     * Used for resource consumption calculations and event outcomes.
     * 
     * @return Total number of family members plus the player
     */
    public int getFamilySize() {
        return familyMembers.length + 1;
    }

    /**
     * Gets the player's current money.
     * Returns the amount of money available for spending.
     * 
     * @return The amount of money
     */
    public int getMoney() {
        return money;
    }

    /**
     * Spends money from the player's funds.
     * Ensures money cannot go below 0.
     * 
     * @param amount The amount to spend (must be non-negative)
     */
    public void spendMoney(int amount) {
        this.money = Math.max(0, this.money - amount);
    }

    /**
     * Adds money to the player's funds.
     * 
     * @param amount The amount to add (must be non-negative)
     */
    public void addMoney(int amount) {
        this.money += amount;
    }

    /**
     * Gets the player's current health.
     * Returns a value between 0 and 100.
     * 
     * @return The health value (0-100)
     */
    public int getHealth() {
        return health;
    }

    /**
     * Gets a text description of the player's health status.
     * Returns a descriptive string based on health ranges:
     * 
     * @return Health status as:
     *         - "Good" (81-100)
     *         - "Fair" (51-80)
     *         - "Poor" (21-50)
     *         - "Very poor" (0-20)
     */
    public String getHealthStatus() {
        if (health > 80) {
            return "Good";
        } else if (health > 50) {
            return "Fair";
        } else if (health > 20) {
            return "Poor";
        } else {
            return "Very poor";
        }
    }

    /**
     * Decreases the player's health by a specified amount.
     * Sets player as dead if health reaches 0.
     * 
     * @param amount The amount of health to decrease (must be non-negative)
     */
    public void decreaseHealth(int amount) {
        health = Math.max(0, health - amount);
        if (health == 0) {
            isDead = true;
        }
    }
    
    /**
     * Decreases health and assigns a specific cause if the player dies.
     * Sets player as dead if health reaches 0.
     * 
     * @param amount Amount of health to decrease (must be non-negative)
     * @param cause Specific cause to assign if player dies from this damage
     */
    public void decreaseHealth(int amount, String cause) {
        health = Math.max(0, health - amount);
        if (health == 0) {
            this.causeOfDeath = cause;
            isDead = true;
        }
    }

    /**
     * Increases the player's health by a specified amount.
     * Caps health at 100.
     * 
     * @param amount The amount of health to increase (must be non-negative)
     */
    public void increaseHealth(int amount) {
        health = Math.min(100, health + amount);
    }

    /**
     * Decreases the player's morale by a specified amount.
     * Caps morale at 0.
     * 
     * @param amount The amount of morale to decrease (must be non-negative)
     */
    public void decreaseMorale(int amount) {
        morale = Math.max(0, morale - amount);
    }

    /**
     * Increases the player's morale by a specified amount.
     * Caps morale at 100.
     * 
     * @param amount The amount of morale to increase (must be non-negative)
     */
    public void increaseMorale(int amount) {
        morale = Math.min(100, morale + amount);
    }

    /**
     * Checks if the player is dead.
     * Returns true if health has reached 0.
     * 
     * @return true if the player is dead, false otherwise
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     * Gets the cause of the player's death.
     * Returns an empty string if the player is alive.
     * 
     * @return The cause of death, or empty string if alive
     */
    public String getCauseOfDeath() {
        return causeOfDeath;
    }

    /**
     * Sets the cause of the player's death.
     * Only used when the player dies from specific events.
     * 
     * @param cause The specific cause of death
     */
    public void setCauseOfDeath(String cause) {
        this.causeOfDeath = cause;
    }

    /**
     * Gets the wagon part breakage modifier based on the player's job.
     * Returns a multiplier that affects the chance of wagon parts breaking.
     * 
     * @return The part breakage modifier (e.g., 0.75 for Blacksmith)
     */
    public double getPartBreakModifier() {
        return job == Job.BLACKSMITH ? 0.75 : 1.0;
    }

    /**
     * Gets the food spoilage modifier based on the player's job.
     * Returns a multiplier that affects the rate of food spoilage.
     * 
     * @return The food spoilage modifier (e.g., 0.75 for Farmer)
     */
    public double getFoodSpoilageModifier() {
        return job == Job.FARMER ? 0.75 : 1.0;
    }

    /**
     * Gets the hunting success modifier based on the player's job.
     * Returns a multiplier that affects the chance of successful hunting.
     * 
     * @return The hunting success modifier (e.g., 1.25 for Hunter)
     */
    public double getHuntingSuccessModifier() {
        return job == Job.HUNTER ? 1.25 : 1.0;
    }

    /**
     * Gets the medical treatment modifier based on the player's job.
     * Returns a multiplier that affects the effectiveness of medical treatments.
     * 
     * @return The medical treatment modifier (e.g., 1.25 for Doctor)
     */
    public double getDoctorModifier() {
        return job == Job.DOCTOR ? 1.25 : 1.0;
    }

    /**
     * Gets the buying price modifier based on the player's job.
     * Returns a multiplier that affects the cost of purchased items.
     * 
     * @return The buying price modifier (e.g., 0.9 for Merchant)
     */
    public double getBuyMerchantModifier() {
        return job == Job.MERCHANT ? 0.9 : 1.0;
    }

    /**
     * Gets the selling price modifier based on the player's job.
     * Returns a multiplier that affects the value of sold items.
     * 
     * @return The selling price modifier (e.g., 1.1 for Merchant)
     */
    public double getSellMerchantModifier() {
        return job == Job.MERCHANT ? 1.1 : 1.0;
    }

    /**
     * Gets the morale loss modifier based on the player's job.
     * Returns a multiplier that affects the rate of morale loss.
     * 
     * @return The morale loss modifier (e.g., 0.75 for Preacher)
     */
    public double getPreacherMoralModifier() {
        return job == Job.PREACHER ? 0.75 : 1.0;
    }
}