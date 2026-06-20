package com.shunm.android.abstinence.bluetooth.viewer.domain.repository

import com.shunm.android.abstinence.bluetooth.viewer.domain.model.BleScanResult
import kotlinx.coroutines.flow.Flow

interface BleScanRepository {
    fun observeResults(): Flow<BleScanResult>
}