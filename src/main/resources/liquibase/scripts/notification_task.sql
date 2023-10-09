-- liquibase formatted sql

-- changeset Angel:1
    CREATE TABLE notification_task (
        id BIGSERIAL PRIMARY KEY,
        chat_id BIGINT NOT NULL,
        notification_text TEXT NOT NULL,
        send_date_time TIMESTAMP NOT NULL
);