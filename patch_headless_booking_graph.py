with open('app-headless/src/main/java/com/caribeanroyal/headless/navigation/BookingHeadlessGraph.kt', 'r') as f:
    content = f.read()

content = content.replace(
    'import androidx.navigation.NavController',
    'import androidx.compose.ui.platform.LocalContext\nimport androidx.compose.runtime.remember\nimport androidx.navigation.NavController\nimport com.caribeanroyal.headless.HeadlessApp\nimport com.caribeanroyal.ecommercesample.feature.booking.viewmodel.BookingViewModelFactory'
)

# Remove bookingFactory from parameters
content = content.replace(
    '    viewModelStoreOwner: ViewModelStoreOwner,\n    bookingFactory: BookingViewModelFactory\n)',
    '    viewModelStoreOwner: ViewModelStoreOwner\n)'
)

# Replace the factory creation
new_factory = """    composable("booking") {
        val context = LocalContext.current
        val appComponent = (context.applicationContext as HeadlessApp).appComponent
        val searchCruisesUseCase = remember { appComponent.searchCruisesUseCase() }
        val bookingFactory = remember { BookingViewModelFactory(searchCruisesUseCase) }
        val bookingViewModel = ViewModelProvider(viewModelStoreOwner, bookingFactory)[BookingViewModel::class.java]

        BookingScreen("""

content = content.replace(
    '    composable("booking") {\n        val bookingViewModel = ViewModelProvider(viewModelStoreOwner, bookingFactory)[BookingViewModel::class.java]\n\n        BookingScreen(',
    new_factory
)

new_factory_detail = """    composable("cruiseDetail/{packageCode}") { backStackEntry ->
        val context = LocalContext.current
        val appComponent = (context.applicationContext as HeadlessApp).appComponent
        val searchCruisesUseCase = remember { appComponent.searchCruisesUseCase() }
        val bookingFactory = remember { BookingViewModelFactory(searchCruisesUseCase) }
        val packageCode = backStackEntry.arguments?.getString("packageCode") ?: ""
        val bookingViewModel = ViewModelProvider(viewModelStoreOwner, bookingFactory)[BookingViewModel::class.java]"""

content = content.replace(
    '    composable("cruiseDetail/{packageCode}") { backStackEntry ->\n        val packageCode = backStackEntry.arguments?.getString("packageCode") ?: ""\n        val bookingViewModel = ViewModelProvider(viewModelStoreOwner, bookingFactory)[BookingViewModel::class.java]',
    new_factory_detail
)

with open('app-headless/src/main/java/com/caribeanroyal/headless/navigation/BookingHeadlessGraph.kt', 'w') as f:
    f.write(content)
