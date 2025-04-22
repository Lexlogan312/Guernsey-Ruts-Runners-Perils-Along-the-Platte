import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.awt.Frame;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * GameController acts as an intermediary between the GUI and the game logic
 */
public class GameController {
    private Player player;
    private Map map;
    private Inventory inventory;
    private Time time;
    private Weather weather;
    private Hunting hunting;
    private Perils perils;
    
    // Trail setup
    private String trail;
    private String departureLocation;
    private String departureMonth;
    private int monthChoice = 0;
    
    // Game state
    private boolean gameStarted = false;
    private boolean isGameRunning = true;
    
    // Event tracking
    private ArrayList<String> journeyEvents = new ArrayList<>();
    
    // Listeners
    private ArrayList<Consumer<String>> messageListeners = new ArrayList<>();
    private ArrayList<Runnable> gameStateListeners = new ArrayList<>();
    
    public GameController() {
        player = new Player("Player", "Male");
        inventory = new Inventory();
        time = new Time(1848, 3); // Default to March 1848
        weather = new Weather(time.getMonth(), "Start");
        map = new Map(1); // Default to Oregon Trail
        hunting = new Hunting(inventory);
        perils = new Perils(player, inventory, weather);
        
        // Set the message listener for Perils class to use our notification system
        perils.setMessageListener(this::notifyListeners);
    }
    
    /**
     * Starts a new game - similar to StartGame constructor 
     */
    public void startNewGame() {
        gameStarted = true;
        isGameRunning = true;
        
        // These will be set through GUI dialogs later
        playerSetup("John", "male", new String[]{"Mary", "Sarah", "Tom"});
        
        // Initialize game objects
        inventory = new Inventory();
        hunting = new Hunting(inventory);
        
        notifyListeners("Welcome to Perils Along the Platte!\n\n" +
                "In the mid-19th century, thousands of Americans embarked on dangerous journeys westward seeking " +
                "better lives, religious freedom, and opportunity. These brave pioneers faced countless hardships as " +
                "they traveled along routes like the Oregon Trail, California Trail, and Mormon Trail.\n\n" +
                "You will lead your family on this perilous journey, making decisions that will determine your fate.");
    }
    
    /**
     * Adds a message listener to receive game messages
     */
    public void addMessageListener(Consumer<String> listener) {
        messageListeners.add(listener);
    }
    
    /**
     * Adds a listener to be notified when game state changes
     */
    public void addGameStateListener(Runnable listener) {
        gameStateListeners.add(listener);
    }
    
    /**
     * Sends a message to all listeners
     */
    private void notifyListeners(String message) {
        for (Consumer<String> listener : messageListeners) {
            listener.accept(message);
        }
    }
    
    /**
     * Notifies all game state listeners
     */
    private void notifyGameStateChanged() {
        for (Runnable listener : gameStateListeners) {
            listener.run();
        }
    }
    
    /**
     * Sets up player with name, gender and family members
     */
    public void playerSetup(String name, String gender, String[] familyMembers) {
        player = new Player(name, gender);
        player.setFamilyMembers(familyMembers);
    }
    
    /**
     * Sets the trail choice
     */
    public void selectTrail(int trailChoice) {
        switch (trailChoice) {
            case 1:
                trail = "Oregon";
                departureLocation = "Independence, Missouri";
                break;
            case 2:
                trail = "California";
                departureLocation = "Independence, Missouri";
                break;
            case 3:
                trail = "Mormon";
                departureLocation = "Nauvoo, Illinois";
                break;
        }
        
        map = new Map(trailChoice);
        
        notifyListeners("You have chosen to travel along the " + trail + " Trail.\n" +
                        "Your journey will begin in " + departureLocation + ".");
    }
    
    /**
     * Sets the departure month
     */
    public void selectDepartureMonth(int month) {
        String[] months = {"March", "April", "May", "June", "July"};
        
        if (month >= 1 && month <= 5) {
            monthChoice = month - 1;
            departureMonth = months[monthChoice];
            
            // Initialize time based on departure month
            int monthNumber = monthChoice + 3; // March (3) to July (7)
            time = new Time(1848, monthNumber);
            
            // Initialize weather with starting month and location
            weather = new Weather(monthNumber, departureLocation);
            
            // Initialize perils
            perils = new Perils(player, inventory, weather);
            
            notifyListeners("You will depart in " + departureMonth + " 1848.");
        }
    }
    
