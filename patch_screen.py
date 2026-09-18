with open('app/src/main/java/com/caribeanroyal/ecommercesample/navigation/Screen.kt', 'r') as f:
    content = f.read()

content = content.replace(
    'sealed interface Screen {',
    'sealed interface Screen {\n    @Serializable\n    data object Splash : Screen\n'
)

with open('app/src/main/java/com/caribeanroyal/ecommercesample/navigation/Screen.kt', 'w') as f:
    f.write(content)
