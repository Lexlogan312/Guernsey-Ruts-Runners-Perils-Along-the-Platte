import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

public class Perils {
    private final Player player;
    private final Inventory inventory;
    private final Random random;
    private final Time time;
    
    // Add a listener callback for messages
    private Consumer<String> messageListener;

    // Arrays of possible events
    private ArrayList<String> diseases;
    private ArrayList<String> injuries;
    private ArrayList<String> wagonProblems;
    private ArrayList<String> positiveEvents;
    private ArrayList<String> weatherEvents;
    private ArrayList<String> animalEvents;

    public Perils(Player player, Inventory inventory, Weather weather, Time time) {
        this.player = player;
        this.inventory = inventory;
        this.time = time;
        this.random = new Random();
        this.messageListener = s -> System.out.println(s); // Default to System.out
        
        initializeEvents();
    }
    
    /**
     * Set a message listener to handle notifications instead of System.out
     */
    public void setMessageListener(Consumer<String> listener) {
        this.messageListener = listener;
    }
    
    /**
     * Show a message through the listener or System.out
     */
    private void showMessage(String message) {
        if (messageListener != null) {
            messageListener.accept(message);
        } else {
            System.out.println(message);
        }
    }

    private void initializeEvents() {
        // Initialize disease events
        diseases = new ArrayList<>();
        diseases.add("cholera");
        diseases.add("typhoid fever");
        diseases.add("dysentery");
        diseases.add("measles");
        diseases.add("fever");
        diseases.add("exhaustion");
        diseases.add("snakebite");
        diseases.add("morale depreciation");

        // Initialize injury events
        injuries = new ArrayList<>();
        injuries.add("broken arm");
        injuries.add("broken leg");
        injuries.add("sprained ankle");
        injuries.add("cut requiring stitches");
        injuries.add("concussion");

        // Initialize wagon problems
        wagonProblems = new ArrayList<>();
        wagonProblems.add("broken wheel");
        wagonProblems.add("broken axle");
        wagonProblems.add("broken tongue");
        wagonProblems.add("stuck in mud");
        wagonProblems.add("wheel loose");

        // Initialize positive events
        positiveEvents = new ArrayList<>();
        positiveEvents.add("found wild berries");
        positiveEvents.add("found an abandoned wagon with supplies");
        positiveEvents.add("met friendly Native Americans who shared food");
        positiveEvents.add("found a clean water source");
        positiveEvents.add("good hunting spot");

        // Initialize weather events
        weatherEvents = new ArrayList<>();
        weatherEvents.add("sudden hailstorm");
        weatherEvents.add("thunderstorm with heavy winds");
        weatherEvents.add("dense fog");
        weatherEvents.add("unexpected snowfall");
        weatherEvents.add("dust storm");

        // Initialize animal events
        animalEvents = new ArrayList<>();
        animalEvents.add("snake in the grass");
        animalEvents.add("wolf pack spotted nearby");
        animalEvents.add("bears raided food");
        animalEvents.add("oxen spooked by coyotes");
        animalEvents.add("biting insects");
    }

    public void generateRandomEvent() {
        // Determine if an event happens (70% chance)
        if (random.nextDouble() > 0.7) {
            return; // No event
        }

        // Determine event type
        int eventType = random.nextInt(6);

        switch (eventType) {
            case 0:
                generateDiseaseEvent();
                break;
            case 1:
                generateInjuryEvent();
                break;
            case 2:
                generateWagonProblem();
                break;
            case 3:
                generatePositiveEvent();
                break;
            case 4:
                generateWeatherEvent();
                break;
            case 5:
                generateAnimalEvent();
                break;
        }
    }

