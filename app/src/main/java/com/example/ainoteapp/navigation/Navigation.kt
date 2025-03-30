package com.example.ainoteapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ainoteapp.youi.screens.AllNotesScreen
import com.example.ainoteapp.youi.screens.EditScreen
import kotlinx.serialization.Serializable


@Serializable
data class EditScreen(val id: String? = null)

@Serializable
object AllNotesScreen

@Composable
fun NavGraph(modifier: Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AllNotesScreen) {
        composable<AllNotesScreen> {
            AllNotesScreen(modifier = modifier, navController = navController)
        }
        composable<EditScreen> {
            val id = it.arguments?.getString("id")
            EditScreen(modifier = modifier, navController = navController, id = id)
        }
    }
}