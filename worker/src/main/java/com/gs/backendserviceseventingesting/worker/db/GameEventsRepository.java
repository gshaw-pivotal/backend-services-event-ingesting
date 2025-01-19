package com.gs.backendserviceseventingesting.worker.db;

import com.gs.backendserviceseventingesting.worker.model.Event;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameEventsRepository extends CrudRepository<Event, UUID> {
}
