import sys
for filepath in ['app/src/main/java/com/caribeanroyal/ecommercesample/di/AppComponent.kt', 'app-headless/src/main/java/com/caribeanroyal/headless/di/AppComponent.kt']:
    with open(filepath, 'r') as f:
        content = f.read()

    content = content.replace(
        'fun searchCruisesUseCase(): SearchCruisesUseCase\n',
        'fun searchCruisesUseCase(): SearchCruisesUseCase\n    fun bookingViewModelFactory(): com.caribeanroyal.ecommercesample.feature.booking.viewmodel.BookingViewModelFactory\n'
    )

    with open(filepath, 'w') as f:
        f.write(content)
