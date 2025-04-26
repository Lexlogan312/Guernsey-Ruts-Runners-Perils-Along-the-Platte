import java.util.Scanner;

public class StartGame {
    private Player player;
    private Map map;
    private final Inventory inventory;
    private final Hunting hunting;
    private final Time time;
    private Weather weather;
    private boolean isGameRunning = true;
    private final Scanner scanner = new Scanner(System.in);
    private final String[] months = {"March", "April", "May", "June", "July"};
    private int monthChoice = 0;
    
    private int daysTraveled;
    private int distanceTraveled;
    private String trail;
    private String departureLocation;
    private String departureMonth;
    private String playerName;
    private String playerGender;
    private final Perils perils;
    
    public StartGame() {
        displayWelcomeMessage();
        setupPlayer();
        setupFamilyMembers();
        selectTrail();
        selectDepartureMonth();
        prepareForDeparture();
        
        // Initialize game objects
        inventory = new Inventory();
        hunting = new Hunting(inventory);
        
        // Initialize time based on departure month
        int monthNumber = monthChoice + 2; // March (3) to July (7)
        time = new Time(1848, monthNumber);
        
        // Initialize weather with starting month and location
        weather = new Weather(monthNumber, departureLocation);
        
        // Initialize perils
        perils = new Perils(player, inventory, weather, time);
        
        visitMarket();
        journeyToFortKearny();
        mainGameLoop();
        displayGameSummary();
    }

