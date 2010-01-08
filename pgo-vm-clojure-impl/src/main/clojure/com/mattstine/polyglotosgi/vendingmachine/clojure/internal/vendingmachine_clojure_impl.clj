(ns com.mattstine.polyglotosgi.vendingmachine.clojure.internal.vendingmachine-clojure-impl
	(:gen-class
	 :implements [com.mattstine.polyglotosgi.vendingmachine.api.VendingMachine]
	 :state state
	 :init init)
	(:import [com.mattstine.polyglotosgi.vendingmachine.api.VendingMachine]
		       [com.mattstine.polyglotosgi.vendingmachine.api.Money]))

(defstruct money-state (. com.mattstine.polyglotosgi.vendingmachine.api.Money NICKLE) 
	(. com.mattstine.polyglotosgi.vendingmachine.api.Money DIME) 
	(. com.mattstine.polyglotosgi.vendingmachine.api.Money QUARTER) 
	(. com.mattstine.polyglotosgi.vendingmachine.api.Money DOLLAR))
	
(defstruct item-state "A" "B" "C")

(defn -init []
	(let [init-money-inserted (struct money-state 0 0 0 0)
			  init-bank (struct money-state 0 0 0 0)
			  init-inventory (struct item-state 0 0 0)
			  init-prices (struct item-state 65 100 150)]
	[[] (atom {:money-inserted init-money-inserted :bank init-bank :inventory init-inventory :coin-order [] :prices init-prices})]))

(defn build-money-string [money-map]
	(str "["
		(apply str 
			(interpose ", ", (for [[key value] money-map]
				(str (.getName key) ":" value)))) "]"))

(defn build-item-string [item-map]
	(str "["
		(apply str
			(interpose ", ", (for [[key value] item-map]
				(str key ":" value)))) "]"))

(defn -showInventory [this]
	(build-item-string (@(.state this) :inventory)))

(defn -showBank [this]
	(build-money-string  (@(.state this) :bank)))

(defn -service [this]
	 (swap! (.state this) assoc :bank (struct money-state 50 50 50 0) :inventory (struct item-state 10 10 10)))

(defn -currentMoneyInserted [this]
	(build-money-string (@(.state this) :money-inserted)))

(defn -insert [this, money]
	(let [old-amount-inserted (@(.state this) :money-inserted) 
		    new-amount-inserted (assoc old-amount-inserted money (+ 1 (old-amount-inserted money)))
				old-coin-order (@(.state this) :coin-order)
				new-coin-order (conj old-coin-order (.getSymbol money))]
		(swap! (.state this) assoc :money-inserted new-amount-inserted :coin-order new-coin-order)))

(defn -currentAmountInserted [this]
	(reduce + (for [[coin, amount] (@(.state this) :money-inserted)] (* (.getAmount coin) amount))))
	
(defn -coinReturn [this]
	(let [coin-return (@(.state this) :coin-order)]
		(swap! (.state this) assoc :money-inserted (struct money-state 0 0 0 0) :coin-order [])
			(apply str
				(interpose ", ", coin-return))))
	
(defn -cashOut [this, money]
	(let [old-bank (@(.state this) :bank)
		    new-bank (assoc old-bank money 0)]
		(swap! (.state this) assoc :bank new-bank)))
		
(defn customer-inserted-enough-money-for? [this, item]
	(let [price ((@(.state this) :prices) item)]
		(>= (.currentAmountInserted this) price)))
		
(defn is-in-stock? [this, item]
	(let [item-stock ((@(.state this) :inventory) item)]
		(> item-stock 0)))
		
(defn add-money-inserted-to-bank [bank, money-inserted]
	(loop [coins (keys bank) coin (first(keys bank)) new-bank bank]
		(if (empty? coins) new-bank
			(recur (rest coins) (first(rest coins)) (assoc new-bank coin (+ (bank coin) (money-inserted coin)))))))		
			
(defn remove-money-inserted-from-bank [bank, money-inserted]
	(loop [coins (keys bank) coin (first(keys bank)) new-bank bank]
		(if (empty? coins) new-bank
			(recur (rest coins) (first(rest coins)) (assoc new-bank coin (- (bank coin) (money-inserted coin)))))))
			
