package com.example.uaspam.ui.viewmodel.bangunan
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uaspam.model.Bangunan
import com.example.uaspam.repository.BangunanRepository
import kotlinx.coroutines.launch

class EditBangunanViewModel(private val bangunanRepository: BangunanRepository) : ViewModel() {

    var uiState by mutableStateOf(EditBangunanUiState())
        private set

    fun updateEditBangunanState(editUiEvent: EditBangunanUiEvent) {
        uiState = EditBangunanUiState(editUiEvent = editUiEvent)
    }

    fun loadBangunanDetail(idBangunan: String) {
        viewModelScope.launch {
            try {
                val bangunan = bangunanRepository.getBangunanById(idBangunan)
                uiState = bangunan.toUiStateBangunan2()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateBangunan() {
        viewModelScope.launch {
            try {
                val bangunan = uiState.editUiEvent.toBangunan()
                bangunanRepository.updateBangunan(bangunan.idBangunan, bangunan)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

data class EditBangunanUiState(
    val editUiEvent: EditBangunanUiEvent = EditBangunanUiEvent()
)

data class EditBangunanUiEvent(
    val idBangunan: String = "",
    val namaBangunan: String = "",
    val jumlahLantai: String = "",
    val alamat: String = ""
)

fun EditBangunanUiEvent.toBangunan(): Bangunan = Bangunan(
    idBangunan = idBangunan,
    namaBangunan = namaBangunan,
    jumlahLantai = jumlahLantai,
    alamat = alamat
)

fun Bangunan.toUiStateBangunan2(): EditBangunanUiState = EditBangunanUiState(
    editUiEvent = toEditUiEvent()
)

fun Bangunan.toEditUiEvent(): EditBangunanUiEvent = EditBangunanUiEvent(
    idBangunan = idBangunan,
    namaBangunan = namaBangunan,
    jumlahLantai = jumlahLantai,
    alamat = alamat
)
