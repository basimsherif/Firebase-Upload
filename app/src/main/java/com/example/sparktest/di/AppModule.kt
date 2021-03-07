package com.example.sparktest.di

import android.content.Context
import com.example.sparktest.data.local.AppDatabase
import com.example.sparktest.data.local.ImageDao
import com.example.sparktest.data.local.LocalDataSource
import com.example.sparktest.data.remote.FirebaseSource
import com.example.sparktest.data.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

/**
 * Module class used for Hilt dependency injection configuration
 */
@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRepository(
        firebaseSource: FirebaseSource,
        localDataSource: LocalDataSource
    ) = Repository(firebaseSource, localDataSource)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideImageDao(db: AppDatabase) = db.imageDao()

    @Singleton
    @Provides
    fun provideLocalDataSource(
        imageDao: ImageDao
    ) = LocalDataSource(imageDao)

    @Singleton
    @Provides
    fun provideFirebaseSource(@ApplicationContext appContext: Context) =
        FirebaseSource(appContext)


}