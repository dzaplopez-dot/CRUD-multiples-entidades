package com.sena.database_connection.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sena.database_connection.entities.Role;
import com.sena.database_connection.repositories.RoleRepository;

@Service
public class RoleService {

    private RoleRepository repository;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    // Metodo obtener todos los usuarios

    public List<Role> obetenerTodos() {

        // Llama al método findAll de JPARepository para obtener todos los
        // registros de la tabla users.
        return this.repository.findAll();
    }


    // Metodo para crear un rol

public Role crear(Role role) {

        // Llama al método save de JPARepository para guardar el usuario
        // en la base de datos.
        return this.repository.save(role);
    }
}

    // Metodo para actualizar el rol 

    