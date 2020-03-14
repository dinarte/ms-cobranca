package br.com.eflux.empreendimento.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.eflux.empreendimento.domain.Empreendimento;
import br.com.eflux.empreendimento.domain.Zona;

@Repository
public interface ZonaRepository extends CrudRepository<Zona, Long>{
	
	
	public List<Zona> findAllByEmpreendimentoOrderByNivel(Empreendimento empreendimento);

	public List<Zona> findAllByEmpreendimentoOrderByNivelAscNomeAsc(Empreendimento e);
	
	public List<Zona> findByZonaPaiOrderByNome(Zona zona);

	public List<Zona> findAllByEmpreendimentoAndZonaPaiIsNull(Empreendimento e);

}
