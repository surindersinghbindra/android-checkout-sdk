with open('sdk/checkout-ui/build.gradle.kts', 'r') as f:
    content = f.read()

packaging_block = """    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
        }
    }"""

content = content.replace('buildFeatures {\n        compose = true\n    }', 'buildFeatures {\n        compose = true\n    }\n' + packaging_block)

with open('sdk/checkout-ui/build.gradle.kts', 'w') as f:
    f.write(content)
