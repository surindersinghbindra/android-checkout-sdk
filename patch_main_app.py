with open('app/src/main/java/com/caribeanroyal/ecommercesample/MainActivity.kt', 'r') as f:
    content = f.read()

content = content.replace('import androidx.compose.material3.Surface\n', '')

with open('app/src/main/java/com/caribeanroyal/ecommercesample/MainActivity.kt', 'w') as f:
    f.write(content)
