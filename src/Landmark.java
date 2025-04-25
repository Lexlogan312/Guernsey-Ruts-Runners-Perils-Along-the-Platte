public class Landmark {
    private String name;
    private int distance;
    private String description;
    private int imageX;
    private int imageY;
    private int labelX;
    private int labelY;

    public Landmark(String name, int distance, int imageX, int imageY, String description) {
        this.name = name;
        this.distance = distance;
        this.imageX = imageX;
        this.imageY = imageY;
        this.description = description;
        this.labelX = 0; // Default value (0 means use automatic positioning)
        this.labelY = 0; // Default value (0 means use automatic positioning)
    }
    
    public Landmark(String name, int distance, int imageX, int imageY, int labelX, int labelY, String description) {
        this.name = name;
        this.distance = distance;
        this.imageX = imageX;
        this.imageY = imageY;
        this.labelX = labelX;
        this.labelY = labelY;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public int getDistance() {
        return distance;
    }

    public String getDescription() {
        return description;
    }

    public int getImageX() {
        return imageX;
    }

    public int getImageY() {
        return imageY;
    }
    
    public int getLabelX() {
        return labelX;
    }
    
    public int getLabelY() {
        return labelY;
    }
    
    // Used to check if custom label position is provided
    public boolean hasCustomLabelPosition() {
        return labelX != 0 || labelY != 0;
    }
}