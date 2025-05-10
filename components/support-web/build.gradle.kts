val jettyVersion: String by project
val springVersion: String by project

dependencies {
    implementation("org.eclipse.jetty:jetty-server:$jettyVersion")
    implementation("org.eclipse.jetty:jetty-servlet:$jettyVersion")
    implementation("org.springframework:spring-webmvc:$springVersion")
}