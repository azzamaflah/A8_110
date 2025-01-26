package com.example.uaspam.repository

import com.example.uaspam.model.Kamar
import com.example.uaspam.service.KamarService
import okio.IOException

interface KamarRepository {
    suspend fun getKamar(): List<Kamar>
    suspend fun insertKamar(kamar: Kamar)
    suspend fun updateKamar(idKamar: String, kamar: Kamar)
    suspend fun deleteKamar(idKamar: String)
    suspend fun getKamarById(idKamar: String): Kamar
}

class NetworkKamarRepository(
    private val kamarApiService: KamarService
) : KamarRepository {
    override suspend fun insertKamar(kamar: Kamar) {
        kamarApiService.insertKamar(kamar)
    }

    override suspend fun updateKamar(idKamar: String, kamar: Kamar) {
        kamarApiService.updateKamar(idKamar, kamar)
    }

    override suspend fun deleteKamar(idKamar: String) {
        try {
            val response = kamarApiService.deleteKamar(idKamar)
            if (!response.isSuccessful) {
                throw IOException("Failed to delete kamar. HTTP Status code: ${response.code()}")
            } else {
                response.message()
                println(response.message())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getKamar(): List<Kamar>  = kamarApiService.getKamar()

    override suspend fun getKamarById(idKamar: String): Kamar {
        return kamarApiService.getKamarById(idKamar)
    }
}