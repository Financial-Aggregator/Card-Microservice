package ru.bank.cardmicroservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bank.cardmicroservice.db.entities.Card;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {}
