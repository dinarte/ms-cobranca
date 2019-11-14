package br.com.eflux.financeiro.controller.task;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.eflux.financeiro.domain.Debito;
import br.com.eflux.financeiro.domain.SituacaoDebitoEnum;
import br.com.eflux.financeiro.repository.DebitoRepository;

@Component
public class DebitosVencidosTask {
	
	private static final Logger log = LoggerFactory.getLogger(DebitosVencidosTask.class);

	@Autowired
	DebitoRepository debitoRepository;
	

	/**
	 * Task que roda periodicamente para alterar a situação do débito para VENCIDO
	 */
	@Transactional
	@Scheduled(fixedRate=2000000)
	public void executarGeracao() {
			
			log.info("Task Alterar Situacao de Débitos para Vencido");
			log.info("Buscando por débitos vencidos... ");
			
				List<Debito> debitos = debitoRepository.findAllVencidos(new Date(), SituacaoDebitoEnum.EM_ABERTO);
				
				log.info("Carregando "+debitos.size()+" debidos vencidos");
				
				debitos.stream()
				.forEach(debito -> {
						debito.setSituacao(SituacaoDebitoEnum.VENCIDA);
						debitoRepository.save(debito);
					
				});
			
			log.info("Alteração de situação de Débitos finalizada com sucesso!");
		}


}
