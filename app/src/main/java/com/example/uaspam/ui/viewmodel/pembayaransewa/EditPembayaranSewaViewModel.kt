package com.example.uaspam.ui.viewmodel.pembayaransewa
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uaspam.model.PembayaraanSewa
import com.example.uaspam.repository.PembayaraanSewaRepository
import kotlinx.coroutines.launch

class EditPembayaranSewaViewModel(private val repository: PembayaraanSewaRepository) : ViewModel() {

    var uiState by mutableStateOf(EditPembayaranSewaUiState())
        private set

    fun updateEditPembayaranSewaState(editUiEvent: EditPembayaranSewaUiEvent) {
        uiState = EditPembayaranSewaUiState(editUiEvent = editUiEvent)
    }

    fun loadPembayaranSewaDetail(idPembayaran: String) {
        viewModelScope.launch {
            try {
                val pembayaranSewa = repository.getSewaById(idPembayaran)
                uiState = pembayaranSewa.toUiStatePembayaranSewa2()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updatePembayaranSewa() {
        viewModelScope.launch {
            try {
                val pembayaranSewa = uiState.editUiEvent.toPembayaranSewa()
                repository.updateSewa(pembayaranSewa.idPembayaran, pembayaranSewa)
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

fun EditPembayaranSewaUiEvent.toPembayaranSewa(): PembayaraanSewa = PembayaraanSewa(
    idPembayaran = idPembayaran,
    idMahasiswa = idMahasiswa,
    jumlah = jumlah,
    tanggalPembayaran = tanggalPembayaran,
    statusPembayaran = statusPembayaran
)

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
