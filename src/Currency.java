/**
 * Currency Class of the Perils Along the Platte Game
 * Manages player's money and financial transactions.
 * Handles balance tracking, deposits, and withdrawals.
 *
 * @author : Alex Randall, Chase McCluskey, Painter Drury, and Domenic Pilla
 * @version : 1.0
 * @date : 03/25/2025
 * @file : currency.java
 */
public class Currency {
    private int balance;

    /**
     * Constructor for creating a new currency object with initial balance.
     *
     * @param startingAmount The initial amount of money
     */
    public Currency(int startingAmount) {
        this.balance = startingAmount;
    }

    /**
     * Adds money to the balance.
     * Only positive amounts can be added.
     *
     * @param amount The amount of money to add
     */
    public void addMoney(int amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    /**
     * Removes money from the balance.
     * Only removes if amount is positive and less than or equal to current balance.
     *
     * @param amount The amount of money to remove
     */
    public void removeMoney(int amount) {
        if (amount > 0 && amount <= this.balance) {
            this.balance -= amount;
        }
    }

    /**
     * Returns the current balance.
     *
     * @return The current balance
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Sets the balance to a specific amount.
     * Only non-negative values are accepted.
     *
     * @param amount The amount to set the balance to
     */
    public void setBalance(int amount) {
        if (amount >= 0) {
            this.balance = amount;
        }
    }
}