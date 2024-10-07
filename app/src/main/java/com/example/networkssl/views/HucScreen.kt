package com.example.networkssl.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.networkssl.services.RemoteApi
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HucScreen() {

    val scope = rememberCoroutineScope()
    var json by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val resources = LocalContext.current.resources
    val contextFactory = remember {
        RemoteApi().getSslContext(resources)
    }


//    val sslContext = SSLContext.getDefault()
//    val sslSocketFactory = sslContext.socketFactory

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "HttpUrlConnection") })
        },
        content = { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .safeContentPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Button(onClick = {
                    isLoading = true
                    println("::::")
                    println("CALLING API")
                    println("::::")

                    scope.launch(IO) {
                        try {
                            json = RemoteApi().getMyJsonTextSSL(contextFactory)
//                                json = RemoteApi().getMyJsonText()
                        } catch (e: Exception) {
                            json = e.localizedMessage?.toString() ?: "ERROR"
                            println("Error fetching data: ${e.localizedMessage}")
                        } finally {
                            isLoading = false
                        }
                    }
                }) {
                    Text("Fetch HUC")
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(json)
                }
            }
        },
    )
}

