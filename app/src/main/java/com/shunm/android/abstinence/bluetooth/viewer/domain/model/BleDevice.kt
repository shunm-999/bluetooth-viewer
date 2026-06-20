package com.shunm.android.abstinence.bluetooth.viewer.domain.model

class BleDevice(
    val address: String,
    val name: String,
) {
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