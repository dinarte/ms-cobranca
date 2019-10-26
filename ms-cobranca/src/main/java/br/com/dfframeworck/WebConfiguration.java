package br.com.dfframeworck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.com.dfframeworck.autocrud.interceptions.AutoCrudInterceptions;
import br.com.dfframeworck.messages.AppMessagesIntercption;
import br.com.dfframeworck.security.DynamicMenuInterception;
import br.com.dfframeworck.security.FirstUserInterception;
import br.com.dfframeworck.security.SecurityInterception;

@EnableWebMvc
@Configuration
@EntityScan("br.com.*")  
@ComponentScan("br.com.*")
public class WebConfiguration implements WebMvcConfigurer{

	@Autowired FirstUserInterception firstUser;
	@Autowired SecurityInterception security;
	@Autowired DynamicMenuInterception dynamicMenu;
	@Autowired AppMessagesIntercption appMsg;
	@Autowired AutoCrudInterceptions autoCrud;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(firstUser);
	    registry.addInterceptor(security);	    
	    registry.addInterceptor(dynamicMenu);
	    registry.addInterceptor(autoCrud);
	    registry.addInterceptor(appMsg);
	}
	
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//registry
        //.addResourceHandler("/resources/**")
        //.addResourceLocations("/resources/");     
		}
}
