import java.util.ArrayList;

public class Map {
    private int trailChoice;
    private ArrayList<Landmark> landmarks;
    private int currentLandmarkIndex;
    private int distanceTraveled;
    private boolean hasRiverCrossing;

    public Map(int var1) {
        this.trailChoice = var1;
        this.landmarks = new ArrayList();
        this.currentLandmarkIndex = 0;
        this.distanceTraveled = 0;
        this.hasRiverCrossing = false;
        this.initializeLandmarks();
    }

    private void initializeLandmarks() {
        if (this.trailChoice == 1) {
            this.landmarks.add(new Landmark("Independence, Missouri", 0, "Independence was the primary starting point for the Oregon Trail. In the 1840s, it became a bustling outfitting and jumping-off point for westward travelers."));
            this.landmarks.add(new Landmark("Kansas River Crossing", 102, "This was one of the first river crossings emigrants faced. The Kansas River could be dangerous when swollen from rain."));
            this.landmarks.add(new Landmark("Fort Kearny", 250, "Established in 1848, Fort Kearny served as protection, rest stop, and supply station for travelers heading west."));
            this.landmarks.add(new Landmark("Chimney Rock", 600, "This distinctive natural landmark served as a navigational aid for travelers. Many emigrants recorded it in their diaries as a significant milestone."));
            this.landmarks.add(new Landmark("Fort Laramie", 750, "Fort Laramie was a significant trading post and military installation that offered emigrants supplies, repairs, and protection."));
            this.landmarks.add(new Landmark("Independence Rock", 950, "Known as the 'Register of the Desert,' emigrants would inscribe their names here. Reaching it by July 4th was considered essential for a successful journey."));
        } else if (this.trailChoice == 2) {
            this.landmarks.add(new Landmark("Independence, Missouri", 0, "Independence was the primary starting point for the California Trail. In the 1840s, it became a bustling outfitting and jumping-off point for westward travelers."));
            this.landmarks.add(new Landmark("Kansas River Crossing", 102, "This was one of the first river crossings emigrants faced. The Kansas River could be dangerous when swollen from rain."));
            this.landmarks.add(new Landmark("Fort Kearny", 250, "Established in 1848, Fort Kearny served as protection, rest stop, and supply station for travelers heading west."));
            this.landmarks.add(new Landmark("Chimney Rock", 600, "This distinctive natural landmark served as a navigational aid for travelers. Many emigrants recorded it in their diaries as a significant milestone."));
            this.landmarks.add(new Landmark("Fort Laramie", 750, "Fort Laramie was a significant trading post and military installation that offered emigrants supplies, repairs, and protection."));
            this.landmarks.add(new Landmark("Independence Rock", 950, "Known as the 'Register of the Desert,' emigrants would inscribe their names here. Reaching it by July 4th was considered essential for a successful journey."));
        } else if (this.trailChoice == 3) {
            this.landmarks.add(new Landmark("Nauvoo, Illinois", 0, "Nauvoo was the starting point of the Mormon exodus west. After persecution and the murder of their founder Joseph Smith, the Mormons were forced to abandon this city they had built."));
            this.landmarks.add(new Landmark("Sugar Creek", 7, "The first campsite after leaving Nauvoo, Sugar Creek was where the Mormons organized into traveling companies for the journey ahead."));
            this.landmarks.add(new Landmark("Garden Grove", 170, "Established as a way station for future Mormon emigrants, Garden Grove was a semi-permanent settlement where fields were plowed and crops planted."));
            this.landmarks.add(new Landmark("Mount Pisgah", 220, "Another significant way station established by the Mormons, Mount Pisgah featured hundreds of acres of cultivated fields to support later travelers."));
            this.landmarks.add(new Landmark("Council Bluffs", 300, "Known to the Mormons as Kanesville, this was a major outfitting point on the Missouri River where emigrants prepared for the journey west."));
            this.landmarks.add(new Landmark("Winter Quarters", 320, "This temporary settlement housed thousands of Mormons during the winter of 1846-47. Many died here due to exposure, malnutrition, and disease."));
            this.landmarks.add(new Landmark("Fort Kearny", 450, "Established in 1848, Fort Kearny served as protection, rest stop, and supply station for travelers heading west."));
            this.landmarks.add(new Landmark("Chimney Rock", 800, "This distinctive natural landmark served as a navigational aid for travelers. Many emigrants recorded it in their diaries as a significant milestone."));
            this.landmarks.add(new Landmark("Fort Laramie", 950, "Fort Laramie was a significant trading post and military installation that offered emigrants supplies, repairs, and protection."));
            this.landmarks.add(new Landmark("Independence Rock", 1150, "Known as the 'Register of the Desert,' emigrants would inscribe their names here. Reaching it by July 4th was considered essential for a successful journey."));
        }

    }

    public String getTrailName() {
        if (this.trailChoice == 1) {
            return "Oregon Trail";
        } else {
            return this.trailChoice == 2 ? "California Trail" : "Mormon Trail";
        }
    }

    public String getStartingLocation() {
        return ((Landmark)this.landmarks.get(0)).getName();
    }

    public String getCurrentLocation() {
        return ((Landmark)this.landmarks.get(this.currentLandmarkIndex)).getName();
    }

    public String getNextLandmark() {
        return this.currentLandmarkIndex < this.landmarks.size() - 1 ? ((Landmark)this.landmarks.get(this.currentLandmarkIndex + 1)).getName() : "End of Trail";
    }

    public int getDistanceToNextLandmark() {
        return this.currentLandmarkIndex < this.landmarks.size() - 1 ? ((Landmark)this.landmarks.get(this.currentLandmarkIndex + 1)).getDistance() - this.distanceTraveled : 0;
    }

    public String getDestination() {
        return ((Landmark)this.landmarks.get(this.landmarks.size() - 1)).getName();
    }

    public int getDistanceTraveled() {
        return this.distanceTraveled;
    }

    public void travel(int var1) {
        this.distanceTraveled += var1;
        this.hasRiverCrossing = Math.random() < 0.2;
    }

    public boolean checkForRiverCrossing() {
        return this.hasRiverCrossing;
    }

    public boolean hasReachedLandmark() {
        if (this.currentLandmarkIndex < this.landmarks.size() - 1) {
            return this.distanceTraveled >= ((Landmark)this.landmarks.get(this.currentLandmarkIndex + 1)).getDistance();
        } else {
            return false;
        }
    }

    public void advanceToNextLandmark() {
        if (this.currentLandmarkIndex < this.landmarks.size() - 1) {
            ++this.currentLandmarkIndex;
        }

    }

    public boolean hasReachedDestination() {
        return this.currentLandmarkIndex == this.landmarks.size() - 1;
    }

    public void displayHistoricalInfo() {
        System.out.println("\n=== " + this.getCurrentLocation() + " ===");
        System.out.println(((Landmark)this.landmarks.get(this.currentLandmarkIndex)).getDescription());
    }
    
    public ArrayList<Landmark> getLandmarks() {
        return this.landmarks;
    }
    
    public int getCurrentLandmarkIndex() {
        return this.currentLandmarkIndex;
    }
}