(defn customer-is-due-change? [this, item]
	(let [price ((@(.state this) :prices) item)]
		(> (.currentAmountInserted this) price)))
		
(defn make-change-for-coin [this, coin, num-of-coins, bank, coin-list, change-amount]
		(loop [rem-coins num-of-coins
					 loop-state {:bank bank :coin-list coin-list :change-amount change-amount}]
			(if (zero? rem-coins) loop-state
				(let [new-bank (loop-state :bank)
					    new-coin-list (loop-state :coin-list)
					    rem-change-amount (loop-state :change-amount)]
				(recur (dec rem-coins)
					 		 {:bank (assoc new-bank coin (- (new-bank coin) 1)) 
					      :coin-list (conj new-coin-list (.getSymbol coin))
					      :change-amount (- rem-change-amount (.getAmount coin))})))))		
		
(defn make-change [this, item]
	(let [change-data (let [bank (@(.state this) :bank)
		    item-price ((@(.state this) :prices) item)
		    amount-inserted (.currentAmountInserted this)
		    total-change-amount (- amount-inserted item-price)
				coin-range [(com.mattstine.polyglotosgi.vendingmachine.api.Money/QUARTER),
				       (com.mattstine.polyglotosgi.vendingmachine.api.Money/DIME),
				       (com.mattstine.polyglotosgi.vendingmachine.api.Money/NICKLE)]]
		(loop [coins coin-range 
			     coin (first coin-range)
			     change-amount total-change-amount 
			     needed-coins (if (nil? coin) 0 (quot change-amount (.getAmount coin)))
					 num-of-coins (if (zero? needed-coins) 0 (if (< (bank coin) needed-coins) (bank coin) needed-coins))
					 loop-state (make-change-for-coin this coin num-of-coins bank [] change-amount)]
			(if (empty? coins) loop-state
				(let [r-coins (rest coins)
					    r-coin (first r-coins)
					    r-change-amount (loop-state :change-amount)
							r-needed-coins (if (nil? r-coin) 0 (quot r-change-amount (.getAmount r-coin)))
							r-num-of-coins (if (zero? r-needed-coins) 0 (if (< (bank r-coin) r-needed-coins) (bank r-coin) r-needed-coins))   
							r-loop-state (make-change-for-coin this r-coin r-num-of-coins (loop-state :bank) (loop-state :coin-list) r-change-amount)]
						(recur r-coins r-coin r-change-amount r-needed-coins r-num-of-coins r-loop-state))
				)))]
		(if (> (change-data :change-amount) 0)
			(throw (Exception. (str "Sorry, not enough change available to purchase " item " with this set of inserted money!")))
			(do 
				(swap! (.state this) assoc :bank (change-data :bank))
				(apply str
					(interpose ", ", (change-data :coin-list)))))))

(defn -buy [this, item]
	(if 
		(not 
			(customer-inserted-enough-money-for? this item)) 
			(throw (Exception. (str "Sorry, you didn't insert enough money to purchase " item "!"))))
	(if
		(not
			(is-in-stock? this item))
			(throw (Exception. (str "Sorry, " item " is not in stock!"))))
	
	(let [old-bank (@(.state this) :bank)
		    money-inserted (@(.state this) :money-inserted)
		    new-bank (add-money-inserted-to-bank old-bank money-inserted)]
			(swap! (.state this) assoc :bank new-bank))
			
	(let [to-vend (if (customer-is-due-change? this item)
									(try
										(str item ", " (make-change this item))
										(catch Exception e 
											(let [old-bank (@(.state this) :bank)
												    money-inserted (@(.state this) :money-inserted)
												    new-bank (remove-money-inserted-from-bank old-bank money-inserted)]
													(swap! (.state this) assoc :bank new-bank))
											 (throw e))
									) item)]
		
		(let [old-inventory (@(.state this) :inventory)
			    new-inventory (assoc old-inventory item (- (old-inventory item) 1))]
			(swap! (.state this) assoc :inventory new-inventory))

		(swap! (.state this) assoc :money-inserted (struct money-state 0 0 0 0))
		
		to-vend))