    private void generateDiseaseEvent() {
        showMessage("\n=== HEALTH PROBLEM ===");

        // Determine who gets sick
        boolean isPlayer = random.nextBoolean();
        String victim;

        if (isPlayer) {
            victim = player.getName();
        } else {
            // Random family member
            String[] family = player.getFamilyMembers();
            victim = family[random.nextInt(family.length)];
        }

        // Select random disease
        String disease = diseases.get(random.nextInt(diseases.size()));
        if(disease == "morale depreciation"){
            if(player.getMorale() < 50) {
                showMessage("Your group morale is " + player.getMorale());
                if(player.getPreacherMoralModifier() > 1.0){
                    showMessage(player.getName() + " is a " + player.getJob() + "your morale has been increased");
                    player.increaseMorale(20);
                }
                else {
                    if(player.getPreacherMoralModifier() > 1.0) {
                        showMessage(player.getName() + " is a " + player.getJob() + "your morale has been increased");
                        player.increaseMorale(10);
                    }
                    showMessage("You need to rest for a day");
                    time.advanceDay();
                    player.increaseMorale(20);
                }
            }
            else{
                showMessage("Your group morale is " + player.getMorale());
                if(player.getMorale() < 70){
                    showMessage("Your morale is dropping, rest for a day to increase morale");
                }
            }
        }
        showMessage(victim + " has come down with " + disease + ".");

        // Health impact
        int healthImpact = 0;

        if(player.getDoctorModifier() > 1){
            healthImpact = (int) ((10 + random.nextInt(20)) / 1.2);
            showMessage(player.getName() + " is a doctor, your health impact was lessoned by 20% " + healthImpact + ".");
        }
        else {
            healthImpact = 10 + random.nextInt(20); // 10-30 health impact
        }

        // If it's a family member, less direct impact on player
        if (!isPlayer) {
            healthImpact = 5 + random.nextInt(10); // 5-15 health impact
            showMessage("Caring for " + victim + " is draining your energy.");
        }

        // Check if medicine is available
        if (inventory.getMedicine() > 0) {
            showMessage("You use 1 medicine to treat the " + disease + ".");
            inventory.useMedicine(1);
            healthImpact /= 2; // Medicine cuts health impact in half
        } else {
            showMessage("You have no medicine to treat the " + disease + ".");
        }

        // Apply health impact
        player.decreaseHealth(healthImpact);
        showMessage("Health decreased by " + healthImpact + " points.");

        // Small chance of death if health is already low
        if (isPlayer && player.getHealth() < 20 && random.nextDouble() < 0.2) {
            player.setCauseOfDeath(disease);
            player.setDead(true);
        }
    }

    private void generateInjuryEvent() {
        showMessage("\n=== INJURY ===");

        // Determine who gets injured
        boolean isPlayer = random.nextBoolean();
        String victim;

        if (isPlayer) {
            victim = player.getName();
        } else {
            // Random family member
            String[] family = player.getFamilyMembers();
            victim = family[random.nextInt(family.length)];
        }

        // Select random injury
        String injury = injuries.get(random.nextInt(injuries.size()));

        showMessage(victim + " has suffered a " + injury + ".");

        // Health impact
        int healthImpact = 15 + random.nextInt(20); // 15-35 health impact

        // If it's a family member, less direct impact on player
        if (!isPlayer) {
            healthImpact = 5 + random.nextInt(10); // 5-15 health impact
            showMessage("Caring for " + victim + " slows your travel.");
        }

        // Check if medicine is available
        if (inventory.getMedicine() > 0) {
            showMessage("You use 1 medicine to treat the " + injury + ".");
            inventory.useMedicine(1);
            healthImpact /= 2; // Medicine cuts health impact in half
        } else {
            showMessage("You have no medicine to treat the " + injury + ".");
        }

        // Apply health impact
        player.decreaseHealth(healthImpact);
        showMessage("Health decreased by " + healthImpact + " points.");
    }

    private void generateWagonProblem() {
        showMessage("\n=== WAGON PROBLEM ===");

        // Select random wagon problem
        String problem = wagonProblems.get(random.nextInt(wagonProblems.size()));

        showMessage("Your wagon has a " + problem + ".");

        // Check if wagon parts are available
        if (inventory.getWagonParts() > 0) {
            showMessage("You use 1 wagon part to fix the problem.");
            inventory.useWagonParts(1);
            showMessage("The repair takes some time, but you're able to continue.");
        } else {
            showMessage("You have no wagon parts to make repairs!");
            showMessage("You'll have to improvise a solution, which takes time and energy.");

            // Health impact from working hard to fix wagon without parts
            int healthImpact = 5 + random.nextInt(10); // 5-15 health impact
            player.decreaseHealth(healthImpact);
            showMessage("Health decreased by " + healthImpact + " points from the hard labor.");

            // Chance of permanent wagon damage
            if (random.nextDouble() < 0.3) {
                showMessage("Your makeshift repair isn't as good as it should be.");
                showMessage("Your wagon will now move a bit slower on the trail.");
                // This effect is simulated in the oxen health factor
                inventory.decreaseOxenHealth(10);
            }
        }
    }

