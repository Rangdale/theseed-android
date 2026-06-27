package com.theseed.app.presentation.auth

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.theseed.app.R
import com.theseed.app.presentation.theme.ForestGreen
import com.theseed.app.presentation.theme.BackgroundOffWhite
import com.theseed.app.presentation.theme.SurfaceWhite
import com.theseed.app.presentation.theme.CharcoalText
import com.theseed.app.presentation.theme.BorderGray
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Tab state: 0 = Login, 1 = Sign Up
    var selectedTab by remember { mutableIntStateOf(0) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Navigate when authenticated
    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) onAuthSuccess()
    }

    // Google Sign-In launcher
    val googleSignInClient = remember {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account.idToken?.let { viewModel.signInWithGoogle(it) }
            } catch (e: ApiException) {
                // handled by uiState.error
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .background(BackgroundOffWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 24.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val screenHeight = LocalConfiguration.current.screenHeightDp.dp

            Spacer(
                Modifier.height(screenHeight * 0.07f)
            )

            // App icon + title
            val screenWidth = LocalConfiguration.current.screenWidthDp.dp
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "TheSeed Logo",
                modifier = Modifier.size(screenWidth * 0.18f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "TheSeed",
                fontSize = 52.sp,
                fontWeight = FontWeight.Bold,
                color = ForestGreen
            )

            Text(
                text = "Cultivate your resilience.",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Tab switcher card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Column(modifier = Modifier.padding(32.dp)) {

                    // Log In / Sign Up tabs
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF0F0EC), RoundedCornerShape(12.dp))
                            .padding(7.dp)
                    ) {
                        listOf("Log In", "Sign Up").forEachIndexed { index, label ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        if (selectedTab == index) Color.White
                                        else Color.Transparent,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .padding(vertical = 1.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                TextButton(onClick = { selectedTab = index }) {
                                    Text(
                                        text = label,
                                        fontSize = 18.sp,
                                        color = if (selectedTab == index) ForestGreen
                                        else Color.Gray,
                                        fontWeight = if (selectedTab == index)
                                            FontWeight.SemiBold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Email field
                    Text("Email Address", fontSize = 15.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("you@example.com", color = Color.LightGray) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(android.R.drawable.ic_dialog_email),
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password field
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Password",
                            fontSize = 15.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        if (selectedTab == 0) {
                            Text(
                                text = "Forgot?",
                                fontSize = 15.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.clickable {
                                    // Forgot password action
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(android.R.drawable.ic_lock_idle_lock),
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = { passwordVisible = !passwordVisible }
                            ) {
                                Icon(
                                    imageVector = if (passwordVisible)
                                        Icons.Outlined.Visibility
                                    else
                                        Icons.Outlined.VisibilityOff,
                                    contentDescription = if (passwordVisible)
                                        "Hide password"
                                    else
                                        "Show password",
                                    tint = Color.Gray
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible)
                            VisualTransformation.None
                        else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Error message
                    uiState.error?.let { error ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(error, color = Color.Red, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Primary button
                    Button(
                        onClick = {
                            if (selectedTab == 0) viewModel.signInWithEmail(email, password)
                            else viewModel.signUpWithEmail(email, password)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForestGreen
                        ),
                        shape = RoundedCornerShape(14.dp),
                        enabled = !uiState.isLoading
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = if (selectedTab == 0) "Continue to Dashboard →"
                                else "Create Account →",
                                fontSize = 18.sp,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Divider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f))
                        Text(
                            "  or connect with  ",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        HorizontalDivider(modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Google Sign-In button
                    OutlinedButton(
                        onClick = { googleLauncher.launch(googleSignInClient.signInIntent) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        enabled = !uiState.isLoading
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.google_logo),
                            contentDescription = "Google",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Continue with Google", color = Color.DarkGray, fontSize = 18.sp)
                    }
                }
            }
        }
    }
}