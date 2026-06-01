package com.springboot.models.entiys;

import jakarta.persistence.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "detalle_pedidos")
public class DetallePedido {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_det_Pedidos")
    private Integer idDetPedidos;


    @ManyToOne
    @JoinColumn(name = "id_Pedidos")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "id_Productos")
    private Producto producto;

    @Column(name = "cantidad")
    private int cantidad;
    
    @Column(name = "precio_unitario")
    private double precioUnitario;

    @Column(name = "total")
    private double total;

	public DetallePedido() {
	}


	public DetallePedido(Integer idDetPedidos, Pedido pedido, Producto producto, int cantidad, double precioUnitario, double total) {
		this.idDetPedidos = idDetPedidos;
		this.pedido = pedido;
		this.producto = producto;
		this.cantidad = cantidad;
		this.precioUnitario = precioUnitario;
		this.total = total;
	}


	public DetallePedido(Integer idDetPedidos, Pedido pedido, Producto producto, int cantidad, double precioUnitario) {
		this.idDetPedidos = idDetPedidos;
		this.pedido = pedido;
		this.producto = producto;
		this.cantidad = cantidad;
		this.precioUnitario = precioUnitario;
	}


	public Integer getIdDetPedidos() {
		return idDetPedidos;
	}


	public void setIdDetPedidos(Integer idDetPedidos) {
		this.idDetPedidos = idDetPedidos;
	}


	public Pedido getPedido() {
		return pedido;
	}


	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}


	public Producto getProducto() {
		return producto;
	}


	public void setProducto(Producto producto) {
		this.producto = producto;
	}


	public int getCantidad() {
		return cantidad;
	}


	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}


	public double getPrecioUnitario() {
		return precioUnitario;
	}


	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}


	public double getTotal() {
		return total;
	}


	public void setTotal(double total) {
		this.total = total;
	}


    
}
