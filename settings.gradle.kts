rootProject.name = "embedded-server"

include(
    "services:spring-server",
    "components:accounts",
    "components:jdbc-support",
    "components:metrics-support",
    "components:test-support",
    "components:web-support"
)
