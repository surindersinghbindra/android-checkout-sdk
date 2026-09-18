with open('gradle/libs.versions.toml', 'r') as f:
    content = f.read()

content = content.replace(
    'androidx-ui-test-manifest = { group = "androidx.compose.ui", name = "ui-test-manifest" }',
    'androidx-ui-test-manifest = { group = "androidx.compose.ui", name = "ui-test-manifest" }\nandroidx-startup = { module = "androidx.startup:startup-runtime", version = "1.1.1" }'
)

with open('gradle/libs.versions.toml', 'w') as f:
    f.write(content)
