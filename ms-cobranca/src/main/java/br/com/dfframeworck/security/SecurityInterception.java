package br.com.dfframeworck.security;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import br.com.dfframeworck.repository.UserRepository;


@Component
@RequestScope
public class SecurityInterception implements HandlerInterceptor {

	
	@Autowired
	Autenticacao autenticacao;
	
	@Value("${dfframeworck.development.auto_login}")
	String autoLogin;
	
	@Autowired
	UserRepository uRepo;

	
	@Override
	public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception{

		if (handler instanceof HandlerMethod) {
			HandlerMethod hMMethod = (HandlerMethod) handler;
			Functionality functionality = hMMethod.getMethodAnnotation(Functionality.class);
			if (Objects.nonNull(functionality) && !functionality.isPublic()) {
				
				
				 if (! autenticacao.isAutenticado() && Objects.nonNull(autoLogin)){ 
					 autenticacao.setUsuario(uRepo.findOneByEmail(autoLogin)); 
					 autenticacao.processarAutenticacao(); 
				 }
				
				autenticacao.checkAthorization();
			}
		}
		return true;
			
	}

	@Override
	public void postHandle(
	  HttpServletRequest request, 
	  HttpServletResponse response,
	  Object handler, 
	  ModelAndView modelAndView) throws Exception {
		if (autenticacao != null && modelAndView != null)
			modelAndView.addObject(autenticacao);
	}
}
