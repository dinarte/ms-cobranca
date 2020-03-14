package br.com.eflux.financeiro.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.eflux.comum.domain.Usuario;
import br.com.eflux.financeiro.domain.Boleto;
import br.com.eflux.financeiro.domain.ContaRecebimento;
import br.com.eflux.financeiro.domain.Contrato;
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
	
	@Query("from Debito where contrato = :contrato and situacao in :situacoes order by dataVencimento")
	public List<Debito> findAllByContratoAndSituacao(@Param("contrato") Contrato contrato, 
			@Param("situacoes") List<SituacaoDebitoEnum> situacaoes);

	@Query("from Debito where contrato = :contrato and situacao in :situacoes order by dataVencimento")
	public List<Debito> findAllByContratoAndSituacaoOrderByDataVencimento(@Param("contrato") Contrato contrato,
			@Param("situacoes") List<SituacaoDebitoEnum> passiveisAcordo);

	
	@Modifying
	@Query("update Debito "
			+ "set dataUltimoPagamento = :dataUltimoPagamento, "
			+ "valorPago = :valorPago, "
			+ "situacao = :situacao, "
			+ "usuarioBaixaManual = :usuario, "
			+ "motivoBaixaManual = :motivo "
		+ "where id = :id")
	public void updateBaixaManual(
			@Param("dataUltimoPagamento") Date dataUltimoPagamento, 
			@Param("valorPago") BigDecimal valorPago, 
			@Param("situacao") SituacaoDebitoEnum situacao, 
			@Param("usuario") Usuario usuario, 
			@Param("motivo") String motivo, 
			@Param("id") Long id);

}
