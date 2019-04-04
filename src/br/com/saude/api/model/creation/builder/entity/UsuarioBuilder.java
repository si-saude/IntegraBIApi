package br.com.saude.api.model.creation.builder.entity;

import java.util.List;

import br.com.saude.api.generic.GenericEntityBuilder;
import br.com.saude.api.model.entity.filter.UsuarioFilter;
import br.com.saude.api.model.entity.po.Usuario;

public class UsuarioBuilder extends GenericEntityBuilder<Usuario,UsuarioFilter> {

	public static UsuarioBuilder newInstance(Usuario usuario) {
		return new UsuarioBuilder(usuario);
	}
	
	public static UsuarioBuilder newInstance(List<Usuario> usuarios) {
		return new UsuarioBuilder(usuarios);
	}
	
	private UsuarioBuilder(Usuario usuario) {
		super(usuario);
	}
	
	private UsuarioBuilder(List<Usuario> usuarios) {
		super(usuarios);
	}

	@Override
	protected Usuario clone(Usuario usuario) {
		Usuario newUsuario = new Usuario();
		
		newUsuario.setId(usuario.getId());
		newUsuario.setChave(usuario.getChave());
		newUsuario.setGestorCss(usuario.isGestorCss());
		newUsuario.setVersion(usuario.getVersion());
		
		return newUsuario;
	}
	
	public UsuarioBuilder loadPerfis() throws Exception {
		return (UsuarioBuilder) this.loadProperty("perfis", usuario -> {
			try {
				return PerfilBuilder
						.newInstance(usuario.getPerfis())
						.loadPermissoes()
						.getEntityList();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		});
	}

	public Usuario cloneFromFilter(UsuarioFilter filter) {
		Usuario usuario = new Usuario();
		usuario.setChave(filter.getChave());
		usuario.setPassword(filter.getPassword());
		return usuario;
	}
}
