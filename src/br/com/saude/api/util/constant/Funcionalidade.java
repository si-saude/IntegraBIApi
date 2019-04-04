package br.com.saude.api.util.constant;

import br.com.saude.api.generic.GenericConstant;

public class Funcionalidade extends GenericConstant {
	
	private static Funcionalidade instance;
	
	private Funcionalidade() {
		
	}
	
	public static Funcionalidade getInstance() {
		if(instance==null)
			instance = new Funcionalidade();
		return instance;
	}
	
	public static final String PERFIL_ADICIONAR 			    	= "PERFIL_ADICIONAR";
	public static final String PERFIL_EDITAR		 				= "PERFIL_EDITAR";
	public static final String PERFIL_DETALHAR 						= "PERFIL_DETALHAR";
	public static final String PERFIL_LISTAR						= "PERFIL_LISTAR";
	public static final String PERFIL_REMOVER 						= "PERFIL_REMOVER";
	
	public static final String USUARIO_ADICIONAR 			    	= "USUARIO_ADICIONAR";
	public static final String USUARIO_EDITAR		 				= "USUARIO_EDITAR";
	public static final String USUARIO_DETALHAR 					= "USUARIO_DETALHAR";
	public static final String USUARIO_LISTAR						= "USUARIO_LISTAR";
	public static final String USUARIO_REMOVER 						= "USUARIO_REMOVER";
}
