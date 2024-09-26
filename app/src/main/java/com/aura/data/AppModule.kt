package com.aura.data

import com.aura.data.remote.ApiService
import com.aura.data.repositories.HomeRepository
import com.aura.data.repositories.LoginRepository
import com.aura.viewmodels.HomeViewModel
import com.aura.viewmodels.LoginViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {
    single {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ApiService::class.java)
    }

    single { LoginRepository(get<ApiService>()) }
    single { HomeRepository(get<ApiService>()) }


    viewModel { LoginViewModel(get()) }
    viewModel { HomeViewModel(get()) }
}