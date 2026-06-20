package com.shunm.android.abstinence.bluetooth.viewer.domain.model

class BleDevice(
    val address: String,
    val name: Name,
) {
    sealed interface Name {
        data class Present(val value: String) : Name
        data object None : Name
    }

    override fun equals(other: Any?): Boolean {
        if (other !is BleDevice) {
            return false
        }
        return address == other.address
    }

    override fun hashCode(): Int {
        return address.hashCode()
    }
}