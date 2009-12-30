package com.mattstine.polyglotosgi.vendingmachine.java.internal;

import com.mattstine.polyglotosgi.vendingmachine.api.Money;
import com.mattstine.polyglotosgi.vendingmachine.api.VendingMachine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class VendingMachineJavaImpl implements VendingMachine {

    Map<Money, Integer> bank = new TreeMap<Money, Integer>();
    Map<Money, Integer> moneyInserted = new TreeMap<Money, Integer>();
    Map<String, Integer> inventory = new TreeMap<String, Integer>();
    List<String> coinOrder = new ArrayList<String>();
    Map<String, Integer> prices = new TreeMap<String, Integer>();

    public void cashOut(Money money) {
        bank.put(money, 0);
    }

    public VendingMachineJavaImpl() {
        bank.put(Money.NICKLE, 0);
        bank.put(Money.DIME, 0);
        bank.put(Money.QUARTER, 0);
        bank.put(Money.DOLLAR, 0);

        moneyInserted.put(Money.NICKLE, 0);
        moneyInserted.put(Money.DIME, 0);
        moneyInserted.put(Money.QUARTER, 0);
        moneyInserted.put(Money.DOLLAR, 0);

        inventory.put("A", 0);
        inventory.put("B", 0);
        inventory.put("C", 0);

        prices.put("A", 65);
        prices.put("B", 100);
        prices.put("C", 150);
    }

    public String showInventory() {
        StringBuilder inventoryBuilder = new StringBuilder("[");

        for (String item : inventory.keySet()) {
            inventoryBuilder.append(item).append(":").append(inventory.get(item));
            if (!item.equals("C")) {
                inventoryBuilder.append(", ");
            }
        }

        inventoryBuilder.append("]");

        return inventoryBuilder.toString();
    }

    public String showBank() {
        StringBuilder bankBuilder = new StringBuilder("[");

        for (Money money : bank.keySet()) {
            bankBuilder.append(money.toString()).append(":").append(bank.get(money));
            if (money != Money.DOLLAR) {
                bankBuilder.append(", ");
            }
        }

        bankBuilder.append("]");

        return bankBuilder.toString();
    }

    public void service() {
        bank.put(Money.NICKLE, 50);
        bank.put(Money.DIME, 50);
        bank.put(Money.QUARTER, 50);

        inventory.put("A", 10);
        inventory.put("B", 10);
        inventory.put("C", 10);
    }

    public String currentMoneyInserted() {
        StringBuilder currentMoneyInsertedBuilder = new StringBuilder("[");

        for (Money money : moneyInserted.keySet()) {
            currentMoneyInsertedBuilder.append(money.toString()).append(":").append(moneyInserted.get(money));
            if (money != Money.DOLLAR) {
                currentMoneyInsertedBuilder.append(", ");
            }
        }

        currentMoneyInsertedBuilder.append("]");

        return currentMoneyInsertedBuilder.toString();
    }

    public void insert(Money money) {
        int amount = moneyInserted.get(money);
        amount++;
        moneyInserted.put(money, amount);

        coinOrder.add(money.getSymbol());
    }

    public int currentAmountInserted() {
        int sum = 0;
        for (Money money : moneyInserted.keySet()) {
            int amount = moneyInserted.get(money);
            sum += amount * money.getAmount();
        }
        return sum;
    }

    public String coinReturn() {
        StringBuilder coinReturnBuilder = new StringBuilder("");
        for (int i = 0; i < coinOrder.size(); i++) {
            coinReturnBuilder.append(coinOrder.get(i));
            if (i < coinOrder.size() - 1) {
                coinReturnBuilder.append(", ");
            }
        }

        moneyInserted.put(Money.NICKLE, 0);
        moneyInserted.put(Money.DIME, 0);
        moneyInserted.put(Money.QUARTER, 0);
        moneyInserted.put(Money.DOLLAR, 0);

        coinOrder.clear();

        return coinReturnBuilder.toString();
    }

    public String buy(String item) {
        if (!(inventory.get(item) > 0))
            throw new RuntimeException("Sorry, " + item + " is not in stock!");

        if (!(currentAmountInserted() >= prices.get(item)))
            throw new RuntimeException("Sorry, you didn't insert enough money to purchase " + item + "!");


        for (Money money : moneyInserted.keySet()) {
            int amountInBank = bank.get(money);
            int amountInserted = moneyInserted.get(money);
            bank.put(money, amountInserted + amountInBank);

        }


        String toVend;
        if (currentAmountInserted() > prices.get(item)) {
            String change;
            try {
                change = makeChange(item);
            } catch (RuntimeException e) {
                for (Money money : moneyInserted.keySet()) {
                    int amountInBank = bank.get(money);
                    int amountInserted = moneyInserted.get(money);
                    bank.put(money, amountInBank - amountInserted);
                }
                throw e;
            }
            toVend = item + ", " + change;
        } else {
            toVend = item;
        }

        int amountOfItem = inventory.get(item);
        amountOfItem--;
        inventory.put(item, amountOfItem);

        for (Money money : moneyInserted.keySet()) {
            moneyInserted.put(money, 0);
        }

        return toVend;
    }

    private String makeChange(String item) {
        int changeAmount = currentAmountInserted() - prices.get(item);
        List<String> coinList = new ArrayList<String>();

        for (Money coin : Money.changeBackRange()) {
            int numOfCoins = changeAmount / coin.getAmount();

            if (bank.get(coin) < numOfCoins) {
                numOfCoins = bank.get(coin);
            }

            if (numOfCoins > 0) {
                for (int i = 1; i <= numOfCoins; i++) {
                    int coinsInBank = bank.get(coin);
                    coinsInBank--;
                    bank.put(coin, coinsInBank);

                    coinList.add(coin.getSymbol());
                    changeAmount -= coin.getAmount();
                }
            }
        }

        if (changeAmount > 0) {
            throw new RuntimeException("Sorry, not enough change available to purchase " + item + " with this set of inserted money!");
        }

        StringBuilder coinListBuilder = new StringBuilder("");
        for (int i = 0; i < coinList.size(); i++) {
            coinListBuilder.append(coinList.get(i));
            if (i < coinList.size() - 1) {
                coinListBuilder.append(", ");
            }
        }

        return coinListBuilder.toString();
    }
}
