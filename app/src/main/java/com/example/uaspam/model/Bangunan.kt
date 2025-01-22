package com.example.uaspam.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Bangunan (
    @SerialName("id_bangunan")
    val idBangunan: String,
    @SerialName("nama_bangunan")
    val namaBangunan: String,
    @SerialName("jumlah_lantai")
    val jumlahLantai: String,
    val alamat: String,
)