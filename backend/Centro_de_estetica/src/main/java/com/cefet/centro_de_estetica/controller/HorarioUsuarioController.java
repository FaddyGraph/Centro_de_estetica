package com.cefet.centro_de_estetica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // Importa tudo (incluindo CrossOrigin)

import com.cefet.centro_de_estetica.dto.HorarioUsuarioRequestDTO;
import com.cefet.centro_de_estetica.entity.HorarioUsuario;
import com.cefet.centro_de_estetica.service.HorarioUsuarioService;

@RestController
@RequestMapping("/horarios")
@CrossOrigin(origins = "*") 
public class HorarioUsuarioController {

    @Autowired
    private HorarioUsuarioService service;

    @GetMapping
    public ResponseEntity<List<HorarioUsuario>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PostMapping
    public ResponseEntity<HorarioUsuario> criar(@RequestBody HorarioUsuarioRequestDTO dto) {
        HorarioUsuario novoHorario = service.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoHorario);
    }
    
    @GetMapping("/funcionario/{idFuncionario}")
    public ResponseEntity<List<HorarioUsuario>> listarPorFuncionario(@PathVariable Long idFuncionario) {
        return ResponseEntity.ok(service.listarPorFuncionario(idFuncionario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}