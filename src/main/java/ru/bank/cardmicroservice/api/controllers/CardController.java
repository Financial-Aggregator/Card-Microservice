package ru.bank.cardmicroservice.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.bank.cardmicroservice.api.dto.*;
import ru.bank.cardmicroservice.exceptions.AccessDeniedException;
import ru.bank.cardmicroservice.exceptions.FailedCreateCardNumberException;
import ru.bank.cardmicroservice.exceptions.FailedGrpcRequestException;
import ru.bank.cardmicroservice.exceptions.NotFoundException;
import ru.bank.cardmicroservice.mappers.CardMapper;
import ru.bank.cardmicroservice.services.CardService;
import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;
    private final CardMapper cardMapper;

    @PostMapping
    @Operation(summary = "Создать карту")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Карта успешно создана",
                        content = @Content(schema = @Schema(implementation = CreateCardResponse.class))
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Некорректные данные",
                        content = @Content(schema = @Schema(implementation = String.class))
                ),
                @ApiResponse(
                        responseCode = "500",
                        description = "Проблема на стороне сервера",
                        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                ),
            }
    )
    public ResponseEntity<CreateCardResponse> createCard(
            @RequestHeader("X-User-Id") UUID userId,
            @Valid @RequestBody CreateCardRequest request) {
        return ResponseEntity.status(201)
                .body(
                        cardMapper.cardToCreateCardResponse(
                                cardService.createCard(userId, request)
                        )
                );
    }


    @Operation(summary = "Получить информацию о карте")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Возвращена информации о карте",
                            content = @Content(schema = @Schema(implementation = GetCardInfoResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Некорректные данные",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Карта не была найдена",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Проблема на стороне сервера",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    ),
            }
    )
    @GetMapping("/{cardId}")
    public ResponseEntity<GetCardInfoResponse> getCardInfo(
            @RequestHeader("X-User-Id") UUID userId,
            @PathVariable("cardId") UUID cardId
    ) {
        return ResponseEntity.status(200)
                .body(
                       cardMapper.cardToGetCardInfoResponse(
                               cardService.getCardInfo(userId, cardId)
                       )
                );
    }

    @ExceptionHandler(FailedCreateCardNumberException.class)
    public ResponseEntity<ErrorResponse> handleFailedCreateCardNumberException(FailedCreateCardNumberException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(500)
                .body(new ErrorResponse(e));
    }

    @ExceptionHandler(FailedGrpcRequestException.class)
    public ResponseEntity<ErrorResponse> handleFailedGrpcRequestException(FailedGrpcRequestException e) {
        log.error("status code = {}, message = {}", e.getCode(), e.getMessage());
        return ResponseEntity.status(500)
                .body(new ErrorResponse(e));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccess(DataAccessException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(500).body(
                new ErrorResponse(e)
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(403)
                .body(new ErrorResponse(e));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(404)
                .body(new ErrorResponse(e));
    }
}
