package br.com.saude.api.generic;

public class DateFilter {
	private long inicio;
	private long fim;
	private String typeFilter;
	
	public long getInicio() {
		return inicio;
	}
	public void setInicio(long inicio) {
		this.inicio = inicio;
	}
	public long getFim() {
		return fim;
	}
	public void setFim(long fim) {
		this.fim = fim;
	}
	public String getTypeFilter() {
		return typeFilter;
	}
	public void setTypeFilter(String typeFilter) {
		this.typeFilter = typeFilter;
	}
}
