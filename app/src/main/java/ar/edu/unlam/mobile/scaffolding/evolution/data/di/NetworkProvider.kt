package ar.edu.unlam.mobile.scaffolding.evolution.data.di

import ar.edu.unlam.mobile.scaffolding.evolution.data.network.SuperHeroApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkProvider {
    // sarasa
    @Singleton
    @Provides
    fun getRetrofitProvider(): Retrofit =
        Retrofit
            .Builder()
            .baseUrl("https://superheroapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun getSuperHeroApiClient(retrofit: Retrofit): SuperHeroApiClient = retrofit.create(SuperHeroApiClient::class.java)
}
