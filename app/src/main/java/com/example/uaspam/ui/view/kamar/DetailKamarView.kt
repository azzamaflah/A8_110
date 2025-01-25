package com.example.uaspam.ui.view.kamar
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
import com.example.uaspam.model.Kamar
import com.example.uaspam.ui.costumewidget.CustomTopAppBar
import com.example.uaspam.ui.navigation.DestinasiNavigasi
import com.example.uaspam.ui.viewmodel.PenyediaViewModel
import com.example.uaspam.ui.viewmodel.kamar.DetailKamarUiState
import com.example.uaspam.ui.viewmodel.kamar.DetailKamarViewModel

// Destinasi untuk Detail Kamar
object DestinasiDetailKamar : DestinasiNavigasi {
    override val route = "detail_kamar"
    override val titleRes = "Detail Kamar"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailKamarView(
    idKamar: String,
    navigateBack: () -> Unit,
    navController: NavHostController,
    viewModel: DetailKamarViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val detailUiState = viewModel.detailKamarUiState

    // Mengambil data kamar berdasarkan idKamar
    LaunchedEffect(idKamar) {
        viewModel.getDetailKamar(idKamar)
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = DestinasiDetailKamar.titleRes,
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("edit_kamar/$idKamar") },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Kamar"
                )
            }
        },
        content = { innerPadding ->
            DetailKamarContent(
                detailUiState = detailUiState,
                modifier = Modifier.padding(innerPadding),
                onDeleteClick = {
                    viewModel.deleteKamar(idKamar)
                    navigateBack()
                }
            )
        }
    )
}

@Composable
fun DetailKamarContent(
    detailUiState: DetailKamarUiState,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit
) {
    when (detailUiState) {
        is DetailKamarUiState.Loading -> OnLoading2(modifier = modifier.fillMaxSize())
        is DetailKamarUiState.Error -> OnError2(retryAction = {}, modifier = modifier.fillMaxSize())
        is DetailKamarUiState.Success -> DetailKamarCard(
            kamar = detailUiState.kamar,
            modifier = modifier,
            onDeleteClick = onDeleteClick
        )
    }
}

@Composable
fun DetailKamarCard(
    kamar: Kamar,
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
                    text = kamar.nomorKamar,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Hapus Kamar"
                    )
                }
            }
            Text(
                text = "ID Kamar: ${kamar.idKamar}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "ID Bangunan: ${kamar.idBangunan}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Kapasitas: ${kamar.kapasitas}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Status Kamar: ${kamar.statusKamar}",
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
            Text("Gagal memuat data kamar")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = retryAction) {
                Text("Coba Lagi")
            }
        }
    }
}
