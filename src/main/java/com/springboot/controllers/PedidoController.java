package com.springboot.controllers;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.models.entiys.DetallePedido;
import com.springboot.models.entiys.Pedido;
import com.springboot.models.entiys.Usuario;
import com.springboot.models.entiys.Pedido.EstadoPedido;
import com.springboot.models.entiys.Pedido.MetodoPago;
import com.springboot.models.repository.DetallePedidoRepository;
import com.springboot.models.repository.PedidoRepository;
import com.springboot.models.service.BoletaService;
import com.springboot.models.serviceImplements.PedidoServiceImpl;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class PedidoController {

	private final Logger LOGGER = LoggerFactory.getLogger(PedidoController.class);

    @Autowired
    private PedidoServiceImpl pedidoService;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private BoletaService boletaService;

    @GetMapping("/listaPedidos")
    public String listado(Model modelo) {
        List<Pedido> listaPedidos = pedidoService.listarPedidos();
        LOGGER.info("Lista de pedidos cargada: {}", listaPedidos);
        modelo.addAttribute("listaPedidos", listaPedidos);
        return "administrador/pedidos";
    }

    @PostMapping("/guardarPedido")
    public String guardar(
    		@RequestParam("metodoPago") String metodoPagoStr,
    		@RequestParam("direccionEntrega") String direccionEntrega,
    		@RequestParam("cartData") String cartDataJson,
    		HttpSession session,
    		Model modelo) {

    	// 1. Verificar sesión del usuario
    	Usuario usuario = (Usuario) session.getAttribute("usuario");
    	if (usuario == null) {
    		LOGGER.warn("Intento de crear pedido sin sesión de usuario");
    		return "redirect:/login";
    	}

    	LOGGER.info("Creando pedido para usuario: {} - CartData: {}", usuario.getCorreo(), cartDataJson);

    	// 2. Parsear cartData JSON manualmente
    	List<Object[]> items = new ArrayList<>();
    	try {
    		// Formato esperado: [{"id":5,"quantity":1},{"id":17,"quantity":2}]
    		String json = cartDataJson.trim();
    		if (json.startsWith("[")) {
    			String[] parts = json.substring(1, json.length() - 1).split("\\},\\{");
    			for (String part : parts) {
    				part = part.replace("{", "").replace("}", "");
    				String[] fields = part.split(",");
    				Integer idProducto = null;
    				Integer cantidad = null;
    				for (String field : fields) {
    					String[] kv = field.split(":");
    					String key = kv[0].trim().replace("\"", "");
    					String value = kv[1].trim().replace("\"", "");
    					if ("id".equals(key)) {
    						idProducto = Integer.parseInt(value);
    					} else if ("quantity".equals(key)) {
    						cantidad = Integer.parseInt(value);
    					}
    				}
    				if (idProducto != null && cantidad != null) {
    					items.add(new Object[]{idProducto, cantidad});
    				}
    			}
    		}
    	} catch (Exception e) {
    		LOGGER.error("Error parseando cartData: {}", e.getMessage());
    		modelo.addAttribute("error", "Error al procesar el carrito");
    		return "usuario/nuevoPedido";
    	}

    	if (items.isEmpty()) {
    		LOGGER.warn("Carrito vacío al intentar crear pedido");
    		modelo.addAttribute("error", "El carrito está vacío");
    		return "usuario/nuevoPedido";
    	}

    	// 3. Crear el pedido
    	Pedido pedido = new Pedido();
    	pedido.setUsuario(usuario);
    	pedido.setMetodoPago(MetodoPago.valueOf(metodoPagoStr));
    	pedido.setDireccionEntrega(direccionEntrega);
    	pedido.setEstado(EstadoPedido.PENDIENTE);
    	pedido.setFecha(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

    	// 4. Guardar con detalles (cálculo de total REAL desde BD)
    	pedidoService.guardarPedidoConDetalles(pedido, items);

    	LOGGER.info("Pedido creado exitosamente para usuario: {}", usuario.getCorreo());

    	return "redirect:/misPedidos?success=true";
    }

    @GetMapping("/editPedido/{id}")
    public String edit(@PathVariable Integer id, Model modelo) {
        Optional<Pedido> optionalPedido = pedidoService.getPedido(id);
        if (optionalPedido.isPresent()) {
            modelo.addAttribute("pedido", optionalPedido.get());
            return "administrador/editarPedido";
        }
        return "redirect:/listaPedidos";
    }

    @PostMapping("/updatePedido")
    public String update(@ModelAttribute Pedido pedido, BindingResult result, Model modelo) {
        if (result.hasErrors()) {
            LOGGER.error("Errores al actualizar el pedido: {}", result.getAllErrors());
            modelo.addAttribute("pedido", pedido);
            return "administrador/editarPedido";
        }
        LOGGER.info("Actualizando pedido: {}", pedido);
        pedidoService.actualizarPedido(pedido);
        return "redirect:/listaPedidos?update=true";
    }

    @GetMapping("/deletePedido/{id}")
    public String delete(@PathVariable Integer id) {
        pedidoService.eliminarPedido(id);
        return "redirect:/listaPedidos";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, "fecha", new CustomDateEditor(dateFormat, true));
    }

    @GetMapping("/realizarPedido")
    public String realizarPedido(Model modelo) {
        return "usuario/nuevoPedido";
    }

    @GetMapping("/misPedidos")
    public String misPedidos(HttpSession session, Model modelo) {
    	Usuario usuario = (Usuario) session.getAttribute("usuario");
    	if (usuario == null) {
    		return "redirect:/login";
    	}
    	List<Pedido> pedidos = pedidoService.obtenerPedidosPorUsuario(usuario.getIdUsuario());
    	modelo.addAttribute("pedidos", pedidos);
    	return "usuario/misPedidos";
    }

    @GetMapping("/detallePedido/{id}")
    public String mostrarDetallePedido(@PathVariable int id, HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Pedido pedido = pedidoRepository.findById(id).orElse(null);

        if (pedido == null) {
        	return "redirect:/misPedidos";
        }

        // Seguridad: si es usuario normal, verificar que el pedido sea suyo
        String perfil = (String) session.getAttribute("perfil");
        if (!"ADMIN".equals(perfil) && (usuario == null || pedido.getUsuario().getIdUsuario() != usuario.getIdUsuario())) {
        	return "redirect:/misPedidos";
        }

        List<DetallePedido> detalles = detallePedidoRepository.findByPedido(pedido);
        model.addAttribute("pedido", pedido);
        model.addAttribute("detalles", detalles);
        model.addAttribute("total", detalles.stream().mapToDouble(d -> d.getPrecioUnitario() * d.getCantidad()).sum());
        return "usuario/detallePedido";
    }

    @GetMapping("/pedido/boleta/{id}")
    public void descargarBoleta(@PathVariable int id, HttpSession session, HttpServletResponse response) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Pedido pedido = pedidoRepository.findById(id).orElse(null);

        if (pedido == null) {
            response.setStatus(404);
            return;
        }

        String perfil = (String) session.getAttribute("perfil");
        if (!"ADMIN".equals(perfil) && (usuario == null || pedido.getUsuario().getIdUsuario() != usuario.getIdUsuario())) {
            response.setStatus(403);
            return;
        }

        List<DetallePedido> detalles = detallePedidoRepository.findByPedido(pedido);
        if (detalles.isEmpty()) {
            response.setStatus(400);
            return;
        }

        double total = detalles.stream().mapToDouble(d -> d.getPrecioUnitario() * d.getCantidad()).sum();

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"Boleta-" + id + ".pdf\"");
        response.setHeader("Cache-Control", "no-cache");

        boletaService.generarBoleta(pedido, detalles, total, response);
    }

}
