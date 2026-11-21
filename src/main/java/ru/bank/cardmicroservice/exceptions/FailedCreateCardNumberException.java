package ru.bank.cardmicroservice.exceptions;

public class FailedCreateCardNumberException extends RuntimeException {
    public FailedCreateCardNumberException(String message) {
        super(message);
    }
}
