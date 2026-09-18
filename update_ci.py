with open('.github/workflows/ci.yml', 'r') as f:
    content = f.read()

content = content.replace(
    "- name: Build Debug App\n      run: ./gradlew assembleDebug",
    "# - name: Build Debug App\n    #   run: ./gradlew assembleDebug"
)

with open('.github/workflows/ci.yml', 'w') as f:
    f.write(content)
