package ru.createtogether.feature_network_impl.di
import ru.createtogether.feature_network_impl.data.ErrorHandlerRepositoryImpl
import ru.createtogether.feature_network_impl.domain.ErrorHandlerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ErrorHandlerModule {

    @Provides
    fun provideErrorHandlerRepository(): ErrorHandlerRepository {
        return ErrorHandlerRepositoryImpl()
    }
}