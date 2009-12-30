package com.mattstine.polyglotosgi.vendingmachine.api;

public interface VendingMachine {
    String showInventory();
    String showBank();
    void service();
    String currentMoneyInserted();
    void insert(Money money);
    int currentAmountInserted();
    String coinReturn();
    String buy(String item);
    void cashOut(Money money);
}