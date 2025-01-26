package com.example.uaspam.ui.view.kamar
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
    editUiEvent: EditKamarUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (EditKamarUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
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
        OutlinedTextField(
            value = editUiEvent.idKamar,
            onValueChange = { onValueChange(editUiEvent.copy(idKamar = it)) },
            label = { Text("ID Kamar") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false, // ID Kamar tidak bisa diubah
            singleLine = true
        )
        OutlinedTextField(
            value = editUiEvent.idBangunan,
            onValueChange = { onValueChange(editUiEvent.copy(idBangunan = it)) },
            label = { Text("ID Bangunan") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = editUiEvent.kapasitas,
            onValueChange = { onValueChange(editUiEvent.copy(kapasitas = it)) },
            label = { Text("Kapasitas") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = editUiEvent.statusKamar,
            onValueChange = { onValueChange(editUiEvent.copy(statusKamar = it)) },
            label = { Text("Status Kamar") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        if (enabled) {
            Text(
                text = "Edit data kamar yang diperlukan.",
                modifier = Modifier.padding(12.dp)
            )
        }
        Divider(
            thickness = 8.dp,
            modifier = Modifier.padding(12.dp)
        )
    }
}
