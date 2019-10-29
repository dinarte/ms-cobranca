package br.com.dfframeworck.autocrud.interceptions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import br.com.dfframeworck.controller.AutoCrudLookUpFinder;

@Component
public class AutoCrudInterceptions implements HandlerInterceptor {
	
	@Autowired
	AutoCrudLookUpFinder lookApFinder;
	
	
	@Override
	public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception{

		//HandlerMethod hMMethod = (HandlerMethod) handler;
		return true; //hMMethod.getBean().getClass().equals(AutoCrudController.class);
			
	}

	@Override
	public void postHandle(
	  HttpServletRequest request, 
	  HttpServletResponse response,
	  Object handler, 
	  ModelAndView modelAndView) throws Exception {
		request.setAttribute("lookApFinder", lookApFinder);
	}

}
