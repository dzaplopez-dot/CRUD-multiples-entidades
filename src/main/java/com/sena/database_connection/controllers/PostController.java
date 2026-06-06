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

import com.sena.database_connection.dtos.PostDto;
import com.sena.database_connection.entities.Post;
import com.sena.database_connection.entities.User;
import com.sena.database_connection.services.PostService;
import com.sena.database_connection.services.UserService;

@RestController
@RequestMapping("/post")
public class PostController {

    // Servicios necesarios para Post y para buscar el User asociado
    private PostService service;
    private UserService userService;

    // Constructor con inyección de dependencias
    public PostController(PostService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    // Endpoint para obtener todos los posts
    @GetMapping
    public List<Post> get() {
        return this.service.obtenerTodos();
    }

    // Endpoint para obtener un post por id
    @GetMapping("/{id}")
    public ResponseEntity<Post> getById(@PathVariable("id") Long id) {
        Optional<Post> post = this.service.porId(id);

        if (post.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.status(200).body(post.get());
    }

    // Endpoint para crear un post
    @PostMapping
    public ResponseEntity<Post> create(@RequestBody PostDto body) {

        // Se busca el usuario al que pertenece el post
        Optional<User> userFound = this.userService.porId(body.getUserId());

        if (userFound.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }

        Post post = new Post();
        post.setTitle(body.getTitle());
        post.setDescription(body.getDescription());
        post.setLikes(body.getLikes());
        post.setUser(userFound.get()); // Se asocia el User encontrado

        return ResponseEntity.status(201).body(this.service.crear(post));
    }

    // Endpoint para actualizar un post existente
    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@PathVariable("id") Long id, @RequestBody PostDto body) {

        // Se busca el usuario asociado
        Optional<User> userFound = this.userService.porId(body.getUserId());

        if (userFound.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }

        Post post = new Post();
        post.setId(id);
        post.setTitle(body.getTitle());
        post.setDescription(body.getDescription());
        post.setLikes(body.getLikes());
        post.setUser(userFound.get());

        Post postUpdated = this.service.actualizar(post);

        if (postUpdated == null) {
            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.status(200).body(postUpdated);
    }

    // Endpoint para eliminar un post por id
    @DeleteMapping("/{id}")
    public ResponseEntity<Post> delete(@PathVariable("id") Long id) {

        Post postDeleted = this.service.eliminar(id);

        if (postDeleted == null) {
            return ResponseEntity.status(404).body(null);
        }

        return ResponseEntity.status(200).body(postDeleted);
    }
}
