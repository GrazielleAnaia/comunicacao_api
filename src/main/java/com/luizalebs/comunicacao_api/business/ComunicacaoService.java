package com.luizalebs.comunicacao_api.business;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

import static org.springframework.util.Assert.notNull;

@Service
@RequiredArgsConstructor

public class ComunicacaoService {

    private final ComunicacaoRepository repository;

    private final ComunicacaoMapper mapper;

    private final ComunicacaoUpdateMapper updateMapper;

    private final EmailClient emailClient;


    public ComunicacaoOutDTO agendarComunicacaoProjetoOriginal(ComunicacaoInDTO dto) {
        if (Objects.isNull(dto)) {
            throw new RuntimeException();
        }
            dto.setStatusEnvio(StatusEnvioEnum.PENDENTE);
            ComunicacaoEntity entity = mapper.paraComunicacaoEntity(dto);
            repository.save(entity);
            ComunicacaoOutDTO outDTO = mapper.paraComunicacaoOutDTO(entity);
            return outDTO;
    }

    public ComunicacaoOutDTO agendarComunicacao(ComunicacaoInDTO dto){
        try{
            notNull(dto, "Dados obrigatorios");
            ComunicacaoEntity entity = mapper.paraComunicacaoEntity(dto);
            dto.setStatusEnvio(StatusEnvioEnum.PENDENTE);
            dto.setModoDeEnvio(ModoEnvioEnum.EMAIL);
            dto.setDataHoraEnvio(Date.from(Instant.now()));
            repository.save(entity);
            ComunicacaoOutDTO outDTO = mapper.paraComunicacaoOutDTO(entity);
            return outDTO;
        } catch (final Exception e) {
            throw new BusinessException("Erro ao agendar comunicacao", e);
        }
    }


public ComunicacaoOutDTO agendarComunicacao2(ComunicacaoInDTO dto){
    notNull(dto, "Dados da comunicacao sao obrigatorios");
    try{
        dto.setStatusEnvio(StatusEnvioEnum.PENDENTE);
        dto.setModoDeEnvio(ModoEnvioEnum.EMAIL);
        dto.setDataHoraEnvio(Date.from(Instant.now()));
        ComunicacaoEntity entity = mapper.paraComunicacaoEntity(dto);
        repository.save(entity);
        ComunicacaoOutDTO outDTO = mapper.paraComunicacaoOutDTO(entity);
        return outDTO;
    } catch (Exception e) {
        throw new BusinessException("Erro ao salvar dados da comunicacao", e);
    }
}

public ComunicacaoOutDTO agendarComunicacaoModificado(ComunicacaoInDTO dto) {
        if(dto == null) {
            throw new IllegalArgumentException("dados obrigatorios");
        }
        try{
            dto.setStatusEnvio(StatusEnvioEnum.PENDENTE);
            dto.setModoDeEnvio(ModoEnvioEnum.EMAIL);
            dto.setDataHoraEnvio(Date.from(Instant.now()));
            ComunicacaoEntity entity = mapper.paraComunicacaoEntity(dto);
           return mapper.paraComunicacaoOutDTO(repository.save(entity));

        } catch(BusinessException e) {
            throw new BusinessException("Erro ao salvar comunicacao", e);
        }

}

    public ComunicacaoOutDTO buscarStatusComunicacao(String emailDestinatario) {
        try{
            ComunicacaoEntity entity = repository.findByEmailDestinatario(emailDestinatario);
            return mapper.paraComunicacaoOutDTO(entity);
        } catch(ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Email nao encontrado", e);
        }
    }

    public ComunicacaoOutDTO alterarStatusComunicacao(String emailDestinatario) {
        try{
            ComunicacaoEntity entity = repository.findByEmailDestinatario(emailDestinatario);
            entity.setStatusEnvio(StatusEnvioEnum.ALTERADO);
            return mapper.paraComunicacaoOutDTO(repository.save(entity));
        } catch(Exception e) {
            throw new ResourceNotFoundException("Email obrigatorio", e);
        }
    }

    //Methods deletarComunicacaoPorEmail e deletarComunicacaoPorId
    public void deletarComunicacao(Long id) {
        ComunicacaoEntity entity = repository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Id mensagem nao encontrado: " + id));
        repository.deleteById(id);
    }

    public void deletarComunicacaoPorEmail(String email) {
        try{
           ComunicacaoEntity entity = repository.findByEmailDestinatario(email);
           repository.deleteByEmailDestinatario(email);
       } catch (ResourceNotFoundException e) {
           throw new ResourceNotFoundException("Email nao encontrado", e);
       }
    }

    public ComunicacaoOutDTO updateDadosComunicacao(ComunicacaoInDTO comunicacaoInDTO, Long id) {
        ComunicacaoEntity entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id " +
                "nao encontrado: " + id));
        comunicacaoInDTO.setStatusEnvio(StatusEnvioEnum.ALTERADO);
        ComunicacaoEntity entity1 = updateMapper.updateComunicacao(comunicacaoInDTO, entity);
        return mapper.paraComunicacaoOutDTO(repository.save(entity1));
    }


    public void implementaComunicacaoPorEmail(ComunicacaoInDTO comunicacaoInDTO) {
        if(comunicacaoInDTO == null) {
            throw new IllegalArgumentException("required");
        }
        try{
            emailClient.enviaEmail(comunicacaoInDTO);
            comunicacaoInDTO.setStatusEnvio(StatusEnvioEnum.ENVIADO);
        } catch(Exception e) {
            throw new EmailException("Erro ao enviar email", e);
        }
    }
}
