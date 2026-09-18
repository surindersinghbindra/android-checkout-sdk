with open('gradle/libs.versions.toml', 'r') as f:
    content = f.read()

content = content.replace('mockk-main = { group = "io.mockk", name = "mockk", version.ref = "mockk" }', 'mockk-main = { group = "io.mockk", name = "mockk", version.ref = "mockk" }\nmockk-android = { group = "io.mockk", name = "mockk-android", version.ref = "mockk" }')

with open('gradle/libs.versions.toml', 'w') as f:
    f.write(content)

with open('sdk/checkout-ui/build.gradle.kts', 'r') as f:
    content2 = f.read()

content2 = content2.replace('androidTestImplementation(libs.mockk.main)', 'androidTestImplementation(libs.mockk.android)')

with open('sdk/checkout-ui/build.gradle.kts', 'w') as f:
    f.write(content2)
