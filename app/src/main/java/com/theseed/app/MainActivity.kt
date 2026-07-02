package com.theseed.app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.auth.FirebaseAuth
import com.theseed.app.presentation.navigation.NavGraph
import com.theseed.app.presentation.navigation.Routes
import com.theseed.app.presentation.splash.SplashScreen
import com.theseed.app.presentation.theme.TheSeedTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseAuth.getInstance().currentUser
                ?.getIdToken(true)
                ?.addOnSuccessListener {
                    Log.d("FIREBASE_TOKEN", it.token ?: "")
                }
            TheSeedTheme {
                var showSplash by rememberSaveable { mutableStateOf(true) }
                var startDestination by rememberSaveable { mutableStateOf<String?>(null) }

                if (showSplash) {
                    SplashScreen(
                        onFinished = {
                            val currentUser = FirebaseAuth.getInstance().currentUser
                            startDestination = if (currentUser != null) Routes.HOME else Routes.AUTH
                            showSplash = false
                        }
                    )
                } else {
                    startDestination?.let { destination ->
                        NavGraph(startDestination = destination)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TheSeedTheme {
        Greeting("Android")
    }
}