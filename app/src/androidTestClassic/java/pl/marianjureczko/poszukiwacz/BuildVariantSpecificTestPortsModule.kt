package pl.marianjureczko.poszukiwacz

import android.content.Context
import com.ocadotechnology.gembus.test.someString
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pl.marianjureczko.poszukiwacz.shared.port.storage.StoragePort
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BuildVariantSpecificTestPortsModule {

    lateinit var storage: TestStoragePort

    @Singleton
    @Provides
    fun storagePort(@ApplicationContext appContext: Context): StoragePort {
        storage = TestStoragePort(appContext)
        return storage
    }

    fun assureRouteIsPresentInStorage(context: Context) {
        if (storage.routes.isEmpty()) {
            storage.initRoute(someString())
        }
    }
}
