package com.example.uaspam.ui.view.pembayaran
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.uaspam.model.PembayaraanSewa
import com.example.uaspam.ui.costumewidget.CustomTopAppBar
import com.example.uaspam.ui.navigation.DestinasiNavigasi
import com.example.uaspam.ui.view.bangunan.EmptyData
import com.example.uaspam.ui.view.bangunan.OnError
import com.example.uaspam.ui.view.bangunan.OnLoading
import com.example.uaspam.ui.viewmodel.PenyediaViewModel
import com.example.uaspam.ui.viewmodel.pembayaran.HomePembayaranSewaUiState
import com.example.uaspam.ui.viewmodel.pembayaran.HomePembayaranSewaViewModel

object DestinasiHomePembayaranSewa : DestinasiNavigasi {
    override val route = "home_pembayaran_sewa"
    override val titleRes = "Home Pembayaran Sewa"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePembayaranSewaView(
    navController: NavHostController,
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {},
    viewModel: HomePembayaranSewaViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val currentRoute = remember { navController.currentBackStackEntry?.destination?.route ?: "" }

    LaunchedEffect(Unit) {
        viewModel.getPembayaranSewa()
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                title = DestinasiHomePembayaranSewa.titleRes,
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                onRefresh = { viewModel.getPembayaranSewa() }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah Pembayaran Sewa"
                )
            }
        },
        bottomBar = {
            com.example.uaspam.ui.costumewidget.BottomAppBar(
                navController = navController,
                currentRoute = currentRoute
            )
        },
        content = { innerPadding ->
            HomePembayaranSewaStatus(
                pembayaranUiState = viewModel.pembayaranSewaUiState,
                retryAction = { viewModel.getPembayaranSewa() },
                modifier = Modifier.padding(innerPadding),
                onDetailClick = onDetailClick,
                onDeleteClick = {
                    viewModel.deletePembayaranSewa(it.idPembayaran)
                    viewModel.getPembayaranSewa()
                }
            )
        }
    )
}

@Composable
fun HomePembayaranSewaStatus(
    pembayaranUiState: HomePembayaranSewaUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (PembayaraanSewa) -> Unit = {},
    onDetailClick: (String) -> Unit
) {
    when (pembayaranUiState) {
        is HomePembayaranSewaUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is HomePembayaranSewaUiState.Success -> {
            if (pembayaranUiState.pembayaranSewa.isEmpty()) {
                EmptyData(modifier)
            } else {
                PembayaranSewaList(
                    pembayaran = pembayaranUiState.pembayaranSewa,
                    modifier = modifier.fillMaxWidth(),
                    onDetailClick = onDetailClick,
                    onDeleteClick = { onDeleteClick(it) }
                )
            }
        }
        is HomePembayaranSewaUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun PembayaranSewaList(
    pembayaran: List<PembayaraanSewa>,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit,
    onDeleteClick: (PembayaraanSewa) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(pembayaran) { item ->
            PembayaranSewaCard(
                pembayaranSewa = item,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDetailClick(item.idPembayaran) },
                onDeleteClick = { onDeleteClick(item) }
            )
        }
    }
}

@Composable
fun PembayaranSewaCard(
    pembayaranSewa: PembayaraanSewa,
    modifier: Modifier = Modifier,
    onDeleteClick: (PembayaraanSewa) -> Unit = {}
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = pembayaranSewa.idMahasiswa,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Hapus Pembayaran"
                    )
                }
            }

            // ID Pembayaran
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "ID Pembayaran",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "ID Pembayaran: ${pembayaranSewa.idPembayaran}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Status Pembayaran
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Status Pembayaran",
                    tint = if (pembayaranSewa.statusPembayaran == "Lunas") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Status: ${pembayaranSewa.statusPembayaran}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }

    // Konfirmasi Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Konfirmasi Hapus") },
            text = { Text("Apakah Anda yakin ingin menghapus pembayaran ini?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteClick(pembayaranSewa)
                        showDialog = false
                    }
                ) {
                    Text("Hapus")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

