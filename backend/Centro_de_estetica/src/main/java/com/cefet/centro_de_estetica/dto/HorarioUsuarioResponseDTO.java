package com.cefet.centro_de_estetica.dto;

import java.time.LocalTime;
import com.cefet.centro_de_estetica.entity.HorarioUsuario;
import com.cefet.centro_de_estetica.enums.DiaDaSemana;
import com.fasterxml.jackson.annotation.JsonFormat; // Importante!

public class HorarioUsuarioResponseDTO {

    private Long id;
    private DiaDaSemana diaSemana;
    
    @JsonFormat(pattern = "HH:mm") // Garante que saia "08:00" para o Angular
    private LocalTime horarioInicio;
    
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horarioFim;
    
    private Long idFuncionario;
    private String nomeFuncionario;

    public HorarioUsuarioResponseDTO() {
    }

    public HorarioUsuarioResponseDTO(HorarioUsuario horarioUsuario) {
        this.id = horarioUsuario.getId();
        this.diaSemana = horarioUsuario.getDiaSemana();
        
        this.horarioInicio = horarioUsuario.getHorarioInicio();
        this.horarioFim = horarioUsuario.getHorarioFim();
        if (horarioUsuario.getFuncionario() != null) {
            this.idFuncionario = horarioUsuario.getFuncionario().getId();
            this.nomeFuncionario = horarioUsuario.getFuncionario().getNome();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public DiaDaSemana getDiaSemana() { return diaSemana; }
    public void setDiaSemana(DiaDaSemana diaSemana) { this.diaSemana = diaSemana; }
    public LocalTime getHorarioInicio() { return horarioInicio; }
    public void setHorarioInicio(LocalTime horarioInicio) { this.horarioInicio = horarioInicio; }
    public LocalTime getHorarioFim() { return horarioFim; }
    public void setHorarioFim(LocalTime horarioFim) { this.horarioFim = horarioFim; }
    public Long getIdFuncionario() { return idFuncionario; }
    public void setIdFuncionario(Long idFuncionario) { this.idFuncionario = idFuncionario; }
    public String getNomeFuncionario() { return nomeFuncionario; }
    public void setNomeFuncionario(String nomeFuncionario) { this.nomeFuncionario = nomeFuncionario; }
}