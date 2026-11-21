package ru.bank.cardmicroservice.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.time.Period;

@Component
@Setter @Getter
@ConfigurationProperties(prefix = "app")
public class ApplicationProperties {
    private CardProperties card;

    @Getter @Setter
    public static class CardProperties {
        private Period expiryPeriod;
        private int cardNumberSize;
        private int retryCount;
    }
}
