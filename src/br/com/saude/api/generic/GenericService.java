package br.com.saude.api.generic;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;

import javax.ws.rs.core.Response;

public abstract class GenericService <	T, 
					F extends GenericFilter,
					BO extends GenericBo<T, F, ?, ?, ?>>{
	
	private BO bo;
	
	@SuppressWarnings("unchecked")
	protected BO getBo() throws Exception {
		if(this.bo == null)
			this.bo = (BO)((Class<BO>)((ParameterizedType)getClass().getGenericSuperclass())
				.getActualTypeArguments()[2])
				.getDeclaredMethod("getInstance", new Class[] {})
				.invoke(null,new Object[] {});
			
		return this.bo;
	}
	
	public Response getList(F filter) {
		try {
			return Response.ok(getBo().getList(filter).getGenericPagedList()).build();
		} catch (Exception e) {
			return returnNotAcceptable(e);
		}
	}
	
	public Response getSelectList(F filter) {
		try {
			return Response.ok(getBo().getSelectList(filter)).build();
		} catch (Exception e) {
			return returnNotAcceptable(e);
		}
	}
	
	public Response get(String id) throws Exception {
		try {
			return Response.ok(getBo().getById(new Long(id))).build();
		}catch (Exception e) {
			return returnNotAcceptable(e);
		}
	}
	
	public Response delete(Object id) {
		try {
			getBo().delete(new Long(Objects.toString(id)));
			return Response.ok("Removido com sucesso.").build();
		} catch (Exception e) {
			return returnNotAcceptable(e);
		}
	}
	
	public Response save(T entity) {
		try {
			getBo().save(entity);
			return Response.ok("Salvo com sucesso.").build();
		} catch (Exception e) {
			return returnNotAcceptable(e);
		}
	}
	
	protected Response returnNotAcceptable(Exception e) {
		return Response.status(Response.Status.NOT_ACCEPTABLE).entity(e.getMessage()).build();
	}
}
