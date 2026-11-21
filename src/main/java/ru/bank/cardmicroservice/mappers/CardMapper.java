package ru.bank.cardmicroservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.bank.cardmicroservice.api.dto.CreateCardRequest;
import ru.bank.cardmicroservice.api.dto.CreateCardResponse;
import ru.bank.cardmicroservice.api.dto.GetCardInfoResponse;
import ru.bank.cardmicroservice.db.entities.Card;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CardMapper {
    Card cardCreateRequestToCard(CreateCardRequest request);
    CreateCardResponse cardToCreateCardResponse(Card card);
    GetCardInfoResponse cardToGetCardInfoResponse(Card card);
}
