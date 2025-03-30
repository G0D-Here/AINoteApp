package com.example.ainoteapp.youi.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.ainoteapp.data.local.TaskEntity
import com.example.ainoteapp.navigation.EditScreen
import com.example.ainoteapp.youi.NotesViewModel
import com.example.ainoteapp.youi.components.CustomTextField
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

val backColor = Color(0xFFe9ddd4)
val textColor = Color(0xFF122620)
val titleColor = Color(0xFF900020)
val cardColor = Color(0xFFE3EAB8)
val buttonColor = Color(0xFFA79277)

@Composable
fun AllNotesScreen(
    modifier: Modifier,
    vm: NotesViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val allNotes by vm.allNotes.collectAsState()
    val allQueries by vm.result.collectAsState()
    var query by remember { mutableStateOf("") }
    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    date.timeZone = TimeZone.getDefault()
    Box(
        Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier
                .fillMaxSize()
                .background(backColor)
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Text(
                text = "NoteHere",
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 15.dp, bottom = 0.dp),
                style = MaterialTheme.typography.titleLarge,
                fontSize = 30.sp,
                fontFamily = FontFamily.Serif,
                color = titleColor
            )

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomTextField(
                    value = query,
                    onValueChange = {
                        query = it
                        vm.getResults(it.trim())
                    },
                    label = "Search notes",
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .weight(1f),
                    leadingIcon = Icons.Outlined.Search,
                    colorIt = TextFieldDefaults.colors(
                        unfocusedTextColor = textColor,
                        focusedTextColor = Color.White,
                        unfocusedContainerColor = backColor,
                        focusedContainerColor = backColor,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                    )
                )
                if (query.isNotEmpty())
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        null,
                        Modifier
                            .size(25.dp)
                            .clickable { query = "" }
                            .weight(0.1f),
                        tint = Color.Black
                    )
            }

            if (allNotes.isEmpty()) Text(
                text = "Add your first note+",
                color = Color.White,
                fontSize = 15.sp
            )

            if (query.isNotEmpty())
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    items(allQueries) { note ->
                        if (query.isNotEmpty())

                            Box(
                                Modifier
                                    .padding(4.dp)
                                    .fillMaxWidth()
                            ) {
                                val formatedDate = date.format(Date(note.timestamp))
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        note.title,
                                        Modifier.clickable {
                                            navController.navigate(EditScreen(note.id.toString()))
                                        },
                                        color = textColor,
                                        fontSize = 20.sp,
                                        maxLines = 1,
                                        fontWeight = FontWeight.W300
                                    )
                                    Text(formatedDate, color = Color.White, fontSize = 12.sp)
                                }
                            }
                    }


                }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 2.dp)
                    .weight(1f)
            ) {

                items(allNotes) { note ->
                    NoteItem(note = note, onDelete = { vm.deleteNote(note) }) {
                        navController.navigate(EditScreen(note.id.toString()))
                    }


                }
            }
        }
        FloatingActionButton(
            onClick = { navController.navigate(EditScreen()) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 25.dp, bottom = 50.dp)
                .size(60.dp),
            shape = CircleShape,
            containerColor = buttonColor,
            content = { Text(text = "Add", color = Color.White) }
        )
    }
}

@Composable
fun NoteItem(note: TaskEntity, onDelete: () -> Unit, onEdit: (String) -> Unit) {
    var showOptions by remember { mutableStateOf(false) }


    Card(
        Modifier
            .padding(2.dp)
            .clip(RoundedCornerShape(20))
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        showOptions = true // Show options when long pressed
                    },
                    onTap = {
                        onEdit(note.id.toString())
                    }
                )
            }
            .heightIn(200.dp), colors = CardDefaults.cardColors(Color(note.color ?: 0xFFEACEB8))
    ) {

        if (showOptions) {
            AlertDialog(
                onDismissRequest = { showOptions = false },
                title = { Text("Options") },
                text = { Text("Choose an action for this note") },
                confirmButton = {
                    Button(onClick = {
                        onDelete()
                        showOptions = false
                    }) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    Button(onClick = { showOptions = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        Column(
            Modifier
                .padding(5.dp)
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Top,
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = note.title,
                    fontWeight = FontWeight.W300,
                    style = MaterialTheme.typography.titleLarge,
                    color = textColor,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = FontFamily.Default
                )
            }
            Text(
                text = note.description ?: "Nothing to show",
                Modifier.padding(bottom = 10.dp, top = 4.dp),
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                color = textColor,
                fontWeight = FontWeight.W300
            )
            val timestamp = note.timestamp
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            date.timeZone = TimeZone.getDefault()
            val formatedDate = date.format(Date(timestamp))
            Text(
                text = formatedDate,
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}