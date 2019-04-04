package br.com.saude.api.util.constant;

import br.com.saude.api.generic.GenericConstant;

public class TypeFilter extends GenericConstant {
	private static TypeFilter instance;
	
	private TypeFilter() {
		
	}
	
	public static TypeFilter getInstance() {
		if(instance==null)
			instance = new TypeFilter();
		return instance;
	}
	
	public static final String MENOR			= "MENOR";
	public static final String MENOR_IGUAL		= "MENOR OU IGUAL";
	public static final String MAIOR			= "MAIOR";
	public static final String MAIOR_IGUAL		= "MAIOR OU IGUAL";
	public static final String IGUAL			= "IGUAL";
	public static final String DIFERENTE		= "DIFERENTE";
	public static final String ENTRE			= "ENTRE";
}
