with open('feature/booking/src/main/java/com/caribeanroyal/ecommercesample/feature/booking/viewmodel/BookingViewModel.kt', 'r') as f:
    content = f.read()

content = content.replace('import javax.inject.Inject\n\n', '')
content = content.replace('import androidx.lifecycle.ViewModel\n', 'import androidx.lifecycle.ViewModel\nimport javax.inject.Inject\n')

with open('feature/booking/src/main/java/com/caribeanroyal/ecommercesample/feature/booking/viewmodel/BookingViewModel.kt', 'w') as f:
    f.write(content)
