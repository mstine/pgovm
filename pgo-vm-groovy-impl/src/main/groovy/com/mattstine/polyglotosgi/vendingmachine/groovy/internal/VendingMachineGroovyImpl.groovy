package com.mattstine.polyglotosgi.vendingmachine.groovy.internal

import com.mattstine.polyglotosgi.vendingmachine.api.Money
import com.mattstine.polyglotosgi.vendingmachine.api.VendingMachine

public class VendingMachineGroovyImpl implements VendingMachine {

  Map moneyInserted = [(Money.NICKLE): 0, (Money.DIME): 0, (Money.QUARTER): 0, (Money.DOLLAR): 0]
  Map bank = [(Money.NICKLE): 0, (Money.DIME): 0, (Money.QUARTER): 0, (Money.DOLLAR): 0]
  Map inventory = [A: 0, B: 0, C: 0]
  List coinOrder = []
  Map prices = [A: 65, B: 100, C: 150]


  String showInventory() {
    inventory.toString()
  }

  String showBank() {
    bank.toString()
  }

  void service() {
    bank = [(Money.NICKLE): 50, (Money.DIME): 50, (Money.QUARTER): 50, (Money.DOLLAR): 0]
    inventory = [A: 10, B: 10, C: 10]
  }

  String currentMoneyInserted() {
    moneyInserted.toString()
  }

  void insert(Money money) {
    moneyInserted[money]++
    coinOrder << money.symbol
  }

  int currentAmountInserted() {
    moneyInserted[Money.QUARTER] * Money.QUARTER.amount +
            moneyInserted[Money.DIME] * Money.DIME.amount +
            moneyInserted[Money.NICKLE] * Money.NICKLE.amount +
            moneyInserted[Money.DOLLAR] * Money.DOLLAR.amount
  }

  String coinReturn() {
    def coins = coinOrder.join(", ")
    coinOrder = []
    moneyInserted = [(Money.NICKLE): 0, (Money.DIME): 0, (Money.QUARTER): 0, (Money.DOLLAR): 0]
    coins
  }

  String buy(String item) {
    if (!customerInsertedEnoughMoneyFor(item))
      throw new Exception("Sorry, you didn't insert enough money to purchase ${item}!")

    if (!isInStock(item))
      throw new Exception("Sorry, ${item} is not in stock!")

    addMoneyInsertedToTheBank()

    String toVend
    if (customerIsDueChange(item)) {
      String change
      try {
        change = makeChange(item)
      } catch (Exception e) {
        removeMoneyInsertedFromTheBank()
        throw (e)
      }
      toVend = "${item}, ${change}"
    } else {
      toVend = item
    }

    inventory[item]--
    moneyInserted = [(Money.NICKLE): 0, (Money.DIME): 0, (Money.QUARTER): 0, (Money.DOLLAR): 0]
    return toVend

  }

  boolean isInStock(String item) {
    return inventory[item] > 0
  }

  boolean customerInsertedEnoughMoneyFor(String item) {
    return currentAmountInserted() >= prices[item]
  }

  boolean customerIsDueChange(String item) {
    return currentAmountInserted() > prices[item]
  }

  void removeMoneyInsertedFromTheBank() {
    moneyInserted.each {k, v ->
      bank[k] -= v
    }
  }

  void addMoneyInsertedToTheBank() {
    moneyInserted.each {k, v ->
      bank[k] += v
    }
  }

  String makeChange(String item) {
    def changeAmount = currentAmountInserted() - prices[item]
    List coinList = []

    def coins = Money.QUARTER..Money.NICKLE
    coins.each {coin ->
      int numOfCoins = changeAmount / coin.amount

      if (bank[coin] < numOfCoins) {
        numOfCoins = bank[coin] as Integer
      }

      if (numOfCoins > 0) {
        1.upto(numOfCoins) {
          bank[coin]--
          coinList << coin.symbol
          changeAmount -= coin.amount
        }
      }
    }

    if (changeAmount > 0) {
      throw new Exception("Sorry, not enough change available to purchase ${item} with this set of inserted money!")
    }

    coinList.join(", ")
  }

  public void cashOut(Money money) {
    bank[money] = 0
  }
}