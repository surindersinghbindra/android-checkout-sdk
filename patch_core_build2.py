with open('sdk/checkout-core/build.gradle.kts', 'r') as f:
    content = f.read()

content = content.replace('compileOnly("androidx.annotation:annotation:1.7.1")', 'implementation("androidx.annotation:annotation:1.7.1")')

with open('sdk/checkout-core/build.gradle.kts', 'w') as f:
    f.write(content)
