package com.example.camperpro.android

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.camperpro.android.destinations.SplashScreenDestination
import com.example.camperpro.android.di.AppDependencyContainer
import com.example.camperpro.android.di.platformModule
import com.example.camperpro.android.home.HomeScreen
import com.example.camperpro.android.onBoarding.SplashScreen
import com.example.camperpro.utils.*
import com.example.camperpro.utils.di.sharedModule
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.compose.getViewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidLogger(level = Level.DEBUG)
            androidContext(this@MainActivity)
            modules(sharedModule() + platformModule())
        }

        startNetworkObserver(this)
        checkPermission(this)
        initGlobalsVar(this.applicationContext)

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

    //    mettre dans un networkmanager
    private fun startNetworkObserver(activity: Activity) {
        NetworkConnectivityObserver(activity.applicationContext).observe().onEach {
            Globals.network.status = it
        }.launchIn(lifecycleScope)
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

    override fun onResume() {
        super.onResume()
        startService(this)
    }

    override fun onPause() {
        super.onPause()
        stopService(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(this)
    }
}

private fun initGlobalsVar(context: Context) {
    Globals.geoLoc.appLanguage = "FR"
    Globals.geoLoc.deviceLanguage = LanguageManager(context).getDeviceLanguage()
    Globals.geoLoc.deviceCountry = LanguageManager(context).getDeviceCountry()
}


//Placer ca dans le Splash
fun checkPermission(activity: ComponentActivity) {
    val locationPermissionRequest = activity.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> {
                LocationClient(activity.applicationContext).startLocationObserver()
            }
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                LocationClient(activity.applicationContext).startLocationObserver()
            }
            else -> {
                // No location access granted.
            }
        }
    }

    if (!activity.hasLocationPermission) {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    } else {
        startService(activity)
    }
}

private fun startService(activity: Activity) {
    Intent(activity.applicationContext, LocationService::class.java).apply {
        action = LocationService.ACTION_START
        activity.startService(this)
    }
}

private fun stopService(activity: Activity) {
    Intent(activity.applicationContext, LocationService::class.java).apply {
        action = LocationService.ACTION_STOP
        activity.stopService(this)
    }
}