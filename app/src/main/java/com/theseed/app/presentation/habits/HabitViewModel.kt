package com.theseed.app.presentation.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theseed.app.domain.model.*
import com.theseed.app.domain.usecase.habit.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HabitListUiState(
    val isLoading: Boolean = false,
    val habits: List<Habit> = emptyList(),
    val error: String? = null
)

data class CreateHabitUiState(
    val isSaving: Boolean = false,
    val error: String? = null,
    val saveSuccess: Boolean = false
)

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val getHabitsUseCase: GetHabitsUseCase,
    private val createHabitUseCase: CreateHabitUseCase,
    private val updateHabitUseCase: UpdateHabitUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase
) : ViewModel() {

    private val _listState = MutableStateFlow(HabitListUiState())
    val listState: StateFlow<HabitListUiState> = _listState

    private val _createState = MutableStateFlow(CreateHabitUiState())
    val createState: StateFlow<CreateHabitUiState> = _createState

    init {
        loadHabits()
    }

    fun loadHabits() {
        viewModelScope.launch {
            _listState.value = _listState.value.copy(isLoading = true, error = null)
            getHabitsUseCase()
                .onSuccess { habits ->
                    _listState.value = HabitListUiState(habits = habits)
                }
                .onFailure { e ->
                    _listState.value = _listState.value.copy(isLoading = false, error = e.message)
                }
        }
    }

    fun createHabit(
        title: String,
        category: HabitCategory,
        difficulty: HabitDifficulty,
        frequency: HabitFrequency,
        reminderTime: String?
    ) {
        viewModelScope.launch {
            _createState.value = CreateHabitUiState(isSaving = true)
            createHabitUseCase(title, category, difficulty, frequency, reminderTime)
                .onSuccess {
                    _createState.value = CreateHabitUiState(saveSuccess = true)
                    loadHabits() // refresh list after creating
                }
                .onFailure { e ->
                    _createState.value = CreateHabitUiState(error = e.message)
                }
        }
    }

    fun deleteHabit(id: String) {
        viewModelScope.launch {
            deleteHabitUseCase(id)
                .onSuccess { loadHabits() }
                .onFailure { e ->
                    _listState.value = _listState.value.copy(error = e.message)
                }
        }
    }

    fun resetCreateState() {
        _createState.value = CreateHabitUiState()
    }
}