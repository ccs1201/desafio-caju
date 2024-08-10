ALTER TABLE transaction
    ADD merchant_id BIGINT;

ALTER TABLE transaction
    ALTER COLUMN merchant_id SET NOT NULL;

ALTER TABLE transaction
    ADD CONSTRAINT FK_TRANSACTION_ON_MERCHANT FOREIGN KEY (merchant_id) REFERENCES merchant (id);

ALTER TABLE transaction
    DROP COLUMN merchant;