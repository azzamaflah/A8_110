package com.example.uaspam.ui.viewmodel.kamar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.uaspam.model.Kamar
import com.example.uaspam.repository.KamarRepository
import kotlinx.coroutines.launch
import java.io.IOException

sealed class HomeKamarUiState {
    data class Success(val kamar: List<Kamar>) : HomeKamarUiState()
    object Error : HomeKamarUiState()
    object Loading : HomeKamarUiState()
}

class HomeKamarViewModel(private val kmr: KamarRepository) : ViewModel() {
    var kamarUiState: HomeKamarUiState by mutableStateOf(HomeKamarUiState.Loading)
        private set



    fun getKamar() {
        viewModelScope.launch {
            kamarUiState = HomeKamarUiState.Loading
            kamarUiState = try {
                HomeKamarUiState.Success(kmr.getKamar())
            } catch (e: IOException) {
                HomeKamarUiState.Error
            } catch (e: HttpException) {
                HomeKamarUiState.Error
            }
        }
    }

    fun deleteKamar(idKamar: String) {
        viewModelScope.launch {
            try {
                kmr.deleteKamar(idKamar)
                getKamar() // Refresh data after deletion
            } catch (e: IOException) {
                kamarUiState = HomeKamarUiState.Error
            } catch (e: HttpException) {
                kamarUiState = HomeKamarUiState.Error
            }
        }
    }
}