package br.com.eflux.financeiro.dto;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.eflux.config.financeiro.domain.ConfiguracaoAcordo;
import br.com.eflux.financeiro.domain.Acordo;
import br.com.eflux.financeiro.domain.AcordoDebito;
import br.com.eflux.financeiro.domain.Contrato;
import br.com.eflux.financeiro.domain.Debito;
import br.com.eflux.financeiro.domain.OrigemDebitoAcordoEnum;
import br.com.eflux.financeiro.domain.SituacaoDebitoEnum;
import br.com.eflux.financeiro.domain.StatusAcordoEnum;

/**
 * Mapeador responsável por criar DTOs para Acordos.
 * @author dinarte
 *
 */
public class AcordoMapper {
	
	/**
	 * Cria um dto a partir de um acordo já popoulado
	 * @param acordo
	 * @return
	 */
	public static AcordoDTO dtoFromObject(Acordo acordo) {
		List<Debito> debitos =  acordo.getDebitos().stream().filter(ad -> ad.getOrigem().equals(OrigemDebitoAcordoEnum.CONTRATO)).map(AcordoDebito::getDebito).collect(Collectors.toList());
		List<Debito> parcelas =  acordo.getDebitos().stream().filter(ad -> ad.getOrigem().equals(OrigemDebitoAcordoEnum.ACORDO)).map(AcordoDebito::getDebito).collect(Collectors.toList());
		Optional<Debito> entrada = parcelas.stream().filter(d->d.getTipoLancamento().equals(acordo.getConfiguracao().getTipoLancamentoEntrada())).findFirst();
		List<Debito> parcelasSemEntrada = parcelas.stream().filter(d->d.getTipoLancamento().equals(acordo.getConfiguracao().getTipoLancamentoEntrada())).collect(Collectors.toList());
		
		parcelas = parcelas.stream()
				.sorted(Comparator.comparingInt(Debito::getNumero))
				.sorted(Comparator.comparing(Debito::getDataVencimento)).collect(Collectors.toList());
		
		
		AcordoDTO dto = new AcordoDTO();
		dto.setContrato(acordo.getContrato());
		dto.setConfiguracao(acordo.getConfiguracao());
		dto.setTotalAcordo(acordo.getValorAcordado());
		dto.setDebitosDoAcordo(debitos);
		dto.setParcelasDoAcordo(parcelas);
		dto.setQuantidadeParcelas(parcelasSemEntrada.size());
		dto.setValorEntrada(entrada.orElse(new Debito()).getValorOriginal());	
		return dto;
	}

	/**
	 * Cria um dto para que seja posteriormente populado.
	 * @param contrato
	 * @param debitos
	 * @return
	 */
	public static AcordoDTO newDto(Contrato contrato, List<Debito> debitos) {
		AcordoDTO acordo = new AcordoDTO();
		acordo.setContrato(contrato);
		acordo.setDebitosDoAcordo(debitos);
		return acordo;
	}
	
	/**
	 * Cria um Acordo a partir de um dto devidamente populado
	 * @param acordoDTO
	 * @return
	 */
	public static Acordo objectFromDto(AcordoDTO acordoDTO, ConfiguracaoAcordo config) {
		
		acordoDTO.getDebitosDoAcordo().forEach(d -> d.setSituacao(SituacaoDebitoEnum.NEGOCIADA));
		
		Acordo acordo = new Acordo();
		acordo.setConfiguracao(config);
		acordo.setContrato(acordoDTO.getContrato());
		acordo.setDebitos( acordoDTO.getDebitosDoAcordo()
				.stream()
				.map(d->new AcordoDebito(acordo, d, OrigemDebitoAcordoEnum.CONTRATO))
				.collect(Collectors.toSet())
				);
		acordoDTO.gerarParcelasDoAcordo(config);
		acordo.getDebitos().addAll( acordoDTO.getParcelasDoAcordo()
				.stream()
				.map(d->new AcordoDebito(acordo, d, OrigemDebitoAcordoEnum.ACORDO))
				.collect(Collectors.toSet())
				);
		acordo.setStatusAcordo(StatusAcordoEnum.ATIVO);
		acordo.setValorAcordado(acordoDTO.getTotalAcordo());
		return acordo;
	}

}
