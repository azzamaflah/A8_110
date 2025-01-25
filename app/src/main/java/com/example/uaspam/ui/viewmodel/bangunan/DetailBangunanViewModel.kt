package com.example.uaspam.ui.viewmodel.bangunan
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uaspam.model.Bangunan
import com.example.uaspam.repository.BangunanRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class DetailBangunanUiState {
    data class Success(val bangunan: Bangunan) : DetailBangunanUiState()
    object Error : DetailBangunanUiState()
    object Loading : DetailBangunanUiState()
}

class DetailBangunanViewModel(private val repository: BangunanRepository) : ViewModel() {
    var detailBangunanUiState: DetailBangunanUiState by mutableStateOf(DetailBangunanUiState.Loading)
        private set

    fun getDetailBangunan(idBangunan: String) {
        viewModelScope.launch {
            detailBangunanUiState = DetailBangunanUiState.Loading
            detailBangunanUiState = try {
                val bangunan = repository.getBangunanById(idBangunan)
                DetailBangunanUiState.Success(bangunan)
            } catch (e: IOException) {
                DetailBangunanUiState.Error
            } catch (e: HttpException) {
                DetailBangunanUiState.Error
            }
        }
    }

    fun deleteBangunan(idBangunan: String) {
        viewModelScope.launch {
            try {
                repository.deleteBangunan(idBangunan)
            } catch (e: IOException) {
                detailBangunanUiState = DetailBangunanUiState.Error
            } catch (e: HttpException) {
                detailBangunanUiState = DetailBangunanUiState.Error
            }
        }
    }
}

