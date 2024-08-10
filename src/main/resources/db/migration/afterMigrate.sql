ALTER SEQUENCE account_id_seq RESTART WITH 1;
ALTER SEQUENCE transaction_id_seq RESTART WITH 1;
ALTER SEQUENCE merchant_id_seq RESTART WITH 1;

delete from account;
delete from transaction;

insert into account (balance_cash, balance_food, balance_meal)
values (1000.00, 500.00, 250.00),
       (2000.00, 1000.00, 500.00),
       (3000.00, 1500.00, 750.00);

insert into merchant (name, city, country, mcc)
values ('UBER TRIP', 'SAO PAULO', 'BR', '9999'),
       ('UBER EATS', 'SAO PAULO', 'BR', '5411'),
       ('PAG*JoseDaSilva', 'RIO DE JANEI', 'BR', '5812'),
       ('PICPAY*BILHETEUNICO', 'GOIANIA', 'BR', '9999');
