package br.com.dfframeworck.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
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
import br.com.dfframeworck.exception.ValidacaoException;
import br.com.dfframeworck.messages.SucessMsg;
import br.com.dfframeworck.repository.Migrable;
import br.com.dfframeworck.security.Functionality;
import br.com.dfframeworck.util.SerializationException;
import br.com.dfframeworckservice.AutoCrudService;
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
	
	@Autowired
	ApplicationContext context;
		
	@Autowired
	AutoCrudCusomizerProvider customizerProvider;
	

	/**
	 * Exibe uma página com a listagem dos registros encontrados referentes 
	 * a entidade que está sendo acessada
	 * @param entity
	 * @param model
	 * @return
	 * @throws ErroException
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@Functionality(isPublic=false, name="Listar", menu="none")
	@RequestMapping("/crud/{entity}")
	public String index(@PathVariable("entity") String entity, Model model, HttpServletRequest request) throws ErroException, InstantiationException, IllegalAccessException {
		AutoCrudEntity crudEntity = helper.constructCrudEntity(entity);
		processListing(entity, model, request, crudEntity);
		return customizerProvider.getCustomizer(crudEntity.getType()).getIndexView();
	}

	private void processListing(String entity, Model model, HttpServletRequest request, AutoCrudEntity crudEntity) {
		processListing(entity, model, request, crudEntity, false);
	}
	
	private void processListingNoFilter(String entity, Model model, HttpServletRequest request, AutoCrudEntity crudEntity) {
		processListing(entity, model, request, crudEntity, true);
	}

	private void processListing(String entity, Model model, HttpServletRequest request, AutoCrudEntity crudEntity, boolean noFilter) {
		
		List<String> filters = new ArrayList<String>();
		
		if (!noFilter) {		
			filters = helper.processFilters(crudEntity, request);
			customizerProvider.getCustomizer(crudEntity.getType()).addfilters(filters);
		}
			
		String page = request.getParameter("page");
		String size = request.getParameter("size");
		AutoCrudPagination pagination = new AutoCrudPagination(page, size );
		Long count = crudService.count(entity, filters);
		pagination.setTotalResults(count.intValue());
		List<?> entityList = crudService.findAll(entity, crudEntity.getMeta().orderBy(),pagination.getFirstRow(), pagination.getSize(), filters);
		
		AutoCrudData autoCrudData = new AutoCrudData();
		autoCrudData.setEntity(crudEntity);
		autoCrudData.setPagination(pagination);
		customizerProvider.getCustomizer(crudEntity.getType()).addActions(autoCrudData.getActions());
		autoCrudData.parseData(entityList);
		
		model.addAttribute("autoCrudData", autoCrudData);
	}



	/**
	 * Seleciona uma entidade de um determinado id e exibe o formulário de edição.
	 * @param entity
	 * @param id
	 * @param model
	 * @return
	 * @throws ErroException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@Functionality(isPublic=false, name="Selecionar", menu="none")
	@RequestMapping("/crud/{entity}/{id}")
	public String select(@PathVariable("entity") String entity, @PathVariable("id") Long id, Model model) throws ErroException, InstantiationException, IllegalAccessException {
		AutoCrudEntity crudEntity = helper.constructCrudEntity(entity, crudService.findOne(entity, id));
		
		customizerProvider.getCustomizer(crudEntity.getType()).onSelect(crudEntity, model);
		
		model.addAttribute("autoCrudEntity", crudEntity);
		return customizerProvider.getCustomizer(crudEntity.getType()).getFormView();
	}
	
	
	
	/**
	 * Direciona para o formulário de cadastro da entidade.
	 * @param entity
	 * @param id
	 * @param model
	 * @return
	 * @throws ErroException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@Functionality(isPublic=false, name="Adicionar", menu="none")
	@RequestMapping("/crud/{entity}/add")
	public String add(@PathVariable("entity") String entity, Model model) throws ErroException, InstantiationException, IllegalAccessException {
	
		AutoCrudEntity crudEntity = helper.constructCrudEntity(entity);
		
		customizerProvider.getCustomizer(crudEntity.getType()).onCreateAddForm(crudEntity, model);
		
		model.addAttribute("autoCrudEntity",crudEntity);
		return customizerProvider.getCustomizer(crudEntity.getType()).getFormView();
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
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws SerializationException 
	 * @throws ValidacaoException 
	 */
	@Functionality(isPublic=false, name="Savlar", menu="none")
	@RequestMapping(path="/crud/{entity}/save", method=RequestMethod.POST)
	@SucessMsg
	public String save(@PathVariable("entity") String entity, Model model, HttpServletRequest request) throws ErroException, 
		IOException, InstantiationException, IllegalAccessException, SerializationException, ValidacaoException {
		
		AutoCrudEntity crudEntity = helper.constructCrudEntity(entity);
		
		Object obj = crudEntity.getType().newInstance();
		String id = request.getParameter(entity+".id");
		if ( Strings.isNotEmpty( id ) )
			obj = crudService.findOne(entity, Long.parseLong(id));
		
		obj =  helper.processEntityObjectNew(crudEntity, request, obj);
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	    Validator validator = factory.getValidator();
	    Set<ConstraintViolation<Object>> restricoes = validator.validate(obj);
	    if (!restricoes.isEmpty()) {
	    	List<String> erros = new ArrayList<>();
	    	restricoes.forEach(c ->{
	    		erros.add(crudEntity.getField( c.getPropertyPath().iterator().next().toString() ).getMeta().label() +": "+ c.getMessage());
	    	});
	    	throw new ValidacaoException(erros);
	    }
		
		crudService.save((Persistable<?>)obj);
		processListingNoFilter(entity, model, request, crudEntity);
		return customizerProvider.getCustomizer(crudEntity.getType()).getIndexView();
		
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
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ValidacaoException 
	 * @throws SerializationException 
	 */
	@SuppressWarnings("unchecked")
	@Functionality(isPublic=false, name="Remover", menu="none")
	@RequestMapping(path="/crud/{entity}/{id}/del")
	@SucessMsg
	public String del(@PathVariable("entity") String entity, @PathVariable("id") Long id, Model model, HttpServletRequest request) 
		throws ErroException, InstantiationException, IllegalAccessException, ValidacaoException, SerializationException {
		
		
		AutoCrudEntity crudEntity = helper.constructCrudEntity(entity);
		request.setAttribute(entity+".id", id);
		Migrable<Long> obj =  (Migrable<Long>) helper.processEntityObject(crudEntity, request);
		obj.setId(id);
		obj = (Migrable<Long>) crudService.findOne(entity, id);
		try {
			crudService.remover((Persistable<?>)obj);
		}catch (DataIntegrityViolationException e) {
			throw new ValidacaoException("Impossível remover este registro, pois ele ainda está sendo referenciado pelo sistema");
		}{
			
		}
		processListing(entity, model, request, crudEntity);
		return customizerProvider.getCustomizer(crudEntity.getType()).getIndexView();
	}
	
}
