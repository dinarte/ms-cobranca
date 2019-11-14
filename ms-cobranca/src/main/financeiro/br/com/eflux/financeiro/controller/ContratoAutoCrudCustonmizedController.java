package br.com.eflux.financeiro.controller;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.dfframeworck.autocrud.AutoCrudAction;
import br.com.dfframeworck.controller.AutoCrudControllerCustomizer;
import br.com.dfframeworck.controller.Customizer;
import br.com.eflux.financeiro.domain.Contrato;

@Component
@Customizer(Contrato.class)
public class ContratoAutoCrudCustonmizedController extends AutoCrudControllerCustomizer {


	@Override
	public void addActions(List<AutoCrudAction> actions) {
		actions.add(new AutoCrudAction("Débitos do Contrato", "/crud/Debito?Debito.contrato={id}&size=200", "fa fa-money","btn btn-info btn-circle", true, 1));
	}
	
	@Override
	public void addfilters(List<String> filters) {
		
		if (filters.size() == 0)
			filters.add("situacaoContrato.contratoAtivo = true ");
	}
	
}
