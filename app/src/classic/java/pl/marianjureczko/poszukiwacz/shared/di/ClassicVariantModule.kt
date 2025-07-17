package pl.marianjureczko.poszukiwacz.shared.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.marianjureczko.poszukiwacz.shared.port.StorageHelper
import pl.marianjureczko.poszukiwacz.usecase.AddTreasureDescriptionToRouteUC
import pl.marianjureczko.poszukiwacz.usecase.RemoveTreasureDescriptionFromRouteUC
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ClassicVariantModule {
    @Singleton
    @Provides
    fun addTreasureDescriptionToTouteUseCase(storage: StorageHelper): AddTreasureDescriptionToRouteUC {
        return AddTreasureDescriptionToRouteUC(storage)
    }

    @Singleton
    @Provides
    fun removeTreasureDescriptionFromRouteUseCase(storage: StorageHelper): RemoveTreasureDescriptionFromRouteUC {
        return RemoveTreasureDescriptionFromRouteUC(storage)
    }
}