package com.example.first.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.first.dto.UsuarioDTO;
import com.example.first.entity.Usuarios;
import com.example.first.repository.UsuarioRepository;

import jakarta.validation.Valid;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioDTO createUser(@Valid Usuarios user) {
        return toDTO(usuarioRepository.save(user));
    }

    public Page<UsuarioDTO> listUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return usuarioRepository.findAll(pageable).map(this::toDTO);
    }

    public UsuarioDTO getUserById(Long id) {
        Usuarios user = findUserById(id);
        return toDTO(user);
    }

    @Transactional
    public UsuarioDTO updateUser(Long id, Usuarios updateUser){
        Usuarios user = findUserById(id);
        return toDTO(usuarioRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id){
        Usuarios user = findUserById(id);
        usuarioRepository.delete(user);
    }

    private Usuarios findUserById(Long id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }


    private UsuarioDTO toDTO(Usuarios usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        BeanUtils.copyProperties(usuario, dto);
        return dto;
    }
}
