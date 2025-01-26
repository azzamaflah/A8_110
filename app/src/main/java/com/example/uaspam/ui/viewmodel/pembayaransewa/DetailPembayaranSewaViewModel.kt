package com.example.uaspam.ui.viewmodel.pembayaransewa
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uaspam.model.PembayaraanSewa
import com.example.uaspam.repository.PembayaraanSewaRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// UI State untuk Detail Pembayaran Sewa
sealed class DetailPembayaranSewaUiState {
    data class Success(val pembayaranSewa: PembayaraanSewa) : DetailPembayaranSewaUiState()
    object Error : DetailPembayaranSewaUiState()
    object Loading : DetailPembayaranSewaUiState()
}

// ViewModel untuk Detail Pembayaran Sewa
class DetailPembayaranSewaViewModel(private val repository: PembayaraanSewaRepository) : ViewModel() {
    var detailPembayaranSewaUiState: DetailPembayaranSewaUiState by mutableStateOf(DetailPembayaranSewaUiState.Loading)
        private set

    // Fungsi untuk mendapatkan detail pembayaran berdasarkan ID
    fun getDetailPembayaran(idPembayaran: String) {
        viewModelScope.launch {
            detailPembayaranSewaUiState = DetailPembayaranSewaUiState.Loading
            detailPembayaranSewaUiState = try {
                val pembayaran = repository.getSewaById(idPembayaran)
                DetailPembayaranSewaUiState.Success(pembayaran)
            } catch (e: IOException) {
                DetailPembayaranSewaUiState.Error
            } catch (e: HttpException) {
                DetailPembayaranSewaUiState.Error
            }
        }
    }

    // Fungsi untuk menghapus pembayaran berdasarkan ID
    fun deletePembayaran(idPembayaran: String) {
        viewModelScope.launch {
            try {
                repository.deleteSewa(idPembayaran)
            } catch (e: IOException) {
                detailPembayaranSewaUiState = DetailPembayaranSewaUiState.Error
            } catch (e: HttpException) {
                detailPembayaranSewaUiState = DetailPembayaranSewaUiState.Error
            }
        }
    }
}
