package com.springboot.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.springboot.models.entiys.Contacto;
import com.springboot.models.serviceImplements.ContactoServiceImpl;

@Controller
public class EncuentranosController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(EncuentranosController.class);

    @Autowired
    private ContactoServiceImpl pedidoService;

	@GetMapping("/encuentranosAdmin")
	public String listaEncuentranos(Model modelo) {
		
		List<Contacto> listaContacto = pedidoService.listar();
		LOGGER.info("Lista de contactos cargada: {}", listaContacto);
		modelo.addAttribute("listaContacto", listaContacto);
		
		return "administrador/encuentranos";
	}
	
	@PostMapping("/guardarContacto")
	public String guardarContacto(@ModelAttribute Contacto contacto) {
		pedidoService.guardar(contacto);
		
		return "redirect:/encuentranosAdmin";
	}
	
	@GetMapping("/deleteContacto/{id}")
    public String delete(@PathVariable Integer id) {
        pedidoService.eliminarContacto(id);
        return "redirect:/encuentranosAdmin"; 
    }
}
