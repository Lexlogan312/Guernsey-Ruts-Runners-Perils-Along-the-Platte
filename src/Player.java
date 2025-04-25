public class Player {
    private final String name;
    private final String gender;
    private int health;
    private boolean isDead;
    private String causeOfDeath;
    private final String[] familyMembers;
    private int money;

    public Player(String name, String gender) {
        this.name = name;
        this.gender = gender;
        this.health = 100;
        this.isDead = false;
        this.causeOfDeath = "";
        this.money = 1000;
        this.familyMembers = new String[3]; // Three family members
    }

    public void setFamilyMembers(String[] members) {
        if (members.length <= 3) {
            System.arraycopy(members, 0, this.familyMembers, 0, members.length);
        }
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
        this.health -= amount;
        if (this.health <= 0) {
            this.health = 0;
            this.isDead = true;

            // If cause of death isn't set, default to "poor health"
            if (this.causeOfDeath.isEmpty()) {
                this.causeOfDeath = "poor health";
            }
        }
    }

    public void increaseHealth(int amount) {
        this.health += amount;
        if (this.health > 100) {
            this.health = 100;
        }
    }

    public void rest() {
        increaseHealth(10);
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public String getCauseOfDeath() {
        return causeOfDeath;
    }

    public void setCauseOfDeath(String cause) {
        this.causeOfDeath = cause;
    }
}