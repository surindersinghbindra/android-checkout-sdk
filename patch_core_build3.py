with open('sdk/checkout-core/build.gradle.kts', 'r') as f:
    content = f.read()

content = content.replace('implementation(libs.androidx.startup)', 'api(libs.androidx.startup)')

with open('sdk/checkout-core/build.gradle.kts', 'w') as f:
    f.write(content)
