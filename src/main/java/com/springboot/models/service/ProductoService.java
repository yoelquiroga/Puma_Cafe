package com.springboot.models.service;

import java.util.List;



import java.util.Optional;

import com.springboot.models.entiys.Producto;

public interface ProductoService {
	//lista todos los productos de la BD
	public List<Producto> listar();
	//Guardar un nuevo producto
	public void guardar(Producto pro);
	
	// Obtiene un producto específico por su ID
	public Optional<Producto> get(Integer id);
	// Actualiza los datos de un producto existente
	public void update(Producto pro);
	//elimina un producto
	public void delete(Integer id);
	
	//lista de mejores producto
	List<Producto> obtenerMejoresProductos();
	//lista de  nuestro cafe
	List<Producto> obtenerNuestroCafe();
			
	List<Producto> obtenerProductosTienda();
	

}