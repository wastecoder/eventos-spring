package com.eventoapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventoapp.models.Convidado;
import com.eventoapp.models.Evento;

@Repository
public interface ConvidadoRepository extends JpaRepository<Convidado, Long>{
	Iterable<Convidado> findByEvento(Evento evento);

	//Busca os filhos (Convidados) por Evento e retorna como Page<>
	Page<Convidado> findAllByEvento(Evento evento, Pageable pegeable);
}
