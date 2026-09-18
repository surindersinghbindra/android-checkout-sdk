with open('app/src/main/java/com/caribeanroyal/ecommercesample/ECommerceApp.kt', 'r') as f:
    content = f.read()

content = content.replace(
    'class ECommerceApp : Application() {',
    'import com.caribeanroyal.ecommercesample.feature.booking.di.BookingComponentProvider\n\nclass ECommerceApp : Application(), BookingComponentProvider {'
)
content = content.replace(
    '    override fun onCreate() {\n        super.onCreate()\n        appComponent = DaggerAppComponent.factory().create(this)\n    }',
    '    override fun onCreate() {\n        super.onCreate()\n        appComponent = DaggerAppComponent.factory().create(this)\n    }\n\n    override fun bookingViewModelFactory() = appComponent.bookingViewModelFactory()'
)

with open('app/src/main/java/com/caribeanroyal/ecommercesample/ECommerceApp.kt', 'w') as f:
    f.write(content)

with open('app-headless/src/main/java/com/caribeanroyal/headless/HeadlessApp.kt', 'r') as f:
    content = f.read()

content = content.replace(
    'class HeadlessApp : Application() {',
    'import com.caribeanroyal.ecommercesample.feature.booking.di.BookingComponentProvider\n\nclass HeadlessApp : Application(), BookingComponentProvider {'
)
content = content.replace(
    '    override fun onCreate() {\n        super.onCreate()\n        appComponent = DaggerAppComponent.factory().create(this)\n    }',
    '    override fun onCreate() {\n        super.onCreate()\n        appComponent = DaggerAppComponent.factory().create(this)\n    }\n\n    override fun bookingViewModelFactory() = appComponent.bookingViewModelFactory()'
)

with open('app-headless/src/main/java/com/caribeanroyal/headless/HeadlessApp.kt', 'w') as f:
    f.write(content)

