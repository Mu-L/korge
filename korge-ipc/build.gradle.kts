import korlibs.korge.gradle.targets.android.*
import korlibs.root.*

plugins {
    //id "kotlin" version "1.6.21"
    kotlin("jvm")
    //kotlin("plugin.serialization")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
    //id "org.jetbrains.kotlin.jvm"
    id("maven-publish")
}

description = "Multiplatform Game Engine written in Kotlin"
group = RootKorlibsPlugin.KORGE_RELOAD_AGENT_GROUP

val jversion = GRADLE_JAVA_VERSION_STR

java {
    setSourceCompatibility(jversion)
    setTargetCompatibility(jversion)
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class).all {
    kotlinOptions {
        jvmTarget = jversion
        apiVersion = "1.8"
        languageVersion = "1.8"
        suppressWarnings = true
    }
}

publishing {
    publications {
        val maven by creating(MavenPublication::class) {
            groupId = group.toString()
            artifactId = "korge-ipc"
            version = version
            from(components["kotlin"])
        }
    }
}

val publishJvmPublicationToMavenLocal = tasks.register("publishJvmPublicationToMavenLocal", Task::class) {
    group = "publishing"
    dependsOn("publishMavenPublicationToMavenLocal")
}

afterEvaluate {
    if (tasks.findByName("publishMavenPublicationToMavenRepository") != null) {
        tasks.register("publishJvmPublicationToMavenRepository", Task::class) {
            group = "publishing"
            dependsOn("publishMavenPublicationToMavenRepository")
        }
    }
}

korlibs.NativeTools.groovyConfigurePublishing(project, false)
korlibs.NativeTools.groovyConfigureSigning(project)

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)
    //implementation(libs.korlibs.all)
    //implementation(libs.korlibs.datastructure.core)
    //implementation(libs.korlibs.io.stream)
    testImplementation(libs.bundles.kotlin.test)
}

tasks { val jvmTest by creating { dependsOn("test") } }
