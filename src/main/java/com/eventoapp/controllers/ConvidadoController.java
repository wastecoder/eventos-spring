package com.eventoapp.controllers;

import com.eventoapp.dtos.ConvidadoDto;
import com.eventoapp.models.Convidado;
import com.eventoapp.models.Evento;
import com.eventoapp.services.ConvidadoService;
import com.eventoapp.services.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/eventos")
public class ConvidadoController {
    private final ConvidadoService convidadoService;
    private final EventoService eventoService;

    @Autowired
    public ConvidadoController(ConvidadoService convidadoService, EventoService eventoService) {
        this.convidadoService = convidadoService;
        this.eventoService = eventoService;
    }

    @PostMapping("/detalhes/{codigo}")
    public ModelAndView salvarCadastroConvidado(@PathVariable("codigo") Long codigo, @Valid ConvidadoDto convidadoValidado, BindingResult result, RedirectAttributes attributes) {
        Evento evento = eventoService.eventoId(codigo);

        if (evento != null) {
            if (result.hasErrors()) {
                Page<Convidado> pagina = convidadoService.escolherPagina(1, "nomeConvidado", "cresc", evento);

                ModelAndView mv = new ModelAndView("detalhesEvento");
                eventoService.paginacao(pagina, 1, "nomeConvidado", "cresc", mv);
                mv.addObject("evento", evento);
                mv.addObject("convidados", pagina);

                return mv;

            } else {
                Convidado convidado = convidadoValidado.toConvidado();
                convidado.setEvento(evento);
                if (convidadoService.salvarConvidado(convidado)) {
                    eventoService.mensagemSucesso(attributes, "Convidado adicionado com sucesso!");
                } else {
                    eventoService.mensagemErro(attributes, "CREATE: RG já cadastrado no evento!");
                }
            }
        }
        return new ModelAndView("redirect:/eventos/detalhes/{codigo}");
    }

    @GetMapping("/deletar-convidado/{id}")
    public String deletarConvidado(@PathVariable("id") Long id, RedirectAttributes attributes) {
        Convidado convidado = convidadoService.convidadoId(id);

        if (convidado != null) {
            Evento evento = convidado.getEvento();
            Long codigo = evento.getCodigo();
            String codigoEvento = Long.toString(codigo);

            if (convidadoService.deletarConvidado(id)) {
                eventoService.mensagemSucesso(attributes, "Convidado [" + id + "] deletado com sucesso!");
                return "redirect:/eventos/detalhes/" + codigoEvento;
            }
        }
        eventoService.mensagemErro(attributes, "DELETE: Convidado [" + id + "] não encontrado!");
        return "redirect:/eventos";
    }

    @GetMapping("/editar-convidado/{id}")
    public ModelAndView editarConvidado(@PathVariable("id") Long id, RedirectAttributes attributes){
        Convidado convidado = convidadoService.convidadoId(id);
        if (convidado != null) {
            ModelAndView mv = new ModelAndView("editarConvidado");
            mv.addObject("convidadoDto", convidado);
            return mv;
        }else {
            eventoService.mensagemErro(attributes,"UPDATE: Convidado [" + id + "] não encontrado!");
            return new ModelAndView("redirect:/eventos");
        }
    }

    @PostMapping("/editar-convidado/{id}")
    public ModelAndView salvarEdicaoConvidado(@PathVariable("id") Long id, @Valid ConvidadoDto convidadoAtualizado, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            return new ModelAndView("editarConvidado");

        } else {
            Convidado convidadoAntigo = convidadoService.convidadoId(id);
            if (convidadoAntigo != null) {
                if (convidadoService.atualizarConvidado(convidadoAntigo, convidadoAtualizado.toConvidado())) {
                    eventoService.mensagemSucesso(attributes, "Convidado [" + id + "] atualizado com sucesso!");
                } else {
                    eventoService.mensagemErro(attributes, "UPDATE: RG já cadastrado no evento!");
                }

                Long eventoId = convidadoAntigo.getEvento().getCodigo();
                return new ModelAndView("redirect:/eventos/detalhes/" + eventoId);
            }
            return null;
        }
    }
}
