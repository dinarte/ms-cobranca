package br.com.dfframeworck.controller;

import java.io.IOException;
import java.util.Objects;

import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
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
	
	@Value("${dfframeworck.development.auto_login}")
	private String autoLogin;
		
	@PostMapping("/login")
	public String login( Usuario usuario) throws LoginException {
		autenticacao.setUsuario(uRepo.findOneByEmailSenha(usuario.getEmail(), usuario.getPass()));
		return autenticacao.processarAutenticacao();	
	}

	@GetMapping({"/login","/"})
	public String login(Model model) throws LoginException, IOException {
		 if (Objects.nonNull(autoLogin)){ 
			 autenticacao.setUsuario(uRepo.findOneByEmail(autoLogin)); 
			 return autenticacao.processarAutenticacao(); 
		 }
		 
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
