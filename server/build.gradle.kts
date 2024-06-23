plugins {
  alias(libs.plugins.kotlinJvm)
  alias(libs.plugins.ktor)
  alias(libs.plugins.kotlinxSerialization)
  application
}

group = "org.example.project"
version = "1.0.0"
application {
  mainClass.set("org.example.project.ApplicationKt")
  applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["development"] ?: "false"}")
}

kotlin {
  compilerOptions {
    freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
    freeCompilerArgs.add("-Xcontext-receivers")
    freeCompilerArgs.add("-Xenable-builder-inference")
  }
}

dependencies {
  implementation(projects.shared)
  implementation(libs.logback)
  implementation(libs.ktor.server.core)
  implementation(libs.ktor.server.netty)
  implementation(libs.ktor.server.content.negotiation)
  implementation(libs.ktor.serialization.kotlinx.json)
  implementation(libs.ktor.server.auth)
  implementation(libs.ktor.server.sessions)
  implementation(libs.ktor.client.content.negotiation)
  implementation(libs.ktor.client.cio)
  implementation(libs.firebase.admin)

  testImplementation(libs.ktor.server.tests)
  testImplementation(libs.kotlin.test.junit)
}