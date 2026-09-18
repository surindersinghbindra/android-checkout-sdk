import re

with open('settings.gradle.kts', 'r') as f:
    settings = f.read()

repo_block = """    repositories {
        google()
        mavenCentral()
        mavenLocal() // For local testing before GitHub packages is ready
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/surindersinghbindra/android-checkout-sdk")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }"""
settings = re.sub(r'    repositories \{\n        google\(\)\n        mavenCentral\(\)\n    \}', repo_block, settings)

# We should also comment out the includes for sdk so it stops using the local ones!
settings = settings.replace('include(":sdk:checkout-core")', '// include(":sdk:checkout-core")')
settings = settings.replace('include(":sdk:checkout-ui")', '// include(":sdk:checkout-ui")')

with open('settings.gradle.kts', 'w') as f:
    f.write(settings)

# App build.gradle.kts
with open('app/build.gradle.kts', 'r') as f:
    app_build = f.read()

app_build = app_build.replace('implementation(project(":sdk:checkout-core"))', 'implementation("com.caribeanroyal.ecommercesample:checkout-core:1.0.0-SNAPSHOT")')
app_build = app_build.replace('implementation(project(":sdk:checkout-ui"))', 'implementation("com.caribeanroyal.ecommercesample:checkout-ui:1.0.0-SNAPSHOT")')

with open('app/build.gradle.kts', 'w') as f:
    f.write(app_build)

# Headless app build.gradle.kts
with open('app-headless/build.gradle.kts', 'r') as f:
    headless_build = f.read()

headless_build = headless_build.replace('implementation(project(":sdk:checkout-core"))', 'implementation("com.caribeanroyal.ecommercesample:checkout-core:1.0.0-SNAPSHOT")')

with open('app-headless/build.gradle.kts', 'w') as f:
    f.write(headless_build)
