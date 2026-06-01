package com.springboot.models.service;

import java.util.Optional;

import com.springboot.models.entiys.Usuario;

public interface LoginService {

	Optional<Usuario> login(String correo, String password);

}
