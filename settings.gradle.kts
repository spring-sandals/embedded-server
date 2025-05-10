rootProject.name = "embedded-server"

include(
    "services:spring-server",
    "services:spring-server-kafka",
    "services:spring-server-oauth",
    "services:spring-server-postgresql",
    "services:spring-server-rabbitmq",
    "services:spring-server-redis",
    "services:spring-server-resources",
    "services:spring-server-zeromq",

    "components:accounts",
    "components:support-jdbc",
    "components:support-kafka",
    "components:support-metrics",
    "components:support-test",
    "components:support-web"
)
