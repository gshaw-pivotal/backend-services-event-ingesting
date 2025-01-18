## Building Services

### Api

Located in the `api` sub-directory.

The `api` service is set to run on port 8800. To change this you need to modify `application.properties` (see `server.port`), the Dockerfile (see `EXPOSE`) and the docker image run command.

```bash
cd api
mvn package
docker image build -t game-event-api-service:latest .
```

## Running Services

### Using Docker Compose

To start up all services from one location, run the following command from the root of the repo:

```bash
docker compose up -d
```

To shut down all services run:

```bash
docker compose down
```

### Startup order

When starting separately and NOT using `docker compose` with the `compose.yaml` file provided.

### Api

To start the `api` service:

```bash
cd api
docker run -p 8800:8800 game-event-api-service:latest
```

## Accessing the DB with Docker

The Postgres DB runs in its own docker container. It can be accessed via a terminal via the following:

Use `docker ps` to get the list of containers and look for the id or name of the container from which the DB is executing.

The included `compose.yaml` sets the container name to `game-event-db` by default, so if this has not been changed you should be able to use this name.

Run `docker exec -it <container_id_or_container_name> bash` to access a command line within the container.

Then execute `psql -U <postgres_username> -d <postgres_database_name>`. These are configured to `postgres` and `game-events` respectively in the included `compose.yaml` file.

From this command line you can now execute queries against the Postgres DB (such as `select * from events;`).