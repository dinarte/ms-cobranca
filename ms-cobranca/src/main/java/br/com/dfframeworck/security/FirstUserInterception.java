package br.com.dfframeworck.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.HandlerInterceptor;

import br.com.dfframeworck.repository.UserRepository;
import br.com.dfframeworckservice.StartUpService;


@Component
@RequestScope
public class FirstUserInterception implements HandlerInterceptor {
	
	@Autowired
	ApplicationContext context; 
	
	@Override
	public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception{

		UserRepository usuRepo = context.getBean(UserRepository.class);
		if (usuRepo.count() == 0) {
			StartUpService startUp = context.getBean(StartUpService.class);
			startUp.startupSistema();
		}
		return true;
	}

}
