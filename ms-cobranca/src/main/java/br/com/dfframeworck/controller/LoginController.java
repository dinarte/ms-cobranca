package br.com.dfframeworck.controller;

import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.annotation.RequestScope;

import br.com.dfframeworck.messages.AppMessages;
import br.com.dfframeworck.repository.UserRepository;
import br.com.dfframeworck.security.Autenticacao;
import br.com.eflux.comum.domain.Usuario;

@Controller
@RequestScope
public class LoginController {

	@Autowired
	private Autenticacao autenticacao;
	
	@Autowired
	private UserRepository uRepo;
	
	@Autowired
	AppMessages appMessages;
	
		
	@PostMapping("/login")
	public String login( Usuario usuario, Model model) throws LoginException {
		autenticacao.setUsuario(uRepo.findOneByEmailSenha(usuario.getEmail(), usuario.getPass()));
		if ( autenticacao.getUsuario() != null) {
			autenticacao.setAutenticado(true);
			model.addAttribute(autenticacao);
			return "redirect:/home";
		}
		else {
			throw new LoginException("Não foi possível authenticar com os dados informados!");
		}	
	}
	
	
	@GetMapping({"/login","/"})
	public String login() throws LoginException {
		return "pages/login";
	}
	
	@GetMapping("/logout")
	public String logout(Model model) {
		String msg = autenticacao.getUsuario().getName().split(" ")[0] + ", "
				+ "Obrigado por utilizar o sistema!";
		appMessages.getSuccessList().add(msg);
		model.addAttribute(appMessages);
		autenticacao.setUsuario(null);
		autenticacao.setAutenticado(false);
		return "pages/login";
	}
	
}
