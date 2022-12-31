package com.eventoapp.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
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
@Getter @Setter @NoArgsConstructor
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
	@DateTimeFormat(pattern = "yyyy-MM-dd") //Se tirar, a data vem vazia ao editar
	private LocalDate data;
	@Column(nullable = false, length = 5)
	private String horario;
	
		//Cardinalidade
	@OneToMany(mappedBy = "evento", cascade = CascadeType.ALL)
	private Set<Convidado> convidados;
	
		//Métodos especiais
	public Evento(Long codigo, String nome, String local, LocalDate data, String horario) {
		this.codigo = codigo;
		this.nome = nome;
		this.local = local;
		this.data = data;
		this.horario = horario;
	}
}
