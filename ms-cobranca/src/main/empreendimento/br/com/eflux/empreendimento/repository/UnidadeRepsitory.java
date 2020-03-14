package br.com.eflux.empreendimento.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.eflux.empreendimento.domain.Unidade;
import br.com.eflux.empreendimento.domain.Zona;

@Repository
public interface UnidadeRepsitory extends CrudRepository<Unidade, Long> {

	List<Unidade> findAllByZonaOrderByNome(Zona z);

}
