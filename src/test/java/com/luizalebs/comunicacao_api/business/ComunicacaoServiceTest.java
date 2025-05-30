package com.luizalebs.comunicacao_api.business;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTOFixture;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTOFixture;
import com.luizalebs.comunicacao_api.business.converter.ComunicacaoMapper;
import com.luizalebs.comunicacao_api.business.converter.ComunicacaoUpdateMapper;
import com.luizalebs.comunicacao_api.infraestructure.client.EmailClient;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import com.luizalebs.comunicacao_api.infraestructure.enums.ModoEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.exceptions.ResourceNotFoundException;
import com.luizalebs.comunicacao_api.infraestructure.repositories.ComunicacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static com.luizalebs.comunicacao_api.infraestructure.enums.ModoEnvioEnum.EMAIL;
import static com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum.PENDENTE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ComunicacaoServiceTest {

    @InjectMocks
    ComunicacaoService comunicacaoService;

    @Mock
    ComunicacaoRepository comunicacaoRepository;
    @Mock
    ComunicacaoMapper comunicacaoMapper;
    @Mock
    ComunicacaoUpdateMapper comunicacaoUpdateMapper;
    @Mock
    EmailClient emailClient;
    @Mock
    ComunicacaoInDTO comunicacaoInDTO;
    @Mock
    ComunicacaoOutDTO comunicacaoOutDTO;
    @Mock
    ComunicacaoEntity comunicacaoEntity;


    private static final Date DATA_HORA_ENVIO = new Date(2025, 05, 29);
    private static final Date DATA_HORA_EVENTO = new Date(2025, 06, 29);
    private static final ModoEnvioEnum MODO_DE_ENVIO = EMAIL;
    private static final StatusEnvioEnum STATUS_ENVIO = PENDENTE;
    private static final String email = "mensagem@email.com";
    private static final Long id = 123L;

    @BeforeEach
    public void setup() {


        comunicacaoEntity = ComunicacaoEntity.builder().id(123L).dataHoraEvento(DATA_HORA_EVENTO).dataHoraEnvio(DATA_HORA_ENVIO).emailDestinatario(
                        "mensagem@email.com").modoDeEnvio(MODO_DE_ENVIO).statusEnvio(STATUS_ENVIO).telefoneDestinatario("303-568-5178")
                .nomeDestinatario("Customer C").mensagem("mensagem").build();

        comunicacaoInDTO = ComunicacaoInDTOFixture.build(DATA_HORA_ENVIO, DATA_HORA_EVENTO, "Customer C", "mensagem@email.com",
                "303-568-5178", "mensagem", MODO_DE_ENVIO, STATUS_ENVIO);

        comunicacaoOutDTO = ComunicacaoOutDTOFixture.build(123L, DATA_HORA_ENVIO, DATA_HORA_EVENTO, "Customer C", "mensagem@email.com",
                "303-568-5178", "mensagem", MODO_DE_ENVIO, STATUS_ENVIO);
    }

    @Test
    public void deletaComunicacaoComSucessoPorId() {
        when(comunicacaoRepository.findById(id)).thenReturn(Optional.ofNullable(comunicacaoEntity));
        doNothing().when(comunicacaoRepository).deleteById(id);
        comunicacaoService.deletarComunicacao(id);
        verify(comunicacaoRepository).findById(id);
        verifyNoMoreInteractions(comunicacaoRepository);
    }

    @Test
    public void deveGerarExcecaoDeletarIdNaoEncontrado() {


        when(comunicacaoRepository.findById(id)).thenThrow(new ResourceNotFoundException("Id mensagem nao encontrado: "));
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> comunicacaoService.deletarComunicacao(id));
        assertThat(exception, notNullValue());
        assertThat(exception.getMessage(), is("Id mensagem nao encontrado: "));
        //assertThat(exception.getCause().getMessage(), is("Id nao encontrado"));
       // assertThat(exception.getCause().getClass(), is(ResourceNotFoundException.class));

        verify(comunicacaoRepository).findById(id);
        verifyNoMoreInteractions(comunicacaoRepository);

    }


}