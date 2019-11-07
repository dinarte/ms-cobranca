package br.com.dfframeworck.converters;

import java.util.Objects;

import org.springframework.stereotype.Component;

import br.com.eflux.comum.domain.EstadoCivilEnum;
import br.com.eflux.comum.domain.MesEnum;
import br.com.eflux.comum.domain.SexoEnum;
import br.com.eflux.comum.domain.TipoPessoaEnum;
import br.com.eflux.financeiro.domain.NaturezaContratoEnum;

@Component
@DataConverter(types= {EstadoCivilEnum.class, SexoEnum.class, TipoPessoaEnum.class, NaturezaContratoEnum.class, SexoEnum.class, MesEnum.class})
public class EnumConverter implements IConverter<Enum>{

	@Override
	public Enum parse(String value, Class<?> type) {
		if (Objects.isNull(value))
			return null;
		else if(value.equals("NULL")){
			return null;
		}else if (value.trim().equals("")){
			return null;
		}		
		else {
		  Enum[] consts =	(Enum[]) type.getEnumConstants();
		  for (Enum object : consts) {
			if( object.name().equals(value) )
				return object;
		}
		}
		return null;
	}

}
