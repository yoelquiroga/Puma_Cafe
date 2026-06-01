package com.springboot.models.service;

import java.util.List;
import java.util.Optional;

import com.springboot.models.entiys.Usuario;

public interface UsuarioService {

	List<Usuario> listarUsuarios();

	Usuario guardarUsuario(Usuario usuario);

	Optional<Usuario> getUsuario(Integer id);

	void eliminarUsuario(Integer id);
}
