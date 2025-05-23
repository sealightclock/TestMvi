package com.example.jonathan.testmvi.shared.permission

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.*

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
