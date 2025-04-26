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
    private ArrayList<String> femaleSpecificEvents;
    private ArrayList<String> maleSpecificEvents;

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
        
        // Initialize female-specific events
        femaleSpecificEvents = new ArrayList<>();
        femaleSpecificEvents.add("pregnancy");
        femaleSpecificEvents.add("childbirth complications");
        femaleSpecificEvents.add("made extra money sewing for other travelers");
        femaleSpecificEvents.add("befriended other pioneer families");
        femaleSpecificEvents.add("negotiated a better trading deal");
        
        // Initialize male-specific events
        maleSpecificEvents = new ArrayList<>();
        maleSpecificEvents.add("injured while hunting");
        maleSpecificEvents.add("back strain from heavy lifting");
        maleSpecificEvents.add("made extra money helping repair wagons");
        maleSpecificEvents.add("found better hunting grounds");
        maleSpecificEvents.add("successfully repaired wagon without parts");
    }

    public void generateRandomEvent() {
        // Determine if an event happens (70% chance)
        if (random.nextDouble() > 0.7) {
            return; // No event
        }

        // Determine if this should be a gender-specific event (15% chance if player is main character)
        if (random.nextDouble() < 0.15) {
            if ("female".equalsIgnoreCase(player.getGender())) {
                generateFemaleSpecificEvent();
                return;
            } else if ("male".equalsIgnoreCase(player.getGender())) {
                generateMaleSpecificEvent();
                return;
            }
        }

        // Determine regular event type
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

    /**
     * Generate events specific to female characters
     */
    private void generateFemaleSpecificEvent() {
        showMessage("\n=== EVENT ===");
        
        // Select random female-specific event
        String event = femaleSpecificEvents.get(random.nextInt(femaleSpecificEvents.size()));
        
        if (event.contains("pregnancy")) {
            showMessage("You discover you are pregnant.");
            showMessage("Historically, many women on the trail were pregnant or gave birth during the journey.");
            showMessage("This will make travel more difficult, but the journey must continue.");
            
            // Health impact
            int healthImpact = 10 + random.nextInt(10); // 10-20 health impact
            player.decreaseHealth(healthImpact);
            showMessage("Health decreased by " + healthImpact + " points due to pregnancy symptoms.");
            
            // Extra food consumption
            int extraFood = 2;
            inventory.consumeFood(extraFood);
            showMessage("You need to eat more to maintain your strength. You consume " + extraFood + " extra pounds of food.");
        } 
        else if (event.contains("childbirth")) {
            showMessage("You face childbirth complications on the trail.");
            showMessage("Historically, childbirth was extremely dangerous on the frontier, with little medical help available.");
            
            // Significant health impact
            int healthImpact = 20 + random.nextInt(20); // 20-40 health impact
            
            // Medicine helps substantially
            if (inventory.getMedicine() > 0) {
                showMessage("You use 1 medicine kit to help with the complications.");
                inventory.useMedicine(1);
                healthImpact /= 2;
            } else {
                showMessage("You have no medicine kits available for this difficult situation.");
                
                // Serious risk of death without medicine
                if (random.nextDouble() < 0.15) {
                    player.setCauseOfDeath("childbirth complications");
                    player.setDead(true);
                    return;
                }
            }
            
            player.decreaseHealth(healthImpact);
            showMessage("Health decreased by " + healthImpact + " points.");
            showMessage("You'll need to rest more often in the coming days.");
        }
        else if (event.contains("sewing")) {
            showMessage("You use your sewing skills to mend clothing for other travelers in exchange for supplies.");
            showMessage("Historically, women's domestic skills were highly valued on the trail and could be traded for needed goods.");
            
            // Random gain of supplies
            int foodGained = 10 + random.nextInt(20); // 10-30 pounds of food
            int moneyGained = 5 + random.nextInt(10); // $5-$15
            
            inventory.addFood(foodGained);
            player.addMoney(moneyGained);
            
            showMessage("You gain " + foodGained + " pounds of food and $" + moneyGained + " from your work.");
        }
        else if (event.contains("befriended")) {
            showMessage("You befriend other pioneer families traveling the same route.");
            showMessage("Historically, women formed important social bonds on the trail that helped with childcare and sharing resources.");
            
            // Health boost
            int healthBoost = 5 + random.nextInt(10); // 5-15 health boost
            player.increaseHealth(healthBoost);
            showMessage("The social support improves your morale and health by " + healthBoost + " points.");
            
            // Small supply boost
            int foodGained = 5 + random.nextInt(10); // 5-15 pounds of food
            inventory.addFood(foodGained);
            showMessage("Your new friends share " + foodGained + " pounds of food with your family.");
        }
        else if (event.contains("trading")) {
            showMessage("Your negotiation skills secure a better trading deal with a passing merchant.");
            showMessage("Historically, many women were skilled negotiators and managed family finances on the frontier.");
            
            // Better deals in trading
            int moneyGained = 10 + random.nextInt(15); // $10-$25
            player.addMoney(moneyGained);
            
            // Random supply gain
            int supplyType = random.nextInt(3);
            if (supplyType == 0) {
                int partsGained = 1 + random.nextInt(2); // 1-2 parts
                inventory.addWagonParts(partsGained);
                showMessage("You negotiate a better price and save $" + moneyGained + ".");
                showMessage("You also secure " + partsGained + " wagon parts at a discount.");
            } else if (supplyType == 1) {
                int medicineGained = 1 + random.nextInt(2); // 1-2 medicine kits
                inventory.addMedicine(medicineGained);
                showMessage("You negotiate a better price and save $" + moneyGained + ".");
                showMessage("You also secure " + medicineGained + " medicine kit" + (medicineGained > 1 ? "s" : "") + " at a discount.");
            } else {
                int ammoGained = 5 + random.nextInt(10); // 5-15 ammo
                inventory.addAmmunition(ammoGained);
                showMessage("You negotiate a better price and save $" + moneyGained + ".");
                showMessage("You also secure " + ammoGained + " ammunition at a discount.");
            }
        }
    }
    
    /**
     * Generate events specific to male characters
     */
    private void generateMaleSpecificEvent() {
        showMessage("\n=== EVENT ===");
        
        // Select random male-specific event
        String event = maleSpecificEvents.get(random.nextInt(maleSpecificEvents.size()));
        
        if (event.contains("hunting")) {
            showMessage("You are injured while hunting for food.");
            showMessage("Historically, hunting accidents were common on the trail, with many men suffering gunshot wounds or falls.");
            
            // Health impact
            int healthImpact = 15 + random.nextInt(15); // 15-30 health impact
            
            // Medicine helps
            if (inventory.getMedicine() > 0) {
                showMessage("You use 1 medicine kit to treat your injury.");
                inventory.useMedicine(1);
                healthImpact /= 2;
            } else {
                showMessage("You have no medicine kits to treat your injury.");
            }
            
            player.decreaseHealth(healthImpact);
            showMessage("Health decreased by " + healthImpact + " points.");
            
            // Potential ammunition loss
            if (random.nextDouble() < 0.5 && inventory.getAmmunition() > 0) {
                int ammoLost = 1 + random.nextInt(5); // 1-5 ammo lost
                inventory.useAmmunition(Math.min(ammoLost, inventory.getAmmunition()));
                showMessage("You lost " + ammoLost + " ammunition in the accident.");
            }
        }
        else if (event.contains("back strain")) {
            showMessage("You've strained your back while loading heavy supplies.");
            showMessage("Historically, men did most of the heavy lifting on the trail, leading to frequent injuries.");
            
            // Health impact
            int healthImpact = 10 + random.nextInt(10); // 10-20 health impact
            player.decreaseHealth(healthImpact);
            showMessage("Health decreased by " + healthImpact + " points.");
            showMessage("You'll need to move more carefully for the next few days.");
        }
        else if (event.contains("repair wagons")) {
            showMessage("You help repair the wagons of other travelers and earn some extra money.");
            showMessage("Historically, men with carpentry and blacksmithing skills were in high demand on the trail.");
            
            // Money gain
            int moneyGained = 15 + random.nextInt(20); // $15-$35
            player.addMoney(moneyGained);
            showMessage("You earn $" + moneyGained + " for your work.");
            
            // Skill improvement
            showMessage("The experience makes you better at repairing your own wagon.");
            showMessage("Future repairs will require fewer resources.");
        }
        else if (event.contains("hunting grounds")) {
            showMessage("Your tracking skills lead you to excellent hunting grounds.");
            showMessage("Historically, men's hunting skills were essential for supplementing trail rations with fresh meat.");
            
            // Better hunting outcomes
            if (inventory.getAmmunition() >= 2) {
                int ammoUsed = 2;
                inventory.useAmmunition(ammoUsed);
                
                int foodGained = 50 + random.nextInt(100); // 50-150 pounds
                inventory.addFood(foodGained);
                
                showMessage("You use " + ammoUsed + " ammunition and bring back " + foodGained + " pounds of food!");
            } else {
                showMessage("Unfortunately, you don't have enough ammunition to take advantage of the hunting grounds.");
            }
        }
        else if (event.contains("repaired wagon")) {
            showMessage("You successfully improvise repairs on your wagon without using spare parts.");
            showMessage("Historically, men were expected to maintain wagons with whatever materials were available.");
            
            // Save wagon parts
            showMessage("Your ingenuity saves valuable wagon parts for future emergencies.");
            
            // Health impact from hard work
            int healthImpact = 5 + random.nextInt(5); // 5-10 health impact
            player.decreaseHealth(healthImpact);
            showMessage("The hard work costs you " + healthImpact + " health points, but your wagon is fixed.");
            
            // Skill improvement
            showMessage("This experience makes you better at future repairs.");
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
        // Gender specific differences in disease susceptibility
        else if (isPlayer && "female".equalsIgnoreCase(player.getGender())) {
            // Women were historically considered more susceptible to certain illnesses on the trail
            healthImpact += 5; // Additional 5 health impact
            showMessage("Women on the trail often faced additional health challenges.");
        }

        // Check if medicine is available
        if (inventory.getMedicine() > 0) {
            showMessage("You use 1 medicine kit to treat the " + disease + ".");
            inventory.useMedicine(1);
            healthImpact /= 2; // Medicine cuts health impact in half
        } else {
            showMessage("You have no medicine kits to treat the " + disease + ".");
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
            showMessage("You use 1 medicine kit to treat the " + injury + ".");
            inventory.useMedicine(1);
            healthImpact /= 2; // Medicine cuts health impact in half
        } else {
            showMessage("You have no medicine kits to treat the " + injury + ".");
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
        
        // Determine which part is needed based on the problem description
        boolean useWheel = problem.contains("wheel") || problem.contains("tire");
        boolean useAxle = problem.contains("axle") || problem.contains("undercarriage");
        boolean useTongue = problem.contains("tongue") || problem.contains("pole");
        boolean useWagonBow = problem.contains("bow") || problem.contains("cover") || problem.contains("canvas");
        
        // If no specific part is identified, default to a random part
        if (!useWheel && !useAxle && !useTongue && !useWagonBow) {
            int randomPart = random.nextInt(4);
            switch (randomPart) {
                case 0: useWheel = true; break;
                case 1: useAxle = true; break;
                case 2: useTongue = true; break;
                case 3: useWagonBow = true; break;
            }
        }
        
        String partNeeded = useWheel ? "wheel" : 
                           useAxle ? "axle" : 
                           useTongue ? "tongue" : 
                           "wagon bow";
        
        // Check if the needed part is available
        boolean hasNeededPart = (useWheel && inventory.getWheels() > 0) ||
                               (useAxle && inventory.getAxles() > 0) ||
                               (useTongue && inventory.getTongues() > 0) ||
                               (useWagonBow && inventory.getWagonBows() > 0);
        
        if (hasNeededPart) {
            showMessage("You need to use a " + partNeeded + " to fix the problem.");
            
            // Use the appropriate part
            if (useWheel) {
                inventory.useWheels(1);
                showMessage("You used 1 wheel from your inventory.");
            } else if (useAxle) {
                inventory.useAxles(1);
                showMessage("You used 1 axle from your inventory.");
            } else if (useTongue) {
                inventory.useTongues(1);
                showMessage("You used 1 tongue from your inventory.");
            } else {
                inventory.useWagonBows(1);
                showMessage("You used 1 wagon bow from your inventory.");
            }
            
            showMessage("The repair takes some time, but you're able to continue.");
            
            // Gender-specific skill difference in repairs
            if ("male".equalsIgnoreCase(player.getGender())) {
                showMessage("Your experience with wagon repairs makes this a quicker fix.");
                // Men recover a small amount of health due to confidence in their work
                player.increaseHealth(2);
            }
        } else {
            // Check if player has any parts, even if not the specific needed one
            boolean hasAnyParts = inventory.getWheels() > 0 || 
                                 inventory.getAxles() > 0 || 
                                 inventory.getTongues() > 0 || 
                                 inventory.getWagonBows() > 0;
            
            if (hasAnyParts) {
                showMessage("You don't have a " + partNeeded + " for the repair, but you might be able to improvise...");
                
                // Use any available part as a substitute, but with diminished effectiveness
                if (inventory.getWagonBows() > 0) {
                    inventory.useWagonBows(1);
                    showMessage("You try using a wagon bow to create a makeshift " + partNeeded + ".");
                } else if (inventory.getTongues() > 0) {
                    inventory.useTongues(1);
                    showMessage("You try adapting a wagon tongue to work as a " + partNeeded + ".");
                } else if (inventory.getAxles() > 0) {
                    inventory.useAxles(1);
                    showMessage("You try modifying an axle to function as a " + partNeeded + ".");
                } else if (inventory.getWheels() > 0) {
                    inventory.useWheels(1);
                    showMessage("You try salvaging parts from a wheel to create a makeshift " + partNeeded + ".");
                }
                
                // Improvised repair has a chance to work
                if (random.nextDouble() < 0.6) {
                    showMessage("Your improvised repair seems to be holding up!");
                    
                    // Gender-specific skill difference in repairs
                    if ("male".equalsIgnoreCase(player.getGender()) && random.nextDouble() < 0.6) {
                        showMessage("Your mechanical experience helps the repair work better than expected.");
                        // No additional penalty
                    } else {
                        showMessage("The wagon seems a bit unsteady with the improvised repair.");
                        inventory.decreaseOxenHealth(5);
                    }
                } else {
                    showMessage("Unfortunately, your improvised repair isn't very effective.");
                    showMessage("The wagon's performance will be diminished until you can make a proper repair.");
                    inventory.decreaseOxenHealth(15);
                }
            } else {
                showMessage("You have no wagon parts to make repairs!");
                showMessage("You'll have to improvise a solution, which takes time and energy.");

                // Health impact from working hard to fix wagon without parts
                int healthImpact = 5 + random.nextInt(10); // 5-15 health impact
                
                // Gender-specific repair outcomes
                if ("male".equalsIgnoreCase(player.getGender()) && random.nextDouble() < 0.6) {
                    healthImpact -= 2; // Men take slightly less health damage from repairs
                    showMessage("Your experience with mechanical repairs helps you find a solution more quickly.");
                } else if ("female".equalsIgnoreCase(player.getGender())) {
                    healthImpact += 2; // Women take slightly more health damage from repairs
                    showMessage("Without formal training in wagon repairs common for women of this era, you struggle more with the fix.");
                }
                
                player.decreaseHealth(healthImpact);
                showMessage("Health decreased by " + healthImpact + " points from the hard labor.");

                // Chance of permanent wagon damage
                if (random.nextDouble() < 0.3) {
                    showMessage("Your makeshift repair isn't as good as it should be.");
                    showMessage("Your wagon will now move a bit slower on the trail.");
                    // This effect is simulated in the oxen health factor
                    inventory.decreaseOxenHealth(20);
                }
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
            
            // Women were historically better foragers
            if ("female".equalsIgnoreCase(player.getGender())) {
                foodGained += 5; // Additional 5 pounds of food
                showMessage("Your knowledge of wild plants helps you find more edible berries.");
            }
            
            inventory.addFood(foodGained);
            showMessage("You add " + foodGained + " pounds of food to your supplies.");
        } else if (event.contains("abandoned wagon")) {
            // Random assortment of supplies
            int foodGained = 10 + random.nextInt(30); // 10-40 pounds of food
            int medicineGained = random.nextInt(2); // 0-1 medicine kit
            int ammoGained = random.nextInt(20); // 0-19 ammunition

            // Add resources
            inventory.addFood(foodGained);
            inventory.addMedicine(medicineGained);
            inventory.addAmmunition(ammoGained);

            showMessage("You scavenge:");
            showMessage("- " + foodGained + " pounds of food");
            if (medicineGained > 0) showMessage("- " + medicineGained + " medicine kit");
            if (ammoGained > 0) showMessage("- " + ammoGained + " rounds of ammunition");
        } else if (event.contains("Native Americans")) {
            int foodGained = 15 + random.nextInt(15); // 15-30 pounds of food
            
            // Women often had better diplomatic relations with native peoples
            if ("female".equalsIgnoreCase(player.getGender())) {
                foodGained += 10; // Additional 10 pounds of food
                int medicineGained = 1; // Bonus medicine
                inventory.addMedicine(medicineGained);
                showMessage("Your friendly approach leads to a cultural exchange. You learn about medicinal plants.");
                showMessage("You gain 1 medicine from this knowledge.");
            }
            
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

            // Men were typically better hunters in this era
            if ("male".equalsIgnoreCase(player.getGender())) {
                foodGained += 25; // Additional 25 pounds of food
                ammoUsed = Math.max(1, ammoUsed - 1); // Use 1 less ammunition (minimum 1)
                showMessage("Your hunting skills allow you to bring down larger game with fewer shots.");
            } else if ("female".equalsIgnoreCase(player.getGender())) {
                foodGained -= 10; // 10 pounds less food
                showMessage("Though women weren't typically the primary hunters in this era, you manage a successful hunt.");
            }

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
        } else if (event.contains("herbs")) {
            showMessage("You discover medicinal herbs and learn how to use them from a fellow traveler.");
            showMessage("This knowledge will benefit your family's health on the journey.");
            
            // Health boost
            int healthBoost = 10 + random.nextInt(10); // 10-20 health boost
            player.increaseHealth(healthBoost);
            showMessage("Immediate health improved by " + healthBoost + " points.");
            
            // Medicine gain
            int medicineGained = 1; // Bonus medicine kit
            inventory.addMedicine(medicineGained);
            
            showMessage("You gain 1 medicine kit from this knowledge.");
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
            
            // Small chance of freezing/exposure death if health is already low
            if (player.getHealth() < 30 && random.nextDouble() < 0.10) {
                player.decreaseHealth(player.getHealth()); // Ensure death
                player.setCauseOfDeath("exposure to cold");
                player.setDead(true);
                showMessage("The bitter cold is too much in your weakened condition. Someone in your party succumbs to the freezing temperatures.");
            }
        } else if (event.contains("lightning")) {
            // Lightning has a chance to damage supplies or wagon
            boolean wagonDamage = random.nextDouble() < 0.4;
            
            if (wagonDamage) {
                showMessage("A lightning strike damages your wagon!");
                
                // Random part damaged
                int partType = random.nextInt(4);
                String damagedPart;
                boolean hasPart = false;
                
                switch (partType) {
                    case 0:
                        damagedPart = "wheel";
                        hasPart = inventory.getWheels() > 0;
                        if (hasPart) inventory.useWheels(1);
                        break;
                    case 1:
                        damagedPart = "axle";
                        hasPart = inventory.getAxles() > 0;
                        if (hasPart) inventory.useAxles(1);
                        break;
                    case 2:
                        damagedPart = "tongue";
                        hasPart = inventory.getTongues() > 0;
                        if (hasPart) inventory.useTongues(1);
                        break;
                    default:
                        damagedPart = "wagon bow";
                        hasPart = inventory.getWagonBows() > 0;
                        if (hasPart) inventory.useWagonBows(1);
                        break;
                }
                
                if (hasPart) {
                    showMessage("The strike damaged a " + damagedPart + ". You use a spare to repair it.");
                } else {
                    showMessage("The strike damaged a " + damagedPart + ", but you have no spare parts!");
                    showMessage("You have no spare parts to properly repair your wagon.");
                    inventory.decreaseOxenHealth(15);
                    showMessage("Your makeshift repairs will slow your travel significantly.");
                }
            } else {
                // Just a scare
                showMessage("The storm passes without causing serious harm.");
            }
            
            // Very small chance of lightning strike on person
            if (random.nextDouble() < 0.02) { // 2% chance
                int healthLost = 30 + random.nextInt(30); // 30-60 damage
                player.decreaseHealth(healthLost);
                showMessage("Lightning strikes dangerously close, causing injuries!");
                
                if (player.getHealth() <= 0) {
                    player.setCauseOfDeath("lightning strike");
                    player.setDead(true);
                    showMessage("Tragedy! Someone was struck by lightning and killed instantly.");
                }
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
        } else if (event.contains("drought") || event.contains("heat")) {
            // Drought affects health more than supplies
            int healthLoss = 7 + random.nextInt(8); // 7-15 health loss
            player.decreaseHealth(healthLoss);
            showMessage("The extreme heat and drought leaves everyone dehydrated and weakened.");
            showMessage("You lost " + healthLoss + " health points.");
            
            // Chance of heat stroke/death in extreme conditions
            if (player.getHealth() < 25 && random.nextDouble() < 0.15) {
                player.decreaseHealth(player.getHealth()); // Ensure death
                player.setCauseOfDeath("heat stroke");
                player.setDead(true);
                showMessage("The relentless heat causes someone in your party to suffer fatal heat stroke.");
            }
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
                
                // Men typically had more hunting experience
                if ("male".equalsIgnoreCase(player.getGender())) {
                    foodGained += 50; // Additional 50 pounds
                    ammoUsed = Math.max(2, ammoUsed - 1); // Use 1 less ammo (min 2)
                    showMessage("Your hunting experience helps you make a more efficient kill.");
                } else {
                    foodGained -= 50; // 50 pounds less
                    showMessage("Though women weren't typically the primary hunters in this era, your shot is successful.");
                }
                
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
                    
                    // Gender-based hunting differences
                    if ("male".equalsIgnoreCase(player.getGender())) {
                        foodGained += 30; // Additional 30 pounds
                        showMessage("Your hunting experience allows you to make a clean kill.");
                    }
                    
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