package com.cefet.centro_de_estetica.mapper;

import com.cefet.centro_de_estetica.dto.HorarioUsuarioRequestDTO;
import com.cefet.centro_de_estetica.dto.HorarioUsuarioResponseDTO;
import com.cefet.centro_de_estetica.entity.HorarioUsuario;
import com.cefet.centro_de_estetica.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class HorarioUsuarioMapper {

    public HorarioUsuarioResponseDTO toResponse(HorarioUsuario entity) {
        if (entity == null) return null;

        return new HorarioUsuarioResponseDTO(
            entity.getId(),
            entity.getDiaSemana(),
            entity.getHorario(),
            entity.getFuncionario().getId(),
            entity.getFuncionario().getNome() 
        );
    }

    public HorarioUsuario toEntity(HorarioUsuarioRequestDTO dto, Usuario funcionario) {
        if (dto == null) return null;

        HorarioUsuario entity = new HorarioUsuario();
        entity.setDiaSemana(dto.diaSemana());
        entity.setHorario(dto.horario());
        entity.setFuncionario(funcionario);

        return entity;
    }
}