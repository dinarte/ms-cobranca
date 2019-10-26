package br.com.dfframeworck.autocrud.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Anotação responsável por determinar que uma propriedade estará expoxta
 * no Auto Crud.
 * @author dinarte
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EnableAutoCrudField {
	/**
	 * Nome amigável para o campo
	 * @return
	 */
	String label();
	
	/**
	 * descrição / explicação do campo 
	 * @return
	 */
	String description() default "";

	
	/**
	 * Indica se o campo será exibido como um filtro da listagem</br>
	 * valor padrão: false;
	 */
	public boolean enableForFilter() default true;
	
	/**
	 * Indica se o campo vai ser exibido na listagem da entidade
	 * valor padrão: true;
	 */
	public boolean enableForList() default true;
	
	/**
	 * Indica se o campo será exibido no formulário de criação da entidade
	 * valor padrão: true;
	 */
	public boolean enableForCreate() default true;
	
	/**
	 * Indica se o campo será exibido no formulário de alteração da entidade
	 * valor padrão: true;
	 */
	public boolean enableForUpdate() default true;
	/**
	 * Determina se o campo será somente leitura no formulário de alteração
	 * valor padrão: falso;
	 */
	public boolean readOnlyForUpdate() default false;
	
	/**
	 * Caso o tipo do campo for composto, uma entidade do sistema, esse campo deverá conter o nome do camo
	 * desta entidade que deverá ser exibido.
	 */
	public String lookUpFieldName() default "";
	
	/**
	 * Tipo de componente UI que será usado para renderizar o campo no formulário
	 * @return
	 */
	public String ui() default "default";
	
	
	
}
