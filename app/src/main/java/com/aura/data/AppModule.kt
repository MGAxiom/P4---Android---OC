package com.aura.data

import com.aura.data.remote.LoginApiService
import com.aura.viewmodels.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://your-api-base-url/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LoginApiService::class.java)
    }

    single { LoginRepository(get()) }

    viewModel { LoginViewModel(get()) }
}