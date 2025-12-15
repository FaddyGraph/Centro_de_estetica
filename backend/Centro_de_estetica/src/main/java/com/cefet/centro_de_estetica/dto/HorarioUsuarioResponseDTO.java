package com.cefet.centro_de_estetica.dto;

import java.time.LocalTime;

import com.cefet.centro_de_estetica.entity.HorarioUsuario;
import com.cefet.centro_de_estetica.enums.DiaDaSemana;

public class HorarioUsuarioResponseDTO {

    private Long id;
    private DiaDaSemana diaSemana;
    private LocalTime horarioInicio;
    private LocalTime horarioFim;
    private Long idFuncionario;
    private String nomeFuncionario;

    public HorarioUsuarioResponseDTO() {
    }

    public HorarioUsuarioResponseDTO(HorarioUsuario horarioUsuario) {
        this.id = horarioUsuario.getId();
        this.diaSemana = horarioUsuario.getDiaSemana();
        
        this.idFuncionario = horarioUsuario.getFuncionario().getId();
        this.nomeFuncionario = horarioUsuario.getFuncionario().getNome();
    }

    public HorarioUsuarioResponseDTO(Long id, DiaDaSemana diaSemana, LocalTime horarioInicio, LocalTime horarioFim, Long idFuncionario, String nomeFuncionario) {
        this.id = id;
        this.diaSemana = diaSemana;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
        this.idFuncionario = idFuncionario;
        this.nomeFuncionario = nomeFuncionario;
    }


    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public DiaDaSemana getDiaSemana() { 
        return diaSemana; 
    }

    public void setDiaSemana(DiaDaSemana diaSemana) { 
        this.diaSemana = diaSemana; 
    }


    public LocalTime getHorarioInicio() {
		return horarioInicio;
	}

	public void setHorarioInicio(LocalTime horarioInicio) {
		this.horarioInicio = horarioInicio;
	}

	public LocalTime getHorarioFim() {
		return horarioFim;
	}

	public void setHorarioFim(LocalTime horarioFim) {
		this.horarioFim = horarioFim;
	}

	public Long getIdFuncionario() { 
        return idFuncionario; 
    }

    public void setIdFuncionario(Long idFuncionario) { 
        this.idFuncionario = idFuncionario; 
    }

    public String getNomeFuncionario() { 
        return nomeFuncionario; 
    }
    
    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario; 
    }
}