    /**
     * Performs trading at the market
     */
    public void visitMarket() {
        SwingUtilities.invokeLater(() -> {
            // Find the owner frame to make the dialog properly modal
            Frame owner = null;
            for (Frame frame : Frame.getFrames()) {
                if (frame.isVisible()) {
                    owner = frame;
                    break;
                }
            }
            
            JDialog marketDialog = new JDialog(owner, "General Store", true);
            marketDialog.setSize(800, 600);
            marketDialog.setLocationRelativeTo(owner);
            
            // Create market panel with the player's current money
            Market market = new Market(player, inventory);
            JPanel marketPanel = market.createMarketPanel();
            
            // Add the market panel to the dialog
            marketDialog.add(marketPanel);
            marketDialog.setVisible(true);
            
            // Notify listeners about purchases
            notifyListeners("You've stocked up on supplies for your journey.");
            notifyGameStateChanged();
        });
    }
    
    /**
     * Simulates the journey to Fort Kearny
     */
    public void journeyToFortKearny() {
        // Only proceed if we have a map and time initialized
        if (map == null || time == null) {
            return;
        }
        
        // Clear previous journey events
        journeyEvents.clear();
        
        int fortKearnyDistance = 0;
        Landmark fortKearnyLandmark = null;
        List<Landmark> landmarksPassed = new ArrayList<>();
        
        // Find Fort Kearny's distance and collect landmarks along the way
        for (int i = 0; i < map.getLandmarks().size(); i++) {
            Landmark landmark = map.getLandmarks().get(i);
            if (landmark.getName().contains("Fort Kearny")) {
                fortKearnyDistance = landmark.getDistance();
                fortKearnyLandmark = landmark;
                landmarksPassed.add(landmark);
                break;
            } else if (landmark.getDistance() < fortKearnyDistance) {
                landmarksPassed.add(landmark);
            }
        }
        
        if (fortKearnyDistance == 0) {
            // Couldn't find Fort Kearny - shouldn't happen, but just in case
            return;
        }

        int averageDailyDistance = 15;
        int daysToFortKearny = fortKearnyDistance / averageDailyDistance;

        // Adjust based on departure month
        if (departureMonth.equals("March")) {
            daysToFortKearny += 3; // Muddy conditions in March
            journeyEvents.add("Muddy spring conditions slowed your travel by 3 days.");
        } else if (departureMonth.equals("July")) {
            daysToFortKearny -= 1; // Better roads in summer
            journeyEvents.add("The dry summer roads allowed you to travel faster, saving a day.");
        }
        
        notifyListeners("\n=== JOURNEY TO FORT KEARNY ===\n" +
                       "Your journey to Fort Kearny takes " + daysToFortKearny + " days.");
        
        // Add landmarks passed to journey events
        if (!landmarksPassed.isEmpty()) {
            StringBuilder landmarksPassedText = new StringBuilder("Notable landmarks you passed: ");
            for (int i = 0; i < landmarksPassed.size(); i++) {
                Landmark lm = landmarksPassed.get(i);
                landmarksPassedText.append(lm.getName());
                if (i < landmarksPassed.size() - 1) {
                    landmarksPassedText.append(", ");
                }
                
                // Also add individual landmark entries with their descriptions
                if (lm.getDescription() != null && !lm.getDescription().isEmpty()) {
                    journeyEvents.add("LANDMARK: " + lm.getName() + " - " + lm.getDescription());
                }
            }
            journeyEvents.add(landmarksPassedText.toString());
        }
        
        // Simulate the journey to Fort Kearny
        for (int i = 0; i < daysToFortKearny; i++) {
            // Advance time
            time.advanceDay();
            
            // Consume food
            int dailyFoodConsumption = player.getFamilySize() * 2;
            
            // Check if enough food is available
            if (inventory.getFood() >= dailyFoodConsumption) {
                inventory.consumeFood(dailyFoodConsumption);
            } else {
                // Consume whatever food is left
                int remainingFood = inventory.getFood();
                inventory.consumeFood(remainingFood);
                
                // Health impact based on food shortage
                int healthLoss = 5;
                player.decreaseHealth(healthLoss);
                journeyEvents.add("You ran out of food for " + (daysToFortKearny - i) + " days, causing everyone to lose " + healthLoss + " health points.");
                
                // Only record this once
                break;
            }
            
            // Check for random events (1/3 chance)
            if (Math.random() < 0.33) {
                // Create a custom listener to capture events for the journey summary
                Consumer<String> eventCaptureListener = eventText -> {
                    // Clean up the event text a bit
                    if (eventText.startsWith("\n===")) {
                        // This is a header, start a new entry
                        String cleanedText = eventText.replace("\n=== ", "");
                        cleanedText = cleanedText.replace(" ===", ":");
                        journeyEvents.add(cleanedText);
                    } else if (!eventText.isEmpty()) {
                        // This is content, append to the last entry
                        int lastIndex = journeyEvents.size() - 1;
                        if (lastIndex >= 0) {
                            String currentEvent = journeyEvents.get(lastIndex);
                            journeyEvents.set(lastIndex, currentEvent + " " + eventText);
                        }
                    }
                };
                
                // Temporarily redirect events to our capture listener
                Consumer<String> originalListener = message -> notifyListeners(message);
                perils.setMessageListener(eventCaptureListener);
                
                // Generate the event
                perils.generateRandomEvent();
                
                // Restore the original listener
                perils.setMessageListener(originalListener);
            }
            
            // Check if player died
            if (player.isDead()) {
                isGameRunning = false;
                String deathMessage = "You died of " + player.getCauseOfDeath() + " after " + time.getTotalDays() + " days on the trail.";
                journeyEvents.add(deathMessage);
                notifyListeners("\n" + deathMessage);
                return;
            }
        }
        
        // Update the map position
        map.travel(fortKearnyDistance);
        
        // Explicitly set current location to Fort Kearny
        if (fortKearnyLandmark != null) {
            map.setCurrentLocation(fortKearnyLandmark.getName());
        }
        
        notifyListeners("\nYou have arrived at Fort Kearny!\n" +
                       "This is where the real challenges of your journey begin.");
                       
        // Notify game state listeners to update display
        notifyGameStateChanged();
    }
    
