with open('app/build.gradle.kts', 'r') as f:
    content = f.read()

content = content.replace('// implementation(libs.checkout.core)', 'implementation(libs.checkout.core)')
content = content.replace('// implementation(libs.checkout.ui)', 'implementation(libs.checkout.ui)')
content = content.replace('implementation(project(":sdk:checkout-core"))', '// implementation(project(":sdk:checkout-core"))')
content = content.replace('implementation(project(":sdk:checkout-ui"))', '// implementation(project(":sdk:checkout-ui"))')

with open('app/build.gradle.kts', 'w') as f:
    f.write(content)

with open('app-headless/build.gradle.kts', 'r') as f:
    content2 = f.read()

content2 = content2.replace('// implementation(libs.checkout.core)', 'implementation(libs.checkout.core)')
content2 = content2.replace('implementation(project(":sdk:checkout-core"))', '// implementation(project(":sdk:checkout-core"))')

with open('app-headless/build.gradle.kts', 'w') as f:
    f.write(content2)
