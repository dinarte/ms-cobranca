package br.com.dfframeworck.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AutoCrudCusomizerProvider {
	
	@Autowired  
	ApplicationContext context;
	
	/**
	 * Carrega o been de customização do Auto Crud para a entidade em questão, caso não haja, retorna um customaizer padrão.
	 * @param type
	 * @return
	 */
	public AutoCrudControllerCustomizer getCustomizer(Class<?> type){
		
		AutoCrudControllerCustomizer customizer = (AutoCrudControllerCustomizer) 
				context.getBeansWithAnnotation(Customizer.class)
				.values()
				.stream()
				.filter(bean->bean.getClass().getDeclaredAnnotation(Customizer.class).value().equals(type))
				.findFirst()
				.orElse(new AutoCrudControllerCustomizer());
		
		return customizer;
				
	}

}
