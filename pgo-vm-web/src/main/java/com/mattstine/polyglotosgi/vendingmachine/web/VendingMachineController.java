package com.mattstine.polyglotosgi.vendingmachine.web;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Controller; 
import org.springframework.ui.ModelMap; 
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RequestParam;

import com.mattstine.polyglotosgi.vendingmachine.api.VendingMachine;
import com.mattstine.polyglotosgi.vendingmachine.api.Money;

import java.util.List;
import javax.annotation.Resource;

@Controller
public class VendingMachineController {
	
	@Autowired
	VendingMachineProvider vendingMachineProvider;
	
	VendingMachine currentVendingMachine;
	
	@RequestMapping("/vm.html")
	public String index(ModelMap model) {
		currentVendingMachine = vendingMachineProvider.getVendingMachines().get(0);
		updateModelMap(model, 0);
		return "index";
	}
	
	@RequestMapping("/selectVendingMachine.html")
	public String selectVendingMachine(@RequestParam("vendingMachine") int index, ModelMap model) {
		currentVendingMachine = vendingMachineProvider.getVendingMachines().get(index);
		updateModelMap(model, index);
		return "index";
	}
	
	@RequestMapping("/serviceVendingMachine.html")
	public String serviceVendingMachine(@RequestParam("vendingMachine") int index, ModelMap model) {
		currentVendingMachine.service();
		updateModelMap(model, index);
		return "index";
	}
	
	@RequestMapping("/insertMoney.html")
	public String insertMoney(@RequestParam("money") Money money, @RequestParam("vendingMachine") int index, ModelMap model) {
		currentVendingMachine.insert(money);
		updateModelMap(model, index);
		return "index";
	}
	
	@RequestMapping("/coinReturn.html")
	public String coinReturn(@RequestParam("vendingMachine") int index, ModelMap model) {
		String result = currentVendingMachine.coinReturn();
		model.addAttribute("output", result);
		updateModelMap(model, index);
		return "index";
	}
	
	@RequestMapping("/buyItem.html")
	public String buyItem(@RequestParam("item") String item, @RequestParam("vendingMachine") int index, ModelMap model) {
		String result = currentVendingMachine.buy(item);
		model.addAttribute("output", result);
		updateModelMap(model, index);
		return "index";
	}
	
	private void updateModelMap(ModelMap model, int index) {
		model.addAttribute("vendingMachines", vendingMachineProvider.getVendingMachines());
		model.addAttribute("bank", currentVendingMachine.showBank());
		model.addAttribute("inventory", currentVendingMachine.showInventory());
		model.addAttribute("moneyInserted", currentVendingMachine.currentMoneyInserted());
		model.addAttribute("amountInserted", currentVendingMachine.currentAmountInserted());
		model.addAttribute("index", index);
	}
	
}