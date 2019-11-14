package br.com.eflux.financeiro.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.eflux.config.financeiro.domain.ConfiguracaoBoletoConta;
import br.com.eflux.financeiro.domain.ContaRecebimento;

@Repository
public interface ConfiguracaoInvoiceContaRepository extends CrudRepository<ConfiguracaoBoletoConta, Long>{
	

	ConfiguracaoBoletoConta findTop1ByContaRecebimento(ContaRecebimento contaRecebimento);
	
}
