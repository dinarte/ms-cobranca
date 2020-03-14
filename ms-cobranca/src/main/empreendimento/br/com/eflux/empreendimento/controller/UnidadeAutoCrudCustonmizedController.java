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
import br.com.eflux.empreendimento.domain.Unidade;
import br.com.eflux.empreendimento.domain.Zona;
import br.com.eflux.empreendimento.repository.UnidadeRepsitory;
import br.com.eflux.empreendimento.repository.ZonaRepository;

@Controller
@Customizer(Unidade.class)
public class UnidadeAutoCrudCustonmizedController extends AutoCrudControllerCustomizer {

	/**
	 * Empreendimento no qual uam zona será criada
	 */
	private Zona zona;

	@Autowired
	ZonaRepository zonaRepository;
		
	@Autowired
	AppFlashMessages flahsMsgs;

	@Autowired
	UnidadeRepsitory unidadeRepsitory;
	
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
	@Functionality(name="Adicionar Unidade a uma Zona de um Empreendimento")
	@RequestMapping("/empreendimento/zona/{idZona}/unidade/add")
	public String criarUnidadeNoEmpreendimento(@PathVariable Long idZona, Model model) 
			throws ErroException, InstantiationException, IllegalAccessException {

		zona = zonaRepository.findById(idZona)
				.orElseThrow(() -> new IllegalAddException("Não conseguimos encontrar Zona com o id informado"));
		
		return getController().add("Unidade", model);
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
	@Functionality(name="Editar uma Unidade de um Empreendimento")
	@RequestMapping("/empreendimento/unidade/{idUnidade}")
	public String editarUnidadeDoEmpreendimento(@PathVariable Long idUnidade, Model model) 
			throws ErroException, InstantiationException, IllegalAccessException {
		setZona(idUnidade);
		return getController().select("Unidade", idUnidade, model);
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
	@Functionality(name="Remover uma Unidade de um Empreendimento")
	@RequestMapping("/empreendimento/unidade/{idUnidade}/del")
	public String removerUnidadeDoEmpreendimento(@PathVariable Long idUnidade, Model model, HttpServletRequest request) 
			throws ErroException, InstantiationException, IllegalAccessException, ValidacaoException, SerializationException {
		setZona(idUnidade);
		return getController().del("Unidade", idUnidade, model, request);
	}



	private void setZona(Long idUnidade) {
		Unidade unidade = unidadeRepsitory.findById(idUnidade)
				.orElseThrow(() -> new IllegalAddException("Não conseguimos encontrar Unidade com o id informado"));
		
		zona = unidade.getZona();
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
		crudEntity.getField("zona").setValue(zona);
		crudEntity.getField("zona").setReadOnly(true);
		model.addAttribute("zonaList", zonaRepository.findAllByEmpreendimentoOrderByNivelAscNomeAsc(zona.getEmpreendimento()) );
	}
	
	
	
	/**
	 * altera a página inicial do auto crud
	 */
	@Override
	public String getIndexView() {
		flahsMsgs.getMessages().getSuccessList().add("Zona criada com sucesso");
		return "redirect:/empreendimento/"+zona.getEmpreendimento().getId()+"/unidades";
	}
	
	
	
	
}
