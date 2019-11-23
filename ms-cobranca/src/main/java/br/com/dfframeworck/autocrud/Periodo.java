package br.com.dfframeworck.autocrud;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import br.com.dfframeworck.converters.DateConverter;

public class Periodo{
	
	public Date inicio;
	public Date fim;
	
	
	public Periodo() {
		//anoAtual();
	}
	
	public Periodo(String periodo) {
		this.setPeriodo(periodo);;
	}
	
	
	public void setPeriodo(String periodo) {
		String[] token = periodo.split(" - ");
		this.inicio = new DateConverter().parse(token[0], Date.class);
		this.fim = new DateConverter().parse(token[1], Date.class);
	}
	
	public String getPeriodo() {
		if (inicio == null || fim == null)
			return "";
		SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
		return formater.format(inicio) + " - " + formater.format(fim);
	}
	
	public String getSqlStyle() {
		if (inicio == null || fim == null)
			return "";
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		return  " '"+formater.format(inicio) + "' AND '" + formater.format(fim)+"'";
	}
	
	
	public Periodo mesAtual() {
		Calendar hoje = new GregorianCalendar();
		int frist = hoje.getMinimum(Calendar.DAY_OF_MONTH); 
		int last = hoje.getMaximum(Calendar.DAY_OF_MONTH);
		int ano = hoje.get(Calendar.YEAR);
		int mes = hoje.get(Calendar.MONTH);
		Calendar inicio = new GregorianCalendar(ano, mes, frist);
		Calendar fim = new GregorianCalendar(ano, mes, last);

		this.inicio = inicio.getTime();
		this.fim = fim.getTime();
		
		return this;
	}
	
	public Periodo blank() {
		this.inicio = null;
		this.fim = null;
		return null;
	}
	
	public Periodo anoAtual() {
		Calendar hoje = new GregorianCalendar();
		int ano = hoje.get(Calendar.YEAR);
		
		Calendar data = new GregorianCalendar(2019,11,1);
		int last = data.getMaximum(Calendar.DAY_OF_MONTH);
		
		Calendar inicio = new GregorianCalendar(ano, 0, 1);
		Calendar fim = new GregorianCalendar(ano, 11, last);

		this.inicio = inicio.getTime();
		this.fim = fim.getTime();
		
		return this;
	}

	public String getInicioStr(String partner) {
		if (inicio == null)
			return "";
		SimpleDateFormat formater = new SimpleDateFormat(partner);
		return formater.format(inicio); 
	}

	public String getFimStr(String partner) {
		if (fim == null)
			return "";
		SimpleDateFormat formater = new SimpleDateFormat(partner);
		return formater.format(fim);
	}

	public Date getInicio() {
		return inicio;
	}


	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}


	public Date getFim() {
		return fim;
	}


	public void setFim(Date fim) {
		this.fim = fim;
	}
	
	@Override
	public String toString() {
		return getSqlStyle();
	}
	
	
}