package com.horionDev.climbingapp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.horionDev.climbingapp.android.destinations.SplashScreenDestination
import com.horionDev.climbingapp.android.di.AppDependencyContainer
import com.horionDev.climbingapp.android.home.HomeScreen
import com.horionDev.climbingapp.android.onBoarding.SplashScreen
import com.horionDev.climbingapp.android.onBoarding.SplashScreenViewModel
import org.koin.androidx.compose.getViewModel

val LocalDependencyContainer = compositionLocalOf<AppDependencyContainer> {
    error("No dependency container provided!")
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColors(
            primary = Color(0xFFBB86FC),
            primaryVariant = Color(0xFF3700B3),
            secondary = Color(0xFF03DAC5)
        )
    } else {
        lightColors(
            primary = Color(0xFF6200EE),
            primaryVariant = Color(0xFF3700B3),
            secondary = Color(0xFF03DAC5)
        )
    }
    val typography = Typography(
        body1 = TextStyle(
            fontFamily = FontFamily.Default, fontWeight = FontWeight.Normal, fontSize = 16.sp
        )
    )

    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
    )

    MaterialTheme(
        colors = colors, typography = typography, shapes = shapes, content = content
    )
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                val appViewModel: AppViewModel = getViewModel()
                val dependencyContainer = AppDependencyContainer(appViewModel)

                CompositionLocalProvider(LocalDependencyContainer provides dependencyContainer) {
                    RootNavGraph(navController = rememberNavController())
                }
            }
        }
    }

    @Composable
    fun RootNavGraph(navController: NavHostController) {
        NavHost(
            navController = navController, route = Graphs.ROOT, startDestination =
            SplashScreenDestination.route
        ) {

            composable(route = SplashScreenDestination.route) {
                val context = LocalContext.current
//                val setupApp: SetupApp by inject()
//                val languageManager: LanguageManager by inject { parametersOf(context) }
                SplashScreen(
                    navController = navController
                )
            }

            composable(route = Graphs.HOME) {
                HomeScreen()
            }
        }
    }

    object Graphs {
        const val ROOT = "root_graph"
        const val HOME = "home_graph"
    }
}