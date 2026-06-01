package com.springboot;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.springboot.models.entiys.Usuario;
import com.springboot.models.repository.UsuarioRepository;

@Component
@Order(1)
public class PasswordMigrationComponent implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(PasswordMigrationComponent.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        for (Usuario u : usuarios) {
            String pwd = u.getContraseña();
            if (pwd != null && !pwd.startsWith("$2a$")) {
                u.setContraseña(passwordEncoder.encode(pwd));
                usuarioRepository.save(u);
                LOG.info("Migrated usuario id={} ({}) to BCrypt", u.getIdUsuario(), u.getCorreo());
            }
        }
    }
}
