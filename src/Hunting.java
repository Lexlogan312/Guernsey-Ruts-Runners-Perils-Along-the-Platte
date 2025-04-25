import java.util.Scanner;

public class Hunting {
    private final Inventory inventory;

    public Hunting(Inventory inventory) {
        this.inventory = inventory;
    }

    public void hunt(Scanner scanner) {
        System.out.println("\n=== HUNTING ===");

        // Check if player has ammunition
        if (inventory.getAmmunition() <= 0) {
            System.out.println("You don't have any ammunition for hunting!");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            return;
        }

        System.out.println("You set out to hunt for food.");
        System.out.println("Hunting requires ammunition and time, but can provide a lot of food.");
        System.out.println("You have " + inventory.getAmmunition() + " rounds of ammunition.");
        System.out.println("\nPress Enter to begin hunting...");
        scanner.nextLine();

        // Hunting minigame
        System.out.println("\nYou spot some game in the distance!");
        System.out.println("Type 'shoot' quickly to take your shot!");

        long startTime = System.currentTimeMillis();
        String input = scanner.nextLine().toLowerCase();
        long endTime = System.currentTimeMillis();

        // Calculate reaction time in seconds
        double reactionTime = (endTime - startTime) / 1000.0;

        // Use some ammunition
        int ammoUsed = 1 + (int)(Math.random() * 3); // 1-3 rounds used
        inventory.useAmmunition(ammoUsed);

        if (!input.equals("shoot")) {
            System.out.println("You missed your shot! The animal got away.");
            System.out.println("You used " + ammoUsed + " rounds of ammunition.");
            return;
        }

        // Calculate success based on reaction time
        boolean success = false;
        int foodGained = 0;
        String animal = "";

        if (reactionTime < 1.5) {
            // Great shot!
            success = true;

            // Determine what animal was hunted based on random chance
            double animalChance = Math.random();

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
        } else if (reactionTime < 3.0) {
            // Decent shot
            success = true;

            // Smaller game or partial success
            double animalChance = Math.random();

            if (animalChance < 0.4) {
                animal = "rabbit";
                foodGained = 5 + (int)(Math.random() * 5); // 5-10 pounds
            } else {
                animal = "squirrel";
                foodGained = 1 + (int)(Math.random() * 3); // 1-4 pounds
            }
        } else {
            // Too slow, missed shot
            System.out.println("You were too slow! The animal got away.");
            System.out.println("You used " + ammoUsed + " rounds of ammunition.");
            return;
        }

        if (success) {
            System.out.println("Great shot! You got a " + animal + "!");
            System.out.println("You gained " + foodGained + " pounds of food.");
            System.out.println("You used " + ammoUsed + " rounds of ammunition.");

            // Add food to inventory
            inventory.addFood(foodGained);
        }

        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
} 