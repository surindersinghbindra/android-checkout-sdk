with open('app-headless/build.gradle.kts', 'r') as f:
    content = f.read()

if 'implementation(project(":core:database"))' not in content:
    content = content.replace(
        'implementation(project(":core:network"))',
        'implementation(project(":core:network"))\n    implementation(project(":core:database"))'
    )

with open('app-headless/build.gradle.kts', 'w') as f:
    f.write(content)
