package br.com.eflux.financeiro.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.dfframeworck.controller.AutoCrudController;
import br.com.dfframeworck.exception.ErroException;
import br.com.dfframeworck.exception.ValidacaoException;
import br.com.dfframeworck.messages.AppFlashMessages;
import br.com.dfframeworck.messages.AppMessages;
import br.com.dfframeworck.security.Functionality;
import br.com.dfframeworck.util.NavigationUtils;
import br.com.eflux.config.financeiro.domain.ConfiguracaoBoletoConta;
import br.com.eflux.financeiro.domain.Boleto;
import br.com.eflux.financeiro.domain.Debito;
import br.com.eflux.financeiro.domain.SituacaoDebitoEnum;
import br.com.eflux.financeiro.domain.StatusGeracaoInvoiceEnum;
import br.com.eflux.financeiro.repository.BoletoRepository;
import br.com.eflux.financeiro.repository.ConfiguracaoInvoiceContaRepository;
import br.com.eflux.financeiro.repository.DebitoRepository;
import br.com.eflux.payments.api.Invoice;
import br.com.eflux.payments.api.PaymentApiConfigurationAccount;
import br.com.eflux.payments.api.PaymentApiConsumer;
import br.com.eflux.payments.api.PaymentApiException;

/**
 * Controller responsável por prover operações individuais com invoice de um determinado débito.
 * @author dinarte
 *
 */
@Controller
public class GerenciadorInvoiceController {

	@Autowired DebitoRepository debitoRepo;
	
	@Autowired ConfiguracaoInvoiceContaRepository configContaRepo;
	
	@Autowired BoletoRepository boletoRepo;

	@Autowired ApplicationContext context;
	
	@Autowired AutoCrudController crudController;
	
	@Autowired AppMessages appMessages;

	@Autowired AppFlashMessages appFlashMessages;
	
	/**
	 * Visuzaliza o invoice gerado para o debito informado.
	 * Caso o invoice não esteja ainda criado no sistema, será exibido as opções de solicitar a criação do invoice na plataforma de cobranças e de 
	 * Associar um invoice já criado previamente diretamente na plataforma de cobranças.
	 * @param idDebito
	 * @param msgs
	 * @param m
	 * @return
	 * @throws ErroException
	 */
	
