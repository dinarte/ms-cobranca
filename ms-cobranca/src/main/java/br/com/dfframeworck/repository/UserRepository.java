package br.com.dfframeworck.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.eflux.comum.domain.Usuario;

@Repository
public interface UserRepository  extends CrudRepository<Usuario, Long>{

	@Query("from Usuario where email = :email and pass = :pass")
	public Usuario findOneByEmailSenha(@Param("email") String email, @Param("pass") String pass);
	
	public Usuario findByEmail(String email);

	public Usuario findOneByEmail(String string);

}
