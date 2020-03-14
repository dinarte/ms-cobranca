package br.com.eflux.financeiro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.eflux.financeiro.domain.Contrato;
import br.com.eflux.financeiro.vo.ContratoComDebitosVencidosVo;

@Repository
public interface ContratoRepository extends CrudRepository<Contrato, Long> {
	
	
	
	@Query(value="select contrato.id_contrato idContrato, count(debito.id_contrato) qtdVencidas \n" + 
			"from financeiro.contrato \n" + 
			"left join financeiro.debito on debito.id_contrato = contrato.id_contrato  \n" + 
			"and debito.situacao = 'VENCIDA' \n" + 
			"group by contrato.id_contrato", nativeQuery=true)
	public List<ContratoComDebitosVencidosVo> findAllContratosComDebitoVencidosVo();

	@Modifying
	@Query(value="update Contrato set quantidadeParcelasVencidas = :qtdVencidas where id = :idContrato")
	public void updateQuantidadeDebitosVencidos(@Param("idContrato") Long idContrato, @Param("qtdVencidas") Long qtdVencidas);

}
