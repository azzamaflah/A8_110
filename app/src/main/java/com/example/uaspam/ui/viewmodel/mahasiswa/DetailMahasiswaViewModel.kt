package com.example.uaspam.ui.viewmodel.mahasiswa
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uaspam.model.Mahasiswa
import com.example.uaspam.repository.MahasiswaRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// UI State untuk Detail Mahasiswa
sealed class DetailMahasiswaUiState {
    data class Success(val mahasiswa: Mahasiswa) : DetailMahasiswaUiState()
    object Error : DetailMahasiswaUiState()
    object Loading : DetailMahasiswaUiState()
}

// ViewModel untuk Detail Mahasiswa
class DetailMahasiswaViewModel(private val repository: MahasiswaRepository) : ViewModel() {
    var detailMahasiswaUiState: DetailMahasiswaUiState by mutableStateOf(DetailMahasiswaUiState.Loading)
        private set


    // Fungsi untuk mendapatkan detail mahasiswa berdasarkan ID Mahasiswa
    fun getDetailMahasiswa(idMahasiswa: String) {
        viewModelScope.launch {
            detailMahasiswaUiState = DetailMahasiswaUiState.Loading
            detailMahasiswaUiState = try {
                val mahasiswa = repository.getMahasiswaById(idMahasiswa)
                DetailMahasiswaUiState.Success(mahasiswa)
            } catch (e: IOException) {
                DetailMahasiswaUiState.Error
            } catch (e: HttpException) {
                DetailMahasiswaUiState.Error
            }
        }
    }

    // Fungsi untuk menghapus mahasiswa berdasarkan ID Mahasiswa
    fun deleteMahasiswa(idMahasiswa: String) {
        viewModelScope.launch {
            try {
                repository.deleteMahasiswa(idMahasiswa)
            } catch (e: IOException) {
                detailMahasiswaUiState = DetailMahasiswaUiState.Error
            } catch (e: HttpException) {
                detailMahasiswaUiState = DetailMahasiswaUiState.Error
            }
        }
    }
}
