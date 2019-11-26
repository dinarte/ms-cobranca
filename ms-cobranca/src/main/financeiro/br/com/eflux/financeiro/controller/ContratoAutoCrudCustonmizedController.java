package br.com.eflux.financeiro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.dfframeworck.autocrud.AutoCrudAction;
import br.com.dfframeworck.autocrud.annotations.EnableAutoCrudField;
import br.com.dfframeworck.controller.AutoCrudControllerCustomizer;
import br.com.dfframeworck.controller.Customizer;
import br.com.dfframeworck.security.Functionality;
import br.com.eflux.empreendimento.domain.Empreendimento;
import br.com.eflux.financeiro.domain.Acordo;
import br.com.eflux.financeiro.domain.Contrato;
import br.com.eflux.financeiro.domain.Debito;
import br.com.eflux.financeiro.dto.ContratoMapper;
import br.com.eflux.financeiro.helper.SituacaoDebitoHelper;
import br.com.eflux.financeiro.repository.AcordoRepository;
import br.com.eflux.financeiro.repository.ContratoRepository;
import br.com.eflux.financeiro.repository.DebitoRepository;

@Controller
@Customizer(Contrato.class)
public class ContratoAutoCrudCustonmizedController extends AutoCrudControllerCustomizer {

	@Autowired private ContratoRepository contratoRepo;
	@Autowired private DebitoRepository debitoRepo;
	@Autowired private AcordoRepository acordoRepo;
	
	
	/**
	 * Adiciona o filtro adicional Empreendimento na tela de listagem de contratos 
	 */
	@EnableAutoCrudField(label="Empreendimento", enableForFilter=true, enableForCreate=false, ordinal=0,
			enableForUpdate=false, enableForList=false, lookUpFieldName="unidade.zona.empreendimento.nome", path="unidade.zona.empreendimento")
	private Empreendimento empreendimento;
	
	
	/**
	 * Adiciona ações a listagem de contratos
	 */
	@Override
	public void addActions(List<AutoCrudAction> actions) {
		
		actions.add(new AutoCrudAction("Visualizar Contrato", "/contrato/{id}", "fa fa-file-contract","btn btn-default btn-circle", true, 0));
		actions.add(new AutoCrudAction("Débitos do Contrato", "/crud/Debito?Debito.contrato={id}&size=200", "fa fa-money","btn btn-info btn-circle", true, 1));
		actions.add(new AutoCrudAction("Iniciar um Acordo", "/acordo/{id}/iniciar", "fas fa-handshake","btn btn-warning btn-circle", true, 2));
	}
	
	
	/**
	 * Visualiza um contrato
	 * @param idContrato
	 * @param m
	 * @return
	 */
	@Functionality(isPublic=false, name="Vizualizar", menu="none")
	@RequestMapping("/contrato/{idContrato}")
	public String vizualizar(@PathVariable("idContrato") Long idContrato, Model m) {
		
		Contrato contrato =  contratoRepo.findById(idContrato)
				.orElseThrow(()-> new IllegalArgumentException("Não foi encontrado um contrato para o id informado."));
		
		List<Debito> debitoList = debitoRepo.findAllByContratoAndSituacao(contrato,SituacaoDebitoHelper.getAll()); 
		List<Acordo> acordoList = acordoRepo.findAllByContrato(contrato); 
		
		m.addAttribute("contratoDTO", ContratoMapper.dtoFromObjects(contrato, debitoList, acordoList));
		
		return "/contrato/visualizar";
	}
	

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}
	
	
	
}
