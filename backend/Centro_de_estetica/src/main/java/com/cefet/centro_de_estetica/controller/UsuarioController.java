package com.cefet.centro_de_estetica.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping; 
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cefet.centro_de_estetica.dto.UsuarioRequestDTO;
import com.cefet.centro_de_estetica.dto.UsuarioResponseDTO;
import com.cefet.centro_de_estetica.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    // --- MÉTODOS EXISTENTES ---

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criar(@RequestBody @Validated UsuarioRequestDTO dados) {
        UsuarioResponseDTO novoUsuario = service.salvar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        List<UsuarioResponseDTO> lista = service.listarTodos();
        return ResponseEntity.ok(lista);
    }
    
    @GetMapping("/servico/{idServico}")
    public ResponseEntity<List<UsuarioResponseDTO>> listarPorServico(@PathVariable Long idServico) {
        List<UsuarioResponseDTO> profissionais = service.listarProfissionaisPorServico(idServico);
        
        if (profissionais.isEmpty()) {
            return ResponseEntity.noContent().build(); 
        }
        
        return ResponseEntity.ok(profissionais);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<UsuarioResponseDTO> buscarPorEmail(@RequestParam String email) {
        return service.buscarPorEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/buscar-nome")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarPorNome(@RequestParam String nome) {
        List<UsuarioResponseDTO> resultados = service.buscarProfissionaisPorParteDoNome(nome);
        return ResponseEntity.ok(resultados);
    }
    
    @GetMapping("/verificar-email")
    public ResponseEntity<Boolean> verificarEmail(@RequestParam String email) {
        boolean existe = service.verificarSeEmailExiste(email);
        return ResponseEntity.ok(existe);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Validated UsuarioRequestDTO dados) {
        UsuarioResponseDTO atualizado = service.atualizar(id, dados);
        return ResponseEntity.ok(atualizado);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // --- NOVOS ENDPOINTS PARA A TELA DE CLIENTES ---

    // 1. Listar apenas CLIENTES (usado na tabela)
    @GetMapping("/clientes")
    public ResponseEntity<List<UsuarioResponseDTO>> listarClientes() {
        return ResponseEntity.ok(service.listarClientes());
    }

    // 2. Cadastrar Cliente via Modal (Endereço específico chamado pelo front)
    @PostMapping("/clientes")
    public ResponseEntity<UsuarioResponseDTO> cadastrarCliente(@RequestBody @Validated UsuarioRequestDTO dados) {
        // Reutilizamos o método salvar, que já trata a criação
        UsuarioResponseDTO novoCliente = service.salvar(dados);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
    }

    // 3. Alterar Status (Ativar/Desativar)
    @PatchMapping("/{id}/status")
    public ResponseEntity<UsuarioResponseDTO> alterarStatus(@PathVariable Long id) {
        return ResponseEntity.ok(service.alterarStatus(id));
    }
}