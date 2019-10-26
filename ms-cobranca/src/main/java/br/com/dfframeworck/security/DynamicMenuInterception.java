package br.com.dfframeworck.security;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.logging.MemoryHandler;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import br.com.dfframeworck.autocrud.annotations.AutoCrud;
import br.com.dfframeworck.controller.AutoCrudController;
import br.com.dfframeworckservice.AutoCrudService;


@Component
@RequestScope
public class DynamicMenuInterception implements HandlerInterceptor {
	
	private static final Logger log = LoggerFactory.getLogger(DynamicMenuInterception.class);
	
	@Autowired
	ApplicationContext context; 
	
	@Autowired
	EntityManager em;
	
	@Autowired
	MenuNode menuRoot;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception{
		HandlerMethod met = (HandlerMethod) handler;
		
		log.info("Interceptando " + met.getBeanType() + "." + met.getMethod().getName() + " e iniciando a scanear o menu dinâmico.");
		log.info("Analisando as funcionalidades declaradas nos controlleres.");
		java.util.List<MenuItem> menuItens = new ArrayList<MenuItem>();

		
		String[] controllers = context.getBeanNamesForAnnotation(Controller.class);
		for (String controllerName : controllers) {
			Method[] methods = context.getBean(controllerName).getClass().getMethods();
			for (Method method : methods) {
				Functionality funcionality = method.getAnnotation(Functionality.class);
				if (Objects.nonNull(funcionality) && !funcionality.menu().equals("none")) {
					RequestMapping rquestMapping = method.getAnnotation(RequestMapping.class);
					if (Objects.nonNull(RequestMapping.class)) {
						MenuItem mi = new MenuItem();
						mi.setController(context.getBean(controllerName).getClass());
						mi.setMethod(method);
						mi.setHierarchy(funcionality.menu());
						mi.setName(funcionality.name());
						mi.setPath(rquestMapping.value()[0]);
						menuItens.add(mi);
					}
				}
			}
		}
		log.info("Analisando as funcionalidades de AutoCrud declaradas nas entidades.");
		
		Set<javax.persistence.metamodel.EntityType<?>> entities = ((Session) em.getDelegate()).getSessionFactory().getMetamodel().getEntities() ;
		entities
		.stream()
		.filter(item->item.getJavaType().isAnnotationPresent(AutoCrud.class))
		.forEach(iten->{
			Functionality funcionality = iten.getJavaType().getAnnotation(AutoCrud.class).funtionality();
			if (Objects.nonNull(funcionality) && !funcionality.menu().equals("none")) {
				MenuItem mi = new MenuItem();
				Method[] methods = AutoCrudController.class.getMethods();
				for (Method method : methods) {
					if(method.isAnnotationPresent(RequestMapping.class)) {
						if (method.getName().equals("index")) {
							mi.setController(AutoCrudController.class);
							mi.setMethod(method);
							mi.setHierarchy(funcionality.menu());
							mi.setName(funcionality.name());
							mi.setPath(method.getAnnotation(RequestMapping.class).value()[0].replace("{entity}", iten.getName()));
							if (mi.getPath() == null) mi.setPath("#");
							menuItens.add(mi);
						}	
					}
				}
			}	
		});
		log.info("Criando estrutura de menu hierárquico.");
		menuItens.forEach(m -> { 
			//System.out.println(m.getHierarchy() +" - " +m.getPath());
			menuRoot.add(m, m.getHierarchy());
		});
		log.info("Fim da interceptação.");
		
		menuRoot.getChildren().forEach(node-> {
			
			if (node.getItem() == null)
				System.out.println("NULL: " + node.getHierarchy());
			
			System.out.println(node.getItem().getName() + " (" +node.getItem().getPath()+ ") ");
			
				node.getChildren().forEach(node2-> {
					if (node2.getItem() == null)
						System.out.println("NULL: " + node2.getHierarchy());
					
					System.out.println(">>>>>>>" + node2.getItem().getName() + " (" +node2.getItem().getPath()+ ") ");
					node2.getChildren().forEach(node3-> {
						
						if (node3.getItem() == null)
							System.out.println("NULL: " + node3.getHierarchy());
						
						System.out.println(">>>>>>>>>>>>" + node3.getItem().getName() + " (" +node3.getItem().getPath()+ ") ");
				});
			});
		});
		
		log.info("Disponibilizando o menuRoot para a view.");
		request.setAttribute("menuRoot", menuRoot);
		log.info("Fim da interceptação.");

		return true;
	}
	
	@Override
	public void postHandle(
	  HttpServletRequest request, 
	  HttpServletResponse response,
	  Object handler, 
	  ModelAndView modelAndView) throws Exception {
	}

}
