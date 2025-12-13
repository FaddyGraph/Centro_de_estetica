package com.cefet.centro_de_estetica.mapper;

import com.cefet.centro_de_estetica.dto.ServicoRequestDTO;
import com.cefet.centro_de_estetica.dto.ServicoResponseDTO;
import com.cefet.centro_de_estetica.entity.Area;
import com.cefet.centro_de_estetica.entity.Servico;
import org.springframework.stereotype.Component;

@Component
public class ServicoMapper {

    public ServicoResponseDTO toResponse(Servico servico) {
        if (servico == null) {
            return null;
        }

        return new ServicoResponseDTO(
            servico.getId(),
            servico.getNome(),
            servico.getDescricao(),
            servico.getValor(),
            servico.getTempoAtendimento(),
            servico.getArea().getNome() 
        );
    }

    public Servico toEntity(ServicoRequestDTO dto, Area area) {
        if (dto == null) {
            return null;
        }

        Servico servico = new Servico();
        servico.setNome(dto.nome());
        servico.setDescricao(dto.descricao());
        servico.setValor(dto.valor());
        servico.setTempoAtendimento(dto.tempoAtendimento());
        
        servico.setArea(area); 

        return servico;
    }
}