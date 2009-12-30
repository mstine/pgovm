package com.mattstine.polyglotosgi.vendingmachine.api;

import java.util.List;
import java.util.ArrayList;

public enum Money {
    NICKLE(5, "N", "Nickle"),
    DIME(10, "D", "Dime"),
    QUARTER(25, "Q", "Quarter"),    
    DOLLAR(100, "DOLLAR", "Dollar");

    Money(int amount, String symbol, String name) {
        this.amount = amount;
        this.symbol = symbol;
        this.name = name;
    }

    private int amount;
    private String symbol;
    private String name;

    public String toString() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public static List<Money> changeBackRange() {
        List<Money> changeBack = new ArrayList<Money>();
        changeBack.add(QUARTER);
        changeBack.add(DIME);
        changeBack.add(NICKLE);
        return changeBack;
    }
}