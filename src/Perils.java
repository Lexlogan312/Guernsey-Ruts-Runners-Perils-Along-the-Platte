import java.util.ArrayList;
import java.util.Random;

public class Perils {
    private Player player;
    private Inventory inventory;
    private Weather weather;
    private Random random;

    // Arrays of possible events
    private ArrayList<String> diseases;
    private ArrayList<String> injuries;
    private ArrayList<String> wagonProblems;
    private ArrayList<String> positiveEvents;
    private ArrayList<String> weatherEvents;
    private ArrayList<String> animalEvents;

    public Perils(Player player, Inventory inventory, Weather weather) {
        this.player = player;
        this.inventory = inventory;
        this.weather = weather;
        this.random = new Random();

        initializeEvents();
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
        System.out.println("\n=== HEALTH PROBLEM ===");

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

        System.out.println(victim + " has come down with " + disease + ".");

        // Health impact
        int healthImpact = 10 + random.nextInt(20); // 10-30 health impact

        // If it's a family member, less direct impact on player
        if (!isPlayer) {
            healthImpact = 5 + random.nextInt(10); // 5-15 health impact
            System.out.println("Caring for " + victim + " is draining your energy.");
        }

        // Check if medicine is available
        if (inventory.getMedicine() > 0) {
            System.out.println("You use 1 medicine to treat the " + disease + ".");
            inventory.useMedicine(1);
            healthImpact /= 2; // Medicine cuts health impact in half
        } else {
            System.out.println("You have no medicine to treat the " + disease + ".");
        }

        // Apply health impact
        player.decreaseHealth(healthImpact);
        System.out.println("Health decreased by " + healthImpact + " points.");

        // Small chance of death if health is already low
        if (isPlayer && player.getHealth() < 20 && random.nextDouble() < 0.2) {
            player.setCauseOfDeath(disease);
            player.setDead(true);
        }
    }

    private void generateInjuryEvent() {
        System.out.println("\n=== INJURY ===");

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

        System.out.println(victim + " has suffered a " + injury + ".");

        // Health impact
        int healthImpact = 15 + random.nextInt(20); // 15-35 health impact

        // If it's a family member, less direct impact on player
        if (!isPlayer) {
            healthImpact = 5 + random.nextInt(10); // 5-15 health impact
            System.out.println("Caring for " + victim + " slows your travel.");
        }

        // Check if medicine is available
        if (inventory.getMedicine() > 0) {
            System.out.println("You use 1 medicine to treat the " + injury + ".");
            inventory.useMedicine(1);
            healthImpact /= 2; // Medicine cuts health impact in half
        } else {
            System.out.println("You have no medicine to treat the " + injury + ".");
        }

        // Apply health impact
        player.decreaseHealth(healthImpact);
        System.out.println("Health decreased by " + healthImpact + " points.");
    }

    private void generateWagonProblem() {
        System.out.println("\n=== WAGON PROBLEM ===");

        // Select random wagon problem
        String problem = wagonProblems.get(random.nextInt(wagonProblems.size()));

        System.out.println("Your wagon has a " + problem + ".");

        // Check if wagon parts are available
        if (inventory.getWagonParts() > 0) {
            System.out.println("You use 1 wagon part to fix the problem.");
            inventory.useWagonParts(1);
            System.out.println("The repair takes some time, but you're able to continue.");
        } else {
            System.out.println("You have no wagon parts to make repairs!");
            System.out.println("You'll have to improvise a solution, which takes time and energy.");

            // Health impact from working hard to fix wagon without parts
            int healthImpact = 5 + random.nextInt(10); // 5-15 health impact
            player.decreaseHealth(healthImpact);
            System.out.println("Health decreased by " + healthImpact + " points from the hard labor.");

            // Chance of permanent wagon damage
            if (random.nextDouble() < 0.3) {
                System.out.println("Your makeshift repair isn't as good as it should be.");
                System.out.println("Your wagon will now move a bit slower on the trail.");
                // This effect is simulated in the oxen health factor
                inventory.decreaseOxenHealth(10);
            }
        }
    }

