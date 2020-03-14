package br.com.eflux.empreendimento.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.eflux.empreendimento.domain.Empreendimento;

@Repository
public interface EmpreendimentoRepository extends CrudRepository<Empreendimento, Long>{

}
