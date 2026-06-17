package com.shunm.android.abstinence.bluetooth.viewer.domain

sealed interface Option<out T> {
    fun unwrap(): T {
        return when (this) {
            is Some<T> -> value
            is None -> throw IllegalStateException("None cannot be unwrapped")
        }
    }

    fun unwrapOrNull(): T? {
        return when (this) {
            is Some<T> -> value
            is None -> null
        }
    }

    fun isSome(): Boolean {
        return when (this) {
            is Some<T> -> true
            is None -> false
        }
    }
}

data class Some<T>(val value: T) : Option<T>

data object None : Option<Nothing>