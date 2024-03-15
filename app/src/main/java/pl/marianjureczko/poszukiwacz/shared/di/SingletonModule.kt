package pl.marianjureczko.poszukiwacz.shared.di

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pl.marianjureczko.poszukiwacz.activity.searching.LocationCalculator
import pl.marianjureczko.poszukiwacz.activity.searching.n.LocationFetcher
import pl.marianjureczko.poszukiwacz.shared.Settings
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {

    @Singleton
    @Provides
    fun storageHelper(@ApplicationContext appContext: Context): StorageHelper {
        return StorageHelper(appContext)
    }

    @Singleton
    @Provides
    fun settings(@ApplicationContext appContext: Context): Settings {
        return Settings(appContext.assets)
    }

    @Singleton
    @Provides
    fun resources(@ApplicationContext appContext: Context): Resources {
        return appContext.resources
    }

    @Singleton
    @Provides
    fun locationCalculator(): LocationCalculator {
        return LocationCalculator()
    }

    @Singleton
    @Provides
    fun locationService(@ApplicationContext appContext: Context): LocationFetcher {
        return LocationFetcher(appContext)
    }
}