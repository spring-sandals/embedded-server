val dropwizardVersion: String by project
val freemarkerVersion: String by project
val jacksonVersion: String by project
val jettyVersion: String by project
val rabbitVersion: String by project
val springAMQPVersion: String by project
val springVersion: String by project

dependencies {
    implementation(project(":components:support-metrics"))
    implementation(project(":components:support-rabbitmq"))
    implementation(project(":components:support-web"))
    implementation(project(":services:spring-server-resources"))

    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("io.dropwizard.metrics:metrics-core:$dropwizardVersion")
    implementation("org.freemarker:freemarker:$freemarkerVersion")
    implementation("org.springframework:spring-context:$springVersion")
    implementation("org.springframework:spring-webmvc:$springVersion")

    implementation("com.rabbitmq:amqp-client:$rabbitVersion")
    implementation("org.springframework.amqp:spring-rabbit:$springAMQPVersion")

    testImplementation(project(":components:support-test"))
}

tasks.register<JavaExec>("run", fun JavaExec.() {
    classpath = files(tasks.jar)
})

tasks {
    jar {
        manifest { attributes("Main-Class" to "com.sandals.server.ApplicationKt") }
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        from({
            configurations.runtimeClasspath.get()
                .filter { it.name.endsWith("jar") }
                .map(::zipTree)
        })
    }
}