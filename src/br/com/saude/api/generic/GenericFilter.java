package br.com.saude.api.generic;

public class GenericFilter {
	protected long id;
	protected int pageNumber = 1;
	protected int pageSize;
	protected OrderFilter order;
	protected boolean idNotEq; 
	
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public OrderFilter getOrder() {
		return order;
	}
	public void setOrder(OrderFilter order) {
		this.order = order;
	}
	public boolean isIdNotEq() {
		return idNotEq;
	}
	public void setIdNotEq(boolean idNotEq) {
		this.idNotEq = idNotEq;
	}
}
