package com.example.uaspam.ui.viewmodel.kamar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uaspam.model.Kamar
import com.example.uaspam.repository.KamarRepository
import kotlinx.coroutines.launch

class EditKamarViewModel(private val repository: KamarRepository) : ViewModel() {

    var uiState by mutableStateOf(EditKamarUiState())
        private set

    fun updateEditKamarState(editUiEvent: EditKamarUiEvent) {
        uiState = EditKamarUiState(editUiEvent = editUiEvent)
    }

    fun loadKamarDetail(idKamar: String) {
        viewModelScope.launch {
            try {
                val kamar = repository.getKamarById(idKamar)
                uiState = kamar.toUiStateKamar2()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateKamar() {
        viewModelScope.launch {
            try {
                val kamar = uiState.editUiEvent.toKamar()
                repository.updateKamar(kamar.idKamar, kamar)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

// UI State
data class EditKamarUiState(
    val editUiEvent: EditKamarUiEvent = EditKamarUiEvent()
)

data class EditKamarUiEvent(
    val idKamar: String = "",
    val nomorKamar: String = "",
    val idBangunan: String = "",
    val kapasitas: String = "",
    val statusKamar: String = ""
)

fun EditKamarUiEvent.toKamar(): Kamar = Kamar(
    idKamar = idKamar,
    nomorKamar = nomorKamar,
    idBangunan = idBangunan,
    kapasitas = kapasitas,
    statusKamar = statusKamar
)

fun Kamar.toUiStateKamar2(): EditKamarUiState = EditKamarUiState(
    editUiEvent = toEditKamarUiEvent()
)

fun Kamar.toEditKamarUiEvent(): EditKamarUiEvent = EditKamarUiEvent(
    idKamar = idKamar,
    nomorKamar = nomorKamar,
    idBangunan = idBangunan,
    kapasitas = kapasitas,
    statusKamar = statusKamar
)
