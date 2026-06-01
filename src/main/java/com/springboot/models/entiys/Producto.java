package com.springboot.models.entiys;

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

@Entity
@Table(name = "productos")
public class Producto {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idProductos; 
	
	@Column(name = "nombre")
	private String nombre;
	
	@Column(columnDefinition = "TEXT", name="descripcion")
	private String descripcion;
	
	@Column(name = "precio")
	private double precio;
	
	@Column(name = "stock")
	private int stock;
	
	@Column(name = "imagen")
	private String imagen;
	
	@Enumerated(EnumType.STRING)
	private TipoProducto tipoProducto;
	
	@ManyToOne
	@JoinColumn(name = "idCategoria")
    private CategoriaProducto categoria;
	
    @OneToMany(mappedBy = "producto")
    private List<DetallePedido> detallesPedido;
    
    public enum TipoProducto {
    	CAFE_PREPARADO, CAFE_GRANO, CAFE_MOLIDO
    }

    // Constructor vacío
	public Producto() {
	}

    // Constructor con parámetros
	public Producto(Integer idProductos, String nombre, String descripcion, double precio, int stock, String imagen,
			TipoProducto tipoProducto, CategoriaProducto categoria,
			List<DetallePedido> detallesPedido) {
		this.idProductos = idProductos;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
		this.stock = stock;
		this.imagen = imagen;
		this.tipoProducto = tipoProducto;
		this.categoria = categoria;
		this.detallesPedido = detallesPedido;
	}

    // Getters y setters
	public Integer getIdProductos() {
		return idProductos;
	}

	public void setIdProductos(Integer idProductos) {
		this.idProductos = idProductos;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public TipoProducto getTipoProducto() {
		return tipoProducto;
	}

	public void setTipoProducto(TipoProducto tipoProducto) {
		this.tipoProducto = tipoProducto;
	}

	public CategoriaProducto getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaProducto categoria) {
		this.categoria = categoria;
	}

	public List<DetallePedido> getDetallesPedido() {
		return detallesPedido;
	}

	public void setDetallesPedido(List<DetallePedido> detallesPedido) {
		this.detallesPedido = detallesPedido;
	}

	@Override
	public String toString() {
		return "Producto [idProductos=" + idProductos + ", nombre=" + nombre + ", descripcion=" + descripcion
				+ ", precio=" + precio + ", stock=" + stock + ", imagen=" + imagen + ", tipoProducto=" + tipoProducto
				+ ", categoria=" + categoria +"]";
	}

}
