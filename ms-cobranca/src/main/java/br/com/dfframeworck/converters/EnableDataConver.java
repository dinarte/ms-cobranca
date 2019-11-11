package br.com.dfframeworck.converters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Habilita o conversor para ser utilizado na interpretação da migração de dados
 * @author Dinarte
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableDataConver  {

	public Class<?>[] types();
	public boolean enableForMigration();
	public boolean enableForForm();
	
}
