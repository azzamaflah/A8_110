package com.example.uaspam.ui.viewmodel.pembayaransewa
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uaspam.model.Mahasiswa
import com.example.uaspam.model.PembayaraanSewa
import com.example.uaspam.repository.MahasiswaRepository
import com.example.uaspam.repository.PembayaraanSewaRepository
import kotlinx.coroutines.launch

class EditPembayaranSewaViewModel(
    private val pembayaranRepository: PembayaraanSewaRepository,
    private val mahasiswaRepository: MahasiswaRepository
) : ViewModel() {

    var uiState by mutableStateOf(EditPembayaranSewaUiState())
        private set

    var mahasiswaList by mutableStateOf<List<Mahasiswa>>(emptyList())
        private set

    fun updateEditPembayaranSewaState(editUiEvent: EditPembayaranSewaUiEvent) {
        uiState = EditPembayaranSewaUiState(editUiEvent = editUiEvent)
    }

    fun loadPembayaranSewaDetail(idPembayaran: String) {
        viewModelScope.launch {
            try {
                val pembayaranSewa = pembayaranRepository.getSewaById(idPembayaran)
                uiState = pembayaranSewa.toUiStatePembayaranSewa2()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadMahasiswaList() {
        viewModelScope.launch {
            try {
                mahasiswaList = mahasiswaRepository.getMahasiswa()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updatePembayaranSewa() {
        viewModelScope.launch {
            try {
                val pembayaranSewa = uiState.editUiEvent.toPembayaranSewa()
                pembayaranRepository.updateSewa(pembayaranSewa.idPembayaran, pembayaranSewa)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

// UI State
data class EditPembayaranSewaUiState(
    val editUiEvent: EditPembayaranSewaUiEvent = EditPembayaranSewaUiEvent()
)

data class EditPembayaranSewaUiEvent(
    val idPembayaran: String = "",
    val idMahasiswa: String = "",
    val jumlah: String = "",
    val tanggalPembayaran: String = "",
    val statusPembayaran: String = ""
)

// Konversi UI Event ke Model
fun EditPembayaranSewaUiEvent.toPembayaranSewa(): PembayaraanSewa = PembayaraanSewa(
    idPembayaran = idPembayaran,
    idMahasiswa = idMahasiswa,
    jumlah = jumlah,
    tanggalPembayaran = tanggalPembayaran,
    statusPembayaran = statusPembayaran
)

// Konversi Model ke UI State
fun PembayaraanSewa.toUiStatePembayaranSewa2(): EditPembayaranSewaUiState = EditPembayaranSewaUiState(
    editUiEvent = toEditPembayaranSewaUiEvent()
)

fun PembayaraanSewa.toEditPembayaranSewaUiEvent(): EditPembayaranSewaUiEvent = EditPembayaranSewaUiEvent(
    idPembayaran = idPembayaran,
    idMahasiswa = idMahasiswa,
    jumlah = jumlah,
    tanggalPembayaran = tanggalPembayaran,
    statusPembayaran = statusPembayaran
)
