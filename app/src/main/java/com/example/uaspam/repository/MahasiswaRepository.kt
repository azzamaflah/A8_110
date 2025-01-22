package com.example.uaspam.repository

import com.example.uaspam.model.Mahasiswa
import com.example.uaspam.service.MahasiswaService
import java.io.IOException

interface MahasiswaRepository {
    suspend fun getMahasiswa(): List<Mahasiswa>
    suspend fun insertMahasiswa(mahasiswa: Mahasiswa)
    suspend fun updateMahasiswa(idMahasiswa: String, mahasiswa: Mahasiswa)
    suspend fun deleteMahasiswa(idMahasiswa: String)
    suspend fun getMahasiswaById(idMahasiswa: String): Mahasiswa
}

class NetworkMahasiswaRepository(
    private val mahasiswaApiService: MahasiswaService
) : MahasiswaRepository {
    override suspend fun insertMahasiswa(mahasiswa: Mahasiswa) {
        mahasiswaApiService.insertMahasiswa(mahasiswa)
    }

    override suspend fun updateMahasiswa(idMahasiswa: String, mahasiswa: Mahasiswa) {
        mahasiswaApiService.updateMahasiswa(idMahasiswa, mahasiswa)
    }

    override suspend fun deleteMahasiswa(idMahasiswa: String) {
        try {
            val response = mahasiswaApiService.deleteMahasiswa(idMahasiswa)
            if (!response.isSuccessful) {
                throw IOException("Failed to delete mahasiswa. HTTP Status code: ${response.code()}")
            } else {
                response.message()
                println(response.message())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getMahasiswa(): List<Mahasiswa> = mahasiswaApiService.getMahasiswa()
    override suspend fun getMahasiswaById(idMahasiswa: String): Mahasiswa {
        return mahasiswaApiService.getMahasiswaById("idMahasiswa")
    }
}