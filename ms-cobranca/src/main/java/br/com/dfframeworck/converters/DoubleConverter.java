package br.com.dfframeworck.converters;

import org.springframework.stereotype.Component;

import br.com.dfframeworck.autocrud.Periodo;

@Component
@EnableDataConver(types= {Periodo.class}, enableForForm=true, enableForMigration=true)
public class DoubleConverter implements IConverter<Periodo> {
	
	@Override
	public Periodo parse(String value, Class<?> type) {
		Periodo p = new Periodo();
		p.setPeriodo(value);
		return p;
	}
	
}
