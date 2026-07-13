package com.theseed.app.presentation.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theseed.app.presentation.theme.BackgroundOffWhite
import com.theseed.app.presentation.theme.ForestGreen
import com.theseed.app.presentation.theme.SurfaceWhite
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.theseed.app.domain.model.GridDay
import com.theseed.app.domain.model.HabitDifficulty
import com.theseed.app.domain.model.TodayHabit
import com.theseed.app.domain.model.WeeklyDay
import com.theseed.app.presentation.theme.InsightGreen
import com.theseed.app.presentation.theme.color

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadDashboard()
    }

    val score = state.disciplineScore?.score ?: 0
    val consistency = state.disciplineScore?.consistencyScore ?: 0
    val growthStage = state.disciplineScore?.growthStage ?: "Seed"
    val displayName = state.dashboard?.displayName ?: "there"
    val currentStreak = state.dashboard?.currentStreak ?: 0
    val weekly = state.dashboard?.weekly ?: emptyList()
    val grid = state.dashboard?.grid ?: emptyList()
    val todayHabits = state.dashboard?.todayHabits ?: emptyList()
    val completedToday = state.dashboard?.completedToday ?: 0
    val totalHabits = state.dashboard?.totalHabits ?: 0

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundOffWhite),
        contentPadding = PaddingValues(
            start = 20.dp,
            end = 20.dp,
            top = 0.dp,
            bottom = 32.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            GreetingSection(displayName)

            Spacer(modifier = Modifier.height(8.dp))

            DisciplineScoreCard(score, growthStage)
        }

        item {
            StatsRow(
                consistency = consistency,
                streak = currentStreak
            )
        }

        item {
            InsightCard()
        }

        item {
            WeeklyConsistencyCard(weekly)
        }

        item {
            GrowthActivitySection(grid)
        }

        item {
            TodayHabitsSection(
                habits = todayHabits,
                completedToday = completedToday,
                totalHabits = totalHabits
            )
        }
    }
}

@Composable
private fun GreetingSection(displayName: String) {

    Column {

        Text(
            text = "Good morning, $displayName",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Your resilience is growing. Keep nurturing your foundation.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = .75f),
            lineHeight = 28.sp
        )
    }
}

@Composable
private fun DisciplineScoreCard(
    score: Int, growthStage: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceWhite
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 28.dp,
                    bottom = 24.dp,
                    start = 20.dp,
                    end = 20.dp
                ),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            ScoreRing(score)

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Discipline Score",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(ForestGreen.copy(alpha = .12f))
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {

                Text(
                    text = "$growthStage Stage",
                    color = ForestGreen,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun ScoreRing(
    score: Int
) {

    Box(
        modifier = Modifier.size(110.dp),
        contentAlignment = Alignment.Center
    ) {

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {

            val stroke = 10.dp.toPx()

            drawArc(
                color = ForestGreen.copy(alpha = .12f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(
                    width = stroke,
                    cap = StrokeCap.Round
                )
            )

            drawArc(
                color = ForestGreen,
                startAngle = -90f,
                sweepAngle = score / 100f * 360f,
                useCenter = false,
                style = Stroke(
                    width = stroke,
                    cap = StrokeCap.Round
                )
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(ForestGreen),

                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Eco,
                    contentDescription = null,
                    tint = SurfaceWhite,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = score.toString(),
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = ForestGreen
            )
        }
    }
}

@Composable
private fun StatsRow(
    consistency: Int,
    streak: Int
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.TrendingUp,
            iconBackground = Color(0xFFE8F5E9),
            iconTint = ForestGreen,
            title = "Consistency",
            value = "$consistency%"
        )

        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.LocalFireDepartment,
            iconBackground = Color(0xFFF5E9DE),
            iconTint = Color(0xFF8D5A2B),
            title = "Current Streak",
            value = "$streak Days"
        )
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconBackground: Color,
    iconTint: Color,
    title: String,
    value: String
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceWhite
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(iconBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = ForestGreen,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun InsightCard() {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceWhite
        )
    ) {

        Column(
            modifier = Modifier.padding(22.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(InsightGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Lightbulb,
                        contentDescription = "Insight",
                        tint = ForestGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Today's Insight",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "You perform best after 8 PM. Consider shifting focus-heavy tasks to your evening blocks.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = .80f),
                lineHeight = 26.sp
            )
        }
    }
}

@Composable
private fun WeeklyConsistencyCard(weekly: List<WeeklyDay>) {

    val labels = listOf("M","T","W","T","F","S","S")
    val values = if (weekly.isEmpty()) {
        List(7) { 0f }
    } else {
        weekly.map { it.ratio }
    }

    val avgCompletion = if (weekly.isEmpty()) 0
    else (weekly.map { it.ratio }.average() * 100).toInt()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceWhite
        )
    ) {

        Column(
            modifier = Modifier.padding(22.dp)
        ) {

            Text(
                text = "Weekly Consistency",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {

                values.forEachIndexed { index, value ->

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {

                        Box(
                            modifier = Modifier
                                .width(18.dp)
                                .height((value * 100).dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(ForestGreen)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = labels[index],
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "$avgCompletion% Average Completion",
                color = ForestGreen,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun GrowthActivitySection(grid: List<GridDay>) {

    val rows = grid.chunked(7)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceWhite
        )
    ) {

        Column(
            modifier = Modifier.padding(22.dp)
        ) {

            Text(
                text = "Growth Activity",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            rows.forEach { row ->

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 8.dp,
                        alignment = Alignment.CenterHorizontally
                    )
                ) {
                    row.forEach { day ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(24.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(activityColor(day.level))
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

private fun activityColor(level: Int): Color {

    return when(level){

        0 -> Color(0xFFEDEFEA)

        1 -> Color(0xFFDDEAD8)

        2 -> Color(0xFFBCD4B4)

        3 -> Color(0xFF7FA06F)

        else -> ForestGreen
    }
}

@Composable
private fun TodayHabitsSection(
    habits: List<TodayHabit>,
    completedToday: Int,
    totalHabits: Int
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = SurfaceWhite
        )
    ) {

        Column(
            modifier = Modifier.padding(22.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Today's Habits",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "$completedToday / $totalHabits Completed",
                    color = ForestGreen,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (habits.isEmpty()) {
                Text("No habits yet — add some from the Habits tab")
            }
            else {
                habits.forEach {  habit ->

                    HabitRow(
                        HabitUi(
                            title = habit.title,
                            duration = habit.difficulty,   // use difficulty as label for now
                            category = habit.category,
                            completed = habit.completedToday  // ← real completion
                        )
                    )

                    if (habit != habits.last())
                    HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = .2f)
                        )
                }
            }
        }
    }
}

private data class HabitUi(
    val title: String,
    val duration: String,
    val category: String,
    val completed: Boolean
)

@Composable
private fun HabitRow(
    habit: HabitUi
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(
                    if (habit.completed)
                        ForestGreen
                    else
                        Color.Transparent
                )
                .border(
                    2.dp,
                    ForestGreen,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {

            if (habit.completed) {

                Text(
                    "✓",
                    color = SurfaceWhite,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = habit.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = habit.category,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {

            Text(
                text = habit.duration,
                color = when (habit.duration.lowercase()) {
                    "easy" -> HabitDifficulty.EASY.color()
                    "medium" -> HabitDifficulty.MEDIUM.color()
                    "hard" -> HabitDifficulty.HARD.color()
                    else -> ForestGreen
                },
                fontWeight = FontWeight.Medium
            )
        }
    }
}
