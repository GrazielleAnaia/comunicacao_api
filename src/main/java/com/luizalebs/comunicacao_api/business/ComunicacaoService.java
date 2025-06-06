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
import com.luizalebs.comunicacao_api.infraestructure.exceptions.ResourceNotFoundException;
import com.luizalebs.comunicacao_api.infraestructure.repositories.ComunicacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

import static java.util.Objects.isNull;
import static org.springframework.util.Assert.notNull;

@Service
@RequiredArgsConstructor

public class ComunicacaoService {

    private final ComunicacaoRepository repository;

    private final ComunicacaoMapper mapper;

    private final ComunicacaoUpdateMapper updateMapper;

    private final EmailClient emailClient;



    //method agendarComunicacaoProjetoOriginal()
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

    //modificacao do metodo agendarComunicacaoProjetoOriginal() para agendarComunicacao() usando o try-catch
    public ComunicacaoOutDTO agendarComunicacao(ComunicacaoInDTO dto){
        try{
            notNull(dto, "Dados obrigatorios");
            dto.setStatusEnvio(StatusEnvioEnum.PENDENTE);
            dto.setModoDeEnvio(ModoEnvioEnum.EMAIL);
            dto.setDataHoraEnvio(Date.from(Instant.now()));
            ComunicacaoEntity entity = mapper.paraComunicacaoEntity(dto);
            repository.save(entity);
            ComunicacaoOutDTO outDTO = mapper.paraComunicacaoOutDTO(entity);
            return outDTO;
        } catch (final Exception e) {
            throw new BusinessException("Erro ao agendar comunicacao", e);
        }
    }


//agendarComunicacao2() ----> eu nao sei se isso eh logico, eu quero dizer colocar o notNull fora do try-catch <-----
//os testes nao passaram, eu nao sei qual classe de excecao chamar para o notNull(dto), quando esta fora do try-catch, e nao se pode colocar
//o if porque esse metodo retorna um void e nao boolean
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
    } catch (BusinessException e) {
        throw new BusinessException("Erro ao salvar dados da comunicacao", e);
    }
}


    public ComunicacaoOutDTO buscarStatusComunicacao(String emailDestinatario) {
        ComunicacaoEntity entity = repository.findByEmailDestinatario(emailDestinatario);
        if (isNull(entity.getEmailDestinatario())) {
            throw new ResourceNotFoundException("Email nao encontrado" + emailDestinatario);
        }
        return mapper.paraComunicacaoOutDTO(entity);
    }

    public ComunicacaoOutDTO buscarStatusComunicacao2(String emailDestinatario) {
        try{
            ComunicacaoEntity entity = repository.findByEmailDestinatario(emailDestinatario);
            return mapper.paraComunicacaoOutDTO(entity);
        } catch(ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Email nao encontrado", e);
        }
    }


    public ComunicacaoOutDTO alterarStatusComunicacao(String emailDestinatario) {
        ComunicacaoEntity entity = repository.findByEmailDestinatario(emailDestinatario);
        if (isNull(entity)) {
            throw new ResourceNotFoundException("Email nao encontrado" + emailDestinatario);
        }
        entity.setStatusEnvio(StatusEnvioEnum.ALTERADO);
        repository.save(entity);
        return mapper.paraComunicacaoOutDTO(entity);
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


//method implementaEmailComunicacao usando emailClient FeignClient
    public void implementaEmailComunicacao(ComunicacaoInDTO comunicacaoInDTO){
        try{
            emailClient.enviaEmail(comunicacaoInDTO);
            comunicacaoInDTO.setStatusEnvio(StatusEnvioEnum.ENVIADO);
        } catch (EmailException e) {
            throw new EmailException("Erro ao enviar email" + e);
        }
    }











}
