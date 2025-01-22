package com.example.uaspam.repository

import com.example.uaspam.model.Bangunan
import com.example.uaspam.service.BangunanService
import java.io.IOException

interface BangunanRepository {
    suspend fun getBangunan(): List<Bangunan>
    suspend fun insertBangunan(bangunan: Bangunan)
    suspend fun updateBangunan(idBangunan: String, bangunan: Bangunan)
    suspend fun deleteBangunan(idBangunan: String)
    suspend fun getBangunanById(idBangunan: String): Bangunan
}

class NetworkBangunanRepository(
    private val bangunanApiService: BangunanService
) : BangunanRepository {
    override suspend fun insertBangunan(bangunan: Bangunan) {
        bangunanApiService.insertBangunan(bangunan)
    }

    override suspend fun updateBangunan(idBangunan: String, bangunan: Bangunan) {
        bangunanApiService.updateBangunan(idBangunan, bangunan)
    }

    override suspend fun deleteBangunan(idBangunan: String) {
        try {
            val response = bangunanApiService.deleteBangunan(idBangunan)
            if (!response.isSuccessful) {
                throw IOException("Failed to delete bangunan. HTTP Status code: ${response.code()}")
            } else {
                response.message()
                println(response.message())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getBangunan(): List<Bangunan> = bangunanApiService.getBangunan()

    override suspend fun getBangunanById(idBangunan: String): Bangunan {
        return bangunanApiService.getBangunanById(idBangunan)
    }
}