package com.springboot.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.springboot.models.entiys.Pedido;
import com.springboot.models.entiys.Pedido.EstadoPedido;
import com.springboot.models.repository.PedidoRepository;
import com.springboot.models.repository.ProductoRepository;
import com.springboot.models.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/admin/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String perfil = (String) session.getAttribute("perfil");
        if (!"ADMIN".equals(perfil)) {
            return "redirect:/login";
        }

        // KPIs
        Double totalVentas = pedidoRepository.totalVentas();
        totalVentas = totalVentas != null ? totalVentas : 0.0;

        long totalPedidos = pedidoRepository.countPedidosActivos();

        long totalClientes = usuarioRepository.countByPerfil("USER");

        long productosBajoStock = productoRepository.countByStockLessThan(10);

        model.addAttribute("totalVentas", totalVentas);
        model.addAttribute("totalPedidos", totalPedidos);
        model.addAttribute("totalClientes", totalClientes);
        model.addAttribute("productosBajoStock", productosBajoStock);

        // Ventas por mes (Chart.js) — orden cronológico real
        List<Pedido> pedidosNoCancelados = pedidoRepository.findByEstadoNot(EstadoPedido.CANCELADO);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM yyyy", new Locale("es", "ES"));
        Calendar cal = Calendar.getInstance();
        Map<Date, Double> ventasPorMesMap = new TreeMap<>();
        for (Pedido p : pedidosNoCancelados) {
            if (p.getFecha() != null) {
                cal.setTime(p.getFecha());
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                ventasPorMesMap.merge(cal.getTime(), p.getTotal(), Double::sum);
            }
        }
        List<String> mesesList = new ArrayList<>();
        List<Double> montosList = new ArrayList<>();
        for (Map.Entry<Date, Double> e : ventasPorMesMap.entrySet()) {
            mesesList.add(sdf.format(e.getKey()));
            montosList.add(e.getValue());
        }
        String mesesJson = mesesList.stream().map(m -> "\"" + m + "\"").collect(Collectors.joining(",", "[", "]"));
        String montosJson = montosList.stream().map(Object::toString).collect(Collectors.joining(",", "[", "]"));
        model.addAttribute("mesesJson", mesesJson);
        model.addAttribute("montosJson", montosJson);

        // Últimos 5 pedidos
        List<Pedido> ultimosPedidos = pedidoRepository.findTop5ByOrderByIdPedidosDesc();
        model.addAttribute("ultimosPedidos", ultimosPedidos);

        return "administrador/dashboard";
    }

    @GetMapping("/menuAdmin")
    public String menuAdmin() {
        return "redirect:/admin/dashboard";
    }
}
