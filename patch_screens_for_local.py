import re

files = [
    'feature/booking/src/main/java/com/caribeanroyal/ecommercesample/feature/booking/ui/BookingScreen.kt',
    'feature/booking/src/main/java/com/caribeanroyal/ecommercesample/feature/booking/ui/CruiseDetailScreen.kt'
]

for filepath in files:
    with open(filepath, 'r') as f:
        content = f.read()

    # Remove the context, factory, and remember block
    content = re.sub(
        r'    val context = LocalContext\.current.*?bookingViewModelFactory\(\) \n    }',
        '',
        content,
        flags=re.DOTALL
    )

    content = content.replace(
        'val viewModel: BookingViewModel = viewModel(factory = factory)',
        'val viewModel: BookingViewModel = viewModel(factory = LocalBookingViewModelFactory.current)'
    )

    # Add import
    content = content.replace(
        'import com.caribeanroyal.ecommercesample.feature.booking.di.BookingComponentProvider',
        'import com.caribeanroyal.ecommercesample.feature.booking.di.LocalBookingViewModelFactory'
    )

    with open(filepath, 'w') as f:
        f.write(content)

