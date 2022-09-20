package com.eventoapp.services;

import com.eventoapp.models.Convidado;
import com.eventoapp.models.Evento;
import com.eventoapp.repository.ConvidadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConvidadoService {
    private final ConvidadoRepository convidadoRepository;

    @Autowired
    public ConvidadoService(ConvidadoRepository convidadoRepository) {
        this.convidadoRepository = convidadoRepository;
    }

    public Iterable<Convidado> acharPorEvento(Evento evento) {
        return convidadoRepository.findByEvento(evento);
    }

    public void salvarConvidado(Convidado convidado) {
        convidadoRepository.save(convidado);
    }

    public Convidado convidadoId(Long codigo) {
        Optional<Convidado> opt = convidadoRepository.findById(codigo);

        return opt.orElse(null);
    }

    public boolean deletarConvidado(Long codigo) {
        try {
            convidadoRepository.deleteById(codigo);
            return true;
        } catch (EmptyResultDataAccessException error) {
            return false;
        }
    }
}
