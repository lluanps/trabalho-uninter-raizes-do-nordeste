package com.lluanps.raizes_nordeste_api_uninter.logs.service;

import com.lluanps.raizes_nordeste_api_uninter.logs.model.LogAuditoria;
import com.lluanps.raizes_nordeste_api_uninter.logs.repository.LogAuditoriaRepository;
import com.lluanps.raizes_nordeste_api_uninter.usuario.model.Usuario;
import com.lluanps.raizes_nordeste_api_uninter.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogAuditoriaService {

    private final LogAuditoriaRepository logAuditoriaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrar(String emailUsuario, String acao, String entidade, Integer entidadeId, String detalhes) {
        try {
            Usuario usuario = null;
            if (emailUsuario != null) {
                usuario = usuarioRepository.findByEmail(emailUsuario).orElse(null);
            }

            LogAuditoria auditoria = LogAuditoria.builder()
                    .usuario(usuario)
                    .acao(acao)
                    .entidade(entidade)
                    .entidadeId(entidadeId)
                    .detalhes(detalhes)
                    .build();

            logAuditoriaRepository.save(auditoria);
        } catch (Exception e) {
            log.error("Falha ao registrar auditoria: acao={} entidade={} id={} — {}",
                    acao, entidade, entidadeId, e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrar(String acao, String entidade, Integer entidadeId, String detalhes) {
        registrar(null, acao, entidade, entidadeId, detalhes);
    }

}
