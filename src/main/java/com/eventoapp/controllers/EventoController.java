package com.eventoapp.controllers;

import javax.validation.Valid;

import com.eventoapp.dtos.ConvidadoDto;
import com.eventoapp.dtos.EventoDto;
import com.eventoapp.services.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventoapp.models.Convidado;
import com.eventoapp.models.Evento;
import com.eventoapp.repository.ConvidadoRepository;

@Controller
@RequestMapping("/eventos")
public class EventoController {
	private final EventoService eventoService;

	@Autowired
	public EventoController(EventoService eventoService) {
		this.eventoService = eventoService;
	}

	@Autowired
	private ConvidadoRepository cr;

	@GetMapping
	public ModelAndView listaEventos() {
		ModelAndView mv = new ModelAndView("index");
		Iterable<Evento> event1 = eventoService.todosEventos();
		mv.addObject("eventos", event1);
		return mv;
	}

	@GetMapping("/cadastrar-evento")
	public String cadastrarEvento(EventoDto requisicao) { //Para não dar erro no th:object
		return "formEvento";
	}

	@PostMapping("/cadastrar-evento")
	public ModelAndView salvarCadastroEvento(@Valid EventoDto requisicao, BindingResult result, RedirectAttributes attributes) {
		Evento evento = requisicao.toEvento();
		if(result.hasErrors()) {
			return new ModelAndView("formEvento"); //Retorna e exibe o que já foi preenchido
		}
		eventoService.salvarEvento(evento);
		eventoService.mensagemSucesso(attributes, "Evento cadastrado com sucesso!");
		return new ModelAndView("redirect:/eventos");
	}

	@GetMapping("/detalhes/{codigo}")
	public ModelAndView detalhesEvento(@PathVariable("codigo") Long codigo, RedirectAttributes attributes, ConvidadoDto convidadoDto) {
		Evento evento = eventoService.eventoId(codigo);

		if (evento != null) {
			ModelAndView mv = new ModelAndView("detalhesEvento");
			mv.addObject("evento", evento);

			Iterable<Convidado> convidados1 = cr.findByEvento(evento);
			mv.addObject("convidados", convidados1);

			return mv;
		} else {
			eventoService.mensagemErro(attributes, "READ: Evento [" + codigo + "] não encontrado!");
			return new ModelAndView("redirect:/eventos");
		}
	}
	
	@GetMapping("/deletar-evento/{codigo}")
	public String deletarEvento(@PathVariable("codigo") Long codigo, RedirectAttributes attributes) {
		if (eventoService.deletarEvento(codigo)) {
			eventoService.mensagemSucesso(attributes, "Evento [" + codigo + "] deletado com sucesso!");
		} else {
			eventoService.mensagemErro(attributes, "DELETE: Evento [" + codigo + "] não encontrado!");
		}
		return "redirect:/eventos";
	}

	@GetMapping("/editar-evento/{codigo}")
	public ModelAndView editarEvento(@PathVariable("codigo") Long codigo, RedirectAttributes attributes){
		Evento evento = eventoService.eventoId(codigo);
		if (evento != null) {
			ModelAndView mv = new ModelAndView("editarEvento");
			mv.addObject("eventoDto", evento);
			return mv;
		}else {
			eventoService.mensagemErro(attributes,"UPDATE: Evento [" + codigo + "] não encontrado!");
			return new ModelAndView("redirect:/eventos");
		}
	}

	@PostMapping("/editar-evento/{codigo}")
	public ModelAndView salvarEdicaoEvento(@PathVariable("codigo") Long codigo, @Valid EventoDto requisicao, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			return new ModelAndView("editarEvento");

		} else {
			Evento evento = eventoService.eventoId(codigo);
			if (evento != null) {
				eventoService.atualizarEvento(evento, requisicao.toEvento());

				eventoService.mensagemSucesso(attributes, "Evento [" + codigo + "] atualizado com sucesso!");
				return new ModelAndView("redirect:/eventos");
			}
			return null;
		}
	}
}
