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

### Queue and Worker

RabbitMQ was used to provide the queue while Java and Spring Boot were used to develop the Worker service.

Similar to the Api service, Spring Boot was chosen due to the ease of integrating a Spring Boot based application with RabbitMQ. A listener can be easily created and when a message arrives on the queue being listened to, the associated function / method will run. This makes it easy to have a Spring Boot application consume and process messages as they arrive on the queue.

### Database

Postgres was used to provide a SQL based database to store the events fed into the Api service. Postgres is a very commonly used SQL database and requires minimal setup to run successfully within a docker container. This ease of set up was one of the primary reasons for choosing it.

A NoSQL database like MongoDB could be an alternative if we are able to treat each 'game event' as a document as opposed to say something more akin to a transaction, which would leverage a SQL database's strengths.

### System Structure

