package ru.bank.cardmicroservice.tools.gencardnumber;

public interface CardNumberGenerator {
    String generateCardNumber();
    boolean isValidCardNumber(String cardNumber);
}
