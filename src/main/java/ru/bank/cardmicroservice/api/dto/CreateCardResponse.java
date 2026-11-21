package ru.bank.cardmicroservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.bank.cardmicroservice.db.types.CardStatus;

import java.time.LocalDate;
import java.util.UUID;

@Setter @Getter
@NoArgsConstructor
@Schema(description = "Ответ на создание карты")
public class CreateCardResponse {
    @Schema(description = "id карты")
    private UUID id;

    @Schema(description = "Номер карты")
    private String number;

    @Schema(description = "Дата истечения карты")
    private LocalDate expiryDate;

    @Schema(description = "Статус карты")
    private CardStatus status;
}
