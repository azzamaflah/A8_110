package com.example.uaspam.ui.view.mahasiswa
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
import com.example.uaspam.model.Mahasiswa
import com.example.uaspam.ui.costumewidget.CustomTopAppBar
import com.example.uaspam.ui.navigation.DestinasiNavigasi
import com.example.uaspam.ui.viewmodel.PenyediaViewModel
import com.example.uaspam.ui.viewmodel.mahasiswa.DetailMahasiswaUiState
import com.example.uaspam.ui.viewmodel.mahasiswa.DetailMahasiswaViewModel

// Destinasi untuk Detail Mahasiswa
object DestinasiDetailMahasiswa : DestinasiNavigasi {
    override val route = "detail_mahasiswa"
    override val titleRes = "Detail Mahasiswa"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailMahasiswaView(
    idMahasiswa: String,
    navigateBack: () -> Unit,
    navController: NavHostController,
    viewModel: DetailMahasiswaViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val detailUiState = viewModel.detailMahasiswaUiState

    // Mengambil data mahasiswa berdasarkan ID Mahasiswa
    LaunchedEffect(idMahasiswa) {
        viewModel.getDetailMahasiswa(idMahasiswa)
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = DestinasiDetailMahasiswa.titleRes,
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("edit_mahasiswa/$idMahasiswa") },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Mahasiswa"
                )
            }
        },
        content = { innerPadding ->
            DetailMahasiswaContent(
                detailUiState = detailUiState,
                modifier = Modifier.padding(innerPadding),
                onDeleteClick = {
                    viewModel.deleteMahasiswa(idMahasiswa)
                    navigateBack()
                }
            )
        }
    )
}

@Composable
fun DetailMahasiswaContent(
    detailUiState: DetailMahasiswaUiState,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit
) {
    when (detailUiState) {
        is DetailMahasiswaUiState.Loading -> OnLoading2(modifier = modifier.fillMaxSize())
        is DetailMahasiswaUiState.Error -> OnError2(retryAction = {}, modifier = modifier.fillMaxSize())
        is DetailMahasiswaUiState.Success -> DetailMahasiswaCard(
            mahasiswa = detailUiState.mahasiswa,
            modifier = modifier,
            onDeleteClick = onDeleteClick
        )
    }
}

@Composable
fun DetailMahasiswaCard(
    mahasiswa: Mahasiswa,
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
                    text = mahasiswa.namaMahasiswa,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Hapus Mahasiswa"
                    )
                }
            }
            Text(
                text = "ID Mahasiswa: ${mahasiswa.idMahasiswa}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Nomor Identitas: ${mahasiswa.nomorIdentitas}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Email: ${mahasiswa.email}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Nomor Telepon: ${mahasiswa.nomorTelepon}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "ID Kamar: ${mahasiswa.idKamar}",
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
            Text("Gagal memuat data mahasiswa")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = retryAction) {
                Text("Coba Lagi")
            }
        }
    }
}
