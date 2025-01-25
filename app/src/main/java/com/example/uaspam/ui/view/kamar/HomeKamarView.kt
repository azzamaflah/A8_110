package com.example.uaspam.ui.view.kamar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uaspam.model.Kamar
import com.example.uaspam.ui.costumewidget.CustomTopAppBar
import com.example.uaspam.ui.navigation.DestinasiNavigasi
import com.example.uaspam.ui.view.mahasiswa.EmptyData
import com.example.uaspam.ui.view.mahasiswa.OnError
import com.example.uaspam.ui.view.mahasiswa.OnLoading
import com.example.uaspam.ui.viewmodel.PenyediaViewModel
import com.example.uaspam.ui.viewmodel.kamar.HomeKamarUiState
import com.example.uaspam.ui.viewmodel.kamar.HomeKamarViewModel
import kotlinx.coroutines.launch

object DestinasiKamarHome : DestinasiNavigasi {
    override val route = "home_kamar"
    override val titleRes = "Home Kamar"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeKamarScreen(
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {},
    viewModel: HomeKamarViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                title = DestinasiKamarHome.titleRes,
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
                onRefresh = { viewModel.getKamar() }
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
                    contentDescription = "Add Kamar"
                )
            }
        },
        content = { innerPadding ->
            HomeKamarStatus(
                kamarUiState = viewModel.kamarUiState,
                retryAction = { viewModel.getKamar() },
                modifier = Modifier.padding(innerPadding),
                onDetailClick = onDetailClick,
                onDeleteClick = {
                    viewModel.deleteKamar(it.idKamar)
                    viewModel.getKamar()
                }
            )
        }
    )
}

@Composable
fun HomeKamarStatus(
    kamarUiState: HomeKamarUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (Kamar) -> Unit = {},
    onDetailClick: (String) -> Unit
) {
    when (kamarUiState) {
        is HomeKamarUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is HomeKamarUiState.Success -> {
            if (kamarUiState.kamar.isEmpty()) {
                EmptyData(modifier)
            } else {
                KamarLayout(
                    kamar = kamarUiState.kamar,
                    modifier = modifier.fillMaxWidth(),
                    onDetailClick = { onDetailClick(it.idKamar) },
                    onDeleteClick = { onDeleteClick(it) }
                )
            }
        }
        is HomeKamarUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun KamarLayout(
    kamar: List<Kamar>,
    modifier: Modifier = Modifier,
    onDetailClick: (Kamar) -> Unit,
    onDeleteClick: (Kamar) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(kamar) { kamar ->
            KamarCard(
                kamar = kamar,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDetailClick(kamar) },
                onDeleteClick = { onDeleteClick(kamar) }
            )
        }
    }
}

@Composable
fun KamarCard(
    kamar: Kamar,
    modifier: Modifier = Modifier,
    onDeleteClick: (Kamar) -> Unit = {}
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = kamar.nomorKamar,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null
                    )
                }
            }
            Text(
                text = kamar.kapasitas,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = kamar.statusKamar,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(text = "Konfirmasi Hapus")
            },
            text = {
                Text("Apakah Anda yakin ingin menghapus kamar ini?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteClick(kamar)
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
