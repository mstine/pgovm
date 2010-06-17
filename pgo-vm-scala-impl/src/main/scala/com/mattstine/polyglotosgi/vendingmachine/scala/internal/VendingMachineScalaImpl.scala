package com.mattstine.polyglotosgi.vendingmachine.scala.internal

import _root_.java.util.Iterator
import collection.jcl.MutableIterator.Wrapper
import collection.mutable.Map
import com.mattstine.polyglotosgi.vendingmachine.api.{VendingMachine, Money}

class VendingMachineScalaImpl extends VendingMachine {
  implicit def javaIteratorToScalaIterator[A](it: Iterator[A]) = new Wrapper(it)

  var bank = Map(Money.NICKLE -> 0, Money.DIME -> 0, Money.QUARTER -> 0, Money.DOLLAR -> 0)
  var moneyInserted = Map(Money.NICKLE -> 0, Money.DIME -> 0, Money.QUARTER -> 0, Money.DOLLAR -> 0)
  var inventory = Map("A" -> 0, "B" -> 0, "C" -> 0)
  var coinOrder = List[String]()
  val prices = Map("A" -> 65, "B" -> 100, "C" -> 150)

  def service = {
    bank = Map(Money.NICKLE -> 50, Money.DIME -> 50, Money.QUARTER -> 50, Money.DOLLAR -> 0)
    inventory = Map("A" -> 10, "B" -> 10, "C" -> 10)
  }

  def cashOut(money: Money) = {
    bank(money) = 0
  }

  def insert(money: Money) = {
    moneyInserted(money) += 1
    coinOrder = money.getSymbol() :: coinOrder
  }

  def showInventory = ("[A:" ++ inventory("A").toString() ++ ", B:" ++ inventory("B").toString() ++ ", C:" ++ inventory("C").toString ++ "]").toString()

  def showBank =
    ("[" ++ Money.NICKLE.toString() ++ ":" ++ bank(Money.NICKLE).toString() ++ ", " ++ Money.DIME.toString() ++ ":" ++ bank(Money.DIME).toString() ++ ", " ++
            Money.QUARTER.toString() ++ ":" ++ bank(Money.QUARTER).toString() ++ ", " ++ Money.DOLLAR.toString() ++ ":" ++ bank(Money.DOLLAR).toString() ++ "]").toString()

  def coinReturn = {
    coinOrder = coinOrder.reverse
    val coinReturnString = coinOrder.mkString(", ")
    coinOrder = Nil

    moneyInserted = Map(Money.NICKLE -> 0, Money.DIME -> 0, Money.QUARTER -> 0, Money.DOLLAR -> 0)

    coinReturnString
  }

  def currentAmountInserted = moneyInserted(Money.NICKLE) * Money.NICKLE.getAmount() + moneyInserted(Money.DIME) * Money.DIME.getAmount +
          moneyInserted(Money.QUARTER) * Money.QUARTER.getAmount() + moneyInserted(Money.DOLLAR) * Money.DOLLAR.getAmount()

  def currentMoneyInserted = ("[" ++ Money.NICKLE.toString() ++ ":" ++ moneyInserted(Money.NICKLE).toString() ++ ", " ++ Money.DIME.toString() ++ ":" ++ moneyInserted(Money.DIME).toString() ++ ", " ++
          Money.QUARTER.toString() ++ ":" ++ moneyInserted(Money.QUARTER).toString() ++ ", " ++ Money.DOLLAR.toString() ++ ":" ++ moneyInserted(Money.DOLLAR).toString() ++ "]").toString()

  def buy(item: String) = {
    if (!(inventory(item) > 0))
      throw new RuntimeException("Sorry, " + item + " is not in stock!")

    if (!(currentAmountInserted() >= prices(item)))
      throw new RuntimeException("Sorry, you didn't insert enough money to purchase " + item + "!")

    for ((key, value) <- moneyInserted) bank(key) += value

    var toVend : String = null
    if (currentAmountInserted > prices(item)) {
      var change : String = null
      try {
        change = makeChange(item)
      } catch {
        case ex: Exception => {
          for ((key, value) <- moneyInserted) bank(key) -= value
          throw(ex)
        }
      }
      toVend = item + ", " + change
    } else {
      toVend = item
    }

    inventory(item) -= 1
    moneyInserted = Map(Money.NICKLE -> 0, Money.DIME -> 0, Money.QUARTER -> 0, Money.DOLLAR -> 0)
    toVend                            
  }

  def makeChange(item: String) : String = {
    var changeAmount = currentAmountInserted() - prices(item)
    var coinList = List[String]()

    for (coin <- Money.changeBackRange().iterator()) {
      var numOfCoins = changeAmount / coin.getAmount()

      if (bank(coin) < numOfCoins)
        numOfCoins = bank(coin)

      if (numOfCoins > 0) {
        for (i <- 1 to numOfCoins) {
          bank(coin) -= 1
          coinList = coin.getSymbol() :: coinList
          changeAmount -= coin.getAmount()
        }
      }
    }

    if (changeAmount > 0)
      throw new RuntimeException("Sorry, not enough change available to purchase " + item + " with this set of inserted money!")


    coinList = coinList.reverse
    return coinList.mkString(", ")
  }
}