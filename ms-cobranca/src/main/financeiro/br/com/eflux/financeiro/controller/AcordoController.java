package br.com.eflux.financeiro.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.dfframeworck.exception.ValidacaoException;
import br.com.dfframeworck.security.Functionality;
import br.com.eflux.config.financeiro.service.ConfiguracaoAcordoService;
import br.com.eflux.financeiro.domain.Acordo;
import br.com.eflux.financeiro.domain.Contrato;
import br.com.eflux.financeiro.domain.Debito;
import br.com.eflux.financeiro.domain.SituacaoDebitoEnum;
import br.com.eflux.financeiro.dto.AcordoDTO;
import br.com.eflux.financeiro.dto.AcordoMapper;
import br.com.eflux.financeiro.helper.SituacaoDebitoHelper;
import br.com.eflux.financeiro.repository.AcordoRepository;
import br.com.eflux.financeiro.repository.ContratoRepository;
import br.com.eflux.financeiro.repository.DebitoRepository;


/**
 * Operações de acordo
 * @author dinarte
 *
 */
@Controller
public class AcordoController {

	@Autowired
	ContratoRepository contratoRepo;

	@Autowired
	DebitoRepository debitoRepo;

	@Autowired
	AcordoRepository acordoRepo;
	
	@Autowired
	ConfiguracaoAcordoService configAcordoService;
	
	/**
	 * Inicia o processo de acordo listando todos os débitos do contrato que são passíveis de acordo para que sejam selecionadas,
	 * é exibido um formulário com os dados básicos do acordo.
	 * @param idContrato
	 * @return
	 * @throws ValidacaoException 
	 */
	@Functionality(name="Iniciar", isPublic=false, menu="none")
	@RequestMapping("/acordo/{idContrato}/iniciar")
	public String iniciarAcordo(@PathVariable("idContrato") Long idContrato, Model m) throws ValidacaoException {
		
		Contrato contrato = contratoRepo.findById(idContrato)
				.orElseThrow(()->new IllegalArgumentException("Não foi encontrado o de id: "+idContrato));
		
		//chama o método apenas para verificar se existe configuracao
		configAcordoService.getFirstByContrato(contrato);
		
		List<Debito> debitos = buscarDebitosPassiveisDeAcordo(contrato);
		AcordoDTO acordo = AcordoMapper.newDto(contrato, debitos);		
		m.addAttribute("acordoDTO", acordo);
		
		return "/acordo/iniciar";
	}
	
	/**
	 * Segundo passo do acordo onde o sistema exibe as novas parcelas a serem geradas para que o 
	 * usuário revise e possa editar as datas de vencimento.
	 * @return
	 */
	@Functionality(name="Criar proposta", isPublic=false, menu="none")
	@RequestMapping(path = "/acordo/proposta")
	public String proposta(@Valid AcordoDTO acordoDTO, Model m) {
		acordoDTO.getDebitosDoAcordo().forEach(d -> d.setSituacao(SituacaoDebitoEnum.NEGOCIADA));
		acordoDTO.gerarParcelasDoAcordo(configAcordoService.getFirstByContrato(acordoDTO.getContrato()));
		m.addAttribute("acordoDTO", acordoDTO);
		return "/acordo/proposta";
	}
	
	/**
	 * efetiva o acordo, salvando-o no banco de dados.
	 * @return
	 */
	@Functionality(name="Efetivar Acordo", isPublic=false, menu="none")
	@RequestMapping(path = "/acordo/efetivar")
	public String efetivar(@Valid AcordoDTO acordoDTO, Model m) {
		Acordo acordo = AcordoMapper.objectFromDto(acordoDTO, configAcordoService.getFirstByContrato(acordoDTO.getContrato()));
		acordoRepo.save(acordo);
		return "redirect:/acordo/"+acordo.getId()+"/visualizar";
	}


	
	/**
	 * Vizualizar um acordo efetivado
	 * @return
	 */
	@Functionality(name="Vizualiar Acordo", isPublic=false, menu="none")
	@RequestMapping("/acordo/{idAcordo}/visualizar")
	public String vizualizar(@PathVariable("idAcordo") Long idAcordo, Model m) {
		Acordo acordo = acordoRepo.findById(idAcordo)
				.orElseThrow(()-> new IllegalArgumentException("Não foi possível encontrar o acordo com o id: " +idAcordo));
		m.addAttribute("acordoDTO", AcordoMapper.dtoFromObject(acordo));
		return "/acordo/visualizar";
	}


	private List<Debito> buscarDebitosPassiveisDeAcordo(Contrato contrato) throws ValidacaoException {
		List<Debito> debitos = debitoRepo.findAllByContratoAndSituacaoOrderByDataVencimento(contrato, SituacaoDebitoHelper.getPassiveisAcordo());
		if  (debitos.size() == 0) {
			throw new ValidacaoException("Não é possível iniciar um acordo, pois não foram encontrados débitos vencidos.");
		}
		return debitos;
	}

}
