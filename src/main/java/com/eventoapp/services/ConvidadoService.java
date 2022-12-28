package com.eventoapp.services;

import com.eventoapp.models.Convidado;
import com.eventoapp.models.Evento;
import com.eventoapp.repository.ConvidadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Page<Convidado> escolherPagina(int numeroPagina, String campo, String ordem, Evento evento) {
        int registrosPorPagina = 4;
        Sort ordenacao = Sort.by(campo);
        ordenacao = ordem.equals("cresc") ? ordenacao.ascending() : ordenacao.descending();

        Pageable pageable = PageRequest.of(--numeroPagina, registrosPorPagina, ordenacao);

        return convidadoRepository.findAllByEvento(evento, pageable);
    }

    public boolean salvarConvidado(Convidado convidado) {
        if (convidadoUnico(convidado, -1L)) {
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

    public boolean atualizarConvidado(Convidado convidadoAntigo, Convidado convidadoAtualizado) {
        if (convidadoUnico(convidadoAtualizado, convidadoAntigo.getId())) {
            convidadoAntigo.setNomeConvidado(convidadoAtualizado.getNomeConvidado());
            convidadoAntigo.setRg(convidadoAtualizado.getRg());

            convidadoRepository.save(convidadoAntigo);
            return true;
        }
        return false;
    }

    //Retorna false se há um RG idêntico no evento atual
    public boolean convidadoUnico(Convidado convidadoNovo, Long idExistente) {
        String rgNovo = convidadoNovo.getRg();

        Iterable<Convidado> todosConvidados;
        if (idExistente == -1) { //Entra aqui apenas no cadastro
            todosConvidados = this.acharPorEvento(convidadoNovo.getEvento());
        } else {
            //Precisa fazer isso porque no atualizarConvidado(), o convidadoAtualizado era um ConvidadoDTO. Ou seja, ele vem sem eventos e o iterable todosConvidados ficaria vazio.
            //Dessa forma ele vai achar o evento pelo idExistente, não pelo convidadoNovo.
            todosConvidados = this.acharPorEvento(convidadoId(idExistente).getEvento());
        }

        for (Convidado convidadoAtual : todosConvidados) {
            String rgAtual = convidadoAtual.getRg();
            Long idAtual = convidadoAtual.getId();

            if (idExistente.equals(idAtual)) continue;
            if (rgNovo.equals(rgAtual)) {
                return false;
            }
        }

        return true;
    }
}
