package com.example.camperpro.android

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.camperpro.android.components.checkPermission
import com.example.camperpro.android.destinations.SplashScreenDestination
import com.example.camperpro.android.di.AppDependencyContainer
import com.example.camperpro.android.di.viewModelModule
import com.example.camperpro.android.home.HomeScreen
import com.example.camperpro.android.onBoarding.SplashScreen
import com.example.camperpro.utils.di.sharedModule
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.compose.getViewModel
import org.koin.core.context.startKoin

val LocalDependencyContainer = compositionLocalOf<AppDependencyContainer> {
    error("No dependency container provided!")
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit
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
    @OptIn(
        ExperimentalAnimationApi::class,
        ExperimentalMaterialNavigationApi::class,
        ExperimentalMaterialApi::class
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> {
                    // Precise location access granted.
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                    // Only approximate location access granted.
                }
                else -> {
                    // No location access granted.
                }
            }
        }

        if (!this.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && !this
                .checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

        startKoin {
            androidLogger()
            androidContext(this@MainActivity)
            modules(viewModelModule + sharedModule())
        }

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
                SplashScreen(navController = navController)
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

    @Composable
    fun Greeting(text: String) {
        Text(text = text)
    }

    @Preview
    @Composable
    fun DefaultPreview() {
        MyApplicationTheme {
            Greeting("Hello, Android!")
        }
    }
}
