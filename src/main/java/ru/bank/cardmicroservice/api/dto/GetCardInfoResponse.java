package ru.bank.cardmicroservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.bank.cardmicroservice.db.types.CardPaymentSystem;
import ru.bank.cardmicroservice.db.types.CardStatus;
import ru.bank.cardmicroservice.db.types.CardType;
import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Schema(description = "Ответ на получение информации о карте")
public class GetCardInfoResponse {
    @Schema(description = "id карты")
    private UUID id;

    @Schema(description = "id пользователя")
    private UUID userId;

    @Schema(description = "id счета")
    private UUID accountId;

    @Schema(description = "номер карты")
    private String number;

    @Schema(description = "дата истчения карты")
    private LocalDate expiryDate;

    @Schema(description = "статус карты")
    private CardStatus status;

    @Schema(description = "тип карты")
    private CardType type;

    @Schema(description = "платежная система")
    private CardPaymentSystem paymentSystem;
}
