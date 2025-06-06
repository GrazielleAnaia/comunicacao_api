package com.luizalebs.comunicacao_api.business.converter;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-06T10:18:21-0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class ComunicacaoUpdateMapperImpl implements ComunicacaoUpdateMapper {

    @Override
    public ComunicacaoEntity updateComunicacao(ComunicacaoInDTO comunicacaoInDTO, ComunicacaoEntity entity) {
        if ( comunicacaoInDTO == null ) {
            return entity;
        }

        if ( comunicacaoInDTO.getDataHoraEnvio() != null ) {
            entity.setDataHoraEnvio( comunicacaoInDTO.getDataHoraEnvio() );
        }
        if ( comunicacaoInDTO.getDataHoraEvento() != null ) {
            entity.setDataHoraEvento( comunicacaoInDTO.getDataHoraEvento() );
        }
        if ( comunicacaoInDTO.getNomeDestinatario() != null ) {
            entity.setNomeDestinatario( comunicacaoInDTO.getNomeDestinatario() );
        }
        if ( comunicacaoInDTO.getEmailDestinatario() != null ) {
            entity.setEmailDestinatario( comunicacaoInDTO.getEmailDestinatario() );
        }
        if ( comunicacaoInDTO.getTelefoneDestinatario() != null ) {
            entity.setTelefoneDestinatario( comunicacaoInDTO.getTelefoneDestinatario() );
        }
        if ( comunicacaoInDTO.getMensagem() != null ) {
            entity.setMensagem( comunicacaoInDTO.getMensagem() );
        }
        if ( comunicacaoInDTO.getModoDeEnvio() != null ) {
            entity.setModoDeEnvio( comunicacaoInDTO.getModoDeEnvio() );
        }
        if ( comunicacaoInDTO.getStatusEnvio() != null ) {
            entity.setStatusEnvio( comunicacaoInDTO.getStatusEnvio() );
        }

        return entity;
    }
}
