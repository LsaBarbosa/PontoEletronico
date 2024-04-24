package com.santanna.pontoeletronico.service;

import com.santanna.pontoeletronico.domain.entity.Usuario;
import com.santanna.pontoeletronico.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {


    @Autowired
    private UsuarioRepository repository;

    public Usuario getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public List<Usuario> getAll() {
        return repository.findAll();
    }

    public Usuario create(Usuario usuario) {

        if (repository.existsByName(usuario.getName())) {
            throw new RuntimeException("Já existe um usuário com este nome.");
        }
        return repository.save(usuario);
    }

    public Usuario update(Long id, Usuario usuario) {
        Usuario existingUsuario = getById(id);
        existingUsuario.setName(usuario.getName());
        // Outros campos que podem ser atualizados
        return repository.save(existingUsuario);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
