package com.eventoapp.controllers;

import com.eventoapp.dtos.ConvidadoDto;
import com.eventoapp.models.Convidado;
import com.eventoapp.models.Evento;
import com.eventoapp.services.ConvidadoService;
import com.eventoapp.services.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
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
                ModelAndView mv = new ModelAndView("detalhesEvento");
                mv.addObject("evento", evento);

                Iterable<Convidado> convidados = convidadoService.acharPorEvento(evento);
                mv.addObject("convidados", convidados);

                return mv;

            } else {
                Convidado convidado = convidadoValidado.toConvidado();
                convidado.setEvento(evento);
                if (convidadoService.salvarConvidado(convidado)) {
                    eventoService.mensagemSucesso(attributes, "Convidado adicionado com sucesso!");
                } else {
                    eventoService.mensagemErro(attributes, "Convidado já cadastrado!");
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
}
