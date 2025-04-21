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
            this.landmarks.add(new Landmark("Kansas River Crossing", 100, "A major early river crossing near present-day Topeka, Kansas, where emigrants began adapting to frontier travel."));
            this.landmarks.add(new Landmark("Big Blue River Crossing", 160, "Located near Marysville, Kansas, this river crossing was known for its challenging currents and required careful wagon handling."));
            this.landmarks.add(new Landmark("Fort Kearny", 300, "Established in 1848, Fort Kearny was the first U.S. Army post along the trail and offered supplies, protection, and rest."));
            this.landmarks.add(new Landmark("Buffalo Creek", 360, "A small stream west of Fort Kearny, known for nearby buffalo and good grazing. A common stop for pioneers."));
            this.landmarks.add(new Landmark("Alkali Lakes", 420, "These shallow, salty lakes were often avoided for drinking water. They reminded emigrants of the environmental challenges of the plains."));
            this.landmarks.add(new Landmark("Windlass Hill", 495, "A steep descent into Ash Hollow. Wagons had to be locked or roped to descend safely."));
            this.landmarks.add(new Landmark("Ash Hollow", 500, "A steep descent into the North Platte Valley, it offered fresh water and grass but was difficult for wagons."));
            this.landmarks.add(new Landmark("Ancient Bluff Ruins", 515, "Natural rock formations resembling ruined buildings. Noted in journals for their striking appearance."));
            this.landmarks.add(new Landmark("Courthouse Rock and Jail Rock", 530, "These sandstone formations served as early landmarks for travelers entering western Nebraska."));
            this.landmarks.add(new Landmark("Chimney Rock", 550, "This towering spire became one of the most iconic landmarks on the trail, often mentioned in emigrant journals."));
            this.landmarks.add(new Landmark("Scotts Bluff", 570, "A large bluff that required a detour around Mitchell Pass. It marked a difficult but scenic part of the journey."));
            this.landmarks.add(new Landmark("Horse Creek", 630, "A stream near Fort Laramie and the site of the 1851 treaty between U.S. officials and Plains tribes."));
            this.landmarks.add(new Landmark("Fort Laramie", 640, "Originally a fur trading post, Fort Laramie became a key military post offering protection, mail, and supplies."));
            this.landmarks.add(new Landmark("Guernsey Ruts", 670, "Deep wagon ruts carved into sandstone by thousands of wagon wheels, still visible today."));
            this.landmarks.add(new Landmark("Register Cliff", 675, "A soft limestone bluff where emigrants carved their names, leaving a record of their passing."));
            this.landmarks.add(new Landmark("Red Buttes", 695, "Red sandstone hills that marked a nearby North Platte River ford and the final stretch before Independence Rock."));
            this.landmarks.add(new Landmark("Independence Rock", 705, "Nicknamed the 'Great Register of the Desert,' emigrants scratched their names into this granite rock hoping to reach it by July 4."));
        } else if (this.trailChoice == 2) {
            this.landmarks.add(new Landmark("Independence, Missouri", 0, "Independence was the primary starting point for the Oregon Trail. In the 1840s, it became a bustling outfitting and jumping-off point for westward travelers."));
            this.landmarks.add(new Landmark("Kansas River Crossing", 100, "A major early river crossing near present-day Topeka, Kansas, where emigrants began adapting to frontier travel."));
            this.landmarks.add(new Landmark("Big Blue River Crossing", 160, "Located near Marysville, Kansas, this river crossing was known for its challenging currents and required careful wagon handling."));
            this.landmarks.add(new Landmark("Fort Kearny", 300, "Established in 1848, Fort Kearny was the first U.S. Army post along the trail and offered supplies, protection, and rest."));
            this.landmarks.add(new Landmark("Buffalo Creek", 360, "A small stream west of Fort Kearny, known for nearby buffalo and good grazing. A common stop for pioneers."));
            this.landmarks.add(new Landmark("Alkali Lakes", 420, "These shallow, salty lakes were often avoided for drinking water. They reminded emigrants of the environmental challenges of the plains."));
            this.landmarks.add(new Landmark("Windlass Hill", 495, "A steep descent into Ash Hollow. Wagons had to be locked or roped to descend safely."));
            this.landmarks.add(new Landmark("Ash Hollow", 500, "A steep descent into the North Platte Valley, it offered fresh water and grass but was difficult for wagons."));
            this.landmarks.add(new Landmark("Ancient Bluff Ruins", 515, "Natural rock formations resembling ruined buildings. Noted in journals for their striking appearance."));
            this.landmarks.add(new Landmark("Courthouse Rock and Jail Rock", 530, "These sandstone formations served as early landmarks for travelers entering western Nebraska."));
            this.landmarks.add(new Landmark("Chimney Rock", 550, "This towering spire became one of the most iconic landmarks on the trail, often mentioned in emigrant journals."));
            this.landmarks.add(new Landmark("Scotts Bluff", 570, "A large bluff that required a detour around Mitchell Pass. It marked a difficult but scenic part of the journey."));
            this.landmarks.add(new Landmark("Horse Creek", 630, "A stream near Fort Laramie and the site of the 1851 treaty between U.S. officials and Plains tribes."));
            this.landmarks.add(new Landmark("Fort Laramie", 640, "Originally a fur trading post, Fort Laramie became a key military post offering protection, mail, and supplies."));
            this.landmarks.add(new Landmark("Guernsey Ruts", 670, "Deep wagon ruts carved into sandstone by thousands of wagon wheels, still visible today."));
            this.landmarks.add(new Landmark("Register Cliff", 675, "A soft limestone bluff where emigrants carved their names, leaving a record of their passing."));
            this.landmarks.add(new Landmark("Red Buttes", 695, "Red sandstone hills that marked a nearby North Platte River ford and the final stretch before Independence Rock."));
            this.landmarks.add(new Landmark("Independence Rock", 705, "Nicknamed the 'Great Register of the Desert,' emigrants scratched their names into this granite rock hoping to reach it by July 4."));
        } else if (this.trailChoice == 3) {
            this.landmarks.add(new Landmark("Nauvoo, Illinois", 0, "Nauvoo was the home of the early Latter-day Saints community. Persecution forced their migration west in 1846."));
            this.landmarks.add(new Landmark("Sugar Creek", 8, "The first major encampment after leaving Nauvoo. Here, Mormons organized their wagons before heading west."));
            this.landmarks.add(new Landmark("Richardson’s Point", 60, "A campsite where early migrants waited out bad weather. Several pioneers were buried here."));
            this.landmarks.add(new Landmark("Chariton River Crossing", 100, "A challenging river ford in southern Iowa, notorious for mud and flooding."));
            this.landmarks.add(new Landmark("Garden Grove", 135, "One of the first way stations built by pioneers to support later companies with food and shelter."));
            this.landmarks.add(new Landmark("Mount Pisgah", 185, "Another semi-permanent settlement where crops were planted and weary travelers rested."));
            this.landmarks.add(new Landmark("Council Bluffs (Kanesville)", 265, "A key gathering and outfitting point for Mormon pioneers, located on the Missouri River."));
            this.landmarks.add(new Landmark("Missouri River Crossing", 270, "A major logistical hurdle. Ferries or makeshift rafts were used to cross into Nebraska."));
            this.landmarks.add(new Landmark("Winter Quarters", 275, "A major temporary settlement where many Mormons stayed during the winter of 1846–47."));
            this.landmarks.add(new Landmark("Elkhorn River Crossing", 295, "Another river crossing west of Winter Quarters, often swollen and muddy."));
            this.landmarks.add(new Landmark("Loup Fork", 340, "A branch of the Platte River, near Genoa, Nebraska, followed by Mormons as they joined the main trail."));
            this.landmarks.add(new Landmark("Fort Kearny", 420, "A military outpost providing safety and a checkpoint for progress."));
            this.landmarks.add(new Landmark("Buffalo Creek", 480, "A small stream west of Fort Kearny, known for nearby buffalo and good grazing. A common stop for pioneers."));
            this.landmarks.add(new Landmark("Alkali Lakes", 540, "These shallow, salty lakes were often avoided for drinking water. They reminded emigrants of the environmental challenges of the plains."));
            this.landmarks.add(new Landmark("Windlass Hill", 615, "A steep descent into Ash Hollow. Wagons had to be locked or roped to descend safely."));
            this.landmarks.add(new Landmark("Ash Hollow", 620, "A steep descent into the North Platte Valley, it offered fresh water and grass but was difficult for wagons."));
            this.landmarks.add(new Landmark("Ancient Bluff Ruins", 635, "Natural rock formations resembling ruined buildings. Noted in journals for their striking appearance."));
            this.landmarks.add(new Landmark("Courthouse Rock and Jail Rock", 650, "These sandstone formations served as early landmarks for travelers entering western Nebraska."));
            this.landmarks.add(new Landmark("Chimney Rock", 670, "This natural formation marked progress westward and boosted morale."));
            this.landmarks.add(new Landmark("Scotts Bluff", 690, "A landmark detour that signaled a transition into more rugged terrain."));
            this.landmarks.add(new Landmark("Horse Creek", 750, "A stream near Fort Laramie and the site of the 1851 treaty between U.S. officials and Plains tribes."));
            this.landmarks.add(new Landmark("Fort Laramie", 755, "A hub for trading and resupplying along the Mormon and Oregon Trails."));
            this.landmarks.add(new Landmark("Guernsey Ruts", 785, "Wagon ruts visible in stone, left by thousands of pioneers heading west."));
            this.landmarks.add(new Landmark("Register Cliff", 790, "A site where many Mormon pioneers inscribed their names into limestone as a legacy."));
            this.landmarks.add(new Landmark("Red Buttes", 810, "Red sandstone hills that marked a nearby North Platte River ford and the final stretch before Independence Rock."));
            this.landmarks.add(new Landmark("Independence Rock", 820, "A granite monolith where pioneers hoped to arrive by July 4, seen as a major milestone in the journey."));
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
