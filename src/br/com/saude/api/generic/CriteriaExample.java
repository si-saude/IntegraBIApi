package br.com.saude.api.generic;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.sql.JoinType;
import org.javatuples.Triplet;

public class CriteriaExample {
	private Example example;
	private List<Criterion> criterions;
	private List<Triplet<String,CriteriaExample,JoinType>> criterias;
	private Object entity;
	
	public Example getExample() {
		return example;
	}
	public void setExample(Example example) {
		this.example = example;
	}
	public List<Criterion> getCriterions() {
		return criterions;
	}
	public void setCriterions(List<Criterion> criterions) {
		this.criterions = criterions;
	}
	public Object getEntity() {
		return entity;
	}
	public void setEntity(Object entity) {
		this.entity = entity;
	}
	public List<Triplet<String, CriteriaExample, JoinType>> getCriterias() {
		return criterias;
	}
	public void setCriterias(List<Triplet<String, CriteriaExample, JoinType>> criterias) {
		this.criterias = criterias;
	}
}
