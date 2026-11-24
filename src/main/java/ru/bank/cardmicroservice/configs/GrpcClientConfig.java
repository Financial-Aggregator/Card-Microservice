package ru.bank.cardmicroservice.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;
import ru.bank.cardmicroservice.proto.card.account.ClientCardAccountServiceGrpc;
import ru.bank.cardmicroservice.proto.card.user.ClientCardUserGrpc;

@Configuration
public class GrpcClientConfig {
    @Bean
    public ClientCardAccountServiceGrpc.ClientCardAccountServiceBlockingStub clientCardAccountServiceBlockingStub(
            GrpcChannelFactory channels
    ) {
        return  ClientCardAccountServiceGrpc.newBlockingStub(
                channels.createChannel("account")
        );
    }

    @Bean
    public ClientCardAccountServiceGrpc.ClientCardAccountServiceStub clientCardAccountServiceAsyncStub(
            GrpcChannelFactory channels
    ) {
        return  ClientCardAccountServiceGrpc.newStub(
                channels.createChannel("account")
        );
    }

    @Bean
    public ClientCardUserGrpc.ClientCardUserStub clientCardUserAsyncStub(
            GrpcChannelFactory channels
    ) {
        return ClientCardUserGrpc.newStub(
                channels.createChannel("user")
        );
    }

    @Bean
    public ClientCardUserGrpc.ClientCardUserBlockingStub clientCardUserBlockingStub(
            GrpcChannelFactory channels
    ) {
        return ClientCardUserGrpc.newBlockingStub(
                channels.createChannel("user")
        );
    }
}
