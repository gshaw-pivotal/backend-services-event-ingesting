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

### Startup order

### Api

To start the `api` service:

```bash
cd api
docker run -p 8800:8800 game-event-api-service:latest
```
