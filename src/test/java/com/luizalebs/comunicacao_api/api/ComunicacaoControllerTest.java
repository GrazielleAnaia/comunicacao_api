package com.luizalebs.comunicacao_api.api;


import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static com.luizalebs.comunicacao_api.infraestructure.enums.ModoEnvioEnum.EMAIL;
import static com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum.PENDENTE;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)

//@AutoConfigureMockMvc
public class ComunicacaoControllerTest {

    private MockMvc mockMvc;
    @InjectMocks
    ComunicacaoController controller;


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

    private static final Long id = 123L;

    private String json;
    private String url;
    private String emailDestinatario;


    @BeforeEach
    public void setup() throws JsonProcessingException {
       mockMvc = MockMvcBuilders.standaloneSetup(controller).alwaysDo(print()).build();

        url = "/comunicacao";
        comunicacaoInDTO = ComunicacaoInDTOFixture.build(DATA_HORA_ENVIO, DATA_HORA_EVENTO, "Customer C", "mensagem@email.com",
                "303-568-5178", "mensagem", MODO_DE_ENVIO, STATUS_ENVIO);
        comunicacaoOutDTO = ComunicacaoOutDTOFixture.build(123L, DATA_HORA_ENVIO, DATA_HORA_EVENTO, "Customer C", "mensagem@email.com",
                "303-568-5178", "mensagem", MODO_DE_ENVIO, STATUS_ENVIO);
        json = objectMapper.writeValueAsString(comunicacaoInDTO);
        emailDestinatario = "mensagem@email.com";
    }


    @DisplayName("method agendarComunicacao()")
    @Test
    void deve_AgendarComunicacao() throws Exception {
        when(service.agendarComunicacao(comunicacaoInDTO)).thenReturn(comunicacaoOutDTO);
        mockMvc.perform(post(url + "/agendar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
        verify(service).agendarComunicacao(comunicacaoInDTO);
        verifyNoMoreInteractions(service);
    }

    @DisplayName("method agendarComunicacao()")
    @Test
    void naoDeve_AgendarComunicacao_SeDtoNull() throws Exception {
        mockMvc.perform(post(url + "/agendar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @DisplayName("method buscarStatusComunicacao()")
    @Test
    void deve_BuscarStatusComunicacao() throws Exception {
        when(service.buscarStatusComunicacao(emailDestinatario)).thenReturn(comunicacaoOutDTO);
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("emailDestinatario", emailDestinatario))
                .andExpect(status().isOk());
        verify(service).buscarStatusComunicacao(emailDestinatario);
        verifyNoMoreInteractions(service);
    }


    @DisplayName("method buscarStatusComunicacao()")
    @Test
    void naoDeve_BuscarStatusComunicacao() throws Exception {
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @DisplayName("method alterarStatusComunicacao()")
    @Test
    void deve_CancelarStatusComunicacao() throws Exception {
        when(service.alterarStatusComunicacao(emailDestinatario)).thenReturn(comunicacaoOutDTO);
        mockMvc.perform(patch(url + "/cancelar")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("emailDestinatario", emailDestinatario))
                .andExpect(status().isOk());
        verify(service).alterarStatusComunicacao(emailDestinatario);
        verifyNoMoreInteractions(service);
    }

    @DisplayName("method alterarStatusComunicacao()")
    @Test
    void naoDeve_AlterarStatusComunicacao() throws Exception {
        mockMvc.perform(patch(url + "/cancelar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(service);
    }

    @DisplayName("method deletarComunicacao()")
    @Test
    void deve_DeletarPorId() throws Exception {
        doNothing().when(service).deletarComunicacao(id);
        mockMvc.perform(delete(url + "/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .pathInfo("/{id}"))
                .andExpect(status().isOk());
        verify(service).deletarComunicacao(id);
        verifyNoMoreInteractions(service);
    }

    @DisplayName("method updateDadosComunicacao()")
    @Test
    void deve_UpdateComunicacao() throws Exception {
        when(service.updateDadosComunicacao(comunicacaoInDTO, id)).thenReturn(comunicacaoOutDTO);
        mockMvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
                .param("id", id.toString()))
        .andExpect(status().isOk());
        verify(service).updateDadosComunicacao(comunicacaoInDTO, id);
        verifyNoMoreInteractions(service);
    }


}
