package br.com.dfframeworck.controller;

import java.util.List;

import br.com.dfframeworck.autocrud.AutoCrudAction;

public class AutoCrudControllerCustomizer {
	
	
	public void addActions(List<AutoCrudAction> actions) {
		
	}
	
	public String getIndexView(){
		return "/auto_crud/listar";
	}
	
	/**
	 * Retorna a view padrão do formulário de adição / edição do AutoCrud
	 * @return
	 */
	public String getFormView(){
		return "/auto_crud/form";
	}
}