    private void generatePositiveEvent() {
        System.out.println("\n=== GOOD FORTUNE ===");

        // Select random positive event
        String event = positiveEvents.get(random.nextInt(positiveEvents.size()));

        System.out.println("Good news! You " + event + ".");

        // Apply positive effect
        if (event.contains("berries")) {
            int foodGained = 5 + random.nextInt(10); // 5-15 pounds of food
            inventory.addFood(foodGained);
            System.out.println("You add " + foodGained + " pounds of food to your supplies.");
        } else if (event.contains("abandoned wagon")) {
            // Random assortment of supplies
            int foodGained = 10 + random.nextInt(30); // 10-40 pounds of food
            int partsGained = random.nextInt(2); // 0-1 parts
            int medicineGained = random.nextInt(2); // 0-1 medicine

            inventory.addFood(foodGained);
            inventory.addWagonParts(partsGained);
            inventory.addMedicine(medicineGained);

            System.out.println("You salvage:");
            System.out.println("- " + foodGained + " pounds of food");
            if (partsGained > 0) System.out.println("- " + partsGained + " wagon parts");
            if (medicineGained > 0) System.out.println("- " + medicineGained + " medicine");
        } else if (event.contains("Native Americans")) {
            int foodGained = 15 + random.nextInt(15); // 15-30 pounds of food
            inventory.addFood(foodGained);
            System.out.println("They share " + foodGained + " pounds of food with you.");
            System.out.println("They also show you a better route, saving you time.");
        } else if (event.contains("water source")) {
            System.out.println("Everyone in your party feels refreshed.");
            player.increaseHealth(10);
            System.out.println("Health increased by 10 points.");
        } else if (event.contains("hunting")) {
            int foodGained = 25 + random.nextInt(50); // 25-75 pounds of food
            int ammoUsed = 1 + random.nextInt(3); // 1-3 ammunition used

            if (inventory.getAmmunition() >= ammoUsed) {
                inventory.useAmmunition(ammoUsed);
                inventory.addFood(foodGained);
                System.out.println("You use " + ammoUsed + " ammunition and get " + foodGained + " pounds of food.");
            } else {
                System.out.println("Unfortunately, you don't have enough ammunition to hunt properly.");
                foodGained /= 3; // Much less food without ammo
                inventory.addFood(foodGained);
                System.out.println("You manage to trap a few small animals for " + foodGained + " pounds of food.");
            }
        }
    }

    private void generateWeatherEvent() {
        System.out.println("\n=== WEATHER EVENT ===");

        // Select random weather event
        String event = weatherEvents.get(random.nextInt(weatherEvents.size()));

        System.out.println("A " + event + " moves through the area.");

        // Different effects based on weather
        if (event.contains("hailstorm")) {
            System.out.println("The hail pelts your wagon and oxen.");
            inventory.decreaseOxenHealth(10);
            System.out.println("Oxen health decreased by 10 points.");

            if (random.nextDouble() < 0.2) {
                int foodLost = 5 + random.nextInt(10); // 5-15 pounds of food
                inventory.consumeFood(foodLost);
                System.out.println("Some of your food was damaged, losing " + foodLost + " pounds.");
            }
        } else if (event.contains("thunderstorm")) {
            System.out.println("The strong winds and rain drench everything.");
            int healthLost = 5 + random.nextInt(5); // 5-10 health points
            player.decreaseHealth(healthLost);
            System.out.println("Health decreased by " + healthLost + " points from exposure.");

            if (random.nextDouble() < 0.3) {
                System.out.println("The winds damaged your wagon cover.");
                if (inventory.getWagonParts() > 0) {
                    System.out.println("You use 1 wagon part to repair it.");
                    inventory.useWagonParts(1);
                } else {
                    int foodLost = 10 + random.nextInt(20); // 10-30 pounds of food
                    inventory.consumeFood(foodLost);
                    System.out.println("Without repairs, " + foodLost + " pounds of food was spoiled by rain.");
                }
            }
        } else if (event.contains("fog")) {
            System.out.println("The fog makes it impossible to see the trail clearly.");
            System.out.println("You have to slow down significantly to avoid getting lost.");
            // Travel impact handled in main game loop by weather system
        } else if (event.contains("snowfall")) {
            System.out.println("The unexpected snow makes travel difficult.");
            int healthLost = 5 + random.nextInt(10); // 5-15 health points
            player.decreaseHealth(healthLost);
            System.out.println("Health decreased by " + healthLost + " points from the cold.");
            inventory.decreaseOxenHealth(5);
            System.out.println("Oxen health decreased by 5 points.");
        } else if (event.contains("dust")) {
            System.out.println("The dust gets everywhere, in your food and water.");
            int healthLost = 3 + random.nextInt(7); // 3-10 health points
            player.decreaseHealth(healthLost);
            System.out.println("Health decreased by " + healthLost + " points from dust inhalation.");

            if (random.nextDouble() < 0.4) {
                int foodLost = 5 + random.nextInt(5); // 5-10 pounds of food
                inventory.consumeFood(foodLost);
                System.out.println(foodLost + " pounds of food is ruined by the dust.");
            }
        }
    }

