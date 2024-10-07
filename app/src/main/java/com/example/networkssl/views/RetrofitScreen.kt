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
import androidx.compose.ui.unit.dp
import com.example.networkssl.services.ApiService
import kotlinx.coroutines.launch
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okio.IOException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.SSLHandshakeException

// SSL pinning setup
val serverHost = "devapi.adwebtech.com"
val certificate = "sha256/CT1VeVb1fqiHw3Ra/fuGRM4BLXp6FTiDpmlS7kWvR2E="
//    "9A:38:3C:E0:04:02:BC:17:B3:2B:95:05:9D:FE:76:2C:19:B9:CD:22:6D:C4:DC:A8:02:D3:B5:24:0B:D2:B4:7F"

val certPinner = CertificatePinner.Builder()
    .add(serverHost, certificate)
    .build()

val logInterceptor = HttpLoggingInterceptor()

val okHttpClient = OkHttpClient.Builder()
    .certificatePinner(certPinner)
    .addInterceptor(logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY))
    .build()

// Retrofit Setup
val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://$serverHost")
    .addConverterFactory(GsonConverterFactory.create())
    .client(okHttpClient)
    .build()

val apiService: ApiService = retrofit.create(ApiService::class.java)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RetrofitScreen() {
    var isLoading by remember { mutableStateOf(false) }
    var json by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Retrofit Screen") })
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
                    coroutineScope.launch {
                        val response = apiService.getJsonText()
                        isLoading = false
                        try {
                            if (response.isSuccessful) {
                                println("::::")
                                println("RESPONSE GOTT ::: ${response.body()}")
                                json = response.body()?.toString() ?: "No Data"
                                println("::::")
                            } else {
                                println("::::")
                                println("ERROR GOTT ::: ${response.errorBody()}")
                                json = "Error: ${response.errorBody()?.string()}"
                                println("::::")
                            }
                        } catch (e: SSLHandshakeException) {
                            // Handle SSL handshake errors (e.g., certificate pinning failure)
                            // Notify the user or perform other error handling actions
                            println("::::")
                            println("CATCH SSL GOTT ::: ${response.body()}")
                            json = "CATCH SSL GOTT ::: ${response.body()}"
                            e.printStackTrace()
                            println("::::")
                        } catch (e: IOException) {
                            println("::::")
                            println("CATCH IO GOTT ::: ${response.body()}")
                            json = "CATCH IO GOTT ::: ${response.body()}"
                            e.printStackTrace()
                            println("::::")
                        }

                    }
                }) {
                    Text("Fetch API")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = json)

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


/// SSL Pinning setup

//fun createRetrofitInstanceWithoutOkHttp(context: Context): Retrofit {
//    val sslSocketFactory = getPinnedSSLSocketFactory(context)
//
//    return Retrofit.Builder()
//        .baseUrl("https://your-api-base-url.com/")
//        .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
//        .build()
//}