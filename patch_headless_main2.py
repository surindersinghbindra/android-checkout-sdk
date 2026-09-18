import re

with open('app-headless/src/main/java/com/caribeanroyal/headless/MainActivity.kt', 'r') as f:
    content = f.read()

# Add enableEdgeToEdge
content = content.replace(
    'super.onCreate(savedInstanceState)',
    'super.onCreate(savedInstanceState)\n        enableEdgeToEdge()'
)

# Remove unused imports
unused_imports = [
    'import androidx.compose.ui.Alignment',
    'import androidx.compose.ui.graphics.Color',
    'import androidx.compose.ui.unit.dp',
    'import androidx.navigation.compose.NavHost',
    'import androidx.navigation.compose.composable',
    'import androidx.navigation.compose.rememberNavController',
    'import com.caribeanroyal.ecommercesample.feature.booking.ui.BookingScreen',
    'import com.caribeanroyal.ecommercesample.feature.booking.ui.CruiseDetailScreen',
    'import com.caribeanroyal.ecommercesample.feature.booking.viewmodel.BookingViewModel',
    'import kotlinx.coroutines.launch',
    'import androidx.lifecycle.ViewModelProvider',
]

for imp in unused_imports:
    content = content.replace(imp + '\n', '')

with open('app-headless/src/main/java/com/caribeanroyal/headless/MainActivity.kt', 'w') as f:
    f.write(content)
