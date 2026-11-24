package ru.bank.cardmicroservice.exceptions;

public class NoValidExeption extends RuntimeException {
    public NoValidExeption(String message) {
        super(message);
    }
}
