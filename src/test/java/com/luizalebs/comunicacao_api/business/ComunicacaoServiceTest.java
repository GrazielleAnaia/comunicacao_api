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
import com.luizalebs.comunicacao_api.infraestructure.exceptions.*;
import com.luizalebs.comunicacao_api.infraestructure.exceptions.IllegalArgumentException;
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

    @DisplayName("deve bucarStatusComunicacao() com sucesso")
    @Test
    public void deve_BuscarStatusComunicacao_ComSucesso() {
        when(comunicacaoRepository.findByEmailDestinatario(email)).thenReturn(comunicacaoEntity);
        when(comunicacaoMapper.paraComunicacaoOutDTO(comunicacaoEntity)).thenReturn(comunicacaoOutDTO);
        ComunicacaoOutDTO outDTO = comunicacaoService.buscarStatusComunicacao(email);
        assertEquals(comunicacaoOutDTO, outDTO);
        verify(comunicacaoRepository).findByEmailDestinatario(email);
        verify(comunicacaoMapper).paraComunicacaoOutDTO(comunicacaoEntity);
        verifyNoMoreInteractions(comunicacaoRepository, comunicacaoMapper);
    }

    @DisplayName("outra forma de verificacao da Exception ao buscarStatusComunicacao() se emailDestinatario null")
    @Test
    public void geraExcecao_Quando_BuscarStatusComunicacao_SeEmailNull() {
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

    @DisplayName("gera ResourceNotFoundException ao buscarStatusComunicacao() se emailDestinatario null")
    @Test
    public void gera_ResourceNotFoundException_Quando_BuscarStatusComunicacao_SeEmailNull() {
        when(comunicacaoRepository.findByEmailDestinatario(null)).thenThrow(new ResourceNotFoundException("Excecao"));
        ResourceNotFoundException exc = assertThrows(ResourceNotFoundException.class, ()->
                comunicacaoService.buscarStatusComunicacao(null));
        assertThat(exc, notNullValue());
        assertThat(exc.getMessage(), is("Email nao encontrado"));
        assertThat(exc.getCause().getMessage(), is("Excecao"));
        assertThat(exc.getCause().getClass(), is(ResourceNotFoundException.class));
        verify(comunicacaoRepository).findByEmailDestinatario(null);
        verifyNoMoreInteractions(comunicacaoRepository);
        verifyNoInteractions(comunicacaoMapper);
    }

    @DisplayName("deve alterarStatusComunicacao() com sucesso")
    @Test
    public void deve_AlterarStatusComunicacao_ComSucesso() {
        when(comunicacaoRepository.findByEmailDestinatario(email)).thenReturn(comunicacaoEntity);
        when(comunicacaoRepository.save(comunicacaoEntity)).thenReturn(comunicacaoEntity);
        when(comunicacaoMapper.paraComunicacaoOutDTO(comunicacaoEntity)).thenReturn(comunicacaoOutDTO);
        ComunicacaoOutDTO outDTO = comunicacaoService.alterarStatusComunicacao(email);
        assertEquals(comunicacaoOutDTO, outDTO);
        verify(comunicacaoRepository).findByEmailDestinatario(email);
        verify(comunicacaoRepository).save(comunicacaoEntity);
        verify(comunicacaoMapper).paraComunicacaoOutDTO(comunicacaoEntity);
        verifyNoMoreInteractions(comunicacaoRepository, comunicacaoMapper);
    }

    @DisplayName("gera ResourceNotFoundException ao alterarStatusComunicacao() se emailDestinatario null")
    @Test
    public void geraExcecao_Quando_AlterarStatusComunicacao() {
        when(comunicacaoRepository.findByEmailDestinatario(null)).thenThrow(new RuntimeException("Erro"));
        ResourceNotFoundException exc = assertThrows(ResourceNotFoundException.class, () ->
                comunicacaoService.alterarStatusComunicacao(null));
        assertThat(exc, notNullValue());
        assertThat(exc.getMessage(), is("Email obrigatorio"));
        assertThat(exc.getCause().getMessage(), is("Erro"));
        assertThat(exc.getClass(), is (ResourceNotFoundException.class));
        assertThat(exc.getCause().getClass(), is(RuntimeException.class));
        verify(comunicacaoRepository).findByEmailDestinatario(null);
        verifyNoMoreInteractions(comunicacaoRepository);
        verifyNoInteractions(comunicacaoMapper);
}

    @DisplayName("deve deletarComunicacao() usando o id com sucesso")
    @Test
    public void deve_DeletarComunicacaoPorId_ComSucesso() {
        when(comunicacaoRepository.findById(id)).thenReturn(Optional.ofNullable(comunicacaoEntity));
        doNothing().when(comunicacaoRepository).deleteById(id);
        comunicacaoService.deletarComunicacao(id);
        verify(comunicacaoRepository).findById(id);
        verifyNoMoreInteractions(comunicacaoRepository);
    }

    @DisplayName("deve deletarComunicacaoPorEmail() usando o emailDestinatario")
    @Test
    public void deve_DeletarComunicacaoPorEmail_ComSucesso() {
        when(comunicacaoRepository.findByEmailDestinatario(email)).thenReturn(comunicacaoEntity);
        doNothing().when(comunicacaoRepository).deleteByEmailDestinatario(email);
        comunicacaoService.deletarComunicacaoPorEmail(email);
        verify(comunicacaoRepository).findByEmailDestinatario(email);
        verifyNoMoreInteractions(comunicacaoRepository);
    }

    @DisplayName("gera ResourceNotFoundException ao deletarComunicacaoPorEmail() se o emailDestinatario null")
    @Test
    public void geraExcecao_Quando_DeletarComunicacaoPorEmail_SeEmailNull() {
        when(comunicacaoRepository.findByEmailDestinatario(null)).thenThrow(new ResourceNotFoundException("Excecao"));
        ResourceNotFoundException exc = assertThrows(ResourceNotFoundException.class, () -> comunicacaoService.deletarComunicacaoPorEmail(null));
        assertThat(exc, notNullValue());
        assertThat(exc.getMessage(), is("Email nao encontrado"));
        assertThat(exc.getCause().getMessage(), is("Excecao"));
        assertThat(exc.getCause().getClass(), is (ResourceNotFoundException.class));
        verify(comunicacaoRepository).findByEmailDestinatario(null);
        verifyNoMoreInteractions(comunicacaoRepository);
    }

    @DisplayName("gera ResourceNotFoundException ao deletarComunicacaoPorId() se o id null")
    @Test
    public void geraExcecao_QuandoDeletarComunicacaoPorId_SeIdNull() {
        ResourceNotFoundException exc = new ResourceNotFoundException("Id mensagem nao encontrado: " + id);
        Throwable throwable = assertThrows(ResourceNotFoundException.class, () -> comunicacaoService.deletarComunicacao(id));
        assertEquals("Id mensagem nao encontrado: " + id, throwable.getMessage());
    }

    @DisplayName("deve fazer o updateDadosComunicacao() com sucesso")
    @Test
    public void deve_UpdateDadosComunicacao_ComSucesso() {
        when(comunicacaoRepository.findById(id)).thenReturn(Optional.of(comunicacaoEntity));
        when(comunicacaoUpdateMapper.updateComunicacao(comunicacaoInDTO, comunicacaoEntity)).thenReturn(comunicacaoEntity);
        when(comunicacaoRepository.save(comunicacaoEntity)).thenReturn(comunicacaoEntity);
        when(comunicacaoMapper.paraComunicacaoOutDTO(comunicacaoEntity)).thenReturn(comunicacaoOutDTO);
        ComunicacaoOutDTO outDTO = comunicacaoService.updateDadosComunicacao(comunicacaoInDTO, comunicacaoEntity.getId());
        assertEquals(comunicacaoOutDTO, outDTO);
        verify(comunicacaoRepository).findById(id);
        verify(comunicacaoUpdateMapper).updateComunicacao(comunicacaoInDTO, comunicacaoEntity);
        verify(comunicacaoRepository).save(comunicacaoEntity);
        verify(comunicacaoMapper).paraComunicacaoOutDTO(comunicacaoEntity);
        verifyNoMoreInteractions(comunicacaoRepository, comunicacaoUpdateMapper, comunicacaoMapper);
    }

    @DisplayName("gera ResourceNotFoundException ao updateDadosComunicacao() se id null")
    @Test
    public void geraException_QuandoUpdateDadosComunicacao_SeIdNull() {
        when(comunicacaoRepository.findById(null)).thenThrow(new ResourceNotFoundException ("Id nao encontrado: " + null));
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () ->
                comunicacaoService.updateDadosComunicacao(comunicacaoInDTO, null));
        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Id nao encontrado: " + null));
        assertThat(e.getClass(), is(ResourceNotFoundException.class));
        verify(comunicacaoRepository).findById(null);
        verifyNoMoreInteractions(comunicacaoRepository);
        verifyNoInteractions(comunicacaoUpdateMapper);
    }

    @DisplayName("deve agendarComunicacaoProjetoOriginall() com sucesso")
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

    @Test
    public void geraExcecao_Quando_AgendarComunicacaoProjetoOriginal_SeDtoNull() {
        RuntimeException e = assertThrows(RuntimeException.class, () ->
                comunicacaoService.agendarComunicacaoProjetoOriginal(null));
        assertThat(e, notNullValue());
        assertThat(e.getClass(), is(RuntimeException.class));
        verifyNoInteractions(comunicacaoRepository, comunicacaoMapper);
    }

    @DisplayName("deve agendarComunicacao() com sucesso")
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

    @DisplayName("gera BusinessException ao agendarComunicacao() se dto null")
    @Test
    void naoDeveSalvar_QuandoAgendarComunicacao_SeDtoNull() {
        final BusinessException e = assertThrows(BusinessException.class, ()->
        {comunicacaoService.agendarComunicacao(null);});
        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Erro ao agendar comunicacao"));
        assertThat(e.getCause().getMessage(), is("Dados obrigatorios"));
        verifyNoInteractions(comunicacaoRepository);
    }

    @DisplayName("gera BusinessException ao agendarComunicacao() se falhar ao salvar")
    @Test
    public void geraException_Quando_AgendarComunicacao_SeFalharAoSalvar() {
        when(comunicacaoMapper.paraComunicacaoEntity(comunicacaoInDTO)).thenReturn(comunicacaoEntity);
        when(comunicacaoRepository.save(comunicacaoEntity)).thenThrow(new RuntimeException("Falha ao salvar comunicacao"));
        final BusinessException e = assertThrows(BusinessException.class, () ->
                comunicacaoService.agendarComunicacao(comunicacaoInDTO));
        assertThat(e.getMessage(), is("Erro ao agendar comunicacao"));
        assertThat(e, notNullValue());
        assertThat(e.getCause().getClass(), is(RuntimeException.class));
        assertThat(e.getCause().getMessage(), is("Falha ao salvar comunicacao"));
        verify(comunicacaoRepository).save(comunicacaoEntity);
        verifyNoMoreInteractions(comunicacaoRepository, comunicacaoMapper);
    }

    @DisplayName("gera ConflictException, BusinessException ao agendarComunicacao2() se falhar ao salvar")
    @Test
    public void geraException_Quando_AgendarComunicacao2_SeFalharAoSalvar() {
        when(comunicacaoMapper.paraComunicacaoEntity(comunicacaoInDTO)).thenReturn(comunicacaoEntity);
        when(comunicacaoRepository.save(comunicacaoEntity)).thenThrow(new ConflictException("Excecao"));
        BusinessException exception = assertThrows(BusinessException.class, () -> comunicacaoService.agendarComunicacao2(comunicacaoInDTO));
        assertThat(exception, notNullValue());
        assertThat(exception.getMessage(), is("Erro ao salvar dados da comunicacao"));
        assertThat(exception.getCause().getClass(), is(ConflictException.class));
        assertThat(exception.getClass(), is(BusinessException.class));
        assertThat(exception.getCause().getMessage(), is("Excecao"));
        verify(comunicacaoMapper).paraComunicacaoEntity(comunicacaoInDTO);
        verify(comunicacaoRepository).save(comunicacaoEntity);
        verifyNoMoreInteractions(comunicacaoMapper, comunicacaoRepository);
    }

    @DisplayName("deve implementaComunicacaoPorEmail() com sucesso")
    @Test
    public void deve_ImplementarEmailComunicacao_ComSucesso() {
        doNothing().when(emailClient).enviaEmail(comunicacaoInDTO);
        comunicacaoService.implementaComunicacaoPorEmail(comunicacaoInDTO);
        verify(emailClient).enviaEmail(comunicacaoInDTO);
        verifyNoMoreInteractions(emailClient);
}

    @DisplayName("gera excecao IllegalArgumentException ao method implementaComunicacaoPorEmail() se dto null")
    @Test
    public void geraException_IllegalArgumentException_ImplementaEmailModificado_SeDTONull() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                comunicacaoService.implementaComunicacaoPorEmail(null));
        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("required"));
        assertThat(e.getClass(), is(IllegalArgumentException.class));
        verifyNoInteractions(emailClient);
    }

    @DisplayName("gera excecao EmailException ao method implementaComunicacaoPorEmail() se falhar")
    @Test
    public void geraException_EmailException_ImplementaEmailModificado_SeFalhar() {
        doThrow(new EmailException("Erro ao enviar email")).when(emailClient).enviaEmail(comunicacaoInDTO);
        EmailException e = assertThrows(EmailException.class, () ->
                comunicacaoService.implementaComunicacaoPorEmail(comunicacaoInDTO));
        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Erro ao enviar email"));
        assertThat(e.getClass(), is(EmailException.class));
        verify(emailClient).enviaEmail(comunicacaoInDTO);
        verifyNoMoreInteractions(emailClient);
    }

    @DisplayName("gera RuntimeException e IllegalArgumentException ao implementaComunicacaoPorEmail() se falhar")
    @Test
    public void gera_EmailException_RuntimeException_ImplementaEmailModificado_SeFahar() {
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

    @DisplayName("deve agendarComunicacaoModificado() com sucesso")
    @Test
    public void deve_AgendarComunicacaoModificado_ComSucesso() {
        when(comunicacaoMapper.paraComunicacaoEntity(comunicacaoInDTO)).thenReturn(comunicacaoEntity);
        when(comunicacaoRepository.save(comunicacaoEntity)).thenReturn(comunicacaoEntity);
        when(comunicacaoMapper.paraComunicacaoOutDTO(comunicacaoEntity)).thenReturn(comunicacaoOutDTO);
        ComunicacaoOutDTO outDTO = comunicacaoService.agendarComunicacaoModificado(comunicacaoInDTO);
        assertEquals(comunicacaoOutDTO, outDTO);
        verify(comunicacaoMapper).paraComunicacaoEntity(comunicacaoInDTO);
        verify(comunicacaoRepository).save(comunicacaoEntity);
        verify(comunicacaoMapper).paraComunicacaoOutDTO(comunicacaoEntity);
        verifyNoMoreInteractions(comunicacaoMapper, comunicacaoRepository);
    }

    @DisplayName("gera IllegalArgumentException ao agendarComunicacaoModificado() se dto null")
    @Test
    public void geraException_Quando_AgendarComunicacaoModificado_SeDtoNull() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                comunicacaoService.agendarComunicacaoModificado(null));
        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("dados obrigatorios"));
        assertThat(e.getClass(), is(IllegalArgumentException.class));
        verifyNoInteractions(comunicacaoRepository, comunicacaoMapper);
    }

    @DisplayName("gera BusinessException ao agendarComunicacaoModificado() ao falhar ao salvar")
    @Test
    public void geraBusinessException_Quando_AgendarComunicacaoModificado_SeFalharAoSalvar() {
        when(comunicacaoMapper.paraComunicacaoEntity(comunicacaoInDTO)).thenReturn(comunicacaoEntity);
        when(comunicacaoRepository.save(comunicacaoEntity)).thenThrow(new BusinessException("Erro"));
        BusinessException e = assertThrows(BusinessException.class, () ->
                comunicacaoService.agendarComunicacaoModificado(comunicacaoInDTO));
        assertThat(e, notNullValue());
        assertThat(e.getMessage(), is("Erro ao salvar comunicacao"));
        assertThat(e.getCause().getMessage(), is("Erro"));
        assertThat(e.getClass(), is(BusinessException.class));
        verify(comunicacaoMapper).paraComunicacaoEntity(comunicacaoInDTO);
        verify(comunicacaoRepository).save(comunicacaoEntity);
        verifyNoMoreInteractions(comunicacaoMapper, comunicacaoRepository);
    }




}