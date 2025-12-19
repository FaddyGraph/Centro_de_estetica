package com.cefet.centro_de_estetica.dto;

import java.time.LocalTime;
import com.cefet.centro_de_estetica.enums.DiaDaSemana;
import com.fasterxml.jackson.annotation.JsonFormat;

public class HorarioUsuarioRequestDTO {

    private Long idFuncionario;
    private DiaDaSemana diaSemana;

    @JsonFormat(pattern = "HH:mm") 
    private LocalTime horarioInicio;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime horarioFim;

    // Getters e Setters
    public Long getIdFuncionario() { return idFuncionario; }
    public void setIdFuncionario(Long idFuncionario) { this.idFuncionario = idFuncionario; }

    public DiaDaSemana getDiaSemana() { return diaSemana; }
    public void setDiaSemana(DiaDaSemana diaSemana) { this.diaSemana = diaSemana; }

    public LocalTime getHorarioInicio() { return horarioInicio; }
    public void setHorarioInicio(LocalTime horarioInicio) { this.horarioInicio = horarioInicio; }

    public LocalTime getHorarioFim() { return horarioFim; }
    public void setHorarioFim(LocalTime horarioFim) { this.horarioFim = horarioFim; }
}