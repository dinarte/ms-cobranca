package financeiro;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

import br.com.eflux.financeiro.domain.Contrato;
import br.com.eflux.financeiro.domain.Debito;
import br.com.eflux.financeiro.domain.SituacaoDebitoEnum;
import junit.framework.TestCase;

public class CalculoJurosMoraTest extends TestCase{
	
	
	public void testGetJurosAtrazoVencidaComCorrecaoEJurosRemuneratorios() {
		
		Debito d = criarDebito();
		criarDataVencida(d, -1);
		
		d.setCorrecao(new BigDecimal(30));
		d.setJurosRemuneratorio(new BigDecimal(70));
				
		assertEquals(new BigDecimal(4), d.getJurosAtrazo());
		
	}
	
	
	public void testGetJurosAtrazoVencidaComApenasJurosRemuneratorios() {
		
		Debito d = criarDebito();
		criarDataVencida(d, -1);		
		d.setJurosRemuneratorio(new BigDecimal(100));
	
		assertEquals(new BigDecimal(4), d.getJurosAtrazo());
	}
	
	
	public void testGetJurosAtrazoVencidaComApenasCorrecao() {
		
		Debito d = criarDebito();
		criarDataVencida(d, -1);		
		d.setCorrecao(new BigDecimal(100));
	
		assertEquals(new BigDecimal(4), d.getJurosAtrazo());
	}
	
	public void testGetJurosAtrazoVencidaSEMCorrecaoEJurosCompensatorios() {
		Debito d = criarDebito();
		criarDataVencida(d,-1);
		assertEquals(new BigDecimal(2), d.getJurosAtrazo());
	}
	
	

	private void criarDataVencida(Debito d, int qtd) {
		GregorianCalendar hoje = new GregorianCalendar();
		hoje.add(Calendar.DAY_OF_MONTH, qtd);
		d.setDataVencimento(hoje.getTime());
	}

	private Debito criarDebito() {
		Contrato c = new Contrato();
		c.setPorcentagemJuros(2.0);
		Debito d = new Debito();
		d.setSituacao(SituacaoDebitoEnum.VENCIDA);
		d.setContrato(c);
		d.setValorOriginal(new BigDecimal(100.0));
		return d;
	}

}
