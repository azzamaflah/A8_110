package com.example.uaspam.ui.view.pembayaransewa
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uaspam.model.Mahasiswa
import com.example.uaspam.ui.costumewidget.CustomTopAppBar
import com.example.uaspam.ui.navigation.DestinasiNavigasi
import com.example.uaspam.ui.viewmodel.PenyediaViewModel
import com.example.uaspam.ui.viewmodel.pembayaransewa.InsertPembayaranSewaUiEvent
import com.example.uaspam.ui.viewmodel.pembayaransewa.InsertPembayaranSewaUiState
import com.example.uaspam.ui.viewmodel.pembayaransewa.InsertPembayaranSewaViewModel
import kotlinx.coroutines.launch

object DestinasiEntryPembayaran : DestinasiNavigasi {
    override val route = "entry_pembayaran"
    override val titleRes = "Entry Pembayaran"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertPembayaranSewaView(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertPembayaranSewaViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val mahasiswaList = viewModel.mahasiswaList

    LaunchedEffect(Unit) {
        viewModel.fetchMahasiswa()
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                title = DestinasiEntryPembayaran.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        EntryPembayaranBody(
            insertUiState = viewModel.uiState,
            mahasiswaList = mahasiswaList,
            onPembayaranValueChange = viewModel::updateInsertPembayaranState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.insertPembayaran()
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
fun EntryPembayaranBody(
    insertUiState: InsertPembayaranSewaUiState,
    mahasiswaList: List<Mahasiswa>,
    onPembayaranValueChange: (InsertPembayaranSewaUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FormPembayaranInput(
            insertUiEvent = insertUiState.insertUiEvent,
            mahasiswaList = mahasiswaList,
            onValueChange = onPembayaranValueChange,
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
fun FormPembayaranInput(
    insertUiEvent: InsertPembayaranSewaUiEvent,
    mahasiswaList: List<Mahasiswa>,
    modifier: Modifier = Modifier,
    onValueChange: (InsertPembayaranSewaUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    val statusPembayaranOptions = listOf("Lunas", "Gagal")
    var expandedMahasiswa by remember { mutableStateOf(false) }
    var selectedMahasiswa by remember { mutableStateOf<Mahasiswa?>(null) }
    var expandedStatus by remember { mutableStateOf(false) }
    val (selectedStatus, onStatusSelected) = remember { mutableStateOf(insertUiEvent.statusPembayaran) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = insertUiEvent.idPembayaran,
            onValueChange = { onValueChange(insertUiEvent.copy(idPembayaran = it)) },
            label = { Text("ID Pembayaran") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        // Dropdown Menu for ID Mahasiswa
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedMahasiswa?.namaMahasiswa ?: "Pilih Mahasiswa",
                onValueChange = {},
                label = { Text("ID Mahasiswa") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedMahasiswa = true },
                enabled = false,
                trailingIcon = {
                    IconButton(onClick = { expandedMahasiswa = !expandedMahasiswa }) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown"
                        )
                    }
                },
                singleLine = true
            )

            DropdownMenu(
                expanded = expandedMahasiswa,
                onDismissRequest = { expandedMahasiswa = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                mahasiswaList.forEach { mahasiswa ->
                    DropdownMenuItem(
                        text = { Text(mahasiswa.namaMahasiswa) },
                        onClick = {
                            selectedMahasiswa = mahasiswa
                            onValueChange(insertUiEvent.copy(idMahasiswa = mahasiswa.idMahasiswa))
                            expandedMahasiswa = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = insertUiEvent.jumlah,
            onValueChange = { onValueChange(insertUiEvent.copy(jumlah = it)) },
            label = { Text("Jumlah") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertUiEvent.tanggalPembayaran,
            onValueChange = { onValueChange(insertUiEvent.copy(tanggalPembayaran = it)) },
            label = { Text("Tanggal Pembayaran") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            placeholder = { Text("yyyy-MM-dd") }
        )

        // Dropdown Menu for Status Pembayaran
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedStatus,
                onValueChange = {},
                label = { Text("Status Pembayaran") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                trailingIcon = {
                    IconButton(onClick = { expandedStatus = !expandedStatus }) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown"
                        )
                    }
                },
                singleLine = true
            )

            DropdownMenu(
                expanded = expandedStatus,
                onDismissRequest = { expandedStatus = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                statusPembayaranOptions.forEach { status ->
                    DropdownMenuItem(
                        text = { Text(status) },
                        onClick = {
                            onStatusSelected(status)
                            onValueChange(insertUiEvent.copy(statusPembayaran = status))
                            expandedStatus = false
                        }
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

