package com.example.uaspam.ui.navigation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.uaspam.ui.view.bangunan.DestinasiDetailBangunan
import com.example.uaspam.ui.view.bangunan.DestinasiEditBangunan
import com.example.uaspam.ui.view.bangunan.DestinasiEntryBangunan
import com.example.uaspam.ui.view.bangunan.DestinasiHomeBangunan
import com.example.uaspam.ui.view.bangunan.DetailBangunanView
import com.example.uaspam.ui.view.bangunan.EditBangunanView
import com.example.uaspam.ui.view.bangunan.HomeBangunanView
import com.example.uaspam.ui.view.bangunan.InsertBangunanView
import com.example.uaspam.ui.view.kamar.DestinasiDetailKamar
import com.example.uaspam.ui.view.kamar.DestinasiEditKamar
import com.example.uaspam.ui.view.kamar.DestinasiEntryKamar
import com.example.uaspam.ui.view.kamar.DestinasiKamarHome
import com.example.uaspam.ui.view.kamar.DetailKamarView
import com.example.uaspam.ui.view.kamar.EditKamarView
import com.example.uaspam.ui.view.kamar.HomeKamarScreen
import com.example.uaspam.ui.view.kamar.InsertKamarView
import com.example.uaspam.ui.view.mahasiswa.DestinasiDetailMahasiswa
import com.example.uaspam.ui.view.mahasiswa.DestinasiEditMahasiswa
import com.example.uaspam.ui.view.mahasiswa.DestinasiEntryMahasiswa
import com.example.uaspam.ui.view.mahasiswa.DestinasiHomeMahasiswa
import com.example.uaspam.ui.view.mahasiswa.DetailMahasiswaView
import com.example.uaspam.ui.view.mahasiswa.EditMahasiswaView
import com.example.uaspam.ui.view.mahasiswa.HomeMahasiswaView
import com.example.uaspam.ui.view.mahasiswa.InsertMahasiswaView
import com.example.uaspam.ui.view.pembayaran.DestinasiHomePembayaranSewa
import com.example.uaspam.ui.view.pembayaran.HomePembayaranSewaView
import com.example.uaspam.ui.view.pembayaransewa.DestinasiDetailPembayaranSewa
import com.example.uaspam.ui.view.pembayaransewa.DestinasiEditPembayaranSewa
import com.example.uaspam.ui.view.pembayaransewa.DestinasiEntryPembayaran
import com.example.uaspam.ui.view.pembayaransewa.DetailPembayaranSewaView
import com.example.uaspam.ui.view.pembayaransewa.EditPembayaranSewaView
import com.example.uaspam.ui.view.pembayaransewa.InsertPembayaranSewaView

