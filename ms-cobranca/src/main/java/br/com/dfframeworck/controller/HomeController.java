package br.com.dfframeworck.controller;

import org.springframework.ui.Model;

import br.com.dfframeworck.exception.AppException;

//@Controller
//@RequestScope
public class HomeController {

//	@Functionality(isPublic=false, name="Página Principal", menu="root->home", icon="fa fa-dashboard")
//	@RequestMapping({"/home"})
	public String home(Model m) throws AppException {
		return "pages/home"; 
	}
	
}
