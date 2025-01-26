package com.example.uaspam.ui.viewmodel.mahasiswa
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uaspam.model.Mahasiswa
import com.example.uaspam.repository.MahasiswaRepository
import kotlinx.coroutines.launch

class EditMahasiswaViewModel(private val repository: MahasiswaRepository) : ViewModel() {

    var uiState by mutableStateOf(EditMahasiswaUiState())
        private set

    fun updateEditMahasiswaState(editUiEvent: EditMahasiswaUiEvent) {
        uiState = EditMahasiswaUiState(editUiEvent = editUiEvent)
    }

    fun loadMahasiswaDetail(idMahasiswa: String) {
        viewModelScope.launch {
            try {
                val mahasiswa = repository.getMahasiswaById(idMahasiswa)
                uiState = mahasiswa.toUiStateMahasiswa2()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateMahasiswa() {
        viewModelScope.launch {
            try {
                val mahasiswa = uiState.editUiEvent.toMahasiswa()
                repository.updateMahasiswa(mahasiswa.idMahasiswa, mahasiswa)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

// UI State
data class EditMahasiswaUiState(
    val editUiEvent: EditMahasiswaUiEvent = EditMahasiswaUiEvent()
)

data class EditMahasiswaUiEvent(
    val idMahasiswa: String = "",
    val namaMahasiswa: String = "",
    val nomorIdentitas: String = "",
    val email: String = "",
    val nomorTelepon: String = "",
    val idKamar: String = ""
)

fun EditMahasiswaUiEvent.toMahasiswa(): Mahasiswa = Mahasiswa(
    idMahasiswa = idMahasiswa,
    namaMahasiswa = namaMahasiswa,
    nomorIdentitas = nomorIdentitas,
    email = email,
    nomorTelepon = nomorTelepon,
    idKamar = idKamar
)

fun Mahasiswa.toUiStateMahasiswa2(): EditMahasiswaUiState = EditMahasiswaUiState(
    editUiEvent = toEditMahasiswaUiEvent()
)

fun Mahasiswa.toEditMahasiswaUiEvent(): EditMahasiswaUiEvent = EditMahasiswaUiEvent(
    idMahasiswa = idMahasiswa,
    namaMahasiswa = namaMahasiswa,
    nomorIdentitas = nomorIdentitas,
    email = email,
    nomorTelepon = nomorTelepon,
    idKamar = idKamar
)
