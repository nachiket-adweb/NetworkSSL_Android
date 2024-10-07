package com.example.networkssl

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.networkssl.ui.theme.NetworkSSLTheme
import com.example.networkssl.views.FuelScreen
import com.example.networkssl.views.HucScreen
import com.example.networkssl.views.MainScreen
import com.example.networkssl.views.OkHttpScreen
import com.example.networkssl.views.RetrofitScreen
import com.example.networkssl.views.VolleyScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NetworkSSLTheme {
                val navController = rememberNavController()
                NavHost(
                    navController,
                    startDestination = "main"
                ) {
                    composable("main") { MainScreen(navController) }
                    composable("huc") { HucScreen() }
                    composable("retrofit") { RetrofitScreen() }
                    composable("volley") { VolleyScreen() }
                    composable("fuel") { FuelScreen() }
                    composable("okhttp") { OkHttpScreen() }
                }
            }
        }
    }
}


// https://devapi.adwebtech.com/sample/employees.json
