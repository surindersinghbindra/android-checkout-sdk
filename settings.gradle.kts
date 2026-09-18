pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal() // For local testing before GitHub packages is ready
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/surindersinghbindra/android-checkout-sdk")
            credentials {
                val gprUser = System.getenv("GITHUB_ACTOR") ?: providers.gradleProperty("gpr.user").orNull
                val gprKey = System.getenv("GITHUB_TOKEN") ?: providers.gradleProperty("gpr.key").orNull
                if (gprUser != null && gprKey != null) {
                    username = gprUser
                    password = gprKey
                }
            }
        }
    }
}

rootProject.name = "ECommerceSample"
include(":app")
include(":library:designsystem")
include(":core:network")
include(":core:database")
include(":core:domain")
include(":feature:booking")
include(":sdk:checkout-core")
include(":sdk:checkout-ui")
include(":app-headless")
