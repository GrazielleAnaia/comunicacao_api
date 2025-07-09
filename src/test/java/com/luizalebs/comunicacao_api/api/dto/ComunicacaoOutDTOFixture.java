package com.luizalebs.comunicacao_api.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.luizalebs.comunicacao_api.infraestructure.enums.ModoEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;

import java.util.Date;

public class ComunicacaoOutDTOFixture {

    public static ComunicacaoOutDTO build(Long id,
                                          Date dataHoraEnvio,
                                          Date dataHoraEvento,
                                          String nomeDestinatario,
                                          String emailDestinatario,
                                          String telefoneDestinatario,
                                          String mensagem,
                                          ModoEnvioEnum modoDeEnvio,
                                          StatusEnvioEnum statusEnvio) {
        return new ComunicacaoOutDTO(id, dataHoraEnvio, dataHoraEvento, nomeDestinatario,
                emailDestinatario, telefoneDestinatario, mensagem, modoDeEnvio, statusEnvio);
    }
}
