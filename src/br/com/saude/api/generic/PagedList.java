package br.com.saude.api.generic;

import java.util.List;

public class PagedList<T> {
	
	public class GenericPagedList {
		private int pageNumber;
		private int pageSize;
		private long total;
		private List<?> list;
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
		public long getTotal() {
			return total;
		}
		public void setTotal(long total) {
			this.total = total;
		}
		public List<?> getList() {
			return list;
		}
		public void setList(List<?> list) {
			this.list = list;
		}
	}
	
	private GenericPagedList genericPagedList;
	
	public PagedList() {
		this.genericPagedList = new GenericPagedList();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> getList() {
		return (List<T>) this.genericPagedList.list;
	}
	public void setList(List<T> list) {
		this.genericPagedList.list = list;
	}
	public int getPageNumber() {
		return this.genericPagedList.pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.genericPagedList.pageNumber = pageNumber;
	}
	public int getPageSize() {
		return this.genericPagedList.pageSize;
	}
	public void setPageSize(int pageSize) {
		this.genericPagedList.pageSize = pageSize;
	}
	public long getTotal() {
		return this.genericPagedList.total;
	}
	public void setTotal(long total) {
		this.genericPagedList.total = total;
	}

	public GenericPagedList getGenericPagedList() {
		return genericPagedList;
	}
}
