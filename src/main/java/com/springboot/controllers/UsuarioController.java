package com.springboot.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.models.entiys.Usuario;
import com.springboot.models.repository.UsuarioRepository;
import com.springboot.models.serviceImplements.UsuarioServiceImp;

import jakarta.servlet.http.HttpSession;

@Controller
public class UsuarioController {

	private final Logger LOGGER = LoggerFactory.getLogger(UsuarioController.class);

	@Autowired
	private UsuarioServiceImp usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@GetMapping("/listaClientes")
	public String listado(Model modelo) {
		List<Usuario> todos = usuarioService.listarUsuarios();
		List<Usuario> clientes = todos.stream()
			.filter(u -> "USER".equals(u.getPerfil()))
			.collect(Collectors.toList());
		LOGGER.info("Lista de clientes cargada: {}", clientes);
		modelo.addAttribute("listaClientes", clientes);
		return "administrador/clientes";
	}

	@GetMapping("/deleteCliente/{id}")
	public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		try {
			usuarioService.eliminarUsuario(id);
			LOGGER.info("Usuario eliminado con ID: {}", id);
			redirectAttributes.addFlashAttribute("success", "Usuario eliminado correctamente");
		} catch (Exception e) {
			LOGGER.error("Error al eliminar usuario con ID: {}", id, e);
			redirectAttributes.addFlashAttribute("error", "Error al eliminar el usuario");
		}
		return "redirect:/listaClientes";
	}

	@GetMapping("/clientes/registro")
	public String mostrarFormularioRegistro() {
		return "usuario/login";
	}

	@PostMapping("/clientes/registro")
	public String registrarCliente(
			@RequestParam("nombre") String nombre,
			@RequestParam("apellido") String apellido,
			@RequestParam("correo") String correo,
			@RequestParam("contraseña") String contraseña,
			HttpSession session, Model model) {

		Optional<Usuario> existente = usuarioRepository.findByCorreo(correo);
		if (existente.isPresent()) {
			model.addAttribute("error", "El correo ya está registrado. Usa otro o inicia sesión.");
			return "usuario/login";
		}

		Usuario nuevoUsuario = new Usuario();
		nuevoUsuario.setNombre(nombre);
		nuevoUsuario.setApellido(apellido);
		nuevoUsuario.setCorreo(correo);
		nuevoUsuario.setContraseña(contraseña);
		nuevoUsuario.setPerfil("USER");

		Usuario guardado = usuarioService.guardarUsuario(nuevoUsuario);

		session.setAttribute("usuario", guardado);
		session.setAttribute("user", guardado.getCorreo());
		session.setAttribute("nombre", guardado.getNombre());
		session.setAttribute("perfil", guardado.getPerfil());

		model.addAttribute("success", "Registro exitoso. ¡Bienvenido!");
		return "redirect:/";
	}
}
