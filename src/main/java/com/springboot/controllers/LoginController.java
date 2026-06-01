package com.springboot.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.models.entiys.Usuario;
import com.springboot.models.service.LoginService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class LoginController {

	@Autowired
	private LoginService loginService;

    @GetMapping
    public String showLoginPage(HttpSession session, Model modelo) {
        if (session.getAttribute("user") != null) {
            String perfil = (String) session.getAttribute("perfil");
            if ("ADMIN".equals(perfil)) {
                return "redirect:/admin/dashboard";
            } else if ("USER".equals(perfil)) {
                return "usuario/index";
            }
        }
        return "usuario/login";
    }

	@PostMapping
	public String login(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {
		Optional<Usuario> optUsuario = loginService.login(username, password);
		if (optUsuario.isPresent()) {
			Usuario usuario = optUsuario.get();
			session.setAttribute("usuario", usuario);
			session.setAttribute("user", usuario.getCorreo());
			session.setAttribute("nombre", usuario.getNombre());
			session.setAttribute("perfil", usuario.getPerfil());

			model.addAttribute("user", usuario.getCorreo());
			model.addAttribute("nombre", usuario.getNombre());
			model.addAttribute("success", "Inicio de sesión exitoso.");

			if ("ADMIN".equals(usuario.getPerfil())) {
				return "redirect:/admin/dashboard";
			}
			return "usuario/index";
		}

		model.addAttribute("error", "Credenciales incorrectas. Inténtalo de nuevo.");
		return "usuario/login";
	}
	
}
