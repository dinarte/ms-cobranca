package br.com.eflux.financeiro.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.eflux.financeiro.domain.Acordo;
import br.com.eflux.financeiro.domain.Contrato;

@Repository
public interface AcordoRepository extends CrudRepository<Acordo, Long>{

	List<Acordo> findAllByContrato(Contrato contrato);
	

}
