package com.springboot.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.springboot.models.entiys.Contacto;
import com.springboot.models.entiys.Producto;
import com.springboot.models.serviceImplements.ContactoServiceImpl;
import com.springboot.models.serviceImplements.ProductoServiceImp;

import jakarta.servlet.http.HttpSession;

@Controller
public class IndexController {
	
	@Autowired
	private ProductoServiceImp services;
	
	@Autowired
    private ContactoServiceImpl pedidoService;
	
	@GetMapping("/")
	public String index(HttpSession session, Model modelo) {
		
		//List<Producto> productos = services.listar();
		List<Producto> mejoresProductos = services.obtenerMejoresProductos();
		modelo.addAttribute("productos", mejoresProductos);
		
		List<Producto> nuestrosCafes = services.obtenerNuestroCafe(); // Nueva implementación para "Nuestros Cafés"
	    modelo.addAttribute("cafes", nuestrosCafes);
		
		return "usuario/index";
	}
	@GetMapping("/login")
	public String login() {
		return "usuario/login";
	}
	
	@GetMapping("/tienda")
	public String tienda(Model modelo) {
		List<Producto> productosTienda = services.obtenerProductosTienda();
	    modelo.addAttribute("productosTienda", productosTienda);
		return "usuario/tienda";
	}
	
	@GetMapping("/cafeteria")
	public String cafeteria(Model modelo) {
		
		// Obtener productos de cada categoría
	    List<Producto> productosCafeClasico = services.obtenerProductosCafeClasico();
	    List<Producto> productosCafeEspecial = services.obtenerProductosCafeEspecial();
	    List<Producto> productosCafeFrio = services.obtenerProductosCafeFrio();
	    
	    // Pasar los productos al modelo
	    modelo.addAttribute("productosCafeClasico", productosCafeClasico);
	    modelo.addAttribute("productosCafeEspecial", productosCafeEspecial);
	    modelo.addAttribute("productosCafeFrio", productosCafeFrio);
		
		return "usuario/cafeteria";
	}
	
	@GetMapping("/encuentranos")
	public String encuentranos() {
		return "usuario/encuentranos";
	}
	
	@PostMapping("/guardarEncuentranos")
	public String guardarContacto(@ModelAttribute Contacto contacto, Model model) {
		contacto.setFecha(new Date());
		pedidoService.guardar(contacto);
		model.addAttribute("contactoExitoso", true);
		return "usuario/encuentranos";
	}
	
	@GetMapping("/historia")
	public String historia() {
		return "usuario/historia";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}
}
