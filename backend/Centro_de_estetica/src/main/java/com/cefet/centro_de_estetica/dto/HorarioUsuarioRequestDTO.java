package com.cefet.centro_de_estetica.dto;

import java.time.LocalTime;

public record HorarioUsuarioRequestDTO(
    Integer diaSemana,
    LocalTime horario,
    Long idFuncionario
) {}