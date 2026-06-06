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

import com.sena.database_connection.dtos.ProfileDto;
import com.sena.database_connection.entities.Profile;
import com.sena.database_connection.entities.User;
import com.sena.database_connection.services.ProfileService;
import com.sena.database_connection.services.UserService;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    // Servicios necesarios para Profile y para buscar el User asociado
    private ProfileService service;
    private UserService userService;

    // Constructor con inyección de dependencias
    public ProfileController(ProfileService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    // Endpoint para obtener todos los profiles
    @GetMapping
    public List<Profile> get() {
        return this.service.obtenerTodos();
    }

    // Endpoint para obtener un profile por id
    @GetMapping("/{id}")
    public ResponseEntity<Profile> getById(@PathVariable("id") Long id) {
        Optional<Profile> profile = this.service.porId(id);

        if (profile.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.status(200).body(profile.get());
    }

    // Endpoint para crear un profile
    @PostMapping
    public ResponseEntity<Profile> create(@RequestBody ProfileDto body) {

        // Se busca el usuario al que pertenece el profile
        Optional<User> userFound = this.userService.porId(body.getUserId());

        if (userFound.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }

        Profile profile = new Profile();
        profile.setUsername(body.getUsername());
        profile.setDescription(body.getDescription());
        profile.setUser(userFound.get()); // Se asocia el User encontrado

        return ResponseEntity.status(201).body(this.service.crear(profile));
    }

    // Endpoint para actualizar un profile existente
    @PutMapping("/{id}")
    public ResponseEntity<Profile> update(@PathVariable("id") Long id, @RequestBody ProfileDto body) {

        // Se busca el usuario asociado
        Optional<User> userFound = this.userService.porId(body.getUserId());

        if (userFound.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }

        Profile profile = new Profile();
        profile.setId(id);
        profile.setUsername(body.getUsername());
        profile.setDescription(body.getDescription());
        profile.setUser(userFound.get());

        Profile profileUpdated = this.service.actualizar(profile);

        if (profileUpdated == null) {
            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.status(200).body(profileUpdated);
    }

    // Endpoint para eliminar un profile por id
    @DeleteMapping("/{id}")
    public ResponseEntity<Profile> delete(@PathVariable("id") Long id) {

        Profile profileDeleted = this.service.eliminar(id);

        if (profileDeleted == null) {
            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.status(200).body(profileDeleted);
    }
}
