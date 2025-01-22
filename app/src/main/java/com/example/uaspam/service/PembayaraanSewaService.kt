package com.example.uaspam.service

import com.example.uaspam.model.PembayaraanSewa
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface PembayaraanSewaService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("bacasewa.php")
    suspend fun getSewa(): List<PembayaraanSewa>

    @GET("bacasewaid.php/{id_pembayaran}")
    suspend fun getSewaById(@Query("id_pembayaran") id_pembayaran:String): PembayaraanSewa

    @POST("insertsewa.php")
    suspend fun insertSewa(@Body pembayaraanSewa: PembayaraanSewa)

    @PUT("editsewa.php")
    suspend fun updateSewa(@Query("id_pembayaran")id_pembayaran: String, @Body pembayaraanSewa: PembayaraanSewa)

    @DELETE("deletesewa.php/{id_pembayaran}")
    suspend fun deleteSewa(@Query("id_pembayaran")id_pembayaran: String): Response<Void>
}