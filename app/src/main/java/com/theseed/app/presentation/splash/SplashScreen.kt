package com.theseed.app.presentation.splash

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.*

@Composable
fun SplashScreen(
    onFinished: () -> Unit
) {

    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("theseed_splash.json")
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1
    )

    LaunchedEffect(progress) {
        if (progress >= 0.99f) {
            onFinished()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.fillMaxSize()
        )
    }
}