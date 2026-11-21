package ru.bank.cardmicroservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.bank.cardmicroservice.db.types.CardPaymentSystem;
import ru.bank.cardmicroservice.db.types.CardType;
import java.util.UUID;

@Setter @Getter
@NoArgsConstructor
@Schema(description = "Запрос на создание карты")
public class CreateCardRequest {
    @NotNull
    @Schema(
            description = "id пользователя на которого надо создать карту",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID userId;

    @NotNull
    @Schema(
            description = "id счета на который надо создать новую карту",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID accountId;

    @NotNull
    @Schema(
            description = "Тип карты",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private CardType type;

    @NotNull
    @Schema(
            description = "Тип платежной системы",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private CardPaymentSystem paymentSystem;
}
