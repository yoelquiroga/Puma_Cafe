package com.springboot.models.serviceImplements;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.models.entiys.Contacto;
import com.springboot.models.repository.ContactoRepository;
import com.springboot.models.service.ContactoService;

@Service
public class ContactoServiceImpl implements ContactoService{
	
	@Autowired
	private ContactoRepository contactoRepository;

	@Override
	public List<Contacto> listar() {
		return contactoRepository.findAll();
	}

	@Override
	public void guardar(Contacto con) {
		contactoRepository.save(con);
		
	}

	@Override
	public Optional<Contacto> get(Integer id) {
		return contactoRepository.findById(id);
	}

	@Override
	public void update(Contacto con) {
		contactoRepository.save(con);
		
	}

	@Override
	public void eliminarContacto(Integer id) {
		contactoRepository.deleteById(id);
		
	}

	
	
	


}
