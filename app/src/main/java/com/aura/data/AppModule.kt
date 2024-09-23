package com.aura.data

import com.aura.data.remote.LoginApiService
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
            .create(LoginApiService::class.java)
    }

    single { LoginRepository(get<LoginApiService>()) }

    viewModel { LoginViewModel(get<LoginRepository>()) }
}