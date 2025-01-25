package com.example.uaspam.ui.viewmodel.kamar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uaspam.model.Kamar
import com.example.uaspam.repository.KamarRepository
import kotlinx.coroutines.launch
class InsertKamarViewModel(private val kamarRepository: KamarRepository) : ViewModel() {
    var uiState by mutableStateOf(InsertKamarUiState())
        private set

    fun updateInsertKamarState(insertUiEvent: InsertKamarUiEvent) {
        uiState = InsertKamarUiState(insertUiEvent = insertUiEvent)
    }

    fun insertKamar() {
        viewModelScope.launch {
            try {
                kamarRepository.insertKamar(uiState.insertUiEvent.toKamar())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

data class InsertKamarUiState(
    val insertUiEvent: InsertKamarUiEvent = InsertKamarUiEvent()
)

data class InsertKamarUiEvent(
    val idKamar: String = "",
    val nomorKamar: String = "",
    val idBangun: String = "",
    val kapasitas: String = "",
    val statusKamar: String = ""
)

// Konversi InsertKamarUiEvent ke Model Kamar
fun InsertKamarUiEvent.toKamar(): Kamar = Kamar(
    idKamar = idKamar,
    nomorKamar = nomorKamar,
    idBangunan = idBangun,
    kapasitas = kapasitas,
    statusKamar = statusKamar
)

// Konversi Model Kamar ke UiEvent
fun Kamar.toInsertUiEvent(): InsertKamarUiEvent = InsertKamarUiEvent(
    idKamar = idKamar,
    nomorKamar = nomorKamar,
    idBangun = idBangunan,
    kapasitas = kapasitas,
    statusKamar = statusKamar
)


fun Kamar.toUiStateKamar(): InsertKamarUiState = InsertKamarUiState(
    insertUiEvent = toInsertUiEvent()
)
