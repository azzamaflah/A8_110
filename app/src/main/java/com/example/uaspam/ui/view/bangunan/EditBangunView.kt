package com.example.uaspam.ui.view.bangunan
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uaspam.ui.costumewidget.CustomTopAppBar
import com.example.uaspam.ui.viewmodel.PenyediaViewModel
import com.example.uaspam.ui.viewmodel.bangunan.EditBangunanUiEvent
import com.example.uaspam.ui.viewmodel.bangunan.EditBangunanUiState
import com.example.uaspam.ui.viewmodel.bangunan.EditBangunanViewModel
import kotlinx.coroutines.launch

object DestinasiEditBangunan {
    const val route = "edit_bangunan"
    const val titleRes = "Edit Bangunan"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBangunanView(
    idBangunan: String,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditBangunanViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var isLoading by remember { mutableStateOf(true) }

    // Load bangunan detail
    LaunchedEffect(idBangunan) {
        viewModel.loadBangunanDetail(idBangunan)
        isLoading = false
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                title = DestinasiEditBangunan.titleRes,
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
            EditBangunanBody(
                editUiState = viewModel.uiState,
                onBangunanValueChange = viewModel::updateEditBangunanState,
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.updateBangunan()
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
fun EditBangunanBody(
    editUiState: EditBangunanUiState,
    onBangunanValueChange: (EditBangunanUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FormBangunanInput(
            editUiEvent = editUiState.editUiEvent,
            onValueChange = onBangunanValueChange,
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
fun FormBangunanInput(
    editUiEvent: EditBangunanUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (EditBangunanUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = editUiEvent.namaBangunan,
            onValueChange = { onValueChange(editUiEvent.copy(namaBangunan = it)) },
            label = { Text("Nama Bangunan") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = editUiEvent.idBangunan,
            onValueChange = { onValueChange(editUiEvent.copy(idBangunan = it)) },
            label = { Text("ID Bangunan") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false, // ID tidak boleh diubah
            singleLine = true
        )
        OutlinedTextField(
            value = editUiEvent.jumlahLantai,
            onValueChange = { onValueChange(editUiEvent.copy(jumlahLantai = it)) },
            label = { Text("Jumlah Lantai") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = editUiEvent.alamat,
            onValueChange = { onValueChange(editUiEvent.copy(alamat = it)) },
            label = { Text("Alamat") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        if (enabled) {
            Text(
                text = "Edit data bangunan dengan benar.",
                modifier = Modifier.padding(12.dp)
            )
        }
        Divider(
            thickness = 8.dp,
            modifier = Modifier.padding(12.dp)
        )
    }
}
