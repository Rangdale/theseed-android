package com.theseed.app.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.theseed.app.presentation.auth.AuthViewModel

@Composable
fun HomeScreen(
    viewModel: AuthViewModel = hiltViewModel()
) {
    val backendVerified by viewModel.backendVerified.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Home Screen", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        // Test 1 — logged in user hits /auth/me
        Button(onClick = { viewModel.verifyWithBackend() }) {
            Text("Test: Hit /auth/me with token")
        }

        Spacer(modifier = Modifier.height(16.dp))

        backendVerified?.let {
            Text(it, style = MaterialTheme.typography.bodyMedium)
        }
    }
}