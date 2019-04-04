package br.com.saude.api.model.business.validate;

import br.com.saude.api.generic.GenericValidator;
import br.com.saude.api.generic.Helper;
import br.com.saude.api.model.entity.po.Perfil;

public class PerfilValidator extends GenericValidator<Perfil>{

	@Override
	public void validate(Perfil perfil ) throws Exception {
		
		super.validate(perfil);
		
		if(Helper.isNotNull(perfil.getPermissoes()))
			new PermissaoValidator().validate(perfil.getPermissoes());
	}
}
