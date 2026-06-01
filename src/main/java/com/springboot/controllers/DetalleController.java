package com.springboot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DetalleController {

	@GetMapping("/detallePedidos")
	public String detalle() {
		return "usuario/detallePedido";
	}
}
