public class Item {
    private final String name;
    private final String description;
    private final int value;
    private int weight;
    private double spoilRate;

    public Item(String name, String description, int value) {
        this.name = name;
        this.description = description;
        this.value = value;
    }

    public Item(String name, int weight, double spoilRate) {
        this.name = name;
        this.weight = weight;
        this.spoilRate = spoilRate;
        this.description = "";
        this.value = 0;
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

    public double getSpoilRate(){
        return spoilRate;
    }

    public int getWeight() {
        return weight;
    }
}