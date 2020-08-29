# immudb4j test

This test project uses [immudb4j](https://github.com/codenotary/immudb4j) for connecting to an ImmuDB instance to store/retrieve a bunch objects in a
safe/non-tamperable way (with JSON serialization).

This project requires:

- Java >= 8
- Docker and Docker Compose (optional)

## How to run

As first thing, run:

```
./mvnw package
```

to compile the project and generate a runnable JAR, that can be found under
`target/immudb4j-test-1.0-SNAPSHOT.jar`.

After running a local instance of immudb, the JAR file can be run with:

```
java -jar immudb4j-test-1.0-SNAPSHOT.jar
```

### Run via Docker

Alternatively, a stand-alone Docker Compose configuration is available too.

To run it, after issuing the `./mvnw package` command, enter:
 
```
# Build the Docker image with Google Jib
./mvwn jib:dockerBuild

# Start the project and a containerized immudb instance
docker-compose up
```
