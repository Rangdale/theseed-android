package com.theseed.app.presentation.habits

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.EventRepeat
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.theseed.app.domain.model.HabitCategory
import com.theseed.app.domain.model.HabitDifficulty
import com.theseed.app.domain.model.HabitFrequency
import com.theseed.app.presentation.theme.ForestGreen
import java.util.Calendar

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditHabitScreen(
    onSaved: () -> Unit,
    onCancel: () -> Unit,
    viewModel: HabitViewModel = hiltViewModel()
) {
    val state by viewModel.createState.collectAsStateWithLifecycle()

    var title by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(HabitCategory.OTHER) }
    var selectedDifficulty by remember { mutableStateOf(HabitDifficulty.MEDIUM) }
    var selectedFrequency by remember { mutableStateOf(HabitFrequency.DAILY) }
    var expanded by remember { mutableStateOf(false) }
    var reminderTime by remember { mutableStateOf("") }
    var expandedFrequency by remember {
        mutableStateOf(false)
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

    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess) {
            viewModel.resetCreateState()
            onSaved()
        }
    }

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Column {
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
                }
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
                "Plant a New Seed",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
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
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
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

                            value = selectedCategory.name.lowercase()
                                .replaceFirstChar { it.uppercase() },

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

                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),

                            shape = RoundedCornerShape(16.dp)
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            }
                        ) {

                            HabitCategory.entries.forEach { category ->

                                DropdownMenuItem(

                                    text = {
                                        Text(
                                            category.name.lowercase()
                                                .replaceFirstChar { it.uppercase() }
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
                                        difficulty.name
                                            .lowercase()
                                            .replaceFirstChar { it.uppercase() }
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = ForestGreen,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    labelColor = MaterialTheme.colorScheme.onSurface
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
                            }
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

                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = onCancel
                        ) {
                            Text("Cancel")
                        }

                        Button(
                            onClick = {
                                viewModel.createHabit(
                                    title = title,
                                    category = selectedCategory,
                                    difficulty = selectedDifficulty,
                                    frequency = selectedFrequency,
                                    reminderTime = reminderTime.ifBlank { null }
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ForestGreen
                            ),
                            enabled = !state.isSaving
                        ) {

                            if (state.isSaving) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp)
                                )
                            } else {
                                Text("Save Habit")
                            }
                        }
                    }
                }
            }
        }
    }
}

