package com.eventoapp.services;

import com.eventoapp.models.Evento;
import com.eventoapp.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
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

    public Page<Evento> escolherPagina(int numeroDaPagina, String campo, String ordem) {
        int registrosPorPagina = 10;
        Sort ordenacao = Sort.by(campo);
        ordenacao = ordem.equals("cresc") ? ordenacao.ascending() : ordenacao.descending();

        Pageable pageable = PageRequest.of(--numeroDaPagina, registrosPorPagina, ordenacao);
        return eventoRepository.findAll(pageable);
    }

    public boolean salvarEvento(Evento evento) {
        if (eventoUnico(evento, -1L)) {
            eventoRepository.save(evento);
            return true;
        }

        return false;
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

    public boolean atualizarEvento(Evento eventoAntigo, Evento eventoAtualizado) {
        //Verifica se alterou para um nome e local já existente
        //Não verifica no ID do evento atualizado, pois não alteraria ao mudar só a data ou horário
        if (eventoUnico(eventoAtualizado, eventoAntigo.getCodigo())) {
            eventoAntigo.setNome(eventoAtualizado.getNome());
            eventoAntigo.setLocal(eventoAtualizado.getLocal());
            eventoAntigo.setData(eventoAtualizado.getData());
            eventoAntigo.setHorario(eventoAtualizado.getHorario());

            eventoRepository.save(eventoAntigo);
            return true;
        }
        return false;
    }

    public void mensagemSucesso(RedirectAttributes attributes, String messagem) {
        attributes.addFlashAttribute("mensagem", messagem);
        attributes.addFlashAttribute("error", false);
    }

    public void mensagemErro(RedirectAttributes attributes, String messagem) {
        attributes.addFlashAttribute("mensagem", messagem);
        attributes.addFlashAttribute("error", true);
    }

    //Retorna false se há um evento com o mesmo nome e local já registrado
    public boolean eventoUnico(Evento eventoNovo, Long idExistente) {
        //TODO: transformar esse método em query e colocar no EventoRepository
        Iterable<Evento> todosEventos = this.todosEventos();
        String nomeNovo = eventoNovo.getNome();
        String localNovo = eventoNovo.getLocal();

        for (Evento eventoAtual : todosEventos) {
            String nomeAtual = eventoAtual.getNome();
            String localAtual = eventoAtual.getLocal();
            Long idAtual = eventoAtual.getCodigo();

            if (idExistente.equals(idAtual)) continue;
            if (nomeNovo.equalsIgnoreCase(nomeAtual) &&
                    localNovo.equalsIgnoreCase(localAtual)) {
                return false;
            }
        }

        return true;
    }

    //T para aceitar Page<> de Evento e Convidado
    public <T> void paginacao(Page<T> pagina, int paginaSelecionada, String campoSelecionado, String ordem, ModelAndView mv) {
        int totalPaginas = pagina.getTotalPages() == 0 ? 1 : pagina.getTotalPages(); //Caso tenha 0 registro, os botões ficariam: 1, 0

        int inicioLaco, fimLaco; //Dá para iniciar elas com valores que se repetem, deixei assim para ser mais legível
        if (totalPaginas <= 5) {
            inicioLaco = 1;
            fimLaco = totalPaginas;
        } else if (paginaSelecionada <= 3) {
            inicioLaco = 1;
            fimLaco = 5;
        } else if (paginaSelecionada < totalPaginas - 2) {
            inicioLaco = paginaSelecionada - 2;
            fimLaco = paginaSelecionada + 2;
        } else {
            inicioLaco = totalPaginas - 4;
            fimLaco = totalPaginas;
        }

        String legenda = "Exibindo " + pagina.getNumberOfElements()
                + " de " + pagina.getTotalElements() + " registros";

        mv.addObject("paginaAtual", paginaSelecionada);
        mv.addObject("campo", campoSelecionado);
        mv.addObject("ordem", ordem);
        mv.addObject("ordemInversa", ordem.equals("cresc") ? "decre" : "cresc");
        mv.addObject("totalPaginas", totalPaginas);
        mv.addObject("inicio", inicioLaco);
        mv.addObject("fim", fimLaco);
        mv.addObject("legenda", legenda);
    }
}
