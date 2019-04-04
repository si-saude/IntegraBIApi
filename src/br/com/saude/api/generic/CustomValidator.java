package br.com.saude.api.generic;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CustomValidator {
	Class<?> validatorClass();
}
