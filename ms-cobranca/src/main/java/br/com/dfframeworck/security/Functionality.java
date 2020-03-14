package br.com.dfframeworck.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Interface para ser utilizado nos métodos que fiarão protegidos pela camada de segurança da aplicação,
 * Os métodos antados também serão entendidos pelo sistema como uma funcionalidade.
 * @author dinarte
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Functionality {
	
	boolean isPublic() default false;
	
	String name();
	
	String menu() default "none";
	
	String icon() default "fa fa-table";

}
