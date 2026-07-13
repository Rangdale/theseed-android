package com.theseed.app.presentation.analytics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.outlined.MilitaryTech
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.theseed.app.domain.model.AnalyticsDashboard
import com.theseed.app.domain.model.PeakTime
import com.theseed.app.domain.model.TrendPoint
import com.theseed.app.presentation.theme.BackgroundOffWhite
import com.theseed.app.presentation.theme.ForestGreen
import com.theseed.app.presentation.theme.SurfaceWhite

@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadAnalytics(state.period)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundOffWhite),
        contentPadding = PaddingValues(
            start = 20.dp, end = 20.dp,
            top = 0.dp, bottom = 32.dp
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Header
        item {
            Text(
                text = "Analytics",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Track your discipline patterns",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f)
            )
        }

        // Period toggle
        item {
            PeriodToggle(
                selected = state.period,
                onSelect = { viewModel.setPeriod(it) }
            )
        }

        if (state.isLoading) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = ForestGreen)
                }
            }
        } else {
            state.data?.let { data ->

                // 4 stat cards
                item { StatsGrid(data) }

                // Consistency trend line chart
                item { ConsistencyTrendCard(data.trend) }

                // Streak insights
                item { StreakInsightsCard(data) }

                // Completion statistics
                item { CompletionStatsCard(data) }

                // Peak performance bar chart
                item { PeakPerformanceCard(data.peakTimes) }
            }
        }
    }
}

@Composable
private fun PeriodToggle(
    selected: String,
    onSelect: (String) -> Unit
) {

    Row(
        modifier = Modifier
            .wrapContentWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFF1F2EE))
            .padding(3.dp)
    ) {

        listOf("weekly", "monthly").forEach { period ->

            Box(
                modifier = Modifier
                    .width(112.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        if (selected == period)
                            SurfaceWhite
                        else
                            Color.Transparent
                    )
                    .clickable {
                        onSelect(period)
                    },
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = period.replaceFirstChar { it.uppercase() },
                    fontSize = 15.sp,
                    fontWeight = if (selected == period)
                        FontWeight.SemiBold
                    else
                        FontWeight.Normal,
                    color = if (selected == period)
                        ForestGreen
                    else
                        Color.Gray
                )
            }
        }
    }
}

@Composable
private fun StatsGrid(data: AnalyticsDashboard) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatCard(
                modifier = Modifier.weight(1f),
                label = "Discipline",
                value = "${data.score}",
                sub = if (data.scoreDelta >= 0) "+${data.scoreDelta}" else "${data.scoreDelta}",
                subColor = if (data.scoreDelta >= 0) ForestGreen else MaterialTheme.colorScheme.error
            )
            StatCard(
                modifier = Modifier.weight(1f),
                label = "Consistency",
                value = "${data.consistencyScore}%",
                sub = "Past ${if (data.period == "weekly") "7" else "30"} days"
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatCard(
                modifier = Modifier.weight(1f),
                label = "Streak",
                value = "${data.currentStreak}d",
                sub = "Personal best"
            )
            StatCard(
                modifier = Modifier.weight(1f),
                label = "Deep Work",
                value = "${data.deepWorkHours}h",
                sub = "This ${if (data.period == "weekly") "week" else "month"}"
            )
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    sub: String,
    subColor: Color = Color.Gray
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Text(
                text = value,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(text = sub, fontSize = 12.sp, color = subColor)
        }
    }
}

@Composable
private fun ConsistencyTrendCard(trend: List<TrendPoint>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
    ) {

        Column(
            modifier = Modifier.padding(22.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    "Consistency Trend",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Completion %",
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            ConsistencyTrendGraph(values = trend.map { it.ratio.toFloat() })
        }
    }
}

