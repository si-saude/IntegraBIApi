package br.com.saude.api.model.creation.builder.entity;

import java.util.List;

import br.com.saude.api.generic.GenericEntityBuilder;
import br.com.saude.api.model.entity.filter.PerfilFilter;
import br.com.saude.api.model.entity.po.Perfil;

public class PerfilBuilder extends GenericEntityBuilder<Perfil,PerfilFilter> {
	
	public static PerfilBuilder newInstance(Perfil perfil) {
		return new PerfilBuilder(perfil);
	}
	
	public static PerfilBuilder newInstance(List<Perfil> perfis) {
		return new PerfilBuilder(perfis);
	}
	
	private PerfilBuilder(Perfil perfil) {
		super(perfil);
	}

	private PerfilBuilder(List<Perfil> perfis) {
		super(perfis);
	}

	@Override
	protected Perfil clone(Perfil perfil) {
		Perfil newPerfil = new Perfil();
		
		newPerfil.setId(perfil.getId());
		newPerfil.setTitulo(perfil.getTitulo());
		newPerfil.setVersion(perfil.getVersion());
		
		return newPerfil;
	}
	
	public PerfilBuilder loadPermissoes() throws Exception {
		return (PerfilBuilder) this.loadProperty("permissoes", perfil -> {
			return PermissaoBuilder.newInstance(perfil.getPermissoes()).getEntityList();
		});
	}
}
