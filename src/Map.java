import java.util.ArrayList;

/**
 * Represents the game map, landmarks, and player position.
 * CHANGES:
 * - Added resetRiverCrossing method.
 */
public class Map {
    private final int trailChoice;
    private final ArrayList<Landmark> landmarks;
    private int currentLandmarkIndex;
    private int distanceTraveled;
    private boolean riverCrossingPending; // Renamed for clarity

    public Map(int trailChoice) {
        this.trailChoice = trailChoice;
        this.landmarks = new ArrayList<>();
        this.currentLandmarkIndex = 0;
        this.distanceTraveled = 0;
        this.riverCrossingPending = false; // Initialize to false
        this.initializeLandmarks();
    }

    private void initializeLandmarks() {
        if (this.trailChoice == 1) {
            this.landmarks.add(new Landmark("Independence, Missouri", 0, 0, 0, "Independence was the primary starting point for the Oregon Trail. In the 1840s, it became a bustling outfitting and jumping-off point for westward travelers."));
            this.landmarks.add(new Landmark("Kansas River Crossing", 100, 0, 0,"A major early river crossing near present-day Topeka, Kansas, where emigrants began adapting to frontier travel."));
            this.landmarks.add(new Landmark("Big Blue River Crossing", 160, 0, 0,"Located near Marysville, Kansas, this river crossing was known for its challenging currents and required careful wagon handling."));
            this.landmarks.add(new Landmark("Fort Kearny", 300, 1076, 495, 1080, 432, "Established in 1848, Fort Kearny was the first U.S. Army post along the trail and offered supplies, protection, and rest."));
            this.landmarks.add(new Landmark("Ash Hollow", 500, 821, 465, 850, 415, "A steep descent into the North Platte Valley, it offered fresh water and grass but was difficult for wagons."));
            this.landmarks.add(new Landmark("Courthouse Rock and Jail Rock", 530, 704, 444, 580, 550, "These sandstone formations served as early landmarks for travelers entering western Nebraska."));
            this.landmarks.add(new Landmark("Chimney Rock", 550, 670, 440, 680, 383, "This towering spire became one of the most iconic landmarks on the trail, often mentioned in emigrant journals."));
            this.landmarks.add(new Landmark("Scotts Bluff", 570, 651, 424, 560, 500, "A large bluff that required a detour around Mitchell Pass. It marked a difficult but scenic part of the journey."));
            this.landmarks.add(new Landmark("Fort Laramie", 640, 514, 399, 400, 460, "Originally a fur trading post, Fort Laramie became a key military post offering protection, mail, and supplies."));
            this.landmarks.add(new Landmark("Guernsey Ruts", 670, 425, 344, 450, 306, "Deep wagon ruts carved into sandstone by thousands of wagon wheels, still visible today."));
            this.landmarks.add(new Landmark("Register Cliff", 675, 368, 335, 280, 392, "A soft limestone bluff where emigrants carved their names, leaving a record of their passing."));
            this.landmarks.add(new Landmark("Red Buttes", 695, 325, 275, 400, 258, "Red sandstone hills that marked a nearby North Platte River ford and the final stretch before Independence Rock."));
            this.landmarks.add(new Landmark("Independence Rock", 705, 295, 275, 180, 200, "Nicknamed the 'Great Register of the Desert,' emigrants scratched their names into this granite rock hoping to reach it by July 4."));
        } else if (this.trailChoice == 2) {
            this.landmarks.add(new Landmark("Independence, Missouri", 0, 0, 0, "Independence was the primary starting point for the Oregon Trail. In the 1840s, it became a bustling outfitting and jumping-off point for westward travelers."));
            this.landmarks.add(new Landmark("Kansas River Crossing", 100, 0, 0,"A major early river crossing near present-day Topeka, Kansas, where emigrants began adapting to frontier travel."));
            this.landmarks.add(new Landmark("Big Blue River Crossing", 160, 0, 0,"Located near Marysville, Kansas, this river crossing was known for its challenging currents and required careful wagon handling."));
            this.landmarks.add(new Landmark("Fort Kearny", 300, 1076, 495, 1080, 432, "Established in 1848, Fort Kearny was the first U.S. Army post along the trail and offered supplies, protection, and rest."));
            this.landmarks.add(new Landmark("Ash Hollow", 500, 821, 465, 850, 415, "A steep descent into the North Platte Valley, it offered fresh water and grass but was difficult for wagons."));
            this.landmarks.add(new Landmark("Courthouse Rock and Jail Rock", 530, 704, 444, 580, 550, "These sandstone formations served as early landmarks for travelers entering western Nebraska."));
            this.landmarks.add(new Landmark("Chimney Rock", 550, 670, 440, 680, 383, "This towering spire became one of the most iconic landmarks on the trail, often mentioned in emigrant journals."));
            this.landmarks.add(new Landmark("Scotts Bluff", 570, 651, 424, 560, 500, "A large bluff that required a detour around Mitchell Pass. It marked a difficult but scenic part of the journey."));
            this.landmarks.add(new Landmark("Fort Laramie", 640, 514, 399, 400, 460, "Originally a fur trading post, Fort Laramie became a key military post offering protection, mail, and supplies."));
            this.landmarks.add(new Landmark("Guernsey Ruts", 670, 425, 344, 450, 306, "Deep wagon ruts carved into sandstone by thousands of wagon wheels, still visible today."));
            this.landmarks.add(new Landmark("Register Cliff", 675, 368, 335, 280, 392, "A soft limestone bluff where emigrants carved their names, leaving a record of their passing."));
            this.landmarks.add(new Landmark("Red Buttes", 695, 325, 275, 400, 258, "Red sandstone hills that marked a nearby North Platte River ford and the final stretch before Independence Rock."));
            this.landmarks.add(new Landmark("Independence Rock", 705, 295, 275, 180, 200, "Nicknamed the 'Great Register of the Desert,' emigrants scratched their names into this granite rock hoping to reach it by July 4."));
        } else if (this.trailChoice == 3) {
            this.landmarks.add(new Landmark("Nauvoo, Illinois", 0, 0, 0, "Nauvoo was the home of the early Latter-day Saints community. Persecution forced their migration west in 1846."));
            this.landmarks.add(new Landmark("Chariton River Crossing", 100, 0, 0, "A challenging river ford in southern Iowa, notorious for mud and flooding."));
            this.landmarks.add(new Landmark("Garden Grove", 135, 0, 0, "One of the first way stations built by pioneers to support later companies with food and shelter."));
            this.landmarks.add(new Landmark("Mount Pisgah", 185, 0, 0, "Another semi-permanent settlement where crops were planted and weary travelers rested."));
            this.landmarks.add(new Landmark("Council Bluffs (Kanesville)", 265, 0, 0, "A key gathering and outfitting point for Mormon pioneers, located on the Missouri River."));
            this.landmarks.add(new Landmark("Loup Fork", 340, 0, 0, "A branch of the Platte River, near Genoa, Nebraska, followed by Mormons as they joined the main trail."));
            this.landmarks.add(new Landmark("Fort Kearny", 420, 1076, 495, 1080, 432, "Established in 1848, Fort Kearny was the first U.S. Army post along the trail and offered supplies, protection, and rest."));
            this.landmarks.add(new Landmark("Ash Hollow", 500, 821, 465, 850, 415, "A steep descent into the North Platte Valley, it offered fresh water and grass but was difficult for wagons."));
            this.landmarks.add(new Landmark("Courthouse Rock and Jail Rock", 530, 704, 444, 580, 550, "These sandstone formations served as early landmarks for travelers entering western Nebraska."));
            this.landmarks.add(new Landmark("Chimney Rock", 550, 670, 440, 680, 383, "This towering spire became one of the most iconic landmarks on the trail, often mentioned in emigrant journals."));
            this.landmarks.add(new Landmark("Scotts Bluff", 570, 651, 424, 560, 500, "A large bluff that required a detour around Mitchell Pass. It marked a difficult but scenic part of the journey."));
            this.landmarks.add(new Landmark("Fort Laramie", 640, 514, 399, 400, 460, "Originally a fur trading post, Fort Laramie became a key military post offering protection, mail, and supplies."));
            this.landmarks.add(new Landmark("Guernsey Ruts", 670, 425, 344, 450, 306, "Deep wagon ruts carved into sandstone by thousands of wagon wheels, still visible today."));
            this.landmarks.add(new Landmark("Register Cliff", 675, 368, 335, 280, 392, "A soft limestone bluff where emigrants carved their names, leaving a record of their passing."));
            this.landmarks.add(new Landmark("Red Buttes", 695, 325, 275, 400, 258, "Red sandstone hills that marked a nearby North Platte River ford and the final stretch before Independence Rock."));
            this.landmarks.add(new Landmark("Independence Rock", 705, 295, 275, 180, 200, "Nicknamed the 'Great Register of the Desert,' emigrants scratched their names into this granite rock hoping to reach it by July 4."));
        }
    }


