package br.com.eflux.financeiro.controller;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.dfframeworck.autocrud.AutoCrudAction;
import br.com.dfframeworck.autocrud.Periodo;
import br.com.dfframeworck.autocrud.annotations.EnableAutoCrudField;
import br.com.dfframeworck.controller.AutoCrudControllerCustomizer;
import br.com.dfframeworck.controller.Customizer;
import br.com.eflux.comum.domain.Pessoa;
import br.com.eflux.empreendimento.domain.Empreendimento;
import br.com.eflux.financeiro.domain.Debito;

@Component
@Customizer(Debito.class)
public class DebitoAutoCrudCustonmizedController extends AutoCrudControllerCustomizer {


	@EnableAutoCrudField(label="Empreendimento", enableForFilter=true, enableForCreate=false, ordinal=0,
			enableForUpdate=false, enableForList=false, lookUpFieldName="contrato.unidade.zona.empreendimento.nome", path="contrato.unidade.zona.empreendimento")
	public Empreendimento empreendimento;
	
	@EnableAutoCrudField(label="Contratante", enableForFilter=true, enableForCreate=false, ordinal=0,
			enableForUpdate=false, enableForList=false, lookUpFieldName="nome", path="contrato.pessoa", ui="autocomplete")
	public Pessoa pessoa;

	@EnableAutoCrudField(label="Data de Vencimento", enableForFilter=true, enableForCreate=false, ordinal=0, enableForUpdate=false, enableForList=false, path="dataVencimento")
	public Periodo periodoVencimento = new Periodo();

	
	@EnableAutoCrudField(label="Data Pagamento", enableForFilter=true, enableForCreate=false, ordinal=0, enableForUpdate=false, enableForList=false, path="dataUltimoPagamento")
	public Periodo periodoPgamento = new Periodo();

	
	
	@Override
	public void addActions(List<AutoCrudAction> actions) {
		actions.add(new AutoCrudAction("Invoice", "/invoice/{id}/visualizar", "fa fa-file-pdf-o ","btn btn-info btn-circle", true, 1));
	}
	
}
