with open('sdk/checkout-core/src/main/AndroidManifest.xml', 'r') as f:
    content = f.read()

content = content.replace('android:mergeRule="merge"', 'xmlns:tools="http://schemas.android.com/tools"\n            tools:node="merge"')

with open('sdk/checkout-core/src/main/AndroidManifest.xml', 'w') as f:
    f.write(content)
