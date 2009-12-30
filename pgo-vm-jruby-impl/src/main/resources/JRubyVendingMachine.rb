include Java

class JRubyVendingMachine
	include Java::com.mattstine.polyglotosgi.vendingmachine.api.VendingMachine

	def showInventory
		"[A:0, B:0, C:0]"
	end
	
	def showBank
		puts "Not Implemented yet!"
	end
	
	def service
		puts "Not Implemented yet!"
	end
	
	def currentMoneyInserted
		puts "Not Implemented yet!"
	end
	
	def insert
		puts "Not Implemented yet!"
	end
	
	def currentAmountInserted
		puts "Not Implemented yet!"
	end
	
	def coinReturn
		puts "Not Implemented yet!"
	end
	
	def buy
		puts "Not Implemented yet!"
	end
	
	def cashOut
		puts "Not Implemented yet!"
	end
end

JRubyVendingMachine.new