with open('app-headless/src/main/java/com/caribeanroyal/headless/MainActivity.kt', 'r') as f:
    content = f.read()

content = content.replace(
    '        val appComponent = DaggerAppComponent.factory().create(applicationContext)\n        val searchCruisesUseCase = appComponent.searchCruisesUseCase()\n        val bookingFactory = BookingViewModelFactory(searchCruisesUseCase)\n\n        val analyticsTracker = object : CheckoutAnalytics {',
    '        val analyticsTracker = object : CheckoutAnalytics {'
)
content = content.replace(
    '                            viewModelStoreOwner = this@MainActivity,\n                            bookingFactory = bookingFactory\n                        )',
    '                            viewModelStoreOwner = this@MainActivity\n                        )'
)
content = content.replace('import com.caribeanroyal.headless.di.DaggerAppComponent\n', '')
content = content.replace('import com.caribeanroyal.ecommercesample.feature.booking.viewmodel.BookingViewModelFactory\n', '')
content = content.replace('import com.caribeanroyal.ecommercesample.core.domain.usecase.SearchCruisesUseCase\n', '')

with open('app-headless/src/main/java/com/caribeanroyal/headless/MainActivity.kt', 'w') as f:
    f.write(content)
