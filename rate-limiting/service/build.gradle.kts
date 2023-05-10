val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val prometheus_version: String by project
val rabbitmq_version: String by project
val ktor_rabbitmq_version: String by project

group = "com.example"
version = "0.0.1"

plugins {
    kotlin("jvm") version "1.8.10"
    id("io.ktor.plugin") version "2.2.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"
}

ktor {
    docker {
        localImageName.set("rate-limiting-service")
    }
}

application {
    mainClass.set("com.example.service.ServiceKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-metrics-micrometer:$ktor_version")
    implementation("io.micrometer:micrometer-registry-prometheus:$prometheus_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("aws.sdk.kotlin:dynamodb:0.18.0-beta")
    implementation("com.rabbitmq:amqp-client:$rabbitmq_version")
    implementation("io.github.resilience4j:resilience4j-kotlin:2.0.2")
    implementation("io.github.resilience4j:resilience4j-ratelimiter:2.0.2")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}