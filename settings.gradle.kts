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
