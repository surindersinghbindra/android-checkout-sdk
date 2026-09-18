import sys

files = [
    'app/src/main/java/com/caribeanroyal/ecommercesample/navigation/BookingGraph.kt',
    'app-headless/src/main/java/com/caribeanroyal/headless/navigation/BookingHeadlessGraph.kt'
]

for filepath in files:
    with open(filepath, 'r') as f:
        content = f.read()

    # App
    content = content.replace(
        'fun NavGraphBuilder.bookingGraph(\n    navController: NavController,\n    viewModelStoreOwner: ViewModelStoreOwner\n) {',
        'fun NavGraphBuilder.bookingGraph(\n    navController: NavController\n) {'
    )
    
    # Inside composable("booking") or composable<Screen.Booking>
    # We just need to strip the ViewModelProvider block and remove the viewModel argument
    import re
    # App-headless
    content = re.sub(
        r'        val context = LocalContext\.current.*?val bookingViewModel = ViewModelProvider.*?::class\.java\]',
        '',
        content,
        flags=re.DOTALL
    )
    # App
    content = re.sub(
        r'        val context = LocalContext\.current.*?val bookingViewModel =\n            ViewModelProvider.*?::class\.java\]',
        '',
        content,
        flags=re.DOTALL
    )
    
    content = content.replace('viewModel = bookingViewModel,', '')
    
    # Imports cleanup
    content = content.replace('import androidx.lifecycle.ViewModelProvider\n', '')
    content = content.replace('import androidx.lifecycle.ViewModelStoreOwner\n', '')
    content = content.replace('import androidx.compose.ui.platform.LocalContext\n', '')
    content = content.replace('import com.caribeanroyal.ecommercesample.ECommerceApp\n', '')
    content = content.replace('import com.caribeanroyal.headless.HeadlessApp\n', '')
    content = content.replace('import com.caribeanroyal.ecommercesample.feature.booking.viewmodel.BookingViewModel\n', '')
    content = content.replace('import com.caribeanroyal.ecommercesample.feature.booking.viewmodel.BookingViewModelFactory\n', '')
    content = content.replace('import androidx.compose.runtime.remember\n', '')

    with open(filepath, 'w') as f:
        f.write(content)
