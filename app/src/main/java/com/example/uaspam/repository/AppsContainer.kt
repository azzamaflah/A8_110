package com.example.uaspam.repository

import com.example.uaspam.service.BangunanService
import com.example.uaspam.service.KamarService
import com.example.uaspam.service.MahasiswaService
import com.example.uaspam.service.PembayaraanSewaService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val mahasiswaRepository: MahasiswaRepository
    val bangunanRepository: BangunanRepository
    val kamarRepository: KamarRepository
    val pembayaraanSewa : PembayaraanSewaRepository
}

class AppsContainer : AppContainer {
    private val baseUrl = "http://10.0.2.2/backend/" // Local backend URL
    private val json = Json { ignoreUnknownKeys = true }
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl).build()

    private val mahasiswaService: MahasiswaService by lazy { retrofit.create(MahasiswaService::class.java) }
    private val bangunanService: BangunanService by lazy { retrofit.create(BangunanService::class.java) }
    private val kamarService: KamarService by lazy { retrofit.create(KamarService::class.java) }
    private val sewaService: PembayaraanSewaService by lazy { retrofit.create(PembayaraanSewaService::class.java) }

    override val mahasiswaRepository: MahasiswaRepository by lazy {
        NetworkMahasiswaRepository(mahasiswaService)}
    override val bangunanRepository: BangunanRepository by lazy {
            NetworkBangunanRepository(bangunanService)}
    override val kamarRepository: KamarRepository by lazy {
                NetworkKamarRepository(kamarService)}
    override val pembayaraanSewa: PembayaraanSewaRepository by lazy {
                    NetworkPembayaraanSewaRepository(sewaService)}
}