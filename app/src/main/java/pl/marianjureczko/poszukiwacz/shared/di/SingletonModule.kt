package pl.marianjureczko.poszukiwacz.shared.di

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import pl.marianjureczko.poszukiwacz.activity.searching.LocationCalculator
import pl.marianjureczko.poszukiwacz.activity.searching.n.LocationFetcher
import pl.marianjureczko.poszukiwacz.screen.main.CustomInitializerForRoute
import pl.marianjureczko.poszukiwacz.shared.PhotoHelper
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher

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
    @IoDispatcher
    fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @MainDispatcher
    fun mainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @DefaultDispatcher
    fun defaultDispatcher(): CoroutineDispatcher = Dispatchers.Main

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
    fun locationCalculator(): LocationCalculator {
        return LocationCalculator()
    }

    @Singleton
    @Provides
    fun fusedLocationClient(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Singleton
    @Provides
    fun locationService(@ApplicationContext appContext: Context, locationClient: FusedLocationProviderClient): LocationFetcher {
        return LocationFetcher(appContext, locationClient)
    }
}