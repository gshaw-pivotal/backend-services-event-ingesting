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

### Using Compose

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
