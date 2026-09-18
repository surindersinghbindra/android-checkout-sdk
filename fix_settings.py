with open('settings.gradle.kts', 'r') as f:
    content = f.read()

content = content.replace('// include(":sdk:checkout-core")', 'include(":sdk:checkout-core")')
content = content.replace('// include(":sdk:checkout-ui")', 'include(":sdk:checkout-ui")')

with open('settings.gradle.kts', 'w') as f:
    f.write(content)
