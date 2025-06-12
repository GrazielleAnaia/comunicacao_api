package com.luizalebs.comunicacao_api.api;

import com.luizalebs.comunicacao_api.api.dto.ComunicacaoInDTO;
import com.luizalebs.comunicacao_api.api.dto.ComunicacaoOutDTO;
import com.luizalebs.comunicacao_api.business.ComunicacaoService;
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
    @Operation(summary = "Agendar comunicacao com o cliente", description = "agenda comunicacao com o cliente")
    @ApiResponse(responseCode = "201", description = "mensagem agendada com sucesso")
    @ApiResponse(responseCode = "409", description = "cliente ja cadastrado")
    @ApiResponse(responseCode = "500", description = "erro de servidor")
    public ResponseEntity<ComunicacaoOutDTO> agendar(@RequestBody ComunicacaoInDTO dto)  {
        return ResponseEntity.ok(service.agendarComunicacao2(dto));
    }

    @GetMapping()
    @Operation(summary = "Buscar o status da comunicacao por email", description = "busca status da comunicacao por email")
    @ApiResponse(responseCode = "200", description = "mensagem encontrada com sucesso")
    @ApiResponse(responseCode = "404", description = "email do destinario nao encontrado")
    @ApiResponse(responseCode = "500", description = "erro de servidor")
    public ResponseEntity<ComunicacaoOutDTO> buscarStatus(@RequestParam ("emailDestinatario")String emailDestinatario) {
        return ResponseEntity.ok(service.buscarStatusComunicacao(emailDestinatario));
    }

    @PatchMapping("/cancelar")
    @Operation(summary = "Alterar o status da comunicacao por email", description = "altera status da comunicacao por email")
    @ApiResponse(responseCode = "200", description = "mensagem alterada com sucesso")
    @ApiResponse(responseCode = "404", description = "email do destinario nao encontrado")
    @ApiResponse(responseCode = "500", description = "erro de servidor")
    public ResponseEntity<ComunicacaoOutDTO> cancelarStatus(@RequestParam String emailDestinatario) {
        return ResponseEntity.ok(service.alterarStatusComunicacao(emailDestinatario));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar mensagem de comunicacao por id", description = "deleta mensagem por id")
    @ApiResponse(responseCode = "204", description = "mensagem deletada com sucesso")
    @ApiResponse(responseCode = "404", description = "id de mensagem nao encontrado")
    @ApiResponse(responseCode = "500", description = "erro de servidor")
    public ResponseEntity<Void> deletarPorId(@PathVariable("id") Long id) {
        service.deletarComunicacao(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping()
    @Operation(summary = "Atualizar dados da comunicacao", description = "atualiza dados da comunicacao por id")
    @ApiResponse(responseCode = "200", description = "mensagem atualizada com sucesso")
    @ApiResponse(responseCode = "404", description = "email do destinario nao encontrado")
    @ApiResponse(responseCode = "500", description = "erro de servidor")
    public ResponseEntity<ComunicacaoOutDTO> updateComunicacao(@RequestBody ComunicacaoInDTO dto,
                                                               @RequestParam("id") Long id) {
        return ResponseEntity.ok(service.updateDadosComunicacao(dto, id));
    }

    @PostMapping("/email")
    @Operation(summary = "Enviar email de comunicacao", description = "envia email de comunicacao usando api externa")
    @ApiResponse(responseCode = "200", description = "mensagem enviada com sucesso")
    @ApiResponse(responseCode = "500", description = "erro de servidor")
    @ApiResponse(responseCode = "503", description = "servico nao disponivel")
    public ResponseEntity<Void>enviarEmailComunicacao(@RequestBody ComunicacaoInDTO dto) {
        service.implementarEmailComunicacao(dto);
        return ResponseEntity.ok().build();
    }

        }
