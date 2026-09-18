import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm")
    id("maven-publish")
}

dependencies {
    implementation(libs.coroutines.core)
    compileOnly("androidx.annotation:annotation:1.7.1")
    testImplementation(libs.junit)
    testImplementation(libs.mockk.main)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.caribeanroyal.ecommercesample"
            artifactId = "checkout-core"
            version = (project.findProperty("version") as? String)?.takeIf { it != "unspecified" } ?: "1.0.0-SNAPSHOT"
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/surindersinghbindra/android-checkout-sdk")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_1_8
    }
}
