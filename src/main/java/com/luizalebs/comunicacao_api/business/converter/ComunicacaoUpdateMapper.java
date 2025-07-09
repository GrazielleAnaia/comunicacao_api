package com.luizalebs.comunicacao_api.business.converter;


import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

public interface ComunicacaoUpdateMapper {

    ComunicacaoUpdateMapper INSTANCE = Mappers.getMapper(ComunicacaoUpdateMapper.class);
    ComunicacaoEntity updateComunicacao(ComunicacaoInDTO comunicacaoInDTO, @MappingTarget ComunicacaoEntity entity);
}
