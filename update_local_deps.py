import re

# App build.gradle.kts
with open('app/build.gradle.kts', 'r') as f:
    app_build = f.read()

app_replacement = """    // --- To use the published remote SDK ---
    implementation("com.caribeanroyal.ecommercesample:checkout-core:1.0.5")
    implementation("com.caribeanroyal.ecommercesample:checkout-ui:1.0.5")

    // --- To build the SDK from local source (uncomment these and comment the remote ones) ---
    // implementation(project(":sdk:checkout-core"))
    // implementation(project(":sdk:checkout-ui"))"""

app_build = re.sub(r'    implementation\("com\.caribeanroyal\.ecommercesample:checkout-core:.*"\)\n    implementation\("com\.caribeanroyal\.ecommercesample:checkout-ui:.*"\)', app_replacement, app_build)

with open('app/build.gradle.kts', 'w') as f:
    f.write(app_build)

# Headless app build.gradle.kts
with open('app-headless/build.gradle.kts', 'r') as f:
    headless_build = f.read()

headless_replacement = """    // --- To use the published remote SDK ---
    implementation("com.caribeanroyal.ecommercesample:checkout-core:1.0.5")

    // --- To build the SDK from local source (uncomment this and comment the remote one) ---
    // implementation(project(":sdk:checkout-core"))"""

headless_build = re.sub(r'    implementation\("com\.caribeanroyal\.ecommercesample:checkout-core:.*"\)', headless_replacement, headless_build)

with open('app-headless/build.gradle.kts', 'w') as f:
    f.write(headless_build)
