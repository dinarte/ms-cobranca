package br.com.dfframeworck.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import br.com.dfframeworck.autocrud.AutoCrudAction;
import br.com.dfframeworck.autocrud.AutoCrudEntity;
import br.com.dfframeworck.autocrud.AutoCrudField;

/**
 * O DF-Framework permite criar compoentes que adicionam ou modificam comportamentos do AutoCrudController, nós os chamamos de customizadores, 
 * para isso o desenvolverdor pode criar uma classe anotada como @Component ou @Controller e eradar esta classe, além de adicionar a anotação @Customaizer.
 * Uma vez erdada, esta classe fornecerá ao customizador uma série de métodos que representão ações que serão chamados durante a execução do AutoCrudController.
 * Não é obrigatótio existir um customizador para cada entidade configurada para ser gerenciada pelo AutoCrudController, sendo assim ao ser executado o AutoCrud irá
 * criar uma instancia em branco dessa classe, sendo assim nenhum comportamento adicional será executado. 
 * @author Dinarte
 *
 */
public class AutoCrudControllerCustomizer {
	
	@Autowired
	private AutoCrudController controller;
	
	/**
	 * Prover funcinalidade para que o customizador de contreller possa adicionar ações 
	 * ao auto crud.
	 * @param actions
	 */
	public void addActions(List<AutoCrudAction> actions) {
		
	}
	
	/**
	 * Prover funcionalidade para que o customizador de controller possa adicionar filtros
	 * na consulta do auto crud.
	 * @param filters
	 */
	public void addfilters(List<String> filters) {
		// TODO Auto-generated method stub
		
	}
	
	public void addFieterField(List<AutoCrudField> filters) {
		
	}

	/**
	 * Implementa uma ação com o crudEntity no ato da rendereização do formulário 
	 * de adição do auto crud
	 * @param crudEntity
	 */
	public void onCreateAddForm(AutoCrudEntity crudEntity, Model model) {
		
	}
	
	/**
	 * @param crudEntity
	 * @param model
	 */
	public void onSelect(AutoCrudEntity crudEntity, Model model) {
		// TODO Auto-generated method stub
		
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

	

	public AutoCrudController getController() {
		return controller;
	}


	
}
