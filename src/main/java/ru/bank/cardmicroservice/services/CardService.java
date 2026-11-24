package ru.bank.cardmicroservice.services;

import io.grpc.Context;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.bank.cardmicroservice.api.dto.CreateCardRequest;
import ru.bank.cardmicroservice.api.dto.CreateTransferByCardNumberRequest;
import ru.bank.cardmicroservice.api.dto.CreateTransferByPhoneNumberRequest;
import ru.bank.cardmicroservice.api.dto.GetCardInfoResponse;
import ru.bank.cardmicroservice.configs.ApplicationProperties;
import ru.bank.cardmicroservice.db.entities.Card;
import ru.bank.cardmicroservice.db.types.CardStatus;
import ru.bank.cardmicroservice.exceptions.AccessDeniedException;
import ru.bank.cardmicroservice.exceptions.FailedCreateCardNumberException;
import ru.bank.cardmicroservice.exceptions.FailedGrpcRequestException;
import ru.bank.cardmicroservice.exceptions.NotFoundException;
import ru.bank.cardmicroservice.mappers.CardMapper;
import ru.bank.cardmicroservice.proto.card.account.ClientCardAccountServiceGrpc;
import ru.bank.cardmicroservice.proto.card.account.GetCardHolderRequest;
import ru.bank.cardmicroservice.proto.card.account.GetCardHolderResponse;
import ru.bank.cardmicroservice.proto.card.user.ClientCardUserGrpc;
import ru.bank.cardmicroservice.proto.card.user.IsExistUserByIdRequest;
import ru.bank.cardmicroservice.proto.card.user.IsExistUserByIdResponse;
import ru.bank.cardmicroservice.repositories.CardRepository;
import ru.bank.cardmicroservice.tools.gencardnumber.CardNumberGenerator;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final ApplicationProperties applicationProperties;
    private final ClientCardAccountServiceGrpc.ClientCardAccountServiceBlockingStub clientCardAccountBlockStub;
    private final ClientCardAccountServiceGrpc.ClientCardAccountServiceStub clientCardAccountAsyncStub;
    private final ClientCardUserGrpc.ClientCardUserStub clientCardUserAsyncStub;
    private final CardNumberGenerator cardNumberGenerator;

    public Card createCard(UUID userId, CreateCardRequest request)
            throws FailedCreateCardNumberException, AccessDeniedException, NotFoundException
    {
        CompletableFuture<Void> futureAccount = new CompletableFuture<>();
        CompletableFuture<Void> futureUser = new CompletableFuture<>();
        Context.CancellableContext ctxAccount = Context.current().withCancellation();
        Context.CancellableContext ctxUser = Context.current().withCancellation();

        ctxAccount.run(() -> {
            UUID accountId = request.getAccountId();
            clientCardAccountAsyncStub.getCardHolder(
                    GetCardHolderRequest.newBuilder().setAccountId(accountId.toString()).build(),
                    new StreamObserver<GetCardHolderResponse>() {
                        @Override
                        public void onNext(GetCardHolderResponse value) {
                            UUID cardHolderId = UUID.fromString(value.getUserId());
                            if (!isCardHolder(cardHolderId, userId)) {
                                ctxUser.cancel(
                                        new AccessDeniedException("Пользователь не является держателем счета")
                                );
                            }
                            futureAccount.complete(null);
                        }

                        @Override
                        public void onError(Throwable t) {
                            Status status = Status.fromThrowable(t);
                            Status.Code code = status.getCode();

                            if (code == Status.Code.NOT_FOUND) {
                                futureAccount.completeExceptionally(
                                        new NotFoundException(
                                                String.format("Счета с id=%s не существует", accountId)
                                        )
                                );
                            } else {
                                futureAccount.completeExceptionally(t);
                            }

                            ctxAccount.cancel(null);
                        }

                        @Override
                        public void onCompleted() {
                            ctxAccount.cancel(null);
                        }
                    }
            );
        });
        ctxUser.run(() -> {
            clientCardUserAsyncStub.isExistUserById(
                    IsExistUserByIdRequest.newBuilder().setId(userId.toString()).build(),
                    new StreamObserver<IsExistUserByIdResponse>() {
                        @Override
                        public void onNext(IsExistUserByIdResponse value) {
                            if (!value.getResult()) {
                                ctxAccount.cancel(
                                        new NotFoundException(
                                            String.format("Пользователь с userId = %s не существует", userId)
                                        )
                                );
                            }
                            futureUser.complete(null);
                        }

                        @Override
                        public void onError(Throwable t) {
                            futureUser.completeExceptionally(t);
                            ctxUser.cancel(null);
                        }

                        @Override
                        public void onCompleted() {
                            ctxUser.cancel(null);
                        }
                    }
            );
        });

        try {
            CompletableFuture.allOf(futureAccount, futureUser).join();
        } catch (CompletionException e) {
            throw (RuntimeException) e.getCause();
        }

        Card card = cardMapper.cardCreateRequestToCard(request).toBuilder()
                .status(CardStatus.ACTIVE)
                .number(cardNumberGenerator.generateCardNumber())
                .expiryDate(
                        LocalDate.now().plus(
                            applicationProperties.getCard().getExpiryPeriod()
                        )
                ).build();
        int attempt_count = applicationProperties.getCard().getRetryCount();
        for (int attempt = 0; attempt < attempt_count; attempt++) {
            try {
                return cardRepository.save(card);
            } catch (DataIntegrityViolationException e) {
                card = card.toBuilder().number(cardNumberGenerator.generateCardNumber()).build();
            }
        }
        throw new FailedCreateCardNumberException(
                String.format("Не удалось сгенерировать уникальный номер карты за %d попыток", attempt_count)
        );
    }


    public Card getCardInfo(UUID userId, UUID cardId)
            throws NotFoundException, AccessDeniedException, FailedGrpcRequestException
    {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(
                        () -> new NotFoundException(
                                String.format("Карты с id=%s не существует", cardId)
                        )
                );

        if (card.getUserId().equals(userId)) {
            return card;
        }

        try {
            GetCardHolderResponse response = clientCardAccountBlockStub.getCardHolder(
                    GetCardHolderRequest.newBuilder()
                            .setAccountId(card.getAccountId().toString())
                            .build()
            );

            if (!isCardHolder(UUID.fromString(response.getUserId()), userId)) {
                throw new AccessDeniedException("У вас не прав на получение информации о карте");
            }
        } catch (StatusRuntimeException e) {
            throw new FailedGrpcRequestException(
                    e.getStatus().getCode(), "Неизвестная ошибка во время обращения в сервис счетов"
            );
        }

        return card;
    }

    public void createTransferByPhoneNumber(CreateTransferByPhoneNumberRequest request, UUID userId) {

    }

    public void createTransferByCardNumber(CreateTransferByCardNumberRequest request, UUID userId) {
    }

    private boolean isCardHolder(UUID cardHolder, UUID userId) {
        return cardHolder.equals(userId);
    }
}
