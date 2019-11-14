package br.com.eflux.empreendimento.controller;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.dfframeworck.autocrud.AutoCrudAction;
import br.com.dfframeworck.controller.AutoCrudControllerCustomizer;
import br.com.dfframeworck.controller.Customizer;
import br.com.eflux.empreendimento.domain.Empreendimento;

@Component
@Customizer(Empreendimento.class)
public class EmpreendimentoAutoCrudCustonmizedController extends AutoCrudControllerCustomizer {

	@Override
	public void addActions(List<AutoCrudAction> actions) {
		actions.add(new AutoCrudAction("Gerenciar Unidades do Empreendimento", "/empreendimento/{id}/unidades", "fa fa-th","btn btn-info btn-circle", true, 1));
	}
	
}
