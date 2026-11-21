package ru.bank.cardmicroservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.bank.cardmicroservice.tools.gencardnumber.CardNumberGenerator;
import ru.bank.cardmicroservice.tools.gencardnumber.RandomCardNumberGeneratorImpl;

@Configuration
public class ToolsConfiguration {
    @Bean
    public CardNumberGenerator cardNumberGenerator() {
        return new RandomCardNumberGeneratorImpl(16);
    }
}
