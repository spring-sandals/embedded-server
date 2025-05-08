val dropwizardVersion: String by project
val jacksonVersion: String by project
val jettyVersion: String by project
val springVersion: String by project

dependencies {
    implementation(project(":components:jdbc-support"))
    implementation(project(":components:web-support"))

    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("io.dropwizard.metrics:metrics-core:$dropwizardVersion")
    implementation("org.springframework:spring-webmvc:$springVersion")

    testImplementation(project(":components:test-support"))
}