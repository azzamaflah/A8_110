package com.example.uaspam.ui.view.pembayaransewa
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
import com.example.uaspam.ui.viewmodel.pembayaransewa.EditPembayaranSewaUiEvent
import com.example.uaspam.ui.viewmodel.pembayaransewa.EditPembayaranSewaUiState
import com.example.uaspam.ui.viewmodel.pembayaransewa.EditPembayaranSewaViewModel
import kotlinx.coroutines.launch

object DestinasiEditPembayaranSewa {
    const val route = "edit_pembayaran_sewa"
    const val titleRes = "Edit Pembayaran Sewa"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPembayaranSewaView(
    idPembayaran: String,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditPembayaranSewaViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var isLoading by remember { mutableStateOf(true) }

    // Load pembayaran sewa detail
    LaunchedEffect(idPembayaran) {
        viewModel.loadPembayaranSewaDetail(idPembayaran)
        isLoading = false
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                title = DestinasiEditPembayaranSewa.titleRes,
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
            EditPembayaranSewaBody(
                editUiState = viewModel.uiState,
                onPembayaranValueChange = viewModel::updateEditPembayaranSewaState,
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.updatePembayaranSewa()
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
fun EditPembayaranSewaBody(
    editUiState: EditPembayaranSewaUiState,
    onPembayaranValueChange: (EditPembayaranSewaUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FormPembayaranSewaInput(
            editUiEvent = editUiState.editUiEvent,
            onValueChange = onPembayaranValueChange,
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
fun FormPembayaranSewaInput(
    editUiEvent: EditPembayaranSewaUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (EditPembayaranSewaUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = editUiEvent.idMahasiswa,
            onValueChange = { onValueChange(editUiEvent.copy(idMahasiswa = it)) },
            label = { Text("ID Mahasiswa") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = editUiEvent.idPembayaran,
            onValueChange = { onValueChange(editUiEvent.copy(idPembayaran = it)) },
            label = { Text("ID Pembayaran") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false, // ID Pembayaran tidak bisa diubah
            singleLine = true
        )
        OutlinedTextField(
            value = editUiEvent.jumlah,
            onValueChange = { onValueChange(editUiEvent.copy(jumlah = it)) },
            label = { Text("Jumlah Pembayaran") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = editUiEvent.tanggalPembayaran,
            onValueChange = { onValueChange(editUiEvent.copy(tanggalPembayaran = it)) },
            label = { Text("Tanggal Pembayaran") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = editUiEvent.statusPembayaran,
            onValueChange = { onValueChange(editUiEvent.copy(statusPembayaran = it)) },
            label = { Text("Status Pembayaran") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        if (enabled) {
            Text(
                text = "Edit data pembayaran sewa yang diperlukan.",
                modifier = Modifier.padding(12.dp)
            )
        }
        Divider(
            thickness = 8.dp,
            modifier = Modifier.padding(12.dp)
        )
    }
}
