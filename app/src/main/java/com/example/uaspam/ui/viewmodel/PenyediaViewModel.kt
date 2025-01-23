package com.example.uaspam.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.uaspam.RunApp
import com.example.uaspam.ui.viewmodel.bangunan.HomeBangunanViewModel
import com.example.uaspam.ui.viewmodel.kamar.HomeKamarViewModel
import com.example.uaspam.ui.viewmodel.mahasiswa.HomeViewModel
import com.example.uaspam.ui.viewmodel.pembayaran.HomePembayaranSewaViewModel


object PenyediaViewModel {
    val Factory = viewModelFactory {
        // Initializer untuk HomeKamarViewModel
        initializer {
            HomeKamarViewModel(
                RunApp().appsContainer.kamarRepository
            )
        }

        // Initializer untuk HomeBangunanViewModel
        initializer {
            HomeBangunanViewModel(
                RunApp().appsContainer.bangunanRepository
            )
        }

        // Initializer untuk HomePembayaranSewaViewModel
        initializer {
            HomePembayaranSewaViewModel(
                RunApp().appsContainer.pembayaraanSewa
            )
        }

        // Initializer untuk HomeViewModel (Mahasiswa)
        initializer {
            HomeViewModel(
                RunApp().appsContainer.mahasiswaRepository
            )
        }
    }
}

// Extension function untuk mengakses instance RunApp
fun CreationExtras.RunApp(): RunApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RunApp)