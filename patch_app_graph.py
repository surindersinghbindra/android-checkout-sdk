with open('app/src/main/java/com/caribeanroyal/ecommercesample/navigation/CheckoutGraph.kt', 'r') as f:
    content = f.read()

content = content.replace(
    'val checkoutRoute = backStackEntry.toRoute<Screen.Checkout>()',
    'val checkoutRoute = backStackEntry.toRoute<Screen.Checkout>()\n\n        // Manually trigger the initializer only when the checkout feature is opened!\n        AppInitializer.getInstance(LocalContext.current).initializeComponent(CheckoutSdkInitializer::class.java)'
)

with open('app/src/main/java/com/caribeanroyal/ecommercesample/navigation/CheckoutGraph.kt', 'w') as f:
    f.write(content)
