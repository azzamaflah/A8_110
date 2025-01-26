package com.example.uaspam.ui.view.mahasiswa
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
import com.example.uaspam.ui.viewmodel.mahasiswa.EditMahasiswaUiEvent
import com.example.uaspam.ui.viewmodel.mahasiswa.EditMahasiswaUiState
import com.example.uaspam.ui.viewmodel.mahasiswa.EditMahasiswaViewModel
import kotlinx.coroutines.launch

object DestinasiEditMahasiswa {
    const val route = "edit_mahasiswa"
    const val titleRes = "Edit Mahasiswa"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMahasiswaView(
    idMahasiswa: String,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditMahasiswaViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var isLoading by remember { mutableStateOf(true) }

    // Load mahasiswa detail
    LaunchedEffect(idMahasiswa) {
        viewModel.loadMahasiswaDetail(idMahasiswa)
        isLoading = false
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                title = DestinasiEditMahasiswa.titleRes,
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
            EditMahasiswaBody(
                editUiState = viewModel.uiState,
                onMahasiswaValueChange = viewModel::updateEditMahasiswaState,
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.updateMahasiswa()
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
fun EditMahasiswaBody(
    editUiState: EditMahasiswaUiState,
    onMahasiswaValueChange: (EditMahasiswaUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FormMahasiswaInput(
            editUiEvent = editUiState.editUiEvent,
            onValueChange = onMahasiswaValueChange,
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
fun FormMahasiswaInput(
    editUiEvent: EditMahasiswaUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (EditMahasiswaUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = editUiEvent.namaMahasiswa,
            onValueChange = { onValueChange(editUiEvent.copy(namaMahasiswa = it)) },
            label = { Text("Nama Mahasiswa") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = editUiEvent.idMahasiswa,
            onValueChange = { onValueChange(editUiEvent.copy(idMahasiswa = it)) },
            label = { Text("ID Mahasiswa") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false, // ID Mahasiswa tidak bisa diubah
            singleLine = true
        )
        OutlinedTextField(
            value = editUiEvent.nomorIdentitas,
            onValueChange = { onValueChange(editUiEvent.copy(nomorIdentitas = it)) },
            label = { Text("Nomor Identitas") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = editUiEvent.email,
            onValueChange = { onValueChange(editUiEvent.copy(email = it)) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = editUiEvent.nomorTelepon,
            onValueChange = { onValueChange(editUiEvent.copy(nomorTelepon = it)) },
            label = { Text("Nomor Telepon") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = editUiEvent.idKamar,
            onValueChange = { onValueChange(editUiEvent.copy(idKamar = it)) },
            label = { Text("ID Kamar") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        Divider(
            thickness = 8.dp,
            modifier = Modifier.padding(12.dp)
        )
    }
}
