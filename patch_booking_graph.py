with open('app/src/main/java/com/caribeanroyal/ecommercesample/navigation/BookingGraph.kt', 'r') as f:
    content = f.read()

content = content.replace(
    'import androidx.lifecycle.ViewModelStoreOwner\nimport androidx.navigation.NavController',
    'import androidx.compose.ui.platform.LocalContext\nimport androidx.lifecycle.ViewModelStoreOwner\nimport androidx.navigation.NavController\nimport com.caribeanroyal.ecommercesample.ECommerceApp\nimport androidx.compose.runtime.remember'
)

# Remove searchCruisesUseCase from parameters
content = content.replace(
    '    viewModelStoreOwner: ViewModelStoreOwner,\n    searchCruisesUseCase: SearchCruisesUseCase\n)',
    '    viewModelStoreOwner: ViewModelStoreOwner\n)'
)

# Replace the factory creation
old_factory = '    val bookingFactory = BookingViewModelFactory(searchCruisesUseCase)'
new_factory = """    composable<Screen.Booking> {
        val context = LocalContext.current
        val appComponent = (context.applicationContext as ECommerceApp).appComponent
        val searchCruisesUseCase = remember { appComponent.searchCruisesUseCase() }
        val bookingFactory = remember { BookingViewModelFactory(searchCruisesUseCase) }
        val bookingViewModel =
            ViewModelProvider(viewModelStoreOwner, bookingFactory)[BookingViewModel::class.java]

        BookingScreen("""

content = content.replace(
    '    val bookingFactory = BookingViewModelFactory(searchCruisesUseCase)\n\n    composable<Screen.Booking> {\n        val bookingViewModel =\n            ViewModelProvider(viewModelStoreOwner, bookingFactory)[BookingViewModel::class.java]\n\n        BookingScreen(',
    new_factory
)

new_factory_detail = """    composable<Screen.CruiseDetail> { backStackEntry ->
        val context = LocalContext.current
        val appComponent = (context.applicationContext as ECommerceApp).appComponent
        val searchCruisesUseCase = remember { appComponent.searchCruisesUseCase() }
        val bookingFactory = remember { BookingViewModelFactory(searchCruisesUseCase) }
        val detailRoute = backStackEntry.toRoute<Screen.CruiseDetail>()
        val bookingViewModel =
            ViewModelProvider(viewModelStoreOwner, bookingFactory)[BookingViewModel::class.java]"""

content = content.replace(
    '    composable<Screen.CruiseDetail> { backStackEntry ->\n        val detailRoute = backStackEntry.toRoute<Screen.CruiseDetail>()\n        val bookingViewModel =\n            ViewModelProvider(viewModelStoreOwner, bookingFactory)[BookingViewModel::class.java]',
    new_factory_detail
)

with open('app/src/main/java/com/caribeanroyal/ecommercesample/navigation/BookingGraph.kt', 'w') as f:
    f.write(content)
