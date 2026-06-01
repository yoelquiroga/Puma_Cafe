package com.springboot.models.serviceImplements;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.models.entiys.DetallePedido;
import com.springboot.models.entiys.Pedido;
import com.springboot.models.entiys.Producto;
import com.springboot.models.entiys.Usuario;
import com.springboot.models.repository.DetallePedidoRepository;
import com.springboot.models.repository.PedidoRepository;
import com.springboot.models.repository.ProductoRepository;
import com.springboot.models.service.PedidoService;

@Service
public class PedidoServiceImpl implements PedidoService {

	@Autowired
    private PedidoRepository pedidoRepository;

	@Autowired
	private DetallePedidoRepository detallePedidoRepository;

	@Autowired
	private ProductoRepository productoRepository;

	@Override
	public List<Pedido> listarPedidos() {
		return pedidoRepository.findAll();
	}

	@Override
	public void guardarPedido(Pedido pedido) {
		pedidoRepository.save(pedido);
	}

	@Override
	public Optional<Pedido> getPedido(Integer id) {
		return pedidoRepository.findById(id);
	}

	@Override
	public Pedido actualizarPedido(Pedido pedido) {
		Pedido pedidoExistente = pedidoRepository.findById(pedido.getIdPedidos())
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

		pedidoExistente.setMetodoPago(pedido.getMetodoPago());
		pedidoExistente.setDireccionEntrega(pedido.getDireccionEntrega());
		pedidoExistente.setEstado(pedido.getEstado());
		pedidoExistente.setFecha(pedido.getFecha());

		return pedidoRepository.save(pedidoExistente);
	}

	@Override
	public void eliminarPedido(Integer id) {
		pedidoRepository.deleteById(id);
	}

	@Override
	public List<Pedido> obtenerPedidosPorUsuario(Integer idUsuario) {
		return pedidoRepository.findByUsuarioIdUsuario(idUsuario);
	}

	@Transactional
	public Pedido guardarPedidoConDetalles(Pedido pedido, List<Object[]> items) {
		// Calcular total REAL desde la BD
		double totalCalculado = 0.0;
		for (Object[] item : items) {
			Integer idProducto = (Integer) item[0];
			Integer cantidad = (Integer) item[1];
			Producto producto = productoRepository.findById(idProducto)
				.orElseThrow(() -> new RuntimeException("Producto no encontrado: " + idProducto));
			totalCalculado += producto.getPrecio() * cantidad;
		}
		pedido.setTotal(totalCalculado);

		// Guardar pedido
		Pedido pedidoGuardado = pedidoRepository.save(pedido);

		// Guardar detalles
		for (Object[] item : items) {
			Integer idProducto = (Integer) item[0];
			Integer cantidad = (Integer) item[1];
			Producto producto = productoRepository.findById(idProducto)
				.orElseThrow(() -> new RuntimeException("Producto no encontrado: " + idProducto));

			DetallePedido detalle = new DetallePedido();
			detalle.setPedido(pedidoGuardado);
			detalle.setProducto(producto);
			detalle.setCantidad(cantidad);
			detalle.setPrecioUnitario(producto.getPrecio());
			detalle.setTotal(producto.getPrecio() * cantidad);
			detallePedidoRepository.save(detalle);
		}

		return pedidoGuardado;
	}

}
