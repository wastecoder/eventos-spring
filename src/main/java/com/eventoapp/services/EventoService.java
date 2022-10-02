package com.eventoapp.services;

import com.eventoapp.models.Evento;
import com.eventoapp.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        //TODO: Verificar se há um evento igual (nome e local) no banco
        eventoRepository.save(evento);
    }

    public Evento eventoId(Long codigo) {
        Optional<Evento> opt = eventoRepository.findById(codigo);

        return opt.orElse(null); //return opt.isPresent() ? opt.get : null;
    }

    public boolean deletarEvento(Long codigo) {
        Evento retorno = this.eventoId(codigo);

        if (retorno != null) {
            eventoRepository.deleteById(codigo);
            return true;
        } else {
            return false;
        }
    }

    public void atualizarEvento(Evento eventoAntigo, Evento eventoAtualizado) {
        //TODO: Verificar se o evento foi alterado (ambos são iguais)
        //Se os eventos não foram alterados, não salvar no banco

        eventoAntigo.setNome(eventoAtualizado.getNome());
        eventoAntigo.setLocal(eventoAtualizado.getLocal());
        eventoAntigo.setData(eventoAtualizado.getData());
        eventoAntigo.setHorario(eventoAtualizado.getHorario());

        this.salvarEvento(eventoAntigo);
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
