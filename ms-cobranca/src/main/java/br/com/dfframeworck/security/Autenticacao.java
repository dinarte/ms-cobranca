package br.com.dfframeworck.security;

import javax.security.auth.login.LoginException;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import br.com.eflux.comum.domain.Usuario;

@Component
@Scope("session")
public class Autenticacao{

	private Usuario usuario = new Usuario();
	
	private  boolean autenticado;

	public Usuario getUsuario() {
		return usuario;
	}
	
	public void checkAdminAthorization() throws LoginException {
		checkAthorization();
		if (getUsuario().getAdmin()!= true)
			throw new LoginException("Você precisa ser admin para acessar esta operação");
	}
	
	public void checkAdminAthorization(Model model) throws LoginException {
		checkAdminAthorization();
		model.addAttribute(this);
	}
	
	public void checkAthorization() throws LoginException {
		if (!isAutenticado())
			throw new LoginException("Você precisa estar autenticado para acessar esta operação, "
					+ "você não éfetuou login ou seu tempo de conexão expirou");
	}
	

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public boolean isAutenticado() {
		return autenticado;
	}

	public void setAutenticado(boolean autenticado) {
		this.autenticado = autenticado;
	}
}
