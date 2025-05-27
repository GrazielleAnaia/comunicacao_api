package com.luizalebs.comunicacao_api.business.converter;


import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ComunicacaoUpdateMapper {
    ComunicacaoEntity updateComunicacao(ComunicacaoInDTO comunicacaoInDTO, @MappingTarget ComunicacaoEntity entity);
}
