import re

files = [
    'app/src/main/java/com/caribeanroyal/ecommercesample/navigation/ECommerceApp.kt',
    'app-headless/src/main/java/com/caribeanroyal/headless/navigation/ECommerceHeadlessApp.kt'
]

imports = """import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.caribeanroyal.ecommercesample.feature.booking.di.BookingComponentProvider
import com.caribeanroyal.ecommercesample.feature.booking.di.LocalBookingViewModelFactory
"""

for filepath in files:
    with open(filepath, 'r') as f:
        content = f.read()

    # Add imports
    content = content.replace('import androidx.compose.runtime.Composable\n', 'import androidx.compose.runtime.Composable\n' + imports)

    # Replace NavHost call
    navhost_replacement = """    val context = LocalContext.current
    val bookingFactory = remember { 
        (context.applicationContext as BookingComponentProvider).bookingViewModelFactory() 
    }

    CompositionLocalProvider(
        LocalBookingViewModelFactory provides bookingFactory
    ) {
        NavHost("""
    
    content = content.replace('    NavHost(', navhost_replacement)
    
    # Add closing brace for CompositionLocalProvider
    content = content.replace('        )\n    }\n}', '        )\n    }\n    }\n}')

    with open(filepath, 'w') as f:
        f.write(content)
