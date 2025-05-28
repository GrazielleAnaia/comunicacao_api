package com.luizalebs.comunicacao_api.business.service;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.business.converter.ComunicacaoMapper;
import com.luizalebs.comunicacao_api.business.converter.ComunicacaoUpdateMapper;
import com.luizalebs.comunicacao_api.infraestructure.client.EmailClient;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import com.luizalebs.comunicacao_api.infraestructure.enums.ModoEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.enums.StatusEnvioEnum;
import com.luizalebs.comunicacao_api.infraestructure.exceptions.ConflictException;
import com.luizalebs.comunicacao_api.infraestructure.exceptions.EmailException;
import com.luizalebs.comunicacao_api.infraestructure.exceptions.MissingArgumentException;
import com.luizalebs.comunicacao_api.infraestructure.exceptions.ResourceNotFoundException;
import com.luizalebs.comunicacao_api.infraestructure.repositories.ComunicacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ComunicacaoService {

    private final ComunicacaoRepository repository;
    //private final ComunicacaoConverter converter;
    private final ComunicacaoMapper mapper;

    private final ComunicacaoUpdateMapper updateMapper;

    private final EmailClient emailClient;

    public ComunicacaoOutDTO agendarComunicacao(ComunicacaoInDTO dto) {
        try{
            if (Objects.isNull(dto)) {
                throw new MissingArgumentException("Dados da comunicacao sao obrigatorios");
            }
            dto.setStatusEnvio(StatusEnvioEnum.PENDENTE);
            dto.setModoDeEnvio(ModoEnvioEnum.EMAIL);
            dto.setDataHoraEnvio(Date.from(Instant.now()));
            ComunicacaoEntity entity = mapper.paraComunicacaoEntity(dto);
            repository.save(entity);
            ComunicacaoOutDTO outDTO = mapper.paraComunicacaoOutDTO(entity);
            return outDTO;
        } catch (ConflictException e) {
            throw new ConflictException("Dados de comunicacao ja existentes", e);
        }

    }

    public ComunicacaoOutDTO buscarStatusComunicacao(String emailDestinatario) {
        ComunicacaoEntity entity = repository.findByEmailDestinatario(emailDestinatario);
        if (Objects.isNull(entity)) {
            throw new ResourceNotFoundException("Email nao encontrado: " + emailDestinatario);
        }
        return mapper.paraComunicacaoOutDTO(entity);
    }

    public ComunicacaoOutDTO alterarStatusComunicacao(String emailDestinatario) {
        ComunicacaoEntity entity = repository.findByEmailDestinatario(emailDestinatario);
        if (Objects.isNull(entity)) {
            throw new ResourceNotFoundException("Email nao encontrado: " + emailDestinatario);
        }
        entity.setStatusEnvio(StatusEnvioEnum.CANCELADO);
        repository.save(entity);
        return (mapper.paraComunicacaoOutDTO(entity));
    }

    public void deletarComunicacao(Long id) {
        ComunicacaoEntity entity = repository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Id mensagem nao encontrado: " + id));
        repository.deleteById(id);
    }

    public ComunicacaoOutDTO updateDadosComunicacao(ComunicacaoInDTO comunicacaoInDTO, Long id) {
        ComunicacaoEntity entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id " +
                "nao encontrado: " + id));
        comunicacaoInDTO.setStatusEnvio(StatusEnvioEnum.ALTERADO);
        //Fazer metodo se a pessoa quiser alterar a forma de envio
    ComunicacaoEntity entity1 = updateMapper.updateComunicacao(comunicacaoInDTO, entity);
    return mapper.paraComunicacaoOutDTO(repository.save(entity1));
    }

    public void implementaEmailComunicacao(ComunicacaoInDTO comunicacaoInDTO){
        try{
            emailClient.enviaEmail(comunicacaoInDTO);
            comunicacaoInDTO.setStatusEnvio(StatusEnvioEnum.ENVIADO);
        } catch (EmailException e) {
            throw new EmailException("Erro ao enviar email" + e);
        }


    }



}
