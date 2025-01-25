package com.example.uaspam.ui.view.mahasiswa
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uaspam.model.Mahasiswa
import com.example.uaspam.ui.navigation.DestinasiNavigasi
import com.example.uaspam.ui.viewmodel.PenyediaViewModel
import com.example.uaspam.ui.viewmodel.mahasiswa.HomeViewModel
import com.example.uaspam.ui.viewmodel.mahasiswa.HomeUiState
import kotlinx.coroutines.launch

object DestinasiHomeMahasiswa : DestinasiNavigasi {
    override val route = "home_mahasiswa"
    override val titleRes = "Home Mahasiswa"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeMahasiswaView(
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {},
    viewModel: HomeViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }


    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(DestinasiHomeMahasiswa.titleRes) },
                actions = {
                    IconButton(onClick = { viewModel.getMhs() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToItemEntry) {
                Icon(Icons.Default.Add, contentDescription = "Add Mahasiswa")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { innerPadding ->
            HomeMahasiswaStatus(
                mahasiswaUiState = viewModel.mhsUIState,
                retryAction = { viewModel.getMhs() },
                modifier = Modifier.padding(innerPadding),
                onDetailClick = onDetailClick,
                onDeleteClick = { mahasiswa ->
                    coroutineScope.launch {
                        try {
                            viewModel.deleteMhs(mahasiswa.idMahasiswa)
                            snackbarHostState.showSnackbar("Mahasiswa berhasil dihapus")
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("Gagal menghapus mahasiswa")
                        }
                        viewModel.getMhs()
                    }
                }
            )
        }
    )
}

@Composable
fun HomeMahasiswaStatus(
    mahasiswaUiState: HomeUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (Mahasiswa) -> Unit = {},
    onDetailClick: (String) -> Unit
) {
    when (mahasiswaUiState) {
        is HomeUiState.Loading -> OnLoading(modifier.fillMaxSize())
        is HomeUiState.Success -> {
            if (mahasiswaUiState.mahasiswa.isEmpty()) {
                EmptyData(modifier)
            } else {
                MahasiswaList(
                    mahasiswa = mahasiswaUiState.mahasiswa,
                    onDetailClick = onDetailClick,
                    onDeleteClick = onDeleteClick,
                    modifier = modifier.fillMaxSize()
                )
            }
        }
        is HomeUiState.Error -> OnError(retryAction, modifier.fillMaxSize())
    }
}

@Composable
fun MahasiswaList(
    mahasiswa: List<Mahasiswa>,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit,
    onDeleteClick: (Mahasiswa) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(mahasiswa) { mhs ->
            MahasiswaCard(
                mahasiswa = mhs,
                onDetailClick = onDetailClick,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Composable
fun MahasiswaCard(
    mahasiswa: Mahasiswa,
    onDetailClick: (String) -> Unit,
    onDeleteClick: (Mahasiswa) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onDetailClick(mahasiswa.idMahasiswa) },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = mahasiswa.namaMahasiswa,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "ID: ${mahasiswa.idMahasiswa}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = { onDeleteClick(mahasiswa) }) {
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
        Text("Tidak ada data mahasiswa")
    }
}

@Composable
fun OnError(retryAction: () -> Unit, modifier: Modifier = Modifier) {
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
