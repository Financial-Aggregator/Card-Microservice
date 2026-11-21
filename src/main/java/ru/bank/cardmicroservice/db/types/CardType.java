package ru.bank.cardmicroservice.db.types;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Тип карты")
public enum CardType {
    DEBIT, CREDIT
}
