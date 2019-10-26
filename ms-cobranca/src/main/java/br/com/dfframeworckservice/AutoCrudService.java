package br.com.dfframeworckservice;

import java.io.Serializable;
import java.util.List;

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

	private static final Logger log = LoggerFactory.getLogger(AutoCrudService.class);
	
	@Autowired
	private EntityManager em;
	
	public void cadastrar(Persistable<?> obj){
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
	public List<Persistable<?>> findAll(String entity, int firstResult, int maxResults){
		return (List<Persistable<?>>) em.createQuery("from "+entity)
				.setFirstResult(firstResult)
				.setMaxResults(maxResults)
				.getResultList();
	}
	
	public Long count(String entity){
		return (Long) em.createQuery("select count(id) from "+entity).getSingleResult();
				
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


	
	
	
	
}
