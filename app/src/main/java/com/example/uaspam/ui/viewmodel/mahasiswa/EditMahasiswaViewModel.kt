package com.example.uaspam.ui.viewmodel.mahasiswa
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uaspam.model.Kamar
import com.example.uaspam.model.Mahasiswa
import com.example.uaspam.repository.KamarRepository
import com.example.uaspam.repository.MahasiswaRepository
import kotlinx.coroutines.launch

class EditMahasiswaViewModel(
    private val mahasiswaRepository: MahasiswaRepository,
    private val kamarRepository: KamarRepository
) : ViewModel() {

    var uiState by mutableStateOf(EditMahasiswaUiState())
        private set

    var kamarList by mutableStateOf(listOf<Kamar>())
        private set

    fun updateEditMahasiswaState(editUiEvent: EditMahasiswaUiEvent) {
        uiState = EditMahasiswaUiState(editUiEvent = editUiEvent)
    }

    // Load detail mahasiswa
    fun loadMahasiswaDetail(idMahasiswa: String) {
        viewModelScope.launch {
            try {
                val mahasiswa = mahasiswaRepository.getMahasiswaById(idMahasiswa)
                uiState = mahasiswa.toUiStateMahasiswa2()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Load daftar kamar
    fun loadKamarList() {
        viewModelScope.launch {
            try {
                kamarList = kamarRepository.getKamar()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Update data mahasiswa
    fun updateMahasiswa() {
        viewModelScope.launch {
            try {
                val mahasiswa = uiState.editUiEvent.toMahasiswa()
                mahasiswaRepository.updateMahasiswa(mahasiswa.idMahasiswa, mahasiswa)
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

// Konversi UI Event ke Model Mahasiswa
fun EditMahasiswaUiEvent.toMahasiswa(): Mahasiswa = Mahasiswa(
    idMahasiswa = idMahasiswa,
    namaMahasiswa = namaMahasiswa,
    nomorIdentitas = nomorIdentitas,
    email = email,
    nomorTelepon = nomorTelepon,
    idKamar = idKamar
)

// Konversi Model Mahasiswa ke UI State
fun Mahasiswa.toUiStateMahasiswa2(): EditMahasiswaUiState = EditMahasiswaUiState(
    editUiEvent = toEditMahasiswaUiEvent()
)

// Konversi Model Mahasiswa ke UI Event
fun Mahasiswa.toEditMahasiswaUiEvent(): EditMahasiswaUiEvent = EditMahasiswaUiEvent(
    idMahasiswa = idMahasiswa,
    namaMahasiswa = namaMahasiswa,
    nomorIdentitas = nomorIdentitas,
    email = email,
    nomorTelepon = nomorTelepon,
    idKamar = idKamar
)
