package com.example.jonathan.testmvi.shared.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.jonathan.testmvi.shared.preferences.PermissionPreferences
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionGate(
    onPermissionGranted: @Composable () -> Unit
) {
    val context = LocalContext.current
    val fineLocationState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val coroutineScope = rememberCoroutineScope()

    val hasRequestedFineLocation by produceState(initialValue = false, context) {
        PermissionPreferences.hasRequestedFineLocation(context).collect { value = it }
    }

    when {
        fineLocationState.status.isGranted -> {
            // Permission is granted: Proceed to show location UI
            onPermissionGranted()
        }

        fineLocationState.status.shouldShowRationale -> {
            // User previously denied, but can still grant
            RationaleUI("Location permission is required to show your current location and speed.") {
                coroutineScope.launch {
                    PermissionPreferences.markFineLocationRequested(context)
                    fineLocationState.launchPermissionRequest()
                }
            }
        }

        !hasRequestedFineLocation -> {
            // First time asking
            RationaleUI("This feature requires location access.") {
                coroutineScope.launch {
                    PermissionPreferences.markFineLocationRequested(context)
                    fineLocationState.launchPermissionRequest()
                }
            }
        }

        else -> {
            // Permission permanently denied
            PermanentlyDeniedUI("Location permission is permanently denied. Please enable it in App Settings.") {
                openAppSettings(context)
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
