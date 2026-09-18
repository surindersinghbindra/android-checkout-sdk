import re

# Update libs.versions.toml
with open('gradle/libs.versions.toml', 'r') as f:
    toml = f.read()

# Add version if not exists
if 'checkoutSdk =' not in toml:
    toml = re.sub(r'(\[versions\]\n.*)', r'\1\ncheckoutSdk = "1.0.5"', toml)

# Add libraries if not exists
if 'checkout-core =' not in toml:
    toml = re.sub(r'(\[libraries\]\n.*)', r'\1\ncheckout-core = { group = "com.caribeanroyal.ecommercesample", name = "checkout-core", version.ref = "checkoutSdk" }\ncheckout-ui = { group = "com.caribeanroyal.ecommercesample", name = "checkout-ui", version.ref = "checkoutSdk" }', toml)

with open('gradle/libs.versions.toml', 'w') as f:
    f.write(toml)

# Update app build.gradle.kts
with open('app/build.gradle.kts', 'r') as f:
    app = f.read()
    
app = app.replace('implementation("com.caribeanroyal.ecommercesample:checkout-core:1.0.5")', 'implementation(libs.checkout.core)')
app = app.replace('implementation("com.caribeanroyal.ecommercesample:checkout-ui:1.0.5")', 'implementation(libs.checkout.ui)')
with open('app/build.gradle.kts', 'w') as f:
    f.write(app)
    
# Update headless build.gradle.kts
with open('app-headless/build.gradle.kts', 'r') as f:
    headless = f.read()
    
headless = headless.replace('implementation("com.caribeanroyal.ecommercesample:checkout-core:1.0.5")', 'implementation(libs.checkout.core)')
with open('app-headless/build.gradle.kts', 'w') as f:
    f.write(headless)
