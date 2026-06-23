package com.lluanps.raizes_nordeste_api_uninter.usuario.service;

import com.lluanps.raizes_nordeste_api_uninter.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

}
