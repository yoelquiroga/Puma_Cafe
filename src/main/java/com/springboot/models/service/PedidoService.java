package com.springboot.models.service;

import java.util.List;
import java.util.Optional;

import com.springboot.models.entiys.Pedido;

public interface PedidoService {

	  List<Pedido> listarPedidos();

	  void guardarPedido(Pedido pedido);

	  Optional<Pedido> getPedido(Integer id);

	  Pedido actualizarPedido(Pedido pedido);

	  void eliminarPedido(Integer id);

	  List<Pedido> obtenerPedidosPorUsuario(Integer idUsuario);

}
