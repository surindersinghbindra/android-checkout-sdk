import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun VisualStepper(currentStep: Int, totalSteps: Int, themeConfig: CheckoutThemeConfig) {
    val stepTitles = listOf("Party", "Room", "Extras", "Review", "Guest", "Pay")
    Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()) {
        for (i in 0 until totalSteps) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(48.dp)) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = if (i <= currentStep) themeConfig.primaryColor else Color.LightGray,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "${i + 1}", color = Color.White, style = MaterialTheme.typography.labelSmall)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stepTitles.getOrElse(i) { "" }, 
                    style = MaterialTheme.typography.labelSmall, 
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = if (i <= currentStep) themeConfig.primaryColor else Color.Gray
                )
            }
            if (i < totalSteps - 1) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 11.dp) // Half of 24.dp to center with circle
                        .height(2.dp)
                        .background(if (i < currentStep) themeConfig.primaryColor else Color.LightGray)
                )
            }
        }
    }
}
