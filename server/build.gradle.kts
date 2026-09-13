plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
}

group = "dev.ell.koogbasicagent"
version = "1.0.0"
application {
    mainClass = "dev.ell.koogbasicagent.ApplicationKt"
}

dependencies {
    api(project(":core"))
    api(project(":app:shared"))
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
}