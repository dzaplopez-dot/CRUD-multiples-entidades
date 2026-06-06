package com.sena.database_connection.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sena.database_connection.dtos.RoleDto;
import com.sena.database_connection.entities.Role;
import com.sena.database_connection.services.RoleService;

@RestController
@RequestMapping("/role")
public class RoleController {

    // Servicio encargado de la lógica de roles
    private RoleService service;

    // Constructor con inyección de dependencias
    public RoleController(RoleService service) {
        this.service = service;
    }

    // Endpoint para obtener todos los roles
    @GetMapping
    public List<Role> get() {
        return this.service.obtenerTodos();
    }

    // Endpoint para obtener un role por id
    @GetMapping("/{id}")
    public ResponseEntity<Role> getById(@PathVariable("id") Long id) {
        Optional<Role> role = this.service.porId(id);

        if (role.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.status(200).body(role.get());
    }

    // Endpoint para crear un role
    @PostMapping
    public Role create(@RequestBody RoleDto body) {

        Role role = new Role();
        role.setName(body.getName());

        return this.service.crear(role);
    }

    // Endpoint para actualizar un role existente
    @PutMapping("/{id}")
    public ResponseEntity<Role> update(@PathVariable("id") Long id, @RequestBody RoleDto body) {

        Role role = new Role();
        role.setId(id);
        role.setName(body.getName());

        Role roleUpdated = this.service.actualizar(role);

        if (roleUpdated == null) {
            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.status(200).body(roleUpdated);
    }

    // Endpoint para eliminar un role por id
    @DeleteMapping("/{id}")
    public ResponseEntity<Role> delete(@PathVariable("id") Long id) {

        Role roleDeleted = this.service.eliminar(id);

        if (roleDeleted == null) {
            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.status(200).body(roleDeleted);
    }
}
