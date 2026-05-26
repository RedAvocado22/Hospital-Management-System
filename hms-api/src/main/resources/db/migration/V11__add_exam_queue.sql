CREATE TABLE queue
(
    id         BINARY(16)   NOT NULL,
    created_at datetime     NULL,
    updated_at datetime     NULL,
    patient_id BINARY(16)   NOT NULL,
    date       date         NOT NULL,
    position   INT          NOT NULL,
    status     VARCHAR(255) NOT NULL,
    CONSTRAINT pk_queue PRIMARY KEY (id),
    CONSTRAINT fk_queue_patient FOREIGN KEY (patient_id) REFERENCES patient_info(id)
);