plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    id("maven-publish")
}

android {
    namespace = "com.caribeanroyal.ecommercesample.sdk.checkout.core"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        consumerProguardFiles("consumer-rules.pro")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation("androidx.annotation:annotation:1.7.1")
    
    api(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    
    api(libs.androidx.startup)

    testImplementation(libs.junit)
    testImplementation(libs.mockk.main)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.caribeanroyal.ecommercesample"
            artifactId = "checkout-core"
            version = (project.findProperty("version") as? String)?.takeIf { it != "unspecified" } ?: "1.0.0-SNAPSHOT"
            
            afterEvaluate {
                from(components["release"])
            }
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
