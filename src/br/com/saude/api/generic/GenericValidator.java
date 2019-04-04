package br.com.saude.api.generic;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

public abstract class GenericValidator<T> {
	
	public void validate(T entity) throws Exception{
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Set<ConstraintViolation<T>> violation = factory.getValidator().validate(entity);
		if(violation.size() > 0)
			throw new Exception(violation.iterator().next().getMessage());
	}
	
	public void validate(List<T> entityList) throws Exception {
		if(entityList != null)
			for(T entity : entityList)
				validate(entity);
	}
	
	@SuppressWarnings("unchecked")
	public Class<T> getEntityClass(){
		return (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}
}
