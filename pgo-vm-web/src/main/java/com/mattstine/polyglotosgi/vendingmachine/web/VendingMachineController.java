package com.mattstine.polyglotosgi.vendingmachine.web;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Controller; 
import org.springframework.ui.ModelMap; 
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RequestMethod;

import com.mattstine.polyglotosgi.vendingmachine.api.VendingMachine;
import com.mattstine.polyglotosgi.vendingmachine.api.Money;

@Controller
@RequestMapping("/vm.html")
public class VendingMachineController {
	
	@Autowired
	VendingMachine vendingMachine;
	
	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute("impl", vendingMachine.getClass().getName());
		model.addAttribute("bank", vendingMachine.showBank());
		return "index";
	}
	
}