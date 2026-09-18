with open('app-headless/build.gradle.kts', 'r') as f:
    content = f.read()

if 'alias(libs.plugins.ksp)' not in content:
    content = content.replace(
        'alias(libs.plugins.kotlin.compose)',
        'alias(libs.plugins.kotlin.compose)\n    alias(libs.plugins.ksp)'
    )

if 'libs.dagger.main' not in content:
    content = content.replace(
        'implementation(libs.okhttp.logging)',
        'implementation(libs.okhttp.logging)\n    implementation(libs.dagger.main)\n    ksp(libs.dagger.compiler)'
    )

with open('app-headless/build.gradle.kts', 'w') as f:
    f.write(content)
