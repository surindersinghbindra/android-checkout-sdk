with open('app-headless/src/main/java/com/caribeanroyal/headless/navigation/BookingHeadlessGraph.kt', 'r') as f:
    content = f.read()

content = content.replace(
    '    composable("cruiseDetail/{packageCode}") { backStackEntry ->\n\n        CruiseDetailScreen(',
    '    composable("cruiseDetail/{packageCode}") { backStackEntry ->\n        val packageCode = backStackEntry.arguments?.getString("packageCode") ?: ""\n\n        CruiseDetailScreen('
)

with open('app-headless/src/main/java/com/caribeanroyal/headless/navigation/BookingHeadlessGraph.kt', 'w') as f:
    f.write(content)
