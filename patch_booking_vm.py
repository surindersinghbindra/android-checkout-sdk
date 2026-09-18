with open('feature/booking/src/main/java/com/caribeanroyal/ecommercesample/feature/booking/viewmodel/BookingViewModel.kt', 'r') as f:
    content = f.read()

content = content.replace(
    'class BookingViewModelFactory(\n    private val searchCruisesUseCase: SearchCruisesUseCase\n)',
    'import javax.inject.Inject\n\nclass BookingViewModelFactory @Inject constructor(\n    private val searchCruisesUseCase: SearchCruisesUseCase\n)'
)

with open('feature/booking/src/main/java/com/caribeanroyal/ecommercesample/feature/booking/viewmodel/BookingViewModel.kt', 'w') as f:
    f.write(content)
