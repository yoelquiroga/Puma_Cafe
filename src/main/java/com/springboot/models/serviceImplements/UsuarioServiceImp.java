package com.springboot.models.serviceImplements;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.models.entiys.Pedido;
import com.springboot.models.entiys.Usuario;
import com.springboot.models.repository.UsuarioRepository;
import com.springboot.models.service.PedidoService;
import com.springboot.models.service.UsuarioService;

@Service
public class UsuarioServiceImp implements UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PedidoService pedidoService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public List<Usuario> listarUsuarios() {
		return usuarioRepository.findAll();
	}

	@Override
	public Usuario guardarUsuario(Usuario usuario) {
		String password = usuario.getContraseña();
		if (password != null && !password.startsWith("$2a$")) {
			usuario.setContraseña(passwordEncoder.encode(password));
		}
		return usuarioRepository.save(usuario);
	}

	@Override
	public Optional<Usuario> getUsuario(Integer id) {
		return usuarioRepository.findById(id);
	}

	@Override
	public void eliminarUsuario(Integer id) {
		Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
		if (usuarioOpt.isPresent()) {
			Usuario usuario = usuarioOpt.get();
			List<Pedido> pedidos = usuario.getPedidos();
			pedidos.forEach(pedido -> pedidoService.eliminarPedido(pedido.getIdPedidos()));
			usuarioRepository.deleteById(id);
		} else {
			throw new RuntimeException("Usuario no encontrado con ID: " + id);
		}
	}
}
