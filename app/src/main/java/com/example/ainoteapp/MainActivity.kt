package com.example.ainoteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.ainoteapp.navigation.NavGraph
import com.example.ainoteapp.ui.theme.AINoteAppTheme
import com.example.ainoteapp.youi.screens.backColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AINoteAppTheme {
                Scaffold {
                    NavGraph(
                        modifier = Modifier
                            .background(backColor)
                            .padding(it)
                    )

                }
            }
        }
    }
}
