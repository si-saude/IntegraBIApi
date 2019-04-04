package br.com.saude.api.model.persistence;

import org.hibernate.Hibernate;

import br.com.saude.api.generic.GenericDao;
import br.com.saude.api.model.entity.po.Usuario;

public class UsuarioDao extends GenericDao<Usuario> {
	
	private static UsuarioDao instance;
	
	private UsuarioDao() {
		super();
	}
	
	@Override
	protected void initializeFunctions() {
		this.functionLoadAll = usuario -> {
			
			if(usuario.getPerfis() != null) {
				Hibernate.initialize(usuario.getPerfis());
				
				usuario.getPerfis().forEach(p -> {
					if (p.getPermissoes() != null) 
						Hibernate.initialize(p.getPermissoes());
				});
			}
			
			return usuario;
		};
	}
	
	public static UsuarioDao getInstance() {
		if(instance == null)
			instance = new UsuarioDao();
		return instance;
	}
	
	@Override
	public Usuario getById(Object id) throws Exception {
		return super.getById(id, this.functionLoadAll);
	}
}
