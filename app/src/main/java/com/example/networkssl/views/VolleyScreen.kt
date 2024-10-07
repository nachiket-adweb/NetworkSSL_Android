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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VolleyScreen() {
    var isLoading by remember { mutableStateOf(false) }
    var jsonResponse by remember { mutableStateOf("") }
    val context = LocalContext.current
    val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    val url = "https://devapi.adwebtech.com/sample/employees.json"

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Volley Screen") })
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
                    fetchJson(requestQueue) { response ->
                        jsonResponse = response
                        isLoading = false
                    }

                }) {
                    Text("Fetch API")
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(text = jsonResponse)
                }
            }
        },
    )

}

fun fetchJson(requestQueue: RequestQueue, onResult: (String) -> Unit) {
    val url = "https://devapi.adwebtech.com/sample/employees.json"

    val stringRequest = StringRequest(
        Request.Method.GET, url,
        { response ->
            println("::::")
            println("RESPONSE GOTTT >> $response")
            println("::::")
            onResult(response)
        },
        {
            println("::::")
            println("RESPONSE GOTTT >> ${it.message}")
            println("::::")
            onResult("Error: ${it.message}")
        },
    )

    requestQueue.add(stringRequest)
}
