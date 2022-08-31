package com.eventoapp.models;

import javax.persistence.*;

@Entity
@Table(name = "Convidado")
public class Convidado {
		//Atributos - colunas da tabela
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 12)
	private String rg;
	@Column(nullable = false, length = 60)
	private String nomeConvidado;
	
		//Cardinalidade
		//Vários candidados podem entrar em 1 evento
	@ManyToOne
	private Evento evento;
	
		//Métodos especiais
	public Long getId() {
		return id;
	}

	public String getRg() {
		return rg;
	}
	
	public void setRg(String rg) {
		this.rg = rg;
	}
	
	public String getNomeConvidado() {
		return nomeConvidado;
	}
	
	public void setNomeConvidado(String nomeConvidado) {
		this.nomeConvidado = nomeConvidado;
	}
	
	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}
}
