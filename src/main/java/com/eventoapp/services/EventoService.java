package com.eventoapp.services;

import com.eventoapp.models.Evento;
import com.eventoapp.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Service
public class EventoService {
    private final EventoRepository eventoRepository;

    @Autowired
    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public Iterable<Evento> todosEventos() {
        return eventoRepository.findAll();
    }

    public void salvarEvento(Evento evento) {
        eventoRepository.save(evento);
    }

    public Evento eventoId(Long codigo) {
        Optional<Evento> opt = eventoRepository.findById(codigo);

        return opt.orElse(null); //return opt.isPresent() ? opt.get : null;
    }

    public boolean deletarEvento(Long codigo) {
        try {
            eventoRepository.deleteById(codigo);
            return true;
        } catch (EmptyResultDataAccessException error) {
            return false;
        }
    }

    public void mensagemSucesso(RedirectAttributes attributes, String messagem) {
        attributes.addFlashAttribute("mensagem", messagem);
        attributes.addFlashAttribute("error", false);
    }

    public void mensagemErro(RedirectAttributes attributes, String messagem) {
        attributes.addFlashAttribute("mensagem", messagem);
        attributes.addFlashAttribute("error", true);
    }
}
