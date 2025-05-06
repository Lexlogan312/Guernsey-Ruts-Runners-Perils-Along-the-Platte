/**
 * Map Class of the Perils Along the Platte Game
 * Manages the game's trail system, landmarks, and player position.
 * Handles trail selection, landmark tracking, river crossings, and distance calculations.
 * Provides historical accuracy for three major trails: Oregon, California, and Mormon.
 *
 * Features:
 * - Three historically accurate trail routes
 * - Dynamic landmark tracking and progression
 * - River crossing simulation
 * - Distance-based event triggers
 * - Historical landmark descriptions
 * - Trail-specific starting points
 * - Progress tracking and validation
 *
 * The Map class serves as the game's geographical engine, managing:
 * - Trail selection and initialization
 * - Landmark placement and progression
 * - River crossing events
 * - Distance calculations and travel
 * - Historical accuracy and context
 *
 * @author Alex Randall and Chase McCluskey
 * @version 1.0
 * @date 05/06/2025
 * @file Map.java
 */

import java.util.ArrayList;

public class Map {
    // The selected trail choice (1=Oregon, 2=California, 3=Mormon)
    private final int trailChoice;

    // List of landmarks along the selected trail
    private final ArrayList<Landmark> landmarks;

    // Current position in the landmark list
    private int currentLandmarkIndex;

    // Total distance traveled on the trail
    private int distanceTraveled;

    // Flag indicating if a river crossing is pending
    private boolean riverCrossingPending;

    /**
     * Inner class representing a river crossing point with historical information.
     * Each crossing includes:
     * - Distance from trail start
     * - Historical name
     * - Description of challenges
     * - Historical context
     */
    private class RiverCrossing {
        int distance;
        String name;
        String description;
        
        /**
         * Constructs a new RiverCrossing with historical details.
         * @param distance The distance from the start of the trail
         * @param name The name of the river crossing
         * @param description Historical description of the crossing
         */
        RiverCrossing(int distance, String name, String description) {
            this.distance = distance;
            this.name = name;
            this.description = description;
        }
    }
    
    // List of pending river crossings on the trail
    private final ArrayList<RiverCrossing> pendingRiverCrossings = new ArrayList<>();

    // The current active river crossing
    private RiverCrossing currentRiverCrossing;

    /**
     * Constructs a new Map for the specified trail.
     * Initializes landmarks and river crossings based on the trail choice.
     * Sets up the initial game state for travel and progression.
     * 
     * @param trailChoice The selected trail (1 = Oregon, 2 = California, 3 = Mormon)
     */
    public Map(int trailChoice) {
        this.trailChoice = trailChoice;
        this.landmarks = new ArrayList<>();
        this.currentLandmarkIndex = 0;
        this.distanceTraveled = 0;
        this.riverCrossingPending = false;
        this.initializeLandmarks();
        this.initializeRiverCrossings();
    }

