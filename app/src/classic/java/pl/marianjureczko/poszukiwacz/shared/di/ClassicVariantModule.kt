package pl.marianjureczko.poszukiwacz.shared.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import pl.marianjureczko.poszukiwacz.usecase.AddTreasureDescriptionToRoute
import pl.marianjureczko.poszukiwacz.usecase.RemoveTreasureDescriptionFromRoute
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ClassicVariantModule {
    @Singleton
    @Provides
    fun addTreasureDescriptionToTouteUseCase(storage: StorageHelper): AddTreasureDescriptionToRoute {
        return AddTreasureDescriptionToRoute(storage)
    }

    @Singleton
    @Provides
    fun removeTreasureDescriptionFromRouteUseCase(storage: StorageHelper): RemoveTreasureDescriptionFromRoute {
        return RemoveTreasureDescriptionFromRoute(storage)
    }
}