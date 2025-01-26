package com.example.uaspam.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.uaspam.RunApp
import com.example.uaspam.ui.viewmodel.bangunan.DetailBangunanViewModel
import com.example.uaspam.ui.viewmodel.bangunan.EditBangunanViewModel
import com.example.uaspam.ui.viewmodel.bangunan.HomeBangunanViewModel
import com.example.uaspam.ui.viewmodel.bangunan.InsertBangunanViewModel
import com.example.uaspam.ui.viewmodel.kamar.DetailKamarViewModel
import com.example.uaspam.ui.viewmodel.kamar.EditKamarViewModel
import com.example.uaspam.ui.viewmodel.kamar.HomeKamarViewModel
import com.example.uaspam.ui.viewmodel.kamar.InsertKamarViewModel
import com.example.uaspam.ui.viewmodel.mahasiswa.DetailMahasiswaViewModel
import com.example.uaspam.ui.viewmodel.mahasiswa.EditMahasiswaViewModel
import com.example.uaspam.ui.viewmodel.mahasiswa.HomeViewModel
import com.example.uaspam.ui.viewmodel.mahasiswa.InsertMahasiswaViewModel
import com.example.uaspam.ui.viewmodel.pembayaran.HomePembayaranSewaViewModel
import com.example.uaspam.ui.viewmodel.pembayaransewa.DetailPembayaranSewaViewModel
import com.example.uaspam.ui.viewmodel.pembayaransewa.EditPembayaranSewaViewModel
import com.example.uaspam.ui.viewmodel.pembayaransewa.InsertPembayaranSewaViewModel


object PenyediaViewModel {
    val Factory = viewModelFactory {
        // Initializer untuk HomeKamarViewModel
        initializer {
            HomeKamarViewModel(
                RunApp().appsContainer.kamarRepository
            )
        }

        initializer {
            InsertKamarViewModel(RunApp().appsContainer.kamarRepository)
        }

        initializer {
            DetailKamarViewModel(
                RunApp().appsContainer.kamarRepository
            )
        }

        initializer {
            EditKamarViewModel(
                RunApp().appsContainer.kamarRepository
            )
        }

        // Initializer untuk HomeBangunanViewModel
        initializer {
            HomeBangunanViewModel(
                RunApp().appsContainer.bangunanRepository
            )
        }

        initializer {
            InsertBangunanViewModel(RunApp().appsContainer.bangunanRepository)
        }

        initializer {
            DetailBangunanViewModel(
                RunApp().appsContainer.bangunanRepository
            )
        }

        initializer {
            EditBangunanViewModel(
                RunApp().appsContainer.bangunanRepository
            )
        }

        // Initializer untuk HomePembayaranSewaViewModel
        initializer {
            HomePembayaranSewaViewModel(
                RunApp().appsContainer.pembayaraanSewa
            )
        }

        initializer {
            InsertPembayaranSewaViewModel(RunApp().appsContainer.pembayaraanSewa)
        }

        initializer {
            DetailPembayaranSewaViewModel(RunApp().appsContainer.pembayaraanSewa)
        }

        initializer {
            EditPembayaranSewaViewModel(RunApp().appsContainer.pembayaraanSewa)
        }

        // Initializer untuk HomeViewModel (Mahasiswa)
        initializer {
            HomeViewModel(
                RunApp().appsContainer.mahasiswaRepository
            )
        }

        initializer {
            InsertMahasiswaViewModel(RunApp().appsContainer.mahasiswaRepository)
        }

        initializer {
            DetailMahasiswaViewModel(RunApp().appsContainer.mahasiswaRepository)
        }

        initializer {
            EditMahasiswaViewModel(RunApp().appsContainer.mahasiswaRepository)
        }

    }
}

// Extension function untuk mengakses instance RunApp
fun CreationExtras.RunApp(): RunApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RunApp)