    private void generatePositiveEvent() {
        showMessage("\n=== GOOD FORTUNE ===");

        // Select random positive event
        String event = positiveEvents.get(random.nextInt(positiveEvents.size()));

        showMessage("Good news! You " + event + ".");

        // Apply positive effect
        if (event.contains("berries")) {
            int foodGained = 5 + random.nextInt(10); // 5-15 pounds of food
            inventory.addFood(foodGained);
            showMessage("You add " + foodGained + " pounds of food to your supplies.");
        } else if (event.contains("abandoned wagon")) {
            // Random assortment of supplies
            int foodGained = 10 + random.nextInt(30); // 10-40 pounds of food
            int partsGained = random.nextInt(2); // 0-1 parts
            int medicineGained = random.nextInt(2); // 0-1 medicine

            inventory.addFood(foodGained);
            inventory.addWagonParts(partsGained);
            inventory.addMedicine(medicineGained);

            showMessage("You salvage:");
            showMessage("- " + foodGained + " pounds of food");
            if (partsGained > 0) showMessage("- " + partsGained + " wagon parts");
            if (medicineGained > 0) showMessage("- " + medicineGained + " medicine");
        } else if (event.contains("Native Americans")) {
            int foodGained = 15 + random.nextInt(15); // 15-30 pounds of food
            inventory.addFood(foodGained);
            showMessage("They share " + foodGained + " pounds of food with you.");
            showMessage("They also show you a better route, saving you time.");
        } else if (event.contains("water source")) {
            showMessage("Everyone in your party feels refreshed.");
            player.increaseHealth(10);
            showMessage("Health increased by 10 points.");
        } else if (event.contains("hunting")) {
            int foodGained = 25 + random.nextInt(50); // 25-75 pounds of food
            int ammoUsed = 1 + random.nextInt(3); // 1-3 ammunition used

            if (inventory.getAmmunition() >= ammoUsed) {
                inventory.useAmmunition(ammoUsed);
                inventory.addFood(foodGained);
                showMessage("You use " + ammoUsed + " ammunition and get " + foodGained + " pounds of food.");
            } else {
                showMessage("Unfortunately, you don't have enough ammunition to hunt effectively.");
                foodGained = 5 + random.nextInt(10); // Much less food without ammunition
                inventory.addFood(foodGained);
                showMessage("You manage to gather " + foodGained + " pounds of food instead.");
            }
        }
    }

    private void generateWeatherEvent() {
        showMessage("\n=== WEATHER EVENT ===");

        // Select random weather event
        String event = weatherEvents.get(random.nextInt(weatherEvents.size()));

        showMessage(event);

        // Apply weather effect
        if (event.contains("rain")) {
            // Rain slows travel but may help find water
            inventory.decreaseOxenHealth(5); // Muddy conditions strain animals
            showMessage("The rain slows your travel but provides fresh drinking water.");
            
            // Food loss chance
            if (random.nextDouble() < 0.3) {
                int foodLost = 5 + random.nextInt(15); // 5-20 pounds
                inventory.consumeFood(foodLost);
                showMessage("Some of your food got wet and spoiled. You lost " + foodLost + " pounds of food.");
            }
        } else if (event.contains("snow")) {
            // Snow significantly slows travel
            inventory.decreaseOxenHealth(10);
            player.decreaseHealth(5);
            showMessage("The severe cold affects everyone's health and slows travel considerably.");
            
            // Food consumption increases
            int extraFood = player.getFamilySize();
            if (inventory.getFood() >= extraFood) {
                inventory.consumeFood(extraFood);
                showMessage("The cold weather makes everyone hungrier. You consume " + extraFood + " extra pounds of food.");
            } else {
                player.decreaseHealth(3);
                showMessage("You don't have enough extra food for the cold weather. Everyone suffers a bit more.");
            }
        } else if (event.contains("lightning")) {
            // Lightning has a chance to damage supplies or wagon
            boolean wagonDamage = random.nextDouble() < 0.4;
            
            if (wagonDamage) {
                showMessage("A lightning strike damages your wagon!");
                
                if (inventory.getWagonParts() > 0) {
                    inventory.useWagonParts(1);
                    showMessage("You use 1 wagon part to repair the damage.");
                } else {
                    showMessage("You have no spare parts to properly repair your wagon.");
                    inventory.decreaseOxenHealth(15);
                    showMessage("Your makeshift repairs will slow your travel significantly.");
                }
            } else {
                // Just a scare
                showMessage("The storm passes without causing serious harm.");
            }
        } else if (event.contains("hail")) {
            // Hail can damage food supplies
            int foodLost = 10 + random.nextInt(20); // 10-30 pounds
            inventory.consumeFood(foodLost);
            showMessage("Hail damages some of your supplies. You lost " + foodLost + " pounds of food.");
            
            // Small chance of injury
            if (random.nextDouble() < 0.2) {
                player.decreaseHealth(5);
                showMessage("The hail causes minor injuries to members of your party.");
            }
        } else if (event.contains("drought")) {
            // Drought affects health more than supplies
            player.decreaseHealth(7);
            showMessage("The drought leaves everyone dehydrated and weakened.");
            showMessage("You'll need to find water soon or health will continue to decline.");
        } else if (event.contains("fog")) {
            // Fog mostly just slows travel and creates risk
            showMessage("The fog makes travel treacherous. You proceed with caution.");
            
            // Small chance of wagon accident
            if (random.nextDouble() < 0.2) {
                showMessage("In the fog, your wagon hits a rock and sustains damage.");
                
                if (inventory.getWagonParts() > 0) {
                    inventory.useWagonParts(1);
                    showMessage("You use 1 wagon part to repair the damage.");
                } else {
                    showMessage("You have no spare parts to properly repair your wagon.");
                    inventory.decreaseOxenHealth(10);
                }
            }
        }
    }

