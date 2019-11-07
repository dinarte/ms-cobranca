package br.com.dfframeworck.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

	
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Customizer {
	
	Class<?> value();
	
}
