package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.ConfirmationModal
import com.example.ui.components.InputField
import com.example.ui.components.PrimaryButton
import com.example.ui.components.SecondaryButton
import com.example.ui.theme.*

@Composable
fun LoginScreen(
    onLoginSuccess: (username: String, password: String) -> Boolean
) {
    var username by remember { mutableStateOf("inspector.demo") }
    var password by remember { mutableStateOf("123456") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    var showForgotDialog by remember { mutableStateOf(false) }
    var showAdminContactDialog by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    fun performLogin() {
        focusManager.clearFocus()
        if (username.isBlank()) {
            errorMessage = "Please enter your authorized username or Employee ID."
            return
        }
        if (password.isBlank()) {
            errorMessage = "Please enter your secure password."
            return
        }
        if (password.length < 6) {
            errorMessage = "Password must be at least 6 characters."
            return
        }

        isLoading = true
        errorMessage = null
        val success = onLoginSuccess(username.trim(), password)
        if (!success) {
            isLoading = false
            errorMessage = "Invalid credentials. Use inspector.demo / 123456 for demo access."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GovBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Government Navy Top Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(GovNavyDark)
                .padding(vertical = 24.dp, horizontal = 20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Emblem
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.12f))
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_gov_emblem),
                        contentDescription = "Government Emblem",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Government Inspection & Monitoring System",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                )

                Text(
                    text = "Authorized Field Personnel Portal",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFFFF9933),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }

        // Form Container
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, GovBorderLight, RoundedCornerShape(8.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Authorized Officer Login",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = GovNavyPrimary,
                            fontSize = 17.sp
                        )
                    )
                    Text(
                        text = "Enter official credentials issued by DOJS Directorate",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = GovTextSecondary,
                            fontSize = 12.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (errorMessage != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(GovRedLight)
                                .border(1.dp, GovRedDanger.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                .padding(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.ErrorOutline,
                                    contentDescription = null,
                                    tint = GovRedDanger,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = errorMessage ?: "",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = GovRedDanger,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    // Username Field
                    InputField(
                        value = username,
                        onValueChange = {
                            username = it
                            errorMessage = null
                        },
                        label = "Username / Inspector ID",
                        placeholder = "e.g. inspector.demo or GOI-DOJS-8924",
                        leadingIcon = Icons.Default.Badge,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Password Field
                    InputField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorMessage = null
                        },
                        label = "Secure Password",
                        placeholder = "Enter your 6-digit password",
                        leadingIcon = Icons.Default.Lock,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                    tint = GovTextMuted,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { performLogin() })
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Remember Me & Forgot Password
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { rememberMe = !rememberMe }
                        ) {
                            Checkbox(
                                checked = rememberMe,
                                onCheckedChange = { rememberMe = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = GovNavyPrimary,
                                    checkmarkColor = Color.White
                                ),
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Remember me",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = GovTextSecondary,
                                    fontSize = 12.sp
                                )
                            )
                        }

                        TextButton(
                            onClick = { showForgotDialog = true },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                text = "Forgot Password?",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = GovNavyPrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Login Button
                    PrimaryButton(
                        text = if (isLoading) "Authenticating..." else "Secure Login",
                        icon = Icons.Default.VpnKey,
                        onClick = { performLogin() },
                        enabled = !isLoading
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Demo Credentials Helper Pill
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(GovNavyContainer)
                            .border(1.dp, GovNavyPrimary.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                            .clickable {
                                username = "inspector.demo"
                                password = "123456"
                                errorMessage = null
                            }
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.TouchApp,
                                contentDescription = null,
                                tint = GovNavyPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Tap to Auto-fill Demo Credentials (inspector.demo / 123456)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = GovNavyPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Secondary Actions & Help
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(onClick = { showAdminContactDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.SupportAgent,
                        contentDescription = null,
                        tint = GovNavyPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Contact DOJS Helpdesk / Administrator",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = GovNavyPrimary,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Government Disclaimer Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.White)
                    .border(1.dp, GovBorderLight, RoundedCornerShape(6.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = GovNavyPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Authorized Government Inspection System",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = GovNavyPrimary,
                                fontSize = 11.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Access is strictly restricted to authorized statutory personnel. All actions, logins, geotags and inspection records are digitally logged and audited under the Information Technology Act.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = GovTextMuted,
                                fontSize = 10.sp,
                                lineHeight = 14.sp
                            )
                        )
                    }
                }
            }
        }
    }

    // Forgot Password Modal
    ConfirmationModal(
        show = showForgotDialog,
        title = "Password Recovery",
        message = "For security reasons, password resets must be authorized by your Zonal System Administrator. Please contact helpdesk@dojs.gov.in with your Official Employee ID.",
        confirmText = "Got it",
        cancelText = "Close",
        onConfirm = { showForgotDialog = false },
        onDismiss = { showForgotDialog = false }
    )

    // Admin Contact Modal
    ConfirmationModal(
        show = showAdminContactDialog,
        title = "DOJS Technical Helpdesk",
        message = "National Directorate Support Line:\nToll-Free: 1800-11-DOJS (3657)\nEmail: support.gims@dojs.gov.in\nOperating Hours: 08:00 AM - 08:00 PM IST",
        confirmText = "Acknowledge",
        cancelText = "Dismiss",
        onConfirm = { showAdminContactDialog = false },
        onDismiss = { showAdminContactDialog = false }
    )
}
