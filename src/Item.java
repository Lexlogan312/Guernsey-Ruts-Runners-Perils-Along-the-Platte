/**
 * Item Class of the Perils Along the Platte Game
 * Represents a basic item in the game with properties for name, weight, and spoilage rate.
 * Used as a base class for various items in the game's inventory system.
 *
 * @author Alex Randall and Chase McCluskey
 * @version 1.0
 * @date 05/06/2025
 * @file Item.java
 */
public class Item {
    private final String name;

    private final int weight;
    
    // The rate at which the item spoils between 0.0 and 1.0
    private double spoilRate;

    /**
     * Constructs a new Item with specified properties.
     * Initializes an item with its basic characteristics for use in the game.
     * 
     * @param name The name of the item
     * @param weight The weight of the item in pounds
     * @param spoilRate The rate at which the item spoils (0.0 to 1.0)
     */
    public Item(String name, int weight, double spoilRate) {
        this.name = name;
        this.weight = weight;
        this.spoilRate = spoilRate;
    }

    /**
     * Gets the name of the item.
     * Used for identification and display in the game interface.
     * 
     * @return The item's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the spoilage rate of the item.
     * The spoilage rate determines how quickly the item deteriorates:
     * - 0.0: Item never spoils (e.g., tools, weapons)
     * - 0.1-0.3: Slow spoilage (e.g., dried goods)
     * - 0.4-0.7: Moderate spoilage (e.g., fresh food)
     * - 0.8-1.0: Fast spoilage (e.g., perishable items)
     * 
     * @return The rate at which the item spoils (0.0 to 1.0)
     */
    public double getSpoilRate(){
        return spoilRate;
    }
  
    /**
     * Gets the weight of the item.
     * The weight affects various aspects of gameplay:
     * - Total wagon load capacity
     * - Travel speed and efficiency
     * - Oxen fatigue and health
     * - Resource consumption rates
     * 
     * @return The item's weight in pounds
     */
    public int getWeight() {
        return weight;
    }
}