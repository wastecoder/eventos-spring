package com.eventoapp.services;

import com.eventoapp.models.Convidado;
import com.eventoapp.models.Evento;
import com.eventoapp.repository.ConvidadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public boolean salvarConvidado(Convidado convidado) {
        if (convidadoUnico(convidado)) {
            convidadoRepository.save(convidado);
            return true;
        }
        return false;
    }

    public Convidado convidadoId(Long codigo) {
        Optional<Convidado> opt = convidadoRepository.findById(codigo);

        return opt.orElse(null);
    }

    public boolean deletarConvidado(Long codigo) {
        Convidado retorno = this.convidadoId(codigo);

        if (retorno != null) {
            convidadoRepository.deleteById(codigo);
            return true;
        } else {
            return false;
        }
    }

    //Retorna false se há um RG idêntico no evento atual
    public boolean convidadoUnico(Convidado convidadoNovo) {
        Iterable<Convidado> todosConvidados = this.acharPorEvento(convidadoNovo.getEvento());
        String rgNovo = convidadoNovo.getRg();

        for (Convidado convidadoAtual : todosConvidados) {
            String rgAtual = convidadoAtual.getRg();

            if (rgNovo.equals(rgAtual)) {
                return false;
            }
        }

        return true;
    }
}
