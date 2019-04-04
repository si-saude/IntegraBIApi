package br.com.saude.api.util;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.KeyGenerator;

import br.com.saude.api.generic.Helper;
import br.com.saude.api.model.business.UsuarioBo;
import br.com.saude.api.model.entity.po.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class UserManager {
	
	private static UserManager instance;
	
	private List<Usuario> usuarios;
	
	private UserManager() {
		this.usuarios = new ArrayList<Usuario>();
	}
	
	public static UserManager getInstance() {
		if(instance == null)
			instance = new UserManager();
		return instance;
	}
	
	public boolean isTokenValid(String token) {
		
		boolean ret = this.usuarios.stream().filter(u->u.getToken().equals(token) && 
				u.getSessionTime() > (Helper.getNow() - (1000*60*90)) ).count() > 0;
				
		if(ret) {
			this.usuarios.stream().filter(u->u.getToken().equals(token)).findFirst()
				.get().setSessionTime(Helper.getNow());
		}
		
		return ret;
	}
	
	public Usuario authenticate(Usuario usuario) throws Exception {
		String token = Jwts.builder()
			.setSubject(usuario.getChave())
			.signWith(SignatureAlgorithm.HS256, KeyGenerator.getInstance("HmacSHA256").generateKey())
			.compact();
		
		Usuario user = UsuarioBo.getInstance().getById(usuario.getId());
		if (user != null)
			usuario.setPerfis(user.getPerfis());
		
		usuario.setPassword(null);
		usuario.setToken(token);
		this.usuarios.removeIf(u->u.getId() == usuario.getId());
		usuario.setSessionTime(Helper.getNow());
		this.usuarios.add(usuario);
		return usuario;
	}
}
