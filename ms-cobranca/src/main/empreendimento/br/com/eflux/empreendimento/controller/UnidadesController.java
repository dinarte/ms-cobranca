package br.com.eflux.empreendimento.controller;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.dfframeworck.autocrud.DataTable;
import br.com.dfframeworck.security.Functionality;
import br.com.eflux.empreendimento.domain.Empreendimento;
import br.com.eflux.empreendimento.domain.Unidade;
import br.com.eflux.empreendimento.domain.Zona;
import br.com.eflux.empreendimento.dto.TreeNode;
import br.com.eflux.empreendimento.dto.ZonaNodeDTO;
import br.com.eflux.empreendimento.repository.EmpreendimentoRepository;
import br.com.eflux.empreendimento.repository.UnidadeRepsitory;
import br.com.eflux.empreendimento.repository.ZonaRepository;

@Controller
public class UnidadesController {
	
	
	@Autowired
	EmpreendimentoRepository empreendimentoRepository;
	
	@Autowired
	ZonaRepository zonaRepository;

	@Autowired
	private UnidadeRepsitory unidadeRepository;
	
	@Functionality(isPublic=false, name="Unidades do Empreendimento", menu="none")
	@RequestMapping("/empreendimento/{idEmpreendimento}/unidades")
	public String unidades(@PathVariable("idEmpreendimento") Long idEmpreendimento, Model m) {
		
		Empreendimento empreendimento  = empreendimentoRepository.findById(idEmpreendimento)
											.orElseThrow(() -> new IllegalArgumentException(
													"Não encontramos Empreendimento com o id informado."));
		
	
		m.addAttribute("empreendimento", empreendimento);
		
		
		return "/empreendimento/unidades";
	}
	
	

	@Functionality(isPublic=false, name="Recuparar a árvore de zonas", menu="none")
	@RequestMapping(path="/empreendimento/{idEmpreendimento}/unidades/tree-root", produces=MediaType.APPLICATION_JSON)
	@ResponseBody
	public TreeNode treeRoot(@PathVariable("idEmpreendimento") Long idEmpreendimento, Model m) {
		Empreendimento e = new Empreendimento();
		e.setId(idEmpreendimento);
		List<Zona> zonaList = zonaRepository.findAllByEmpreendimentoAndZonaPaiIsNull(e);
		TreeNode treeRoot = getTreeRootOfZonaNodes(zonaList);
		return treeRoot;
	}
	
	
	
	@Functionality(isPublic=false, name="Recuperar a lista de unidades", menu="none")
	@RequestMapping(path="/empreendimento/zona/{idZona}/unidade", produces=MediaType.APPLICATION_JSON)
	@ResponseBody
	public DataTable listarUnidadesPorZona(@PathVariable("idZona") Long idZona, Model m) {
		Zona z = new Zona();
		z.setId(idZona);
		List<Unidade> unidadeList = unidadeRepository.findAllByZonaOrderByNome(z);
		return DataTable.fromData(unidadeList, new String[]{"id", "nome", "descricao", "area", "matricula"}) ;
		
	}
	
	
	
	private TreeNode getTreeRootOfZonaNodes(List<Zona> zonaList) {
		
		ZonaNodeDTO root = new ZonaNodeDTO();
		addFilhos(zonaList, root);
		return root;
	}



	private void addFilhos(List<Zona> zonaList, ZonaNodeDTO root) {
		zonaList.stream()
			.forEach(zona->{
				ZonaNodeDTO newNode = new ZonaNodeDTO(root, zona);
				root.getChildren().add(newNode);
				List<Zona> zonasFilhas = zonaRepository.findByZonaPaiOrderByNome(zona); 
				if (!zonasFilhas.isEmpty());
					addFilhos(zonasFilhas, newNode);
			});
	}
	

}
