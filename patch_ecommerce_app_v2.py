with open('app/src/main/java/com/caribeanroyal/ecommercesample/navigation/ECommerceApp.kt', 'r') as f:
    content = f.read()

content = content.replace(
    '        bookingGraph(\n            navController = navController,\n            viewModelStoreOwner = viewModelStoreOwner\n        )',
    '        bookingGraph(\n            navController = navController\n        )'
)

with open('app/src/main/java/com/caribeanroyal/ecommercesample/navigation/ECommerceApp.kt', 'w') as f:
    f.write(content)

with open('app-headless/src/main/java/com/caribeanroyal/headless/navigation/ECommerceHeadlessApp.kt', 'r') as f:
    content = f.read()

content = content.replace(
    '        bookingGraph(\n            navController = navController,\n            viewModelStoreOwner = viewModelStoreOwner\n        )',
    '        bookingGraph(\n            navController = navController\n        )'
)

with open('app-headless/src/main/java/com/caribeanroyal/headless/navigation/ECommerceHeadlessApp.kt', 'w') as f:
    f.write(content)
