# Spring Sandals - embedded server

A kotlin-jetty-spring example with a small set of runtime dependencies.

```kotlin
private fun springServletContextHandler(configLocations: String): ServletContextHandler {
   val context = AnnotationConfigWebApplicationContext().apply {
      setConfigLocation(configLocations)
   }
   val contextHandler = ServletContextHandler().apply {
      contextPath = "/"
      addServlet(ServletHolder(DispatcherServlet(context)), "/*")
      addEventListener(ContextLoaderListener(context))
   }
   return contextHandler
}
```

## Getting started

Install dependencies.

- [Java 21](https://adoptium.net/temurin/releases/?version=21)
- [Flyway](https://documentation.red-gate.com/fd)
- [Docker Desktop](https://www.docker.com/products/docker-desktop)
- [Postgres 15](https://www.postgresql.org/)

_please refer to dependency documentation if any of the toolchain is new._

1. Set up the database.
   ```bash
   psql postgres < ./databases/create_databases.sql
   flyway -reportEnabled=false -cleanDisabled=false -user=initialdev -password=initialdev -url="jdbc:postgresql://localhost/sandals_development" -locations=filesystem:databases/spring clean migrate
   flyway -reportEnabled=false -cleanDisabled=false -user=initialdev -password=initialdev -url="jdbc:postgresql://localhost/sandals_test" -locations=filesystem:databases/spring clean migrate
   ```

1. Build the application.
    ```bash
    ./gradlew clean build
    ```

1. Build and run the application locally.
   ```bash
   ./gradlew build
   source .env.example
   java -jar services/spring-server/build/libs/spring-server.jar
   ```

## Docker

1. Build with Docker.
   ```bash
    docker build -t spring-suite . --platform linux/amd64
    ```

1. Run with Docker.
   ```bash
   docker run -p 8888:8888 --env-file .env.docker.example spring-suite
   ```