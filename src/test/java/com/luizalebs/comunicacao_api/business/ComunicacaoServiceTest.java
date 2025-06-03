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
import com.luizalebs.comunicacao_api.infraestructure.exceptions.ConflictException;
import com.luizalebs.comunicacao_api.infraestructure.exceptions.ResourceNotFoundException;
import com.luizalebs.comunicacao_api.infraestructure.repositories.ComunicacaoRepository;
import org.hibernate.annotations.Comment;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    public void deletarComunicacaoPorEmail_testeThrownException() {
        when(comunicacaoRepository.findByEmailDestinatario(null)).thenThrow(new ResourceNotFoundException("Excecao"));
        ResourceNotFoundException exc = assertThrows(ResourceNotFoundException.class, () -> comunicacaoService.deletarComunicacaoPorEmail(null));
        assertThat(exc, notNullValue());
        assertThat(exc.getMessage(), is("Email nao encontrado"));
        assertThat(exc.getCause().getMessage(), is("Excecao"));
        assertThat(exc.getCause().getClass(), is (ResourceNotFoundException.class));
        verify(comunicacaoRepository).findByEmailDestinatario(null);
        verifyNoMoreInteractions(comunicacaoRepository);
    }

    @Test
    public void deveGerarExcecaoDeletarIdNaoEncontrado$Test() {
        ResourceNotFoundException exc = new ResourceNotFoundException("Id mensagem nao encontrado: " + id );
        Throwable throwable = assertThrows(ResourceNotFoundException.class, () -> comunicacaoService.deletarComunicacao(id));
        assertEquals("Id mensagem nao encontrado: " + id, throwable.getMessage());
    }

    @Test
    public void deveAgendarComunicacao() {
        when(comunicacaoMapper.paraComunicacaoEntity(comunicacaoInDTO)).thenReturn(comunicacaoEntity);
        when(comunicacaoRepository.save(comunicacaoEntity)).thenReturn(comunicacaoEntity);
        when(comunicacaoMapper.paraComunicacaoOutDTO(comunicacaoEntity)).thenReturn(comunicacaoOutDTO);
        ComunicacaoOutDTO outDTO = comunicacaoService.agendarComunicacao(comunicacaoInDTO);
        assertEquals(comunicacaoOutDTO, outDTO);
        verify(comunicacaoRepository).save(comunicacaoEntity);
        verify(comunicacaoMapper).paraComunicacaoEntity(comunicacaoInDTO);
        verify(comunicacaoMapper).paraComunicacaoOutDTO(comunicacaoEntity);
        verifyNoMoreInteractions(comunicacaoRepository, comunicacaoMapper);
    }

    @Test
    public void deveNaoAgendarComunicacaoSeDadosNull() {
        java.lang.IllegalArgumentException exception = assertThrows(java.lang.IllegalArgumentException.class, () ->
                comunicacaoService.agendarComunicacao2(null));
        assertThat(exception.getMessage(), is("Dados da comunicacao sao obrigatorios"));
        assertThat(exception.getCause().getClass(), is(IllegalArgumentException.class));
        assertThat(exception, notNullValue());
        assertThat(exception.getCause().getMessage(), is("Dados da comunicacao sao obrigatorios"));
        verifyNoInteractions(comunicacaoMapper, comunicacaoRepository);
    }
    @Comment("Metodo agendarComunicacao2()")
    @Test
    public void deveGerarExcecaoSeDadosComunicacao2JaExistentes() {
        when(comunicacaoMapper.paraComunicacaoEntity(comunicacaoInDTO)).thenReturn(comunicacaoEntity);
        when(comunicacaoRepository.save(comunicacaoEntity)).thenThrow(new ConflictException("Excecao"));
        ConflictException exception = assertThrows(ConflictException.class, () -> comunicacaoService.agendarComunicacao2(comunicacaoInDTO));
        assertThat(exception, notNullValue());
        assertThat(exception.getMessage(), is("Dados de comunicacao ja existentes"));
        assertThat(exception.getCause().getClass(), is(ConflictException.class));
        assertThat(exception.getCause().getMessage(), is("Excecao"));
        verify(comunicacaoMapper).paraComunicacaoEntity(comunicacaoInDTO);
        verify(comunicacaoRepository).save(comunicacaoEntity);
        verifyNoMoreInteractions(comunicacaoMapper, comunicacaoRepository);
    }

    @Test
    public void deveBuscarStatusComunicacaoComSucesso() {
        when(comunicacaoRepository.findByEmailDestinatario(email)).thenReturn(comunicacaoEntity);
        when(comunicacaoMapper.paraComunicacaoOutDTO(comunicacaoEntity)).thenReturn(comunicacaoOutDTO);
        ComunicacaoOutDTO outDTO = comunicacaoService.buscarStatusComunicacao(email);
        assertEquals(comunicacaoOutDTO, outDTO);
        verify(comunicacaoRepository).findByEmailDestinatario(email);
        verify(comunicacaoMapper).paraComunicacaoOutDTO(comunicacaoEntity);
        verifyNoMoreInteractions(comunicacaoRepository, comunicacaoMapper);
    }

    @Test
    public void deveGerarExcecaoAoBuscarStatusComunicacaoSeEmailNull() {
        when(comunicacaoRepository.findByEmailDestinatario(anyString())).thenAnswer(invocation -> {
                    String str = invocation.getArgument(0);
                    if (str.equals("Email nao encontrado")) {
                        throw new ResourceNotFoundException("Email nao encontrado");
                    }
                    return str.isEmpty();
                });
        assertThrows(ResourceNotFoundException.class, () ->
                comunicacaoService.buscarStatusComunicacao("Email nao encontrado"));
    }

    @Test
    public void deveGerarExcecaoSeEmailNullAoBuscarStatusComunicacao2() {
        when(comunicacaoRepository.findByEmailDestinatario(null)).thenThrow(new ResourceNotFoundException("Excecao"));
        ResourceNotFoundException exc = assertThrows(ResourceNotFoundException.class, ()->
                comunicacaoService.buscarStatusComunicacao2(null));
        assertThat(exc, notNullValue());
        assertThat(exc.getMessage(), is("Email nao encontrado"));
        assertThat(exc.getCause().getMessage(), is("Excecao"));
        assertThat(exc.getCause().getClass(), is(ResourceNotFoundException.class));
        verify(comunicacaoRepository).findByEmailDestinatario(null);
        verifyNoMoreInteractions(comunicacaoRepository);
        verifyNoInteractions(comunicacaoMapper);
    }

    @Test
    public void deveGerarExcecaoAoBuscarStatusNaoEncontrado() {
        ResourceNotFoundException exc = new ResourceNotFoundException("Email nao encontrado: " + null);
        Throwable throwable = assertThrows(ResourceNotFoundException.class, () -> comunicacaoService.buscarStatusComunicacao(null));
        assertEquals("Email nao encontrado: " + null, throwable.getMessage());
    }


@Test
    public void alteraStatusComunicacao_testeThrownException() {
    ResourceNotFoundException spy = spy(new ResourceNotFoundException("Email nao encontrado"));
    ResourceNotFoundException mock = mock(ResourceNotFoundException.class);
    when(comunicacaoRepository.findByEmailDestinatario(null)).thenThrow(spy);
    ResourceNotFoundException exc = assertThrows(ResourceNotFoundException.class, () ->
            comunicacaoService.alterarStatusComunicacao(null));
    assertThat(exc, notNullValue());
    assertThat(exc.getMessage(), is("Email nao encontrado"));
    assertThat(exc.getCause().getMessage(), is("Email nao encontrado"));
    assertThat(exc.getCause().getClass(), is (ResourceNotFoundException.class));
    verify(comunicacaoRepository).findByEmailDestinatario(null);
    verifyNoMoreInteractions(comunicacaoRepository);
    }





}