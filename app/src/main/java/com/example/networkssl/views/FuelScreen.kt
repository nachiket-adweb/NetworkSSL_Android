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
import com.github.kittinunf.fuel.Fuel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FuelScreen() {
    var isLoading by remember { mutableStateOf(false) }
    var jsonResponse by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Fuel Screen") })
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
                    Fuel.get("https://devapi.adwebtech.com/sample/employees.json")
                        .responseString { _, _, result ->
                            result.fold(
                                { data ->
                                    jsonResponse = data
                                    isLoading = false
                                    println("::::")
                                    println("RESPONSE GOTT :: $jsonResponse")
                                    println("::::")
                                },
                                { error ->
                                    jsonResponse = "Error: ${error.message}"
                                    isLoading = false
                                    println("::::")
                                    println("ERROR GOTT :: $jsonResponse")
                                    println("::::")
                                }
                            )
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
