package br.com.saude.api.generic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.function.Function;

public abstract class GenericBo<T, F extends GenericFilter, D extends GenericDao<T>,
							B extends GenericEntityBuilder<T,F>,
							X extends GenericExampleBuilder<T,F>> {
	
	private D dao;
	
	protected Function<B,B> functionLoad;
	protected Function<B,B> functionLoadAll;
	
	protected GenericBo() {
		this.initializeFunctions();
	}
	
	protected abstract void initializeFunctions();
	
	@SuppressWarnings("unchecked")
	public D getDao() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		if(this.dao == null)
			this.dao = (D)((Class<D>)((ParameterizedType)getClass().getGenericSuperclass())
					.getActualTypeArguments()[2])
					.getDeclaredMethod("getInstance", new Class[] {})
					.invoke(null,new Object[] {});
		return dao;
	}

	@SuppressWarnings("unchecked")
	public B getBuilder(Object entity) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		try {
			return (B)((Class<B>)((ParameterizedType)getClass().getGenericSuperclass())
					.getActualTypeArguments()[3])
					.getDeclaredMethod("newInstance", new Class[] {entity.getClass()})
					.invoke(null,new Object[] {entity});
		}catch(NoSuchMethodException ex) {
			return (B)((Class<B>)((ParameterizedType)getClass().getGenericSuperclass())
					.getActualTypeArguments()[3])
					.getDeclaredMethod("newInstance", new Class[] { List.class })
					.invoke(null,new Object[] {entity});
		}
	}

	@SuppressWarnings("unchecked")
	public X getExampleBuilder(F filter) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		return (X)((Class<X>)((ParameterizedType)getClass().getGenericSuperclass())
				.getActualTypeArguments()[4])
				.getDeclaredMethod("newInstance", new Class[] {filter.getClass()})
				.invoke(null,new Object[] {filter});
	}

	public PagedList<T> getList(F filter) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, Exception {
		return getList(filter,null);
	}
	
	public PagedList<T> getOrderedList(F filter, String property) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, Exception {
		filter.setOrder(new OrderFilter());
		filter.getOrder().setProperty(property);
		return getList(filter,null);
	}
	
	public PagedList<T> getDescOrderedList(F filter, String property) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, Exception {
		filter.setOrder(new OrderFilter());
		filter.getOrder().setProperty(property);
		filter.getOrder().setDesc(true);
		return getList(filter,null);
	}
	
	public PagedList<T> getList(GenericExampleBuilder<T, F> exampleBuilder) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, Exception{
		PagedList<T> pagedList = getDao().getList(exampleBuilder);
		return getList(pagedList,null);
	}
	
	public PagedList<T> getList(GenericExampleBuilder<T, F> exampleBuilder, Function<B,B> loadFunction) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, Exception{
		PagedList<T> pagedList = getDao().getList(exampleBuilder);
		return getList(pagedList,loadFunction);
	}
	
	protected PagedList<T> getList(F filter, Function<B,B> loadFunction) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, Exception{
		PagedList<T> pagedList = getDao().getList(getExampleBuilder(filter).example());
		return getList(pagedList,loadFunction);
	}
	
	protected PagedList<T> getList(PagedList<T> pagedList, Function<B,B> loadFunction) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		if(loadFunction != null)
			pagedList.setList(loadFunction.apply(getBuilder(pagedList.getList())).getEntityList());
		else
			pagedList.setList(getBuilder(pagedList.getList()).getEntityList());
		return pagedList;
	}
	
	public List<T> getSelectList(F filter) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, Exception{
		return getSelectList(filter,null);
	}
	
	protected List<T> getSelectList(F filter, Function<B,B> loadFunction) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, Exception{
		PagedList<T> pagedList = getDao().getList(getExampleBuilder(filter).example());
		
		if(loadFunction != null)
			return loadFunction.apply(getBuilder(pagedList.getList())).getEntityList();
		
		return getBuilder(pagedList.getList()).getEntityList();
	}
	
	protected List<T> getSelectList(List<T> list, Function<B,B> loadFunction) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		if(loadFunction != null)
			list = loadFunction.apply(getBuilder(list)).getEntityList();
		else
			list = getBuilder(list).getEntityList();
		return list;
	}
	
	public T getById(Object id) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, Exception {
		return getById(id,null);
	}
	
	protected T getById(Object id, Function<B,B> loadFunction) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, Exception {
		T entity = getDao().getById(id);
		return getByEntity(entity,loadFunction);
	}
	
	protected T getByEntity(T entity, Function<B,B> loadFunction) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		if(loadFunction!=null)
			return loadFunction.apply(getBuilder(entity)).getEntity();
		return entity != null ? getBuilder(entity).getEntity() : null;
	}
	
	public T save(T entity) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, Exception {
		return save(entity,null);
	}
	
	public List<T> saveList(List<T> entities) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, Exception {
		return saveList(entities,null);
	}
	
	protected T save(T entity, Function<B,B> loadFunction) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, Exception {
		entity = getDao().save(entity);
		if(loadFunction!=null)
			return loadFunction.apply(getBuilder(entity)).getEntity();
		return getBuilder(entity).getEntity();
	}
	
	protected List<T> saveList(List<T> entities, Function<B,B> loadFunction) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, Exception {
		entities = getDao().saveList(entities);
		if(loadFunction!=null)
			return loadFunction.apply(getBuilder(entities)).getEntityList();
		return getBuilder(entities).getEntityList();
	}
	
	public void delete(Object id) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		getDao().delete(id);
	}
}
