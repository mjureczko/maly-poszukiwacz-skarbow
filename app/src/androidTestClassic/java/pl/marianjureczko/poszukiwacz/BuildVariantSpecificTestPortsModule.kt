package pl.marianjureczko.poszukiwacz

import android.content.Context
import com.ocadotechnology.gembus.test.someString
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

    lateinit var storage: TestStoragePort

    @Singleton
    @Provides
    fun storageHelper(@ApplicationContext appContext: Context): StorageHelper {
        storage = TestStoragePort(appContext)
        return storage
    }

    fun assureRouteIsPresentInStorage() {
        if (storage.routes.isEmpty()) {
            storage.initRoute(someString())
        }
    }
}
