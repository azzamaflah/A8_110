package com.example.uaspam.service

import com.example.uaspam.model.Bangunan
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface BangunanService {

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @GET("bacabangunan.php")
    suspend fun getBangunan(): List<Bangunan>

    @GET("bacabangunanid.php/{id_bangunan}")
    suspend fun getBangunanById(@Query("id_bangunan") iid_bangunan:String): Bangunan

    @POST("insertbangunan.php")
    suspend fun insertBangunan(@Body bangunan: Bangunan)

    @PUT("editbangunan.php")
    suspend fun updateBangunan(@Query("id_bangunan")id_bangunan: String, @Body bangunan: Bangunan)

    @DELETE("deletebangunan.php/{id_bangunan}")
    suspend fun deleteBangunan(@Query("id_bangunan")id_bangunan: String): Response<Void>
}