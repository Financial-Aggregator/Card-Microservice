package ru.bank.cardmicroservice.api.dto;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final  String type;
    private final  String error;

    public ErrorResponse(Throwable t) {
        this.type = t.getClass().getSimpleName();
        this.error = t.getMessage();
    }
}
