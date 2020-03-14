package br.com.eflux.financeiro.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.dfframeworck.autocrud.components.LastFilters;
import br.com.dfframeworck.exception.ValidacaoException;
import br.com.dfframeworck.security.Autenticacao;
import br.com.dfframeworck.security.Functionality;
import br.com.eflux.financeiro.domain.Debito;
import br.com.eflux.financeiro.dto.BaixaDebitoDTO;
import br.com.eflux.financeiro.helper.SituacaoDebitoHelper;
import br.com.eflux.financeiro.repository.DebitoRepository;
import br.com.eflux.financeiro.service.BaixaManualService;
import br.com.eflux.payments.api.PaymentApiException;

@Controller
public class DebitoBaixaManualController {
	
	@Autowired
	DebitoRepository debitoRepository;

	@Autowired
	BaixaManualService baixaManualService;
	
	@Autowired
	Autenticacao autenticacao;
	
	@Autowired
	LastFilters lastFilters;
	
	@RequestMapping(path="/debito/{idDebito}/baixar", method=RequestMethod.GET)
	@Functionality(name="Baixar débito manualmente")
	public String baixar(@PathVariable Long idDebito, Model m, HttpServletRequest request) throws ValidacaoException {
		
		Debito debito = debitoRepository.findById(idDebito).orElseThrow(()->new IllegalArgumentException("Não encontramos o débito informado."));
		
		if (SituacaoDebitoHelper.getNaoPassiveisQuitacao().contains(debito.getSituacao()) )
			throw new ValidacaoException("Não é possível prosseguir com a baixa manual, pois o Débito já encontra-se com a situação "+ debito.getSituacao());
		
		BaixaDebitoDTO baixa = new BaixaDebitoDTO();
		baixa.setIdDebito(debito.getId());
		baixa.setDataUltimoPagamento(new Date());
		baixa.setValorPago(debito.getValorAtualizado());
		
		m.addAttribute(debito);
		m.addAttribute("baixa", baixa);
		
		return "/debito/baixar";
		
	}
	
	
	@RequestMapping(path="/debito/baixar", method=RequestMethod.POST)
	@Functionality(name="Baixar débito manualmente")
	public String baixar(BaixaDebitoDTO baixa, Model m) throws ValidacaoException, PaymentApiException {
		baixa.setUsuario(autenticacao.getUsuario());
		validate(baixa);
		baixaManualService.baixar(baixa);
		return lastFilters.getRedirectFor();
	}


	private void validate(BaixaDebitoDTO baixa) throws ValidacaoException {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	    Validator validator = factory.getValidator();
	    Set<ConstraintViolation<Object>> restricoes = validator.validate(baixa);
	    if (!restricoes.isEmpty()) {
	    	List<String> erros = new ArrayList<>();
	    	restricoes.forEach(c ->{
	    		erros.add(c.getMessage());
	    	});
	    	throw new ValidacaoException(erros);
	    }
	}
	
	

}