	@Functionality(name="Visualizar um Invoice para um dado débito",isPublic=false,menu="none")
	@RequestMapping(path="/invoice/{idDebito}/visualizar", method=RequestMethod.GET)
	public String visualizar(@PathVariable("idDebito") Long idDebito, Model m, HttpServletRequest request) throws ErroException {
		
		Debito d = debitoRepo.findById(idDebito).get(); 
		
		if (d.getSituacao().equals(SituacaoDebitoEnum.QUITADA))
			throw new ErroException("Este débito já se encontra QUITADO");

		
		if (d.getInvoice() == null) {
			m.addAttribute("debito",d);
			m.addAttribute("referer", NavigationUtils.getReferer(request, "/crud/Debito"));
			return "/invoice/visualizar-no-invoice";
		}
		
		//if (!d.getInvoice().getStatus().equals(Boleto.STATUS_REGISTRADO))
			//throw new ErroException("Apenas é possível gerar invoices com a situação REGISTRADO e a situaçao atual deste invoice é :" +d.getInvoice().getStatus());
		
		
		PaymentApiConfigurationAccount configConta = d.getInvoice().getConfigConta();
		PaymentApiConsumer consumer =  (PaymentApiConsumer) context.getBean(configConta.getBoletoApiConfiguration().getApiImplementation());
		consumer.basicAuthentication(configConta);
		
		return "redirect:" +  consumer.getPath(d.getInvoice().getTokenId());
	}
	
	
	
	
	/**
	 * É raro, mas pode ser que você tenha invoices que foram criados diretamente na plataforma de cobranças e queira associá-la aos débitos referentes no sistema.  
     * Você poderá usar  o formulário abaixo para fazer isso, mas lembre-se, esta operação é suceptível a erro humano e é muito aconcelhável 
     * que você saiba exatamente o que está fazendo.
	 * @param idDebito
	 * @param m
	 * @param request
	 * @return
	 * @throws ErroException
	 */
	@Functionality(name="Criar um invoice na plataforma de cobranças",isPublic=false,menu="none")
	@RequestMapping(path="/invoice/{idDebito}/criar", method=RequestMethod.GET)
	public String criar(@PathVariable("idDebito") Long idDebito, Model m, HttpServletRequest request) throws ErroException {
		
		Debito debito = debitoRepo.findById(idDebito).get();
		
		m.addAttribute(debito);
		
		ConfiguracaoBoletoConta configConta = configContaRepo.findTop1ByContaRecebimento(debito.getContaRecebimento());
		PaymentApiConsumer consumer =  (PaymentApiConsumer) context.getBean(configConta.getBoletoApiConfiguration().getApiImplementation());
		consumer.basicAuthentication(configConta);
		
		Invoice invoice;
		try {
			invoice =   consumer.createFromDebito(debito);
		}catch (PaymentApiException e) {
			appMessages.getErrorList().add(e.getMessage());
			return "/invoice/visualizar-no-invoice";
		}
		
		invoice = boletoRepo.save((Boleto)invoice);
		debito.setStatusCriacaoInvoice(StatusGeracaoInvoiceEnum.SUCESSO);
		debito.setDataCriacaoInvoice(new Date());
		debito.setInvoice((Boleto)invoice);
		debitoRepo.save(debito);
		appMessages.getSuccessList().add("Invoice associado com sucesso.");
		
		
		return "redirect:" +  consumer.getPath(debito.getInvoice().getTokenId());
	
	}
	
	
	/**
	 * Esta funcionalidade deverá ser usada apenas quando um Invoice foi criado diretamente na platarforma utilizada para trabalhar com combranças, 
	 * Estas plataformas geralmente são utilizadas para geração de boletos, faturas, cobranças via cartão de crédito etc... 
	 * Perceba que não é recomendado criar os invoices diretmente na plataforma, já que existe integração para que o Sistema gerenci automáticamente
	 * este processo.
	 * Associa um invoice já existente a um débito em questão.
	 * @param idDebito
	 * @param msgs
	 * @param m
	 * @return
	 * @throws ErroException
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ValidacaoException 
	 * @throws PaymentApiException 
	 */
	@Functionality(name="Associar Invoice a um Deito",isPublic=false,menu="none")
	@RequestMapping(path="/invoice/associar", method=RequestMethod.POST)
	public String associar(Debito debito, @RequestParam String referer, AppMessages msgs, Model m, HttpServletRequest request) throws InstantiationException, IllegalAccessException, ValidacaoException, ErroException {
		
		
		m.addAttribute("debito", debito);
		
		validarCamposDoDebitoParaAssociacao(debito);
		
		if (!appMessages.getErrorList().isEmpty())
			return "/invoice/visualizar-no-invoice";
		
		if (debitoRepo.existsByInvoice(debito.getInvoice())) {
			Debito debitoInvoice = debitoRepo.findTop1ByInvoice(debito.getInvoice());
			appMessages.getErrorList().add("Já existe um débito associado a este invoice: <a href='/crud/Debito?Debito.numero="+debitoInvoice.getNumero()+"&Debito.contrato="+debitoInvoice.getContrato().getId()+"'>Clique para saber qual é</a>");
			return "/invoice/visualizar-no-invoice";
		}
		
		Debito debitoPersistente = debitoRepo.findById(debito.getId()).get();
		
		ConfiguracaoBoletoConta configConta = configContaRepo.findTop1ByContaRecebimento(debitoPersistente.getContaRecebimento());
		
		if (configConta == null) {
			appMessages.getErrorList().add("A conta relacionada ao débito ("+debitoPersistente.getContaRecebimento().getDescricao()+") não está configurada para gerar invoices.");
			return "/invoice/visualizar-no-invoice";
		}
		
		
		PaymentApiConsumer consumer =  (PaymentApiConsumer) context.getBean(configConta.getBoletoApiConfiguration().getApiImplementation());
		consumer.basicAuthentication(configConta);
		
		try {
			debito.getInvoice().setPdfFile( consumer.get( debito.getInvoice().getTokenId()) );
		}catch (PaymentApiException e) {
			appMessages.getErrorList().add(e.getMessage());
			return "/invoice/visualizar-no-invoice";
		}
		
		boletoRepo.save(debito.getInvoice());
		debitoPersistente.setInvoice(debito.getInvoice());
		debitoPersistente.setStatusCriacaoInvoice(StatusGeracaoInvoiceEnum.SUCESSO);
		debitoRepo.save(debitoPersistente);
		appFlashMessages.getMessages().getSuccessList().add("Invoice associado com sucesso.");
		
		return "redirect:"+referer;
	}


	private void validarCamposDoDebitoParaAssociacao(Debito debito) {
		if (Strings.isBlank(debito.getInvoice().getTokenId()))
			appMessages.getErrorList().add("Informe o tokenId");
		if (Strings.isBlank(debito.getInvoice().getNossoNumero()))
			appMessages.getErrorList().add("Informe o nosso número");
		if (Strings.isBlank(debito.getInvoice().getLocation()))	
			appMessages.getErrorList().add("Informe a Localização");
	}
	
	
	
}
