with open('app-headless/src/main/java/com/caribeanroyal/headless/navigation/ECommerceHeadlessApp.kt', 'r') as f:
    content = f.read()

content = content.replace(
    '    viewModelStoreOwner: ViewModelStoreOwner,\n    bookingFactory: BookingViewModelFactory,\n',
    '    viewModelStoreOwner: ViewModelStoreOwner,\n'
)
content = content.replace(
    '        bookingGraph(\n            navController = navController,\n            viewModelStoreOwner = viewModelStoreOwner,\n            bookingFactory = bookingFactory\n        )',
    '        bookingGraph(\n            navController = navController,\n            viewModelStoreOwner = viewModelStoreOwner\n        )'
)
content = content.replace(
    'import com.caribeanroyal.ecommercesample.feature.booking.viewmodel.BookingViewModelFactory\n',
    ''
)

with open('app-headless/src/main/java/com/caribeanroyal/headless/navigation/ECommerceHeadlessApp.kt', 'w') as f:
    f.write(content)
