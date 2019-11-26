package financeiro;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

import br.com.eflux.financeiro.domain.Contrato;
import br.com.eflux.financeiro.domain.Debito;
import junit.framework.TestCase;

public class CalculoMultaTest extends TestCase{
	
	
	public void testGetMultaAtrazoVencidaComCorrecaoEJurosRemuneratorios() {
		
		Debito d = criarDebito();
		criarDataVencida(d);
		
		d.setCorrecao(new BigDecimal(30));
		d.setJurosRemuneratorio(new BigDecimal(70));
				
		assertEquals(new BigDecimal(4), d.getMultaAtrazo());
		
	}
	
	
	public void testGetMultaAtrazoVencidaComApenasJurosRemuneratorios() {
		
		Debito d = criarDebito();
		criarDataVencida(d);		
		d.setJurosRemuneratorio(new BigDecimal(100));
	
		assertEquals(new BigDecimal(4), d.getMultaAtrazo());
	}
	
	
	public void testGetMultaAtrazoVencidaComApenasCorrecao() {
		
		Debito d = criarDebito();
		criarDataVencida(d);		
		d.setCorrecao(new BigDecimal(100));
	
		assertEquals(new BigDecimal(4), d.getMultaAtrazo());
	}
	
	public void testGetMultaAtrazoVencidaSEMCorrecaoEJurosCompensatorios() {
		Debito d = criarDebito();
		criarDataVencida(d);
		assertEquals(new BigDecimal(2), d.getMultaAtrazo());
	}
	
	

	private void criarDataVencida(Debito d) {
		Calendar hoje = new GregorianCalendar();
		hoje.set(2019, 10, 1);
		d.setDataVencimento(hoje.getTime());
	}

	private Debito criarDebito() {
		Contrato c = new Contrato();
		c.setPorcentagemMulta(2.0);
		Debito d = new Debito();
		d.setContrato(c);
		d.setValorOriginal(new BigDecimal(100.0));
		return d;
	}

}
