package com.luizalebs.comunicacao_api.infraestructure.client;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.business.converter.ComunicacaoUpdateMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "envia-email", url = "${envia-email.url}")
public interface EmailClient {

   // EmailClient INSTANCE = Mappers.getMapper(EmailClient.class);

    @PostMapping("/email")
   void enviaEmail(@RequestBody ComunicacaoInDTO dto);
}
