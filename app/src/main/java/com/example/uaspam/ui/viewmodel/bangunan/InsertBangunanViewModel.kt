package com.example.uaspam.ui.viewmodel.bangunan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uaspam.model.Bangunan
import com.example.uaspam.repository.BangunanRepository
import kotlinx.coroutines.launch

class InsertBangunanViewModel(private val bangunanRepository: BangunanRepository) : ViewModel() {
    var uiState by mutableStateOf(InsertBangunanUiState())
        private set

    fun updateInsertBangunanState(insertUiEvent: InsertBangunanUiEvent) {
        uiState = InsertBangunanUiState(insertUiEvent = insertUiEvent)
    }

    fun insertBangunan() {
        viewModelScope.launch {
            try {
                bangunanRepository.insertBangunan(uiState.insertUiEvent.toBangunan())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

// UI State untuk Insert Bangunan
data class InsertBangunanUiState(
    val insertUiEvent: InsertBangunanUiEvent = InsertBangunanUiEvent()
)

// UI Event untuk menangani input data
data class InsertBangunanUiEvent(
    val idBangunan: String = "",
    val namaBangunan: String = "",
    val jumlahLantai: String = "",
    val alamat: String = ""
)

// Konversi InsertUiEvent ke model Bangunan
fun InsertBangunanUiEvent.toBangunan(): Bangunan = Bangunan(
    idBangunan = idBangunan,
    namaBangunan = namaBangunan,
    jumlahLantai = jumlahLantai,
    alamat = alamat
)

// Konversi model Bangunan ke UiState
fun Bangunan.toUiStateBangunan(): InsertBangunanUiState = InsertBangunanUiState(
    insertUiEvent = toInsertUiEvent()
)

// Konversi model Bangunan ke InsertUiEvent
fun Bangunan.toInsertUiEvent(): InsertBangunanUiEvent = InsertBangunanUiEvent(
    idBangunan = idBangunan,
    namaBangunan = namaBangunan,
    jumlahLantai = jumlahLantai,
    alamat = alamat
)