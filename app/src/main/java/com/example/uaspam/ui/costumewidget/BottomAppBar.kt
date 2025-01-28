package com.example.uaspam.ui.costumewidget

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.uaspam.ui.view.bangunan.DestinasiHomeBangunan
import com.example.uaspam.ui.view.kamar.DestinasiKamarHome
import com.example.uaspam.ui.view.mahasiswa.DestinasiHomeMahasiswa
import com.example.uaspam.ui.view.pembayaran.DestinasiHomePembayaranSewa

data class BottomNavItem(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String
)

@Composable
fun BottomAppBar(
    navController: NavController,
    currentRoute: String,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem("Bangunan", Icons.Default.Build, DestinasiHomeBangunan.route),
        BottomNavItem("Kamar", Icons.Default.Home, DestinasiKamarHome.route),
        BottomNavItem("Mahasiswa", Icons.Default.Person, DestinasiHomeMahasiswa.route),
        BottomNavItem("Pembayaran", Icons.Default.ShoppingCart, DestinasiHomePembayaranSewa.route)
    )

    NavigationBar(modifier = modifier) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}