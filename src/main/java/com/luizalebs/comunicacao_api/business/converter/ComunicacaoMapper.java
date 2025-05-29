package com.luizalebs.comunicacao_api.business.converter;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")

public interface ComunicacaoMapper {
    @Mapping(source = "dataHoraEnvio", target = "dataHoraEnvio")
    @Mapping(source = "dataHoraEvento", target = "dataHoraEvento")
    @Mapping(source = "emailDestinatario", target = "emailDestinatario")
   @Mapping(source = "id", target = "id")
    ComunicacaoOutDTO paraComunicacaoOutDTO(ComunicacaoEntity comunicacaoEntity);
    ComunicacaoEntity paraComunicacaoEntity(ComunicacaoInDTO comunicacaoInDTO);


}
