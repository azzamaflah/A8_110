package com.example.uaspam.ui.view.kamar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uaspam.model.Bangunan
import com.example.uaspam.ui.costumewidget.CustomTopAppBar
import com.example.uaspam.ui.viewmodel.PenyediaViewModel
import com.example.uaspam.ui.viewmodel.kamar.EditKamarUiEvent
import com.example.uaspam.ui.viewmodel.kamar.EditKamarUiState
import com.example.uaspam.ui.viewmodel.kamar.EditKamarViewModel
import kotlinx.coroutines.launch

object DestinasiEditKamar {
    const val route = "edit_kamar"
    const val titleRes = "Edit Kamar"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditKamarView(
    idKamar: String,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditKamarViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var isLoading by remember { mutableStateOf(true) }
    val bangunanList = viewModel.bangunanList

    // Load kamar detail
    LaunchedEffect(idKamar) {
        viewModel.loadKamarDetail(idKamar)
        isLoading = false
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                title = DestinasiEditKamar.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            EditKamarBody(
                bangunanList = bangunanList,
                editUiState = viewModel.uiState,
                onKamarValueChange = viewModel::updateEditKamarState,
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.updateKamar()
                        navigateBack()
                    }
                },
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun EditKamarBody(
    bangunanList: List<Bangunan>, // Ubah menjadi List<Bangunan>
    editUiState: EditKamarUiState,
    onKamarValueChange: (EditKamarUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FormKamarInput(
            bangunanList = bangunanList, // Ubah menjadi List<Bangunan>
            editUiEvent = editUiState.editUiEvent,
            onValueChange = onKamarValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Simpan Perubahan")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormKamarInput(
    bangunanList: List<Bangunan>, // Ubah menjadi List<Bangunan>
    editUiEvent: EditKamarUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (EditKamarUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    var expandedStatus by remember { mutableStateOf(false) }
    val statusOptions = listOf("TERSEDIA", "TIDAK TERSEDIA", "TERBOKING")
    var selectedBangunan by remember { mutableStateOf<Bangunan?>(null) }
    var selectedStatus by remember { mutableStateOf(editUiEvent.statusKamar) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = editUiEvent.nomorKamar,
            onValueChange = { onValueChange(editUiEvent.copy(nomorKamar = it)) },
            label = { Text("Nomor Kamar") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        // Nama Bangunan dropdown
        Box {
            OutlinedTextField(
                value = selectedBangunan?.namaBangunan ?: "Pilih Bangunan",
                onValueChange = {},
                label = { Text("Nama Bangunan") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }, // Menambahkan clickable
                enabled = false, // Tidak bisa mengedit nama bangunan
                singleLine = true
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                bangunanList.forEach { bangunan ->
                    DropdownMenuItem(
                        onClick = {
                            selectedBangunan = bangunan
                            onValueChange(editUiEvent.copy(idBangunan = bangunan.idBangunan))
                            expanded = false
                        },
                        text = { Text(bangunan.namaBangunan) }
                    )
                }
            }
        }

        OutlinedTextField(
            value = editUiEvent.kapasitas,
            onValueChange = { onValueChange(editUiEvent.copy(kapasitas = it)) },
            label = { Text("Kapasitas") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        // Status Kamar dropdown
        Box {
            OutlinedTextField(
                value = selectedStatus.ifEmpty { "Pilih Status Kamar" },
                onValueChange = {},
                label = { Text("Status Kamar") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedStatus = true }, // Menambahkan clickable untuk status
                enabled = false, // Tidak bisa mengedit status
                singleLine = true
            )
            DropdownMenu(
                expanded = expandedStatus,
                onDismissRequest = { expandedStatus = false }
            ) {
                statusOptions.forEach { status ->
                    DropdownMenuItem(
                        onClick = {
                            selectedStatus = status
                            onValueChange(editUiEvent.copy(statusKamar = status)) // Update statusKamar
                            expandedStatus = false
                        },
                        text = { Text(status) }
                    )
                }
            }
        }
    }
}