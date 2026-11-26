create type card_type as enum (
    'DEBIT', 'CREDIT'
);

create type card_payment_system as enum (
    'MIR', 'VISA', 'MASTERCARD'
);

create type card_status as enum (
    'ACTIVE', 'SUSPENDED', 'CLOSED', 'EXPIRED'
);

create table card (
    id UUID primary key,
    user_id UUID not null,
    account_id UUID not null ,
    number varchar(16) not null unique,
    type card_type not null,
    payment_system card_payment_system not null,
    expiry_date date not null,
    status card_status not null,
    is_primary boolean not null;
);