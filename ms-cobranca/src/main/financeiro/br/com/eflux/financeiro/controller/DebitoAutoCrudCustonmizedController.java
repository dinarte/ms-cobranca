package br.com.eflux.financeiro.controller;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.dfframeworck.autocrud.AutoCrudAction;
import br.com.dfframeworck.controller.AutoCrudControllerCustomizer;
import br.com.dfframeworck.controller.Customizer;
import br.com.eflux.financeiro.domain.Debito;

@Component
@Customizer(Debito.class)
public class DebitoAutoCrudCustonmizedController extends AutoCrudControllerCustomizer {


	@Override
	public void addActions(List<AutoCrudAction> actions) {
		actions.add(new AutoCrudAction("Invoice", "/invoice/{id}/visualizar", "fa fa-file-pdf-o ","btn btn-info btn-circle", true, 1));
	}
	
}
