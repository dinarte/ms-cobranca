package br.com.dfframeworck.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.dfframeworckservice.MenuItemService;

@Controller
public class FuncionalidadeController {
	
	MenuItemService menuItemService;
	
	@RequestMapping("/funcionalidades")
	public String funcionalidades(Model model) {
		model.addAttribute("menuItems", menuItemService.getMenuItens());
		return "development/funcionalidades";
	}

}
