with open('feature/booking/src/main/java/com/caribeanroyal/ecommercesample/feature/booking/ui/CruiseDetailScreen.kt', 'r') as f:
    content = f.read()

imports = """import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.caribeanroyal.ecommercesample.feature.booking.di.BookingComponentProvider
"""
content = content.replace('import androidx.compose.runtime.*\n', 'import androidx.compose.runtime.*\n' + imports)

sig = """@Composable
fun CruiseDetailScreen(
    packageCode: String,
    onNavigateBack: () -> Unit,
    onNavigateToCheckout: (Double, String, String, String) -> Unit
) {
    val context = LocalContext.current
    val factory = remember { 
        (context.applicationContext as BookingComponentProvider).bookingViewModelFactory() 
    }
    val viewModel: BookingViewModel = viewModel(factory = factory)
    val state by viewModel.state.collectAsState()"""

content = content.replace(
    '@Composable\nfun CruiseDetailScreen(\n    packageCode: String,\n    viewModel: BookingViewModel,\n    onNavigateBack: () -> Unit,\n    onNavigateToCheckout: (Double, String, String, String) -> Unit\n) {\n    val state by viewModel.state.collectAsState()',
    sig
)

with open('feature/booking/src/main/java/com/caribeanroyal/ecommercesample/feature/booking/ui/CruiseDetailScreen.kt', 'w') as f:
    f.write(content)
