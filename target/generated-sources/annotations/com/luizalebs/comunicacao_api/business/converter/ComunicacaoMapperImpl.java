package com.luizalebs.comunicacao_api.business.converter;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-30T12:57:41-0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class ComunicacaoMapperImpl implements ComunicacaoMapper {

    @Override
    public ComunicacaoOutDTO paraComunicacaoOutDTO(ComunicacaoEntity comunicacaoEntity) {
        if ( comunicacaoEntity == null ) {
            return null;
        }

        ComunicacaoOutDTO.ComunicacaoOutDTOBuilder comunicacaoOutDTO = ComunicacaoOutDTO.builder();

        comunicacaoOutDTO.dataHoraEnvio( comunicacaoEntity.getDataHoraEnvio() );
        comunicacaoOutDTO.dataHoraEvento( comunicacaoEntity.getDataHoraEvento() );
        comunicacaoOutDTO.emailDestinatario( comunicacaoEntity.getEmailDestinatario() );
        comunicacaoOutDTO.id( comunicacaoEntity.getId() );
        comunicacaoOutDTO.nomeDestinatario( comunicacaoEntity.getNomeDestinatario() );
        comunicacaoOutDTO.telefoneDestinatario( comunicacaoEntity.getTelefoneDestinatario() );
        comunicacaoOutDTO.mensagem( comunicacaoEntity.getMensagem() );
        comunicacaoOutDTO.modoDeEnvio( comunicacaoEntity.getModoDeEnvio() );
        comunicacaoOutDTO.statusEnvio( comunicacaoEntity.getStatusEnvio() );

        return comunicacaoOutDTO.build();
    }

    @Override
    public ComunicacaoEntity paraComunicacaoEntity(ComunicacaoInDTO comunicacaoInDTO) {
        if ( comunicacaoInDTO == null ) {
            return null;
        }

        ComunicacaoEntity.ComunicacaoEntityBuilder comunicacaoEntity = ComunicacaoEntity.builder();

        comunicacaoEntity.dataHoraEnvio( comunicacaoInDTO.getDataHoraEnvio() );
        comunicacaoEntity.dataHoraEvento( comunicacaoInDTO.getDataHoraEvento() );
        comunicacaoEntity.nomeDestinatario( comunicacaoInDTO.getNomeDestinatario() );
        comunicacaoEntity.emailDestinatario( comunicacaoInDTO.getEmailDestinatario() );
        comunicacaoEntity.telefoneDestinatario( comunicacaoInDTO.getTelefoneDestinatario() );
        comunicacaoEntity.mensagem( comunicacaoInDTO.getMensagem() );
        comunicacaoEntity.modoDeEnvio( comunicacaoInDTO.getModoDeEnvio() );
        comunicacaoEntity.statusEnvio( comunicacaoInDTO.getStatusEnvio() );

        return comunicacaoEntity.build();
    }
}
