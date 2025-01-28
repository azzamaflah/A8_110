package com.example.uaspam.ui.view.kamar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.uaspam.model.Bangunan
import com.example.uaspam.ui.costumewidget.CustomTopAppBar
import com.example.uaspam.ui.navigation.DestinasiNavigasi
import com.example.uaspam.ui.viewmodel.PenyediaViewModel
import com.example.uaspam.ui.viewmodel.kamar.InsertKamarUiEvent
import com.example.uaspam.ui.viewmodel.kamar.InsertKamarUiState
import com.example.uaspam.ui.viewmodel.kamar.InsertKamarViewModel
import kotlinx.coroutines.launch

object DestinasiEntryKamar : DestinasiNavigasi {
    override val route = "entry_kamar"
    override val titleRes = "Entry Kamar"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertKamarView(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertKamarViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val bangunanList = viewModel.bangunanList

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                title = DestinasiEntryKamar.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        EntryKamarBody(
            insertUiState = viewModel.uiState,
            bangunanList = bangunanList,
            onKamarValueChange = viewModel::updateInsertKamarState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.insertKamar()
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
fun EntryKamarBody(
    insertUiState: InsertKamarUiState,
    bangunanList: List<Bangunan>,
    onKamarValueChange: (InsertKamarUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FormKamarInput(
            insertUiEvent = insertUiState.insertUiEvent,
            bangunanList = bangunanList,
            onValueChange = onKamarValueChange,
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
fun FormKamarInput(
    insertUiEvent: InsertKamarUiEvent,
    bangunanList: List<Bangunan>,
    modifier: Modifier = Modifier,
    onValueChange: (InsertKamarUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedBangunan by remember { mutableStateOf<Bangunan?>(null) }
    var expandedStatus by remember { mutableStateOf(false) }
    val statusOptions = listOf("TERSEDIA", "TIDAK TERSEDIA", "TERBOKING")
    var selectedStatus by remember { mutableStateOf(insertUiEvent.statusKamar) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = insertUiEvent.nomorKamar,
            onValueChange = { onValueChange(insertUiEvent.copy(nomorKamar = it)) },
            label = { Text("Nomor Kamar") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        Box {
            OutlinedTextField(
                value = selectedBangunan?.namaBangunan ?: "Pilih Bangunan",
                onValueChange = {},
                label = { Text("ID Bangunan") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                enabled = false,
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
                            onValueChange(insertUiEvent.copy(idBangun = bangunan.idBangunan))
                            expanded = false
                        },
                        text = { Text(bangunan.namaBangunan) }
                    )
                }
            }
        }

        OutlinedTextField(
            value = insertUiEvent.kapasitas,
            onValueChange = { onValueChange(insertUiEvent.copy(kapasitas = it)) },
            label = { Text("Kapasitas") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        // Status Kamar Dropdown
        Box {
            OutlinedTextField(
                value = selectedStatus.ifEmpty { "Pilih Status Kamar" },
                onValueChange = {},
                label = { Text("Status Kamar") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedStatus = true },
                enabled = false,
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
                            onValueChange(insertUiEvent.copy(statusKamar = status))  // Update statusKamar
                            expandedStatus = false
                        },
                        text = { Text(status) }
                    )
                }
            }
        }
    }
}