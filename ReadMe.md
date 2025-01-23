## Building Services

### Api

Located in the `api` sub-directory.

The `api` service is set to run on port 8800. To change this you need to modify `application.properties` (see `server.port`), the Dockerfile (see `EXPOSE`) and the docker image run command.

```bash
cd api
mvn clean package
docker image build -t game-event-api-service:latest .
```

### Worker

Located in the `worker` sub-directory.

The `worker` service is set to run on port 8801. To change this you need to modify `application.properties` (see `server.port`), the Dockerfile (see `EXPOSE`) and the docker image run command.

```bash
cd worker
mvn clean package
docker image build -t game-event-worker-service:latest .
```

## Running Services

### Using Docker Compose

Before running the Docker Compose file it is necessary to build the `api` and `worker` services. This can be done in the respective directories of each of these services. To build these applications run the following:

```bash
mvn clean package
```

To start up all services from one location, run the following command from the root of the repo:

```bash
docker compose up -d
```

To shut down all services run:

```bash
docker compose down
```

### Individual Services

The `api` service and `worker` service can be started without the use of the `compose.yaml` file.

#### Api

To start the `api` service:

```bash
cd api
docker run -p 8800:8800 game-event-api-service:latest
```

#### Worker

To start the `worker` service:

```bash
cd worker
docker run -p 8801:8801 game-event-worker-service:latest
```

## Accessing the DB with Docker

The Postgres DB runs in its own docker container. It can be accessed via a terminal via the following:

Use `docker ps` to get the list of containers and look for the id or name of the container from which the DB is executing.

The included `compose.yaml` sets the container name to `game-event-db` by default, so if this has not been changed you should be able to use this name.

Run `docker exec -it <container_id_or_container_name> bash` to access a command line within the container.

Then execute `psql -U <postgres_username> -d <postgres_database_name>`. These are configured to `postgres` and `game-events` respectively in the included `compose.yaml` file.

From this command line you can now execute queries against the Postgres DB (such as `select * from events;`).

## Technology

Most decisions regarding technology and development languages were chosen due to familiarity with them and aiming for a simple approach that does not require a complex or large amount of set up.

Other tech stacks and development languages could be chosen and achieve a working outcome. For example Node.js could be used for the Api and Worker services. However, Node.js would potentially be a less than ideal choice if either of the services needed to perform a lot of computations as part of preping an event to be sent to the queue or processing an event off the queue. Is would be due to Node.js being single threaded. Java and Spring Boot are multi-threaded and thus can handle work that require CPU processing without degrading performance for other users / requests.

### API

Java and Spring Boot were used to develop the Api service.

Spring Boot; with Spring Boot Starter Web; is a common approach to building and providing REST based APIs. It is commonly used with Java and/or Kotlin.

Spring Boot also integrates with RabbitMQ via Spring Boot Starter Amqp which makes it easier to have a Spring Boot based service integrate with RabbitMQ messaging / queuing. This ease of integration makes it easy to have the Api service send messages to a RabbitMq queue for another service to consume. Thus the level of setup and configuration required within the application is minimal.

An alternative to using Java and Spring Boot could be Node.js, especially if the Api service has minimal processing work.

### Queue and Worker

RabbitMQ was used to provide the queue while Java and Spring Boot (with Spring Boot Starter Amqp) were used to develop the Worker service.

Similar to the Api service, Spring Boot was chosen due to the ease of integrating a Spring Boot based application with RabbitMQ. A listener can be easily created and when a message arrives on the queue being listened to, the associated function / method will run. This makes it easy to have a Spring Boot application consume and process messages as they arrive on the queue.

Similar to the Api service, if the processing requirements are minimal, Node.js may be an option. However, we went with the assumption that there will be some form of data processing by the Worker service when processing events from the queue and thus a multi-threaded technology stack, like Java and Spring Boot is the better option longer term as the complexity of the game event data structure and queue processing grows.

### Database

Postgres was used to provide a SQL based database to store the events fed into the Api service. Postgres is a very commonly used SQL database and requires minimal setup to run successfully within a docker container. This ease of set up was one of the primary reasons for choosing it.

A NoSQL database like MongoDB could be an alternative if we are able to treat each 'game event' as a document as opposed to say something more akin to a transaction, which would leverage a SQL database's strengths.

### System Structure

The system is made of 4 main parts:
- Api service, which handles the requests to save/store game events and retrieve previously saved/stored game events. The Api service validates a save request and then publishes onto a queue for later processing
- Queue, using RabbitMQ the Api service publishes to this queue and the Worker service consumes / listens to this queue. The game event is published and consumed via the queue. This allows for asynchronous processing of the game events.
- Worker service, which listens to the queue and when a message (game event) is present, consumes it and processes it, ultimately storing it in the database. The Worker service also has a basic retry mechanism should processing of an event from the message queue fail. The event will be put back on the queue for processing again. Each message will get 5 attempts after which it will be discarded.
- Database, a SQL database using Postgres to store all of the game events that are saved to it via the Worker service. The Api service also queries this database to get the details of a particular game event.

As mentioned previously, the design is based around keeping the system simple while maintaining the separation between components.

A `Game Event` currently contains a set of key fields:
- `playerId` an unique identifier for the player that generated the event. The player is identified via a UUID.
- `eventId` an unique identifier that identifies a given game event. The Api service generates this UUID for each new game event that it receives through its POST endpoint. This UUID is used by the Api service to query the database when it is being asked to return the details for a given game event.
- `timestamp` the timestamp of when the event happens, stored as a Long using the milliseconds since epoch. This simplifies processing by all components in the system and means none of the components have to directly concern themselves with timezones. This timestamp is provided in the request sent to the POST endpoint of the Api service
- `eventCode` a string that identifies the type of game event a particular event is. This code is limited to a length of 10 characters.
- `eventDesc` a string that provides a description about what the game event is. There is no limit to the length of this description beyond practical limitations of string representation in Java or text type in Postgres.

This structure can be expanded in the future as the nature of game events evolve.

In the docker compose specification we have set up both the Api and Worker services to have dependencies on the database and will not start until the database is running and healthy. Without a running database and given this system's current configuration, there would be no value in having either of these services running and operating, hence this dependency which will stop a user from submitting new game events when there is no way for them to be persisted into the database.

The Api service is responsible for ensuring there is an appropriate table in the database via its `schema.sql` file. This could easily be done in either the Api or Worker service and there is no real advantage of one over the other as long as only one service is responsible for this. Duplicating this would just cause unnecessary work (creating twice).