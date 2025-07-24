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
import pl.marianjureczko.poszukiwacz.screen.main.CustomInitializerForRoute
import pl.marianjureczko.poszukiwacz.screen.searching.LocationCalculator
import pl.marianjureczko.poszukiwacz.shared.PhotoHelper
import pl.marianjureczko.poszukiwacz.shared.port.StorageHelper
import pl.marianjureczko.poszukiwacz.shared.port.XmlHelper
import pl.marianjureczko.poszukiwacz.usecase.ResetProgressUC
import pl.marianjureczko.poszukiwacz.usecase.UpdateLocationUC
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
    fun xmlHelper(): XmlHelper {
        return XmlHelper()
    }

    @Singleton
    @Provides
    fun resetProgressUseCase(storage: StorageHelper): ResetProgressUC {
        return ResetProgressUC(storage)
    }

    @Singleton
    @Provides
    fun updateLocationUC(storage: StorageHelper, locationCalculator: LocationCalculator): UpdateLocationUC {
        return UpdateLocationUC(storage, locationCalculator)
    }
}