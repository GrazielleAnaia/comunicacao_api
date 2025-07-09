package com.luizalebs.comunicacao_api.api.dto;

import com.luizalebs.comunicacao_api.infraestructure.enums.ModoEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;

import java.util.Date;

public class ComunicacaoInDTOFixture {

    public static ComunicacaoInDTO build(
            Date dataHoraEnvio,
            Date dataHoraEvento,
            String nomeDestinatario,
            String emailDestinatario,
            String telefoneDestinatario,
            String mensagem,
            ModoEnvioEnum modoDeEnvio,
            StatusEnvioEnum statusEnvio){
        return new ComunicacaoInDTO(dataHoraEnvio, dataHoraEvento, nomeDestinatario, emailDestinatario,
                telefoneDestinatario, mensagem, modoDeEnvio, statusEnvio);
    }

}