    private void generateAnimalEvent() {
        System.out.println("\n=== ANIMAL ENCOUNTER ===");

        // Select random animal event
        String event = animalEvents.get(random.nextInt(animalEvents.size()));

        System.out.println("There's a " + event + "!");

        // Different effects based on animal
        if (event.contains("snake")) {
            if (random.nextDouble() < 0.3) {
                System.out.println("The snake bites " + player.getName() + "!");
                int healthLost = 15 + random.nextInt(15); // 15-30 health points

                if (inventory.getMedicine() > 0) {
                    System.out.println("You use 1 medicine to treat the bite.");
                    inventory.useMedicine(1);
                    healthLost /= 2;
                } else {
                    System.out.println("You have no medicine to treat the snakebite!");
                }

                player.decreaseHealth(healthLost);
                System.out.println("Health decreased by " + healthLost + " points.");
            } else {
                System.out.println("You manage to avoid the snake. That was close!");
            }
        } else if (event.contains("wolf")) {
            System.out.println("The wolves are eyeing your oxen.");

            if (inventory.getAmmunition() >= 2) {
                System.out.println("You fire 2 shots to scare them away.");
                inventory.useAmmunition(2);
                System.out.println("The wolves flee into the wilderness.");
            } else {
                System.out.println("Without ammunition to scare them, the wolves attack your oxen!");
                int oxenDamage = 15 + random.nextInt(15); // 15-30 oxen health
                inventory.decreaseOxenHealth(oxenDamage);
                System.out.println("Oxen health decreased by " + oxenDamage + " points.");

                if (random.nextDouble() < 0.2) {
                    System.out.println("One of your oxen is killed by the wolves!");
                    if (inventory.getOxen() > 0) {
                        inventory.addOxen(-1); // Lose an ox
                    }
                }
            }
        } else if (event.contains("bears")) {
            System.out.println("Bears have gotten into your food supply!");
            int foodLost = 20 + random.nextInt(30); // 20-50 pounds of food
            inventory.consumeFood(foodLost);
            System.out.println("You lose " + foodLost + " pounds of food.");

            if (inventory.getAmmunition() >= 3) {
                System.out.println("You fire 3 shots to drive the bears away.");
                inventory.useAmmunition(3);

                if (random.nextDouble() < 0.4) {
                    System.out.println("You manage to hit one of the bears!");
                    int foodGained = 40 + random.nextInt(60); // 40-100 pounds of food
                    inventory.addFood(foodGained);
                    System.out.println("You butcher the bear for " + foodGained + " pounds of food.");
                }
            }
        } else if (event.contains("oxen spooked")) {
            System.out.println("Your oxen are frightened and trying to run!");

            if (random.nextDouble() < 0.4) {
                System.out.println("You lose control of the wagon!");
                int wagonDamage = random.nextInt(2); // 0-1 wagon parts
                if (wagonDamage > 0) {
                    System.out.println("The wagon is damaged in the chaos.");

                    if (inventory.getWagonParts() >= wagonDamage) {
                        System.out.println("You use " + wagonDamage + " wagon parts for repairs.");
                        inventory.useWagonParts(wagonDamage);
                    } else {
                        System.out.println("You don't have enough parts for a proper repair.");
                        inventory.decreaseOxenHealth(10);
                        System.out.println("Oxen health decreased by 10 points from the strain of pulling a damaged wagon.");
                    }
                }

                int healthLost = 5 + random.nextInt(10); // 5-15 health points
                player.decreaseHealth(healthLost);
                System.out.println("You're battered during the incident. Health decreased by " + healthLost + " points.");
            } else {
                System.out.println("You manage to calm the oxen before any damage is done.");
            }
        } else if (event.contains("insects")) {
            System.out.println("The air is filled with biting insects!");
            int healthLost = 5 + random.nextInt(5); // 5-10 health points
            player.decreaseHealth(healthLost);
            System.out.println("Everyone is bitten and uncomfortable. Health decreased by " + healthLost + " points.");
            inventory.decreaseOxenHealth(5);
            System.out.println("Oxen health decreased by 5 points.");
        }
    }
} 