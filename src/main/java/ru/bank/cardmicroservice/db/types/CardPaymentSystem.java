package ru.bank.cardmicroservice.db.types;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Платежная система")
public enum CardPaymentSystem {
    MIR, VISA, MASTERCARD
}
