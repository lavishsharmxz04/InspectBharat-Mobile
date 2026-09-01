package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.data.repository.InspectionRepository
import com.example.ui.navigation.AppNavigation
import com.example.ui.theme.GovBackground
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val repository = remember { InspectionRepository() }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = GovBackground
                ) {
                    AppNavigation(repository = repository)
                }
            }
        }
    }
}

