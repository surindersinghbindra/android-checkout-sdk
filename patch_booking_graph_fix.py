with open('app/src/main/java/com/caribeanroyal/ecommercesample/navigation/BookingGraph.kt', 'r') as f:
    content = f.read()

content = content.replace(
    '    composable<Screen.CruiseDetail> { backStackEntry ->\n\n\n        CruiseDetailScreen(',
    '    composable<Screen.CruiseDetail> { backStackEntry ->\n        val detailRoute = backStackEntry.toRoute<Screen.CruiseDetail>()\n\n        CruiseDetailScreen('
)

with open('app/src/main/java/com/caribeanroyal/ecommercesample/navigation/BookingGraph.kt', 'w') as f:
    f.write(content)
