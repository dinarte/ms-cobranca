package br.com.dfframeworckservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.dfframeworck.repository.UserRepository;
import br.com.eflux.comum.domain.Usuario;

@Transactional
@Service
public class SingUpService {

	@Autowired
	UserRepository userRepo;
		
	
	/**
	 * Cria um usuario, o participante e os palpites 
	 * @param usuario
	 */
	public void criarUsuario(Usuario usuario) {	
		usuario.setAdmin(false);
		userRepo.save(usuario);
	}

	
}