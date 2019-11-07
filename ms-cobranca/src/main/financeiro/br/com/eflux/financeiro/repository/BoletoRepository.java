package br.com.eflux.financeiro.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.eflux.financeiro.domain.Boleto;

@Repository
public interface BoletoRepository extends CrudRepository<Boleto, Long>{
	

}
