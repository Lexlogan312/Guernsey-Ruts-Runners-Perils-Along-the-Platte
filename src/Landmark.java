public class Landmark {
    private String name;
    private int distance;
    private String description;

    public Landmark(String name, int distance, String description) {
        this.name = name;
        this.distance = distance;
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
}