package br.com.saude.api.model.entity.po;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Permissao implements Comparable<Permissao> {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Size(max = 100)
	private String funcionalidade;
	
	private boolean valor;
	
	@NotNull(message="É necessário informar o Perfil da Permissão.")
	@ManyToOne(fetch=FetchType.LAZY)
	private Perfil perfil;
	
	@Version
	private long version;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public boolean isValor() {
		return valor;
	}
	public void setValor(boolean valor) {
		this.valor = valor;
	}
	public long getVersion() {
		return version;
	}
	public void setVersion(long version) {
		this.version = version;
	}
	public String getFuncionalidade() {
		return funcionalidade;
	}
	public void setFuncionalidade(String funcionalidade) {
		this.funcionalidade = funcionalidade;
	}
	public Perfil getPerfil() {
		return perfil;
	}
	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	
	@Override
	public int compareTo(Permissao o) {
		return this.funcionalidade.compareTo(o.funcionalidade);
	}
}