    public String getTrailName() {
        if (this.trailChoice == 1) {
            return "Oregon Trail";
        } else if (this.trailChoice == 2) {
            return "California Trail";
        } else if (this.trailChoice == 3) {
            return "Mormon Trail";
        } else {
            return "Unknown Trail";
        }
    }

    public String getStartingLocation() {
        return landmarks.isEmpty() ? "Unknown" : landmarks.get(0).getName();
    }

    public String getCurrentLocation() {
        if (landmarks.isEmpty() || currentLandmarkIndex < 0 || currentLandmarkIndex >= landmarks.size()) {
            return "Lost"; // Or some other indicator of an invalid state
        }
        // Return the *last passed* landmark's name
        return landmarks.get(currentLandmarkIndex).getName();
    }

    public String getNextLandmark() {
        int nextIndex = currentLandmarkIndex + 1;
        if (nextIndex < landmarks.size()) {
            return landmarks.get(nextIndex).getName();
        } else {
            return "End of Trail";
        }
    }

    public int getDistanceToNextLandmark() {
        int nextIndex = currentLandmarkIndex + 1;
        if (nextIndex < landmarks.size()) {
            int nextDistance = landmarks.get(nextIndex).getDistance();
            return Math.max(0, nextDistance - this.distanceTraveled); // Ensure non-negative
        } else {
            return 0; // No more landmarks
        }
    }

