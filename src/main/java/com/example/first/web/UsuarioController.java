package com.example.first.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.first.dto.UsuarioDTO;
import com.example.first.entity.Usuarios;
import com.example.first.service.UsuarioService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/test")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<UsuarioDTO> createUser(@Valid @RequestBody Usuarios user) {
        return ResponseEntity.ok(usuarioService.createUser(user));
    }
    
    @PostMapping("/register1")
    public ResponseEntity<Void> teste(@Valid @RequestBody UsuarioDTO user) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/")
    public ResponseEntity<Page<UsuarioDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listUsers(0, 20));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getMethodName(@RequestParam Long id) {
        return ResponseEntity.ok(usuarioService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUser(@PathVariable Long id, @RequestBody Usuarios user) {
        return ResponseEntity.ok(usuarioService.updateUser(id, user));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        usuarioService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
