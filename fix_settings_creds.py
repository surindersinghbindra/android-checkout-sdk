with open('settings.gradle.kts', 'r') as f:
    content = f.read()

repo_block = """        maven {
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
        }"""

import re
content = re.sub(r'        maven \{\n            name = "GitHubPackages"\n            url = uri\("https://maven\.pkg\.github\.com/surindersinghbindra/android-checkout-sdk"\)\n            credentials \{\n                username = System\.getenv\("GITHUB_ACTOR"\)\n                password = System\.getenv\("GITHUB_TOKEN"\)\n            \}\n        \}', repo_block, content)

with open('settings.gradle.kts', 'w') as f:
    f.write(content)

