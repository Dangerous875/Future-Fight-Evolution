package ar.edu.unlam.mobile.scaffolding.evolution.data.di

import ar.edu.unlam.mobile.scaffolding.evolution.data.firebase.AnalyticsManager
import ar.edu.unlam.mobile.scaffolding.evolution.data.repository.StorageService
import ar.edu.unlam.mobile.scaffolding.evolution.domain.repository.ImageRepository
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFireStore(): FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(): FirebaseAnalytics = Firebase.analytics

    @Provides
    @Singleton
    fun provideFirebaseStorage() = Firebase.storage

    @Provides
    fun provideImageRepository(
        storage: FirebaseStorage,
        db: FirebaseFirestore,
        auth: FirebaseAuth,
    ): ImageRepository =
        StorageService(
            storage = storage,
            db = db,
            auth = auth,
        )

    @Provides
    @Singleton
    fun provideFirebaseConfig(): FirebaseRemoteConfig =
        FirebaseRemoteConfig.getInstance().apply {
            setConfigSettingsAsync(remoteConfigSettings { minimumFetchIntervalInSeconds = 3600 })
            fetchAndActivate()
        }

    @Provides
    fun provideAnalyticsManager(analytics: FirebaseAnalytics): AnalyticsManager = AnalyticsManager(analytics)
}
