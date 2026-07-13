package com.theseed.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theseed.app.domain.model.DisciplineScore
import com.theseed.app.domain.model.HomeDashboard
import com.theseed.app.domain.repository.HomeRepository
import com.theseed.app.domain.usecase.discipline.CalculateDisciplineScoreUseCase
import com.theseed.app.domain.usecase.discipline.GetDisciplineScoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val dashboard: HomeDashboard? = null,
    val disciplineScore: DisciplineScore? = null,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val getDisciplineScoreUseCase: GetDisciplineScoreUseCase,
    private val calculateDisciplineScoreUseCase: CalculateDisciplineScoreUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        viewModelScope.launch {
            _uiState.value = HomeUiState(isLoading = true)

            // Run dashboard data and score calculation in parallel
            launch {
                homeRepository.getDashboard()
                    .onSuccess { dashboard ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            dashboard = dashboard
                        )
                    }
                    .onFailure {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = it.message
                        )
                    }
            }

            launch {
                calculateDisciplineScoreUseCase()
                    .onSuccess { score ->
                        _uiState.value = _uiState.value.copy(
                            disciplineScore = score
                        )
                    }
            }
        }
    }
}