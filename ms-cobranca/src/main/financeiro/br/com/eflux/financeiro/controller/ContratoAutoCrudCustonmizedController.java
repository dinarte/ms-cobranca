package br.com.eflux.financeiro.controller;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.dfframeworck.autocrud.AutoCrudAction;
import br.com.dfframeworck.autocrud.annotations.EnableAutoCrudField;
import br.com.dfframeworck.controller.AutoCrudControllerCustomizer;
import br.com.dfframeworck.controller.Customizer;
import br.com.eflux.empreendimento.domain.Empreendimento;
import br.com.eflux.financeiro.domain.Contrato;

@Component
@Customizer(Contrato.class)
public class ContratoAutoCrudCustonmizedController extends AutoCrudControllerCustomizer {

	@EnableAutoCrudField(label="Empreendimento", enableForFilter=true, enableForCreate=false, ordinal=0,
			enableForUpdate=false, enableForList=false, lookUpFieldName="unidade.zona.empreendimento.nome", path="unidade.zona.empreendimento")
	private Empreendimento empreendimento;
	
	@Override
	public void addActions(List<AutoCrudAction> actions) {
		actions.add(new AutoCrudAction("Débitos do Contrato", "/crud/Debito?Debito.contrato={id}&size=200", "fa fa-money","btn btn-info btn-circle", true, 1));
		actions.add(new AutoCrudAction("Iniciar um Acordo", "/acordo/{id}/iniciar", "fas fa-handshake","btn btn-warning btn-circle", true, 2));
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}
	
	
	
}
