package br.com.saude.api.model.creation.builder.example;

import br.com.saude.api.generic.GenericExampleBuilder;
import br.com.saude.api.model.entity.filter.PerfilFilter;
import br.com.saude.api.model.entity.po.Perfil;

public class PerfilExampleBuilder extends GenericExampleBuilder<Perfil,PerfilFilter> {

	public static PerfilExampleBuilder newInstance(PerfilFilter filter) {
		return new PerfilExampleBuilder(filter);
	}
	
	private PerfilExampleBuilder(PerfilFilter filter) {
		super(filter);
	}

	private void addTitulo() {
		this.ilike("titulo", this.filter.getTitulo());
	}
	

	@Override
	protected void createExample() {
		addTitulo();
	}
}
