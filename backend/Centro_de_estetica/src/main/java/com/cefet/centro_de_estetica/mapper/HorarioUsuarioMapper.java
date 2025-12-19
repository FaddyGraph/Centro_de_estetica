package com.cefet.centro_de_estetica.mapper;

import org.springframework.stereotype.Component;

import com.cefet.centro_de_estetica.dto.HorarioUsuarioRequestDTO;
import com.cefet.centro_de_estetica.dto.HorarioUsuarioResponseDTO;
import com.cefet.centro_de_estetica.entity.HorarioUsuario;
import com.cefet.centro_de_estetica.entity.Usuario;

@Component
public class HorarioUsuarioMapper {

    public HorarioUsuarioResponseDTO toResponse(HorarioUsuario entity) {
        if (entity == null) return null;

        return new HorarioUsuarioResponseDTO(entity);
    }

    public HorarioUsuario toEntity(HorarioUsuarioRequestDTO dto, Usuario funcionario) {
        if (dto == null) return null;

        HorarioUsuario entity = new HorarioUsuario();
        entity.setDiaSemana(dto.getDiaSemana());
        entity.setHorarioInicio(dto.getHorarioInicio());
        entity.setHorarioFim(dto.getHorarioFim());
        entity.setFuncionario(funcionario);

        return entity;
    }
}