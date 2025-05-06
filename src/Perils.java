/**
 * Perils Class of the Perils Along the Platte Game
 * Manages random events and challenges that occur during the journey.
 * Handles various types of events including diseases, injuries, wagon problems,
 * positive events, weather events, animal encounters, and gender-specific events.
 * Provides a dynamic and unpredictable gameplay experience that affects the player's
 * health, morale, and resources.
 *
 * @author Alex Randall and Chase McCluskey
 * @version 1.0
 * @date 05/06/2025
 * @file Perils.java
 */

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

    /**
     * Constructs a new Perils manager with the specified game components.
     * @param player The player who will experience the events
     * @param inventory The inventory that may be affected by events
     * @param weather The weather system that may influence events
     * @param time The time system for tracking event timing
     */
    public Perils(Player player, Inventory inventory, Weather weather, Time time) {
        this.player = player;
        this.inventory = inventory;
        this.time = time;
        this.random = new Random();
        this.messageListener = s -> System.out.println(s); // Default to System.out
        
        initializeEvents();
    }
    
    /**
     * Sets a custom message listener to handle event notifications.
     * @param listener The consumer that will handle message notifications
     */
    public void setMessageListener(Consumer<String> listener) {
        this.messageListener = listener;
    }
    
    /**
     * Displays a message through the configured listener or System.out.
     * @param message The message to display
     */
    private void showMessage(String message) {
        if (messageListener != null) {
            messageListener.accept(message);
        } else {
            System.out.println(message);
        }
    }

    /**
     * Initializes all possible event types and their variations.
     * Creates lists of diseases, injuries, wagon problems, positive events,
     * weather events, animal encounters, and gender-specific events.
     * Includes job-specific variations for certain event types.
     */
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
        positiveEvents.add("found extra supplies left by another wagon");
        positiveEvents.add("met friendly natives who shared food");
        positiveEvents.add("found a clear stream with fresh fish");
        positiveEvents.add("found an abandoned wagon with usable parts");
        positiveEvents.add("weather conditions improved unexpectedly");
        positiveEvents.add("found a shortcut that saved time");
        positiveEvents.add("found a good campsite with plenty of resources");
        
        // Add farmer-specific food gathering events
        if (player.getJob() == Job.FARMER) {
            positiveEvents.add("identified edible wild plants others would miss");
            positiveEvents.add("successfully harvested wild grain");
            positiveEvents.add("found and harvested wild root vegetables");
            positiveEvents.add("expertly foraged for food in the area");
        }

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
        femaleSpecificEvents.add("increased family morale");
        
        // Initialize male-specific events
        maleSpecificEvents = new ArrayList<>();
        maleSpecificEvents.add("injured while hunting");
        maleSpecificEvents.add("back strain from heavy lifting");
        maleSpecificEvents.add("made extra money helping repair wagons");
        maleSpecificEvents.add("found better hunting grounds");
        maleSpecificEvents.add("successfully repaired wagon without parts");
    }

    /**
     * Generates a random event based on probability and player characteristics.
     * Events have a 30% chance of occurring each day.
     * Gender-specific events have a 15% chance if applicable.
     * Regular events are distributed among six categories:
     * 1. Diseases
     * 2. Injuries
     * 3. Wagon Problems
     * 4. Positive Events
     * 5. Weather Events
     * 6. Animal Events
     */
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
     * Generates an event specific to female characters.
     * Includes events like childbirth, women's council meetings,
     * supply management issues, and caregiving challenges.
     * Events can affect health, morale, and inventory.
     */
    private void generateFemaleSpecificEvent() {
        if (!player.getGender().equalsIgnoreCase("female")) return; // Only for female players

        String event = femaleSpecificEvents.get(random.nextInt(femaleSpecificEvents.size()));
        String message = "";
        int healthDamage = 0;
        int moraleLoss = 0;

        switch (event) {
            case "Difficult Childbirth":
                message = "Difficult Childbirth: Childbirth on the trail is perilous. Complications arise.";
                healthDamage = 30 + random.nextInt(40); // Significant health risk (30-70)
                moraleLoss = 20 + random.nextInt(20); // Significant morale impact (20-40)
                if (player.getHealth() - healthDamage <= 0) {
                    message += "\nYou died from complications during childbirth.";
                    player.decreaseHealth(healthDamage, "childbirth complications"); // Specific cause
                } else {
                    player.decreaseHealth(healthDamage);
                    player.decreaseMorale(moraleLoss);
                    message += "\nYou lost " + healthDamage + " health and " + moraleLoss + " morale, but survived.";
                }
                break;

            case "Women's Council":
                message = "Women's Council: The women gather to discuss trail matters and share wisdom.";
                int moraleGain = 5 + random.nextInt(10);
                player.increaseMorale(moraleGain);
                message += "\nSharing experiences boosts morale by " + moraleGain + ".";
                // Bonus: Chance to gain medicine knowledge
                if (player.getJob() == Job.DOCTOR || random.nextDouble() < 0.2) {
                    int medicineGain = random.nextInt(2);
                    if (medicineGain > 0) {
                        inventory.addMedicine(medicineGain);
                        message += "\nYou also learned about local remedies, gaining " + medicineGain + " medicine kit(s).";
                    }
                }
                break;

            case "Lost Supplies":
                message = "Lost Supplies: While managing the wagon, some crucial supplies shift and fall off.";
                int foodLost = 10 + random.nextInt(20); // 10-30 lbs food
                inventory.consumeFood(foodLost);
                moraleLoss = 5 + random.nextInt(10);
                player.decreaseMorale(moraleLoss);
                message += "\nYou lost " + foodLost + " lbs of food and " + moraleLoss + " morale.";
                // Chance to lose medicine
                if (random.nextDouble() < 0.2 && inventory.getMedicine() > 0) {
                    inventory.useMedicine(1);
                    message += "\nA medicine kit was also damaged.";
                }
                break;

            case "Unexpected Kindness":
                message = "Unexpected Kindness: Another family shares some excess supplies with you.";
                int foodGained = 5 + random.nextInt(15); // 5-20 lbs food
                inventory.addFood(foodGained);
                moraleGain = 5 + random.nextInt(10);
                player.increaseMorale(moraleGain);
                message += "\nYou received " + foodGained + " lbs of food. Morale increased by " + moraleGain + ".";
                break;
                
            case "Exhaustion from Caregiving":
                message = "Exhaustion from Caregiving: Tending to sick family members and daily chores takes its toll.";
                healthDamage = 5 + random.nextInt(10); // 5-15 health damage
                moraleLoss = 5 + random.nextInt(10); // 5-15 morale loss
                if (player.getHealth() - healthDamage <= 0) {
                    message += "\nThe constant strain was too much. You died from exhaustion.";
                    player.decreaseHealth(healthDamage, "exhaustion"); // Specific cause
                } else {
                    player.decreaseHealth(healthDamage);
                    player.decreaseMorale(moraleLoss);
                    message += "\nYou lost " + healthDamage + " health and " + moraleLoss + " morale.";
                }
                break;
                
            case "Mending Clothes":
                 message = "Mending Clothes: You spend the evening mending clothes for the family.";
                 moraleGain = 3 + random.nextInt(5); // Small morale boost 3-8
                 player.increaseMorale(moraleGain);
                 message += "\nTaking care of necessities provides a small comfort. Morale increased by " + moraleGain + ".";
                 // Small chance to prevent minor negative event later? (Could be complex)
                 break;
                 
             case "Gathering Herbs":
                 message = "Gathering Herbs: You recognize some medicinal plants growing nearby.";
                 int medicineFound = random.nextInt(2); // 0 or 1 medicine kit
                 if (player.getJob() == Job.DOCTOR || random.nextDouble() < 0.3) { // Higher chance if Doctor
                     medicineFound++;
                 }
                 if (medicineFound > 0) {
                     inventory.addMedicine(medicineFound);
                     message += "\nYour knowledge helps you gather enough for " + medicineFound + " medicine kit(s).";
                 } else {
                     message += "\nYou search but don't find enough useful herbs.";
                 }
                 break;

            default:
                message = "An unknown event specific to women occurred: " + event;
                break;
        }

        showMessage(message);
    }
    
    /**
     * Generates an event specific to male characters.
     * Includes events like hunting injuries, physical strain,
     * repair opportunities, and hunting success.
     * Events can affect health, morale, and inventory.
     */
    private void generateMaleSpecificEvent() {
        if (!player.getGender().equalsIgnoreCase("male")) return; // Only for male players

        String event = maleSpecificEvents.get(random.nextInt(maleSpecificEvents.size()));
        String message = "";
        int healthDamage = 0;
        int moraleLoss = 0;

        switch (event) {
            case "Hunting Accident":
                message = "Hunting Accident: While hunting, your firearm misfires or you trip.";
                healthDamage = 15 + random.nextInt(30); // Moderate injury (15-45)
                moraleLoss = 10 + random.nextInt(15); // Morale loss (10-25)
                if (player.getHealth() - healthDamage <= 0) {
                    message += "\nThe accident was fatal. You died from your injuries.";
                    player.decreaseHealth(healthDamage, "accident"); // Specific cause
                } else {
                    player.decreaseHealth(healthDamage);
                    player.decreaseMorale(moraleLoss);
                    message += "\nYou are injured, losing " + healthDamage + " health and " + moraleLoss + " morale.";
                    // Lose some ammo
                    if (inventory.getAmmunition() > 0) {
                        int ammoLost = 5 + random.nextInt(10);
                        inventory.useAmmunition(Math.min(ammoLost, inventory.getAmmunition()));
                        message += "\nYou also lost " + Math.min(ammoLost, inventory.getAmmunition()) + " rounds of ammunition.";
                    }
                }
                break;

            case "Scouting Duty":
                message = "Scouting Duty: You volunteer to scout ahead for the wagon train.";
                // Chance of positive or negative outcome
                if (random.nextDouble() < 0.6) { // Positive outcome (60% chance)
                    int moraleGain = 5 + random.nextInt(10);
                    player.increaseMorale(moraleGain);
                    message += "\nYou find a good campsite or water source. Morale increased by " + moraleGain + ".";
                    // Chance to find something useful
                    if (random.nextDouble() < 0.2) {
                        int foodFound = 5 + random.nextInt(10);
                        inventory.addFood(foodFound);
                        message += "\nYou also find " + foodFound + " lbs of edible plants.";
                    }
                } else { // Negative outcome (40% chance)
                    moraleLoss = 5 + random.nextInt(10);
                    player.decreaseMorale(moraleLoss);
                    message += "\nYou get lost temporarily or encounter difficult terrain. Morale decreased by " + moraleLoss + ".";
                    // Chance of minor injury
                    if (random.nextDouble() < 0.2) {
                         healthDamage = 5 + random.nextInt(10); // Minor (5-15)
                         if (player.getHealth() - healthDamage <= 0) {
                             message += "\nYou succumbed to your injuries while lost.";
                             player.decreaseHealth(healthDamage, "exposure"); // Specific cause
                         } else {
                             player.decreaseHealth(healthDamage);
                             message += "\nYou suffer minor injuries, losing " + healthDamage + " health.";
                         }
                    }
                }
                break;

            case "Wagon Repair Duty":
                message = "Wagon Repair Duty: You spend hours working on maintaining the wagon.";
                healthDamage = 3 + random.nextInt(7); // Minor exertion (3-10)
                player.decreaseHealth(healthDamage);
                message += "\nThe hard labor takes a small toll. Health decreased by " + healthDamage + ".";
                // Chance to prevent a future wagon problem
                // (This could be implemented with a temporary player status flag)
                if (player.getJob() == Job.BLACKSMITH || player.getJob() == Job.CARPENTER || random.nextDouble() < 0.3) {
                    message += "\nYour efforts might prevent future breakdowns.";
                }
                break;

            case "Argument with Traveler":
                message = "Argument with Traveler: A disagreement breaks out with another member of the train.";
                moraleLoss = 10 + random.nextInt(15); // Moderate morale loss (10-25)
                player.decreaseMorale(moraleLoss);
                message += "\nThe conflict lowers spirits. Morale decreased by " + moraleLoss + ".";
                // Chance of escalation (small chance of injury)
                if (random.nextDouble() < 0.1) {
                    healthDamage = 10 + random.nextInt(15); // Minor fight injury (10-25)
                     if (player.getHealth() - healthDamage <= 0) {
                         message += "\nThe argument turned violent, and you died from your injuries.";
                         player.decreaseHealth(healthDamage, "accident"); // Or potentially "violence"
                     } else {
                         player.decreaseHealth(healthDamage);
                         message += "\nThings got heated, resulting in minor injuries. Lost " + healthDamage + " health.";
                     }
                }
                break;
                
            case "Guard Duty":
                 message = "Guard Duty: You stand watch overnight to protect the camp.";
                 healthDamage = 2 + random.nextInt(5); // Minor fatigue (2-7)
                 player.decreaseHealth(healthDamage);
                 moraleLoss = 2 + random.nextInt(5); // Minor morale hit (2-7)
                 player.decreaseMorale(moraleLoss);
                 message += "\nThe long night leaves you tired. Lost " + healthDamage + " health and " + moraleLoss + " morale.";
                 // Chance to prevent theft (positive outcome)
                 if (random.nextDouble() < 0.2) {
                     message += "\nYour vigilance prevented a potential theft during the night.";
                     // Could add a small morale boost here for success?
                     player.increaseMorale(5);
                     message += "\nMorale increased slightly due to your successful watch.";
                 }
                 break;
                 
             case "River Crossing Assistance":
                 message = "River Crossing Assistance: You help guide wagons across a treacherous river.";
                 // Risk of injury or success
                 if (random.nextDouble() < 0.25) { // 25% chance of injury
                     healthDamage = 10 + random.nextInt(20); // 10-30 health damage
                     if (player.getHealth() - healthDamage <= 0) {
                         message += "\nYou slipped during the crossing and drowned.";
                         player.decreaseHealth(healthDamage, "drowning"); // Specific cause
                     } else {
                         player.decreaseHealth(healthDamage);
                         message += "\nIt was dangerous work! You were injured, losing " + healthDamage + " health.";
                     }
                 } else { // 75% chance of success
                     int moraleGain = 5 + random.nextInt(10);
                     player.increaseMorale(moraleGain);
                     message += "\nYou successfully helped others cross. Morale increased by " + moraleGain + ".";
                 }
                 break;

            default:
                message = "An unknown event specific to men occurred: " + event;
                break;
        }

        showMessage(message);
    }

    /**
     * Generates a disease-related event.
     * Diseases can range from cholera to morale depreciation.
     * Effects vary based on the disease type and player's health.
     * May require medicine to treat.
     */
    private void generateDiseaseEvent() {
        String disease = diseases.get(random.nextInt(diseases.size()));
        String message = "Disease strikes! You have contracted " + disease + ".";
        int healthLost = 0;
        int medicineNeeded = 1;

        switch (disease) {
            case "cholera":
            case "dysentery":
                healthLost = 20 + random.nextInt(30); // 20-50 health loss
                medicineNeeded = 2;
                break;
            case "typhoid":
            case "measles":
                healthLost = 15 + random.nextInt(25); // 15-40 health loss
                break;
            case "fever":
                healthLost = 10 + random.nextInt(20); // 10-30 health loss
                break;
            default: // Generic illness
                healthLost = 10 + random.nextInt(15); // 10-25 health loss
                break;
        }

        // Apply Doctor job bonus to reduce severity
        double docBonus = player.getDoctorModifier(); // Assuming 1.2 for doctor
        if (player.getJob() == Job.DOCTOR) {
            healthLost = (int) (healthLost / docBonus); // Reduce health loss
            medicineNeeded = Math.max(1, medicineNeeded - 1); // Reduce medicine needed
        }
        
        // Check if player has medicine
        if (inventory.getMedicine() >= medicineNeeded) {
            inventory.useMedicine(medicineNeeded);
            int healthRecovered = (int)(healthLost * (0.5 + random.nextDouble() * 0.5)); // Recover 50-100% of damage
            healthLost -= healthRecovered;
            message += "\nFortunately, you had " + medicineNeeded + " medicine kit(s). You used it and recovered some health.";
            
            // Ensure healthLost doesn't become negative if recovery is high
            healthLost = Math.max(0, healthLost); 
        } else {
            message += "\nYou have no medicine to treat the illness!";
        }
        
        message += "\nYou lose " + healthLost + " health.";
        
        if (player.getHealth() - healthLost <= 0) {
            message += "\nThe " + disease + " proved fatal.";
            // Pass the specific disease name as the cause of death
            player.decreaseHealth(healthLost, disease);
        } else {
            player.decreaseHealth(healthLost);
        }

        showMessage(message);
    }

    /**
     * Generates an injury-related event.
     * Injuries can range from broken bones to cuts and concussions.
     * Effects vary based on the injury type and severity.
     * May require medicine to treat.
     */
    private void generateInjuryEvent() {
        String injury = injuries.get(random.nextInt(injuries.size()));
        String message = "Injury! You suffered a " + injury + ".";
        int healthLost = 0;
        boolean needsMedicine = false;

        switch (injury) {
            case "broken leg":
            case "broken arm":
                healthLost = 25 + random.nextInt(25); // 25-50 health loss
                needsMedicine = true;
                break;
            case "sprained ankle":
            case "deep cut":
                healthLost = 10 + random.nextInt(15); // 10-25 health loss
                needsMedicine = random.nextDouble() < 0.5; // 50% chance needs medicine
                break;
            case "concussion":
                healthLost = 15 + random.nextInt(20); // 15-35 health loss
                needsMedicine = true;
                break;
            default: // Minor injury
                healthLost = 5 + random.nextInt(10); // 5-15 health loss
                break;
        }

        if (needsMedicine) {
            if (inventory.getMedicine() > 0) {
                inventory.useMedicine(1);
                int healthRecovered = (int)(healthLost * (0.3 + random.nextDouble() * 0.4)); // Recover 30-70%
                healthLost -= healthRecovered;
                message += "\nYou used a medicine kit to treat the " + injury + ".";
                healthLost = Math.max(0, healthLost); // Prevent negative health loss
            } else {
                message += "\nYou have no medicine to properly treat the " + injury + "!";
                healthLost += 5 + random.nextInt(10); // Penalty for no medicine
            }
        }

        message += "\nYou lose " + healthLost + " health.";
        
        if (player.getHealth() - healthLost <= 0) {
            message += "\nThe " + injury + " led to complications, and you died.";
            // Pass the specific injury as the cause
            player.decreaseHealth(healthLost, injury); 
        } else {
            player.decreaseHealth(healthLost);
        }

        showMessage(message);
    }

    /**
     * Generates a wagon-related problem.
     * Problems can include broken parts, getting stuck, or loose components.
     * Effects vary based on the problem type and available parts.
     * May require wagon parts to fix.
     */
    private void generateWagonProblem() {
        String problem = wagonProblems.get(random.nextInt(wagonProblems.size()));
        String message = "Wagon Problem: " + problem + ".";
        int partsNeeded = 0;
        String partType = "";
        int delayDays = 0;
        int healthDamage = 0; // Potential health damage from exertion or accident
        int moraleLoss = 5 + random.nextInt(10); // Base morale loss

        switch (problem) {
            case "Broken Wheel":
                partsNeeded = 1;
                partType = "wheel";
                delayDays = 1 + random.nextInt(2); // 1-2 days delay
                moraleLoss += 5;
                break;
            case "Broken Axle":
                partsNeeded = 1;
                partType = "axle";
                delayDays = 2 + random.nextInt(2); // 2-3 days delay
                moraleLoss += 10;
                break;
            case "Broken Tongue":
                partsNeeded = 1;
                partType = "tongue";
                delayDays = 1 + random.nextInt(2); // 1-2 days delay
                moraleLoss += 8;
                break;
            case "Torn Wagon Cover":
                // Doesn't require parts, but causes delay and potential spoilage
                delayDays = 1;
                moraleLoss += 3;
                message += "\nSupplies are exposed to the elements.";
                // Simulate extra spoilage
                if (inventory.getFood() > 0) {
                     int spoiledFood = (int)(inventory.getFood() * (0.05 + random.nextDouble() * 0.05)); // 5-10% spoilage
                     inventory.consumeFood(spoiledFood);
                     message += "\nLost " + spoiledFood + " lbs of food due to exposure.";
                }
                break;
            case "Oxen Injury":
                delayDays = 1 + random.nextInt(3); // 1-3 days delay
                int oxenHealthLoss = 10 + random.nextInt(20); // 10-30 health loss
                inventory.decreaseOxenHealth(oxenHealthLoss);
                moraleLoss += 10;
                message += "\nOne of your oxen is injured. Oxen health decreased by " + oxenHealthLoss + "%.";
                break;
            case "Wagon Stuck in Mud":
                delayDays = 1;
                moraleLoss += 5;
                healthDamage = 5 + random.nextInt(10); // Exertion (5-15)
                message += "\nIt takes significant effort to free the wagon.";
                break;
            case "Lost Supplies from Wagon":
                 delayDays = 0; // No delay, just loss
                 moraleLoss += 8;
                 message += "\nSome supplies shifted and fell off the wagon.";
                 // Lose random items
                 int foodLost = 10 + random.nextInt(20);
                 inventory.consumeFood(foodLost);
                 message += "\nLost " + foodLost + " lbs of food.";
                 if (random.nextDouble() < 0.3 && inventory.getMedicine() > 0) {
                     inventory.useMedicine(1);
                     message += "\nLost 1 medicine kit.";
                 }
                 if (random.nextDouble() < 0.2 && inventory.getAmmunition() >= 20) {
                     inventory.useAmmunition(20);
                     message += "\nLost 1 box of ammunition.";
                 }
                 break;
            case "Overturned Wagon":
                 partsNeeded = 1 + random.nextInt(2); // 1-2 parts needed
                 delayDays = 2 + random.nextInt(3); // 2-4 days delay
                 moraleLoss += 20;
                 healthDamage = 10 + random.nextInt(20); // Accident injury (10-30)
                 message += "\nThe wagon overturned! Major repairs needed.";
                 // Lose more significant supplies
                 foodLost = 50 + random.nextInt(100); // 50-150 lbs food
                 inventory.consumeFood(foodLost);
                 message += "\nLost " + foodLost + " lbs of food.";
                 if (inventory.getMedicine() > 0) {
                     int medLost = 1 + random.nextInt(inventory.getMedicine() + 1); // Lose 1 to all medicine
                     inventory.useMedicine(medLost);
                     message += "\nLost " + medLost + " medicine kit(s).";
                 }
                 if (inventory.getAmmunition() > 0) {
                     int ammoLost = 20 + random.nextInt(inventory.getAmmunition() / 2 + 1); // Lose 20 to half ammo
                     inventory.useAmmunition(ammoLost);
                     message += "\nLost " + ammoLost + " rounds of ammunition.";
                 }
                 // Higher chance of part needed being critical (axle/wheel)
                 if (random.nextDouble() < 0.6) partType = (random.nextBoolean() ? "axle" : "wheel");
                 else partType = (random.nextBoolean() ? "tongue" : "bow");

                 break;
            default:
                message += "\nRepairs might be needed.";
                break;
        }

        // Apply Blacksmith/Carpenter job bonus
        boolean hasBonus = player.getJob() == Job.BLACKSMITH || player.getJob() == Job.CARPENTER;
        if (hasBonus) {
            delayDays = Math.max(0, delayDays - 1); // Reduce delay by 1 day
            moraleLoss = Math.max(0, moraleLoss - 5); // Reduce morale loss
        }

        if (partsNeeded > 0 && !partType.isEmpty()) {
            message += "\nYou need " + partsNeeded + " spare " + partType + (partsNeeded > 1 ? "s" : "") + " for repairs.";
            int availableParts = 0;
            switch (partType) {
                case "wheel": availableParts = inventory.getWheels(); break;
                case "axle": availableParts = inventory.getAxles(); break;
                case "tongue": availableParts = inventory.getTongues(); break;
                case "bow": availableParts = inventory.getWagonBows(); break; // Assuming 'bow' corresponds to Wagon Bows
            }

            if (availableParts >= partsNeeded) {
                // Use parts
                 switch (partType) {
                     case "wheel": inventory.useWheels(partsNeeded); break;
                     case "axle": inventory.useAxles(partsNeeded); break;
                     case "tongue": inventory.useTongues(partsNeeded); break;
                     case "bow": inventory.useWagonBows(partsNeeded); break;
                 }
                 message += "\nYou have the necessary parts and make the repairs.";
                 if (hasBonus) message += "\nYour skills speed up the process.";
            } else {
                 message += "\nYou don't have enough spare parts!";
                 delayDays += 2 + random.nextInt(3); // Extra delay scavenging/trading
                 moraleLoss += 15;
                 message += "\nSignificant delay while you try to find or trade for parts.";
                 // Chance of abandoning items to lighten load?
                 if (random.nextDouble() < 0.3) {
                     int foodAbandoned = 20 + random.nextInt(30);
                     inventory.consumeFood(foodAbandoned);
                     message += "\nTo proceed, you abandon " + foodAbandoned + " lbs of food.";
                 }
            }
        }

        if (delayDays > 0) {
            message += "\nYou are delayed by " + delayDays + " day(s).";
            for (int i = 0; i < delayDays; i++) {
                time.advanceDay();
                // Consume food during delay
                int foodConsumed = player.getFamilySize() * 2;
                inventory.consumeFood(foodConsumed);
                 message += "\n(Consumed " + foodConsumed + " lbs food during delay)";
            }
        }
        
        player.decreaseMorale(moraleLoss);
        message += "\nMorale decreases by " + moraleLoss + ".";
        
        if (healthDamage > 0) {
            if (player.getHealth() - healthDamage <= 0) {
                message += "\nYou died from injuries sustained during the wagon incident.";
                 // Specific cause based on the problem
                String deathCause = problem.contains("Overturned") || problem.contains("Stuck") ? "accident" : "exhaustion";
                player.decreaseHealth(healthDamage, deathCause);
            } else {
                 player.decreaseHealth(healthDamage);
                 message += "\nThe incident causes " + healthDamage + " health damage.";
            }
        }

        showMessage(message);
    }

    /**
     * Generates a positive event that benefits the player.
     * Events can include finding supplies, meeting helpful people,
     * discovering resources, or finding shortcuts.
     * Effects typically improve morale and may add resources.
     */
    private void generatePositiveEvent() {
        String event = positiveEvents.get(random.nextInt(positiveEvents.size()));
        String message = "Good Fortune: " + event + ".";
        int moraleGain = 0;
        int healthGain = 0;

        switch (event) {
            case "Found Wild Berries":
                int foodGained = 10 + random.nextInt(20); // 10-30 lbs
                inventory.addFood(foodGained);
                moraleGain = 5 + random.nextInt(10);
                message += "\nYou find " + foodGained + " lbs of edible berries.";
                break;
            case "Clear Skies Ahead":
                moraleGain = 10 + random.nextInt(10);
                message += "\nThe weather is perfect for travel, lifting everyone's spirits.";
                // Could add a temporary small speed boost?
                break;
            case "Met Friendly Natives":
                moraleGain = 15 + random.nextInt(10);
                message += "\nYou have a peaceful encounter with local Native Americans.";
                // Chance of trade or gaining supplies
                if (random.nextDouble() < 0.4) {
                    if (random.nextBoolean() && inventory.getFood() > 20) {
                        inventory.consumeFood(10);
                        inventory.addMedicine(1);
                        message += "\nYou trade 10 lbs of food for a medicine kit.";
                    } else if (inventory.getAmmunition() > 10) {
                         inventory.useAmmunition(10);
                         int foodTraded = 15 + random.nextInt(15);
                         inventory.addFood(foodTraded);
                         message += "\nYou trade 10 rounds of ammunition for " + foodTraded + " lbs of dried meat.";
                    } else {
                         message += "\nThey share some knowledge about the trail ahead.";
                    }
                }
                break;
            case "Successful Hunt (Minor)":
                // Check if player has ammo first
                if (inventory.getAmmunition() > 0) {
                    int ammoUsed = 1 + random.nextInt(2);
                    inventory.useAmmunition(Math.min(ammoUsed, inventory.getAmmunition()));
                    foodGained = 15 + random.nextInt(25); // 15-40 lbs (rabbit, bird)
                    inventory.addFood(foodGained);
                    moraleGain = 8 + random.nextInt(7);
                    message += "\nA quick hunt yields " + foodGained + " lbs of small game. Used " + Math.min(ammoUsed, inventory.getAmmunition()) + " ammo.";
                } else {
                    message = "Good Fortune: You spot game, but have no ammunition to hunt.";
                    moraleGain = -2; // Slight disappointment
                }
                break;
            case "Found Lost Item":
                 moraleGain = 5 + random.nextInt(5);
                 message += "\nYou find a small valuable item lost by a previous traveler.";
                 // Small money gain
                 int moneyFound = 5 + random.nextInt(10);
                 player.addMoney(moneyFound);
                 message += "\nIt fetches $" + moneyFound + " at the next trading post (added to your funds now).";
                 break;
             case "Restful Night":
                 healthGain = 5 + random.nextInt(10); // 5-15 health
                 moraleGain = 5 + random.nextInt(10); // 5-15 morale
                 message += "\nEveryone gets a particularly good night's sleep.";
                 // Also slightly recover oxen health
                 inventory.increaseOxenHealth(3 + random.nextInt(5));
                 message += "\nOxen also seem more rested.";
                 break;
             case "Inspiring Sermon (if Preacher)":
                 if (player.getJob() == Job.PREACHER) {
                     moraleGain = 15 + random.nextInt(15); // Significant boost (15-30)
                     message = "Good Fortune: Your words inspire the wagon train.";
                     message += "\nMorale significantly increases for everyone.";
                 } else {
                     // Reroll event if not preacher
                     generatePositiveEvent();
                     return; // Avoid double message
                 }
                 break;
             case "Good Fishing Spot":
                 moraleGain = 5 + random.nextInt(8);
                 foodGained = 5 + random.nextInt(15); // 5-20 lbs fish
                 inventory.addFood(foodGained);
                 message += "\nYou stop by a river and catch " + foodGained + " lbs of fish.";
                 break;

            default:
                moraleGain = 5;
                break;
        }

        player.increaseMorale(moraleGain);
        player.increaseHealth(healthGain);
        message += "\nMorale increases by " + moraleGain + "." + (healthGain > 0 ? " Health increases by " + healthGain + "." : "");

        showMessage(message);
    }
    
    /**
     * Generates a weather-related event.
     * Events can include storms, fog, snow, or dust storms.
     * Effects vary based on the weather type and severity.
     * May affect travel speed and resource consumption.
     */
    private void generateWeatherEvent() {
        String event = weatherEvents.get(random.nextInt(weatherEvents.size()));
        String message = "Weather Event: " + event + ".";
        int delayDays = 0;
        int healthDamage = 0;
        int moraleLoss = 0;
        int foodSpoilagePercent = 0;
        boolean partDamage = false;
        String deathCause = "exposure"; // Default cause for weather deaths

        switch (event) {
            case "Thunderstorm":
                moraleLoss = 5 + random.nextInt(10);
                delayDays = random.nextInt(2); // 0-1 day delay
                message += "\nA fierce storm rolls in.";
                // Small chance of lightning strike
                if (random.nextDouble() < 0.05) {
                    healthDamage = 50 + random.nextInt(51); // High damage (50-100)
                    message += "\nDisaster! Lightning strikes near your wagon!";
                    deathCause = "lightning strike";
                } else {
                    healthDamage = 3 + random.nextInt(7); // Minor exposure/fatigue (3-10)
                    foodSpoilagePercent = 5; // 5% spoilage
                    partDamage = random.nextDouble() < 0.1; // 10% chance minor part damage
                }
                break;

            case "Heavy Fog":
                moraleLoss = 3 + random.nextInt(5);
                delayDays = 1; // Usually causes a delay
                message += "\nThick fog blankets the trail, making travel impossible.";
                healthDamage = 2 + random.nextInt(4); // Minor fatigue from waiting (2-6)
                break;

            case "Hail Storm":
                moraleLoss = 10 + random.nextInt(10);
                delayDays = random.nextInt(2); // 0-1 day delay
                message += "\nLarge hailstones batter your wagon.";
                healthDamage = 5 + random.nextInt(10); // Minor injuries from hail (5-15)
                partDamage = random.nextDouble() < 0.3; // 30% chance of part damage (cover/bows)
                // Damage oxen slightly
                inventory.decreaseOxenHealth(5 + random.nextInt(10));
                message += "\nOxen are bruised by the hail.";
                deathCause = "accident"; // If somehow fatal
                break;

            case "Blizzard":
                moraleLoss = 20 + random.nextInt(15); // High morale loss (20-35)
                delayDays = 2 + random.nextInt(4); // Significant delay (2-5 days)
                message += "\nA blinding blizzard traps you in place.";
                healthDamage = 15 + random.nextInt(20); // Significant cold exposure (15-35)
                foodSpoilagePercent = 10; // Higher spoilage
                deathCause = "exposure to cold";
                 // High chance of oxen health loss
                if (random.nextDouble() < 0.7) {
                    int oxenDamage = 15 + random.nextInt(20);
                    inventory.decreaseOxenHealth(oxenDamage);
                    message += "\nOxen suffer from the extreme cold, health decreased by " + oxenDamage + "%.";
                }
                break;

            case "Extreme Heat":
                moraleLoss = 10 + random.nextInt(10);
                message += "\nScorching heat beats down on the trail.";
                healthDamage = 10 + random.nextInt(15); // Heat exhaustion (10-25)
                delayDays = random.nextInt(2); // Potential delay looking for water/shade (0-1)
                foodSpoilagePercent = 15; // High spoilage in heat
                deathCause = "heat stroke";
                // Increase oxen fatigue/health loss
                 int oxenDamage = 10 + random.nextInt(15);
                 inventory.decreaseOxenHealth(oxenDamage);
                 message += "\nOxen struggle in the heat, health decreased by " + oxenDamage + "%.";
                break;

            case "Dust Storm":
                moraleLoss = 8 + random.nextInt(7);
                delayDays = 1;
                message += "\nA choking dust storm reduces visibility and makes breathing difficult.";
                healthDamage = 5 + random.nextInt(10); // Respiratory issues/fatigue (5-15)
                foodSpoilagePercent = 5;
                deathCause = "exhaustion";
                break;
                
            case "Sudden Freeze":
                 moraleLoss = 10 + random.nextInt(10);
                 message += "\nAn unexpected cold snap hits overnight.";
                 healthDamage = 8 + random.nextInt(12); // Cold exposure (8-20)
                 delayDays = random.nextInt(2); // 0-1 day delay
                 foodSpoilagePercent = 5; // Less spoilage than blizzard but still risk
                 deathCause = "exposure to cold";
                 // Chance of damaging fragile items? (Less likely than parts)
                 break;
                 
             case "Flooding River": // Could happen away from main crossing
                 moraleLoss = 15 + random.nextInt(10);
                 delayDays = 1 + random.nextInt(3); // 1-3 days delay waiting for water to subside
                 message += "\nHeavy rains cause a nearby creek to flood, blocking the path.";
                 healthDamage = 5 + random.nextInt(5); // Fatigue from waiting/finding detour
                 // Chance of losing items if camped too close
                 if (random.nextDouble() < 0.2) {
                     int foodWashedAway = 10 + random.nextInt(20);
                     inventory.consumeFood(foodWashedAway);
                     message += "\nSome supplies stored near the ground were washed away! Lost " + foodWashedAway + " lbs food.";
                 }
                 break;

            default:
                message += "\nThe weather takes a turn.";
                moraleLoss = 5;
                break;
        }

        // Apply delay
        if (delayDays > 0) {
            message += "\nYou are delayed by " + delayDays + " day(s).";
            for (int i = 0; i < delayDays; i++) {
                time.advanceDay();
                int foodConsumed = player.getFamilySize() * 2;
                inventory.consumeFood(foodConsumed);
                message += "\n(Consumed " + foodConsumed + " lbs food during delay)";
                // Health can decline further during severe weather delay
                if (healthDamage > 10 && random.nextDouble() < 0.3) { // Extra damage if already significant
                     int extraDamage = 2 + random.nextInt(5);
                     healthDamage += extraDamage;
                     message += "\n(Conditions worsen, +" + extraDamage + " health damage)";
                }
            }
        }

        // Apply food spoilage
        if (foodSpoilagePercent > 0 && inventory.getFood() > 0) {
            int spoiledFood = (int)(inventory.getFood() * (foodSpoilagePercent / 100.0));
            inventory.consumeFood(spoiledFood);
            message += "\n" + spoiledFood + " lbs of food spoiled due to the weather.";
        }
        
        // Apply part damage
        if (partDamage) {
            String brokenPart = inventory.repairRandomBrokenPart(); // Let's assume this simulates damage for now
            if (brokenPart != null) {
                 message += "\nThe harsh weather damaged a wagon " + brokenPart + ".";
                 // TODO: Need a better damage system than using repair method
            } else if (inventory.getWagonParts() > 0) {
                // If repair didn't work but parts exist, maybe damage one specifically?
                inventory.useWagonParts(1); // Use a random part
                message += "\nThe harsh weather damaged a wagon part.";
            }
        }

        player.decreaseMorale(moraleLoss);
        message += "\nMorale decreases by " + moraleLoss + ".";

        if (healthDamage > 0) {
            if (player.getHealth() - healthDamage <= 0) {
                message += "\nYou succumbed to the harsh conditions (" + deathCause + ").";
                player.decreaseHealth(healthDamage, deathCause); // Use specific cause
            } else {
                player.decreaseHealth(healthDamage);
                message += "\nThe weather causes " + healthDamage + " health damage.";
            }
        }

        showMessage(message);
    }

    /**
     * Generates an animal-related event.
     * Events can include dangerous encounters, food theft,
     * or oxen disturbances.
     * Effects vary based on the animal type and situation.
     * May affect health, morale, and resources.
     */
    private void generateAnimalEvent() {
        String event = animalEvents.get(random.nextInt(animalEvents.size()));
        String message = "Animal Encounter: " + event + ".";
        int healthDamage = 0;
        int moraleChange = 0; // Can be positive or negative
        int ammoUsed = 0;
        String deathCause = "animal attack"; // Default for attacks

        switch (event) {
            case "Snakebite":
                message += "\nA venomous snake bites you!";
                healthDamage = 30 + random.nextInt(40); // Serious health impact (30-70)
                moraleChange = -15 - random.nextInt(10); // Significant fear/morale loss
                deathCause = "snakebite";
                // Check for medicine
                if (inventory.getMedicine() > 0) {
                    inventory.useMedicine(1);
                    int healthRecovered = (int)(healthDamage * (0.4 + random.nextDouble() * 0.4)); // Recover 40-80%
                    healthDamage -= healthRecovered;
                    message += "\nYou quickly use a medicine kit as an antivenom.";
                    healthDamage = Math.max(0, healthDamage); // Prevent negative health loss
                } else {
                    message += "\nYou have no medicine to treat the bite!";
                    healthDamage += 10 + random.nextInt(15); // Penalty
                }
                break;

            case "Wolf Pack Nearby":
                message += "\nA pack of wolves is spotted shadowing the wagon train.";
                moraleChange = -10 - random.nextInt(10); // Fear
                // Chance of attack if low on ammo or traveling slow?
                if (inventory.getAmmunition() < 20 && random.nextDouble() < 0.3) {
                     message += "\nSeeing weakness, the wolves attack!";
                     healthDamage = 15 + random.nextInt(20); // Moderate attack (15-35)
                     ammoUsed = Math.min(inventory.getAmmunition(), 5 + random.nextInt(10)); // Use 5-15 ammo or less if not available
                     inventory.useAmmunition(ammoUsed);
                     message += "\nYou fend them off, using " + ammoUsed + " rounds.";
                     deathCause = "wolf attack"; // More specific
                } else {
                    message += "\nThey keep their distance for now.";
                }
                break;

            case "Bison Stampede":
                message += "\nA massive bison herd stampedes nearby!";
                moraleChange = -5 - random.nextInt(10); // Fear and awe
                // Chance of wagon damage or injury
                if (random.nextDouble() < 0.15) {
                     message += "\nYour wagon is caught on the edge of the stampede!";
                     healthDamage = 20 + random.nextInt(30); // Significant injury (20-50)
                     // Damage wagon parts
                     int partsDamaged = 1 + random.nextInt(2);
                     inventory.useWagonParts(partsDamaged);
                     message += "\n" + partsDamaged + " wagon part(s) were damaged.";
                     // Damage oxen
                     inventory.decreaseOxenHealth(20 + random.nextInt(20));
                     message += "\nOxen were injured in the chaos.";
                     deathCause = "accident";
                } else {
                     message += "\nYou manage to steer clear of the main herd.";
                }
                break;

            case "Bear Sighting":
                 message += "\nA large bear is seen near the camp.";
                 moraleChange = -8 - random.nextInt(7); // Fear
                 // Chance of raid if food is not secured? (Simplification: random chance)
                 if (random.nextDouble() < 0.2) {
                      message += "\nThe bear raids your camp during the night!";
                      int foodStolen = 20 + random.nextInt(30); // 20-50 lbs food
                      inventory.consumeFood(foodStolen);
                      message += "\nIt stole " + foodStolen + " lbs of food.";
                      // Chance of injury if someone confronts it
                      if (random.nextDouble() < 0.2) {
                           healthDamage = 25 + random.nextInt(25); // Bear attack (25-50)
                           message += "\nSomeone was injured trying to scare it off!";
                           deathCause = "bear attack";
                      }
                 } else {
                      message += "\nIt eventually wanders away.";
                 }
                 break;
                 
             case "Wild Horse Encounter":
                  message += "\nA herd of wild horses gallops past.";
                  moraleChange = 5 + random.nextInt(5); // Inspiring sight
                  // Small chance to potentially trade for one? (Very rare/complex)
                  message += "\nTheir wild spirit is an inspiring sight.";
                  break;
                  
             case "Prairie Dog Town":
                  message += "\nYou pass through a large prairie dog town.";
                  moraleChange = 2 + random.nextInt(4); // Mildly amusing
                  // Small chance of wagon wheel damage from holes
                  if (random.nextDouble() < 0.05 && inventory.getWheels() > 0) {
                       inventory.useWheels(1);
                       message += "\nCareful! A wheel got stuck in a hole and was damaged.";
                       moraleChange = -5; // Annoyance overrides amusement
                  } else {
                       message += "\nTheir antics provide some amusement.";
                  }
                  break;

            default:
                message += "\nAn animal is nearby.";
                moraleChange = -2;
                break;
        }

        if (moraleChange > 0) {
             player.increaseMorale(moraleChange);
             message += "\nMorale increases by " + moraleChange + ".";
        } else if (moraleChange < 0) {
             player.decreaseMorale(Math.abs(moraleChange));
             message += "\nMorale decreases by " + Math.abs(moraleChange) + ".";
        }

        if (healthDamage > 0) {
            if (player.getHealth() - healthDamage <= 0) {
                message += "\nThe encounter proved fatal (" + deathCause + ").";
                player.decreaseHealth(healthDamage, deathCause); // Use specific cause
            } else {
                player.decreaseHealth(healthDamage);
                message += "\nYou suffer " + healthDamage + " health damage.";
            }
        }

        showMessage(message);
    }
} 