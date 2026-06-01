package com.springboot.models.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.springboot.models.entiys.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
	List<Pedido> findByUsuarioIdUsuario(Integer idUsuario);

	List<Pedido> findTop5ByOrderByIdPedidosDesc();

	@Query(value = "SELECT COALESCE(SUM(total), 0) FROM pedidos WHERE estado != 'CANCELADO'", nativeQuery = true)
	Double totalVentas();

	@Query(value = "SELECT COUNT(*) FROM pedidos WHERE estado IN ('PENDIENTE','EN_RUTA')", nativeQuery = true)
	Long countPedidosActivos();

	List<Pedido> findByEstadoNot(Pedido.EstadoPedido estado);
}
