package com.theseed.app.presentation.habits

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Swipe
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.theseed.app.domain.model.Habit
import com.theseed.app.presentation.theme.BackgroundOffWhite
import com.theseed.app.presentation.theme.ForestGreen
import com.theseed.app.presentation.theme.SurfaceWhite
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import com.theseed.app.domain.model.displayName
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.ui.graphics.Color
import com.theseed.app.presentation.theme.color

@Composable
fun HabitListScreen(
    onAddHabit: () -> Unit,
    onEditHabit: (String) -> Unit,
    viewModel: HabitViewModel = hiltViewModel()
) {
    val state by viewModel.listState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        // Only load if list is empty — avoids redundant fetches
        if (state.habits.isEmpty()) {
            viewModel.loadHabits()
        }
    }

    Scaffold(
        containerColor = BackgroundOffWhite,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddHabit,
                containerColor = ForestGreen
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add habit", tint = SurfaceWhite)
            }
        }
    ) { padding ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = ForestGreen)
                }
            }
            state.habits.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No habits yet — tap + to plant your first seed")
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),

                    contentPadding = PaddingValues(
                        start = 20.dp,
                        end = 20.dp,
                        top = 0.dp,
                        bottom = 32.dp
                    ),

                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // ---------- HEADER ----------

                    item {

                        Text(
                            text = "Today's Focus",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Stay consistent with your natural rhythms.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector = Icons.Default.Swipe,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = "Swipe habits to act",
                                fontSize = 12.sp,
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    // ---------- CATEGORY LIST ----------

                    val grouped = state.habits.groupBy { it.category }

                    grouped.forEach { (category, habitsInCategory) ->

                        item {

                            Text(
                                text = category.displayName(),
                                style = MaterialTheme.typography.titleMedium,
                                color = ForestGreen,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        items(habitsInCategory) { habit ->
                            HabitCard(
                                habit = habit,
                                isCompleted = habit.id in state.completedHabitIds,
                                streak = state.streaks[habit.id] ?: 0,
                                onToggleComplete = { viewModel.toggleCompletion(habit.id) },
                                onDelete = { viewModel.deleteHabit(habit.id) },
                                onEdit = { onEditHabit(habit.id) }  // ← add this
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitCard(
    habit: Habit,
    isCompleted: Boolean,
    streak: Int,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else false
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.error),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = SurfaceWhite,
                    modifier = Modifier
                        .padding(end = 24.dp)
                        .size(28.dp)
                )
            }
        }
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        top = 16.dp,
                        bottom = 16.dp,
                        end = 12.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left — habit info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = habit.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AssistChip(
                            onClick = {},
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = Color.Transparent,
                                labelColor = habit.difficulty.color()
                            ),
                            label = {
                                Text(
                                    habit.difficulty.name
                                        .lowercase()
                                        .replaceFirstChar { it.uppercase() }
                                )
                            }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = ForestGreen
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = if (streak > 0) "$streak Day Streak" else "No streak yet",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = .75f)
                        )
                    }
                }

                // Right — complete button + 3-dot menu
                Row(verticalAlignment = Alignment.CenterVertically) {

                    // Completion toggle
                    // Completion toggle
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(
                                if (isCompleted) ForestGreen
                                else MaterialTheme.colorScheme.surface
                            )
                            .clickable { onToggleComplete() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = if (isCompleted) SurfaceWhite
                            else MaterialTheme.colorScheme.outline
                        )
                    }

                    Spacer(modifier = Modifier.width(2.dp))

                    // 3-dot menu
                    Box {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clickable { menuExpanded = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options",
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }

                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false },
                            containerColor = MaterialTheme.colorScheme.surface,
                        ) {
                            DropdownMenuItem(
                                text = { Text("Edit") },
                                leadingIcon = {
                                    Icon(Icons.Default.Edit, contentDescription = null)
                                },
                                onClick = {
                                    menuExpanded = false
                                    onEdit()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Delete", color = MaterialTheme.colorScheme.error) },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                },
                                onClick = {
                                    menuExpanded = false
                                    onDelete()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}