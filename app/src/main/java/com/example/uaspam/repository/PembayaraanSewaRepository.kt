package com.example.uaspam.repository
import com.example.uaspam.model.PembayaraanSewa
import com.example.uaspam.service.PembayaraanSewaService
import java.io.IOException

interface PembayaraanSewaRepository {
    suspend fun getSewa(): List<PembayaraanSewa>
    suspend fun insertSewa(pembayaraanSewa: PembayaraanSewa)
    suspend fun updateSewa(idPembayaran: String, pembayaraanSewa: PembayaraanSewa)
    suspend fun deleteSewa(idPembayaran: String)
    suspend fun getSewaById(idPembayaran: String): PembayaraanSewa
}

class NetworkPembayaraanSewaRepository(
    private val sewaApiService: PembayaraanSewaService
) : PembayaraanSewaRepository {
    override suspend fun insertSewa(pembayaraanSewa: PembayaraanSewa) {
        sewaApiService.insertSewa(pembayaraanSewa)
    }

    override suspend fun updateSewa(idPembayaran: String, pembayaraanSewa: PembayaraanSewa) {
        sewaApiService.updateSewa(idPembayaran, pembayaraanSewa)
    }

    override suspend fun deleteSewa(idPembayaran: String) {
        try {
            val response = sewaApiService.deleteSewa(idPembayaran)
            if (!response.isSuccessful) {
                throw IOException("Failed to delete pembayaran sewa. HTTP Status code: ${response.code()}")
            } else {
                response.message()
                println(response.message())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getSewa(): List<PembayaraanSewa> {
        return sewaApiService.getSewa()
    }

    override suspend fun getSewaById(idPembayaran: String): PembayaraanSewa {
        return sewaApiService.getSewaById(idPembayaran)
    }
}