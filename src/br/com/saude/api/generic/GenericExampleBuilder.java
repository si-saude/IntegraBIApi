package br.com.saude.api.generic;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.javatuples.Triplet;

import java.lang.reflect.Field;

public abstract class GenericExampleBuilder<T,F extends GenericFilter> {
	protected T entity;
	protected F filter;
	protected List<Criterion> criterions;
	protected List<Triplet<String,CriteriaExample,JoinType>> criterias;
	protected Function<Example,Example> finishExampleFunction;
	protected List<String> excludingProperties;
	
	protected GenericExampleBuilder(F filter) {
		this.filter = filter;
		this.finishExampleFunction = example -> {
			
			for(String property:this.excludingProperties)
				example = example.excludeProperty(property);
			
			return example;
		};
	}
	
	public GenericExampleBuilder<T, F> example() throws InstantiationException, IllegalAccessException{
		if(this.filter!=null) {
			initialize();
			createExample();
			this.addId();
			this.criterions.add(getExample());
		}
		return this;
	}
	
	protected abstract void createExample() throws InstantiationException, IllegalAccessException;
	
	@SuppressWarnings("unchecked")
	protected void initialize() throws InstantiationException, IllegalAccessException {
		this.excludingProperties = new ArrayList<String>();
		this.criterions = new ArrayList<Criterion>();
		this.criterias = new ArrayList<Triplet<String,CriteriaExample,JoinType>>();
		this.entity = (((Class<T>) ((ParameterizedType)getClass().getGenericSuperclass())
				.getActualTypeArguments()[0])).newInstance();
	}
	
	protected Example getExample() {
		Example example = Example.create(this.entity).enableLike().excludeZeroes(); 
		return this.finishExampleFunction.apply(example);
	}

	public List<Criterion> getCriterions() {
		return criterions;
	}
	
	public List<Triplet<String,CriteriaExample,JoinType>> getCriterias() {
		return criterias;
	}
	
	public int getPageNumber() {
		return this.filter != null ?
				this.filter.getPageNumber()
				: 0;
	}
	
	public int getPageSize() {
		return this.filter != null ?
				this.filter.getPageSize()
				: 0;
	}
	
	public CriteriaExample getCriteriaExample() throws InstantiationException, IllegalAccessException {
		if(this.filter != null) {
			CriteriaExample criteriaExample = new CriteriaExample();
			initialize();
			createExample();
			this.addId();
			
			setProperties();
			
			criteriaExample.setCriterions(this.criterions);
			criteriaExample.setExample(getExample());
			criteriaExample.setEntity(this.entity);
			criteriaExample.setCriterias(this.criterias);
			return criteriaExample;
		}
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public CriteriaExample getCriteriaExample(Function function) throws InstantiationException, IllegalAccessException {
		if(this.filter != null) {
			CriteriaExample criteriaExample = new CriteriaExample();
			initialize();
			function.apply(this);
			
			setProperties();
			
			criteriaExample.setCriterions(this.criterions);
			criteriaExample.setExample(getExample());
			criteriaExample.setEntity(this.entity);
			criteriaExample.setCriterias(this.criterias);
			return criteriaExample;
		}
		return null;
	}
	
	private void setProperties() throws IllegalAccessException {
		for(Triplet<String,CriteriaExample,JoinType> criteria : this.criterias) {
			try {
				Field field = this.entity.getClass().getDeclaredField(criteria.getValue0());
				field.setAccessible(true);
				field.set(this.entity, criteria.getValue1().getEntity());
			} catch (IllegalArgumentException | NoSuchFieldException | SecurityException e) {
				e.printStackTrace();
			}
		}
	}

	public F getFilter() {
		return filter;
	}
	
	protected void addData(String propertyName, DateFilter dateFilter) {
		Criterion criterion = Helper.getCriterionDateFilter(propertyName, dateFilter);
		if(criterion!=null)
			this.criterions.add(criterion);
	}
	
	protected boolean addBoolean(String propertyName, BooleanFilter booleanFilter) {
		if(booleanFilter == null || booleanFilter.getValue() <= 0 || booleanFilter.getValue() > 2)
			this.excludingProperties.add(propertyName);
		else if(booleanFilter.getValue() == 1)
			return false;
		return true;
	}
	
	protected void ilike(String property, String value) {
		if(value != null && value.trim().length() > 0) {
			this.criterions.add(Restrictions.ilike(property, Helper.filterLike(value)));
		}
	}
	
	protected void ilikeEx(String property, String value) {
		if(value != null && value.trim().length() > 0) {
			this.criterions.add(Restrictions.ilike(property, value));
		}
	}
	
	private void addId() {
		if(this.filter.getId() > 0) {
			if(this.filter.isIdNotEq()) {
				this.criterions.add(Restrictions.ne("id", (long)this.filter.getId()));
			} else {
				this.criterions.add(Restrictions.eq("id", (long)this.filter.getId()));
			}
		}
	}
	
	protected void addGenericFilter(String property, GenericFilter filter, GenericExampleBuilder<?,?> builder) throws InstantiationException, IllegalAccessException {
		if(Helper.isNotNull(filter)) {
			this.addLeftJoinCriteria(property, builder);
		}
	}
	
	protected void addLeftJoinGenericFilter(String property, GenericFilter filter, GenericExampleBuilder<?,?> builder) throws InstantiationException, IllegalAccessException {
		if(Helper.isNotNull(filter)) {
			this.addLeftJoinCriteria(property, builder);
		}
	}
	
	private void addLeftJoinCriteria(String property, GenericExampleBuilder<?,?> builder) throws InstantiationException, IllegalAccessException {
		CriteriaExample criteriaExample = builder.getCriteriaExample();
		this.criterias.add(new Triplet<String,CriteriaExample,JoinType>(property, criteriaExample, JoinType.LEFT_OUTER_JOIN));
	}
}
