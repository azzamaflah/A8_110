package com.example.uaspam.ui.view.bangunan
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.uaspam.ui.costumewidget.CustomTopAppBar
import com.example.uaspam.ui.navigation.DestinasiNavigasi
import com.example.uaspam.ui.viewmodel.PenyediaViewModel
import com.example.uaspam.ui.viewmodel.bangunan.InsertBangunanViewModel
import com.example.uaspam.ui.viewmodel.bangunan.InsertBangunanUiEvent
import com.example.uaspam.ui.viewmodel.bangunan.InsertBangunanUiState
import kotlinx.coroutines.launch

object DestinasiEntryBangunan : DestinasiNavigasi {
    override val route = "entry_bangunan"
    override val titleRes = "Entry Bangunan"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertBangunanView(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertBangunanViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CustomTopAppBar(
                title = DestinasiEntryBangunan.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        EntryBangunanBody(
            insertUiState = viewModel.uiState,
            onBangunanValueChange = viewModel::updateInsertBangunanState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.insertBangunan()
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
fun EntryBangunanBody(
    insertUiState: InsertBangunanUiState,
    onBangunanValueChange: (InsertBangunanUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FormBangunanInput(
            insertUiEvent = insertUiState.insertUiEvent,
            onValueChange = onBangunanValueChange,
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
fun FormBangunanInput(
    insertUiEvent: InsertBangunanUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (InsertBangunanUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = insertUiEvent.namaBangunan,
            onValueChange = { onValueChange(insertUiEvent.copy(namaBangunan = it)) },
            label = { Text("Nama Bangunan") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertUiEvent.idBangunan,
            onValueChange = { onValueChange(insertUiEvent.copy(idBangunan = it)) },
            label = { Text("ID Bangunan") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertUiEvent.jumlahLantai,
            onValueChange = { onValueChange(insertUiEvent.copy(jumlahLantai = it)) },
            label = { Text("Jumlah Lantai") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertUiEvent.alamat,
            onValueChange = { onValueChange(insertUiEvent.copy(alamat = it)) },
            label = { Text("Alamat") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

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