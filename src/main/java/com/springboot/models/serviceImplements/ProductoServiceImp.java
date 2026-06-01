package com.springboot.models.serviceImplements;

import java.util.Arrays;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.models.entiys.Producto;
import com.springboot.models.repository.ProductoRepository;
import com.springboot.models.service.ProductoService;

@Service
public class ProductoServiceImp implements ProductoService{
	
	@Autowired
	private ProductoRepository repository;

	@Override
	public List<Producto> listar() {
		return repository.findAll();
	}

	@Override
	public void guardar(Producto pro) {
		repository.save(pro);
		
	}

	@Override
	public Optional<Producto> get(Integer id) {
		return repository.findById(id);
	}

	@Override
	public void update(Producto pro) {
		repository.save(pro);
		
	}

	@Override
	public void delete(Integer id) {
		repository.deleteById(id);
		
	}
	
	public List<Producto> obtenerMejoresProductos() {
	    // Aquí seleccionas productos manualmente por sus IDs
	    return repository.findAllById(Arrays.asList(5, 6, 7, 4, 9, 1, 10, 8)); 
	}

	@Override
	public List<Producto> obtenerNuestroCafe() {
		return repository.findAllById(Arrays.asList(14, 15, 16, 17, 18, 19, 20, 21));
	}
	
	@Override
	public List<Producto> obtenerProductosTienda() {
	    // Obtener productos de la tienda (café en grano o molido)
	    return repository.findByTipoProductoIn(Arrays.asList("CAFE_GRANO", "CAFE_MOLIDO"));
	}
	
	// Obtener productos de la categoría Café Clásico (idCategoria = 1)
    public List<Producto> obtenerProductosCafeClasico() {
        return repository.findByCategoriaIdCategoria(1);
    }

    // Obtener productos de la categoría Café Especial (idCategoria = 2)
    public List<Producto> obtenerProductosCafeEspecial() {
        return repository.findByCategoriaIdCategoria(2);
    }

    // Obtener productos de la categoría Café Frío (idCategoria = 3)
    public List<Producto> obtenerProductosCafeFrio() {
        return repository.findByCategoriaIdCategoria(3);
    }
}
