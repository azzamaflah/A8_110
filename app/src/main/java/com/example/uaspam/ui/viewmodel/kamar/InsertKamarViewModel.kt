package com.example.uaspam.ui.viewmodel.kamar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uaspam.model.Bangunan
import com.example.uaspam.model.Kamar
import com.example.uaspam.repository.BangunanRepository
import com.example.uaspam.repository.KamarRepository
import kotlinx.coroutines.launch

class InsertKamarViewModel(
    private val kamarRepository: KamarRepository,
    private val bangunanRepository: BangunanRepository
) : ViewModel() {
    var uiState by mutableStateOf(InsertKamarUiState())
        private set

    var bangunanList by mutableStateOf<List<Bangunan>>(emptyList()) // Daftar bangunan untuk dropdown
        private set

    init {
        loadBangunanList() // Muat daftar bangunan saat ViewModel dibuat
    }

    private fun loadBangunanList() {
        viewModelScope.launch {
            try {
                bangunanList = bangunanRepository.getAllBangunan() // Ambil data bangunan dari repository
            } catch (e: Exception) {
                bangunanList = emptyList()
            }
        }
    }

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

// UI State untuk insert kamar
data class InsertKamarUiState(
    val insertUiEvent: InsertKamarUiEvent = InsertKamarUiEvent()
)

data class InsertKamarUiEvent(
    val idKamar: String = "",
    val nomorKamar: String = "",
    val idBangun: String = "", // Hubungkan ke ID bangunan
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