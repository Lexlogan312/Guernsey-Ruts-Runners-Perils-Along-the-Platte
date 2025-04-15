import java.util.ArrayList;

public class Inventory {
    private int food;
    private int oxen;
    private int wagonParts;
    private int medicine;
    private int ammunition;
    private int oxenHealth;
    private ArrayList<Item> items;

    public Inventory() {
        this.food = 0;
        this.oxen = 0;
        this.wagonParts = 0;
        this.medicine = 0;
        this.ammunition = 0;
        this.oxenHealth = 100;
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

    public int getWagonParts() {
        return wagonParts;
    }

    public void addWagonParts(int amount) {
        this.wagonParts += amount;
    }

    public void useWagonParts(int amount) {
        this.wagonParts -= amount;
        if (this.wagonParts < 0) {
            this.wagonParts = 0;
        }
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

    public void displayInventory() {
        System.out.println("\n=== INVENTORY ===");
        System.out.println("Food: " + food + " pounds");
        System.out.println("Oxen: " + oxen + " (Health: " + oxenHealth + "%)");
        System.out.println("Wagon parts: " + wagonParts);
        System.out.println("Medicine: " + medicine);
        System.out.println("Ammunition: " + ammunition + " rounds");

        if (!items.isEmpty()) {
            System.out.println("\nOther items:");
            for (Item item : items) {
                System.out.println("- " + item.getName() + ": " + item.getDescription());
            }
        }
    }
} 