package com.lluanps.raizes_nordeste_api_uninter.unidade.service;

import com.lluanps.raizes_nordeste_api_uninter.exceptions.BussinessException;
import com.lluanps.raizes_nordeste_api_uninter.unidade.dto.UnidadeRequest;
import com.lluanps.raizes_nordeste_api_uninter.unidade.dto.UnidadeResponse;
import com.lluanps.raizes_nordeste_api_uninter.unidade.model.Unidade;
import com.lluanps.raizes_nordeste_api_uninter.unidade.repository.UnidadeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UnidadeService {
    
    @Autowired
    private UnidadeRepository repository;

    public UnidadeResponse criarUnidade(@Valid UnidadeRequest request) {
        Unidade unidade = Unidade.builder()
                .nome(request.getNome())
                .endereco(request.getEndereco())
                .telefone(request.getTelefone())
                .ativo(true)
                .build();

        return getUnidadeResponse(repository.save(unidade));
    }

    public Unidade findUnidadeById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new BussinessException("Unidade nao encontrada com id: " + id));
    }

    public Page<UnidadeResponse> listarUnidades(Pageable pageable) {
        return repository.findAll(pageable).map(this::getUnidadeResponse);
    }

    public UnidadeResponse atualizar(Integer id, @Valid UnidadeRequest request) {
        Unidade unidadeById = findUnidadeById(id);
        unidadeById.setNome(request.getNome());
        unidadeById.setTelefone(request.getTelefone());
        unidadeById.setEndereco(request.getEndereco());
        return getUnidadeResponse(repository.save(unidadeById));
    }

    public UnidadeResponse desativarUnidade(Integer id) {
        Unidade unidadeById = findUnidadeById(id);
        unidadeById.setAtivo(false);

        return getUnidadeResponse(repository.save(unidadeById));
    }

    private UnidadeResponse getUnidadeResponse(Unidade unidade) {
        return UnidadeResponse.builder()
                .id(unidade.getId())
                .nome(unidade.getNome())
                .endereco(unidade.getEndereco())
                .telefone(unidade.getTelefone())
                .ativo(unidade.isAtivo())
                .criadoEm(unidade.getCriadoEm())
                .build();
    }

    public void deletarUnidade(Integer id) {
        Unidade unidadeById = findUnidadeById(id);
        repository.deleteById(id);
    }
}