    public String getDestination() {
        return landmarks.isEmpty() ? "Unknown" : landmarks.get(landmarks.size() - 1).getName();
    }

    public int getDistanceTraveled() {
        return distanceTraveled;
    }

    /**
     * Updates distance traveled and checks for potential river crossings.
     * @param milesTraveled The distance covered in the current step.
     */
    public void travel(int milesTraveled) {
        if (milesTraveled <= 0) return; // No travel occurred

        int previousDistance = this.distanceTraveled;
        this.distanceTraveled += milesTraveled;

        // Check if a landmark was passed during this travel step
        // This logic is now handled more in GameController's advanceDay/handleLandmarkArrival

        // Simple random chance for a river crossing event during travel
        // More complex logic could tie this to specific map segments or distances
        if (Math.random() < 0.15) { // 15% chance per travel action
            this.riverCrossingPending = true;
        }
    }

    /**
     * Checks if a river crossing event has been triggered.
     * @return true if a river crossing needs to be handled.
     */
    public boolean checkForRiverCrossing() {
        return this.riverCrossingPending;
    }

    /**
     * Resets the river crossing flag after it has been handled.
     */
    public void resetRiverCrossing() {
        this.riverCrossingPending = false;
    }


    /**
     * Checks if the distance traveled has reached or passed the next landmark.
     * @return true if the next landmark's distance threshold is met.
     */
    public boolean hasReachedLandmark() {
        int nextIndex = currentLandmarkIndex + 1;
        if (nextIndex < landmarks.size()) {
            return this.distanceTraveled >= landmarks.get(nextIndex).getDistance();
        } else {
            return false; // No more landmarks to reach
        }
    }

    /**
     * Advances the current landmark index. Should be called after confirming
     * hasReachedLandmark() is true and handling the arrival event.
     */
    public void advanceToNextLandmark() {
        if (currentLandmarkIndex < landmarks.size() - 1) {
            currentLandmarkIndex++;
            // Optional: Reset river crossing flag when reaching a landmark?
            // resetRiverCrossing();
        }
    }

    /**
     * Checks if the player has reached the final landmark.
     * @return true if the current landmark index points to the last landmark.
     */
    public boolean hasReachedDestination() {
        // Check if distance traveled meets or exceeds the final landmark's distance
        if (!landmarks.isEmpty()) {
            int finalDistance = landmarks.get(landmarks.size() - 1).getDistance();
            return this.distanceTraveled >= finalDistance;
        }
        return false;
        // Alternative check: return this.currentLandmarkIndex == this.landmarks.size() - 1;
        // This depends on whether advanceToNextLandmark is called exactly when reaching the destination distance.
        // Checking distance is usually safer.
    }


    /**
     * Displays historical info for the current landmark (to console - GUI handles display)
     */
    public void displayHistoricalInfo() {
        if (landmarks.isEmpty() || currentLandmarkIndex < 0 || currentLandmarkIndex >= landmarks.size()) {
            System.out.println("No historical information available for current location.");
            return;
        }
        System.out.println("\n=== " + getCurrentLocation() + " ===");
        System.out.println(landmarks.get(currentLandmarkIndex).getDescription());
    }

    public ArrayList<Landmark> getLandmarks() {
        return this.landmarks;
    }

    public int getCurrentLandmarkIndex() {
        return this.currentLandmarkIndex;
    }

    // Method removed as it's unused and could be confusing
    // public Landmark getCurrentLandmark(int location) { ... }

    /**
     * Sets the current location by landmark name. Primarily used for initialization or debugging.
     * Also updates distance traveled to match the landmark's distance.
     * @param landmarkName The name of the landmark to set as current
     * @return true if landmark found and set, false otherwise
     */
    public boolean setCurrentLocation(String landmarkName) {
        for (int i = 0; i < landmarks.size(); i++) {
            if (landmarks.get(i).getName().equals(landmarkName)) {
                currentLandmarkIndex = i;
                // Set distance traveled to *exactly* the landmark's distance when setting location this way
                distanceTraveled = landmarks.get(i).getDistance();
                System.out.println("Map: Set current location to " + landmarkName + " at index " + i + ", distance " + distanceTraveled);
                return true;
            }
        }
        System.err.println("Map: Landmark not found: " + landmarkName);
        return false;
    }
}
