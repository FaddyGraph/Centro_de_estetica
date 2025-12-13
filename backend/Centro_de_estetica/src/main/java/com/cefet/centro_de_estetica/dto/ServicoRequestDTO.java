package com.cefet.centro_de_estetica.dto;

import java.math.BigDecimal;
import java.time.LocalTime;

public record ServicoRequestDTO(
    String nome,
    String descricao,
    BigDecimal valor,
    LocalTime tempoAtendimento,
    Long idArea
) {
}