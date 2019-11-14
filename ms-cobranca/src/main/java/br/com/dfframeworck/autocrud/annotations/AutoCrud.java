package br.com.dfframeworck.autocrud.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Anotação responsável determinar que uma entidade será exposta para ser administrada
 * através do Auto Crrud.
 * @author dinarte
 */

import br.com.dfframeworck.security.Functionality;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoCrud {
	/**
	 * Titulo da funcionalidade que aparecerá na View
	 * @return
	 */
	public String name();
	
	/**
	 * Descrição da funcionalidade que aparaverá na View
	 * @return
	 */
	public String description();
	
	public String orderBy() default "";
	
	public Functionality funtionality();
}
