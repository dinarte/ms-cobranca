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
	
	
	
	@Query(value="select id_contrato as idContrato, \n" + 
			"		count(*) as qtdVencidas\n" + 
			"from financeiro.debito c\n" + 
			"where c.situacao = 'VENCIDA'\n" + 
			"group by id_contrato", nativeQuery=true)
	public List<ContratoComDebitosVencidosVo> findAllContratosComDebitoVencidosVo();

	@Modifying
	@Query(value="update Contrato set quantidadeParcelasVencidas = :qtdVencidas where id = :idContrato")
	public void updateQuantidadeDebitosVencidos(@Param("idContrato") Long idContrato, @Param("qtdVencidas") Long qtdVencidas);	
	

}
