package com.luizalebs.comunicacao_api.infraestructure.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusEnvioEnum {
    PENDENTE,
    ALTERADO,
    ENVIADO,
    CANCELADO;
}
