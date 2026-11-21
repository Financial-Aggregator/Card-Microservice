package ru.bank.cardmicroservice.exceptions;

import io.grpc.Status;
import lombok.Getter;

public class FailedGrpcRequestException extends RuntimeException {
    @Getter
    private final Status.Code code;

    public FailedGrpcRequestException(Status.Code code, String message) {
        super(message);
        this.code = code;
    }
}
