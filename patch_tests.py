with open('sdk/checkout-ui/build.gradle.kts', 'r') as f:
    content = f.read()

deps = """    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)
    androidTestImplementation(libs.mockk.main)"""

content = content.replace('testImplementation(libs.turbine.main)', 'testImplementation(libs.turbine.main)\n' + deps)

with open('sdk/checkout-ui/build.gradle.kts', 'w') as f:
    f.write(content)
