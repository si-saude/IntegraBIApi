package br.com.saude.api.model.entity.filter;

import br.com.saude.api.generic.BooleanFilter;
import br.com.saude.api.generic.GenericFilter;

public class UsuarioFilter extends GenericFilter {
	
	private String chave;
	private String password;
	private BooleanFilter gestorCss;
	
	public String getChave() {
		return chave;
	}
	public void setChave(String chave) {
		this.chave = chave;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public BooleanFilter getGestorCss() {
		return gestorCss;
	}
	public void setGestorCss(BooleanFilter gestorCss) {
		this.gestorCss = gestorCss;
	}
	
}
