package br.com.saude.api.model.creation.builder.example;

import java.util.List;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;

import br.com.saude.api.generic.GenericExampleBuilder;
import br.com.saude.api.model.entity.filter.UsuarioFilter;
import br.com.saude.api.model.entity.po.Usuario;

public class UsuarioExampleBuilder extends GenericExampleBuilder<Usuario,UsuarioFilter> {

	public static UsuarioExampleBuilder newInstance(UsuarioFilter filter) {
		return new UsuarioExampleBuilder(filter);
	}
	
	private UsuarioExampleBuilder(UsuarioFilter filter) {
		super(filter);
	}
	
	private void addChave() {
		this.ilike("chave", this.filter.getChave());
	}
	
	private void addChaveEx() {
		this.ilikeEx("chave", this.filter.getChave());
	}
	
	private void addPasswordEq() {
		if(this.filter.getPassword()!= null)
			this.entity.setPassword(this.filter.getPassword());
	}
	
	protected void addGestoCss() {
		this.entity.setGestorCss(this.addBoolean("gestorCss", this.filter.getGestorCss()));
	}

	public List<Criterion> getExampleAutenticacao() throws InstantiationException, IllegalAccessException {
		if(this.filter != null) {
			initialize();
			addChaveEx();
			addPasswordEq();
			addGestoCss();
			this.criterions.add(this.finishExampleFunction.apply(Example.create(this.entity)));
			return this.criterions;
		}
		else
			return null;
	}
	
	public GenericExampleBuilder<Usuario, UsuarioFilter> exampleCheck() throws InstantiationException, IllegalAccessException {
		if(this.filter!=null) {
			initialize();
			createExampleCheck();
			this.criterions.add(getExample());
		}
		return this;
	}
	
	private void createExampleCheck() throws InstantiationException, IllegalAccessException {
		addChaveEx();
		addGestoCss();
	}

	@Override
	protected void createExample() throws InstantiationException, IllegalAccessException {
		addChave();
		addGestoCss();
	}
}
