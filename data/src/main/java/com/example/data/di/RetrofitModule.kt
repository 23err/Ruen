package com.example.ruen.di

import com.example.data.apiservices.libre.LibreTranslateService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val retrofitModule = module {
    // retrofit
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(get<String>(named("BASE_URL")))
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    // LibreTranslateService
    single<LibreTranslateService> { get<Retrofit>().create(LibreTranslateService::class.java) }
    // interceptor
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    // client
    single { OkHttpClient.Builder().addInterceptor(get<HttpLoggingInterceptor>()).build() }
    // base url
    single(named("BASE_URL")) { "https://libretranslate.com/" }
}