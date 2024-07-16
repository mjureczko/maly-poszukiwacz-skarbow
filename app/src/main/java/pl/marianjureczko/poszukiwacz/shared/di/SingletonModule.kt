package pl.marianjureczko.poszukiwacz.shared.di

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import pl.marianjureczko.poszukiwacz.activity.searching.n.LocationFetcher
import pl.marianjureczko.poszukiwacz.screen.main.CustomInitializerForRoute
import pl.marianjureczko.poszukiwacz.shared.PhotoHelper
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonModule {

    @Singleton
    @Provides
    fun assetManager(@ApplicationContext appContext: Context): AssetManager {
        return appContext.assets
    }

    @Singleton
    @Provides
    fun customInitializerForRoute(storageHelper: StorageHelper, assetManager: AssetManager): CustomInitializerForRoute {
        return CustomInitializerForRoute(storageHelper, assetManager)
    }

    @Provides
    fun dispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun photoHelper(@ApplicationContext appContext: Context, storageHelper: StorageHelper): PhotoHelper {
        return PhotoHelper(appContext, storageHelper)
    }

    @Singleton
    @Provides
    fun storageHelper(@ApplicationContext appContext: Context): StorageHelper {
        return StorageHelper(appContext)
    }

    @Singleton
    @Provides
    fun resources(@ApplicationContext appContext: Context): Resources {
        return appContext.resources
    }

    @Singleton
    @Provides
    fun locationCalculator(): pl.marianjureczko.poszukiwacz.activity.searching.LocationCalculator {
        return pl.marianjureczko.poszukiwacz.activity.searching.LocationCalculator()
    }

    @Singleton
    @Provides
    fun locationService(@ApplicationContext appContext: Context): LocationFetcher {
        return LocationFetcher(appContext)
    }
}