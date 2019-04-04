package br.com.saude.api.model.business;

import br.com.saude.api.generic.GenericBo;
import br.com.saude.api.model.business.validate.UsuarioValidator;
import br.com.saude.api.model.creation.builder.entity.UsuarioBuilder;
import br.com.saude.api.model.creation.builder.example.UsuarioExampleBuilder;
import br.com.saude.api.model.entity.filter.UsuarioFilter;
import br.com.saude.api.model.entity.po.Usuario;
import br.com.saude.api.model.persistence.UsuarioDao;
import br.com.saude.api.util.UserManager;

public class UsuarioBo extends GenericBo<Usuario, UsuarioFilter, UsuarioDao, UsuarioBuilder, 
											UsuarioExampleBuilder> {

	private static UsuarioBo instance;
	
	private UsuarioBo() {
		super();
	}
	
	@Override
	protected void initializeFunctions() {
		this.functionLoadAll = builder -> {
			try {
				return builder.loadPerfis();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		};
	}
	
	public static UsuarioBo getInstance() {
		if(instance == null)
			instance = new UsuarioBo();
		return instance;
	}
	
	@Override
	public Usuario getById(Object id) throws Exception {
		return getById(id, this.functionLoadAll);
	}

	private Usuario getByChaveSenha(UsuarioFilter filter) throws Exception {
		Usuario usuario = getBuilder(new Usuario()).cloneFromFilter(filter);
		UsuarioValidator usuarioValidator = new UsuarioValidator();
		usuarioValidator.validate(usuario);
		usuario = getDao().getFirst(getExampleBuilder(filter).getExampleAutenticacao());
		return usuario != null ? getBuilder(usuario).getEntity() : null;
	}
	
	public Usuario autenticar(UsuarioFilter filter) throws Exception {
		Usuario usuario = this.getByChaveSenha(filter);
		
		if(usuario != null) {
			usuario = UserManager.getInstance().authenticate(usuario);
		} else {
			throw new Exception("Senha inválida.");
		}
		
		return usuario;
	}
	
	public boolean check(String chave) throws Exception {
		UsuarioFilter filter = new UsuarioFilter();
		filter.setChave(chave);
		return this.getList(getExampleBuilder(filter).exampleCheck()).getTotal() > 0;
	}
}
