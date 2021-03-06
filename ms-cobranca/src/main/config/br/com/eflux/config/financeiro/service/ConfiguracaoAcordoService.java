package br.com.eflux.config.financeiro.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.eflux.config.financeiro.domain.ConfiguracaoAcordo;
import br.com.eflux.config.financeiro.repository.ConfiguracaoAcordoRepository;
import br.com.eflux.empreendimento.domain.Empreendimento;
import br.com.eflux.empreendimento.domain.Incorporadora;
import br.com.eflux.empreendimento.domain.Unidade;
import br.com.eflux.financeiro.domain.Contrato;

@Service
public class ConfiguracaoAcordoService {
	
	@Autowired
	ConfiguracaoAcordoRepository configAcordoRepo;
	
	/**
	 * Executa a busca hierárquica pela configuração retornando a primeira ocorrencia
	 * @param contrato
	 * @return
	 */
	public ConfiguracaoAcordo getFirstByContrato(Contrato contrato) {
		List<ConfiguracaoAcordo> ret = getAllByContrato(contrato);
		return ret.get(0);		
				
	}

	/**
	 * Executa a busca hierárquica e recupera uma lista de configurações de acordo para o contrato informado.
	 * @param contrato
	 * @return
	 */
	public List<ConfiguracaoAcordo> getAllByContrato(Contrato contrato) {
		Unidade unidade = contrato.getUnidade();
		Empreendimento empreendimento = contrato.getUnidade().getZona().getEmpreendimento();
		Incorporadora incorporadora = contrato.getUnidade().getZona().getEmpreendimento().getIncorporadora();
		
		Optional<List<ConfiguracaoAcordo>> ret = configAcordoRepo.findAllByUnidade(unidade);
		if (ret.isPresent())
			return ret.get();	
		
		ret = configAcordoRepo.findAllByEmpreendimento(empreendimento);
		if (ret.isPresent())
			return ret.get();

		ret = configAcordoRepo.findAllByIncorporadora(incorporadora);
		if (ret.isPresent())
			return ret.get();

		ret = configAcordoRepo.findAllBySistema();
		if (ret.isPresent())
			return ret.get();
		
		throw new RuntimeException("Erro de Configuração do sistema: Não existem configurações de acordo no sistema");
		
	}

}
