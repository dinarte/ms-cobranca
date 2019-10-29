package br.com.dfframeworck.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.RequestScope;

import br.com.dfframeworck.exception.AppException;
import br.com.dfframeworck.security.Functionality;

@Controller
@RequestScope
public class HomeController {

	@Functionality(isPublic=false, name="Página Principal", menu="root->home", icon="fa fa-dashboard")
	@RequestMapping({"/home"})
	public String home(Model m) throws AppException {
		return "pages/home"; 
	}
	
}
