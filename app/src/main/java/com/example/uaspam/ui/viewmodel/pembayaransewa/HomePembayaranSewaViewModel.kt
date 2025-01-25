package com.example.uaspam.ui.viewmodel.pembayaran
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

sealed class HomePembayaranSewaUiState {
    data class Success(val pembayaranSewa: List<PembayaraanSewa>) : HomePembayaranSewaUiState()
    object Error : HomePembayaranSewaUiState()
    object Loading : HomePembayaranSewaUiState()
}

class HomePembayaranSewaViewModel(private val pmbyrn: PembayaraanSewaRepository) : ViewModel() {
    var pembayaranSewaUiState: HomePembayaranSewaUiState by mutableStateOf(HomePembayaranSewaUiState.Loading)
        private set

    init {
        getPembayaranSewa()
    }

    fun getPembayaranSewa() {
        viewModelScope.launch {
            pembayaranSewaUiState = HomePembayaranSewaUiState.Loading
            pembayaranSewaUiState = try {
                HomePembayaranSewaUiState.Success(pmbyrn.getSewa())
            } catch (e: IOException) {
                HomePembayaranSewaUiState.Error
            } catch (e: HttpException) {
                HomePembayaranSewaUiState.Error
            }
        }
    }

    fun deletePembayaranSewa(idPembayaran: String) {
        viewModelScope.launch {
            try {
                pmbyrn.deleteSewa(idPembayaran)
                getPembayaranSewa() // Refresh data
            } catch (e: IOException) {
                pembayaranSewaUiState = HomePembayaranSewaUiState.Error
            } catch (e: HttpException) {
                pembayaranSewaUiState = HomePembayaranSewaUiState.Error
            }
        }
    }
}