    /**
     * Initializes the landmarks for the selected trail.
     * Sets up historical landmarks with their distances and descriptions.
     * Each landmark includes:
     * - Historical name and location
     * - Distance from trail start
     * - Map coordinates
     * - Historical description
     * 
     * Landmarks are placed in chronological order of the journey.
     */
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
            this.landmarks.add(new Landmark("Ash Hollow", 620, 821, 465, 850, 415, "A steep descent into the North Platte Valley, it offered fresh water and grass but was difficult for wagons."));
            this.landmarks.add(new Landmark("Courthouse Rock and Jail Rock", 530, 704, 444, 580, 550, "These sandstone formations served as early landmarks for travelers entering western Nebraska."));
            this.landmarks.add(new Landmark("Chimney Rock", 670, 670, 440, 680, 383, "This towering spire became one of the most iconic landmarks on the trail, often mentioned in emigrant journals."));
            this.landmarks.add(new Landmark("Scotts Bluff", 690, 651, 424, 560, 500, "A large bluff that required a detour around Mitchell Pass. It marked a difficult but scenic part of the journey."));
            this.landmarks.add(new Landmark("Fort Laramie", 760, 514, 399, 400, 460, "Originally a fur trading post, Fort Laramie became a key military post offering protection, mail, and supplies."));
            this.landmarks.add(new Landmark("Guernsey Ruts", 790, 425, 344, 450, 306, "Deep wagon ruts carved into sandstone by thousands of wagon wheels, still visible today."));
            this.landmarks.add(new Landmark("Register Cliff", 795, 368, 335, 280, 392, "A soft limestone bluff where emigrants carved their names, leaving a record of their passing."));
            this.landmarks.add(new Landmark("Red Buttes", 815, 325, 275, 400, 258, "Red sandstone hills that marked a nearby North Platte River ford and the final stretch before Independence Rock."));
            this.landmarks.add(new Landmark("Independence Rock", 825, 295, 275, 180, 200, "Nicknamed the 'Great Register of the Desert,' emigrants scratched their names into this granite rock hoping to reach it by July 4."));
        }
    }

    /**
     * Initializes the specific river crossing points based on trail choice.
     * Sets up historically accurate river crossings with:
     * - Precise distances from trail start
     * - Historical names and descriptions
     * - Challenge levels and context
     * 
     * River crossings are placed between major landmarks
     * based on historical records and emigrant journals.
     */
    private void initializeRiverCrossings() {
        pendingRiverCrossings.clear();
        
        // Find Fort Laramie landmark to set river crossings after it
        int fortLaramieDistance = 0;
        int independenceRockDistance = 0;
        
        for (Landmark landmark : landmarks) {
            if (landmark.getName().contains("Fort Laramie")) {
                fortLaramieDistance = landmark.getDistance();
            }
            if (landmark.getName().contains("Independence Rock")) {
                independenceRockDistance = landmark.getDistance();
            }
        }
        
        if (fortLaramieDistance > 0 && independenceRockDistance > 0) {
            // Calculate distance between Fort Laramie and Independence Rock
            int segmentDistance = independenceRockDistance - fortLaramieDistance;
            
            // Add historically accurate river crossings between Fort Laramie and Independence Rock
            pendingRiverCrossings.add(new RiverCrossing(
                fortLaramieDistance + (segmentDistance / 4),
                "North Platte River Crossing",
                "The North Platte River was a major obstacle on the trail. At this wider, shallower section, wagons had to be carefully guided across."
            ));
            
            pendingRiverCrossings.add(new RiverCrossing(
                fortLaramieDistance + (segmentDistance / 2),
                "Sweetwater River Crossing",
                "The Sweetwater River had to be crossed multiple times along the trail. Its swift currents could be dangerous during high water."
            ));
            
            pendingRiverCrossings.add(new RiverCrossing(
                fortLaramieDistance + (3 * segmentDistance / 4),
                "Deer Creek Crossing",
                "Deer Creek was a critical water source and crossing point. Many emigrants stopped here to rest before the final push to Independence Rock."
            ));
        }
    }

    /**
     * Gets the name of the selected trail.
     * Returns the full historical name of the trail
     * based on the trail choice.
     * 
     * @return The name of the trail (Oregon Trail, California Trail, or Mormon Trail)
     */
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

    /**
     * Gets the starting location of the trail.
     * Returns the historical starting point based on trail choice:
     * - Oregon/California: Independence, Missouri
     * - Mormon: Nauvoo, Illinois
     * 
     * @return The name of the starting landmark
     */
    public String getStartingLocation() {
        return landmarks.isEmpty() ? "Unknown" : landmarks.get(0).getName();
    }

    /**
     * Gets the current location of the player.
     * Returns the name of the last passed landmark
     * or "Lost" if in an invalid state.
     * 
     * @return The name of the last passed landmark
     */
    public String getCurrentLocation() {
        if (landmarks.isEmpty() || currentLandmarkIndex < 0 || currentLandmarkIndex >= landmarks.size()) {
            return "Lost"; // Or some other indicator of an invalid state
        }
        // Return the *last passed* landmark's name
        return landmarks.get(currentLandmarkIndex).getName();
    }

    /**
     * Gets the next landmark ahead on the trail.
     * Returns the name of the upcoming landmark
     * or "End of Trail" if at the final destination.
     * 
     * @return The name of the next landmark, or "End of Trail" if at the end
     */
    public String getNextLandmark() {
        int nextIndex = currentLandmarkIndex + 1;
        if (nextIndex < landmarks.size()) {
            return landmarks.get(nextIndex).getName();
        } else {
            return "End of Trail";
        }
    }

    /**
     * Gets the final destination of the trail.
     * Returns the historical endpoint based on trail choice:
     * - Oregon: Oregon City
     * - California: Sacramento
     * - Mormon: Salt Lake City
     * 
     * @return The name of the destination landmark
     */
    public String getDestination() {
        return landmarks.isEmpty() ? "Unknown" : landmarks.get(landmarks.size() - 1).getName();
    }

    /**
     * Gets the total distance traveled on the trail.
     * Returns the cumulative distance covered since
     * starting the journey.
     * 
     * @return The distance traveled in miles
     */
    public int getDistanceTraveled() {
        return distanceTraveled;
    }

    /**
     * Updates the distance traveled and checks for river crossings.
     * Handles:
     * - Distance accumulation
     * - River crossing detection
     * - Progress validation
     * 
     * @param milesTraveled The distance covered in the current step
     */
    public void travel(int milesTraveled) {
        if (milesTraveled <= 0) return; // No travel occurred

        int previousDistance = this.distanceTraveled;
        this.distanceTraveled += milesTraveled;

        // Check for river crossings at specific distances
        checkForSpecificRiverCrossing(previousDistance, this.distanceTraveled);
    }
    
    /**
     * Checks if the player has crossed a designated river crossing point.
     * Compares previous and current distances to determine
     * if a river crossing has been reached.
     * 
     * @param previousDistance Distance before travel
     * @param currentDistance Distance after travel
     */
    private void checkForSpecificRiverCrossing(int previousDistance, int currentDistance) {
        // If Fort Laramie hasn't been reached yet, don't trigger river crossings
        boolean passedFortLaramie = false;
        for (int i = 0; i <= currentLandmarkIndex; i++) {
            if (landmarks.get(i).getName().contains("Fort Laramie")) {
                passedFortLaramie = true;
                break;
            }
        }
        
        if (!passedFortLaramie) {
            return; // Don't trigger river crossings before Fort Laramie
        }
        
        // Check if we've passed any pending river crossing points
        for (int i = 0; i < pendingRiverCrossings.size(); i++) {
            RiverCrossing crossing = pendingRiverCrossings.get(i);
            
            // If we've crossed this river point during this travel session
            if (previousDistance < crossing.distance && currentDistance >= crossing.distance) {
                riverCrossingPending = true;
                currentRiverCrossing = crossing;
                pendingRiverCrossings.remove(i); // Remove this crossing point
                break; // Only trigger one crossing at a time
            }
        }
    }

    /**
     * Checks if a river crossing event has been triggered.
     * @return true if a river crossing needs to be handled
     */
    public boolean checkForRiverCrossing() {
        return this.riverCrossingPending;
    }
    
    /**
     * Gets the name of the current river crossing.
     * @return The name of the current river crossing, or "River Crossing" if none
     */
    public String getCurrentRiverCrossingName() {
        return currentRiverCrossing != null ? currentRiverCrossing.name : "River Crossing";
    }
    
    /**
     * Gets the description of the current river crossing.
     * @return The historical description of the current river crossing
     */
    public String getCurrentRiverCrossingDescription() {
        return currentRiverCrossing != null ? currentRiverCrossing.description : "";
    }

    /**
     * Resets the river crossing flag after it has been handled.
     */
    public void resetRiverCrossing() {
        this.riverCrossingPending = false;
        this.currentRiverCrossing = null;
    }

    /**
     * Checks if the player has reached a landmark.
     * @return true if the player has reached a landmark
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
     * Advances to the next landmark on the trail.
     */
    public void advanceToNextLandmark() {
        if (currentLandmarkIndex < landmarks.size() - 1) {
            currentLandmarkIndex++;
            // Optional: Reset river crossing flag when reaching a landmark?
            // resetRiverCrossing();
        }
    }

    /**
     * Checks if the player has reached the final destination.
     * @return true if the player has reached the destination
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
     * Gets the list of all landmarks on the trail.
     * @return ArrayList of Landmark objects
     */
    public ArrayList<Landmark> getLandmarks() {
        return this.landmarks;
    }

    /**
     * Gets the index of the current landmark.
     * @return The index of the current landmark
     */
    public int getCurrentLandmarkIndex() {
        return this.currentLandmarkIndex;
    }

    // Method removed as it's unused and could be confusing
    // public Landmark getCurrentLandmark(int location) { ... }

    /**
     * Sets the current location to a specific landmark.
     * @param landmarkName The name of the landmark to set as current location
     */
    public void setCurrentLocation(String landmarkName) {
        for (int i = 0; i < landmarks.size(); i++) {
            if (landmarks.get(i).getName().equals(landmarkName)) {
                currentLandmarkIndex = i;
                // Set distance traveled to *exactly* the landmark's distance when setting location this way
                distanceTraveled = landmarks.get(i).getDistance();
                System.out.println("Map: Set current location to " + landmarkName + " at index " + i + ", distance " + distanceTraveled);
                return;
            }
        }
        System.err.println("Map: Landmark not found: " + landmarkName);
    }
}
