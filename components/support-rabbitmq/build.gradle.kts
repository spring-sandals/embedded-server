val rabbitVersion: String by project
val springAMQPVersion: String by project

dependencies {
    implementation("com.rabbitmq:amqp-client:$rabbitVersion")
    implementation("org.springframework.amqp:spring-rabbit:$springAMQPVersion")
}