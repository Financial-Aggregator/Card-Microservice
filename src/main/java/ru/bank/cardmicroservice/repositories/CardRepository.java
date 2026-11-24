package ru.bank.cardmicroservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.bank.cardmicroservice.db.entities.Card;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {
    @Query("select c from Card c where c.userId = :userId and c.isPrimary = true")
    Optional<Card> findPrimaryCardByUserId(@Param("userId") UUID userId);
    Optional<Card> findByNumber(String number);
}
