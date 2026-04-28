package com.unibo.unievents.ui.screens.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.unievents.ui.NavigationRoute
import com.unibo.unievents.ui.composables.TopBar

@Composable
fun RegistrationScreen(
    state: RegistrationState,
    actions: RegistrationActions,
    navController: NavHostController
) {
    Scaffold(
        topBar = { TopBar(navController, "Register") }
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            // Username text field

            OutlinedTextField(
                leadingIcon = {
                    Icon(Icons.Filled.Person, "User")
                },
                label = { Text("Username") },
                value = state.username,
                onValueChange = actions.updateUsername,
                modifier = Modifier.fillMaxWidth()
            )

            // Badge number text field
            OutlinedTextField(
                leadingIcon = {
                    Icon(Icons.Filled.AccountBox, "Matricola")
                },
                label = { Text("Badge number") },
                value = state.badgeNumber,
                onValueChange = actions.updateBadgeNumber,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // Email field
            OutlinedTextField(
                leadingIcon = {
                    Icon(Icons.Filled.MailOutline, "email")
                },
                label = { Text("Email") },
                value = state.email,
                onValueChange = actions.updateEmail,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                supportingText = {
                    Text("Must end with @studio.unibo.it")
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Password field
            OutlinedTextField(
                leadingIcon = {
                    Icon(Icons.Filled.Lock, "password")
                },
                label = { Text("Password") },
                value = state.password,
                onValueChange = actions.updatePassword,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                supportingText = {
                    Text("Must be at least 8 characters")
                },
                trailingIcon = {
                    IconButton(onClick = actions.toggleShowPassword) {
                        if (state.showPassword) Icon(Icons.Filled.VisibilityOff, "Hide password")
                        else Icon(Icons.Filled.Visibility, "Show password")
                    }
                },
                visualTransformation = if (state.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            // Password confirm field
            OutlinedTextField(
                leadingIcon = {
                    Icon(Icons.Filled.CheckCircle, "Conferma")
                },
                label = { Text("Confirm password") },
                value = state.passwordConfirm,
                onValueChange = actions.updatePasswordConfirm,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (state.showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    actions.confirm()
                    navController.navigate(NavigationRoute.Splash)
                },
                enabled = !state.loading
            ) {
                Text("Register")
            }
        }
    }
}