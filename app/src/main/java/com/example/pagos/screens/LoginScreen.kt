package com.example.pagos.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pagos.R
import com.example.pagos.controllers.AuthController
import com.example.pagos.model.Usuario
import kotlinx.coroutines.launch
import com.example.pagos.model.Grado
import com.example.pagos.model.Rol

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    authController: AuthController,
    onLoginSuccess: (String, Usuario) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showPassword by remember { mutableStateOf(false) }
    var showWelcomeMessage by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(500)
        showWelcomeMessage = true
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF667eea),
                            Color(0xFF764ba2)
                        )
                    )
                )
        ) {
            // Fondo decorativo
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.1f
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Header con logo
                LoginHeader(
                    showWelcomeMessage = showWelcomeMessage,
                    modifier = Modifier.padding(bottom = 48.dp)
                )

                // Card del formulario
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Iniciar Sesión",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Ingresa a tu cuenta de MarketCup",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 32.dp)
                        )

                        // Campo correo
                        OutlinedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                errorMessage = null
                            },
                            label = { Text("Correo electrónico") },
                            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                            placeholder = { Text("ejemplo@marketcup.edu") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth(),
                            isError = errorMessage != null,
                            singleLine = true
                        )

                        Spacer(Modifier.height(16.dp))

                        // Campo contraseña
                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                errorMessage = null
                            },
                            label = { Text("Contraseña") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            trailingIcon = {
                                IconButton(onClick = { showPassword = !showPassword }) {
                                    Icon(
                                        imageVector = if (showPassword) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = "Mostrar u ocultar"
                                    )
                                }
                            },
                            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.fillMaxWidth(),
                            isError = errorMessage != null,
                            singleLine = true
                        )

                        if (errorMessage != null) {
                            Text(
                                text = errorMessage!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            )
                        }

                        Spacer(Modifier.height(24.dp))

                        Button(
                            onClick = {
                                if (validateForm(email, password)) {
                                    isLoading = true
                                    scope.launch {
                                        try {
                                            val result = authController.login(email, password)
                                            isLoading = false
                                            result.onSuccess { token ->
                                                if (token.isNotEmpty()) {
                                                    // Aquí se podría llamar un endpoint /user/me para obtener datos reales del usuario
                                                    val usuarioMock = Usuario(
                                                        idUsuario = 1L,
                                                        primerNombre = "Juan",
                                                        primerApellido = "Pérez",
                                                        carnet = "CP1001",
                                                        correo = email,
                                                        rol = Rol(1L, "ESTUDIANTE"),
                                                        grado = Grado(1L, "10mo Grado A")
                                                    )
                                                    onLoginSuccess(token, usuarioMock)
                                                } else {
                                                    errorMessage = "Token vacío o inválido"
                                                }
                                            }
                                            result.onFailure {
                                                errorMessage = "Credenciales incorrectas o servidor no disponible"
                                            }
                                        } catch (e: Exception) {
                                            isLoading = false
                                            errorMessage = "Error de conexión: ${e.message}"
                                        }
                                    }
                                } else {
                                    errorMessage = "Completa todos los campos correctamente"
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            enabled = !isLoading,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            )
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                                Spacer(Modifier.width(8.dp))
                                Text("Iniciando sesión...")
                            } else {
                                Text("Iniciar Sesión", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                    }
                }
            }
        }
    }
}

@Composable
fun LoginHeader(showWelcomeMessage: Boolean, modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.payments_24px), // Asegúrate de tener el recurso 'logo'
            contentDescription = "Logo MarketCup",
            modifier = Modifier
                .size(80.dp)
                .padding(bottom = 16.dp)
        )
        if (showWelcomeMessage) {
            Text(
                text = "¡Bienvenido a MarketCup!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center
            )
        }
    }
}

// validaciones
private fun validateForm(email: String, password: String): Boolean {
    return email.isNotBlank() &&
            password.isNotBlank() &&
            android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
            password.length >= 6
}
