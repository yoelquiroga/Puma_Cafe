package com.springboot.models.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.springboot.models.entiys.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer>{
	// Filtrar por tipo de producto (Tienda)
    List<Producto> findByTipoProductoIn(List<String> tipos);

    List<Producto> findByCategoriaIdCategoria(Integer id_Categoria);

    long countByStockLessThan(int stock);
}
