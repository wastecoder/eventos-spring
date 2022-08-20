package com.eventoapp.controllers;

import javax.validation.Valid;

import com.eventoapp.dtos.EventoDto;
import jdk.jfr.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventoapp.models.Convidado;
import com.eventoapp.models.Evento;
import com.eventoapp.repository.ConvidadoRepository;
import com.eventoapp.repository.EventoRepository;

import java.util.Optional;

@Controller
@RequestMapping("/eventos")
public class EventoController {
	@Autowired
	private EventoRepository er;

	@Autowired
	private ConvidadoRepository cr;

	@GetMapping
	public ModelAndView listaEventos() {
		ModelAndView mv = new ModelAndView("index");
		Iterable<Evento> event1 = er.findAll();
		mv.addObject("eventos", event1);
		return mv;
	}

	@GetMapping("/cadastrar-evento")
	public String form(EventoDto requisicao) { //Para não dar erro no th:object
		return "formEvento";
	}

	@PostMapping("/cadastrar-evento")
	public ModelAndView form(@Valid EventoDto requisicao, BindingResult result, RedirectAttributes attributes) {
		Evento evento = requisicao.toEvento();
		if(result.hasErrors()) {
			return new ModelAndView("formEvento"); //Retorna e exibe o que já foi preenchido
		}
		er.save(evento);
		attributes.addFlashAttribute("mensagem", "Evento cadastrado com sucesso!");
		attributes.addFlashAttribute("error", false);
		return new ModelAndView("redirect:/eventos");
	}

	@GetMapping("/detalhes/{codigo}")
	public ModelAndView detalhesEvento(@PathVariable("codigo") long codigo, RedirectAttributes attributes) {
		Optional<Evento> opt = er.findById(codigo);

		if (opt.isPresent()) {
			Evento event2 = opt.get();
			ModelAndView mv = new ModelAndView("detalhesEvento");
			mv.addObject("evento", event2);

			Iterable<Convidado> convidados1 = cr.findByEvento(event2);
			mv.addObject("convidados", convidados1);

			return mv;

		} else {
			attributes.addFlashAttribute("mensagem", "Evento [" + codigo + "] não encontrado!");
			attributes.addFlashAttribute("error", true);
			return new ModelAndView("redirect:/eventos");
		}

	}
	
	@PostMapping("/detalhes/{codigo}")
	public String detalhesEventoPost(@PathVariable("codigo") long codigo, @Valid Convidado convidado, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			// Mensagem: formulário inválido, campos restaurados
			// Conflito: parâmetro igual & sem fragment da notificação
			attributes.addFlashAttribute("mensagem", "Campos inválidos!");
			//attributes.addFlashAttribute("error", true);

		} else {
			Optional<Evento> opt = er.findById(codigo);
			if (opt.isPresent()) {
				Evento event3 = opt.get();
				convidado.setEvento(event3);
				cr.save(convidado);
				attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");
				//attributes.addFlashAttribute("error", false);
			}
		}
		return "redirect:/eventos/detalhes/{codigo}";
	}
	
	@GetMapping("/deletar-evento/{codigo}")
	public String deletarEvento(@PathVariable("codigo") long codigo, RedirectAttributes attributes) {
		try {
			er.deleteById(codigo);
			attributes.addFlashAttribute("mensagem", "Evento [" + codigo + "] deletado com sucesso!");
			attributes.addFlashAttribute("error", false);

		} catch (EmptyResultDataAccessException error) {
			attributes.addFlashAttribute("mensagem", "Evento [" + codigo + "] não encontrado!");
			attributes.addFlashAttribute("error", true);
		}
		return "redirect:/eventos";
	}
	
	@GetMapping("/deletar-convidado/{rg}")
	public String deletarConvidado(@PathVariable("rg") String rg, RedirectAttributes attributes) {
		try {
			Convidado convidado = cr.findByRg(rg);
			Evento evento = convidado.getEvento();
			long codigo = evento.getCodigo();
			String codigoEvento = Long.toString(codigo);

			cr.deleteById(rg);
			// Conflito: mesma coisa do método detalhesEventoPost
			attributes.addFlashAttribute("mensagem", "Convidado deletado com sucesso!");
			//attributes.addFlashAttribute("error", false); // Esse arquivo ainda não tem notificação
			return "redirect:/eventos/detalhes/" + codigoEvento;

		} catch (NullPointerException error) {
			attributes.addFlashAttribute("mensagem", "Convidado não encontrado!");
			attributes.addFlashAttribute("error", true);
			return "redirect:/eventos";
		}
	}

	@GetMapping("/editar-evento/{codigo}")
	public ModelAndView editarEvento(@PathVariable("codigo") Long codigo){
		Optional<Evento> opt = er.findById(codigo);
		if (opt.isPresent()) {
			Evento event3 = opt.get();
			ModelAndView mv = new ModelAndView("editarEvento");
			mv.addObject("eventoDto", event3);
			return mv;

		}else {
			return new ModelAndView("redirect:/eventos");
		}
	}

	@PostMapping("/editar-evento/{codigo}")
	public ModelAndView salvarEdicaoEvento(@PathVariable("codigo") Long codigo, @Valid EventoDto requisicao, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			return new ModelAndView("editarEvento");

		} else {
			Optional<Evento> opt = er.findById(codigo);
			if (opt.isPresent()) {
				Evento evento6 = opt.get();
				evento6.setNome(requisicao.getNome());
				evento6.setLocal(requisicao.getLocal());
				evento6.setData(requisicao.getData());
				evento6.setHorario(requisicao.getHorario());
				er.save(evento6);

				attributes.addFlashAttribute("mensagem", "Evento [" + codigo + "] atualizado com sucesso!");
				attributes.addFlashAttribute("error", false);
				return new ModelAndView("redirect:/eventos");
			}
			return null;
		}
	}
}
