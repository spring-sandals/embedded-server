val kafkaClientsVersion: String by project
val springKafkaVersion: String by project

dependencies {
    implementation("org.apache.kafka:kafka-clients:$kafkaClientsVersion")
    implementation("org.springframework.kafka:spring-kafka:$springKafkaVersion")
}