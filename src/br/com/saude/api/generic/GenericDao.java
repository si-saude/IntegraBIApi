package br.com.saude.api.generic;

import java.util.List;
import java.util.function.Function;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.sql.JoinType;
import org.javatuples.Pair;
import org.javatuples.Triplet;


public abstract class GenericDao<T> {
	
	protected Class<T> entityType;
	protected Function<T,T> functionLoad;
	protected Function<T,T> functionLoadAll;
	protected Function<Pair<T,Session>,T> functionBeforeSave;
	protected Function<Pair<T,Session>,T> functionAfterSave;
	
	@SuppressWarnings("unchecked")
	protected GenericDao(){
		this.entityType = (Class<T>) ((ParameterizedType)getClass().getGenericSuperclass())
			.getActualTypeArguments()[0];
		this.initializeFunctions();
	}
	
	protected abstract void initializeFunctions();
	
	private Field getId(Class<?> entityClass) {
		try {
			return entityClass.getDeclaredField("id");
		} catch (NoSuchFieldException e) {
			return getId(entityClass.getSuperclass());
		}
	}
	
	private Field getVersion(Class<?> entityClass) {
		try {
			return entityClass.getDeclaredField("version");
		} catch (NoSuchFieldException e) {
			return getVersion(entityClass.getSuperclass());
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public T save(T entity) throws Exception {
		Session session = HibernateHelper.getSession();
		
		try {
			Transaction transaction = session.beginTransaction();
			
			if(this.functionBeforeSave != null)
				entity = this.functionBeforeSave.apply(new Pair<T,Session>(entity,session));
			
			T entityMerged = (T) session.merge(entity);
			
			Field id = getId(entity.getClass());
			id.setAccessible(true);
			
			if( new Long(id.get(entityMerged).toString()) > 0 ) {
				id.set(entity, id.get(entityMerged));
				
				Field version = getVersion(entity.getClass());
				if(version != null) {
					version.setAccessible(true);
					version.set(entity, version.get(entityMerged));				
				}
			}
			
			if(this.functionAfterSave != null)
				entity = this.functionAfterSave.apply(new Pair<T,Session>(entity,session));
			
			transaction.commit();
		}catch(Exception ex) {
			throw ex;
		}finally {
			HibernateHelper.close(session);
		}
		
		return entity;
	}
	
	public List<T> saveList(List<T> entities) throws Exception {
		Session session = HibernateHelper.getSession();
		
		try {
			Transaction transaction = session.beginTransaction();
			
			for (T t : entities) {
				
				Field id = getId(t.getClass());
				id.setAccessible(true);
				id.set(t, id.get(session.merge(t)));
			}
			transaction.commit();
		}catch(Exception ex) {
			throw ex;
		}finally {
			HibernateHelper.close(session);
		}
		
		return entities;
	}
	
	protected long getCount(Session session, GenericExampleBuilder<?,?> exampleBuilder) {
		@SuppressWarnings("deprecation")
		Criteria criteria = session.createCriteria(entityType);
		
		for(Criterion criterion : exampleBuilder.getCriterions())
			criteria.add(criterion);
		
		if(exampleBuilder.getCriterias() != null)
			for(Triplet<String,CriteriaExample,JoinType> c: exampleBuilder.getCriterias()) {
				Criteria example = criteria.createCriteria(c.getValue0(),c.getValue2());
				for(Criterion criterion : c.getValue1().getCriterions())
					example.add(criterion);
				example.add(c.getValue1().getExample());
			}
		
		criteria = finishCriteria(criteria,exampleBuilder);
		
		return (long)criteria.setProjection(Projections.rowCount()).uniqueResult();
	}
	
	public void delete(Object id) {
		Session session = HibernateHelper.getSession();
		
		try {
			Transaction transaction = session.beginTransaction();
			session.remove(session.get(this.entityType, (Serializable)id));
			transaction.commit();
		}catch(Exception ex) {
			throw ex;
		}finally {
			HibernateHelper.close(session);
		}
	}
	
	public PagedList<T> getList(GenericExampleBuilder<?,?> exampleBuilder) throws Exception{
		return getList(exampleBuilder, null);
	}
	
	protected Criteria finishCriteria(Criteria criteria, GenericExampleBuilder<?,?> exampleBuilder) {
		return criteria;
	}
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	protected PagedList<T> getList(GenericExampleBuilder<?,?> exampleBuilder, Function<T,T> function) throws Exception {
		List<T> list;
		PagedList<T> pagedList = new PagedList<T>();
		List<Criterion> criterions = exampleBuilder.getCriterions();
		Session session = HibernateHelper.getSession();
		
		try {
			Criteria criteria = session.createCriteria(this.entityType,"main");
			criteria.setFirstResult((exampleBuilder.getPageNumber() - 1) * exampleBuilder.getPageSize());
			criteria.setMaxResults(exampleBuilder.getPageSize());
			
			if(criterions != null)
				for(Criterion criterion : criterions)
					criteria.add(criterion);
			
			criteria = Helper.loopCriterias(criteria, exampleBuilder.getCriterias(),null);			
			criteria = finishCriteria(criteria,exampleBuilder);	
			
			if(exampleBuilder.getFilter().getOrder() != null) {
				Order order = exampleBuilder.getFilter().getOrder().isDesc() ?
							Order.desc(exampleBuilder.getFilter().getOrder().getProperty()):
							Order.asc(exampleBuilder.getFilter().getOrder().getProperty());
				criteria.addOrder(order);
			}
			
			list = criteria.list();
			
			if(function != null)
				for(T entity : list)
					entity = function.apply(entity); 			
			
			pagedList.setTotal(getCount(session, exampleBuilder));
			pagedList.setList(list);
			pagedList.setPageNumber(exampleBuilder.getPageNumber());
			pagedList.setPageSize(exampleBuilder.getPageSize());
			
		}catch (Exception ex) {
			throw ex;
		}
		finally {
			HibernateHelper.close(session);
		}
		
		return pagedList;
	}
	
	public T getFirst(List<Criterion> criterions) throws Exception{
		return getFirst(criterions, null);
	}
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	protected T getFirst(List<Criterion> criterions, Function<T,T> function) throws Exception {
		T entity;
		Session session = HibernateHelper.getSession();
		
		try {
			Criteria criteria = session.createCriteria(this.entityType);
			
			if(criterions != null)
				for(Criterion criterion : criterions)
					criteria.add(criterion);					
			
			criteria = criteria.setMaxResults(1);
			entity = (T)criteria.uniqueResult();
			
			if(function != null)
				entity = function.apply(entity);	 			
			
		}catch (Exception ex) {
			throw ex;
		}
		finally {
			HibernateHelper.close(session);
		}
		
		return entity;
	}
	
	public T getById(Object id) throws Exception {
		return getById(id, null);
	}
	
	protected T getById(Object id, Function<T,T> function) throws Exception {
		Session session = HibernateHelper.getSession();
		T entity;
		
		try {
			entity = session.get(this.entityType, (Serializable)id);
			
			if(function != null)
				entity = function.apply(entity);
		}catch (Exception ex) {
			throw ex;
		}
		finally {
			HibernateHelper.close(session);
		}
		return entity;
	}
}