    /**
     * Get journey events that occurred
     */
    public ArrayList<String> getJourneyEvents() {
        return journeyEvents;
    }
    
    // Game actions
    
    /**
     * Travel action
     */
    public void travel() {
        if (!isGameRunning) return;
        
        // Base travel distance
        int baseDistance = 15;
        
        // Modify distance based on weather
        int adjustedDistance = weather.adjustTravelDistance(baseDistance);
        
        // Modify based on oxen health
        adjustedDistance = (int)(adjustedDistance * (inventory.getOxenHealth() / 100.0));
        
        // Make sure at least 1 mile is traveled
        if (adjustedDistance < 1) {
            adjustedDistance = 1;
        }
        
        // Update map with travel distance
        map.travel(adjustedDistance);
        
        // Calculate desired food consumption
        int desiredFoodConsumption = player.getFamilySize() * 2;
        
        // Check if enough food is available
        int actualFoodConsumed = 0;
        if (inventory.getFood() >= desiredFoodConsumption) {
            actualFoodConsumed = desiredFoodConsumption;
            inventory.consumeFood(actualFoodConsumed);
        } else {
            // Consume whatever food is left
            actualFoodConsumed = inventory.getFood();
            inventory.consumeFood(actualFoodConsumed);
            
            // Health impact based on food
            player.decreaseHealth(5);
            notifyListeners("You have no food! Your health is declining.");
        }
        
        // Message about travel
        notifyListeners("You traveled " + adjustedDistance + " miles today.\n" +
                        "Food consumed: " + actualFoodConsumed + " pounds.");
        
        // Small chance of minor injury during travel
        if (Math.random() < 0.1) {
            player.decreaseHealth(2);
            notifyListeners("The rough trail caused some minor injuries and fatigue.");
        }
        
        // Small chance of oxen health decline
        if (Math.random() < 0.15) {
            inventory.decreaseOxenHealth(3);
            notifyListeners("Your oxen are showing signs of fatigue.");
        }
        
        // Advance time and check for events
        advanceDay();
    }
    
