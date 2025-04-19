val hikariVersion: String by project
val postgresqlVersion: String by project
val springVersion: String by project

dependencies {
    implementation("org.postgresql:postgresql:$postgresqlVersion")
    implementation("org.springframework:spring-jdbc:$springVersion")
    implementation("com.zaxxer:HikariCP:$hikariVersion")
}