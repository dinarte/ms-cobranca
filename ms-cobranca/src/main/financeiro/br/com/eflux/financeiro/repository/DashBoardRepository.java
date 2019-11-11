package br.com.eflux.financeiro.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.eflux.financeiro.domain.Contrato;
import br.com.eflux.financeiro.vo.InadiplentesVo;
import br.com.eflux.financeiro.vo.IndicadoresVo;
import br.com.eflux.financeiro.vo.VencimentosVo;

@Repository
public interface DashBoardRepository extends JpaRepository<Contrato, Long> {
	
	
	static final String SQL_VENCIMENTOS =
			"select *, \n" + 
			"    case \n" + 
			"      when coalesce(valorPago,0)=0 and dataVencimento < now() then valorAtualizado\n" + 
			"      else 0\n" + 
			"    end as valorVencido    \n" + 
			"from (\n" + 
			"	select	contrato.id_contrato as idContrato,\n" + 
			"			  			contrato.numero_contrato ||' - '|| pessoa.nome as contrato,  \n" + 
			"			            tipo_lancamento.nome as tipoDebito, \n" + 
			"			            debito.id_debito as idDebito,\n" + 
			"			            debito.numero,\n" + 
			"			            coalesce(valor_original,0) +  \n" + 
			"			            coalesce(multa_atrazo,0) +  \n" + 
			"			            coalesce(juros_atrazo,0) + \n" + 
			"			            coalesce(correcao,0) +\n" + 
			"			            coalesce(juros_remuneratorio,0) as valorAtualizado,  \n" + 
			"    					coalesce(valor_pago,0) as valorPago,\n" + 
			"			            debito.data_vencimento as dataVencimento,\n" + 
			"			            e.nome as empreendimento\n" + 
			"			  from financeiro.contrato \n" + 
			"			  join financeiro.situacao_contrato  using(id_situacao_contrato)\n" + 
			"			  left join financeiro.debito  using(id_contrato)\n" + 
			"    		  left join financeiro.tipo_lancamento using(id_tipo_lancamento)\n" + 
			"			  join empreendimento.unidade u using(id_unidade)\n" + 
			"			  join empreendimento.zona z using(id_zona)\n" + 
			"			  join empreendimento.empreendimento e using(id_empreendimento)\n" + 
			"			  join comum.pessoa using(id_pessoa) \n" + 
			"			  where situacao_contrato.contrato_ativo\n" + 
			"             and (debito.data_vencimento between :inicio and :fim or debito.id_debito is null)  \n" + 
			"			  order by data_vencimento asc \n" + 
			"   )sql_debitos";
	
	
	
	
	@Query(value="	select 	count(distinct idContrato) as contratosAtivos, \n" + 
			"				sum(valorAtualizado) as receitaPrevista, \n" + 
			"				sum(valorPago) as receitaRealizada, \n" + 
			"    			sum(valorVencido) as valorVencido \n" + 
			"		from  ("+SQL_VENCIMENTOS+") as foo",
			nativeQuery=true)
	public IndicadoresVo findIndicadoresByPeriodo(@Param("inicio") Date inicio, @Param("fim") Date fim);
	
	
	
	
	@Query(value="select empreendimento,\n" + 
			"	count(distinct idContrato) as contratosAtivos ,	\n" + 
			"	sum(valorAtualizado) as receitaPrevista, \n" + 
			"	sum(valorPago) as receitaRealizada,\n" + 
			"    sum(valorVencido) as valorVencido\n" + 
			"from ("+SQL_VENCIMENTOS+") as foo\n" + 
			"group by empreendimento\n" + 
			"order by empreendimento\n",
			nativeQuery=true)
	public List<IndicadoresVo> findIndicadoresPorEmpreendimentoByPeriodo(@Param("inicio") Date inicio, @Param("fim") Date fim);
	
	
	
	@Query(value = SQL_VENCIMENTOS, nativeQuery=true)
	public List<VencimentosVo> findDebitosVencidosByPeriodo(@Param("inicio") Date inicio, @Param("fim") Date fim);
	
	@Query(value = SQL_VENCIMENTOS + " limit :maxRows", nativeQuery=true)
	public List<VencimentosVo> findDebitosVencidosByPeriodoLimit(@Param("inicio") Date inicio, @Param("fim") Date fim, @Param("maxRows") int maxRows);
	
	@Query(value = "	select contrato, count(*) debitos, sum(valorAtualizado) as valorAtualizado   \n" + 
					"	from (" + SQL_VENCIMENTOS + ") as foo\n" + 
					" 	group by idContrato, contrato\n" + 
					" 	order by 2 desc "
					+ " limit :maxRows", nativeQuery=true)
	public List<InadiplentesVo> findInadlipentesByPeriodoLimit(@Param("inicio") Date inicio, @Param("fim") Date fim, @Param("maxRows") int maxRows);
	
	
	@Query(value = "	select empreendimento, sum(valorAtualizado) as valorAtualizado   \n" + 
			"	from (" + SQL_VENCIMENTOS + ") as foo\n" + 
			" 	group by empreendimento" + 
			" 	order by 2 desc ", nativeQuery=true)
public List<Map<String, Object>> findInadlipentesPorEmpreendimentoByPeriodoLimit(@Param("inicio") Date inicio, @Param("fim") Date fim);
	

}