@Composable
fun PengelolaHalaman(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = DestinasiHomeMahasiswa.route,
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

         //Home Kamar
        composable(DestinasiKamarHome.route) {
            HomeKamarScreen(
                navigateToItemEntry = { navController.navigate(DestinasiEntryKamar.route) },
                onDetailClick = {idKamar ->
                    navController.navigate("detail_kamar/$idKamar")
                }
            )
        }

        composable(DestinasiEntryKamar.route) {
            InsertKamarView(
                navigateBack = {
                    navController.navigate(DestinasiKamarHome.route) {
                        popUpTo(DestinasiKamarHome.route) { inclusive = true }
                    }
                }
            )
        }

        // Detail Kamar
        composable(
            route = "${DestinasiDetailKamar.route}/{id_kamar}",
            arguments = listOf(navArgument("id_kamar") { type = NavType.StringType })
        ) { backStackEntry ->
            val idKamar = backStackEntry.arguments?.getString("id_kamar") ?: ""
            DetailKamarView(
                idKamar = idKamar,
                navController = navController,
                navigateBack = { navController.popBackStack() }
            )
        }

        // Edit Kamar
        composable(
            route = "${DestinasiEditKamar.route}/{id_kamar}",
            arguments = listOf(navArgument("id_kamar") { type = NavType.StringType })
        ) { backStackEntry ->
            val idKamar = backStackEntry.arguments?.getString("id_kamar") ?: ""
            EditKamarView(
                idKamar = idKamar,
                navigateBack = { navController.popBackStack() }
            )
        }


        // Home Bangunan
        composable(DestinasiHomeBangunan.route) {
            HomeBangunanView(
                navigateToItemEntry = { navController.navigate(DestinasiEntryBangunan.route) },
                onDetailClick = { idBangunan ->
                    navController.navigate("detail_bangunan/$idBangunan")
                }
            )
        }
        composable(DestinasiEntryBangunan.route) {
            InsertBangunanView(
                navigateBack = {
                    navController.navigate(DestinasiHomeBangunan.route) {
                        popUpTo(DestinasiHomeBangunan.route) { inclusive = true }
                    }
                }

            )
        }

        //detail
        composable(
            route = "${DestinasiDetailBangunan.route}/{id_bangunan}",
            arguments = listOf(navArgument("id_bangunan") { type = NavType.StringType })
        ) { backStackEntry ->
            val idBangunan = backStackEntry.arguments?.getString("id_bangunan") ?: ""
            DetailBangunanView(
                idBangunan = idBangunan,
                navController = navController,
                navigateBack = { navController.popBackStack() }
            )
        }

        //edit
        composable(
            route = "${DestinasiEditBangunan.route}/{id_bangunan}",
            arguments = listOf(navArgument("id_bangunan") { type = NavType.StringType })
        ) { backStackEntry ->
            val idBangunan = backStackEntry.arguments?.getString("id_bangunan") ?: ""
            EditBangunanView(
                idBangunan = idBangunan,
                navigateBack = { navController.popBackStack() }
            )
        }


        // home mahasiswa
        composable(DestinasiHomeMahasiswa.route){
            HomeMahasiswaView(
                navigateToItemEntry = { navController.navigate(DestinasiEntryMahasiswa.route) },
                onDetailClick = {idMahasiswa ->
                    navController.navigate("detail_mahasiswa/$idMahasiswa")
                }
            )

        }

        composable(DestinasiEntryMahasiswa.route){
            InsertMahasiswaView(
                navigateBack = {
                    navController.navigate(DestinasiHomeMahasiswa.route) {
                        popUpTo(DestinasiHomeMahasiswa.route) { inclusive = true }
                    }
                }
            )
        }

        //detail
        composable(
            route = "${DestinasiDetailMahasiswa.route}/{id_mahasiswa}",
            arguments = listOf(navArgument("id_mahasiswa") { type = NavType.StringType })
        ) { backStackEntry ->
            val idMahasiswa = backStackEntry.arguments?.getString("id_mahasiswa") ?: ""
            DetailMahasiswaView(
                idMahasiswa = idMahasiswa,
                navController = navController,
                navigateBack = { navController.popBackStack() }
            )
        }

        //edit
        composable(
            route = "${DestinasiEditMahasiswa.route}/{id_mahasiswa}",
            arguments = listOf(navArgument("id_mahasiswa") { type = NavType.StringType })
        ) { backStackEntry ->
            val idMahasiswa = backStackEntry.arguments?.getString("id_mahasiswa") ?: ""
            EditMahasiswaView(
                    idMahasiswa = idMahasiswa,
                navigateBack = { navController.popBackStack() }
            )
        }

        // home pembayaran sewa

        composable(DestinasiHomePembayaranSewa.route){
            HomePembayaranSewaView(
                navigateToItemEntry = { navController.navigate(DestinasiEntryPembayaran.route) },
                onDetailClick = { idPembayaran ->
                    navController.navigate("detail_pembayaran_sewa/$idPembayaran")
                }
            )
        }

        composable(DestinasiEntryPembayaran.route){
            InsertPembayaranSewaView(
                navigateBack = {
                    navController.navigate(DestinasiHomePembayaranSewa.route) {
                        popUpTo(DestinasiHomePembayaranSewa.route) { inclusive = true }
                    }
                }
            )
        }

        //detail
        composable(
            route = "${DestinasiDetailPembayaranSewa.route}/{id_pembayaran}",
            arguments = listOf(navArgument("id_pembayaran") { type = NavType.StringType })
        ) { backStackEntry ->
            val idPembayaran = backStackEntry.arguments?.getString("id_pembayaran") ?: ""
            DetailPembayaranSewaView(
                idPembayaran = idPembayaran,
                navController = navController,
                navigateBack = { navController.popBackStack() }
            )
        }

        //edit
        composable(
            route = "${DestinasiEditPembayaranSewa.route}/{id_pembayaran}",
            arguments = listOf(navArgument("id_pembayaran") { type = NavType.StringType })
        ) { backStackEntry ->
            val idPembayaran = backStackEntry.arguments?.getString("id_pembayaran") ?: ""
            EditPembayaranSewaView(
                idPembayaran = idPembayaran,
                navigateBack = { navController.popBackStack() }
            )
        }


    }
}


