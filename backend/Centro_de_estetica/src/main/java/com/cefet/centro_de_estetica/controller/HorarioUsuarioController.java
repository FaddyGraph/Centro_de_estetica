package com.cefet.centro_de_estetica.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cefet.centro_de_estetica.dto.HorarioUsuarioRequestDTO;
import com.cefet.centro_de_estetica.entity.HorarioUsuario;
import com.cefet.centro_de_estetica.service.HorarioUsuarioService;

@RestController
@RequestMapping("/horarios") 
public class HorarioUsuarioController {

    @Autowired
    private HorarioUsuarioService service;

    @GetMapping
    public ResponseEntity<List<HorarioUsuario>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // --- CORREÇÃO AQUI ---
    @PostMapping
    public ResponseEntity<HorarioUsuario> criar(@RequestBody HorarioUsuarioRequestDTO horario) {
        // Agora o 'horario' chega preenchido com os dados do JSON
        HorarioUsuario novoHorario = service.salvar(horario);
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