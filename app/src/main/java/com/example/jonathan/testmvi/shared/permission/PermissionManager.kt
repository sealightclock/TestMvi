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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
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
    var hasRequested by rememberSaveable { mutableStateOf(false) }

    when {
        permissionState.status.isGranted -> {
            onPermissionGranted()
        }

        permissionState.status.shouldShowRationale -> {
            RationaleUI(message = rationaleMessage) {
                hasRequested = true
                permissionState.launchPermissionRequest()
            }
        }

        hasRequested -> {
            PermanentlyDeniedUI(message = permanentlyDeniedMessage) {
                openAppSettings(context)
            }
        }

        else -> {
            RationaleUI(message = rationaleMessage) {
                hasRequested = true
                permissionState.launchPermissionRequest()
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
@Composable
fun LocationPermissionGate(
    onPermissionGranted: @Composable () -> Unit
) {
    val context = LocalContext.current
    val fineLocationState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val backgroundLocationState = rememberPermissionState(Manifest.permission.ACCESS_BACKGROUND_LOCATION)

    var hasRequestedFine by rememberSaveable { mutableStateOf(false) }
    var hasRequestedBackground by rememberSaveable { mutableStateOf(false) }

    when {
        fineLocationState.status.isGranted && backgroundLocationState.status.isGranted -> {
            onPermissionGranted()
        }

        !fineLocationState.status.isGranted -> {
            when {
                fineLocationState.status.shouldShowRationale -> {
                    RationaleUI("Location permission is required to show your current location and speed.") {
                        hasRequestedFine = true
                        fineLocationState.launchPermissionRequest()
                    }
                }

                hasRequestedFine -> {
                    PermanentlyDeniedUI("Location permission is permanently denied. Please enable it in App Settings.") {
                        openAppSettings(context)
                    }
                }

                else -> {
                    RationaleUI("This feature requires location access.") {
                        hasRequestedFine = true
                        fineLocationState.launchPermissionRequest()
                    }
                }
            }
        }

        !backgroundLocationState.status.isGranted -> {
            when {
                backgroundLocationState.status.shouldShowRationale -> {
                    RationaleUI("Background location is needed to continue tracking when the app is closed.") {
                        hasRequestedBackground = true
                        backgroundLocationState.launchPermissionRequest()
                    }
                }

                hasRequestedBackground -> {
                    PermanentlyDeniedUI("Background location permission is permanently denied. Enable it in App Settings.") {
                        openAppSettings(context)
                    }
                }

                else -> {
                    RationaleUI("To track location in background, grant background access.") {
                        hasRequestedBackground = true
                        backgroundLocationState.launchPermissionRequest()
                    }
                }
            }
        }
    }
}
