package com.gs.backendserviceseventingesting.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@Entity
@Table(name = "EVENTS")
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    private UUID id;

    private UUID player_id;

    private long event_time;

    private String event_code;

    private String event_desc;
}
