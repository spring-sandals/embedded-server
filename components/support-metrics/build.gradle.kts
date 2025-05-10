val dropwizardVersion: String by project
val jacksonVersion: String by project
val jettyVersion: String by project
val springVersion: String by project

dependencies {
    implementation(project(":components:support-jdbc"))
    implementation(project(":components:support-web"))

    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("io.dropwizard.metrics:metrics-core:$dropwizardVersion")
    implementation("org.springframework:spring-webmvc:$springVersion")

    testImplementation(project(":components:support-test"))
}