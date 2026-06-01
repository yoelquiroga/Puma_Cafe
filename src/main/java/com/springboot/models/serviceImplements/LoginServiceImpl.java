package com.springboot.models.serviceImplements;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.models.entiys.Usuario;
import com.springboot.models.repository.UsuarioRepository;
import com.springboot.models.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Optional<Usuario> login(String correo, String password) {
		Optional<Usuario> optUsuario = usuarioRepository.findByCorreo(correo);
		if (optUsuario.isPresent()) {
			Usuario usuario = optUsuario.get();
			String storedPassword = usuario.getContraseña();
			if (storedPassword == null) return Optional.empty();
			if (storedPassword.startsWith("$2a$")) {
				if (passwordEncoder.matches(password, storedPassword)) {
					return optUsuario;
				}
			} else if (password.equals(storedPassword)) {
				return optUsuario;
			}
		}
		return Optional.empty();
	}
}
