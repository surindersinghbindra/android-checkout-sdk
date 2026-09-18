with open('app-headless/src/main/java/com/caribeanroyal/headless/MainActivity.kt', 'r') as f:
    content = f.read()

imports_to_add = """import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Scaffold
import com.caribeanroyal.headless.navigation.ECommerceHeadlessApp
"""

# add it after the package statement
content = content.replace('package com.caribeanroyal.headless\n', 'package com.caribeanroyal.headless\n\n' + imports_to_add)

with open('app-headless/src/main/java/com/caribeanroyal/headless/MainActivity.kt', 'w') as f:
    f.write(content)
