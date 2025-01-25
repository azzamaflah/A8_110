package com.example.uaspam.ui.viewmodel.kamar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uaspam.model.Kamar
import com.example.uaspam.repository.KamarRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// UI State untuk Detail Kamar
sealed class DetailKamarUiState {
    data class Success(val kamar: Kamar) : DetailKamarUiState()
    object Error : DetailKamarUiState()
    object Loading : DetailKamarUiState()
}

// ViewModel untuk Detail Kamar
class DetailKamarViewModel(private val repository: KamarRepository) : ViewModel() {
    var detailKamarUiState: DetailKamarUiState by mutableStateOf(DetailKamarUiState.Loading)
        private set

    // Fungsi untuk mendapatkan detail kamar berdasarkan ID
    fun getDetailKamar(idKamar: String) {
        viewModelScope.launch {
            detailKamarUiState = DetailKamarUiState.Loading
            detailKamarUiState = try {
                val kamar = repository.getKamarById(idKamar)
                DetailKamarUiState.Success(kamar)
            } catch (e: IOException) {
                DetailKamarUiState.Error
            } catch (e: HttpException) {
                DetailKamarUiState.Error
            }
        }
    }

    // Fungsi untuk menghapus kamar berdasarkan ID
    fun deleteKamar(idKamar: String) {
        viewModelScope.launch {
            try {
                repository.deleteKamar(idKamar)
            } catch (e: IOException) {
                detailKamarUiState = DetailKamarUiState.Error
            } catch (e: HttpException) {
                detailKamarUiState = DetailKamarUiState.Error
            }
        }
    }
}
