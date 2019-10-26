package br.com.dfframeworck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MsCobrancaApp {

    public static void main(String[] args) {
        SpringApplication.run(MsCobrancaApp.class, args);
    }
/*
    @Bean	
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
	
            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

        };
    }
*/
}