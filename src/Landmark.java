public class Landmark {
    private final String name;
    private final int distance;
    private final String description;
    private final int imageX;
    private final int imageY;
    private final int labelX;
    private final int labelY;

    public Landmark(String name, int distance, int imageX, int imageY, String description) {
        this.name = name;
        this.distance = distance;
        this.imageX = imageX;
        this.imageY = imageY;
        this.description = description;
        this.labelX = 0;
        this.labelY = 0;
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

}