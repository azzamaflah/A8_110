package com.example.uaspam.ui.view.pembayaransewa
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.uaspam.model.PembayaraanSewa
import com.example.uaspam.ui.costumewidget.CustomTopAppBar
import com.example.uaspam.ui.navigation.DestinasiNavigasi
import com.example.uaspam.ui.viewmodel.PenyediaViewModel
import com.example.uaspam.ui.viewmodel.pembayaransewa.DetailPembayaranSewaUiState
import com.example.uaspam.ui.viewmodel.pembayaransewa.DetailPembayaranSewaViewModel

// Destinasi untuk Detail Pembayaran Sewa
object DestinasiDetailPembayaranSewa : DestinasiNavigasi {
    override val route = "detail_pembayaran_sewa"
    override val titleRes = "Detail Pembayaran Sewa"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPembayaranSewaView(
    idPembayaran: String,
    navigateBack: () -> Unit,
    navController: NavHostController,
    viewModel: DetailPembayaranSewaViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val detailUiState = viewModel.detailPembayaranSewaUiState

    // Mengambil data pembayaran berdasarkan ID Pembayaran
    LaunchedEffect(idPembayaran) {
        viewModel.getDetailPembayaran(idPembayaran)
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = DestinasiDetailPembayaranSewa.titleRes,
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("edit_pembayaran_sewa/$idPembayaran") },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Pembayaran"
                )
            }
        },
        content = { innerPadding ->
            DetailPembayaranSewaContent(
                detailUiState = detailUiState,
                modifier = Modifier.padding(innerPadding),
                onDeleteClick = {
                    viewModel.deletePembayaran(idPembayaran)
                    navigateBack()
                }
            )
        }
    )
}

@Composable
fun DetailPembayaranSewaContent(
    detailUiState: DetailPembayaranSewaUiState,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit
) {
    when (detailUiState) {
        is DetailPembayaranSewaUiState.Loading -> OnLoading2(modifier = modifier.fillMaxSize())
        is DetailPembayaranSewaUiState.Error -> OnError2(retryAction = {}, modifier = modifier.fillMaxSize())
        is DetailPembayaranSewaUiState.Success -> DetailPembayaranSewaCard(
            pembayaran = detailUiState.pembayaranSewa,
            modifier = modifier,
            onDeleteClick = onDeleteClick
        )
    }
}

@Composable
fun DetailPembayaranSewaCard(
    pembayaran: PembayaraanSewa,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = pembayaran.idPembayaran,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Hapus Pembayaran"
                    )
                }
            }
            Text(
                text = "Jumlah: ${pembayaran.jumlah}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Tanggal: ${pembayaran.tanggalPembayaran}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Status: ${pembayaran.statusPembayaran}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun OnLoading2(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun OnError2(retryAction: () -> Unit, modifier: Modifier = Modifier) {
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
