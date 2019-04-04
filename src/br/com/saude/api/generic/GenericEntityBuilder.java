package br.com.saude.api.generic;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class GenericEntityBuilder<T,F extends GenericFilter> {
	protected T entity;
	protected T newEntity;
	protected List<T> entityList;
	protected List<T> newEntityList;
	
	protected GenericEntityBuilder(T entity) {
		this.entity = entity;
		this.newEntity = clone(entity);
	}
	
	protected GenericEntityBuilder(List<T> entityList) {
		this.entityList = entityList;
		this.newEntityList = clone(entityList);
	}
	
	public T getEntity(){
		return this.newEntity;
	}
	
	public List<T> getEntityList(){
		return this.newEntityList;
	}
	
	protected abstract T clone(T entity);
	
	private List<T> clone(List<T> entityList){
		List<T> list = new ArrayList<T>();
		
		for(T entity : entityList)
			list.add(clone(entity));
		
		return list;
	}
	
	private Field checkLoadProperty(String property, T entity) throws Exception {
		Field field = entity.getClass().getDeclaredField(property);
		field.setAccessible(true);
		
		if(field.get(entity) != null)
			return field;
		
		return null;
	}
	
	protected GenericEntityBuilder<T,F> loadProperty(String property, Function<T,Object> function) throws Exception {
		
		Field fi = null;
		
		if(this.entity != null) {
			fi = this.checkLoadProperty(property,this.entity);
			
			if(fi != null)
				fi.set(this.newEntity, function.apply(this.entity));
		}else {
			for(T entity:this.entityList) {
				T newEntity = this.newEntityList.stream()
						.filter(e->{
							try {
								Field field = e.getClass().getDeclaredField("id");
								field.setAccessible(true);
								return field.get(e).equals(field.get(entity));
							} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException
									| SecurityException e1) {
								e1.printStackTrace();
							}
							return false;
						})
						.iterator().next();
				
				fi = this.checkLoadProperty(property,entity);
				
				if(fi != null)
					fi.set(newEntity, function.apply(entity));
			}
		}
		
		return this;
	}
}
