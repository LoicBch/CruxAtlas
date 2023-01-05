package com.example.camperpro.android.home

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.camperpro.android.LocalDependencyContainer
import com.example.camperpro.android.NavGraphs
import com.example.camperpro.android.composables.AppScaffold
import com.example.camperpro.android.composables.BottomBar
import com.example.camperpro.domain.model.Location
import com.example.camperpro.utils.*
import com.ramcosta.composedestinations.DestinationsNavHost
import kotlinx.coroutines.flow.*

// TODO: dep injection for locationcClient and NetworkClient / disposableEffect not optimize

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {

    val appViewModel = LocalDependencyContainer.current.appViewModel
    val sheetState = appViewModel.bottomSheetIsShowing
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val locationIsObserved by appViewModel.locationIsObserved.collectAsState()
    val networkIsObserved by appViewModel.networkIsObserved.collectAsState()
    val observersAreSet by appViewModel.observersAreSet.collectAsState()

    DisposableEffect(lifecycleOwner) {

        LocationClient(context).getLocationUpdates(Constants.GPS_UPDATE_INTERVAL)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                val lat = location.latitude
                val long = location.longitude
                Log.d("location", "$lat, $long")
                Globals.geoLoc.lastKnownLocation = Location(lat, long)
                if (Globals.geoLoc.lastSearchedLocation == null) {
                    Globals.geoLoc.lastSearchedLocation = Location(lat, long)
                }
                if (!locationIsObserved) appViewModel.onLocationObserveStarted()
            }.launchIn(lifecycleOwner.lifecycleScope)

        NetworkConnectivityObserver(context).observe()
            .onEach {
                Globals.network.status = it
                if (!networkIsObserved) appViewModel.onNetworkObserveStarted()
            }.launchIn(lifecycleOwner.lifecycleScope)

        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> Log.d("lifecycle", "onStart")
                Lifecycle.Event.ON_START -> Log.d("lifecycle", "onStart")
                Lifecycle.Event.ON_RESUME -> Log.d("lifecycle", "onResume")
                Lifecycle.Event.ON_PAUSE -> Log.d("lifecycle", "onPause")
                Lifecycle.Event.ON_STOP -> Log.d("lifecycle", "onStop")
                Lifecycle.Event.ON_DESTROY -> Log.d("lifecycle", "onDestroy")
                Lifecycle.Event.ON_ANY -> Log.d("lifecycle", "onAny")
            }
        }
        
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    //    Box(
    //        modifier = Modifier
    //            .fillMaxSize()
    //            .background(Color.Transparent)
    //    ) {

    if (observersAreSet) {
        AppScaffold(
            sheetState = sheetState,
            bottomBar = { BottomBar(navController = navController) },
        ) {
            DestinationsNavHost(
                modifier = Modifier.padding(it),
                navController = navController,
                navGraph = NavGraphs.root,
                startRoute = NavGraphs.root.startRoute
            )
        }
    }

    //        Text(
    //            modifier = Modifier.align(Alignment.Center),
    //            text = "Test",
    //            color = Color.Yellow,
    //            fontSize = 24.sp
    //        )
    //    }
}


