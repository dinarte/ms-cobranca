package br.com.eflux.payments.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class PaymentApiConsumersProvaider {

	@Autowired
	ApplicationContext context;
	
	public String[] getAll(){
		return context.getBeanNamesForType(PaymentApiConsumer.class);
	}
	
	public PaymentApiConsumer get(String beanName) {
		return (PaymentApiConsumer) context.getBean(beanName);
	}
	
}
