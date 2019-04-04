package br.com.saude.api.util;

import java.io.IOException;
import org.glassfish.jersey.server.ContainerRequest;

import br.com.saude.api.generic.CustomValidator;
import br.com.saude.api.generic.GenericValidator;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@RequestInterceptor
@Provider
public class RequestFilterInterceptor implements ContainerRequestFilter {

	@Context
    private ResourceInfo resourceInfo;
	
	private static final String AUTHENTICATION_SCHEME = "Bearer";
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String authorizationHeader =
                requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (!isTokenBasedAuthentication(authorizationHeader)) {
            abortWithUnauthorized(requestContext);
            return;
        }
        
        String token = authorizationHeader
                            .substring(AUTHENTICATION_SCHEME.length()).trim();

        try {
            validateToken(token);
        } catch (Exception e) {
            abortWithUnauthorized(requestContext);
            return;
        }
        
        try {
            validateArgument((ContainerRequest) requestContext);
        } catch (Exception e) {
        	abortWithInvalidArgument(requestContext,e.getMessage());
            return;
        }
	}
	
	private boolean isTokenBasedAuthentication(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.toLowerCase()
                    .startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }
	
	private void validateToken(String token) throws Exception {
		if(!UserManager.getInstance().isTokenValid(token))
			throw new Exception();
    }
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void validateArgument(ContainerRequest containerRequest) throws Exception {
		CustomValidator customValidator = this.resourceInfo.getResourceMethod()
															.getAnnotation(CustomValidator.class);
		if(customValidator != null) {
			containerRequest.bufferEntity();			
			GenericValidator validator = (GenericValidator)customValidator.validatorClass().newInstance();
			Object entity = containerRequest.readEntity(validator.getEntityClass());
			validator.validate(entity);
		}
	}
	
	private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_SCHEME)
                        .build());
    }
	
	private void abortWithInvalidArgument(ContainerRequestContext requestContext, String message) {
        requestContext.abortWith(
                Response.status(Response.Status.BAD_REQUEST)
                		.entity(message)
                        .header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_SCHEME)
                        .build());
    }
}
