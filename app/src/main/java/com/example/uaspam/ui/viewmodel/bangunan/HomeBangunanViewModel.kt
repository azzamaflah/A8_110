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

sealed class HomeBangunanUiState {
    data class Success(val kamar: List<Bangunan>) : HomeBangunanUiState()
    object Error : HomeBangunanUiState()
    object Loading : HomeBangunanUiState()
}

class HomeBangunanViewModel(private val bangunanRepository: BangunanRepository) : ViewModel() {
    var bangunanUiState: HomeBangunanUiState by mutableStateOf(HomeBangunanUiState.Loading)
        private set

    init {
        getBangunan()
    }

    fun getBangunan() {
        viewModelScope.launch {
            bangunanUiState = HomeBangunanUiState.Loading
            bangunanUiState = try {
                HomeBangunanUiState.Success(bangunanRepository.getBangunan())
            } catch (e: IOException) {
                HomeBangunanUiState.Error
            } catch (e: HttpException) {
                HomeBangunanUiState.Error
            }
        }
    }

    fun deleteBangunan(idBangunan: String) {
        viewModelScope.launch {
            try {
                bangunanRepository.deleteBangunan(idBangunan)
                getBangunan() // Refresh data after deletion
            } catch (e: IOException) {
                bangunanUiState = HomeBangunanUiState.Error
            } catch (e: HttpException) {
                bangunanUiState = HomeBangunanUiState.Error
            }
        }
    }
}