    public void displayWelcomeMessage() {
        System.out.println("In the mid-19th century, thousands of Americans");
        System.out.println("embarked on dangerous journeys westward seeking");
        System.out.println("better lives, religious freedom, and opportunity.");
        System.out.println("These brave pioneers faced countless hardships as");
        System.out.println("they traveled along routes like the Oregon Trail,");
        System.out.println("California Trail, and Mormon Trail.");
        System.out.println();
        System.out.println("You will lead your family on this perilous journey,");
        System.out.println("making decisions that will determine your fate.");
        System.out.println("=======================================");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    public void setupPlayer() {
        System.out.println("\n=====================================================");
        System.out.println("                CHARACTER SELECTION                  ");
        System.out.println("=====================================================");
        System.out.println("\nIn the 1800s, men and women faced different challenges");
        System.out.println("on the trail west. Your choice will affect some of the");
        System.out.println("situations you encounter.");

        boolean validChoice = false;
        while (!validChoice) {
            System.out.println("\nSelect your character's gender:");
            System.out.println("1. Male");
            System.out.println("2. Female");
            System.out.print("\nEnter your choice (1-2): ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                playerGender = "male";
                validChoice = true;
            } else if (choice.equals("2")) {
                playerGender = "female";
                validChoice = true;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }

        validChoice = false;
        while (!validChoice) {
            System.out.println("\nEnter your character's name:");
            String name = scanner.nextLine();

            if (name != null && !name.trim().isEmpty()) {
                playerName = name;
                validChoice = true;
            } else {
                System.out.println("Invalid name. Please try again.");
            }
        }
        player = new Player(playerName, playerGender);
    }
    
    public void setupFamilyMembers() {
        System.out.println("\n=====================================================");
        System.out.println("                FAMILY MEMBERS                       ");
        System.out.println("=====================================================");
        System.out.println("You'll need to name three family members traveling with you.");
        
        String[] familyMembers = new String[3];
        
        for (int i = 0; i < 3; i++) {
            boolean validName = false;
            while (!validName) {
                System.out.println("\nEnter the name of family member " + (i+1) + ":");
                String name = scanner.nextLine();
                
                if (name != null && !name.trim().isEmpty()) {
                    familyMembers[i] = name;
                    validName = true;
                } else {
                    System.out.println("Invalid name. Please try again.");
                }
            }
        }
        
        player.setFamilyMembers(familyMembers);
        
        System.out.println("\nYour family consists of:");
        System.out.println("- " + player.getName() + " (you)");
        for (String member : player.getFamilyMembers()) {
            System.out.println("- " + member);
        }
        
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void selectTrail() {
        System.out.println("\n=====================================================");
        System.out.println("                   TRAIL SELECTION                   ");
        System.out.println("=====================================================");

        System.out.println("\nThere were several major routes west. Each trail had");
        System.out.println("different destinations, terrain, and challenges:");

        System.out.println("\n1. Oregon Trail (2,170 miles)");
        System.out.println("   Destination: Oregon's Willamette Valley");
        System.out.println("   Historical Note: Most popular route for farmers seeking fertile land");

        System.out.println("\n2. California Trail (1,950 miles)");
        System.out.println("   Destination: California's gold fields and farmlands");
        System.out.println("   Historical Note: Became heavily traveled after the 1848 Gold Rush");

        System.out.println("\n3. Mormon Trail (1,300 miles)");
        System.out.println("   Destination: Salt Lake Valley, Utah");
        System.out.println("   Historical Note: Used by Mormon pioneers fleeing religious persecution");

        boolean validChoice = false;
        int trailNum = 0;
        while (!validChoice) {
            System.out.print("\nWhich trail would you like to take? (1-3): ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    trail = "Oregon";
                    departureLocation = "Independence, Missouri";
                    trailNum = 1;
                    validChoice = true;
                    break;
                case "2":
                    trail = "California";
                    departureLocation = "Independence, Missouri";
                    trailNum = 2;
                    validChoice = true;
                    break;
                case "3":
                    trail = "Mormon";
                    departureLocation = "Nauvoo, Illinois";
                    trailNum = 3;
                    validChoice = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        map = new Map(trailNum);

        System.out.println("\nYou have chosen to travel along the " + trail + " Trail.");
        System.out.println("Your journey will begin in " + departureLocation + ".");
    }

    public void selectDepartureMonth() {
        System.out.println("\n=====================================================");
        System.out.println("              DEPARTURE MONTH SELECTION              ");
        System.out.println("=====================================================");

        System.out.println("\nThe timing of your departure was crucial for pioneers.");
        System.out.println("Leave too early: face mud and flooding from spring rains.");
        System.out.println("Leave too late: risk being trapped in mountain snow.");
        System.out.println("\nMost emigrants departed between March and June.");

        System.out.println("\nSelect your departure month:");
        for (int i = 0; i < months.length; i++) {
            System.out.println((i+1) + ". " + months[i]);
        }

        boolean validChoice = false;
        while (!validChoice) {
            System.out.print("\nEnter your choice (1-5): ");
            try {
                monthChoice = Integer.parseInt(scanner.nextLine());
                if (monthChoice >= 1 && monthChoice <= 5) {
                    departureMonth = months[monthChoice-1];
                    validChoice = true;

                    switch (monthChoice) {
                        case 1:
                            System.out.println("\nMarch: An early start, but you'll face muddy trails and swollen rivers.");
                            break;
                        case 2:
                            System.out.println("\nApril: A good balance - the trails are drying out and you'll have plenty of time.");
                            break;
                        case 3:
                            System.out.println("\nMay: The most popular month for departure - grass for animals is plentiful.");
                            break;
                        case 4:
                            System.out.println("\nJune: Still a good time to leave, but you'll need to maintain a steady pace.");
                            break;
                        case 5:
                            System.out.println("\nJuly: A late start - you'll need to hurry to cross mountains before winter.");
                            break;
                    }
                } else {
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number between 1 and 5.");
            }
        }
    }

    public void prepareForDeparture() {
        System.out.println("\n=====================================================");
        System.out.println("                PREPARING FOR DEPARTURE              ");
        System.out.println("=====================================================");

        System.out.println("\n" + player.getName() + ", you are in " + departureLocation + ".");
        System.out.println("It is " + departureMonth + " 1848, and you're preparing to depart along the " + trail + ".");

        if (departureLocation.equals("Independence, Missouri")) {
            System.out.println("\nIndependence is bustling with activity as wagon trains form up.");
            System.out.println("The town is crowded with emigrants buying supplies for the journey.");
            System.out.println("Wagons, oxen, and provisions are selling at premium prices.");
        } else {
            System.out.println("\nNauvoo has been largely abandoned by the Mormons after");
            System.out.println("persecution and violence. Most are heading west to find");
            System.out.println("religious freedom in the Utah Territory.");
        }

        System.out.println("\nPress Enter to continue to equipping your wagon...");
        scanner.nextLine();
    }

    private void visitMarket() {
        System.out.println("=======================================");
        System.out.println("                MARKET                 ");
        System.out.println("=======================================");
        System.out.println("Before starting your journey, you need to buy supplies.");
        System.out.println("You have $" + player.getMoney() + " to spend.");
        System.out.println("Press Enter to continue...");
        scanner.nextLine();

        Market market = new Market(player, inventory);
        market.shop(scanner);
    }

    private void journeyToFortKearny() {
        int fortKearnyDistance = 0;
        
        // Find Fort Kearny's distance
        for (int i = 0; i < map.getLandmarks().size(); i++) {
            Landmark landmark = map.getLandmarks().get(i);
            if (landmark.getName().contains("Fort Kearny")) {
                fortKearnyDistance = landmark.getDistance();
                break;
            }
        }

        int averageDailyDistance = 15;
        int daysToFortKearny = fortKearnyDistance / averageDailyDistance;

        if (departureMonth.equals("March")) {
            daysToFortKearny += 3; // Muddy conditions in March
        } else if (departureMonth.equals("July")) {
            daysToFortKearny -= 1; // Better roads in summer
        }
        
        System.out.println("\n=====================================================");
        System.out.println("           JOURNEY TO FORT KEARNY                    ");
        System.out.println("=====================================================");
        System.out.println("Your journey to Fort Kearny takes " + daysToFortKearny + " days.");
        
        // Simulate the journey to Fort Kearny
        for (int i = 0; i < daysToFortKearny; i++) {
            // Advance time
            time.advanceDay();
            
            // Consume food
            int dailyFoodConsumption = player.getFamilySize() * 2;
            inventory.consumeFood(dailyFoodConsumption);
            
            // Check for random events (1/3 chance)
            if (Math.random() < 0.33) {
                perils.generateRandomEvent();
            }
            
            // Check if player died
            if (player.isDead()) {
                return;
            }
        }
        
        // Update the map position
        map.travel(fortKearnyDistance);
        
        System.out.println("\nYou have arrived at Fort Kearny!");
        System.out.println("This is where the real challenges of your journey begin.");
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void mainGameLoop() {
        while (isGameRunning) {
            // Check if game should end
            if (player.isDead()) {
                isGameRunning = false;
                continue;
            }
            
            if (map.hasReachedDestination()) {
                System.out.println("\nCongratulations! You've reached " + map.getDestination() + "!");
                isGameRunning = false;
                continue;
            }
            
            // Check if player reached Independence Rock after July
            if (map.getCurrentLocation().contains("Independence Rock") && time.getMonth() > 7) {
                System.out.println("\nYou've reached Independence Rock, but it's after July...");
                System.out.println("This means you might not make it through the mountains before winter.");
                System.out.println("Your journey becomes much more dangerous from here.");
            }
            
            // Display daily status and options
            displayDailyStatus();
            getDailyChoice();
            
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
                RiverCrossing crossing = new RiverCrossing(player, inventory, weather);
                crossing.crossRiver(scanner);
            }
            
            // Chance for hunting opportunity based on food supply
            if (inventory.getFood() < 100 && Math.random() < 0.3) {
                System.out.println("\nYour food supply is running low. Would you like to hunt? (Y/N)");
                String huntChoice = scanner.nextLine().toUpperCase();
                if (huntChoice.equals("Y")) {
                    hunting.hunt(scanner);
                }
            }
            
            // Check health and offer medicine if low
            if (player.getHealth() < 30 && inventory.getMedicine() > 0) {
                System.out.println("\nYour health is very low. Would you like to use medicine? (Y/N)");
                String medChoice = scanner.nextLine().toUpperCase();
                if (medChoice.equals("Y")) {
                    inventory.useMedicine(1);
                    player.increaseHealth(30);
                    System.out.println("You used 1 medicine and recovered 30 health.");
                }
            }
        }
    }

    private void handleLandmarkArrival() {
        map.advanceToNextLandmark();
        String landmark = map.getCurrentLocation();
        
        System.out.println("\n=====================================================");
        System.out.println("           ARRIVED AT " + landmark.toUpperCase());
        System.out.println("=====================================================");
        
        map.displayHistoricalInfo();
        
        // Check if this is a trading post
        if (landmark.contains("Fort") || landmark.contains("Trading Post")) {
            System.out.println("\nThis is a trading post. Would you like to trade? (Y/N)");
            String tradeChoice = scanner.nextLine().toUpperCase();
            if (tradeChoice.equals("Y")) {
                Trading trading = new Trading(player, inventory);
                trading.trade(scanner);
            }
        }
        
        System.out.println("\nPress Enter to continue your journey...");
        scanner.nextLine();
    }

    public void displayDailyStatus() {
        System.out.println("\n=====================================================");
        System.out.println("                  DAILY STATUS                      ");
        System.out.println("=====================================================");
        System.out.println("Date: " + time.getMonthName() + " " + time.getDay() + ", " + time.getYear());
        System.out.println("Days on trail: " + time.getTotalDays());
        System.out.println("Current location: " + map.getCurrentLocation());
        System.out.println("Distance traveled: " + map.getDistanceTraveled() + " miles");
        System.out.println("Next landmark: " + map.getNextLandmark() + " (" + map.getDistanceToNextLandmark() + " miles)");
        System.out.println("Weather: " + weather.getCurrentWeather());
        System.out.println("Health: " + player.getHealthStatus());
        System.out.println("Food remaining: " + inventory.getFood() + " pounds");
        System.out.println("Oxen health: " + inventory.getOxenHealth() + "%");
    }

    public void getDailyChoice() {
        boolean validChoice = false;
        
        while (!validChoice) {
            System.out.println("\nWhat would you like to do?");
            System.out.println("1. Travel");
            System.out.println("2. Rest");
            System.out.println("3. Hunt");
            System.out.println("4. View inventory");
            System.out.println("5. View historical information");
            System.out.println("6. Quit game");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    validChoice = true;
                    travel();
                    break;
                case "2":
                    validChoice = true;
                    rest();
                    break;
                case "3":
                    validChoice = true;
                    hunting.hunt(scanner);
                    break;
                case "4":
                    inventory.displayInventory();
                    break;
                case "5":
                    map.displayHistoricalInfo();
                    break;
                case "6":
                    System.out.println("Are you sure you want to quit? (Y/N)");
                    String confirm = scanner.nextLine().toUpperCase();
                    if (confirm.equals("Y")) {
                        isGameRunning = false;
                        validChoice = true;
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
        }
    }

    private void travel() {
        // Base travel distance
        int baseDistance = 15;
        
        // Modify distance based on weather
        int adjustedDistance = weather.adjustTravelDistance(baseDistance);
        
        // Modify based on oxen health
        adjustedDistance = (int)(adjustedDistance * (inventory.getOxenHealth() / 100.0));
        
        // Gender differences - women may have slightly different travel outcomes
        if (player.getGender().equals("female") && Math.random() < 0.2) {
            int bonus = (int)(Math.random() * 5);
            adjustedDistance += bonus;
            System.out.println("Your careful planning saved time on the trail! +" + bonus + " miles");
        }
        
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
            System.out.println("You have no food! Your health is declining.");
        }
        
        // Message about travel
        System.out.println("\nYou traveled " + adjustedDistance + " miles today.");
        System.out.println("Food consumed: " + actualFoodConsumed + " pounds.");
        
        // Small chance of minor injury during travel
        if (Math.random() < 0.1) {
            player.decreaseHealth(2);
            System.out.println("The rough trail caused some minor injuries and fatigue.");
        }
        
        // Small chance of oxen health decline
        if (Math.random() < 0.15) {
            inventory.decreaseOxenHealth(3);
            System.out.println("Your oxen are showing signs of fatigue.");
        }
    }

    private void rest() {
        System.out.println("\nYou decide to rest for the day.");
        
        // Recover health
        int healthRecovered = 5 + (int)(Math.random() * 10); // 5-15 health points
        player.increaseHealth(healthRecovered);
        System.out.println("Health improved by " + healthRecovered + " points.");
        
        // Recover oxen health
        int oxenHealthRecovered = 5 + (int)(Math.random() * 10); // 5-15 points
        inventory.increaseOxenHealth(oxenHealthRecovered);
        System.out.println("Oxen health improved by " + oxenHealthRecovered + " points.");
        
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
            System.out.println("You have no food! Your health is declining despite the rest.");
        }
        
        System.out.println("Food consumed: " + actualFoodConsumed + " pounds.");
        
        // Small chance of finding food while resting
        if (Math.random() < 0.2) {
            int foodFound = 2 + (int)(Math.random() * 8); // 2-10 pounds
            inventory.addFood(foodFound);
            System.out.println("While resting, your family found " + foodFound + " pounds of edible plants nearby.");
        }
        
        // Gender-specific event
        if (player.getGender().equals("female") && Math.random() < 0.3) {
            int extraHealth = (int)(Math.random() * 5) + 1;
            player.increaseHealth(extraHealth);
            System.out.println("Your knowledge of medicinal herbs helped everyone recover better. +" + extraHealth + " health");
        }
    }

    private void displayGameSummary() {
        System.out.println("\n=====================================================");
        System.out.println("                   GAME SUMMARY                     ");
        System.out.println("=====================================================");
        
        if (player.isDead()) {
            System.out.println("\nYou have died of " + player.getCauseOfDeath() + ".");
            System.out.println("\nYour journey has come to an end after " + time.getTotalDays() + " days on the trail.");
            System.out.println("You traveled " + map.getDistanceTraveled() + " miles.");
            System.out.println("Your last known location: near " + map.getCurrentLocation());
        } else if (map.hasReachedDestination()) {
            System.out.println("\nCONGRATULATIONS!");
            System.out.println("You have successfully completed your journey to " + map.getDestination() + "!");
            System.out.println("\nYour journey took " + time.getTotalDays() + " days.");
            System.out.println("You traveled " + map.getDistanceTraveled() + " miles.");
            System.out.println("Final date: " + time.getMonthName() + " " + time.getDay() + ", " + time.getYear());
            
            // If Independence Rock is in this trail and you made it past
            if (map.getTrailName().contains("Oregon") || map.getTrailName().contains("Mormon")) {
                System.out.println("\nHistorical Note: Many travelers considered the westward journey a success");
                System.out.println("if they reached Independence Rock by July 4th, giving them enough time");
                System.out.println("to cross the mountains before winter snows.");
                
                if (time.getMonth() <= 7 && time.getDay() <= 4) {
                    System.out.println("\nYou reached your destination in good time, avoiding the winter snows!");
                } else {
                    System.out.println("\nYour journey took longer than ideal, but you still made it to safety.");
                }
            }
        } else {
            System.out.println("\nYou decided to end your journey early after " + time.getTotalDays() + " days on the trail.");
            System.out.println("You traveled " + map.getDistanceTraveled() + " miles.");
            System.out.println("Final location: " + map.getCurrentLocation());
        }
        
        System.out.println("\nThank you for playing Perils Along the Platte!");
    }
}
