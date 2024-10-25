package pl.marianjureczko.poszukiwacz.shared.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.marianjureczko.poszukiwacz.screen.treasureseditor.AddTreasureUseCase
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ClassicVariantModule {
    @Singleton
    @Provides
    fun addTreasureUseCase(storage: StorageHelper): AddTreasureUseCase {
        return AddTreasureUseCase(storage)
    }
}