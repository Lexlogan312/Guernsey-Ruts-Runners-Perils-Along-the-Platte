import java.util.Scanner;

public class RiverCrossing {
    private final Player player;
    private final Inventory inventory;
    private final Weather weather;

    public RiverCrossing(Player player, Inventory inventory, Weather weather) {
        this.player = player;
        this.inventory = inventory;
        this.weather = weather;
    }

    public void crossRiver(Scanner scanner) {
        System.out.println("\n=== RIVER CROSSING ===");

        // Generate river characteristics
        int depth = 2 + (int)(Math.random() * 18); // 2-20 feet deep
        int width = 50 + (int)(Math.random() * 300); // 50-350 feet wide

        // Adjust difficulty based on weather
        if (weather.getCurrentWeather().contains("Rain") ||
                weather.getCurrentWeather().contains("Snow")) {
            depth += 3;
            System.out.println("The recent precipitation has made the river higher than usual.");
        }

        System.out.println("You've come to a river that is " + width + " feet wide and " + depth + " feet deep.");
        System.out.println("You need to decide how to cross.");

        boolean validChoice = false;
        while (!validChoice) {
            System.out.println("\nHow would you like to cross?");
            System.out.println("1. Ford the river (wade across)");
            System.out.println("2. Caulk the wagon and float across");
            System.out.println("3. Use a ferry (costs $10)");
            System.out.println("4. Wait a day and see if conditions improve");

            int choice = 0;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                choice = 0;
            }

            switch (choice) {
                case 1: // Ford the river
                    validChoice = true;
                    fordRiver(depth);
                    break;
                case 2: // Caulk and float
                    validChoice = true;
                    caulkAndFloat(width, depth);
                    break;
                case 3: // Ferry
                    validChoice = true;
                    useFerry();
                    break;
                case 4: // Wait
                    validChoice = true;
                    wait(scanner);
                    break;
                default:
                    System.out.println("Please enter a number between 1 and 4.");
            }
        }
    }

    private void fordRiver(int depth) {
        System.out.println("You attempt to ford the river...");

        // Fording is only safe if river isn't too deep
        double successChance;

        if (depth <= 3) {
            successChance = 0.9; // Very high chance of success for shallow rivers
        } else if (depth <= 8) {
            successChance = 0.6; // Moderate chance for medium depth
        } else {
            successChance = 0.3; // Low chance for deep rivers
        }

        // Modifier based on oxen health and count
        successChance *= (inventory.getOxenHealth() / 100.0);
        successChance *= Math.min(1.0, inventory.getOxen() / 4.0);

        if (Math.random() < successChance) {
            System.out.println("You successfully ford the river!");

            // Small chance of minor issue even on success
            if (Math.random() < 0.2) {
                System.out.println("However, some of your food got wet and spoiled.");
                int foodLost = 10 + (int)(Math.random() * 30); // 10-40 pounds lost
                inventory.consumeFood(foodLost);
                System.out.println("You lost " + foodLost + " pounds of food.");
            }
        } else {
            System.out.println("Disaster! Your wagon overturns in the river!");

            // Lost supplies
            int foodLost = 50 + (int)(Math.random() * 100); // 50-150 pounds lost
            inventory.consumeFood(foodLost);

            // Choose which parts to lose
            boolean loseWheel = Math.random() < 0.3 && inventory.getWheels() > 0;
            boolean loseAxle = Math.random() < 0.2 && inventory.getAxles() > 0;
            boolean loseTongue = Math.random() < 0.15 && inventory.getTongues() > 0;
            boolean loseWagonBow = Math.random() < 0.25 && inventory.getWagonBows() > 0;
            
            int wheelsLost = loseWheel ? 1 : 0;
            int axlesLost = loseAxle ? 1 : 0;
            int tonguesLost = loseTongue ? 1 : 0;
            int wagonBowsLost = loseWagonBow ? 1 : 0;

            // Use parts
            if (wheelsLost > 0) inventory.useWheels(wheelsLost);
            if (axlesLost > 0) inventory.useAxles(axlesLost);
            if (tonguesLost > 0) inventory.useTongues(tonguesLost);
            if (wagonBowsLost > 0) inventory.useWagonBows(wagonBowsLost);

            int medicineLost = (int)(Math.random() * 2); // 0-1 medicine lost
            inventory.useMedicine(medicineLost);

            System.out.println("You lost:");
            System.out.println("- " + foodLost + " pounds of food");
            if (wheelsLost > 0) System.out.println("- " + wheelsLost + " wheel(s)");
            if (axlesLost > 0) System.out.println("- " + axlesLost + " axle(s)");
            if (tonguesLost > 0) System.out.println("- " + tonguesLost + " tongue(s)");
            if (wagonBowsLost > 0) System.out.println("- " + wagonBowsLost + " wagon bow(s)");
            if (medicineLost > 0) System.out.println("- " + medicineLost + " medicine");

            // Potential injury
            if (Math.random() < 0.5) {
                int healthLost = 10 + (int)(Math.random() * 20); // 10-30 health lost
                player.decreaseHealth(healthLost);
                System.out.println("You were injured in the accident and lost " + healthLost + " health.");
            }
        }
    }

    private void caulkAndFloat(int width, int depth) {
        System.out.println("You seal the wagon with pitch and prepare to float across...");

        double successChance;

        // Caulking works better for wider/deeper rivers
        if (depth > 10) {
            successChance = 0.8; // Good for deep rivers
        } else if (depth > 5) {
            successChance = 0.7; // Decent for medium rivers
        } else {
            successChance = 0.5; // Less effective for shallow rivers
        }

        // Weather impact
        if (weather.getCurrentWeather().contains("Rain") ||
                weather.getCurrentWeather().contains("Storm")) {
            successChance -= 0.2;
            System.out.println("The rough water makes this crossing more difficult.");
        }

        if (Math.random() < successChance) {
            System.out.println("You successfully float across the river!");
        } else {
            System.out.println("Your wagon leaks and some of your supplies are damaged!");

            int foodLost = 30 + (int)(Math.random() * 70); // 30-100 pounds lost
            inventory.consumeFood(foodLost);

            System.out.println("You lost " + foodLost + " pounds of food.");

            // Potential loss of other items
            if (Math.random() < 0.3) {
                int ammoLost = 5 + (int)(Math.random() * 10); // 5-15 ammunition lost
                inventory.useAmmunition(ammoLost);
                System.out.println("You also lost " + ammoLost + " rounds of ammunition.");
            }
        }
    }

    private void useFerry(Scanner scanner) {
        System.out.println("You approach the ferryman...");

        if (player.getMoney() >= 10) {
            System.out.println("The ferry costs $10. Do you want to pay? (Y/N)");
            String confirm = scanner.nextLine().toUpperCase();

            if (confirm.equals("Y")) {
                player.spendMoney(10);
                System.out.println("You pay the ferryman $10.");
                System.out.println("He safely transports you and your wagon across the river.");
                System.out.println("This was a safe choice!");
            } else {
                System.out.println("You decide not to use the ferry.");
                System.out.println("You'll need to choose another method to cross.");
                crossRiver(scanner);
            }
        } else {
            System.out.println("You don't have enough money for the ferry ($10).");
            System.out.println("You'll need to choose another method to cross.");
            crossRiver(scanner);
        }
    }

    private void useFerry() {
        System.out.println("You approach the ferryman...");

        if (player.getMoney() >= 10) {
            player.spendMoney(10);
            System.out.println("You pay the ferryman $10.");
            System.out.println("He safely transports you and your wagon across the river.");
            System.out.println("This was a safe choice!");
        } else {
            System.out.println("You don't have enough money for the ferry ($10).");
            System.out.println("The ferryman turns you away.");

            // Choose a random alternative method
            if (Math.random() < 0.5) {
                fordRiver(5 + (int)(Math.random() * 5)); // Force a moderate depth river
            } else {
                caulkAndFloat(100 + (int)(Math.random() * 100), 8 + (int)(Math.random() * 5));
            }
        }
    }

    private void wait(Scanner scanner) {
        System.out.println("You decide to wait a day for conditions to improve.");
        System.out.println("Your party consumes food for the day.");

        // Consume food for waiting
        inventory.consumeFood(player.getFamilySize() * 2);

        // 50% chance conditions improve
        if (Math.random() < 0.5) {
            System.out.println("The river seems lower today. Crossing should be easier.");

            // Cross with improved chances
            RiverCrossing improvedCrossing = new RiverCrossing(player, inventory, new Weather(3, "Improved"));
            improvedCrossing.crossRiver(scanner);
        } else {
            System.out.println("The river hasn't changed much. You'll need to cross now.");
            crossRiver(scanner);
        }
    }
} 