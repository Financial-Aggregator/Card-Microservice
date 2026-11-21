package ru.bank.cardmicroservice.db.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.bank.cardmicroservice.db.types.CardPaymentSystem;
import ru.bank.cardmicroservice.db.types.CardStatus;
import ru.bank.cardmicroservice.db.types.CardType;
import java.time.LocalDate;
import java.util.UUID;


@Entity
@Setter @Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@Table(name = "card")
public class Card {
    public Card() {}

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(name = "number", nullable = false, unique = true)
    private String number;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "type", nullable = false, columnDefinition = "card_type")
    private CardType type;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "payment_system", nullable = false, columnDefinition = "card_payment_system")
    private CardPaymentSystem paymentSystem;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false, columnDefinition = "card_status")
    private CardStatus status;
}
