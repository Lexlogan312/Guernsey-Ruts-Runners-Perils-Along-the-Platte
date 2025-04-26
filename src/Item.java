public class Item {
    private final String name;
    private final String description;
    private final int value;
    private final int weight; // Weight in pounds

    public Item(String name, String description, int value) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.weight = 1; // Default weight of 1 pound
    }
    
    public Item(String name, String description, int value, int weight) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getValue() {
        return value;
    }
    
    public int getWeight() {
        return weight;
    }
}