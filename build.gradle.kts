plugins {
    kotlin("jvm") version "2.1.0"
}

repositories {
    mavenCentral()
}

subprojects {
    if (listOf("components", "services").contains(name)) return@subprojects

    apply(plugin = "kotlin")

    repositories {
        mavenCentral()
    }

    val logbackVersion: String by project
    val slf4jVersion: String by project
    val springVersion: String by project

    dependencies {
        implementation("ch.qos.logback:logback-classic:$logbackVersion")
        implementation("org.slf4j:slf4j-api:$slf4jVersion")
        implementation("org.springframework:spring-context:$springVersion")
        implementation("org.springframework:spring-context-support:$springVersion")

        testImplementation(kotlin("test-junit"))
    }
}