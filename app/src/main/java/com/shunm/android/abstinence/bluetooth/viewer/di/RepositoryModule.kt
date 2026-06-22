package com.shunm.android.abstinence.bluetooth.viewer.di

import com.shunm.android.abstinence.bluetooth.viewer.data.repository.BleScanRepositoryImpl
import com.shunm.android.abstinence.bluetooth.viewer.domain.repository.BleScanRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindBleScanRepository(
        impl: BleScanRepositoryImpl
    ): BleScanRepository
}
