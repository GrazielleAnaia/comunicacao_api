package com.luizalebs.comunicacao_api.api;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.business.service.ComunicacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comunicacao")
@RequiredArgsConstructor
@Tag(name = "comunicacao", description = "api de comunicacao")

public class ComunicacaoController {

    private final ComunicacaoService service;


    @PostMapping("/agendar")
    @Operation(summary = "Agenda comunicacao com o cliente", description = "agenda comunicacao com o cliente")
    @ApiResponse(responseCode = "200", description = "mensagem agendada com sucesso")
    @ApiResponse(responseCode = "409", description = "cliente ja cadastrado")
    @ApiResponse(responseCode = "500", description = "erro de servidor")
    public ResponseEntity<ComunicacaoOutDTO> agendar(@RequestBody ComunicacaoInDTO dto)  {
        return ResponseEntity.ok(service.agendarComunicacao(dto));
    }

    @GetMapping()
    @Operation(summary = "Busca o status da comunicacao por email ", description = "busca status da comunicacao por email")
    @ApiResponse(responseCode = "200", description = "mensagem encontrada com sucesso")
    @ApiResponse(responseCode = "403", description = "email do destinario nao encontrado")
    @ApiResponse(responseCode = "500", description = "erro de servidor")
    @ApiResponse(responseCode = "401", description = "credenciais invalidas")
    public ResponseEntity<ComunicacaoOutDTO> buscarStatus(@RequestParam String emailDestinatario) {
        return ResponseEntity.ok(service.buscarStatusComunicacao(emailDestinatario));
    }

    @PatchMapping("/cancelar")
    @Operation(summary = "Altera o status da comunicacao por email ", description = "altera status da comunicacao por email")
    @ApiResponse(responseCode = "200", description = "mensagem alterada com sucesso")
    @ApiResponse(responseCode = "403", description = "email do destinario nao encontrado")
    @ApiResponse(responseCode = "401", description = "credenciais invalidas")
    @ApiResponse(responseCode = "500", description = "erro de servidor")
    public ResponseEntity<ComunicacaoOutDTO> cancelarStatus(@RequestParam String emailDestinatario) {
        return ResponseEntity.ok(service.alterarStatusComunicacao(emailDestinatario));
    }
}
