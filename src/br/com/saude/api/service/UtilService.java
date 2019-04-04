package br.com.saude.api.service;

import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.saude.api.generic.GenericConstant;
import br.com.saude.api.util.RequestInterceptor;
import br.com.saude.api.util.constant.TypeFilter;

@Path("generic")
@RequestInterceptor
public class UtilService {

	private Map<String,String> getMap(GenericConstant constant, String filter) throws IllegalArgumentException, IllegalAccessException{
		return constant.getList().entrySet().stream() 
		.filter(f-> filter!=null?f.getValue().toLowerCase().contains(filter.toLowerCase()):true)
		.collect(Collectors.toMap(e->e.getKey(),e->e.getValue()));
	}
	
	private Object[] getValues(GenericConstant constant, String filter) throws Exception {
		Map<String, String> map = getMap(constant,filter);
		return map.values().toArray();
	}
	
	@GET
	@Path("/type-filter")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTypeFilter(@QueryParam("filter") String filter) throws Exception {
		Object[] values = this.getValues(TypeFilter.getInstance(), filter);
		return Response.ok(values).build();
	}
}
