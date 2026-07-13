package com.theseed.app.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.theseed.app.presentation.analytics.AnalyticsScreen
import com.theseed.app.presentation.auth.AuthScreen
import com.theseed.app.presentation.habits.AddEditHabitScreen
import com.theseed.app.presentation.habits.HabitListScreen
import com.theseed.app.presentation.home.HomeScreen
import com.theseed.app.presentation.theme.SurfaceWhite
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.LocalFlorist
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.Yard
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.theseed.app.presentation.theme.ForestGreen
import com.theseed.app.presentation.navigation.AppTopBar

object Routes {
    const val AUTH = "auth"
    const val HOME = "home"
    const val HABITS = "habits"
    const val ADD_HABIT = "add_habit"
    const val EDIT_HABIT = "edit_habit/{habitId}"
    const val ANALYTICS = "analytics"

    fun editHabit(habitId: String) = "edit_habit/$habitId"
}

@Composable
fun NavGraph(startDestination: String = Routes.AUTH) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination?.route

    val topBarRoutes = listOf(
        Routes.HOME,
        Routes.HABITS,
        Routes.ANALYTICS
    )

    val pageTitle = when (currentDestination) {
        Routes.HOME -> "Home"
        Routes.HABITS -> "Habits"
        Routes.ANALYTICS -> "Analytics"
        else -> ""
    }

    val bottomNavRoutes = listOf(Routes.HOME, Routes.HABITS, Routes.ANALYTICS)

    val navItemColors = NavigationBarItemDefaults.colors(
        selectedIconColor = ForestGreen,
        selectedTextColor = ForestGreen,
        indicatorColor = Color(0xFFD2DCCC),
        unselectedIconColor = Color.Gray,
        unselectedTextColor = Color.Gray
    )

    Scaffold(
        topBar = {
            if (currentDestination in topBarRoutes) {
                AppTopBar(
                    onBackClick = {
                        if (currentDestination != Routes.HOME) {
                            navController.navigate(Routes.HOME) {
                                popUpTo(Routes.HOME) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (currentDestination in bottomNavRoutes) {
                NavigationBar(containerColor = SurfaceWhite) {
                    NavigationBarItem(
                        selected = currentDestination == Routes.HOME,
                        onClick = { navController.navigate(Routes.HOME) { launchSingleTop = true } },
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Home,
                                contentDescription = "Home"
                            ) },
                        label = { Text("Home") },
                        colors = navItemColors
                    )
                    NavigationBarItem(
                        selected = currentDestination == Routes.HABITS,
                        onClick = { navController.navigate(Routes.HABITS) { launchSingleTop = true } },
                        icon = { Icon(
                            imageVector = Icons.Outlined.Spa,
                            contentDescription = "Habits"
                        ) },
                        label = { Text("Habits") },
                        colors = navItemColors
                    )
                    NavigationBarItem(
                        selected = currentDestination == Routes.ANALYTICS,
                        onClick = { navController.navigate(Routes.ANALYTICS) { launchSingleTop = true } },
                        icon = { Icon(
                            imageVector = Icons.Outlined.BarChart,
                            contentDescription = "Analytics"
                        ) },
                        label = { Text("Analytics") },
                        colors = navItemColors
                    )

                    /*NavigationBarItem(
                        selected = currentDestination == Routes.ANALYTICS,
                        onClick = { navController.navigate(Routes.ANALYTICS) { launchSingleTop = true } },
                        icon = { Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = "Profile"
                        ) },
                        label = { Text("Profile") },
                        colors = navItemColors
                    )*/
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.AUTH) {
                AuthScreen(onAuthSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.AUTH) { inclusive = true }
                    }
                })
            }
            composable(Routes.HOME) { HomeScreen() }
            composable(Routes.HABITS) {
                HabitListScreen(
                    onAddHabit = { navController.navigate(Routes.ADD_HABIT) },
                    onEditHabit = { habitId -> navController.navigate(Routes.editHabit(habitId)) }
                )
            }
            composable(Routes.ADD_HABIT) {
                AddEditHabitScreen(
                    onSaved = { navController.popBackStack() },
                    onCancel = { navController.popBackStack() }
                )
            }
            composable(Routes.ANALYTICS) { AnalyticsScreen() }

            composable(
                route = Routes.EDIT_HABIT,
                arguments = listOf(navArgument("habitId") { type = NavType.StringType })
            ) { backStackEntry ->
                val habitId = backStackEntry.arguments?.getString("habitId")
                AddEditHabitScreen(
                    habitId = habitId,
                    onSaved = { navController.popBackStack() },
                    onCancel = { navController.popBackStack() }
                )
            }
        }
    }
}