package br.com.saude.api.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.saude.api.generic.CustomValidator;
import br.com.saude.api.generic.GenericService;
import br.com.saude.api.model.business.UsuarioBo;
import br.com.saude.api.model.business.validate.UsuarioValidator;
import br.com.saude.api.model.entity.filter.UsuarioFilter;
import br.com.saude.api.model.entity.po.Usuario;
import br.com.saude.api.util.RequestInterceptor;

@Path("usuario")
public class UsuarioService extends GenericService<Usuario,UsuarioFilter,UsuarioBo> {
	
	@RequestInterceptor
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@CustomValidator(validatorClass=UsuarioValidator.class)
	@Override
	public Response save(Usuario usuario) {		
		return super.save(usuario);
	}
	
	@RequestInterceptor
	@Override
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/list")
	public Response getList(UsuarioFilter filter) {		
		return super.getList(filter);
	}

	@RequestInterceptor
	@Override
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/selectList")
	public Response getSelectList(UsuarioFilter filter) {		
		return super.getSelectList(filter);
	}

	@RequestInterceptor
	@Override
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@QueryParam("id") String id) throws Exception {
		return super.get(id);
	}

	@RequestInterceptor
	@Override
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/delete")
	public Response delete(Object id) {
		return super.delete(id);
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/autenticar")
	public Response autenticar(UsuarioFilter filter) {
		try {
			return Response.ok(getBo().autenticar(filter)).build();
		}catch (Exception e) {
			return this.returnNotAcceptable(e);
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/check")
	public Response check(@QueryParam("chave") String chave) {
		try {
			return Response.ok(getBo().check(chave)).build();
		}catch (Exception e) {
			return this.returnNotAcceptable(e);
		}
	}	
}
