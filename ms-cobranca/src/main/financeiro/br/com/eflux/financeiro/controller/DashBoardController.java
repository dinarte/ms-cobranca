package br.com.eflux.financeiro.controller;

import java.util.List;
import java.util.Objects;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.annotation.RequestScope;

import br.com.dfframeworck.autocrud.Periodo;
import br.com.dfframeworck.exception.AppException;
import br.com.dfframeworck.security.Functionality;
import br.com.eflux.financeiro.repository.DashBoardRepository;
import br.com.eflux.financeiro.vo.InadiplentesVo;
import br.com.eflux.financeiro.vo.IndicadoresVo;
import br.com.eflux.financeiro.vo.VencimentosVo;

@Controller
@RequestScope
public class DashBoardController {
	
	
	
	@Autowired
	DashBoardRepository dashRepository;

	@Functionality(isPublic=false, name="DashBord", menu="root->home", icon="fa fa-dashboard")
	@RequestMapping({"/home"})
	public String home(Model m, Periodo periodo) throws AppException {		
		periodo.anoAtual();
		m.addAttribute("periodo", periodo);
		processarIndicadores(m, periodo);
		processarVencimentos(m, periodo);
		processarInadiplentes(m, periodo);
		return "pages/home"; 
	}
	
	@ResponseBody
	@RequestMapping(value="/home/indicadores-por-empreendimento", produces=MediaType.APPLICATION_JSON)
	public List<IndicadoresVo> indicadiresPorEmpreendimento(@RequestParam("periodo") String periodoStr){
		Periodo periodo = new Periodo().anoAtual();
		if (Objects.nonNull(periodoStr))
			periodo.setPeriodo(periodoStr);
		List<IndicadoresVo> indicadores = dashRepository.findIndicadoresPorEmpreendimentoByPeriodo(periodo.getInicio(), periodo.getFim());
		return indicadores;
	}
	
	
	private void processarInadiplentes(Model m, Periodo periodo) {
		List<InadiplentesVo> inadiplentes = dashRepository.findInadlipentesByPeriodoLimit(periodo.inicio, periodo.fim, 30);
		m.addAttribute("inadiplentes", inadiplentes);
	}

	private void processarVencimentos(Model m, Periodo periodo) {
		List<VencimentosVo> vencimentos = dashRepository.findDebitosVencidosByPeriodoLimit(periodo.inicio, periodo.fim, 30);
		m.addAttribute("vencimentos", vencimentos);
	}
	
	
	private void processarIndicadores(Model m, Periodo periodo) {
		IndicadoresVo indicadores = dashRepository.findIndicadoresByPeriodo(periodo.inicio, periodo.fim);
		m.addAttribute("indicadores", indicadores);
	}
	
	
	
	
}
