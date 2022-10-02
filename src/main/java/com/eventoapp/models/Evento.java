package com.eventoapp.models;

import org.hibernate.validator.constraints.UniqueElements;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Evento")
public class Evento implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long codigo;
	
		//Atributos - colunas da tabela
	@Column(nullable = false, length = 60)
	private String nome;
	@Column(nullable = false, length = 30)
	private String local;
	@Column(nullable = false, length = 10)
	private String data;
	@Column(nullable = false, length = 5)
	private String horario;
	
		//Cardinalidade
	@OneToMany(mappedBy = "evento", cascade = CascadeType.ALL)
	private Set<Convidado> convidados;
	
		//Métodos especiais
	public Evento() {
	}

	public Evento(Long codigo, String nome, String local, String data, String horario) {
		this.codigo = codigo;
		this.nome = nome;
		this.local = local;
		this.data = data;
		this.horario = horario;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
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
