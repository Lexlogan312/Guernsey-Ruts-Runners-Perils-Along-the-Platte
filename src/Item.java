public class Item {
    private final String name;
    private final int weight;
    private double spoilRate;

    public Item(String name, int weight, double spoilRate) {
        this.name = name;
        this.weight = weight;
        this.spoilRate = spoilRate;
    }

    public String getName() {
        return name;
    }

    public double getSpoilRate(){
        return spoilRate;
    }
  
    public int getWeight() {
        return weight;
    }
}