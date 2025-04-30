val dropwizardVersion: String by project
val freemarkerVersion: String by project
val jacksonVersion: String by project
val jettyVersion: String by project
val springVersion: String by project

dependencies {
    implementation(project(":components:accounts"))
    implementation(project(":components:jdbc-support"))
    implementation(project(":components:metrics-support"))
    implementation(project(":components:test-support"))
    implementation(project(":components:web-support"))

    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("io.dropwizard.metrics:metrics-core:$dropwizardVersion")
    implementation("org.freemarker:freemarker:$freemarkerVersion")
    implementation("org.springframework:spring-jdbc:$springVersion")
    implementation("org.springframework:spring-webmvc:$springVersion")
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
