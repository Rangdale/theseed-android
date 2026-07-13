package com.theseed.app.presentation.habits

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.theseed.app.domain.model.HabitCategory
import com.theseed.app.domain.model.HabitDifficulty
import com.theseed.app.domain.model.HabitFrequency
import com.theseed.app.domain.model.displayName
import com.theseed.app.presentation.theme.ForestGreen
import com.theseed.app.presentation.theme.color
import java.util.Calendar

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditHabitScreen(
    habitId: String? = null,
    onSaved: () -> Unit,
    onCancel: () -> Unit,
    viewModel: HabitViewModel = hiltViewModel()
) {
    val state by viewModel.createState.collectAsStateWithLifecycle()
    var expanded by remember { mutableStateOf(false) }
    var expandedFrequency by remember {
        mutableStateOf(false)
    }
    val isEditMode = habitId != null
    val createState by viewModel.createState.collectAsStateWithLifecycle()
    val listState by viewModel.listState.collectAsStateWithLifecycle()
    val existingHabit = remember(habitId, listState.habits) {
        if (habitId != null) listState.habits.find { it.id == habitId } else null
    }
    var title by remember(existingHabit) {
        mutableStateOf(existingHabit?.title ?: "")
    }
    var selectedCategory by remember(existingHabit) {
        mutableStateOf(existingHabit?.category ?: HabitCategory.OTHER)
    }
    var selectedDifficulty by remember(existingHabit) {
        mutableStateOf(existingHabit?.difficulty ?: HabitDifficulty.MEDIUM)
    }
    var selectedFrequency by remember(existingHabit) {
        mutableStateOf(existingHabit?.frequency ?: HabitFrequency.DAILY)
    }
    var reminderTime by remember(existingHabit) {
        mutableStateOf(
            existingHabit?.reminderTime?.let {
                String.format("%02d:%02d", it.hour, it.minute)
            } ?: ""
        )
    }
    var durationMinutes by remember(existingHabit) {
        mutableStateOf(existingHabit?.durationMinutes?.toString() ?: "")
    }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val timePickerDialog = TimePickerDialog(
        context,
        { _, hour, minute ->
            reminderTime = String.format("%02d:%02d", hour, minute)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        false
    )

    LaunchedEffect(createState.saveSuccess) {
        if (createState.saveSuccess) {
            viewModel.resetCreateState()
            onSaved()
        }
    }

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onCancel() }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "TheSeed",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = if (isEditMode) "Edit Habit" else "New Habit",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = if (isEditMode) "Edit Your Seed" else "Plant a New Seed",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isEditMode)
                    "Update your intention and keep growing."
                else
                    "Define your intention. Every great journey begins with a single, deliberate action.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(vertical = 20.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 22.dp,
                            vertical = 24.dp
                        )
                ) {
                    Text(
                        text = "Habit Title",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = title,
                        onValueChange = {
                            title = it
                        },

                        modifier = Modifier.fillMaxWidth(),

                        placeholder = {
                            Text("e.g. Morning Meditation")
                        },

                        singleLine = true,

                        shape = RoundedCornerShape(16.dp),

                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Category",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {
                            expanded = !expanded
                        }
                    ) {

                        OutlinedTextField(

                            value = selectedCategory.displayName(),

                            onValueChange = {},

                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),

                            readOnly = true,

                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expanded
                                )
                            },

                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,

                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,

                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,

                                focusedTrailingIconColor = MaterialTheme.colorScheme.outline,
                                unfocusedTrailingIconColor = MaterialTheme.colorScheme.outline,
                            ),

                            shape = RoundedCornerShape(16.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            },
                            containerColor = MaterialTheme.colorScheme.surface
                        ) {

                            HabitCategory.entries.forEach { category ->

                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            category.displayName()
                                        )
                                    },

                                    onClick = {
                                        selectedCategory = category
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Difficulty",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        HabitDifficulty.entries.forEach { difficulty ->
                            FilterChip(
                                selected = selectedDifficulty == difficulty,
                                onClick = { selectedDifficulty = difficulty },
                                label = {
                                    Text(
                                        text = difficulty.name
                                            .lowercase()
                                            .replaceFirstChar { it.uppercase() },
                                        color = if (selectedDifficulty == difficulty)
                                            MaterialTheme.colorScheme.onPrimary
                                        else
                                            difficulty.color()
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = difficulty.color(),
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    labelColor = difficulty.color()
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Reminder Time",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                timePickerDialog.show()
                            }
                    ) {
                        OutlinedTextField(
                            value = reminderTime.ifEmpty { "--:--" },
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        timePickerDialog.show()
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = "Select reminder time"
                                    )
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Repeat Frequency",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ExposedDropdownMenuBox(
                        expanded = expandedFrequency,
                        onExpandedChange = {
                            expandedFrequency = !expandedFrequency
                        }
                    ) {

                        OutlinedTextField(
                            value =
                                if (selectedFrequency == HabitFrequency.DAILY)
                                    "Every Day"
                                else
                                    "Every Week",

                            onValueChange = {},

                            readOnly = true,

                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),

                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,

                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,

                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,

                                focusedTrailingIconColor = MaterialTheme.colorScheme.outline,
                                unfocusedTrailingIconColor = MaterialTheme.colorScheme.outline,
                            ),

                            shape = RoundedCornerShape(16.dp),

                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = expandedFrequency
                                )
                            }
                        )

                        ExposedDropdownMenu(
                            expanded = expandedFrequency,
                            onDismissRequest = {
                                expandedFrequency = false
                            },
                            containerColor = MaterialTheme.colorScheme.surface
                        ) {

                            DropdownMenuItem(
                                text = { Text("Every Day") },
                                onClick = {
                                    selectedFrequency = HabitFrequency.DAILY
                                    expandedFrequency = false
                                }
                            )

                            DropdownMenuItem(
                                text = { Text("Every Week") },
                                onClick = {
                                    selectedFrequency = HabitFrequency.WEEKLY
                                    expandedFrequency = false
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Duration (minutes)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = durationMinutes,
                        onValueChange = { if (it.all { c -> c.isDigit() }) durationMinutes = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g. 30") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF2FAED)
                        ),
                        border = BorderStroke(
                            1.dp,
                            Color(0xFF97AF8C)
                        )
                    ) {

                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.Top
                        ) {

                            Icon(
                                imageVector = Icons.Default.Eco,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = "Consistency builds resilience. This habit will contribute to your overall Discipline Score over time.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.width(16.dp))
                        }
                    }

                    state.error?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = onCancel,
                            border = BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.outline
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.onSurface
                            )
                        ) {
                            Text("Cancel")
                        }

                        Button(
                            onClick = {
                                if (isEditMode && habitId != null) {
                                    viewModel.updateHabit(
                                        id = habitId,
                                        title = title,
                                        category = selectedCategory,
                                        difficulty = selectedDifficulty,
                                        frequency = selectedFrequency,
                                        reminderTime = reminderTime.ifBlank { null },
                                        durationMinutes = durationMinutes.toIntOrNull()
                                    )
                                } else {
                                    viewModel.createHabit(
                                        title = title,
                                        category = selectedCategory,
                                        difficulty = selectedDifficulty,
                                        frequency = selectedFrequency,
                                        reminderTime = reminderTime.ifBlank { null },
                                        durationMinutes = durationMinutes.toIntOrNull()
                                    )
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                            enabled = !createState.isSaving
                        ) {
                            if (createState.isSaving) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp))
                            } else {
                                Text(if (isEditMode) "Update Habit" else "Save Habit")
                            }
                        }
                    }
                }
            }
        }
    }
}

