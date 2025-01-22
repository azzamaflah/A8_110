package com.example.uaspam.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class  Kamar (
    @SerialName("id_kamar")
    val idKamar : String,
    @SerialName("nomor_kamar")
    val nomorKamar: String,
    @SerialName("id_bangunan")
    val idBangunan: String,
    val kapasitas : String,
    @SerialName("status_kamar")
    val statusKamar: String,
)