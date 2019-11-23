package br.com.dfframeworck.view;

import org.springframework.stereotype.Component;

import br.com.dfframeworck.autocrud.AutoCrudField;

/**
 * Componente dicionado a view para prover uma formatação padrão para determinados tiopos de Dados
 * @author dinarte
 *
 */
@Component
public class AutoCrudFormater {
	
	/**
	 * Chama o formatador padrão do AutoCrudField
	 * @param value
	 * @param type
	 * @return
	 */
	public Object format(Object value, AutoCrudField field){
		System.out.println(field.getFieldName());
		return AutoCrudField.getFormatedValue(field, value, field.getType());
	}	

}
