/**
 * Landmark Class of the Perils Along the Platte Game
 * Represents a significant location along the trail with properties for display and interaction.
 * Includes position information for both the landmark image and its label on the map.
 *
 * @author Alex Randall and Chase McCluskey
 * @version 1.0
 * @date 05/06/2025
 * @file Landmark.java
 */
public class Landmark {
    //The name of the landmark
    private final String name;

    //The distance from the starting point in miles
    private final int distance;

    // A detailed description of the landmark and its significance
    private final String description;
    
    // The x-coordinate for displaying the landmark's image on the map
    private final int imageX;
    
    // The y-coordinate for displaying the landmark's image on the map
    private final int imageY;
    
    // The x-coordinate for displaying the landmark's label on the map
    private final int labelX;

    // The y-coordinate for displaying the landmark's label on the ma[
    private final int labelY;

    /**
     * Constructs a new Landmark with basic properties.
     * Label coordinates are set to (0,0) by default.
     * This constructor is used for landmarks that don't
     * require specific label positioning.
     * 
     * @param name The name of the landmark (must not be null or empty)
     * @param distance The distance from the starting point in miles (must be non-negative)
     * @param imageX The x-coordinate for the landmark image
     * @param imageY The y-coordinate for the landmark image
     * @param description A description of the landmark (must not be null)
     */
    public Landmark(String name, int distance, int imageX, int imageY, String description) {
        this.name = name;
        this.distance = distance;
        this.imageX = imageX;
        this.imageY = imageY;
        this.description = description;
        this.labelX = 0;
        this.labelY = 0;
    }
    
    /**
     * Constructs a new Landmark with all properties including label coordinates.
     * This constructor is used for landmarks that require
     * specific positioning for both image and label.
     * 
     * @param name The name of the landmark (must not be null or empty)
     * @param distance The distance from the starting point in miles (must be non-negative)
     * @param imageX The x-coordinate for the landmark image
     * @param imageY The y-coordinate for the landmark image
     * @param labelX The x-coordinate for the landmark label
     * @param labelY The y-coordinate for the landmark label
     * @param description A description of the landmark (must not be null)
     */
    public Landmark(String name, int distance, int imageX, int imageY, int labelX, int labelY, String description) {
        this.name = name;
        this.distance = distance;
        this.imageX = imageX;
        this.imageY = imageY;
        this.labelX = labelX;
        this.labelY = labelY;
        this.description = description;
    }

    /**
     * Gets the name of the landmark.
     * Returns the historical name of the location
     * as it was known during the westward migration.
     * 
     * @return The landmark's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the distance of the landmark from the starting point.
     * Returns the exact distance in miles from the
     * trail's starting point to this landmark.
     * 
     * @return The distance in miles
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Gets the description of the landmark.
     * Returns the historical context and significance
     * of the landmark, including notable events and
     * challenges associated with the location.
     * 
     * @return The landmark's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the x-coordinate for the landmark image.
     * Returns the horizontal position where the
     * landmark's visual representation should be placed.
     * 
     * @return The x-coordinate for image placement
     */
    public int getImageX() {
        return imageX;
    }

    /**
     * Gets the y-coordinate for the landmark image.
     * Returns the vertical position where the
     * landmark's visual representation should be placed.
     * 
     * @return The y-coordinate for image placement
     */
    public int getImageY() {
        return imageY;
    }
    
    /**
     * Gets the x-coordinate for the landmark label.
     * Returns the horizontal position where the
     * landmark's name should be displayed.
     * 
     * @return The x-coordinate for label placement
     */
    public int getLabelX() {
        return labelX;
    }
    
    /**
     * Gets the y-coordinate for the landmark label.
     * Returns the vertical position where the
     * landmark's name should be displayed.
     * 
     * @return The y-coordinate for label placement
     */
    public int getLabelY() {
        return labelY;
    }
}