with open('app/src/main/java/com/caribeanroyal/ecommercesample/MainActivity.kt', 'r') as f:
    content = f.read()

# Add imports for enableEdgeToEdge, Scaffold, Box, padding, WindowInsets
imports = """import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import com.caribeanroyal.ecommercesample.navigation.ECommerceApp
"""
content = content.replace('import androidx.compose.foundation.layout.fillMaxSize', imports + 'import androidx.compose.foundation.layout.fillMaxSize')

# Remove old AppNavigation import if present
content = content.replace('import com.caribeanroyal.ecommercesample.navigation.AppNavigation\n', '')

# Update onCreate
on_create_code = """
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val appComponent = DaggerAppComponent.factory().create(applicationContext)"""
        
content = content.replace('\n    override fun onCreate(savedInstanceState: Bundle?) {\n        super.onCreate(savedInstanceState)\n        \n        val appComponent = DaggerAppComponent.factory().create(applicationContext)', on_create_code)

# Update setContent
set_content_old = """        setContent {
            ECommerceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        checkoutSdk = checkoutSdk,
                        checkoutThemeConfig = sdkThemeConfig,
                        viewModelStoreOwner = this,
                        searchCruisesUseCase = searchCruisesUseCase
                    )
                }
            }
        }"""

set_content_new = """        setContent {
            ECommerceTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background,
                    contentWindowInsets = WindowInsets(0, 0, 0, 0)
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        ECommerceApp(
                            checkoutSdk = checkoutSdk,
                            checkoutThemeConfig = sdkThemeConfig,
                            viewModelStoreOwner = this@MainActivity,
                            searchCruisesUseCase = searchCruisesUseCase
                        )
                    }
                }
            }
        }"""
        
content = content.replace(set_content_old, set_content_new)

with open('app/src/main/java/com/caribeanroyal/ecommercesample/MainActivity.kt', 'w') as f:
    f.write(content)
