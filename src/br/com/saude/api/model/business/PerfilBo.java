package br.com.saude.api.model.business;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.com.saude.api.generic.GenericBo;
import br.com.saude.api.generic.Helper;
import br.com.saude.api.generic.PagedList;
import br.com.saude.api.model.creation.builder.entity.PerfilBuilder;
import br.com.saude.api.model.creation.builder.example.PerfilExampleBuilder;
import br.com.saude.api.model.entity.filter.PerfilFilter;
import br.com.saude.api.model.entity.po.Perfil;
import br.com.saude.api.model.entity.po.Permissao;
import br.com.saude.api.model.persistence.PerfilDao;
import br.com.saude.api.util.constant.Funcionalidade;

public class PerfilBo extends GenericBo<Perfil, PerfilFilter, PerfilDao, 
										PerfilBuilder, PerfilExampleBuilder>{
	
	private static PerfilBo instance;
	
	private PerfilBo() {
		super();
	}
	
	@Override
	protected void initializeFunctions() {
		this.functionLoadAll = builder -> {
			try {
				return builder.loadPermissoes();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		};
	}
	
	public static PerfilBo getInstance() {
		if(instance == null)
			instance = new PerfilBo();
		return instance;
	}
	
	@Override
	public Perfil getById(Object id) throws Exception {
		Perfil perfil = new Perfil();
		
		if((long)id > 0)
			perfil = this.getById(id, this.functionLoadAll);
		else
			perfil.setPermissoes(new ArrayList<Permissao>());
		
		Map<Object, Object> funcionalidades = Funcionalidade.getInstance().getList().entrySet().
				stream().collect(Collectors.toMap(e->e.getKey(),e->e.getValue()));
		
		perfil.getPermissoes().forEach(p -> {
			funcionalidades.remove(p.getFuncionalidade());
		});
		
		for ( Map.Entry<Object,Object> funcionalidade : funcionalidades.entrySet() ) {
			Permissao permissao = new Permissao();
			permissao.setFuncionalidade((String)funcionalidade.getKey());
			permissao.setValor(false);
			perfil.getPermissoes().add(permissao);
		}
		
		Helper.simpleSort(perfil.getPermissoes());
		
		return perfil;
	}
	
	@Override
	public Perfil save(Perfil perfil) throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, Exception {
		
		if(perfil.getPermissoes() != null) {
			List<Permissao> permissoes = perfil.getPermissoes().stream()
					.filter(p -> p.isValor() ).collect(Collectors.toList());
			perfil.setPermissoes(permissoes);
			perfil.getPermissoes().forEach(p-> p.setPerfil(perfil));
		}
		
		return super.save(perfil);
	}
	
	@Override
	public PagedList<Perfil> getList(PerfilFilter filter) throws Exception {
		return super.getOrderedList(filter,"titulo");
	}
}
