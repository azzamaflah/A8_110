package com.example.uaspam.ui.view.mahasiswa
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uaspam.model.Kamar
import com.example.uaspam.ui.costumewidget.CustomTopAppBar
import com.example.uaspam.ui.navigation.DestinasiNavigasi
import com.example.uaspam.ui.viewmodel.PenyediaViewModel
import com.example.uaspam.ui.viewmodel.mahasiswa.InsertMahasiswaUiEvent
import com.example.uaspam.ui.viewmodel.mahasiswa.InsertMahasiswaUiState
import com.example.uaspam.ui.viewmodel.mahasiswa.InsertMahasiswaViewModel
import kotlinx.coroutines.launch

object DestinasiEntryMahasiswa : DestinasiNavigasi {
    override val route = "entry_mahasiswa"
    override val titleRes = "Entry Mahasiswa"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertMahasiswaView(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertMahasiswaViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val kamarList = viewModel.kamarList

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                title = DestinasiEntryMahasiswa.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        EntryMahasiswaBody(
            insertUiState = viewModel.uiState,
            kamarList = kamarList,
            onMahasiswaValueChange = viewModel::updateInsertMahasiswaState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.insertMahasiswa()
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

@Composable
fun EntryMahasiswaBody(
    insertUiState: InsertMahasiswaUiState,
    kamarList: List<Kamar>,
    onMahasiswaValueChange: (InsertMahasiswaUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FormMahasiswaInput(
            insertUiEvent = insertUiState.insertUiEvent,
            kamarList = kamarList,
            onValueChange = onMahasiswaValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Simpan")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormMahasiswaInput(
    insertUiEvent: InsertMahasiswaUiEvent,
    kamarList: List<Kamar>,
    modifier: Modifier = Modifier,
    onValueChange: (InsertMahasiswaUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    var expandedKamar by remember { mutableStateOf(false) }
    var selectedKamar by remember { mutableStateOf<Kamar?>(null) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Nama Mahasiswa
        OutlinedTextField(
            value = insertUiEvent.namaMahasiswa,
            onValueChange = { onValueChange(insertUiEvent.copy(namaMahasiswa = it)) },
            label = { Text("Nama Mahasiswa") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        // ID Mahasiswa
        OutlinedTextField(
            value = insertUiEvent.idMahasiswa,
            onValueChange = { onValueChange(insertUiEvent.copy(idMahasiswa = it)) },
            label = { Text("ID Mahasiswa") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        // Nomor Identitas
        OutlinedTextField(
            value = insertUiEvent.nomorIdentitas,
            onValueChange = { onValueChange(insertUiEvent.copy(nomorIdentitas = it)) },
            label = { Text("Nomor Identitas") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        // Email
        OutlinedTextField(
            value = insertUiEvent.email,
            onValueChange = { onValueChange(insertUiEvent.copy(email = it)) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        // Nomor Telepon
        OutlinedTextField(
            value = insertUiEvent.nomorTelepon,
            onValueChange = { onValueChange(insertUiEvent.copy(nomorTelepon = it)) },
            label = { Text("Nomor Telepon") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        // Dropdown untuk memilih Kamar
        Box {
            OutlinedTextField(
                value = selectedKamar?.nomorKamar ?: "Pilih Kamar",
                onValueChange = {},
                label = { Text("ID Kamar") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedKamar = true },
                enabled = false,
                singleLine = true
            )
            DropdownMenu(
                expanded = expandedKamar,
                onDismissRequest = { expandedKamar = false }
            ) {
                kamarList.forEach { kamar ->
                    DropdownMenuItem(
                        onClick = {
                            selectedKamar = kamar
                            onValueChange(insertUiEvent.copy(idKamar = kamar.idKamar))
                            expandedKamar = false
                        },
                        text = { Text(kamar.nomorKamar) }
                    )
                }
            }
        }

        if (enabled) {
            Text(
                text = "Isi semua data dengan benar!",
                modifier = Modifier.padding(12.dp)
            )
        }
        Divider(
            thickness = 8.dp,
            modifier = Modifier.padding(12.dp)
        )
    }
}