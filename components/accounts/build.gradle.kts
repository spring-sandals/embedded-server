val jacksonVersion: String by project
val jettyVersion: String by project
val springVersion: String by project

dependencies {
    implementation(project(":components:jdbc-support"))
    implementation(project(":components:web-support"))

    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("org.springframework:spring-jdbc:$springVersion")
    implementation("org.springframework:spring-webmvc:$springVersion")
}