package com.springboot.models.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.models.entiys.DetallePedido;
import com.springboot.models.entiys.Pedido;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer>{
	List<DetallePedido> findByPedido(Pedido pedido);
}
