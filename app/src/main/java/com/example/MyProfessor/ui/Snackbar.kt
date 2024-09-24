package com.example.MyProfessor.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Snackbar(snackbarMessage: String){
    // State to control the Snackbar visibility
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Trigger showing the Snackbar
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = snackbarMessage,
                duration = SnackbarDuration.Short  // Short duration is approximately 2 seconds
            )
        }
    }

//    // Scaffold to show the Snackbar
//    Scaffold(
//        modifier = Modifier.fillMaxSize(),
//        snackbarHost = { SnackbarHost(snackbarHostState) }
//    ) {
//
//    }
}
