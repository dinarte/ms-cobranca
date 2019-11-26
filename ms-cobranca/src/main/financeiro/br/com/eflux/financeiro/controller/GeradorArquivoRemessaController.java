package br.com.eflux.financeiro.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.dfframeworck.messages.AppMessages;
import br.com.dfframeworck.security.Functionality;
import br.com.eflux.config.financeiro.domain.ConfiguracaoBoletoConta;
import br.com.eflux.financeiro.domain.RemessaBancaria;
import br.com.eflux.financeiro.repository.ConfiguracaoInvoiceContaRepository;
import br.com.eflux.financeiro.repository.RemessaBancariaRepository;
import br.com.eflux.payments.api.BatchFile;
import br.com.eflux.payments.api.PaymentApiConsumer;
import br.com.eflux.payments.api.PaymentApiException;

/**
 * Controller responsável por prover operações individuais com invoice de um determinado débito.
 * @author dinarte
 *
 */
@Controller
public class GeradorArquivoRemessaController {

	
	@Autowired ConfiguracaoInvoiceContaRepository configBoletoContaRepository;
	

	@Autowired ApplicationContext context;
	
	@Autowired AppMessages msgs;
	
	@Autowired RemessaBancariaRepository remessaBancariaRepository;
	
	/**
	 * gera aqrquivos de remessa para cada conta
	 * @return
	 */
	@Functionality(isPublic=false, name="Gerar Remessas", menu="root->Financeiro->Remessas Bancárias->Gerar Remessas", icon="far fa-copy")
	@RequestMapping("/invoice/remessa/gerar")
	public String gerarArquivoRemessa() {
		
		List<String> erros = new ArrayList<String>();
		
		List<ConfiguracaoBoletoConta> configAccountList = (List<ConfiguracaoBoletoConta>) configBoletoContaRepository.findAll();
		
		configAccountList.forEach( config ->{
			
			
				PaymentApiConsumer consumer =  (PaymentApiConsumer) context.getBean(config.getBoletoApiConfiguration().getApiImplementation());
				consumer.basicAuthentication(config);
				
				RemessaBancaria remessa = new RemessaBancaria();
				try {
					remessa = (RemessaBancaria) consumer.createBatch(config.getToken(), (BatchFile) remessa);
					remessa.setContaRecebimento(config.getContaRecebimento());
					remessaBancariaRepository.save((RemessaBancaria) remessa);
				} catch (PaymentApiException e) {
					erros.add(e.getMessage());
				}
				
				msgs.getErrorList().addAll(erros);
			
		});
		
		return "redirect:/crud/RemessaBancaria";
		
	}
	
}
