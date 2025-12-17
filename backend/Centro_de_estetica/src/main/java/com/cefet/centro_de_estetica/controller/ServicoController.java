package com.cefet.centro_de_estetica.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cefet.centro_de_estetica.dto.ServicoRequestDTO;
import com.cefet.centro_de_estetica.dto.ServicoResponseDTO;
import com.cefet.centro_de_estetica.service.ServicoService;


@RestController
@RequestMapping("/servicos") 
@CrossOrigin("*") 
public class ServicoController {

    private final ServicoService service;

    public ServicoController(ServicoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ServicoResponseDTO> criar(@RequestBody @Validated ServicoRequestDTO dados) {
        try {
            ServicoResponseDTO novoServico = service.salvar(dados);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoServico);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build(); 
        }
    }

    @GetMapping
    public ResponseEntity<List<ServicoResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }
    
    // Rota ajustada de /area para /por-area para bater com o Frontend
    @GetMapping("/por-area/{id}")
    public ResponseEntity<List<ServicoResponseDTO>> listarPelaArea(@PathVariable Long id) {
        return ResponseEntity.ok(service.listarPorArea(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponseDTO> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Validated ServicoRequestDTO dados) {
        try {
            ServicoResponseDTO atualizado = service.atualizar(id, dados);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            service.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}