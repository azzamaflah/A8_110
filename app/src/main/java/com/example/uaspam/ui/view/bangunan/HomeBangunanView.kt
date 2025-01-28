package com.example.uaspam.ui.view.bangunan
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.uaspam.model.Bangunan
import com.example.uaspam.ui.navigation.DestinasiNavigasi
import com.example.uaspam.ui.viewmodel.PenyediaViewModel
import com.example.uaspam.ui.viewmodel.bangunan.HomeBangunanViewModel
import com.example.uaspam.ui.viewmodel.bangunan.HomeBangunanUiState
import kotlinx.coroutines.launch

object DestinasiHomeBangunan : DestinasiNavigasi {
    override val route = "home_bangunan"
    override val titleRes = "Home Bangunan"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBangunanView(
    navController: NavHostController,
    navigateToItemEntry: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit = {},
    viewModel: HomeBangunanViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val currentRoute = remember { navController.currentBackStackEntry?.destination?.route ?: "" }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(DestinasiHomeBangunan.titleRes) },
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(onClick = { viewModel.getBangunan() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
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
                    contentDescription = "Add Bangunan"
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
            HomeBangunanStatus(
                bangunanUiState = viewModel.bangunanUiState,
                retryAction = { viewModel.getBangunan() },
                modifier = Modifier.padding(innerPadding),
                onDetailClick = onDetailClick,
                onDeleteClick = {
                    viewModel.deleteBangunan(it.idBangunan)
                    viewModel.getBangunan()
                }
            )
        }
    )
}

@Composable
fun HomeBangunanStatus(
    bangunanUiState: HomeBangunanUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: (Bangunan) -> Unit = {},
    onDetailClick: (String) -> Unit
) {
    when (bangunanUiState) {
        is HomeBangunanUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is HomeBangunanUiState.Success -> {
            if (bangunanUiState.bangunan.isEmpty()) {
                EmptyData(modifier)
            } else {
                BangunanList(
                    bangunan = bangunanUiState.bangunan,
                    modifier = modifier,
                    onDetailClick = onDetailClick,
                    onDeleteClick = onDeleteClick
                )
            }
        }
        is HomeBangunanUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun BangunanList(
    bangunan: List<Bangunan>,
    modifier: Modifier = Modifier,
    onDetailClick: (String) -> Unit,
    onDeleteClick: (Bangunan) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(bangunan) { bgn ->
            BangunanCard(
                bangunan = bgn,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDetailClick(bgn.idBangunan) },
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Composable
fun BangunanCard(
    bangunan: Bangunan,
    modifier: Modifier = Modifier,
    onDeleteClick: (Bangunan) -> Unit
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
                // Nama Bangunan
                Text(
                    text = bangunan.namaBangunan,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                // Tombol Hapus
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Bangunan"
                    )
                }
            }

            // Menampilkan Jumlah Lantai
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh, // Menggunakan ikon refresh untuk mewakili lantai
                    contentDescription = "Jumlah Lantai",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Lantai: ${bangunan.jumlahLantai}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Menampilkan Alamat Bangunan
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn, // Ikon lokasi
                    contentDescription = "Alamat",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = bangunan.alamat,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Menampilkan ID Bangunan
            Text(
                text = "ID: ${bangunan.idBangunan}",
                style = MaterialTheme.typography.bodySmall
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
                Text("Apakah Anda yakin ingin menghapus bangunan ini?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteClick(bangunan)
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
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Tidak ada data bangunan")
    }
}

@Composable
fun OnError(retryAction: () -> Unit, modifier: Modifier = Modifier) {
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