package com.luizalebs.comunicacao_api.business.converter;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface ComunicacaoMapper {

    ComunicacaoOutDTO paraComunicacaoOutDTO(ComunicacaoEntity comunicacaoEntity);
    ComunicacaoEntity paraComunicacaoEntity(ComunicacaoInDTO comunicacaoInDTO);


}
