package com.example.uaspam.ui.view.bangunan
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.uaspam.model.Bangunan
import com.example.uaspam.ui.costumewidget.CustomTopAppBar
import com.example.uaspam.ui.navigation.DestinasiNavigasi
import com.example.uaspam.ui.viewmodel.PenyediaViewModel
import com.example.uaspam.ui.viewmodel.bangunan.DetailBangunanUiState
import com.example.uaspam.ui.viewmodel.bangunan.DetailBangunanViewModel


object DestinasiDetailBangunan : DestinasiNavigasi {
    override val route = "detail_bangunan"
    override val titleRes = "Detail Bangunan"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBangunanView(
    idBangunan: String,
    navigateBack: () -> Unit,
    navController: NavHostController,
    viewModel: DetailBangunanViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val detailUiState = viewModel.detailBangunanUiState

    // Mengambil data bangunan berdasarkan idBangunan
    LaunchedEffect(idBangunan) {
        viewModel.getDetailBangunan(idBangunan)
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "Detail Bangunan",
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {navController.navigate("edit_bangunan/$idBangunan")},
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Add Bangunan"
                )
            }
        },
        content = { innerPadding ->
            DetailBangunanContent(
                detailUiState = detailUiState,
                modifier = Modifier.padding(innerPadding),
                onDeleteClick = {
                    viewModel.deleteBangunan(idBangunan)
                    navigateBack()
                }
            )
        }
    )
}

@Composable
fun DetailBangunanContent(
    detailUiState: DetailBangunanUiState,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit
) {
    when (detailUiState) {
        is DetailBangunanUiState.Loading -> OnLoading2(modifier = modifier.fillMaxSize())
        is DetailBangunanUiState.Error -> OnError2(retryAction = {}, modifier = modifier.fillMaxSize())
        is DetailBangunanUiState.Success -> DetailBangunanCard(
            bangunan = detailUiState.bangunan,
            modifier = modifier,
            onDeleteClick = onDeleteClick
        )
    }
}

@Composable
fun DetailBangunanCard(
    bangunan: Bangunan,
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
                    text = bangunan.namaBangunan,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Hapus Bangunan"
                    )
                }
            }
            Text(
                text = "ID: ${bangunan.idBangunan}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Jumlah Lantai: ${bangunan.jumlahLantai}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Alamat: ${bangunan.alamat}",
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
            Text("Gagal memuat data bangunan")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = retryAction) {
                Text("Coba Lagi")
            }
        }
    }
}
