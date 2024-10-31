package ar.edu.unlam.mobile.scaffolding.evolution
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import ar.edu.unlam.mobile.scaffolding.evolution.ui.core.NavigationWrapper
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.ScaffoldingV2Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScaffoldingV2Theme(darkTheme = true) {
                NavigationWrapper()
            }
        }
    }
}
