package pl.marianjureczko.poszukiwacz

import android.content.Context
import android.content.res.AssetManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pl.marianjureczko.poszukiwacz.screen.main.CustomInitializerForRoute
import pl.marianjureczko.poszukiwacz.shared.port.storage.StoragePort
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BuildVariantSpecificTestPortsModule {

    @Singleton
    @Provides
    fun storagePort(@ApplicationContext appContext: Context): StoragePort {
        return StoragePort(appContext)
    }

    fun assureRouteIsPresentInStorage(appContext: Context) {
        val storage: StoragePort = StoragePort(appContext)
        val assetManager: AssetManager = appContext.assets
        val initializer = CustomInitializerForRoute(storage, assetManager)
        initializer.copyRouteToLocalStorage(forceCopy = true)
    }

}

