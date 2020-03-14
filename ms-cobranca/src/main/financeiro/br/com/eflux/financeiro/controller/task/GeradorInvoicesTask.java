package br.com.eflux.financeiro.controller.task;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import br.com.dfframeworck.controller.AutoCrudController;
import br.com.dfframeworck.messages.AppFlashMessages;
import br.com.eflux.config.financeiro.domain.ConfiguracaoBoletoConta;
import br.com.eflux.config.financeiro.repository.ConfiguracaoBoletoContaRepository;
import br.com.eflux.financeiro.domain.Boleto;
import br.com.eflux.financeiro.domain.Debito;
import br.com.eflux.financeiro.domain.StatusGeracaoInvoiceEnum;
import br.com.eflux.financeiro.repository.BoletoRepository;
import br.com.eflux.financeiro.repository.DebitoRepository;
import br.com.eflux.payments.api.Invoice;
import br.com.eflux.payments.api.PaymentApiConsumer;
import br.com.eflux.payments.api.PaymentApiException;

@Component
public class GeradorInvoicesTask {
	
	private static final Logger log = LoggerFactory.getLogger(GeradorInvoicesTask.class);

	@Autowired
	ConfiguracaoBoletoContaRepository configBoletoContaRepository;
	
	@Autowired
	DebitoRepository debitoRepository;
	
	@Autowired
	ApplicationContext context;
	
	@Autowired
	BoletoRepository boletoReopository;
	
	@Autowired 
	AppFlashMessages appMesgs;
	
	@Autowired
	AutoCrudController crudCrontroller;
	
	

	/**
	 * Task que roda periodicamente para realizar a geração dos invoices dos débitos.
	 */
	//@Scheduled(fixedDelay = 50000)
	public void executarGeracao() {
			Date dataGeracao = new Date();
			
			log.info("Geração de invoices iniciada em "+ dataGeracao.toString());
			log.info("Buscando por contas configuradas... "+ dataGeracao.toString());
			
			List<ConfiguracaoBoletoConta> configAccountList = (List<ConfiguracaoBoletoConta>) configBoletoContaRepository.findAll(); 
			
			log.info(configAccountList.size() + " contas configuradas encontradas");
			
			configAccountList.forEach( config ->{
				
				List<Debito> debitos = debitoRepository.findByContaRecebimento(config.getContaRecebimento());
				
				log.info("Carregando "+debitos.size()+" debidos da conta:" + config.getContaRecebimento().getDescricao());
				
				debitos
				.stream()
				.filter(debito -> !debito.getStatusCriacaoInvoice().equals(StatusGeracaoInvoiceEnum.SUCESSO))
				.filter(debito -> debito.getDataVencimento().after(new Date()))
				.filter(debito -> !debito.getContrato().isBloquearCobranca())
				.forEach(debito -> {
					
					log.info(debito.getDataVencimento() +" --> "+ debito.getDataVencimento().after(new Date()));
					
					debito.setDataCriacaoInvoice(dataGeracao);
					PaymentApiConsumer consumer =  (PaymentApiConsumer) context.getBean(config.getBoletoApiConfiguration().getApiImplementation());
					
					log.info(" Conssumer carregado: " + config.getBoletoApiConfiguration().getApiImplementation());
					
					try {
						consumer.basicAuthentication(config);
						Invoice invoice = consumer.createFromDebito(debito);
						boletoReopository.save((Boleto)invoice);
						debito.setInvoice((Boleto) invoice);
						debito.setStatusCriacaoInvoice(StatusGeracaoInvoiceEnum.SUCESSO);
						debitoRepository.save(debito);
					} catch (PaymentApiException e) {
						debito.setStatusCriacaoInvoice(StatusGeracaoInvoiceEnum.ERRO);
						debito.setErroCriacaoInvoice(e.getMessage());
						debitoRepository.save(debito);
					}
					
				});
				
			});
			
			log.info("Geração de invoices finalizada!");
		}


}
