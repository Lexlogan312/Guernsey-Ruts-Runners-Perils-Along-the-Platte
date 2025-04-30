import java.util.Scanner;
import java.util.Random;

public class Trading {
    private final Player player;
    private final Inventory inventory;
    private final Random random;

    public Trading(Player player, Inventory inventory) {
        this.player = player;
        this.inventory = inventory;
        this.random = new Random();
    }

    public void trade(Scanner scanner) {
        System.out.println("\n=== TRADING POST ===");
        System.out.println("Trading was an important part of pioneer life on the trail.");
        System.out.println("You can barter with other travelers or local traders for supplies.");
        
        boolean trading = true;
        while (trading) {
            System.out.println("\nWhat would you like to do?");
            System.out.println("1. Buy supplies");
            System.out.println("2. Sell supplies");
            System.out.println("3. Barter with other travelers");
            System.out.println("4. Finish trading");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    buySupplies(scanner);
                    break;
                case "2":
                    sellSupplies(scanner);
                    break;
                case "3":
                    barterWithTravelers(scanner);
                    break;
                case "4":
                    trading = false;
                    System.out.println("You finish your trading and prepare to continue your journey.");
                    break;
                default:
                    System.out.println("Please enter a number between 1 and 4.");
            }
        }
    }
    
    private void buySupplies(Scanner scanner) {
        System.out.println("\n=== BUY SUPPLIES ===");
        System.out.println("Money available: $" + player.getMoney());
        
        System.out.println("\nWhat would you like to buy?");
        System.out.println("1. Food ($1-2 per pound)");
        System.out.println("2. Wagon parts ($20-30 each)");
        System.out.println("3. Medicine ($15-25 each)");
        System.out.println("4. Ammunition ($2-3 per box of 20)");
        System.out.println("5. Return to trading menu");
        
        String choice = scanner.nextLine();
        
        if (choice.equals("5")) {
            return;
        }
        
        // Random price fluctuations for supply and demand
        double priceFactor = 0.8 + (random.nextDouble() * 0.4); // 0.8 to 1.2 multiplier
        
        switch (choice) {
            case "1": // Food
                int foodPrice = Math.max(1, (int)Math.round(1.5 * priceFactor));
                System.out.println("Current price: $" + foodPrice + " per pound of food.");
                System.out.println("How many pounds would you like to buy?");
                
                try {
                    int amount = Integer.parseInt(scanner.nextLine());
                    int totalCost = amount * foodPrice;
                    
                    if (totalCost > player.getMoney()) {
                        System.out.println("You don't have enough money for that purchase.");
                    } else {
                        player.spendMoney(totalCost);
                        inventory.addFood(amount);
                        System.out.println("You purchased " + amount + " pounds of food for $" + totalCost);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                }
                break;
                
            case "2": // Wagon parts
                System.out.println("\n=== BUY WAGON PARTS ===");
                System.out.println("Which type of wagon part would you like to buy?");
                System.out.println("1. Wheels (50 lbs each)");
                System.out.println("2. Axles (40 lbs each)");
                System.out.println("3. Tongues (30 lbs each)");
                System.out.println("4. Wagon Bows (10 lbs each)");
                System.out.println("5. Return to trading menu");
                
                String partChoice = scanner.nextLine();
                
                if (partChoice.equals("5")) {
                    return;
                }
                
                int partPrice;
                String partName;
                int partWeight;
                
                switch (partChoice) {
                    case "1": // Wheels
                        partPrice = Math.max(25, (int)Math.round(30 * priceFactor));
                        partName = "wheel";
                        partWeight = 50;
                        break;
                    case "2": // Axles
                        partPrice = Math.max(20, (int)Math.round(25 * priceFactor));
                        partName = "axle";
                        partWeight = 40;
                        break;
                    case "3": // Tongues
                        partPrice = Math.max(15, (int)Math.round(20 * priceFactor));
                        partName = "tongue";
                        partWeight = 30;
                        break;
                    case "4": // Wagon Bows
                        partPrice = Math.max(10, (int)Math.round(15 * priceFactor));
                        partName = "wagon bow";
                        partWeight = 10;
                        break;
                    default:
                        System.out.println("Please enter a valid number.");
                        return;
                }
                
                System.out.println("Current price: $" + partPrice + " per " + partName + ".");
                System.out.println("How many " + partName + "s would you like to buy?");
                
                try {
                    int amount = Integer.parseInt(scanner.nextLine());
                    int totalCost = amount * partPrice;
                    int totalWeight = amount * partWeight;
                    
                    if (totalCost > player.getMoney()) {
                        System.out.println("You don't have enough money for that purchase.");
                    } else if (!inventory.hasWeightCapacity(totalWeight)) {
                        System.out.println("Your wagon cannot carry that much additional weight!");
                        System.out.println("Current load: " + inventory.getCurrentWeight() + "/" + inventory.getMaxWeightCapacity() + " pounds");
                        System.out.println("Additional weight: " + totalWeight + " pounds");
                    } else {
                        player.spendMoney(totalCost);
                        
                        // Add appropriate parts
                        switch (partChoice) {
                            case "1":
                                inventory.addWheels(amount);
                                break;
                            case "2":
                                inventory.addAxles(amount);
                                break;
                            case "3":
                                inventory.addTongues(amount);
                                break;
                            case "4":
                                inventory.addWagonBows(amount);
                                break;
                        }
                        
                        System.out.println("You purchased " + amount + " " + partName + (amount != 1 ? "s" : "") + " for $" + totalCost);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                }
                break;
                
            case "3": // Medicine
                int medicinePrice = Math.max(15, (int)Math.round(20 * priceFactor));
                System.out.println("Current price: $" + medicinePrice + " per medicine.");
                System.out.println("How many medicines would you like to buy?");
                
                try {
                    int amount = Integer.parseInt(scanner.nextLine());
                    int totalCost = amount * medicinePrice;
                    
                    if (totalCost > player.getMoney()) {
                        System.out.println("You don't have enough money for that purchase.");
                    } else {
                        player.spendMoney(totalCost);
                        inventory.addMedicine(amount);
                        System.out.println("You purchased " + amount + " medicines for $" + totalCost);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                }
                break;
                
            case "4": // Ammunition
                int ammoPrice = Math.max(8, (int)Math.round(10 * priceFactor));
                System.out.println("Current price: $" + ammoPrice + " per box of 20 rounds.");
                System.out.println("How many boxes would you like to buy?");
                
                try {
                    int amount = Integer.parseInt(scanner.nextLine());
                    int totalCost = amount * ammoPrice;
                    
                    if (totalCost > player.getMoney()) {
                        System.out.println("You don't have enough money for that purchase.");
                    } else {
                        player.spendMoney(totalCost);
                        inventory.addAmmunition(amount * 20);
                        System.out.println("You purchased " + amount + " boxes of ammunition for $" + totalCost);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                }
                break;
                
            default:
                System.out.println("Please enter a number between 1 and 5.");
        }
    }
    
    private void sellSupplies(Scanner scanner) {
        System.out.println("\n=== SELL SUPPLIES ===");
        System.out.println("Money available: $" + player.getMoney());
        
        // Display inventory
        inventory.displayInventory();
        
        System.out.println("\nWhat would you like to sell?");
        System.out.println("1. Food ($0.50-1.00 per pound)");
        System.out.println("2. Wagon parts ($10-15 each)");
        System.out.println("3. Medicine ($7-12 each)");
        System.out.println("4. Ammunition ($1-1.50 per box of 20)");
        System.out.println("5. Return to trading menu");
        
        String choice = scanner.nextLine();
        
        if (choice.equals("5")) {
            return;
        }
        
        // Random price fluctuations for supply and demand
        double priceFactor = 0.8 + (random.nextDouble() * 0.4); // 0.8 to 1.2 multiplier
        
        switch (choice) {
            case "1": // Food
                // Ensure minimum price of 1 per pound
                int foodPrice = Math.max(1, (int)Math.round(0.75 * priceFactor));
                System.out.println("Current selling price: $" + foodPrice + " per pound of food.");
                System.out.println("You have " + inventory.getFood() + " pounds of food.");
                System.out.println("How many pounds would you like to sell?");
                
                try {
                    int amount = Integer.parseInt(scanner.nextLine());
                    if (amount > inventory.getFood()) {
                        System.out.println("You don't have that much food to sell.");
                    } else {
                        int totalEarned = amount * foodPrice;
                        inventory.consumeFood(amount);
                        player.addMoney(totalEarned);
                        System.out.println("You sold " + amount + " pounds of food for $" + totalEarned);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                }
                break;
                
            case "2": // Wagon parts
                System.out.println("\n=== SELL WAGON PARTS ===");
                System.out.println("Which type of wagon part would you like to sell?");
                System.out.println("1. Wheels - You have " + inventory.getWheels());
                System.out.println("2. Axles - You have " + inventory.getAxles());
                System.out.println("3. Tongues - You have " + inventory.getTongues());
                System.out.println("4. Wagon Bows - You have " + inventory.getWagonBows());
                System.out.println("5. Return to trading menu");
                
                String partChoice = scanner.nextLine();
                
                if (partChoice.equals("5")) {
                    return;
                }
                
                int partPrice;
                String partName;
                int availableParts;
                
                switch (partChoice) {
                    case "1": // Wheels
                        partPrice = Math.max(12, (int)Math.round(15 * priceFactor));
                        partName = "wheel";
                        availableParts = inventory.getWheels();
                        break;
                    case "2": // Axles
                        partPrice = Math.max(10, (int)Math.round(12 * priceFactor));
                        partName = "axle";
                        availableParts = inventory.getAxles();
                        break;
                    case "3": // Tongues
                        partPrice = Math.max(8, (int)Math.round(10 * priceFactor));
                        partName = "tongue";
                        availableParts = inventory.getTongues();
                        break;
                    case "4": // Wagon Bows
                        partPrice = Math.max(5, (int)Math.round(8 * priceFactor));
                        partName = "wagon bow";
                        availableParts = inventory.getWagonBows();
                        break;
                    default:
                        System.out.println("Please enter a valid number.");
                        return;
                }
                
                System.out.println("Current selling price: $" + partPrice + " per " + partName + ".");
                System.out.println("You have " + availableParts + " " + partName + (availableParts != 1 ? "s" : "") + ".");
                System.out.println("How many would you like to sell?");
                
                try {
                    int amount = Integer.parseInt(scanner.nextLine());
                    if (amount > availableParts) {
                        System.out.println("You don't have that many " + partName + "s to sell.");
                    } else {
                        int totalEarned = amount * partPrice;
                        
                        // Remove appropriate parts
                        switch (partChoice) {
                            case "1":
                                inventory.useWheels(amount);
                                break;
                            case "2":
                                inventory.useAxles(amount);
                                break;
                            case "3":
                                inventory.useTongues(amount);
                                break;
                            case "4":
                                inventory.useWagonBows(amount);
                                break;
                        }
                        
                        player.addMoney(totalEarned);
                        System.out.println("You sold " + amount + " " + partName + (amount != 1 ? "s" : "") + " for $" + totalEarned);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                }
                break;
                
            case "3": // Medicine
                // Ensure minimum price of 7 per medicine
                int medicinePrice = Math.max(7, (int)Math.round(10 * priceFactor));
                System.out.println("Current selling price: $" + medicinePrice + " per medicine.");
                System.out.println("You have " + inventory.getMedicine() + " medicines.");
                System.out.println("How many medicines would you like to sell?");
                
                try {
                    int amount = Integer.parseInt(scanner.nextLine());
                    if (amount > inventory.getMedicine()) {
                        System.out.println("You don't have that much medicine to sell.");
                    } else {
                        int totalEarned = amount * medicinePrice;
                        inventory.useMedicine(amount);
                        player.addMoney(totalEarned);
                        System.out.println("You sold " + amount + " medicines for $" + totalEarned);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                }
                break;
                
            case "4": // Ammunition
                // Ensure minimum price of 5 per box
                int ammoPrice = Math.max(5, (int)Math.round(7.5 * priceFactor));
                System.out.println("Current selling price: $" + ammoPrice + " per box of 20 rounds.");
                System.out.println("You have " + inventory.getAmmunition() + " rounds of ammunition.");
                System.out.println("How many boxes would you like to sell?");
                
                try {
                    int amount = Integer.parseInt(scanner.nextLine());
                    int totalRounds = amount * 20;
                    if (totalRounds > inventory.getAmmunition()) {
                        System.out.println("You don't have that much ammunition to sell.");
                    } else {
                        int totalEarned = amount * ammoPrice;
                        inventory.useAmmunition(totalRounds);
                        player.addMoney(totalEarned);
                        System.out.println("You sold " + amount + " boxes of ammunition for $" + totalEarned);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                }
                break;
                
            default:
                System.out.println("Please enter a number between 1 and 5.");
        }
    }
    
    private void barterWithTravelers(Scanner scanner) {
        System.out.println("\n=== BARTER WITH TRAVELERS ===");
        System.out.println("Some travelers are willing to trade goods directly.");
        
        // Generate random trade offers
        int tradeType = random.nextInt(8);
        String offerDescription = "";
        String playerGives = "";
        String playerGets = "";
        int playerGivesAmount = 0;
        int playerGetsAmount = 0;
        String partTypeGiven = "";  // For wagon parts
        String partTypeReceived = ""; // For wagon parts
        
        switch (tradeType) {
            case 0: // Food for medicine
                playerGivesAmount = 20 + random.nextInt(30); // 20-50 pounds of food
                playerGetsAmount = 1 + random.nextInt(2); // 1-2 medicines
                playerGives = playerGivesAmount + " pounds of food";
                playerGets = playerGetsAmount + " medicine" + (playerGetsAmount > 1 ? "s" : "");
                offerDescription = "A traveler offers to trade " + playerGets + " for " + playerGives + ".";
                break;
                
            case 1: // Ammunition for food
                playerGivesAmount = 20 + random.nextInt(30); // 20-50 rounds
                playerGetsAmount = 10 + random.nextInt(20); // 10-30 pounds of food
                playerGives = playerGivesAmount + " rounds of ammunition";
                playerGets = playerGetsAmount + " pounds of food";
                offerDescription = "A hunter offers to trade " + playerGets + " for " + playerGives + ".";
                break;
                
            case 2: // Wheels for ammunition
                playerGivesAmount = 1;
                playerGetsAmount = 30 + random.nextInt(30); // 30-60 rounds
                partTypeGiven = "wheel";
                playerGives = playerGivesAmount + " " + partTypeGiven;
                playerGets = playerGetsAmount + " rounds of ammunition";
                offerDescription = "A wagon master offers to trade " + playerGets + " for " + playerGives + ".";
                break;
                
            case 3: // Medicine for axles
                playerGivesAmount = 1 + random.nextInt(2); // 1-2 medicines
                playerGetsAmount = 1;
                partTypeReceived = "axle";
                playerGives = playerGivesAmount + " medicine" + (playerGivesAmount > 1 ? "s" : "");
                playerGets = playerGetsAmount + " " + partTypeReceived;
                offerDescription = "A trader offers to trade " + playerGets + " for " + playerGives + ".";
                break;
                
            case 4: // Food for ammunition
                playerGivesAmount = 20 + random.nextInt(30); // 20-50 pounds of food
                playerGetsAmount = 10 + random.nextInt(20); // 10-30 rounds
                playerGives = playerGivesAmount + " pounds of food";
                playerGets = playerGetsAmount + " rounds of ammunition";
                offerDescription = "A pioneer offers to trade " + playerGets + " for " + playerGives + ".";
                break;
                
            case 5: // Tongue for food
                playerGivesAmount = 1;
                playerGetsAmount = 30 + random.nextInt(30); // 30-60 pounds of food
                partTypeGiven = "tongue";
                playerGives = playerGivesAmount + " wagon " + partTypeGiven;
                playerGets = playerGetsAmount + " pounds of food";
                offerDescription = "A stranded traveler offers to trade " + playerGets + " for " + playerGives + " - he has plenty of food but his wagon is disabled.";
                break;
                
            case 6: // Ammunition for wagon bows
                playerGivesAmount = 30 + random.nextInt(20); // 30-50 rounds
                playerGetsAmount = 2;
                partTypeReceived = "wagon bow";
                playerGives = playerGivesAmount + " rounds of ammunition";
                playerGets = playerGetsAmount + " " + partTypeReceived + "s";
                offerDescription = "A trader heading back east offers to trade " + playerGets + " for " + playerGives + ".";
                break;
                
            case 7: // Food for tongues
                playerGivesAmount = 40 + random.nextInt(30); // 40-70 pounds of food
                playerGetsAmount = 1;
                partTypeReceived = "tongue";
                playerGives = playerGivesAmount + " pounds of food";
                playerGets = playerGetsAmount + " wagon " + partTypeReceived;
                offerDescription = "A carpenter at a trading post offers to trade " + playerGets + " for " + playerGives + ".";
                break;
        }
        
        System.out.println(offerDescription);
        System.out.println("Do you accept this trade? (Y/N)");
        
        String answer = scanner.nextLine().toUpperCase();
        
        if (answer.equals("Y")) {
            boolean canTrade = false;
            
            // Check if player has required resources and perform trade
            switch (tradeType) {
                case 0: // Food for medicine
                    canTrade = inventory.getFood() >= playerGivesAmount;
                    if (canTrade) {
                        inventory.consumeFood(playerGivesAmount);
                        inventory.addMedicine(playerGetsAmount);
                    } else {
                        System.out.println("You don't have enough food for this trade.");
                    }
                    break;
                    
                case 1: // Ammunition for food
                    canTrade = inventory.getAmmunition() >= playerGivesAmount;
                    if (canTrade) {
                        inventory.useAmmunition(playerGivesAmount);
                        inventory.addFood(playerGetsAmount);
                    } else {
                        System.out.println("You don't have enough ammunition for this trade.");
                    }
                    break;
                    
                case 2: // Wheels for ammunition
                    canTrade = inventory.getWheels() >= playerGivesAmount;
                    if (canTrade) {
                        inventory.useWheels(playerGivesAmount);
                        inventory.addAmmunition(playerGetsAmount);
                    } else {
                        System.out.println("You don't have enough wheels for this trade.");
                    }
                    break;
                    
                case 3: // Medicine for axles
                    canTrade = inventory.getMedicine() >= playerGivesAmount;
                    if (canTrade) {
                        inventory.useMedicine(playerGivesAmount);
                        inventory.addAxles(playerGetsAmount);
                    } else {
                        System.out.println("You don't have enough medicine for this trade.");
                    }
                    break;
                    
                case 4: // Food for ammunition
                    canTrade = inventory.getFood() >= playerGivesAmount;
                    if (canTrade) {
                        inventory.consumeFood(playerGivesAmount);
                        inventory.addAmmunition(playerGetsAmount);
                    } else {
                        System.out.println("You don't have enough food for this trade.");
                    }
                    break;
                    
                case 5: // Tongue for food
                    canTrade = inventory.getTongues() >= playerGivesAmount;
                    if (canTrade) {
                        inventory.useTongues(playerGivesAmount);
                        inventory.addFood(playerGetsAmount);
                    } else {
                        System.out.println("You don't have enough tongues for this trade.");
                    }
                    break;
                    
                case 6: // Ammunition for wagon bows
                    canTrade = inventory.getAmmunition() >= playerGivesAmount;
                    if (canTrade) {
                        inventory.useAmmunition(playerGivesAmount);
                        inventory.addWagonBows(playerGetsAmount);
                    } else {
                        System.out.println("You don't have enough ammunition for this trade.");
                    }
                    break;
                    
                case 7: // Food for tongues
                    canTrade = inventory.getFood() >= playerGivesAmount;
                    if (canTrade) {
                        inventory.consumeFood(playerGivesAmount);
                        inventory.addTongues(playerGetsAmount);
                    } else {
                        System.out.println("You don't have enough food for this trade.");
                    }
                    break;
            }
            
            if (canTrade) {
                System.out.println("Trade completed successfully!");
                System.out.println("You gave: " + playerGives);
                System.out.println("You received: " + playerGets);
            }
        } else {
            System.out.println("You declined the trade.");
        }
    }
}