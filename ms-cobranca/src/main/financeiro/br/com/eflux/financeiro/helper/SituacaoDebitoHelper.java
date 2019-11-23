package br.com.eflux.financeiro.helper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import br.com.eflux.financeiro.domain.SituacaoDebitoEnum;

/**
 * Prover situacoes de debito sob um ponto de vista das regras de negócio
 * @author dinarte
 *
 */
public class SituacaoDebitoHelper {
	
	/**
	 * Retorna as situações dos dábitos que os qualificam a entrarem em um acordo.
	 * @return
	 */
	public static List<SituacaoDebitoEnum> getPassiveisAcordo(){
		return Arrays.asList( new SituacaoDebitoEnum[]{ 
				SituacaoDebitoEnum.VENCIDA} );
	}
	
	
	/**
	 * Retorna as situações dos dábitos que os qualificam a entrarem em um acordo em formato string.
	 * @return
	 */
	public static List<String> getPassiveisAcordoStr(){
		return getPassiveisAcordo().stream().map(SituacaoDebitoEnum::name).collect(Collectors.toList());
	}

}
