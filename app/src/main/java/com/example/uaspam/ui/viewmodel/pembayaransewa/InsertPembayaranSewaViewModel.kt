package com.example.uaspam.ui.viewmodel.pembayaransewa
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uaspam.model.PembayaraanSewa
import com.example.uaspam.repository.PembayaraanSewaRepository
import kotlinx.coroutines.launch

class InsertPembayaranSewaViewModel(private val pembayaranRepository: PembayaraanSewaRepository) : ViewModel() {
    var uiState by mutableStateOf(InsertPembayaranSewaUiState())
        private set

    fun updateInsertPembayaranState(insertUiEvent: InsertPembayaranSewaUiEvent) {
        uiState = InsertPembayaranSewaUiState(insertUiEvent = insertUiEvent)
    }

    fun insertPembayaran() {
        viewModelScope.launch {
            try {
                pembayaranRepository.insertSewa(uiState.insertUiEvent.toPembayaranSewa())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

// UI State untuk Insert Pembayaran Sewa
data class InsertPembayaranSewaUiState(
    val insertUiEvent: InsertPembayaranSewaUiEvent = InsertPembayaranSewaUiEvent()
)

// UI Event untuk menangani input data
data class InsertPembayaranSewaUiEvent(
    val idPembayaran: String = "",
    val idMahasiswa: String = "",
    val jumlah: String = "",
    val tanggalPembayaran: String = "",
    val statusPembayaran: String = ""
)

// Konversi InsertUiEvent ke model PembayaranSewa
fun InsertPembayaranSewaUiEvent.toPembayaranSewa(): PembayaraanSewa = PembayaraanSewa(
    idPembayaran = idPembayaran,
    idMahasiswa = idMahasiswa,
    jumlah = jumlah,
    tanggalPembayaran = tanggalPembayaran,
    statusPembayaran = statusPembayaran
)

// Konversi model PembayaranSewa ke UiState
fun PembayaraanSewa.toUiStatePembayaranSewa(): InsertPembayaranSewaUiState = InsertPembayaranSewaUiState(
    insertUiEvent = toInsertUiEvent()
)

// Konversi model PembayaranSewa ke InsertUiEvent
fun PembayaraanSewa.toInsertUiEvent(): InsertPembayaranSewaUiEvent = InsertPembayaranSewaUiEvent(
    idPembayaran = idPembayaran,
    idMahasiswa = idMahasiswa,
    jumlah = jumlah,
    tanggalPembayaran = tanggalPembayaran,
    statusPembayaran = statusPembayaran
)
