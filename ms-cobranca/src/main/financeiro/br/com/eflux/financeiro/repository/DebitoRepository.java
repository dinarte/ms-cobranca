package br.com.eflux.financeiro.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.eflux.financeiro.domain.Boleto;
import br.com.eflux.financeiro.domain.ContaRecebimento;
import br.com.eflux.financeiro.domain.Debito;
import br.com.eflux.financeiro.domain.SituacaoDebitoEnum;

@Repository
public interface DebitoRepository extends CrudRepository<Debito, Long>{
	
	public List<Debito> findByContaRecebimento(ContaRecebimento contaRecebimento);

	public List<Debito> findAllByDataCriacaoInvoiceIsNotNullAndInvoiceIsNull();

	@Query("select d from Debito d where d.situacao = :situacao and dataVencimento < :hoje")
	public List<Debito> findAllVencidos(@Param("hoje") Date hoje, @Param("situacao") SituacaoDebitoEnum situacao);

	public Boolean existsByInvoice(Boleto invoice);

	public Debito findTop1ByInvoice(Boleto invoice);

}