    /**
     * Rest action
     */
    public void rest() {
        if (!isGameRunning) return;
        
        notifyListeners("You decide to rest for the day.");
        
        // Recover health
        int healthRecovered = 5 + (int)(Math.random() * 10); // 5-15 health points
        player.increaseHealth(healthRecovered);
        notifyListeners("Health improved by " + healthRecovered + " points.");
        
        // Recover oxen health
        int oxenHealthRecovered = 5 + (int)(Math.random() * 10); // 5-15 points
        inventory.increaseOxenHealth(oxenHealthRecovered);
        notifyListeners("Oxen health improved by " + oxenHealthRecovered + " points.");
        
        // Calculate desired food consumption
        int desiredFoodConsumption = player.getFamilySize() * 2;
        
        // Check if enough food is available
        int actualFoodConsumed = 0;
        if (inventory.getFood() >= desiredFoodConsumption) {
            actualFoodConsumed = desiredFoodConsumption;
            inventory.consumeFood(actualFoodConsumed);
        } else {
            // Consume whatever food is left
            actualFoodConsumed = inventory.getFood();
            inventory.consumeFood(actualFoodConsumed);
            
            // Health impact based on food
            player.decreaseHealth(3);
            notifyListeners("You have no food! Your health is declining despite the rest.");
        }
        
        notifyListeners("Food consumed: " + actualFoodConsumed + " pounds.");
        
        // Small chance of finding food while resting
        if (Math.random() < 0.2) {
            int foodFound = 2 + (int)(Math.random() * 8); // 2-10 pounds
            inventory.addFood(foodFound);
            notifyListeners("While resting, your family found " + foodFound + " pounds of edible plants nearby.");
        }
        
        // Advance time and check for events
        advanceDay();
    }
    
    /**
     * Hunt action
     */
    public void hunt() {
        if (!isGameRunning) return;
        
        // Check if player has ammunition
        if (inventory.getAmmunition() <= 0) {
            notifyListeners("You don't have any ammunition for hunting!");
            return;
        }
        
        notifyListeners("You set out to hunt for food.");
        
        // Use some ammunition
        int ammoUsed = 1 + (int)(Math.random() * 3); // 1-3 rounds used
        inventory.useAmmunition(ammoUsed);
        
        // Calculate success - simplified from original hunting minigame
        boolean success = Math.random() < 0.7;
        
        if (success) {
            // Determine what animal was hunted based on random chance
            double animalChance = Math.random();
            String animal;
            int foodGained;
            
            if (animalChance < 0.1) {
                animal = "bison";
                foodGained = 300 + (int)(Math.random() * 200); // 300-500 pounds
            } else if (animalChance < 0.3) {
                animal = "deer";
                foodGained = 100 + (int)(Math.random() * 100); // 100-200 pounds
            } else if (animalChance < 0.6) {
                animal = "rabbit";
                foodGained = 5 + (int)(Math.random() * 10); // 5-15 pounds
            } else {
                animal = "squirrel";
                foodGained = 2 + (int)(Math.random() * 3); // 2-5 pounds
            }
            
            notifyListeners("Great shot! You got a " + animal + "!\n" +
                            "You gained " + foodGained + " pounds of food.\n" +
                            "You used " + ammoUsed + " rounds of ammunition.");
            
            // Add food to inventory
            inventory.addFood(foodGained);
        } else {
            notifyListeners("You missed your shot! The animal got away.\n" +
                            "You used " + ammoUsed + " rounds of ammunition.");
        }
        
        // Advance time and check for events
        advanceDay();
    }
    
