package br.com.saude.api.model.persistence;

import org.hibernate.Hibernate;
import br.com.saude.api.generic.GenericDao;
import br.com.saude.api.model.entity.po.Perfil;

public class PerfilDao extends GenericDao<Perfil> {
	
	private static PerfilDao instance;
	
	private PerfilDao() {
		super();
	}
	
	@Override
	protected void initializeFunctions() {
		this.functionLoadAll = perfil -> {
			if(perfil.getPermissoes() != null)
				Hibernate.initialize(perfil.getPermissoes());
			return perfil;
		};
	}
	
	public static PerfilDao getInstance() {
		if(instance == null)
			instance = new PerfilDao();
		return instance;
	}
	
	@Override
	public Perfil getById(Object id) throws Exception {
		return super.getById(id, this.functionLoadAll);
	}
}