@Composable
private fun ConsistencyTrendGraph(
    values: List<Float>
) {

    val labels = listOf(
        "Mon",
        "Tue",
        "Wed",
        "Thu",
        "Fri",
        "Sat",
        "Sun"
    )

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {

            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {

                if (values.size < 2) return@Canvas

                val smoothedValues = values.mapIndexed { index, _ ->
                    val previous = values.getOrElse(index - 1) { values[index] }
                    val current = values[index]
                    val next = values.getOrElse(index + 1) { values[index] }

                    (previous + current + next) / 3f
                }

                val topPadding = 35.dp.toPx()
                val bottomPadding = 30.dp.toPx()

                val width = size.width
                val height = size.height

                val horizontalPadding = 20.dp.toPx()

                val graphWidth = width - horizontalPadding * 2

                val spacing = graphWidth / (smoothedValues.size - 1)

                fun y(value: Float): Float {

                val v = value.coerceIn(0f, 1f)

                return (height - bottomPadding) -
                        v * (height - topPadding - bottomPadding)
            }

                val linePath = Path()

                linePath.moveTo(
                    horizontalPadding,
                    y(smoothedValues.first())
                )

                for (i in 0 until smoothedValues.lastIndex) {

                    val x1 = horizontalPadding + spacing * i
                    val y1 = y(smoothedValues[i])

                    val x2 = horizontalPadding + spacing * (i + 1)
                    val y2 = y(smoothedValues[i + 1])

                    val smoothness = 0.30f

                    val cx1 = x1 + spacing * smoothness
                    val cx2 = x2 - spacing * smoothness

                    linePath.cubicTo(
                        cx1,
                        y1,
                        cx2,
                        y2,
                        x2,
                        y2
                    )
                }

                val fillPath = Path()

                fillPath.addPath(linePath)

                fillPath.lineTo(
                    width - horizontalPadding,
                    height
                )

                fillPath.lineTo(
                    horizontalPadding,
                    height
                )

                fillPath.close()

                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        listOf(
                            ForestGreen.copy(alpha = 0.16f),
                            Color.Transparent
                        )
                    )
                )

                drawPath(
                    path = linePath,
                    color = ForestGreen,
                    style = Stroke(
                        width = 5.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            labels.forEach {

                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = .55f),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun StreakInsightsCard(data: AnalyticsDashboard) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Text(
                text = "STREAK INSIGHTS",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = ForestGreen,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            StreakRow(
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.MilitaryTech,
                        contentDescription = null,
                        tint = ForestGreen
                    )
                },
                label = "Longest Streak",
                value = "${data.longestStreak} Days"
            )

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            StreakRow(
                icon = { Icon(Icons.Default.LocalFireDepartment, null, tint = ForestGreen) },
                label = "Current Streak",
                value = "${data.currentStreak} Days"
            )

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            StreakRow(
                icon = { Icon(Icons.Default.ShowChart, null, tint = ForestGreen) },
                label = "Streak Stability",
                value = "${data.streakStability}%"
            )
        }
    }
}

@Composable
private fun StreakRow(
    icon: @Composable () -> Unit,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(ForestGreen.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) { icon() }
            Spacer(modifier = Modifier.width(12.dp))
            Text(label, style = MaterialTheme.typography.bodyLarge)
        }
        Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
private fun CompletionStatsCard(data: AnalyticsDashboard) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Text(
                text = "COMPLETION STATISTICS",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = ForestGreen,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Total completion progress bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Completion", fontWeight = FontWeight.Medium)
                Text("${data.completionRate}%", color = ForestGreen, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { data.completionRate / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = ForestGreen,
                trackColor = ForestGreen.copy(alpha = 0.15f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Best habit + missed rate
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(80.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = BackgroundOffWhite)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Best Habit", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                        Text(
                            text = data.bestHabit?.title ?: "—",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${data.bestHabit?.completionRate ?: 0}% completion",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(80.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = BackgroundOffWhite)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Missed Rate", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                        Text(
                            text = "${data.missedRate}%",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PeakPerformanceCard(peakTimes: List<PeakTime>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite)
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "PEAK PERFORMANCE TIMES",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = ForestGreen,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (peakTimes.isNotEmpty() && peakTimes.any { it.count > 0 }) {
                val peak = peakTimes.maxByOrNull { it.count }

                Text(
                    text = "⚡",
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = when (peak?.label) {
                        "Morning" -> "6 AM – 12 PM"
                        "Afternoon" -> "12 PM – 5 PM"
                        "Evening" -> "5 PM – 9 PM"
                        "Night" -> "9 PM – 12 AM"
                        else -> ""
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = ForestGreen
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "You are most likely to complete habits in the ${peak?.label?.lowercase()}.",
                    modifier = Modifier.fillMaxWidth(0.82f),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = .75f)
                )

                Spacer(modifier = Modifier.height(22.dp))

                val maxRatio = peakTimes.maxOf { it.ratio }.coerceAtLeast(0.01F)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    horizontalArrangement = Arrangement.spacedBy(
                        10.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.Bottom
                ) {

                    peakTimes.forEach { pt ->

                        val height = ((pt.ratio / maxRatio) * 95).coerceAtLeast(12.0F).dp

                        val color =
                            when {

                                pt == peak ->
                                    ForestGreen

                                pt.ratio >= maxRatio * 0.7 ->
                                    Color(0xFF7E8F77)

                                else ->
                                    Color(0xFFE3E7E1)
                            }

                        Box(
                            modifier = Modifier
                                .width(56.dp)
                                .height(height)
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 8.dp,
                                        topEnd = 8.dp
                                    )
                                )
                                .background(color)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        10.dp,
                        Alignment.CenterHorizontally
                    )
                ) {

                    peakTimes.forEach { pt ->

                        Text(
                            text = pt.label.take(3),
                            modifier = Modifier.width(56.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = .55f)
                        )
                    }
                }
            } else {
                Text(
                    text = "Complete some habits to see your peak performance times",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}