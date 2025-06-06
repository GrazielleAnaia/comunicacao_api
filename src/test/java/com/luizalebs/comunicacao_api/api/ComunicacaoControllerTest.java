package com.luizalebs.comunicacao_api.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTOFixture;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTOFixture;
import com.luizalebs.comunicacao_api.business.ComunicacaoService;
import com.luizalebs.comunicacao_api.infraestructure.enums.ModoEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static com.luizalebs.comunicacao_api.infraestructure.enums.ModoEnvioEnum.EMAIL;
import static com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum.PENDENTE;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)

@AutoConfigureMockMvc
public class ComunicacaoControllerTest {

@InjectMocks
    ComunicacaoController controller;

@Mock
    private MockMvc mockMvc;
@Mock
private final ObjectMapper objectMapper = new ObjectMapper();
@Mock
ComunicacaoService service;
@Mock
ComunicacaoOutDTO comunicacaoOutDTO;
@Mock
ComunicacaoInDTO comunicacaoInDTO;

    private static final Date DATA_HORA_ENVIO = new Date(2025, 05, 29);
    private static  final Date DATA_HORA_EVENTO = new Date(2025, 06, 29);
    private static  final ModoEnvioEnum MODO_DE_ENVIO = EMAIL;
    private static  final StatusEnvioEnum STATUS_ENVIO = PENDENTE;
    private static final String email = "mensagem@email.com";
    private static final Long id = 123L;

    private String json;
    private String url;

    @BeforeEach
    public void setup() {
       mockMvc = MockMvcBuilders.standaloneSetup(controller).alwaysDo(print()).build();

        url = "/comunicacao";
        comunicacaoInDTO = ComunicacaoInDTOFixture.build(DATA_HORA_ENVIO, DATA_HORA_EVENTO, "Customer C", "mensagem@email.com",
                "303-568-5178", "mensagem", MODO_DE_ENVIO, STATUS_ENVIO);
        comunicacaoOutDTO = ComunicacaoOutDTOFixture.build(123L, DATA_HORA_ENVIO, DATA_HORA_EVENTO, "Customer C", "mensagem@email.com",
                "303-568-5178", "mensagem", MODO_DE_ENVIO, STATUS_ENVIO);
    }


        @Test
    void deveAgendarComunicacao() throws Exception {
        when(service.agendarComunicacao2(comunicacaoInDTO)).thenReturn(comunicacaoOutDTO);
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
        verify(service).agendarComunicacao2(comunicacaoInDTO);
        verifyNoMoreInteractions(service);
    }
}
