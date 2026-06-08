package com.unibo.unievents.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun PermissionDeniedAlert(
    onAction: () -> Unit,
    onDismiss: () -> Unit,
    title: @Composable (() -> Unit),
    text: @Composable (() -> Unit)
) {
    AlertDialog(
        title = title,
        text = text,
        confirmButton = {
            TextButton(
                onClick = {
                    onAction()
                    onDismiss()
                }
            ) {
                Text("Concedi")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annulla")
            }
        },
        onDismissRequest = onDismiss

    )
}

@Composable
fun PermissionPermanentlyDeniedSnackbar(
    snackbarHostState: SnackbarHostState,
    onAction: () -> Unit,
    onHide: () -> Unit,
    message: String
) {
    LaunchedEffect(snackbarHostState) {
        val res = snackbarHostState.showSnackbar(
            message,
            "Vai alle impostazioni",
            duration = SnackbarDuration.Long
        )

        if (res == SnackbarResult.ActionPerformed) {
            onAction()
        }
        onHide()
    }
}