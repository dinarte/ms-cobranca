package br.com.dfframeworck.messages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class AppMessagesIntercption extends HandlerInterceptorAdapter {
	
	@Autowired
	AppMessages appMessages;

	@Autowired
	AppFlashMessages appFlashMesg;
	
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
		if (handler instanceof HandlerMethod) {
			HandlerMethod hMMethod = (HandlerMethod) handler;
			if (hMMethod.getMethod().isAnnotationPresent(SucessMsg.class)) {
				appMessages.getSuccessList().add( hMMethod.getMethod().getAnnotation(SucessMsg.class).message() );
			}
			appMessages.getErrorList().addAll(appFlashMesg.getMessages().getErrorList());
			appMessages.getSuccessList().addAll(appFlashMesg.getMessages().getSuccessList());
			appMessages.getInfoList().addAll(appFlashMesg.getMessages().getInfoList());
			appMessages.getWarningList().addAll(appFlashMesg.getMessages().getWarningList());
			
			appFlashMesg.getMessages().getErrorList().clear();
			appFlashMesg.getMessages().getSuccessList().clear();
			appFlashMesg.getMessages().getInfoList().clear();
			appFlashMesg.getMessages().getWarningList().clear();
			
			request.setAttribute("appMessages", appMessages);
		}
	}

}
