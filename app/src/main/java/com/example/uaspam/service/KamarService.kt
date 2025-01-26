package com.example.uaspam.service


import com.example.uaspam.model.Kamar
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface KamarService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("bacakamar.php")
    suspend fun getKamar(): List<Kamar>

    @GET("bacakamarid.php")
    suspend fun getKamarById(@Query("id_kamar") id_kamar:String): Kamar

    @POST("insertkamar.php")
    suspend fun insertKamar(@Body kamar: Kamar)

    @PUT("editkamar.php")
    suspend fun updateKamar(@Query("id_kamar")id_kamar: String, @Body kamar: Kamar)

    @DELETE("deletekamar.php")
    suspend fun deleteKamar(@Query("id_kamar")id_kamar: String): retrofit2.Response<Void>
}