package com.example.mobies.di

import com.example.mobies.network.ApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor {
        return object : Interceptor {
            val API_KEY =
                "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1ZjM4MTg2MzU1NzQyN2MwNzFiYzc5MDliMzU3OWY3MiIsIm5iZiI6MTczNTQ2NzAxMi4yMTksInN1YiI6IjY3NzEyMDA0M2RmZjBkZTk5YjYxYmIyNSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.EBR-vGJfgdMY1ceJtWvOGjJ5BNfIIGIgX0lhBGLNDZI" // Replace with your actual API key

            override fun intercept(chain: Interceptor.Chain): Response {

                val request = chain.request().newBuilder()
                    .addHeader("accept", "application/json")
                    .addHeader(
                        "Authorization",
                        "Bearer ${API_KEY}"
                    ) // Or "Api-Key" or whatever the API requires
                    .build()
                return chain.proceed(request)
            }
        }
    }

    @Provides
    @Singleton
    fun provideHttpClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        val baseUrl = "https://api.themoviedb.org/3/"
        return Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
//        .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit:Retrofit): ApiService{
        return retrofit.create(ApiService::class.java)
    }
}