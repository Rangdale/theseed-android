package com.theseed.app.presentation.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theseed.app.domain.model.AnalyticsDashboard
import com.theseed.app.domain.usecase.analytics.GetAnalyticsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AnalyticsUiState(
    val isLoading: Boolean = false,
    val data: AnalyticsDashboard? = null,
    val period: String = "weekly",
    val error: String? = null
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val getAnalyticsUseCase: GetAnalyticsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState

    init {
        loadAnalytics("weekly")
    }

    fun loadAnalytics(period: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, period = period, error = null)
            getAnalyticsUseCase(period)
                .onSuccess { data ->
                    _uiState.value = _uiState.value.copy(isLoading = false, data = data)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }
        }
    }

    fun setPeriod(period: String) {
        if (period != _uiState.value.period) {
            loadAnalytics(period)
        }
    }
}