package com.eventoapp.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
public class EventoController {
	@Autowired
	private EventoRepository er;
	
	@Autowired
	private ConvidadoRepository cr;
	
	@RequestMapping(value="/cadastrarEvento", method=RequestMethod.GET)
	public String form() {
		return "formEvento";
	}
	
	@RequestMapping(value="/cadastrarEvento", method=RequestMethod.POST)
	public String form(@Valid Evento evento, BindingResult result) {
		if(result.hasErrors()) {
			return "redirect:/cadastrarEvento";
		}
		er.save(evento);
		return "redirect:/";
	}
	
	@RequestMapping("/")
	public ModelAndView listaEventos() {
		ModelAndView mv = new ModelAndView("index");
		Iterable<Evento> event1 = er.findAll();
		mv.addObject("eventos", event1);
		return mv;
	}
	
	@RequestMapping(value="/{codigo}", method=RequestMethod.GET)
	public ModelAndView detalhesEvento(@PathVariable("codigo") long codigo) {
		Evento event2 = er.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("detalhesEvento");
		mv.addObject("evento", event2);
		
		Iterable<Convidado> convidados1 = cr.findByEvento(event2);
		mv.addObject("convidados", convidados1);
		
		return mv;
	}
	
	@RequestMapping(value="/{codigo}", method=RequestMethod.POST)
	public String detalhesEventoPost(@PathVariable("codigo") long codigo, @Valid Convidado convidado, BindingResult result, RedirectAttributes attributes) {
		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Campos inválidos!");
			return "redirect:/{codigo}";
		}
		
		Evento event3 = er.findByCodigo(codigo);
		convidado.setEvento(event3);
		cr.save(convidado);
		attributes.addFlashAttribute("mensagem", "Convidado adicionado com sucesso!");
		return "redirect:/{codigo}";
	}
	
	@RequestMapping(value="/deletarEvento", method=RequestMethod.GET)
	public String deletarEvento(long codigo) {
		Evento event4 = er.findByCodigo(codigo);
		er.delete(event4);
		return "redirect:/";
	}
	
	@RequestMapping(value="/deletarConvidado", method=RequestMethod.GET)
	public String deletarConvidado(String rg) {
		Convidado convidado = cr.findByRg(rg);
		cr.delete(convidado);
		
		Evento evento = convidado.getEvento();
		long codigoEvento = evento.getCodigo();
		String codigo = Long.toString(codigoEvento);
		return "redirect:/" + codigo;
	}

	@GetMapping("/editarEvento-{codigo}")
	public ModelAndView editarEvento(@PathVariable("codigo") Long codigo){
		Evento evento5 = er.findByCodigo(codigo);
		ModelAndView mv = new ModelAndView("editarEvento");
		mv.addObject("evento", evento5);
		return mv;
	}

	@PostMapping("/editarEvento-{codigo}")
	public String salvarEdicaoEvento(@PathVariable("codigo") Long codigo, @Valid Evento evento, BindingResult result, RedirectAttributes attributes) {
		long codigoEvento = evento.getCodigo();
		String codigoUrl = Long.toString(codigoEvento);

		if(result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Campos inválidos!");
			return "redirect:/editarEvento-" + codigoUrl;
		}

		Optional<Evento> evento6 = er.findById(codigo);
		if (evento6.isPresent()) {
			Evento storeEvent = evento6.get();
			storeEvent.setNome(evento.getNome());
			storeEvent.setLocal(evento.getLocal());
			storeEvent.setData(evento.getData());
			storeEvent.setHorario(evento.getHorario());
			er.save(storeEvent);
		}

		attributes.addFlashAttribute("mensagem", "Evento atualizado com sucesso!");
		return "redirect:/editarEvento-" + codigoUrl;
	}
}
