package com.luizalebs.comunicacao_api.infraestructure.repositories;

import com.luizalebs.comunicacao_api.infraestructure.entities.ComunicacaoEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ComunicacaoRepository extends CrudRepository<ComunicacaoEntity, Long> {

    ComunicacaoEntity findByEmailDestinatario(String emailDestinatario);

    @Transactional
    void deleteByEmailDestinatario(String email);
}
