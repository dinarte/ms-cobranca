package br.com.dfframeworck.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.RequestScope;

import br.com.dfframeworck.autocrud.AutoCrudData;
import br.com.dfframeworck.autocrud.AutoCrudEntity;
import br.com.dfframeworck.autocrud.AutoCrudField;
import br.com.dfframeworck.autocrud.AutoCrudPagination;
import br.com.dfframeworck.autocrud.annotations.AutoCrud;
import br.com.dfframeworck.exception.ErroException;
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
	EntityManager em;

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
	public String index(@PathVariable("entity") String entity, Map<String,Object> filtros, Model model, HttpServletRequest request) throws ErroException {
		
		EntityType<?> meta = validateIsAutoCrudEnabledAndReturnEntity(entity);
		AutoCrudEntity crudEntity = getAutoCrudEntity(entity, meta);
				
		String page = request.getParameter("page");
		String size = request.getParameter("size");
		AutoCrudPagination pagination = new AutoCrudPagination(page, size);
		Long count = crudService.count(entity);
		pagination.setTotalResults(count.intValue());
		List<?> entityList = crudService.findAll(entity,pagination.getFirstRow(), pagination.getSize());
		
		AutoCrudData autoCrudList = new AutoCrudData();
		autoCrudList.setEntity(crudEntity);
		autoCrudList.parseData(entityList);
		autoCrudList.setPagination(pagination);
		
		model.addAttribute("autoCrudData", autoCrudList);
		
		/*
		try {
			model.addAttribute(ObjectToMap.toMap(crudEntity.getType().newInstance()));
		} catch (InstantiationException | IllegalAccessException e) {
			throw new ErroException("Não foi possível instanciar a entidade: "+e.getMessage());
		}
		*/
		
		return getIndexView();
	}
	
	@Functionality(isPublic=false, name="Buscar", menu="none")
	@RequestMapping("/crud/{entity}/busca")
	public String buscar(@PathVariable("entity") String entity, Model model, HttpServletRequest request) throws ErroException {
		
		Map<String,Object> filtros = new HashMap<String, Object>();
		
		request.getParameterMap()
			.keySet()
			.stream()
			.filter(k -> k.contains(entity+"."))
			.forEach(k -> {
				System.out.println(k + " like '" + request.getParameter(k) +"'" );
				
				filtros.put(k.replace(entity+".", ""), request.getParameter(k));
				
			});
		
		
		return index(entity, filtros, model, request);
	}

	private AutoCrudEntity getAutoCrudEntity(String entity, EntityType<?> meta) {
		AutoCrudEntity crudEntity = new AutoCrudEntity();
		crudEntity.setEntityName(entity);
		crudEntity.setType(meta.getJavaType());
		crudEntity.setFields(new ArrayList<AutoCrudField>());
		
		meta.getAttributes().forEach( atribute -> {
			AutoCrudField field = new AutoCrudField();
			field.setType(atribute.getJavaType());
			field.setFieldName(atribute.getName());
			field.setEntity(crudEntity);;
			crudEntity.getFields().add(field);
		});
		return crudEntity;
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
		validateIsAutoCrudEnabledAndReturnEntity(entity);
		model.addAttribute("entityList", crudService.findOne(entity, id));
		return "/pages/home";
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
	
	
	/**
	 * Valida se a entidade que está endo acessada existe ou possue a anotação AuntoCrud
	 * @param entity
	 * @throws ErroException
	 */
	@SuppressWarnings("deprecation")
	private EntityType<?> validateIsAutoCrudEnabledAndReturnEntity(String entity) throws ErroException {
	  Set<javax.persistence.metamodel.EntityType<?>> entities = ((Session) em.getDelegate()).getSessionFactory().getMetamodel().getEntities() ;
      EntityType<?> entityType = entities.stream().filter(e -> e.getName().equals(entity)).findFirst().orElseThrow(()->throwAutoCrudNotAllowadException(entity));
      if (!entityType.getJavaType().isAnnotationPresent(AutoCrud.class))
    	 throw throwAutoCrudNotAllowadException(entity);
      return entityType;
	}

	/**
	 * Cria um ErroException no sistema para o caso de a entidade ue está sendo acessada não estar acessível pelo AutoCrud
	 * @param entity
	 * @return
	 */
	private ErroException throwAutoCrudNotAllowadException(String entity) {
		return new ErroException(entity + " Não é uma entidade do sistema, ou não é acessível pelo Auto Crud");
	}
	
	public static void main(String[] args) {
		System.out.println(ObjectToMap.toMap(new Pessoa()));
	}
	
	
}
