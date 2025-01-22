package com.example.uaspam.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Mahasiswa (
    @SerialName("id_mahasiswa")
    val idMahasiswa: String,
    @SerialName("nama_mahasiswa")
    val namaMahasiswa: String,
    @SerialName("nomor_identitas")
    val nomorIdentitas: String,
    val email: String,
    @SerialName("nomor_telepon")
    val nomorTelepon: String,
    @SerialName("id_kamar")
    val idKamar: String
)