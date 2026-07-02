package com.theseed.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.theseed.app.presentation.auth.AuthScreen
import com.theseed.app.presentation.habits.AddEditHabitScreen
import com.theseed.app.presentation.habits.HabitListScreen
import com.theseed.app.presentation.home.HomeScreen

object Routes {
    const val AUTH = "auth"
    const val HOME = "home"

    const val ADD_HABIT = "add_habit"
}

@Composable
fun NavGraph(startDestination: String = Routes.AUTH) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.AUTH) {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.AUTH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            HabitListScreen(onAddHabit = { navController.navigate(Routes.ADD_HABIT) })
        }

        composable(Routes.ADD_HABIT) {
            AddEditHabitScreen(
                onSaved = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}