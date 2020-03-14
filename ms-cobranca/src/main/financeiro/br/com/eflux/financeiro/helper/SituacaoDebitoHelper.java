package br.com.eflux.financeiro.helper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import br.com.eflux.financeiro.domain.Debito;
import br.com.eflux.financeiro.domain.SituacaoDebitoEnum;

/**
 * Prover situacoes de debito sob um ponto de vista das regras de negócio
 * @author dinarte
 *
 */
public class SituacaoDebitoHelper {
	
	
	/**
	 * Situações ativas
	 * @return
	 */
	public static List<SituacaoDebitoEnum> getAtivas() {
		return Arrays.asList( new SituacaoDebitoEnum[]{ 
				SituacaoDebitoEnum.EM_ABERTO,
				SituacaoDebitoEnum.VENCIDA,
				SituacaoDebitoEnum.QUITADA} );
		
	}
	
	
	/**
	 * Retorna as situações dos dábitos que os qualificam a entrarem em um acordo.
	 * @return
	 */
	public static List<SituacaoDebitoEnum> getPassiveisAcordo(){
		return Arrays.asList( new SituacaoDebitoEnum[]{ 
				SituacaoDebitoEnum.VENCIDA, 
				SituacaoDebitoEnum.EM_ABERTO} );
	}
	
	
	/**
	 * Retorna as situações dos dábitos que NÃO são passíveis de quitação.
	 * @return
	 */
	public static List<SituacaoDebitoEnum> getNaoPassiveisQuitacao(){
		return Arrays.asList( new SituacaoDebitoEnum[]{ 
				SituacaoDebitoEnum.QUITADA,
				SituacaoDebitoEnum.CANCELADA,
				SituacaoDebitoEnum.NEGOCIADA } );
	}
	
	
	/**
	 * Retorna as situações dos dábitos que os qualificam a entrarem em um acordo em formato string.
	 * @return
	 */
	public static List<String> getPassiveisAcordoStr(){
		return getPassiveisAcordo().stream().map(SituacaoDebitoEnum::name).collect(Collectors.toList());
	}
	
	/**
	 * Retorna as situações que são passíveis de Juros e Multa
	 * @return
	 */
	public static List<SituacaoDebitoEnum> getPassiveisDeJurosEMulta(){
		return Arrays.asList( new SituacaoDebitoEnum[]{ 
				SituacaoDebitoEnum.VENCIDA} );
	}


	/**
	 * Retorna todas as situações possíveis
	 * @return
	 */
	public static List<SituacaoDebitoEnum> getAll() {
		return Arrays.asList( SituacaoDebitoEnum.values() );
	}


	/**
	 * Situações nas quais os Débitos ainda estão à serem pagos
	 * @return
	 */
	public static  List<SituacaoDebitoEnum> getAPagar() {
		return Arrays.asList( new SituacaoDebitoEnum[]{ 
				SituacaoDebitoEnum.EM_ABERTO,
				SituacaoDebitoEnum.VENCIDA} );
	}


	
	

}
