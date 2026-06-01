package com.springboot.controllers;

import java.io.IOException;



import java.util.List;
import java.util.Optional;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springboot.models.entiys.Producto;
import com.springboot.models.service.UploadFileService;
import com.springboot.models.serviceImplements.ProductoServiceImp;

@Controller
public class ProductoController {

	private final Logger LOGGER = LoggerFactory.getLogger(ProductoController.class);

	@Autowired
	private ProductoServiceImp service;

	@Autowired
	private UploadFileService upload;

	@GetMapping("/listaProducto")
	public String listado(Model modelo) {
		List<Producto> lista = service.listar();
		modelo.addAttribute("listaProductos", lista);
		return "administrador/productos";
	}

	@GetMapping("/nuevoProducto")
	public String nuevo(Model modelo) {
		modelo.addAttribute("producto", new Producto());
		return "administrador/nuevo";
	}

	@PostMapping("/guardarProducto")
	public String guardar(@ModelAttribute Producto pro, @RequestParam("img") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
		 try {
		        LOGGER.info("Este es el objeto producto {}", pro);

		        // imagen
		        if (pro.getIdProductos() == null) { // cuando se crea un producto
		            String nombreImagen = upload.saveImage(file);
		            pro.setImagen(nombreImagen); // Guardar el nombre de la imagen en la base de datos
		        }
		        service.guardar(pro);
		        redirectAttributes.addFlashAttribute("success", "Producto registrado correctamente");
		    } catch (Exception e) {
		        redirectAttributes.addFlashAttribute("error", "Error al agregar el producto");
		    }
		return "redirect:/listaProducto";
	}

	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model modelo) {
		Optional<Producto> optionalProducto = service.get(id);
		if (optionalProducto.isEmpty()) {
			return "redirect:/listaProducto?error=Producto+no+encontrado";
		}
		Producto producto = optionalProducto.get();

		LOGGER.info("Producto buscado: {}", producto);
		modelo.addAttribute("producto", producto);

		return "administrador/editar";
	}

	@PostMapping("/update")
	public String update(Producto producto, @RequestParam("img") MultipartFile file, RedirectAttributes redirectAttributes ) throws IOException {
		try {
			Optional<Producto> optP = service.get(producto.getIdProductos());
			if (optP.isEmpty()) {
				redirectAttributes.addFlashAttribute("error", "Producto no encontrado");
				return "redirect:/listaProducto";
			}
			Producto p = optP.get();

	        if (file.isEmpty()) { 
	            producto.setImagen(p.getImagen());
	        } else {
	            if (p.getImagen() != null && !p.getImagen().equals("default.jpg")) {
	                upload.deleteImage(p.getImagen());
	            }

	            String nombreImagen = upload.saveImage(file);
	            producto.setImagen(nombreImagen);
	        }
	        service.update(producto);
	        redirectAttributes.addFlashAttribute("success", "Producto actualizado correctamente");
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("error", "Error al actualizar el producto");
	    }
	    return "redirect:/listaProducto";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		try {
			Optional<Producto> optP = service.get(id);
			if (optP.isEmpty()) {
				redirectAttributes.addFlashAttribute("error", "Producto no encontrado");
				return "redirect:/listaProducto";
			}
			Producto p = optP.get();

	        if (!p.getImagen().equals("default.jpg")) {
	            upload.deleteImage(p.getImagen());
	        }
	        service.delete(id);
	        redirectAttributes.addFlashAttribute("success", "Producto eliminado correctamente");
	    } catch (Exception e) {
	        redirectAttributes.addFlashAttribute("error", "Error al eliminar el producto");
	    }
	    return "redirect:/listaProducto";
	}
		
}