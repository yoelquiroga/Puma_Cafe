package com.springboot.models.entiys;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "pedidos")
public class Pedido {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_Pedidos")
	private int idPedidos;

    @ManyToOne
    @JoinColumn(name = "id_Usuario")
    private Usuario usuario;

    @Column(name = "total")
    private double total;

    @Column(name = "metodo_pago")
    @Enumerated(EnumType.STRING)
    private MetodoPago metodoPago;

    @Column(name = "direccion_entrega")
    private String direccionEntrega;

    @Column(name = "estado")
    @Enumerated(EnumType.STRING)
    private EstadoPedido estado;

    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;

    @OneToMany(mappedBy = "pedido")
    private List<DetallePedido> detallesPedido;

    public enum MetodoPago {
        EFECTIVO, TARJETA_CREDITO
    }

    public enum EstadoPedido {
        PENDIENTE, EN_RUTA, ENTREGADO, CANCELADO
    }

    public Pedido() {
    }

    public Pedido(int idPedidos, Usuario usuario, double total, MetodoPago metodoPago, String direccionEntrega,
                  EstadoPedido estado, Date fecha, List<DetallePedido> detallesPedido) {
        this.idPedidos = idPedidos;
        this.usuario = usuario;
        this.total = total;
        this.metodoPago = metodoPago;
        this.direccionEntrega = direccionEntrega;
        this.estado = estado;
        this.fecha = fecha;
        this.detallesPedido = detallesPedido;
    }

    public int getIdPedidos() {
        return idPedidos;
    }

    public void setIdPedidos(int idPedidos) {
        this.idPedidos = idPedidos;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        if (total < 0) {
            throw new IllegalArgumentException("El total no puede ser negativo.");
        }
        this.total = total;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<DetallePedido> getDetallesPedido() {
        return detallesPedido;
    }

    public void setDetallesPedido(List<DetallePedido> detallesPedido) {
        this.detallesPedido = detallesPedido;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "idPedidos=" + idPedidos +
                ", usuario=" + (usuario != null ? usuario.getNombre() : "null") +
                ", total=" + total +
                ", metodoPago=" + metodoPago +
                ", direccionEntrega='" + direccionEntrega + '\'' +
                ", estado=" + estado +
                ", fecha=" + fecha +
                ", detallesPedido=" + detallesPedido +
                '}';
    }
}
