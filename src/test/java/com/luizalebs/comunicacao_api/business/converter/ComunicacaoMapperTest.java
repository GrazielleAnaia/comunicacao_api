package com.luizalebs.comunicacao_api.business.converter;


import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTOFixture;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTOFixture;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import com.luizalebs.comunicacao_api.infraestructure.enums.ModoEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static com.luizalebs.comunicacao_api.infraestructure.enums.ModoEnvioEnum.EMAIL;
import static com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum.PENDENTE;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ComunicacaoMapperTest {

    ComunicacaoMapper comunicacaoMapper;
    ComunicacaoOutDTO comunicacaoOutDTO;
    ComunicacaoInDTO comunicacaoInDTO;
    ComunicacaoEntity comunicacaoEntity;

    private static final Date DATA_HORA_ENVIO = new Date(2025, 05, 29);
    private static final Date DATA_HORA_EVENTO = new Date(2025, 06, 29);
    private static final ModoEnvioEnum MODO_DE_ENVIO = EMAIL;
    private static final StatusEnvioEnum STATUS_ENVIO = PENDENTE;

    @BeforeEach
    public void setup() {
        comunicacaoMapper = Mappers.getMapper(ComunicacaoMapper.class);
        comunicacaoEntity = ComunicacaoEntity.builder().id(123L).dataHoraEvento(DATA_HORA_EVENTO).dataHoraEnvio(DATA_HORA_ENVIO).emailDestinatario(
        "mensagem@email.com").modoDeEnvio(MODO_DE_ENVIO).statusEnvio(STATUS_ENVIO).telefoneDestinatario("303-568-5178")
        .nomeDestinatario("Customer C").mensagem("mensagem").build();

        comunicacaoInDTO = ComunicacaoInDTOFixture.build(DATA_HORA_ENVIO, DATA_HORA_EVENTO, "Customer C", "mensagem@email.com",
                "303-568-5178", "mensagem", MODO_DE_ENVIO, STATUS_ENVIO);

        comunicacaoOutDTO = ComunicacaoOutDTOFixture.build(123L, DATA_HORA_ENVIO, DATA_HORA_EVENTO, "Customer C", "mensagem@email.com",
                "303-568-5178", "mensagem", MODO_DE_ENVIO, STATUS_ENVIO);
    }

    @Test
    public void converteParaComunicacaoOutDTO() {
        ComunicacaoOutDTO comunicacaoDTO = comunicacaoMapper.paraComunicacaoOutDTO(comunicacaoEntity);
        assertEquals(comunicacaoDTO, comunicacaoOutDTO);
    }

    @Test
    public void converteParaComunicacaoEntity() {
        ComunicacaoEntity entity = comunicacaoMapper.paraComunicacaoEntity(comunicacaoInDTO);
        Assertions.assertThat(entity).usingRecursiveComparison().ignoringFields("id")
                        .isEqualTo(comunicacaoEntity);
        /*The algorithm gets the fields of an actual object and then compares them to the corresponding fields of an expected object.
        However, the comparison doesn’t work in a symmetrical way. The expected object can have more fields than the actual one.*/
    }
}
