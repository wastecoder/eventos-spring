package com.eventoapp.controllers;

import javax.validation.Valid;

import com.eventoapp.dtos.ConvidadoDto;
import com.eventoapp.dtos.EventoDto;
import com.eventoapp.services.ConvidadoService;
import com.eventoapp.services.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventoapp.models.Convidado;
import com.eventoapp.models.Evento;

@Controller
@RequestMapping("/eventos")
public class EventoController {
	private final EventoService eventoService;
	private final ConvidadoService convidadoService;

	@Autowired
	public EventoController(EventoService eventoService, ConvidadoService convidadoService) {
		this.eventoService = eventoService;
		this.convidadoService = convidadoService;
	}

	@GetMapping
	public ModelAndView primeiraPagina() {
		//Depois alterar "codigo" para "nome"
		//Deixei assim para facilitar os meus testes manuais
		return mudarPagina(1, "codigo", "cresc");
	}

	@GetMapping("/pagina/{numeroPagina}")
	public ModelAndView mudarPagina(@PathVariable("numeroPagina") int paginaSelecionada, @RequestParam("campo") String campoSelecionado, @RequestParam("ordem") String ordem) {
		Page<Evento> pagina = eventoService.escolherPagina(paginaSelecionada, campoSelecionado, ordem);

		ModelAndView mv = new ModelAndView("index");
		eventoService.paginacao(pagina, paginaSelecionada, campoSelecionado, ordem, mv);
		mv.addObject("eventos", pagina);

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

		if (eventoService.salvarEvento(evento)) {
			eventoService.mensagemSucesso(attributes, "Evento cadastrado com sucesso!");
		} else {
			eventoService.mensagemErro(attributes, "CREATE: nome e local já cadastrado!");
		}
		return new ModelAndView("redirect:/eventos");
	}

	@GetMapping("/detalhes/{codigo}")
	public ModelAndView detalhesEvento(@PathVariable("codigo") Long codigo, @RequestParam(value = "pagina", defaultValue = "1") int paginaSelecionada, @RequestParam(value = "campo", defaultValue = "nomeConvidado") String campoSelecionado, @RequestParam(value = "ordem", defaultValue = "cresc") String ordem, RedirectAttributes attributes, ConvidadoDto convidadoDto) {
		Evento evento = eventoService.eventoId(codigo);

		if (evento != null) {
			Page<Convidado> pagina = convidadoService.escolherPagina(paginaSelecionada, campoSelecionado, ordem, evento);

			ModelAndView mv = new ModelAndView("detalhesEvento");
			eventoService.paginacao(pagina, paginaSelecionada, campoSelecionado, ordem, mv);
			mv.addObject("evento", evento);
			mv.addObject("convidados", pagina);

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
	public ModelAndView salvarEdicaoEvento(@PathVariable("codigo") Long codigo, @Valid EventoDto eventoAtualizado, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			return new ModelAndView("editarEvento");

		} else {
			Evento eventoAntigo = eventoService.eventoId(codigo);
			if (eventoAntigo != null) {
				if (eventoService.atualizarEvento(eventoAntigo, eventoAtualizado.toEvento())) {
					eventoService.mensagemSucesso(attributes, "Evento [" + codigo + "] atualizado com sucesso!");
				} else {
					eventoService.mensagemErro(attributes, "UPDATE: nome e local já cadastrado!");
				}

				return new ModelAndView("redirect:/eventos");
			}
			return null;
		}
	}
}
