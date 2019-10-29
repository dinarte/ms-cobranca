package br.com.dfframeworckservice;

import java.io.Serializable;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Persistable;
import org.springframework.stereotype.Service;

/**
 * Servico capaz de gerenciar qualquer entidade persistível
 * @author Dinarte
 */

@Service
@Transactional
public class AutoCrudService{
	
	@Autowired
	private EntityManager em;

	private static final Logger log = LoggerFactory.getLogger(AutoCrudService.class);

	private static Map<Class<?>, Map<String, String>> defaultOperators;
	
	static {
		defaultOperators = new HashMap<Class<?>, Map<String,String>>();

		 Map<String,String> details = new HashMap<String, String>();
		 details.put("operator", "like");
		 details.put("quot", "'%{value}%'");
		 defaultOperators.put(String.class, details);
		 
		 details = new HashMap<String, String>();
		 details.put("operator", "=");
		 details.put("quot", "'{value}'");
		 defaultOperators.put(Date.class, details);

		 details = new HashMap<String, String>();
		 details.put("operator", "=");
		 details.put("quot", "{value}");
		 defaultOperators.put(Object.class, details);
		 
		 details = new HashMap<String, String>();
		 details.put("operator", "=");
		 details.put("quot", "'{value}'");
		 defaultOperators.put(Enum.class, details);
		
	}
	
	
	public void save(Persistable<?> obj){
		log.debug("Cadastrando entidade {} em  {} ", obj.toString(), getClass().getName() );	
		if( obj.isNew() ){
			em.persist(obj);
		} else {
			em.merge(obj);
		}
	}

	public void remover(Persistable<?> obj) {
		log.debug("Removendo entidade {} em  {} ", obj.toString(), getClass().getName() );
		em.remove(obj);
	}
	
	@SuppressWarnings("unchecked")
	public List<Persistable<?>> findAll(String entity){
		return (List<Persistable<?>>) em.createQuery("from "+entity)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Persistable<?>> findAll(String entity, int firstResult, int maxResults, List<String> filters){
		String hql = "from ".concat(entity);
		
		hql = addFilters(filters, hql);	
		return (List<Persistable<?>>) em.createQuery(hql)
				.setFirstResult(firstResult)
				.setMaxResults(maxResults)
				.getResultList();
	}

	public Long count(String entity, List<String> filters){
		
		return (Long) em.createQuery(addFilters(filters, "select count(id) from "+entity)).getSingleResult();
		
	}

	private String addFilters(List<String> filters, String hql) {
		if (filters !=null && filters.size() > 0) {
			hql = hql.concat(" where 1 = 1");
			
			for (String filter : filters) {
				hql = hql.concat(" and " + filter);
			}
			
		}
		return hql;
	}
	
	
	public Persistable<?> findOne(String entity, Serializable id){
		return (Persistable<?>) em.createQuery("from "+entity+ " where id =:id")
				.setParameter("id", id)
				.getSingleResult();
	}

	public Persistable<?> findFirst(String entity){
		return (Persistable<?>) em.createQuery("from "+entity)
				.setMaxResults(1)
				.getSingleResult();
	}


	public static Map<String, String> getOperatorsDetails(Class<?> type) {
		
		Map<String, String> ret = AutoCrudService.defaultOperators.get(type);
		if (Objects.isNull(ret))
			ret = AutoCrudService.defaultOperators.get(Object.class);
		if (type.isEnum())
			ret = AutoCrudService.defaultOperators.get(Enum.class);
		
		return ret;
	}
	
	
	public static void main(String[] args) {
		System.out.println(AutoCrudService.getOperatorsDetails(String.class));
		System.out.println(AutoCrudService.getOperatorsDetails(Date.class));
		System.out.println(AutoCrudService.getOperatorsDetails(Integer.class));
		System.out.println(AutoCrudService.getOperatorsDetails(Long.class));
		System.out.println(AutoCrudService.getOperatorsDetails(Boolean.class));
	}
	
	
}
