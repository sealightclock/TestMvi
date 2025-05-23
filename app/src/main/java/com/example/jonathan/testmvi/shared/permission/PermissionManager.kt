package com.example.jonathan.testmvi.shared.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionGate(
    permission: String,
    rationaleMessage: String,
    permanentlyDeniedMessage: String,
    onPermissionGranted: @Composable () -> Unit
) {
    val context = LocalContext.current
    val permissionState = rememberPermissionState(permission)

    when {
        permissionState.status.isGranted -> {
            onPermissionGranted()
        }

        permissionState.status.shouldShowRationale -> {
            RationaleUI(message = rationaleMessage) {
                permissionState.launchPermissionRequest()
            }
        }

        permissionState.status is PermissionStatus.Denied -> {
            if (permissionState.status.isPermanentlyDenied()) {
                PermanentlyDeniedUI(message = permanentlyDeniedMessage) {
                    openAppSettings(context)
                }
            } else {
                RationaleUI(message = rationaleMessage) {
                    permissionState.launchPermissionRequest()
                }
            }
        }
    }
}

@Composable
private fun RationaleUI(message: String, onRequest: () -> Unit) {
    Column(Modifier.padding(16.dp)) {
        Text(message)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRequest) {
            Text("Grant Permission")
        }
    }
}

@Composable
private fun PermanentlyDeniedUI(message: String, onOpenSettings: () -> Unit) {
    Column(Modifier.padding(16.dp)) {
        Text(message)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onOpenSettings) {
            Text("Open App Settings")
        }
    }
}

private fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}

@OptIn(ExperimentalPermissionsApi::class)
private fun PermissionStatus.isPermanentlyDenied(): Boolean {
    return this is PermissionStatus.Denied && !this.shouldShowRationale
}

// Special handling for location permissions:

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionGate(
    onPermissionGranted: @Composable () -> Unit
) {
    val context = LocalContext.current
    val fineLocationState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val backgroundLocationState = rememberPermissionState(Manifest.permission.ACCESS_BACKGROUND_LOCATION)

    val isAndroidQOrLater = remember {
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q
    }

    when {
        fineLocationState.status.isGranted && (!isAndroidQOrLater || backgroundLocationState.status.isGranted) -> {
            // ✅ All necessary permissions granted
            onPermissionGranted()
        }

        !fineLocationState.status.isGranted -> {
            // Step 1: Request foreground permission
            PermissionPrompt(
                message = "Location permission is required to show your current location and speed.",
                onRequest = { fineLocationState.launchPermissionRequest() },
                permanentlyDenied = fineLocationState.status.isPermanentlyDenied(),
                onOpenSettings = { openAppSettings(context) }
            )
        }

        isAndroidQOrLater && !backgroundLocationState.status.isGranted -> {
            // Step 2: Request background permission
            PermissionPrompt(
                message = "Background location is needed to continue tracking when the app is closed.",
                onRequest = { backgroundLocationState.launchPermissionRequest() },
                permanentlyDenied = backgroundLocationState.status.isPermanentlyDenied(),
                onOpenSettings = { openAppSettings(context) }
            )
        }
    }
}

@Composable
private fun PermissionPrompt(
    message: String,
    onRequest: () -> Unit,
    permanentlyDenied: Boolean,
    onOpenSettings: () -> Unit
) {
    Column(Modifier.padding(16.dp)) {
        Text(message)
        Spacer(modifier = Modifier.height(8.dp))
        if (permanentlyDenied) {
            Button(onClick = onOpenSettings) {
                Text("Open App Settings")
            }
        } else {
            Button(onClick = onRequest) {
                Text("Grant Permission")
            }
        }
    }
}

