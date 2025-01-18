CREATE TABLE IF NOT EXISTS EVENTS (
    eventId UUID PRIMARY KEY,
    playerId UUID NOT NULL,
    eventTime integer,
    eventCode varchar(10),
    eventDesc text
);