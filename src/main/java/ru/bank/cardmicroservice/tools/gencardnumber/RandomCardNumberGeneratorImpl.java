package ru.bank.cardmicroservice.tools.gencardnumber;

import lombok.RequiredArgsConstructor;
import java.security.SecureRandom;

@RequiredArgsConstructor
public class RandomCardNumberGeneratorImpl implements CardNumberGenerator {
    private final int CARD_NUMBER_LENGTH;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public String generateCardNumber() {
        StringBuilder sb = new StringBuilder(CARD_NUMBER_LENGTH);
        for (int i = 0; i < CARD_NUMBER_LENGTH; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }

    @Override
    public boolean isValidCardNumber(String cardNumber) {
        if (cardNumber == null) {
            return false;
        }
        // Удаляем все нецифровые символы (пробелы, дефисы)
        String digits = cardNumber.replaceAll("\\D", "");

        // Проверка длины
        return digits.length() == CARD_NUMBER_LENGTH;
    }
}
