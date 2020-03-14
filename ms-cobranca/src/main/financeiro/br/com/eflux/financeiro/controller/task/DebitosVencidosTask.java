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
import br.com.eflux.financeiro.repository.ContratoRepository;
import br.com.eflux.financeiro.repository.DebitoRepository;
import br.com.eflux.financeiro.vo.ContratoComDebitosVencidosVo;

@Component
public class DebitosVencidosTask {
	
	private static final Logger log = LoggerFactory.getLogger(DebitosVencidosTask.class);

	@Autowired
	DebitoRepository debitoRepository;
	
	@Autowired
	ContratoRepository contratoRepository;
	

	/**
	 * Task que roda periodicamente para alterar a situação do débito para VENCIDO calculando juros e multa
	 */
	@Transactional
	@Scheduled(fixedRate=2000000)
	public void alterarStatusParaVencido() {
			
			log.info("Task Alterar Situacao de Débitos para Vencido");
			log.info("Buscando por débitos vencidos... ");
			
				List<Debito> debitos = debitoRepository.findAllVencidos(new Date(), SituacaoDebitoEnum.EM_ABERTO);
				
				log.info("Carregando "+debitos.size()+" debitos vencidos");
				
				debitos.stream()
				.forEach(debito -> {
						debito.setSituacao(SituacaoDebitoEnum.VENCIDA);
						debitoRepository.save(debito);
					
				});
			
			log.info("Alteração de situação de Débitos finalizada com sucesso!");
	}
	
	
	
	/**
	 * Task que roda periodicamente para calcular juros e multas de debitos vencidos
	 */
	@Transactional
	@Scheduled(fixedRate=2000000)
	public void atualizarJurosEMulta() {
			
			log.info("Task Alterar Situacao de Débitos para Vencido");
			log.info("Buscando por débitos vencidos... ");
			
				List<Debito> debitos = debitoRepository.findAllVencidos(new Date(), SituacaoDebitoEnum.VENCIDA);
				
				log.info("Carregando "+debitos.size()+" debitos vencidos");
				
				debitos.stream()
				.forEach(debito -> {
						debito.getJurosAtrazo();
						debito.getMultaAtrazo();
						debitoRepository.save(debito);
					
				});
			
			log.info("Atualizaçcao de juros e multa finalizada com sucesso!");
	}
	
	
	/**
	 * Task que roda periodicamente para calcular quantidade de débitos vencidos por contrato
	 */
	@Transactional
	@Scheduled(fixedRate=2000000)
	public void calcularQuantidadeDebitosVencidos() {
			
			log.info("Task Calcular quantidade de débitos vencidos");
			log.info("Buscando por contratos com débitos vencidos... ");
			
				List<ContratoComDebitosVencidosVo> contratos = contratoRepository.findAllContratosComDebitoVencidosVo();
				
				log.info("Carregando "+contratos.size()+" debitos vencidos");
				
				contratos.stream()
				.forEach(c -> {
						contratoRepository.updateQuantidadeDebitosVencidos(c.getIdContrato(), c.getQtdVencidas());
					
				});
			
			log.info("Contabilização de parcelas vencidas executada com sucesso!");
	}


}
