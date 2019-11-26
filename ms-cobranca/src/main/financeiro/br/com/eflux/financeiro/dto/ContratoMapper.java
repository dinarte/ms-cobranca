package br.com.eflux.financeiro.dto;

import java.util.List;

import br.com.eflux.financeiro.domain.Acordo;
import br.com.eflux.financeiro.domain.Contrato;
import br.com.eflux.financeiro.domain.Debito;

public class ContratoMapper {
	
	
	public static ContratoDTO dtoFromObjects(Contrato contrato, List<Debito> debitos, List<Acordo> acordos) {
		
		ContratoDTO dto = new ContratoDTO();
		dto.setContrato(contrato);
		dto.setDebitoList(debitos);
		dto.setAcordoList(acordos);
		return dto;
	}

}
