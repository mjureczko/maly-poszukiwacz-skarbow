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
import pl.marianjureczko.poszukiwacz.shared.port.location.AndroidLocationFactoryImpl
import pl.marianjureczko.poszukiwacz.shared.port.storage.StoragePort
import pl.marianjureczko.poszukiwacz.shared.port.storage.XmlHelper
import pl.marianjureczko.poszukiwacz.usecase.AndroidLocationFactory
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
    fun customInitializerForRoute(storagePort: StoragePort, assetManager: AssetManager): CustomInitializerForRoute {
        return CustomInitializerForRoute(storagePort, assetManager)
    }

    @Provides
    @MainDispatcher
    fun mainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @DefaultDispatcher
    fun defaultDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Singleton
    @Provides
    fun photoHelper(@ApplicationContext appContext: Context, storagePort: StoragePort): PhotoHelper {
        return PhotoHelper(appContext, storagePort)
    }

    @Singleton
    @Provides
    fun resources(@ApplicationContext appContext: Context): Resources {
        return appContext.resources
    }

    @Singleton
    @Provides
    fun locationCalculator(androidLocationFactory: AndroidLocationFactory): LocationCalculator {
        return LocationCalculator(androidLocationFactory)
    }

    @Singleton
    @Provides
    fun xmlHelper(): XmlHelper {
        return XmlHelper()
    }

    @Singleton
    @Provides
    fun resetProgressUseCase(storage: StoragePort): ResetProgressUC {
        return ResetProgressUC(storage)
    }

    @Singleton
    @Provides
    fun updateLocationUC(storage: StoragePort, locationCalculator: LocationCalculator): UpdateLocationUC {
        return UpdateLocationUC(storage, locationCalculator)
    }

    @Singleton
    @Provides
    fun androidLocationFactory(): AndroidLocationFactory {
        return AndroidLocationFactoryImpl()
    }
}