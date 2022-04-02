package com.eventoapp.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Evento")
public class Evento implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long codigo;
	
		//Atributos - colunas da tabela
	@NotEmpty(message = "Nome vázio, preencha novamente.")
	@Size(min = 3, max = 60, message = "Nome: min 3, max 60.")
	@Column(nullable = false, length = 60)
	private String nome;
	@NotEmpty(message = "Local vázio...")
	@Column(nullable = false, length = 30)
	private String local;
	@NotEmpty
	@Column(nullable = false, length = 10)
	private String data;
	@NotEmpty
	@Column(nullable = false, length = 5)
	private String horario;
	
		//Cardinalidade
	@OneToMany(mappedBy = "evento", cascade = CascadeType.ALL)
	private List<Convidado> convidados;
	
		//Métodos especiais
	public long getCodigo() {
		return codigo;
	}

	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getLocal() {
		return local;
	}
	
	public void setLocal(String local) {
		this.local = local;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public String getHorario() {
		return horario;
	}
	
	public void setHorario(String horario) {
		this.horario = horario;
	}
}
