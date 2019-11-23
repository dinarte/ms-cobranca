package br.com.eflux.config.financeiro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.eflux.config.financeiro.domain.ConfiguracaoAcordo;
import br.com.eflux.empreendimento.domain.Empreendimento;
import br.com.eflux.empreendimento.domain.Incorporadora;
import br.com.eflux.empreendimento.domain.Unidade;

@Repository
public interface ConfiguracaoAcordoRepository extends CrudRepository<ConfiguracaoAcordo, Long> {
	
	public Optional<List<ConfiguracaoAcordo>> findAllByUnidade(Unidade unidade);

	public Optional<List<ConfiguracaoAcordo>> findAllByEmpreendimento(Empreendimento empreendimento);

	public Optional<List<ConfiguracaoAcordo>> findAllByIncorporadora(Incorporadora incorporadora);
	
	@Query("from ConfiguracaoAcordo where unidade = null and empreendimento = null and incorporadora = null")
	public Optional<List<ConfiguracaoAcordo>> findAllBySistema();
	
}
