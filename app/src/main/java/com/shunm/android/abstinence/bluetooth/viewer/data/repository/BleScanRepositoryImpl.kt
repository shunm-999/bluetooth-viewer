package com.shunm.android.abstinence.bluetooth.viewer.data.repository

import com.shunm.android.abstinence.bluetooth.viewer.domain.model.BleScanResult
import com.shunm.android.abstinence.bluetooth.viewer.domain.repository.BleScanRepository
import kotlinx.coroutines.flow.Flow

class BleScanRepositoryImpl : BleScanRepository {

    override fun observeResults(): Flow<BleScanResult> {
        TODO("Not yet implemented")
    }
}