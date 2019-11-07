package br.com.eflux.config.financeiro.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.eflux.config.financeiro.domain.ConfiguracaoBoletoConta;

@Repository
public interface ConfiguracaoBoletoContaRepository extends CrudRepository<ConfiguracaoBoletoConta, Long> {
	
	
}
