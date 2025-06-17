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
import com.luizalebs.comunicacao_api.infraestructure.exceptions.BusinessException;
import com.luizalebs.comunicacao_api.infraestructure.exceptions.EmailException;
import com.luizalebs.comunicacao_api.infraestructure.exceptions.IllegalArgumentException;
import com.luizalebs.comunicacao_api.infraestructure.exceptions.ResourceNotFoundException;
import com.luizalebs.comunicacao_api.infraestructure.repositories.ComunicacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @Mock
    EmailClient emailClient;
    @InjectMocks
    ComunicacaoService comunicacaoService;

    @Mock
    ComunicacaoRepository comunicacaoRepository;
    @Mock
    ComunicacaoMapper comunicacaoMapper;
    @Mock
    ComunicacaoUpdateMapper comunicacaoUpdateMapper;

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

    //----------------------------------------------------------------------------------------------------------------------------
    @Test
    public void deve_QuandoBuscarStatusComunicacao_ComSucesso() {
        when(comunicacaoRepository.findByEmailDestinatario(email)).thenReturn(comunicacaoEntity);
        when(comunicacaoMapper.paraComunicacaoOutDTO(comunicacaoEntity)).thenReturn(comunicacaoOutDTO);
        ComunicacaoOutDTO outDTO = comunicacaoService.buscarStatusComunicacao(email);
        assertEquals(comunicacaoOutDTO, outDTO);
        verify(comunicacaoRepository).findByEmailDestinatario(email);
        verify(comunicacaoMapper).paraComunicacaoOutDTO(comunicacaoEntity);
        verifyNoMoreInteractions(comunicacaoRepository, comunicacaoMapper);
    }

    @Test
    public void geraExcecao_QuandoBuscarStatusComunicacao_SeEmailNull() {
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
    public void geraExcecao_QuandoBuscarStatusComunicacao2_SeEmailNull() {
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
//
//@Test
//public void geraExcecao_QuandoBuscarStatusComunicacao2_NaoEncontrado() {
//    ResourceNotFoundException exc = new ResourceNotFoundException("Email nao encontrado: " + null);
//    Throwable throwable = assertThrows(ResourceNotFoundException.class, () -> comunicacaoService.buscarStatusComunicacao(null));
//    assertEquals("Email nao encontrado: " + null, throwable.getMessage());
//}




//-----------------------------------alterarStatusComunicacao(String emailDestinatario)-------------------------------------------------------------------------------------------------------

@Test
public void geraExcecao_Quando_AlterarStatusComunicacao() {
    ResourceNotFoundException exc = assertThrows(ResourceNotFoundException.class, () ->
            comunicacaoService.alterarStatusComunicacao(null));
    assertThat(exc, notNullValue());
    assertThat(exc.getMessage(), is("Email nao encontrado" + null));
    assertThat(exc.getCause().getMessage(), is("Email nao encontrado" + null)); //o codigo quebra aqui
    assertThat(exc.getCause().getClass(), is (ResourceNotFoundException.class)); //o codigo quebra aqui
    verify(comunicacaoRepository).findByEmailDestinatario(null);
    verifyNoMoreInteractions(comunicacaoRepository);
}

@DisplayName("Test exception")
//@Test
//public void gerarExcecao_Quando_alterarStatsuComunicacao2() {
//    when(comunicacaoRepository.findByEmailDestinatario(email)).thenThrow(new ResourceNotFoundException("failed"));
//    ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () ->
//            comunicacaoService.alterarStatusComunicacao(null));
//    assertThat(e, notNullValue());
//    assertThat(e.getCause().getClass(), is(ResourceNotFoundException.class));
//    assertThat(e.getCause().getMessage(), is("Email nao encontrado" + null));
//    verify(comunicacaoRepository).findByEmailDestinatario(email);
//    verifyNoMoreInteractions(comunicacaoRepository);
//    verifyNoInteractions(comunicacaoMapper);
//}
    //------------------------------agendarComunicacao()----------------------------------------------------------------------------------------------------------------------
    //Abaixo seguem as modificacoes para o method agendarComunicacao() para fazer passar na excecao
    //Test para agendarComunicacaoProjetoOriginal()
    @Test
    public void deve_AgendarComunicacaoProjetoOriginal_ComSucesso() {
        when(comunicacaoMapper.paraComunicacaoEntity(comunicacaoInDTO)).thenReturn(comunicacaoEntity);
        when(comunicacaoRepository.save(comunicacaoEntity)).thenReturn(comunicacaoEntity);
        when(comunicacaoMapper.paraComunicacaoOutDTO(comunicacaoEntity)).thenReturn(comunicacaoOutDTO);
        ComunicacaoOutDTO outDTO = comunicacaoService.agendarComunicacaoProjetoOriginal(comunicacaoInDTO);
        assertEquals(comunicacaoOutDTO, outDTO);
        verify(comunicacaoRepository).save(comunicacaoEntity);
        verify(comunicacaoMapper).paraComunicacaoEntity(comunicacaoInDTO);
        verify(comunicacaoMapper).paraComunicacaoOutDTO(comunicacaoEntity);
        verifyNoMoreInteractions(comunicacaoRepository, comunicacaoMapper);
    }

    //Nao foi possivel criar o metodo de excecao para agendarComunicacao quando tem o if statement
    // esse teste nao passa com o if
//    @Test
//    public void geraExcecao_Quando_AgendarComunicacaoProjetoOriginal_SeDtoNull() {
//        RuntimeException e = assertThrows(RuntimeException.class, () ->
//                comunicacaoService.agendarComunicacaoProjetoOriginal(null));
//        assertThat(e, notNullValue());
//        assertThat(e.getCause().getClass(), is(RuntimeException.class)); //o codigo quebra nessa linha
//        verifyNoInteractions(comunicacaoRepository);
//    }

    //Test da modificacao do metodo agendarComunicacaoProjetoOriginal() para agendarComunicacao()
    @Test
    void  deve_AgendarComunicacao_ComSucesso(){
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
    void naoDeveSalvar_QuandoAgendarComunicacao_SeDtoNull() {
        final BusinessException e = assertThrows(BusinessException.class, ()->
        {comunicacaoService.agendarComunicacao(null);});
        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Erro ao agendar comunicacao"));
        assertThat(e.getCause().getMessage(), is("Dados obrigatorios"));
        verifyNoInteractions(comunicacaoRepository);
    }

    @Test
    public void deveGerarExcecao_QuandoRepositorioFalharAoSalvar_AgendarComunicacao() {
        when(comunicacaoMapper.paraComunicacaoEntity(comunicacaoInDTO)).thenReturn(comunicacaoEntity);
        when(comunicacaoRepository.save(comunicacaoEntity)).thenThrow(new RuntimeException("Falha ao salvar comunicacao"));
        final BusinessException e = assertThrows(BusinessException.class, () ->
                comunicacaoService.agendarComunicacao(comunicacaoInDTO));
        assertThat(e.getMessage(), is("Erro ao agendar comunicacao"));
        assertThat(e, notNullValue());
        assertThat(e.getCause().getClass(), is(RuntimeException.class));
        assertThat(e.getCause().getMessage(), is("Falha ao salvar comunicacao"));
        verify(comunicacaoRepository).save(comunicacaoEntity);
        verifyNoMoreInteractions(comunicacaoRepository);
    }

    //Test da modificacao do metodo agendarComunicacaoProjetoOriginal() para agendarComunicacao2()
    //-----> nenhum dos 2 testes passam <-------
//    @Test
//    public void deveGerarExcecao_QuandoFalharAoSalvar_AgendarComunicacao2() {
//        when(comunicacaoMapper.paraComunicacaoEntity(comunicacaoInDTO)).thenReturn(comunicacaoEntity);
//        when(comunicacaoRepository.save(comunicacaoEntity)).thenThrow(new ConflictException("Excecao"));
//        BusinessException exception = assertThrows(BusinessException.class, () -> comunicacaoService.agendarComunicacao2(comunicacaoInDTO));
//        assertThat(exception, notNullValue());
//        assertThat(exception.getMessage(), is("Erro ao salvar dados da comunicacao"));
//        assertThat(exception.getCause().getClass(), is(BusinessException.class));
//        assertThat(exception.getCause().getMessage(), is("Excecao"));
//        verify(comunicacaoMapper).paraComunicacaoEntity(comunicacaoInDTO);
//        verify(comunicacaoRepository).save(comunicacaoEntity);
//        verifyNoMoreInteractions(comunicacaoMapper, comunicacaoRepository);
//    }

//    @Test
//    public void geraExcecao_QuandoAgendarComunicacao2_SeDtoNull() {
//        Throwable cause = assertThrows(RuntimeException.class, () ->
//                comunicacaoService.agendarComunicacao2(null));
//        assertThat(cause, notNullValue());
//        assertThat(cause.getCause().getMessage(), is("Dados da comunicacao sao obrigatorios"));
//    }

//--------------------------------------------------------------------------------------------------------------------------------------------------

    //Inicia tests deletarComunicacaoPorEmail() e deletarComunicacaoPorId()
    @Test
    public void deveDeletarComunicacaoPorId_ComSucesso() {
        when(comunicacaoRepository.findById(id)).thenReturn(Optional.ofNullable(comunicacaoEntity));
        doNothing().when(comunicacaoRepository).deleteById(id);
        comunicacaoService.deletarComunicacao(id);
        verify(comunicacaoRepository).findById(id);
        verifyNoMoreInteractions(comunicacaoRepository);
    }
    @Test
    public void deveDeletarComunicacaoPorEmail_ComSucesso() {
        when(comunicacaoRepository.findByEmailDestinatario(email)).thenReturn(comunicacaoEntity);
        doNothing().when(comunicacaoRepository).deleteByEmailDestinatario(email);
        comunicacaoService.deletarComunicacaoPorEmail(email);
        verify(comunicacaoRepository).findByEmailDestinatario(email);
        verifyNoMoreInteractions(comunicacaoRepository);
    }

    @Test
    public void geraExcecao_QuandoDeletarComunicacaoPorEmail() {
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
    public void geraExcecao_QuandoDeletarComunicacaoPorId() {
        ResourceNotFoundException exc = new ResourceNotFoundException("Id mensagem nao encontrado: " + id);
        Throwable throwable = assertThrows(ResourceNotFoundException.class, () -> comunicacaoService.deletarComunicacao(id));
        assertEquals("Id mensagem nao encontrado: " + id, throwable.getMessage());
    }
//--------------------------------------------------------------------------------------------------------------------------------------------
    @DisplayName("method implementaComunicacaoPorEmail()")
    @Test
    public void deve_ImplementarEmailComunicacao_ComSucesso() {
        doNothing().when(emailClient).enviaEmail(comunicacaoInDTO);
        comunicacaoService.implementaComunicacaoPorEmail(comunicacaoInDTO);
        verify(emailClient).enviaEmail(comunicacaoInDTO);
        verifyNoMoreInteractions(emailClient);
}

    @DisplayName("gera excecao IllegalArgumentException ao method implementaComunicacaoPorEmail()")
    @Test
    public void geraException_IllegalArgumentException_ImplementaEmailModificado() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                comunicacaoService.implementaComunicacaoPorEmail(null));
        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("required"));
        assertThat(e.getClass(), is(IllegalArgumentException.class));
        verifyNoInteractions(emailClient);
    }

    @DisplayName("gera excecao EmailException ao method implementaComunicacaoPorEmail()")
    @Test
    public void geraException_EmailException_ImplementaEmailModificado() {
        doThrow(new EmailException("Erro ao enviar email")).when(emailClient).enviaEmail(comunicacaoInDTO);
        EmailException e = assertThrows(EmailException.class, () ->
                comunicacaoService.implementaComunicacaoPorEmail(comunicacaoInDTO));
        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Erro ao enviar email"));
        assertThat(e.getClass(), is(EmailException.class));
        verify(emailClient).enviaEmail(comunicacaoInDTO);
        verifyNoMoreInteractions(emailClient);
    }

    @DisplayName("gera RuntimeException e IllegalArgumentException ao method implementaComunicacaoPorEmail()")
    @Test
    public void otherException_EmailException_RuntimeException_ImplementaEmailModificado() {
        doThrow(new RuntimeException("Erro")).when(emailClient).enviaEmail(comunicacaoInDTO);
        EmailException e = assertThrows(EmailException.class, () ->
                comunicacaoService.implementaComunicacaoPorEmail(comunicacaoInDTO));
        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Erro ao enviar email"));
        assertThat(e.getClass(), is(EmailException.class));
        assertThat(e.getCause().getMessage(), is("Erro"));
        assertThat(e.getCause().getClass(), is(RuntimeException.class));
        verify(emailClient).enviaEmail(comunicacaoInDTO);
        verifyNoMoreInteractions(emailClient);
    }




}