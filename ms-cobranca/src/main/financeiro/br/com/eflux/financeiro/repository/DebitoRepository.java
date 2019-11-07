package br.com.eflux.financeiro.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.eflux.financeiro.domain.ContaRecebimento;
import br.com.eflux.financeiro.domain.Debito;

@Repository
public interface DebitoRepository extends CrudRepository<Debito, Long>{
	
	public List<Debito> findByContaRecebimento(ContaRecebimento contaRecebimento);

	public List<Debito> findAllByDataCriacaoInvoiceIsNotNullAndInvoiceIsNull();

}
