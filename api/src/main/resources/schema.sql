DROP TABLE EVENTS;

CREATE TABLE IF NOT EXISTS EVENTS (
    id UUID PRIMARY KEY,
    player_id UUID NOT NULL,
    event_time integer,
    event_code varchar(10),
    event_desc text
);

-- INSERT INTO
--     EVENTS(id, player_id, event_time, event_code, event_desc)
-- VALUES ('f98545a5-4928-45e4-bbd3-8d64123165dd', 'c0af492e-ad57-4f31-b641-540a2fa05197', 1737181447, 'A001', 'Event description here');