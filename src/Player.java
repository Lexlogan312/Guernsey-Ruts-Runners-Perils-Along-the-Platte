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

    public Player(String name, String gender, Job job) {
        this.name = name;
        this.gender = gender;
        this.morale = 100;
        this.health = 100; //starting health
        this.isDead = false;
        this.causeOfDeath = "";
        this.job = job;
        this.money = 1000;
        this.familyMembers = new String[3]; // Three family members
    }

    public void setFamilyMembers(String[] members) {
        if (members.length <= 3) {
            System.arraycopy(members, 0, this.familyMembers, 0, members.length);
        }
    }

    public Job getJob() {
        return job;
    }

    public int getMorale() {
        return morale;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String[] getFamilyMembers() {
        return familyMembers;
    }

    public int getFamilySize() {
        return familyMembers.length + 1; // Player + family members
    }

    public int getMoney() {
        return money;
    }

    public void spendMoney(int amount) {
        this.money -= amount;
        if (this.money < 0) {
            this.money = 0;
        }
    }

    public void addMoney(int amount) {
        this.money += amount;
    }

    public int getHealth() {
        return health;
    }

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

    public void decreaseHealth(int amount) {
        health -= amount;
        if (health <= 0) {
            health = 0;
            isDead = true;
        }
    }
    
    /**
     * Decreases health and assigns a specific cause if the player dies
     * @param amount Amount of health to decrease
     * @param cause Specific cause to assign if player dies from this damage
     */
    public void decreaseHealth(int amount, String cause) {
        health -= amount;
        if (health <= 0) {
            health = 0;
            this.causeOfDeath = cause;
            isDead = true;
        }
    }

    public void increaseHealth(int amount){
        health += amount;
        if(health > 100){
            health = 100;
        }
    }
    
    /**
     * Decreases morale by specified amount without job bonus
     * @param amount Amount to decrease
     */
    public void decreaseMorale(int amount) {
        this.morale -= amount;
        if (this.morale < 0) {
            this.morale = 0;
        }
    }
    
    /**
     * Increases morale by specified amount without job bonus
     * @param amount Amount to increase
     */
    public void increaseMorale(int amount) {
        this.morale += amount;
        if (this.morale > 100) {
            this.morale = 100;
        }
    }

    public boolean isDead() {
        return isDead;
    }

    public String getCauseOfDeath() {
        return causeOfDeath;
    }

    public void setCauseOfDeath(String cause) {
        this.causeOfDeath = cause;
    }

    public double getPartBreakModifier() {
        if (job == Job.BLACKSMITH) {
            return 0.75; // 25% fewer breaks
        }
        return 1.0; // default
    }

    // Example method: How job affects food spoilage
    public double getFoodSpoilageModifier() {
        if (job == Job.FARMER) {
            return 0.75; // 25% less spoilage
        }
        return 1.0;
    }

    // Example method: How job affects hunting success
    public double getHuntingSuccessModifier() {
        if (job == Job.HUNTER) {
            return 1.2; // 20% better success
        }
        return 1.0;
    }

    public double getDoctorModifier() {
        if(job == Job.DOCTOR) {
            return 1.2;
        }
        return 1.0;
    }

    public double getBuyMerchantModifier(){
        if(job == Job.MERCHANT) {
            return .85;
        }
        return 1.0;
    }

    public double getSellMerchantModifier(){
        if(job == Job.MERCHANT) {
            return 1.15;
        }
        return 1.0;
    }

    public double getPreacherMoralModifier(){
        if(job == Job.PREACHER) {
            return 1.25;
        }
        return 1.0;
    }
}