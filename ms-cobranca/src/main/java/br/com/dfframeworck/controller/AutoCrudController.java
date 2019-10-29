package br.com.dfframeworck.controller;


import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Persistable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.annotation.RequestScope;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.dfframeworck.autocrud.AutoCrudData;
import br.com.dfframeworck.autocrud.AutoCrudEntity;
import br.com.dfframeworck.autocrud.AutoCrudPagination;
import br.com.dfframeworck.autocrud.components.AutoCrudHelper;
import br.com.dfframeworck.exception.ErroException;
import br.com.dfframeworck.messages.SucessMsg;
import br.com.dfframeworck.repository.Migrable;
import br.com.dfframeworck.security.Functionality;
import br.com.dfframeworck.util.ObjectToMap;
import br.com.dfframeworckservice.AutoCrudService;
import br.com.eflux.comum.domain.Pessoa;
/**
 * Controller responsável por gerenciar as operações de crud de qualquer entidade.. heheheh
 * @author dinarte
 *
 */
@Controller
@RequestScope
public class AutoCrudController {
	
	@Autowired
	AutoCrudService crudService;
	
	@Autowired
	AutoCrudHelper helper;
	

	/**
	 * Exibe uma página com a listagem dos registros encontrados referentes 
	 * a entidade que está sendo acessada
	 * @param entity
	 * @param model
	 * @return
	 * @throws ErroException
	 */
	@Functionality(isPublic=false, name="Listar", menu="none")
	@RequestMapping("/crud/{entity}")
	public String index(@PathVariable("entity") String entity, Model model, HttpServletRequest request) throws ErroException {
		AutoCrudEntity crudEntity = helper.constructCrudEntity(entity);
		processListing(entity, model, request, crudEntity);
		return getIndexView();
	}



	private void processListing(String entity, Model model, HttpServletRequest request, AutoCrudEntity crudEntity) {
		List<String> filters = helper.processFilters(crudEntity, request);
		String page = request.getParameter("page");
		String size = request.getParameter("size");
		AutoCrudPagination pagination = new AutoCrudPagination(page, size );
		Long count = crudService.count(entity, filters);
		pagination.setTotalResults(count.intValue());
		List<?> entityList = crudService.findAll(entity,pagination.getFirstRow(), pagination.getSize(), filters);
		
		AutoCrudData autoCrudData = new AutoCrudData();
		autoCrudData.setEntity(crudEntity);
		autoCrudData.parseData(entityList);
		autoCrudData.setPagination(pagination);
		
		model.addAttribute("autoCrudData", autoCrudData);
	}



	/**
	 * Seleciona uma entidade de um determinado id e exibe o formulário de edição.
	 * @param entity
	 * @param id
	 * @param model
	 * @return
	 * @throws ErroException 
	 */
	@Functionality(isPublic=false, name="Selecionar", menu="none")
	@RequestMapping("/crud/{entity}/{id}")
	public String select(@PathVariable("entity") String entity, @PathVariable("id") Long id, Model model) throws ErroException {
		AutoCrudEntity crudEntity = helper.constructCrudEntity(entity, crudService.findOne(entity, id));
		model.addAttribute("autoCrudEntity", crudEntity);
		return getFormView();
	}
	
	
	
	/**
	 * Direciona para o formulário de cadastro da entidade.
	 * @param entity
	 * @param id
	 * @param model
	 * @return
	 * @throws ErroException 
	 */
	@Functionality(isPublic=false, name="Adicionar", menu="none")
	@RequestMapping("/crud/{entity}/add")
	public String add(@PathVariable("entity") String entity, Model model) throws ErroException {
		AutoCrudEntity crudEntity = helper.constructCrudEntity(entity);
		model.addAttribute("autoCrudEntity",crudEntity);
		return getFormView();
	}
	
	
	
	/**
	 * Salva a entidade no banco.
	 * @param entity
	 * @param id
	 * @param model
	 * @return
	 * @throws ErroException 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@Functionality(isPublic=false, name="Savlar", menu="none")
	@RequestMapping(path="/crud/{entity}/save", method=RequestMethod.POST)
	@SucessMsg
	public String save(@PathVariable("entity") String entity, Model model, HttpServletRequest request) throws ErroException, JsonParseException, JsonMappingException, IOException {
		AutoCrudEntity crudEntity = helper.constructCrudEntity(entity);
		Object obj =  helper.processEntityObject(crudEntity, request);
		crudService.save((Persistable<?>)obj);
		processListing(entity, model, request, crudEntity);
		return getIndexView();
	}
	
	
	/**
	 * Remove a entidade do banco de dados.
	 * @param entity
	 * @param id
	 * @param model
	 * @return
	 * @throws ErroException 
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@Functionality(isPublic=false, name="Remover", menu="none")
	@RequestMapping(path="/crud/{entity}/{id}/del")
	@SucessMsg
	public String del(@PathVariable("entity") String entity, @PathVariable("id") Long id, Model model, HttpServletRequest request) throws ErroException, JsonParseException, JsonMappingException, IOException {
		AutoCrudEntity crudEntity = helper.constructCrudEntity(entity);
		request.setAttribute(entity+".id", id);
		Migrable<Long> obj =  (Migrable<Long>) helper.processEntityObject(crudEntity, request);
		obj.setId(id);
		obj = (Migrable<Long>) crudService.findOne(entity, id);
		crudService.remover((Persistable<?>)obj);
		processListing(entity, model, request, crudEntity);
		return getIndexView();
	}
	
	
	
	/**
	 * Retorna a view padrão para a istagem do Auto Crud
	 * @return
	 */
	protected String getIndexView(){
		return "/auto_crud/listar";
	}
	
	/**
	 * Retorna a view padrão do formulário de adição / edição do AutoCrud
	 * @return
	 */
	protected String getFormView(){
		return "/auto_crud/form";
	}

	
	public static void main(String[] args) {
		System.out.println(ObjectToMap.toMap(new Pessoa()));
	}
	
	
}
