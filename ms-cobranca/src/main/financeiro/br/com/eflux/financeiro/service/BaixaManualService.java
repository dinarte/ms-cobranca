package br.com.eflux.financeiro.service;

import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.eflux.financeiro.domain.Debito;
import br.com.eflux.financeiro.domain.SituacaoDebitoEnum;
import br.com.eflux.financeiro.dto.BaixaDebitoDTO;
import br.com.eflux.financeiro.repository.DebitoRepository;
import br.com.eflux.payments.api.PaymentApiConfigurationAccount;
import br.com.eflux.payments.api.PaymentApiConsumer;
import br.com.eflux.payments.api.PaymentApiException;

@Service
public class BaixaManualService {

	@Autowired
	DebitoRepository debitoRepository;
	
	@Autowired
	ApplicationContext context;
	
	@Transactional
	public void baixar(BaixaDebitoDTO baixa) throws PaymentApiException {
		Debito debito = carregarDebito(baixa);
		atualizarJurosMultaComBaseNaDataDePagamento(baixa, debito);		
		baixarInvoiceCasoExista(baixa, debito);
		atualizarDadosDaBaixa(baixa);
		
	}

	private void baixarInvoiceCasoExista(BaixaDebitoDTO baixa, Debito debito) throws PaymentApiException {
		if (Objects.nonNull( debito.getInvoice() )) {
		
			PaymentApiConfigurationAccount configConta = debito.getInvoice().getConfigConta();
			PaymentApiConsumer consumer =  (PaymentApiConsumer) context.getBean(configConta.getBoletoApiConfiguration().getApiImplementation());
			consumer.basicAuthentication(configConta);
			consumer.writeOff(debito.getInvoice().getTokenId(), "E-Flux -> Baixa Manual por "+baixa.getUsuario().getName() 
					+ "em " + new Date() + ". Motivo:" + baixa.getMotivoBaixaManual());
		}
	}

	private Debito carregarDebito(BaixaDebitoDTO baixa) {
		Debito debito = debitoRepository.findById(baixa.getIdDebito()).get();
		return debito;
	}

	private void atualizarDadosDaBaixa(BaixaDebitoDTO baixa) {
		debitoRepository.updateBaixaManual(
				baixa.getDataUltimoPagamento(), 
				baixa.getValorPago(), 
				SituacaoDebitoEnum.QUITADA,
				baixa.getUsuario(),
				baixa.getMotivoBaixaManual(),
				baixa.getIdDebito());
	}

	private void atualizarJurosMultaComBaseNaDataDePagamento(BaixaDebitoDTO baixa, Debito debito) {
		debito.setDataUltimoPagamento(baixa.getDataUltimoPagamento());
		debito.getJurosAtrazo();
		debito.getMultaAtrazo();
		debitoRepository.save(debito);
	}
	
}
