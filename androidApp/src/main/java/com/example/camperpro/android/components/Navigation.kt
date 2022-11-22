//package com.example.camperpro.android
//
//import androidx.compose.runtime.Composable
//import androidx.navigation.NavType
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import androidx.navigation.navArgument
//import com.example.camperpro.android.mainmap.MainMap
//import com.example.camperpro.android.mainmap.MainMapViewModel
//import com.example.camperpro.android.spotSheet.SpotSheet
//import org.koin.androidx.compose.getViewModel
//
//@Composable
//fun Navigation()  {
//    val navController = rememberNavController()
//    NavHost(navController = navController, startDestination = Screen.MainMapScreen.route) {
//
//        composable(route = Screen.MainMapScreen.route) {
//            val viewModel : MainMapViewModel = getViewModel()
//            MainMap(navController = navController, viewModel)
//        }
//
//        composable(
//            route = Screen.DetailsScreen.route + "/{spotId}",
//            arguments = listOf(navArgument("spotId") {
//                type = NavType.IntType
//                defaultValue = 0
//                nullable = false
//            })
//        ) { entry ->
//            SpotSheet(0)
//        }
//    }
//}