    private void generateAnimalEvent() {
        showMessage("\n=== ANIMAL ENCOUNTER ===");

        // Select random animal event
        String event = animalEvents.get(random.nextInt(animalEvents.size()));

        showMessage(event);

        // Apply effect based on animal type
        if (event.contains("buffalo")) {
            // Buffalo could be food or danger
            boolean canHunt = inventory.getAmmunition() >= 3;
            
            if (canHunt && random.nextDouble() < 0.6) {
                int ammoUsed = 3;
                inventory.useAmmunition(ammoUsed);
                
                int foodGained = 200 + random.nextInt(300); // 200-500 pounds
                inventory.addFood(foodGained);
                
                showMessage("You successfully hunt a buffalo, using " + ammoUsed + " ammunition.");
                showMessage("You gain " + foodGained + " pounds of food! That should last a while.");
            } else if (canHunt) {
                inventory.useAmmunition(2);
                showMessage("You try to hunt the buffalo, using 2 ammunition, but it escapes.");
            } else {
                showMessage("Without ammunition, you can only watch as the buffalo herd passes by.");
                
                // Small chance of stampede
                if (random.nextDouble() < 0.2) {
                    showMessage("A buffalo gets too close to your wagon and startles your oxen!");
                    inventory.decreaseOxenHealth(10);
                    
                    int foodLost = 10 + random.nextInt(20); // 10-30 pounds
                    if (inventory.getFood() > foodLost) {
                        inventory.consumeFood(foodLost);
                        showMessage("In the chaos, you lose " + foodLost + " pounds of food.");
                    }
                }
            }
        } else if (event.contains("bears")) {
            // Bears are dangerous - ammunition helps
            boolean hasAmmo = inventory.getAmmunition() >= 3;
            
            if (hasAmmo) {
                inventory.useAmmunition(3);
                showMessage("You fire shots to scare away the bears, using 3 ammunition.");
                
                // Small chance to hunt
                if (random.nextDouble() < 0.3) {
                    int foodGained = 100 + random.nextInt(100); // 100-200 pounds
                    inventory.addFood(foodGained);
                    showMessage("You manage to kill a bear, gaining " + foodGained + " pounds of food!");
                }
            } else {
                // No ammunition - health impact
                int healthLoss = 10 + random.nextInt(15); // 10-25 health
                player.decreaseHealth(healthLoss);
                showMessage("Without ammunition, you have to hide and wait for the bears to leave.");
                showMessage("The encounter leaves everyone shaken and costs valuable time.");
                showMessage("Health decreased by " + healthLoss + " points from stress and lost time.");
                
                // Food loss possible
                if (random.nextDouble() < 0.5) {
                    int foodLost = 20 + random.nextInt(30); // 20-50 pounds
                    if (inventory.getFood() > foodLost) {
                        inventory.consumeFood(foodLost);
                        showMessage("The bears get into your food supplies! You lose " + foodLost + " pounds of food.");
                    }
                }
            }
        } else if (event.contains("wolves")) {
            // Wolves are a threat to oxen more than people
            boolean hasAmmo = inventory.getAmmunition() >= 2;
            
            if (hasAmmo) {
                inventory.useAmmunition(2);
                showMessage("You fire shots to scare away the wolves, using 2 ammunition.");
            } else {
                // No ammunition - oxen health impact
                inventory.decreaseOxenHealth(15);
                showMessage("Without ammunition, you struggle to protect your oxen from the wolves.");
                showMessage("Your oxen are injured and exhausted from the encounter.");
                
                // Small health impact to player
                player.decreaseHealth(5);
                showMessage("The stress of the situation affects everyone's health.");
            }
        }
    }
} 