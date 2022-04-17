package com.eventoapp.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Convidado")
public class Convidado {
		//Atributos - colunas da tabela
	@Id
	@NotEmpty @NotBlank
	@Size(min = 9, max = 12)
	@Column(nullable = false, length = 12)
	private String rg;
	@NotEmpty @NotBlank
	@Size(min = 3, max = 60)
	@Column(nullable = false, length = 60)
	private String nomeConvidado;
	
		//Cardinalidade
		//Vários candidados podem entrar em 1 evento
	@ManyToOne
	private Evento evento;
	
		//Métodos especiais	
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
