package br.com.eflux.empreendimento.controller;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.IllegalAddException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.dfframeworck.autocrud.AutoCrudEntity;
import br.com.dfframeworck.controller.AutoCrudControllerCustomizer;
import br.com.dfframeworck.controller.Customizer;
import br.com.dfframeworck.exception.ErroException;
import br.com.dfframeworck.exception.ValidacaoException;
import br.com.dfframeworck.messages.AppFlashMessages;
import br.com.dfframeworck.security.Functionality;
import br.com.dfframeworck.util.SerializationException;
import br.com.eflux.empreendimento.domain.Empreendimento;
import br.com.eflux.empreendimento.domain.Zona;
import br.com.eflux.empreendimento.repository.EmpreendimentoRepository;
import br.com.eflux.empreendimento.repository.ZonaRepository;

@Controller
@Customizer(Zona.class)
public class ZonaAutoCrudCustonmizedController extends AutoCrudControllerCustomizer {

	/**
	 * Empreendimento no qual uam zona será criada
	 */
	private Empreendimento empreendimento;
	
	@Autowired
	private EmpreendimentoRepository EmpreendimentoRepository;
	
	@Autowired
	ZonaRepository zonaRepository;
	
	@Autowired
	UnidadesController unidadesController;
	
	@Autowired
	AppFlashMessages flahsMsgs;
	
	/**
	 * Este método é chamado no lugar do método add padrão do autoCrud 
	 * para que possamos setar o empreendimento no qual será adicionado uma nova zona!
	 * @param idEmpreendimento
	 * @param model
	 * @return
	 * @throws ErroException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@Functionality(name="Adicionar Zona ao Empreendimento")
	@RequestMapping("/empreendimento/{idEmpreendimento}/zona/add")
	public String criarZonaNoEmpreendimento(@PathVariable Long idEmpreendimento, Model model) 
			throws ErroException, InstantiationException, IllegalAccessException {

		empreendimento = EmpreendimentoRepository.findById(idEmpreendimento)
				.orElseThrow(() -> new IllegalAddException("Não conseguimos encontrar o empreendimento com o id informado"));
		
		return getController().add("Zona", model);
	}
	
	
	
	/**
	 * Este método é chamado no lugar do método add padrão do autoCrud 
	 * para que possamos setar o empreendimento no qual será adicionado uma nova zona!
	 * @param idEmpreendimento
	 * @param model
	 * @return
	 * @throws ErroException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@Functionality(name="Editar Zona do Empreendimento")
	@RequestMapping("/empreendimento/zona/{idZona}")
	public String editarZonaNoEmpreendimento(@PathVariable Long idZona, Model model) 
			throws ErroException, InstantiationException, IllegalAccessException {
		setEmpreendimento(idZona);
		return getController().select("Zona", idZona, model);
	}
	
	
	
	/**
	 * Este método é chamado no lugar do método add padrão do autoCrud 
	 * para que possamos setar o empreendimento no qual será adicionado uma nova zona!
	 * @param idEmpreendimento
	 * @param model
	 * @return
	 * @throws ErroException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SerializationException 
	 * @throws ValidacaoException 
	 */
	@Functionality(name="Remover Zona do Empreendimento")
	@RequestMapping("/empreendimento/zona/{idZona}/del")
	public String removerZonaNoEmpreendimento(@PathVariable Long idZona, Model model, HttpServletRequest request) 
			throws ErroException, InstantiationException, IllegalAccessException, ValidacaoException, SerializationException {
		setEmpreendimento(idZona);
		return getController().del("Zona", idZona, model, request);
	}



	private void setEmpreendimento(Long idZona) {
		Zona zona = zonaRepository.findById(idZona).orElseThrow(()->new IllegalArgumentException("Não encontramos a zona com o id selecionado"));
		empreendimento = zona.getEmpreendimento();
	}
	
	
	@Override
	public void onCreateAddForm(AutoCrudEntity crudEntity, Model model) {
		prepareData(crudEntity, model);
	}

	
	@Override
	public void onSelect(AutoCrudEntity crudEntity, Model model) {
		prepareData(crudEntity, model);
	}
	
	private void prepareData(AutoCrudEntity crudEntity, Model model) {
		crudEntity.getField("empreendimento").setValue(empreendimento);
		crudEntity.getField("empreendimento").setReadOnly(true);
		//disponibiliza a lista de zonas so empreendimento
		model.addAttribute("zonaList", zonaRepository.findAllByEmpreendimentoOrderByNivelAscNomeAsc(empreendimento) );
	}

	/**
	 * altera a página inicial do auto crud
	 */
	@Override
	public String getIndexView() {
		flahsMsgs.getMessages().getSuccessList().add("Zona criada com sucesso");
		return "redirect:/empreendimento/"+empreendimento.getId()+"/unidades";
	}
	
	
	
	
}
