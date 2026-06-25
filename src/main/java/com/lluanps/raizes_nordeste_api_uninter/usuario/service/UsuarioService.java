package com.lluanps.raizes_nordeste_api_uninter.usuario.service;

import com.lluanps.raizes_nordeste_api_uninter.exceptions.BussinessException;
import com.lluanps.raizes_nordeste_api_uninter.unidade.dto.UnidadeResponse;
import com.lluanps.raizes_nordeste_api_uninter.unidade.model.Unidade;
import com.lluanps.raizes_nordeste_api_uninter.usuario.dto.RegistroRequest;
import com.lluanps.raizes_nordeste_api_uninter.usuario.dto.UsuarioResponse;
import com.lluanps.raizes_nordeste_api_uninter.usuario.model.Usuario;
import com.lluanps.raizes_nordeste_api_uninter.usuario.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public Usuario findUsuarioById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new BussinessException("Usuario nao encontradoc com id: " + id));
    }

    public Page<UsuarioResponse> listarUsuarios(Pageable pageable) {
        return repository.findAll(pageable).map(this::toUsuario);
    }

    public UsuarioResponse atualizar(Integer id, @Valid RegistroRequest request) {
        Usuario usuarioById = findUsuarioById(id);
        usuarioById.setNome(request.getNome());
        usuarioById.setCpf(request.getCpf());
        usuarioById.setEmail(request.getEmail());
        usuarioById.setAtualizadoEm(LocalDateTime.now());
        if (request.isConsentimentoLgpd()) {
            usuarioById.setConsentimentoLgpd(true);
            usuarioById.setConsentimentoData(LocalDateTime.now());
        }

        return toUsuario(repository.save(usuarioById));
    }

    public void deletarUsuario(Integer id) {
        Usuario usuarioById = findUsuarioById(id);
        repository.deleteById(id);
    }

    public UsuarioResponse toUsuario(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .perfil(usuario.getPerfil())
                .ativo(usuario.isAtivo())
                .criadoEm(usuario.getCriadoEm())
                .build();
    }

}
