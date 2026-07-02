package com.theseed.app.presentation.habits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theseed.app.domain.model.*
import com.theseed.app.domain.repository.HabitRepository
import com.theseed.app.domain.usecase.habit.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HabitListUiState(
    val isLoading: Boolean = false,
    val habits: List<Habit> = emptyList(),
    val completedHabitIds: Set<String> = emptySet(),
    val streaks: Map<String, Int> = emptyMap(),
    val error: String? = null
)

data class CreateHabitUiState(
    val isSaving: Boolean = false,
    val error: String? = null,
    val saveSuccess: Boolean = false
)

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val habitRepository: HabitRepository,
    private val getHabitsUseCase: GetHabitsUseCase,
    private val createHabitUseCase: CreateHabitUseCase,
    private val updateHabitUseCase: UpdateHabitUseCase,
    private val deleteHabitUseCase: DeleteHabitUseCase,
    private val toggleCompletionUseCase: ToggleCompletionUseCase,
    private val getTodayCompletionsUseCase: GetTodayCompletionsUseCase
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

            val habits = getHabitsUseCase().getOrElse {
                _listState.value = _listState.value.copy(
                    isLoading = false,
                    error = it.message
                )
                return@launch
            }

            val completedIds = getTodayCompletionsUseCase()
                .getOrElse { emptySet() }

            // Fetch streak for each habit
            val streaks = mutableMapOf<String, Int>()
            habits.forEach { habit ->
                habitRepository.getHabitStreak(habit.id)
                    .onSuccess { streak -> streaks[habit.id] = streak }
                    .onFailure { streaks[habit.id] = 0 }
            }

            _listState.value = HabitListUiState(
                habits = habits,
                completedHabitIds = completedIds,
                streaks = streaks
            )
        }
    }

    fun toggleCompletion(habitId: String) {
        viewModelScope.launch {
            val currentCompleted = _listState.value.completedHabitIds
            val newCompleted = if (habitId in currentCompleted) {
                currentCompleted - habitId
            } else {
                currentCompleted + habitId
            }
            _listState.value = _listState.value.copy(completedHabitIds = newCompleted)

            android.util.Log.d("CompletionDebug", "Toggling habit: $habitId")

            toggleCompletionUseCase(habitId)
                .onSuccess {
                    android.util.Log.d("CompletionDebug", "Toggle success: completed=${it.completed}")
                }
                .onFailure {
                    android.util.Log.e("CompletionDebug", "Toggle failed: ${it.message}", it)
                    _listState.value = _listState.value.copy(completedHabitIds = currentCompleted)
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
                    loadHabits()
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