    /**
     * Private method to advance day and handle events
     */
    private void advanceDay() {
        // Advance time
        time.advanceDay();
        
        // Update weather based on new day and location
        weather = new Weather(time.getMonth(), map.getCurrentLocation());
        
        // Check if a landmark is reached
        if (map.hasReachedLandmark()) {
            handleLandmarkArrival();
        }
        
        // Random events
        if (Math.random() < 0.2) { // 20% chance each day
            perils.generateRandomEvent();
        }
        
        // Check for river crossing
        if (map.checkForRiverCrossing()) {
            handleRiverCrossing();
        }
        
        // Check if game should end
        if (player.isDead()) {
            isGameRunning = false;
            final String deathMessage = "\nYou have died of " + player.getCauseOfDeath() + ".\n" +
                           "Your journey has come to an end after " + time.getTotalDays() + " days on the trail.\n" +
                           "You traveled " + map.getDistanceTraveled() + " miles.\n" +
                           "Your last known location: near " + map.getCurrentLocation();
            
            notifyListeners(deathMessage);
            
            // Show death dialog
            SwingUtilities.invokeLater(() -> {
                // Find the owner frame
                Frame owner = null;
                for (Frame frame : Frame.getFrames()) {
                    if (frame.isVisible()) {
                        owner = frame;
                        break;
                    }
                }
                
                DeathDialog deathDialog = new DeathDialog(
                    owner,
                    player.getCauseOfDeath(),
                    time.getTotalDays(),
                    map.getDistanceTraveled(),
                    map.getCurrentLocation()
                );
                
                deathDialog.setVisible(true);
            });
        }
        
        if (map.hasReachedDestination()) {
            isGameRunning = false;
            final String completionMessage = "\nCONGRATULATIONS!\n" +
                           "You have successfully completed your journey to " + map.getDestination() + "!\n\n" +
                           "Your journey took " + time.getTotalDays() + " days.\n" +
                           "You traveled " + map.getDistanceTraveled() + " miles.\n" +
                           "Final date: " + time.getMonthName() + " " + time.getDay() + ", " + time.getYear();
            
            notifyListeners(completionMessage);
            
            // Show completion dialog
            SwingUtilities.invokeLater(() -> {
                // Find the owner frame
                Frame owner = null;
                for (Frame frame : Frame.getFrames()) {
                    if (frame.isVisible()) {
                        owner = frame;
                        break;
                    }
                }
                
                CompletionDialog completionDialog = new CompletionDialog(
                    owner,
                    map.getDestination(),
                    time.getTotalDays(),
                    map.getDistanceTraveled(),
                    time.getMonthName() + " " + time.getDay() + ", " + time.getYear()
                );
                
                completionDialog.setVisible(true);
            });
        }
        
        // Notify listeners that game state has changed
        notifyGameStateChanged();
    }
    
    /**
     * Handle landmark arrival
     */
    private void handleLandmarkArrival() {
        map.advanceToNextLandmark();
        String landmark = map.getCurrentLocation();
        
        notifyListeners("\n=== ARRIVED AT " + landmark.toUpperCase() + " ===\n" +
                       map.getLandmarks().get(map.getCurrentLandmarkIndex()).getDescription());
        
        // Check if this is a trading post
        if (landmark.contains("Fort") || landmark.contains("Trading Post")) {
            // Show trading dialog
            SwingUtilities.invokeLater(() -> {
                // Find the owner frame
                Frame owner = null;
                for (Frame frame : Frame.getFrames()) {
                    if (frame.isVisible()) {
                        owner = frame;
                        break;
                    }
                }
                
                TradingDialog tradingDialog = new TradingDialog(owner, player, inventory);
                tradingDialog.setVisible(true);
                
                // Results will be reflected in player money and inventory automatically
                // Notify the game of state changes after dialog closes
                notifyGameStateChanged();
            });
        }
    }
    
    /**
     * Handle river crossing
     */
    private void handleRiverCrossing() {
        notifyListeners("\n=== RIVER CROSSING ===\n" +
                       "You've come to a river that needs to be crossed.");
        
        // Show river crossing dialog
        SwingUtilities.invokeLater(() -> {
            // Find the owner frame
            Frame owner = null;
            for (Frame frame : Frame.getFrames()) {
                if (frame.isVisible()) {
                    owner = frame;
                    break;
                }
            }
            
            RiverCrossingDialog riverDialog = new RiverCrossingDialog(owner, player, inventory, weather);
            riverDialog.setVisible(true);
            
            // Results will be reflected in player health and inventory automatically
            // Notify the game of state changes after dialog closes
            notifyGameStateChanged();
        });
    }
    
    // Getters for game state
    
    public Player getPlayer() {
        return player;
    }
    
    public Map getMap() {
        return map;
    }
    
    public Inventory getInventory() {
        return inventory;
    }
    
    public Time getTime() {
        return time;
    }
    
    public Weather getWeather() {
        return weather;
    }
    
    public boolean isGameStarted() {
        return gameStarted;
    }
    
    public boolean isGameRunning() {
        return isGameRunning;
    }
    
    /**
     * Get the currently chosen trail
     */
    public String getTrail() {
        return trail;
    }
    
    /**
     * Trigger a game state update
     */
    public void updateGameState() {
        notifyGameStateChanged();
    }
} 