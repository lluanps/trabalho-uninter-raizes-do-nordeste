package com.lluanps.raizes_nordeste_api_uninter.autentificacao;

import com.lluanps.raizes_nordeste_api_uninter.enums.Perfil;
import com.lluanps.raizes_nordeste_api_uninter.exceptions.BussinessException;
import com.lluanps.raizes_nordeste_api_uninter.security.JwtService;
import com.lluanps.raizes_nordeste_api_uninter.security.dto.LoginRequest;
import com.lluanps.raizes_nordeste_api_uninter.security.dto.TokenResponse;
import com.lluanps.raizes_nordeste_api_uninter.usuario.dto.RegistroRequest;
import com.lluanps.raizes_nordeste_api_uninter.usuario.dto.UsuarioResponse;
import com.lluanps.raizes_nordeste_api_uninter.usuario.model.Usuario;
import com.lluanps.raizes_nordeste_api_uninter.usuario.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Transactional
    public UsuarioResponse registrar(RegistroRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BussinessException("Email ja existe em nosso sistema, email: " + request.getEmail());
        }

        if (usuarioRepository.existsByCpf(request.getCpf())) {
            throw new BussinessException("CPF ja cadastrado em nosso sistema, cpf: " + request.getCpf());
        }

        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .cpf(request.getCpf())
                .perfil(Perfil.CLIENTE)
                .consentimentoLgpd(request.isConsentimentoLgpd())
                .consentimentoData(
                        request.isConsentimentoLgpd() ? LocalDateTime.now() : null)
                .ativo(true)
                .build();

        usuario = usuarioRepository.save(usuario);

        log.info( "Usuário criado. email={}, id={}",
                usuario.getEmail(),
                usuario.getId()
        );

        return toUsuarioResponse(usuario);
    }

    public TokenResponse login(LoginRequest request) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getSenha())
                );

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

        String token = jwtService.gerarToken(userDetails);

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado")
                );

        log.info("Login realizado. email={}, id={}",
                usuario.getEmail(),
                usuario.getId()
        );

        return TokenResponse.builder()
                .token(token)
                .tipo("Bearer")
                .expiracao(jwtExpiration)
                .usuario(toUsuarioResponse(usuario))
                .build();
    }

    private UsuarioResponse toUsuarioResponse(Usuario usuario) {
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
