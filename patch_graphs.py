with open('app/src/main/java/com/caribeanroyal/ecommercesample/navigation/CheckoutGraph.kt', 'r') as f:
    content = f.read()

content = content.replace('import androidx.navigation.compose.composable\n', 'import androidx.navigation.compose.composable\nimport androidx.startup.AppInitializer\nimport androidx.compose.ui.platform.LocalContext\nimport com.caribeanroyal.ecommercesample.sdk.checkout.core.db.CheckoutSdkInitializer\n')

content = content.replace(
    'val price = priceStr.toDoubleOrNull() ?: 0.0',
    'val price = priceStr.toDoubleOrNull() ?: 0.0\n\n        // Manually trigger the initializer only when the checkout feature is opened!\n        AppInitializer.getInstance(LocalContext.current).initializeComponent(CheckoutSdkInitializer::class.java)'
)

with open('app/src/main/java/com/caribeanroyal/ecommercesample/navigation/CheckoutGraph.kt', 'w') as f:
    f.write(content)

with open('app-headless/src/main/java/com/caribeanroyal/headless/navigation/CheckoutHeadlessGraph.kt', 'r') as f:
    content2 = f.read()

content2 = content2.replace('import androidx.navigation.compose.composable\n', 'import androidx.navigation.compose.composable\nimport androidx.startup.AppInitializer\nimport androidx.compose.ui.platform.LocalContext\nimport com.caribeanroyal.ecommercesample.sdk.checkout.core.db.CheckoutSdkInitializer\n')

content2 = content2.replace(
    'val price = priceStr.toDoubleOrNull() ?: 0.0',
    'val price = priceStr.toDoubleOrNull() ?: 0.0\n\n        // Manually trigger the initializer only when the checkout feature is opened!\n        AppInitializer.getInstance(LocalContext.current).initializeComponent(CheckoutSdkInitializer::class.java)'
)

with open('app-headless/src/main/java/com/caribeanroyal/headless/navigation/CheckoutHeadlessGraph.kt', 'w') as f:
    f.write(content2)

