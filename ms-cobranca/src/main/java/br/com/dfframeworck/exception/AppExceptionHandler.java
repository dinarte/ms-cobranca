package br.com.dfframeworck.exception;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.dfframeworck.messages.AppFlashMessages;
import br.com.dfframeworck.messages.AppMessages;

@ControllerAdvice
public class AppExceptionHandler {
	
	@Autowired
	AppMessages appMessages;
	
	@Autowired
	AppFlashMessages appFlahMenssages;
	
	@ExceptionHandler(value = Exception.class)
    public String generalExceptionHandler(Exception e, Model model){
 		appMessages.getErrorList().add(" Um problema ocorreu que impede que esta operação seja processada "
				+ "" + e.getMessage());
		e.printStackTrace();
		model.addAttribute(appMessages);
        return "/error";
    }
	
	@ExceptionHandler(value = AppException.class)
    public String appErrorHandler(AppException e, Model model, HttpServletRequest request){
		appFlahMenssages.getMessages().getErrorList().add("Um problema ocorreu que impede que esta operação seja processada :( "
				+ "" + e.getMessage());
        return "redirect:"+getRefer(request.getHeader("referer"));
    }
	
	@ExceptionHandler(value = LoginException.class)
    public String loginErrorHandler(LoginException e, Model model){
		model.addAttribute("erro",e);
		appMessages.getErrorList().add(e.getMessage());
		model.addAttribute(appMessages);
        return "pages/login";
    }
	
	@ExceptionHandler(value = ValidacaoException.class)
    public String validacaoHandler(ValidacaoException e, Model model, HttpServletRequest request){
		model.addAttribute("erro",e);
		appFlahMenssages.getMessages().getErrorList().addAll(e.msgs);
        return "redirect:"+getRefer(request.getHeader("referer"));   
        }
	
	
	public String  getRefer(String refer) {
		if (refer == null)
			return "/login";
		refer = refer.substring(refer.indexOf("//")+2);
		refer = refer.substring(refer.indexOf("/"));
		return refer;
	}
}
