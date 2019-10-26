package br.com.dfframeworckservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.dfframeworck.repository.UserRepository;
import br.com.eflux.comum.domain.Usuario;

/**
 * Service 
 * @author dinarte
 * chamado quando o sistema é usado pela primeira vez e executa tarefas 
 * de configuração básica do sistema.
 */

@Service
@Transactional
public class StartUpService {
	
	@Autowired 
	private UserRepository uRepo;

	public void startupSistema() {
		Usuario u = new Usuario();
		u.setAdmin(true);
		u.setName("Administrador do Sistema");
		u.setEmail("fluxincorporadora@gmail.com");
		u.setPass("flux,2009#");
		uRepo.save(u);
					
	}
	
}
