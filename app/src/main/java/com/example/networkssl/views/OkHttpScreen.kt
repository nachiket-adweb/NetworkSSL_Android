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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Request

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OkHttpScreen() {
//    val context = LocalContext.current
    var json by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "OkHttp Screen") })
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
                    println("::::")
                    println("CALLING API")
                    println("::::")
                    isLoading = true
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val request =
                                Request.Builder()
                                    .url("https://devapi.adwebtech.com/sample/employees.json")
                                    .build()
                            val response = okHttpClient.newCall(request).execute()
                            println("::::")
                            println("RESPONSE GOTTT >> ${response.body}")
                            println("::::")
                            json = response.body?.string() ?: "Error"
                        } catch (e: Exception) {
                            println("::::")
                            println("ERROR GOTTT >> $e")
                            println("::::")
                            json = "Error"
                            isLoading = false
                        } finally {
                            isLoading = false
                        }
                    }
                }) {
                    Text("Fetch OkHTTP")
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
