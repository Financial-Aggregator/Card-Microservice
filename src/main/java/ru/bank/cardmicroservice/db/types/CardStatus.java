package ru.bank.cardmicroservice.db.types;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Статус карты")
public enum CardStatus {
    ACTIVE, SUSPENDED, CLOSED, EXPIRED
}
