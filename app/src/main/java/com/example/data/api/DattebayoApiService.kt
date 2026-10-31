package com.example.data.api

import com.example.data.model.CharacterResponse
import com.example.data.model.ClanResponse
import com.example.data.model.NinjaCharacter
import com.example.data.model.VillageResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface DattebayoApiService {
    @GET("characters")
    suspend fun getCharacters(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 40
    ): CharacterResponse

    @GET("characters/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): NinjaCharacter

    @GET("clans")
    suspend fun getClans(): ClanResponse

    @GET("villages")
    suspend fun getVillages(): VillageResponse

    companion object {
        private const val BASE_URL = "https://dattebayo-api.onrender.com/"

        fun create(): DattebayoApiService {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

            val client = OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build()

            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(DattebayoApiService::class.java)
        }
    }
}
