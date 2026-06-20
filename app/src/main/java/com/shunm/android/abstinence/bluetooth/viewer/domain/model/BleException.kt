package com.shunm.android.abstinence.bluetooth.viewer.domain.model

class BleException(
    val error: BleError,
    cause: Throwable? = null,
) : RuntimeException(cause)
