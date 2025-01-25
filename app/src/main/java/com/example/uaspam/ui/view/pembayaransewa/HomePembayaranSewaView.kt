package com.example.uaspam.ui.view.pembayaran
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uaspam.model.PembayaraanSewa
import com.example.uaspam.ui.navigation.DestinasiNavigasi
import com.example.uaspam.ui.viewmodel.PenyediaViewModel
import com.example.uaspam.ui.viewmodel.pembayaran.HomePembayaranSewaViewModel
import com.example.uaspam.ui.viewmodel.pembayaran.HomePembayaranSewaUiState
import kotlinx.coroutines.launch

object DestinasiHomePembayaranSewa : DestinasiNavigasi {
    override val route = "home_pembayaran_sewa"
    override val titleRes = "Home Pembayaran Sewa"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePembayaranSewaView(
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {},
    viewModel: HomePembayaranSewaViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(DestinasiHomePembayaranSewa.titleRes) },
                actions = {
                    IconButton(onClick = { viewModel.getPembayaranSewa() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToItemEntry) {
                Icon(Icons.Default.Add, contentDescription = "Add Pembayaran Sewa")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { innerPadding ->
            HomePembayaranSewaStatus(
                pembayaranSewaUiState = viewModel.pembayaranSewaUiState,
                retryAction = { viewModel.getPembayaranSewa() },
                modifier = Modifier.padding(innerPadding),
                onDetailClick = onDetailClick,
                onDeleteClick = { pembayaran ->
                    coroutineScope.launch {
                        try {
                            viewModel.deletePembayaranSewa(pembayaran.idPembayaran)
                            snackbarHostState.showSnackbar("Pembayaran berhasil dihapus")
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("Gagal menghapus pembayaran")
                        }
                        viewModel.getPembayaranSewa()
                    }
                }
            )
        }
    )
}

@Composable
fun HomePembayaranSewaStatus(
    pembayaranSewaUiState: HomePembayaranSewaUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (PembayaraanSewa) -> Unit = {},
    onDetailClick: (String) -> Unit
) {
    when (pembayaranSewaUiState) {
        is HomePembayaranSewaUiState.Loading -> OnLoading(modifier.fillMaxSize())
        is HomePembayaranSewaUiState.Success -> {
            if (pembayaranSewaUiState.pembayaranSewa.isEmpty()) {
                EmptyData(modifier)
            } else {
                PembayaranSewaList(
                    pembayaranSewa = pembayaranSewaUiState.pembayaranSewa,
                    onDetailClick = onDetailClick,
                    onDeleteClick = onDeleteClick,
                    modifier = modifier.fillMaxSize()
                )
            }
        }
        is HomePembayaranSewaUiState.Error -> OnError(retryAction, modifier.fillMaxSize())
    }
}

@Composable
fun PembayaranSewaList(
    pembayaranSewa: List<PembayaraanSewa>,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit,
    onDeleteClick: (PembayaraanSewa) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(pembayaranSewa) { pembayaran ->
            PembayaranSewaCard(
                pembayaranSewa = pembayaran,
                onDetailClick = onDetailClick,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Composable
fun PembayaranSewaCard(
    pembayaranSewa: PembayaraanSewa,
    onDetailClick: (String) -> Unit,
    onDeleteClick: (PembayaraanSewa) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onDetailClick(pembayaranSewa.idPembayaran) },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = pembayaranSewa.idMahasiswa,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "ID: ${pembayaranSewa.idPembayaran}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { onDeleteClick(pembayaranSewa) }) {
                    Text("Hapus")
                }
            }
        }
    }
}

@Composable
fun OnLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun EmptyData(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text("Tidak ada data pembayaran sewa")
    }
}

@Composable
fun OnError(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Gagal memuat data pembayaran sewa")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = retryAction) {
                Text("Coba Lagi")
            }
        }
    }
}