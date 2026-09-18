with open('app-headless/src/main/AndroidManifest.xml', 'r') as f:
    content = f.read()

content = content.replace(
    '<application\n        android:theme="@style/Theme.ECommerceSample">',
    '<application\n        android:name=".HeadlessApp"\n        android:theme="@style/Theme.ECommerceSample">'
)
content = content.replace(
    '<application\n        android:allowBackup="true"',
    '<application\n        android:name=".HeadlessApp"\n        android:allowBackup="true"'
)

# just in case it's in a single line
import re
content = re.sub(r'<application\s+android:theme', '<application\n        android:name=".HeadlessApp"\n        android:theme', content)

with open('app-headless/src/main/AndroidManifest.xml', 'w') as f:
    f.write(content)
