with open('feature/booking/build.gradle.kts', 'r') as f:
    content = f.read()

content = content.replace(
    'implementation(libs.androidx.lifecycle.runtime.ktx)\n',
    'implementation(libs.androidx.lifecycle.runtime.ktx)\n    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")\n'
)

with open('feature/booking/build.gradle.kts', 'w') as f:
    f.write(content)
