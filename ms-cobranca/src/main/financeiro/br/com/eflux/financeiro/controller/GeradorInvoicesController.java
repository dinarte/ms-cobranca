package br.com.eflux.financeiro.controller;

import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.dfframeworck.controller.AutoCrudController;
import br.com.dfframeworck.exception.ErroException;
import br.com.dfframeworck.messages.AppFlashMessages;
import br.com.dfframeworck.messages.AppMessages;
import br.com.dfframeworck.messages.SucessMsg;
import br.com.dfframeworck.security.Functionality;
import br.com.eflux.config.financeiro.domain.ConfiguracaoBoletoConta;
import br.com.eflux.config.financeiro.repository.ConfiguracaoBoletoContaRepository;
import br.com.eflux.financeiro.domain.Boleto;
import br.com.eflux.financeiro.domain.Debito;
import br.com.eflux.financeiro.repository.BoletoRepository;
import br.com.eflux.financeiro.repository.DebitoRepository;
import br.com.eflux.payments.api.PaymentApiConsumer;
import br.com.eflux.payments.api.PaymentApiException;
import br.com.eflux.payments.api.Invoice;

@Controller
public class GeradorInvoicesController {
	
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
	
	@Functionality(isPublic=false, name="Gerar", menu="root->Financeiro->Invoices->Gerar", icon="fa fa-refresh")
	@RequestMapping("/financeiro/boletos/gerar")
	@SucessMsg(message="Geração de Invoices executada, consulte a listagem de invoices com erros")
	public String gerar() {
		
		Date dataGeracao = new Date();
		
		List<ConfiguracaoBoletoConta> configAccountList = (List<ConfiguracaoBoletoConta>) configBoletoContaRepository.findAll(); 
		configAccountList.forEach( config ->{
			
			List<Debito> debitos = debitoRepository.findByContaRecebimento(config.getContaBancaria());
			
			debitos
			.stream()
			.filter(debito -> !debito.getStatusCriacaoInvoice().equals(Debito.INVOICE_CRIADO_SUCESSO))
			.forEach(debito -> {
				debito.setDataCriacaoInvoice(dataGeracao);
				PaymentApiConsumer consumer =  (PaymentApiConsumer) context.getBean(config.getBoletoApiConfiguration().getApiImplementation());
				try {
					consumer.basicAuthentication(config);
					Invoice invoice = consumer.createFromDebito(debito);
					boletoReopository.save((Boleto)invoice);
					debito.setInvoice((Boleto) invoice);
					debito.setStatusCriacaoInvoice(Debito.INVOICE_CRIADO_SUCESSO);
					debitoRepository.save(debito);
				} catch (PaymentApiException e) {
					debito.setStatusCriacaoInvoice(e.getMessage());
					debitoRepository.save(debito);
				}
				
				
			});
			
			
		});
		
		return "redirect:/financeiro/invoices/resultado";
		
	}
	
	@Functionality(isPublic=false, name="Resultado", menu="root->Financeiro->Invoices->Resultado Geracao", icon="fa fa-check")
	@RequestMapping("/financeiro/invoices/resultado")
	public String resultadoGeracao(Model model) {
		
		List<Debito> debitosNaoGerados = debitoRepository.findAllByDataCriacaoInvoiceIsNotNullAndInvoiceIsNull();
		
		if (debitosNaoGerados.size() > 0)
			appMesgs.getMessages().getWarningList().add("ATENÇÃO: A geração de Invoices não conseguiu gerar "+debitosNaoGerados.size()+" invoice(s)");
		else
			appMesgs.getMessages().getSuccessList().add("A geração de Invoices foi finaizada sem ocorrências");
		
		model.addAttribute("debitosNaoGerados", debitosNaoGerados);
		
		debitosNaoGerados.forEach(d->System.out.println(d.getStatusCriacaoInvoiceMap()));
		
		return "/financeiro/invoices/resultadoGeracao";
	}
	
	
	@RequestMapping("/financeiro/invoices/quikfix/{codigo}/{idDebito}")
	public String quikFix(@PathVariable("codigo") String codigo, @PathVariable("idDebito") Long idDebito, Model model) throws ErroException, InstantiationException, IllegalAccessException {
		
		Debito debito = debitoRepository.findById(idDebito).orElseThrow(()->new ErroException("O Débito não foi encontrado."));
		
		List<String> codigosErroCadastroPessoal = new ArrayList<String>();
		codigosErroCadastroPessoal.add("252E87B1");
		codigosErroCadastroPessoal.add("5412D14F");
		codigosErroCadastroPessoal.add("5CE37CD3");
		codigosErroCadastroPessoal.add("5F13D9B2");
		codigosErroCadastroPessoal.add("97D2874C");
		
		if (codigosErroCadastroPessoal.contains(codigo)) {
			addMensagens(debito, codigosErroCadastroPessoal);
			return crudCrontroller.select("Pessoa", debito.getContrato().getPessoa().getId(), model);
		}
		
		appMesgs.getMessages().getWarningList().add("É uma pena, mas não temos sugestões de resolução rápida para o código informado");
		return resultadoGeracao(model);
	}

	
	private void addMensagens(Debito debito, List<String> codigosErroCadastroPessoal) {
		appMesgs.getMessages().getInfoList().add("<h3> Boas Notícias !</h3>Nós encontramos uma maneira de solucionar rápidamente o problema que está fazendo com que o Invoice em questão não seja gerado, "
				+ "de acordo com o código do erro, identificamos que trata-se de dados cadastrais faltantes para a pessoa que é a pagadora do invoice ou boleto. "
				+ "<br /> <strong>Ja localizamos</strong> o cadastro pessoal de <strong>"+debito.getContrato().getPessoa()+"</strong> para que você informe os dados faltantes.");
		
		String warning = "<h4>Da só uma olhada no que você vai resolver aqui...</h4> ";
		warning = warning.concat("<ul>");
		Map<String, Object> erro = (Map<String, Object>) debito.getStatusCriacaoInvoiceMap().get("erro");
		List<Map<String,Object>> causas = (List<Map<String, Object>>) erro.get("causas");
		for (Map<String, Object> causa : causas) {
			if (codigosErroCadastroPessoal.contains(causa.get("codigo")))
				warning = warning.concat("<li>").concat(causa.get("mensagem").toString().concat("<br />"));
		}
		
		warning = warning.concat("</ul> <strong> LEMBRE-SE: </strong> Após salvar as alterações, execute novamente a geração dos invoices e consulte o resultado para e certificar que a correção deu certo!");
		appMesgs.getMessages().getWarningList().add(warning);
	}

}
