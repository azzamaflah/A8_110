package com.example.uaspam.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class PembayaraanSewa (
    @SerialName("id_pembayaran")
    val idPembayaran: String,
    @SerialName("id_mahasiswa")
    val idMahasiswa: String,
    val jumlah: String,
    @SerialName("tanggal_pembayaran")
    val tanggalPembayaran: String,
    @SerialName("status_pembayaran")
    val statusPembayaran: String,
)