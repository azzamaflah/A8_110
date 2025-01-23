package com.example.uaspam.ui.navigation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.uaspam.ui.view.bangunan.DestinasiHomeBangunan
import com.example.uaspam.ui.view.bangunan.HomeBangunanView
import com.example.uaspam.ui.view.kamar.DestinasiKamarHome
import com.example.uaspam.ui.view.kamar.HomeKamarScreen

@Composable
fun PengelolaHalaman(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = DestinasiHomeBangunan.route,
        modifier = Modifier
    ) {
        // Menu Utama
//        composable("menu_view") {
//            MenuView(
//                onNavigateToKamar = { navController.navigate("home_kamar") },
//                onNavigateToBangunan = { navController.navigate("home_bangunan") },
//                onNavigateToPembayaran = { navController.navigate("home_pembayaran_sewa") }
//            )
//        }

        // Home Kamar
//        composable("home_kamar") {
//            HomeKamarScreen(
//                navigateToItemEntry = { /* Implement navigation logic */ },
//                onDetailClick = { idKamar ->
//                    navController.navigate("detail_kamar/$idKamar")
//                }
//            )
//        }

        // Home Bangunan
        composable(DestinasiHomeBangunan.route) {
            HomeBangunanView(
                navigateToItemEntry = { navController.navigate(DestinasiHomeBangunan.route) },
                onDetailClick = {
                }
            )
        }
    }
    }
        // Home Pembayaran Sewa
//        composable("home_pembayaran_sewa") {
//            HomePembayaranSewaView(
//                navigateToItemEntry = { /* Implement navigation logic */ },
//                onDetailClick = { idPembayaran ->
//                    navController.navigate("detail_pembayaran/$idPembayaran")
//                }
//            )
//        }

