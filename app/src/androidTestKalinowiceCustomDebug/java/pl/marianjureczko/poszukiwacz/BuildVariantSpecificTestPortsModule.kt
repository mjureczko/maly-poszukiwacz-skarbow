package pl.marianjureczko.poszukiwacz

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pl.marianjureczko.poszukiwacz.shared.port.StorageHelper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BuildVariantSpecificTestPortsModule {

    @Singleton
    @Provides
    fun storageHelper(@ApplicationContext appContext: Context): StorageHelper {
        return StorageHelper(appContext)
    }

    fun assureRouteIsPresentInStorage() {
        // do nothing, using real storage
    }

}
