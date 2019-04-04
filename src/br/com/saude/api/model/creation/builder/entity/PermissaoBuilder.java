package br.com.saude.api.model.creation.builder.entity;

import java.util.List;

import br.com.saude.api.generic.GenericEntityBuilder;
import br.com.saude.api.generic.GenericFilter;
import br.com.saude.api.model.entity.po.Permissao;

public class PermissaoBuilder extends GenericEntityBuilder<Permissao,GenericFilter> {

	public static PermissaoBuilder newInstance(Permissao permissao) {
		return new PermissaoBuilder(permissao);
	}
	
	public static PermissaoBuilder newInstance(List<Permissao> permissoes) {
		return new PermissaoBuilder(permissoes);
	}
	
	private PermissaoBuilder(Permissao permissao) {
		super(permissao);
	}
	
	private PermissaoBuilder(List<Permissao> permissoes) {
		super(permissoes);
	}

	@Override
	protected Permissao clone(Permissao permissao) {
		Permissao newPermissao = new Permissao();
		
		newPermissao.setId(permissao.getId());
		newPermissao.setFuncionalidade(permissao.getFuncionalidade());
		newPermissao.setValor(permissao.isValor());
		newPermissao.setVersion(permissao.getVersion());
		
		return newPermissao;
	}
}
