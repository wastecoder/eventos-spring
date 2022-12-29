package com.eventoapp.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "Convidado")
@Getter @Setter @NoArgsConstructor
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
	public Convidado(Long id, String rg, String nomeConvidado) {
		this.id = id;
		this.rg = rg;
		this.nomeConvidado = nomeConvidado;
	}
}
