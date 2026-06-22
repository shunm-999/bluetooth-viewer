package com.shunm.android.abstinence.bluetooth.viewer.presentation.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shunm.android.abstinence.bluetooth.viewer.domain.model.BleException
import com.shunm.android.abstinence.bluetooth.viewer.domain.repository.BleScanRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val bleScanRepository: BleScanRepository
) : ViewModel() {

    private val _uiState : MutableStateFlow<ScanUiState> = MutableStateFlow(ScanUiState.Idle)
    val uiState : StateFlow<ScanUiState> = _uiState

    private var scanJob : Job? = null

    fun startScan() {
        scanJob?.cancel()
        scanJob = viewModelScope.launch {
            bleScanRepository.observeResults()
                .catch { e ->
                    if (e is BleException) {
                        _uiState.value = ScanUiState.Error(error = e.error)
                    } else {
                        throw e
                    }
                }
                .collect { devices ->
                    _uiState.value = ScanUiState.Scanning(devices = devices)
                }
        }
    }

    fun stopScan() {
        scanJob?.cancel()
        scanJob = null
        _uiState.value = ScanUiState.Idle
    }
}
