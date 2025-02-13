package com.example.uaspam.ui.view.kamar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.uaspam.model.Kamar
import com.example.uaspam.ui.costumewidget.CustomTopAppBar
import com.example.uaspam.ui.navigation.DestinasiNavigasi
import com.example.uaspam.ui.view.bangunan.EmptyData
import com.example.uaspam.ui.view.bangunan.OnError
import com.example.uaspam.ui.view.bangunan.OnLoading
import com.example.uaspam.ui.viewmodel.PenyediaViewModel
import com.example.uaspam.ui.viewmodel.kamar.HomeKamarUiState
import com.example.uaspam.ui.viewmodel.kamar.HomeKamarViewModel

object DestinasiKamarHome : DestinasiNavigasi {
    override val route = "home_kamar"
    override val titleRes = "Home Kamar"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeKamarScreen(
    navController: NavHostController,
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {},
    viewModel: HomeKamarViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val currentRoute = remember { navController.currentBackStackEntry?.destination?.route ?: "" }

    LaunchedEffect(Unit) {
        viewModel.getKamar()
    }

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
        bottomBar = {
            com.example.uaspam.ui.costumewidget.BottomAppBar(
                navController = navController,
                currentRoute = currentRoute
            )
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
                    onDetailClick = onDetailClick,
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
    onDetailClick: (String) -> Unit,
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
                    .clickable { onDetailClick(kamar.idKamar) },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Nomor Kamar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "ID Kamar",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "ID Kamar: ${kamar.idKamar}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Kapasitas
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Kapasitas",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Kapasitas: ${kamar.kapasitas}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Status Kamar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Status Kamar",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Status: ${kamar.statusKamar}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Delete Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Kamar"
                    )
                }
            }
        }
    }

    // Dialog confirmation for delete
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
