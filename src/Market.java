import java.util.Scanner;

public class Market {
    private Player player;
    private Inventory inventory;
    private static final int OXEN_PRICE = 40;
    private static final int FOOD_PRICE = 1;
    private static final int WAGON_PART_PRICE = 20;
    private static final int MEDICINE_PRICE = 15;
    private static final int AMMUNITION_PRICE = 2;

    public Market(Player player, Inventory inventory) {
        this.player = player;
        this.inventory = inventory;
    }

    public void shop(Scanner scanner) {
        boolean isShopping = true;

        while (isShopping) {
            System.out.println("\n=== MARKET ===");
            System.out.println("Money remaining: $" + player.getMoney());
            System.out.println("What would you like to buy?");
            System.out.println("1. Oxen ($40 each)");
            System.out.println("2. Food ($1 per pound)");
            System.out.println("3. Wagon parts ($20 each)");
            System.out.println("4. Medicine ($15 each)");
            System.out.println("5. Ammunition ($2 per box of 20 rounds)");
            System.out.println("6. Review purchases");
            System.out.println("7. Finish shopping");

            int userChoice;
            try {
                userChoice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                userChoice = 0;
            }

            switch (userChoice) {
                case 1:
                    buyOxen(scanner);
                    break;
                case 2:
                    buyFood(scanner);
                    break;
                case 3:
                    buyWagonParts(scanner);
                    break;
                case 4:
                    buyMedicine(scanner);
                    break;
                case 5:
                    buyAmmunition(scanner);
                    break;
                case 6:
                    inventory.displayInventory();
                    break;
                case 7:
                    if (canStartJourney()) {
                        isShopping = false;
                    } else {
                        System.out.println("\nYou don't have enough supplies to start your journey!");
                        System.out.println("You need at least:");
                        System.out.println("- 2 oxen");
                        System.out.println("- 200 pounds of food");
                        System.out.println("- 3 wagon parts");
                        System.out.println("- 2 medicine");
                    }
                    break;
                default:
                    System.out.println("Please enter a number between 1 and 7.");
            }
        }

        System.out.println("\nYou have completed your shopping.");
        System.out.println("Money remaining: $" + player.getMoney());
        System.out.println("Press Enter to begin your journey...");
        scanner.nextLine();
    }

    private void buyOxen(Scanner scanner) {
        System.out.println("\n=== BUY OXEN ===");
        System.out.println("Oxen are needed to pull your wagon. You should have at least 2.");
        System.out.println("Each ox costs $40");
        System.out.println("You currently have: " + inventory.getOxen() + " oxen");
        System.out.println("How many would you like to buy?");
        int oxenAmount = getValidAmount(scanner);
        int totalCost = oxenAmount * OXEN_PRICE;
        if (totalCost > player.getMoney()) {
            System.out.println("You don't have enough money for that!");
        } else {
            player.spendMoney(totalCost);
            inventory.addOxen(oxenAmount);
            System.out.println("You bought " + oxenAmount + " oxen for $" + totalCost);
        }
    }

    private void buyFood(Scanner scanner) {
        System.out.println("\n=== BUY FOOD ===");
        System.out.println("Food is needed to feed your family. Each person consumes about 2 pounds per day.");
        System.out.println("Food costs $1 per pound.");
        System.out.println("You currently have: " + inventory.getFood() + " pounds of food");
        System.out.println("How many pounds would you like to buy?");
        int foodAmount = getValidAmount(scanner);
        int totalCost = foodAmount * FOOD_PRICE;
        if (totalCost > player.getMoney()) {
            System.out.println("You don't have enough money for that!");
        } else {
            player.spendMoney(totalCost);
            inventory.addFood(foodAmount);
            System.out.println("You bought " + foodAmount + " pounds of food for $" + totalCost);
        }
    }

    private void buyWagonParts(Scanner scanner) {
        System.out.println("\n=== BUY WAGON PARTS ===");
        System.out.println("Wagon parts are needed for repairs along the trail.");
        System.out.println("Parts cost $20 each.");
        System.out.println("You currently have: " + inventory.getWagonParts() + " parts");
        System.out.println("How many would you like to buy?");
        int partsAmount = getValidAmount(scanner);
        int totalCost = partsAmount * WAGON_PART_PRICE;
        if (totalCost > player.getMoney()) {
            System.out.println("You don't have enough money for that!");
        } else {
            player.spendMoney(totalCost);
            inventory.addWagonParts(partsAmount);
            System.out.println("You bought " + partsAmount + " wagon parts for $" + totalCost);
        }
    }

    private void buyMedicine(Scanner scanner) {
        System.out.println("\n=== BUY MEDICINE ===");
        System.out.println("Medicine can help cure diseases and injuries along the trail.");
        System.out.println("Medicine costs $15 per unit.");
        System.out.println("You currently have: " + inventory.getMedicine() + " medicine");
        System.out.println("How many would you like to buy?");
        int medicineAmount = getValidAmount(scanner);
        int totalCost = medicineAmount * MEDICINE_PRICE;
        if (totalCost > player.getMoney()) {
            System.out.println("You don't have enough money for that!");
        } else {
            player.spendMoney(totalCost);
            inventory.addMedicine(medicineAmount);
            System.out.println("You bought " + medicineAmount + " medicine for $" + totalCost);
        }
    }

    private void buyAmmunition(Scanner scanner) {
        System.out.println("\n=== BUY AMMUNITION ===");
        System.out.println("Ammunition is needed for hunting and protection.");
        System.out.println("Ammunition costs $2 per box (20 rounds).");
        System.out.println("You currently have: " + inventory.getAmmunition() + " rounds");
        System.out.println("How many boxes would you like to buy?");
        int boxesAmount = getValidAmount(scanner);
        int totalCost = boxesAmount * AMMUNITION_PRICE;
        if (totalCost > player.getMoney()) {
            System.out.println("You don't have enough money for that!");
        } else {
            player.spendMoney(totalCost);
            inventory.addAmmunition(boxesAmount * 20);
            System.out.println("You bought " + boxesAmount + " boxes of ammunition for $" + totalCost);
        }
    }

    private int getValidAmount(Scanner scanner) {
        int amount = 0;
        boolean isValid = false;

        while (!isValid) {
            try {
                amount = Integer.parseInt(scanner.nextLine());
                if (amount < 0) {
                    System.out.println("Please enter a positive number.");
                } else {
                    isValid = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        return amount;
    }

    private boolean canStartJourney() {
        return inventory.getOxen() >= 2 && inventory.getFood() >= 200 && inventory.getWagonParts() >= 3 && inventory.getMedicine() >= 2;
    }
}