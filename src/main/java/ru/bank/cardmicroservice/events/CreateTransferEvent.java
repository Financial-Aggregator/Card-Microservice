package ru.bank.cardmicroservice.events;

import lombok.*;
import ru.bank.cardmicroservice.api.types.TransferType;
import java.util.UUID;

@Builder
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransferEvent {
    private UUID eventId;
    private UUID fromCardId;
    private UUID toCardId;
    private TransferType transferType;
    private int amount;
}
