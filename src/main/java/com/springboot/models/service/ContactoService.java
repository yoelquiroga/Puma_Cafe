package com.springboot.models.service;

import java.util.List;
import java.util.Optional;

import com.springboot.models.entiys.Contacto;


public interface ContactoService {
	//lista todos los productos de la BD
		public List<Contacto> listar();
		//Guardar un nuevo producto
		public void guardar(Contacto con);
		// Obtiene un producto específico por su ID
		public Optional<Contacto> get(Integer id);
		// Actualiza los datos de un producto existente
		public void update(Contacto con);
		
		public  void eliminarContacto(Integer id);
}
