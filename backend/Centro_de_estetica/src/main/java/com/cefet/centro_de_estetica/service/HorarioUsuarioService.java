package com.cefet.centro_de_estetica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cefet.centro_de_estetica.dto.HorarioUsuarioRequestDTO;
import com.cefet.centro_de_estetica.entity.HorarioUsuario;
import com.cefet.centro_de_estetica.entity.Usuario;
import com.cefet.centro_de_estetica.repository.HorarioUsuarioRepository;
import com.cefet.centro_de_estetica.repository.UsuarioRepository;

@Service
public class HorarioUsuarioService {

    @Autowired
    private HorarioUsuarioRepository repository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<HorarioUsuario> listarTodos() {
        return repository.findAll();
    }

    public HorarioUsuario salvar(HorarioUsuarioRequestDTO dto) {
        
        Usuario funcionario = usuarioRepository.findById(dto.idFuncionario())
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado com id: " + dto.idFuncionario()));

        HorarioUsuario horario = new HorarioUsuario();
        horario.setDiaSemana(dto.diaSemana());
        horario.setHorarioInicio(dto.horarioInicio());
        horario.setHorarioFim(dto.horarioFim());
        
        horario.setFuncionario(funcionario);

        return repository.save(horario);
    }

    public List<HorarioUsuario> listarPorFuncionario(Long idFuncionario) {
        return repository.findByFuncionarioId(idFuncionario);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}