package com.example.uaspam.ui.viewmodel.mahasiswa

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uaspam.model.Mahasiswa
import com.example.uaspam.repository.MahasiswaRepository
import kotlinx.coroutines.launch

class InsertMahasiswaViewModel(private val mahasiswaRepository: MahasiswaRepository) : ViewModel() {
    var uiState by mutableStateOf(InsertMahasiswaUiState())
        private set

    fun updateInsertMahasiswaState(insertUiEvent: InsertMahasiswaUiEvent) {
        uiState = InsertMahasiswaUiState(insertUiEvent = insertUiEvent)
    }

    fun insertMahasiswa() {
        viewModelScope.launch {
            try {
                mahasiswaRepository.insertMahasiswa(uiState.insertUiEvent.toMahasiswa())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

// UI State untuk Insert Mahasiswa
data class InsertMahasiswaUiState(
    val insertUiEvent: InsertMahasiswaUiEvent = InsertMahasiswaUiEvent()
)

// UI Event untuk menangani input data
data class InsertMahasiswaUiEvent(
    val idMahasiswa: String = "",
    val namaMahasiswa: String = "",
    val nomorIdentitas: String = "",
    val email: String = "",
    val nomorTelepon: String = "",
    val idKamar: String = ""
)

// Konversi InsertUiEvent ke model Mahasiswa
fun InsertMahasiswaUiEvent.toMahasiswa(): Mahasiswa = Mahasiswa(
    idMahasiswa = idMahasiswa,
    namaMahasiswa = namaMahasiswa,
    nomorIdentitas = nomorIdentitas,
    email = email,
    nomorTelepon = nomorTelepon,
    idKamar = idKamar
)

// Konversi model Mahasiswa ke UiState
fun Mahasiswa.toUiStateMahasiswa(): InsertMahasiswaUiState = InsertMahasiswaUiState(
    insertUiEvent = toInsertUiEvent()
)

// Konversi model Mahasiswa ke InsertUiEvent
fun Mahasiswa.toInsertUiEvent(): InsertMahasiswaUiEvent = InsertMahasiswaUiEvent(
    idMahasiswa = idMahasiswa,
    namaMahasiswa = namaMahasiswa,
    nomorIdentitas = nomorIdentitas,
    email = email,
    nomorTelepon = nomorTelepon,
    idKamar = idKamar
)