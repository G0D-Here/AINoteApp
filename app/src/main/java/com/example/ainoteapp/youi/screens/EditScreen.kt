package com.example.ainoteapp.youi.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ainoteapp.data.local.TaskEntity
import com.example.ainoteapp.translator.translator
import com.example.ainoteapp.youi.NotesViewModel
import com.example.ainoteapp.youi.components.CustomTextField

@Composable
fun EditScreen(
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = hiltViewModel(),
    navController: NavHostController,
    id: String? = null
) {
    val note by viewModel.note.collectAsState()
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var backColor by remember { mutableStateOf(note?.color) }

    // Load note if editing existing one
    LaunchedEffect(id) {
        if (!id.isNullOrEmpty()) {
            isLoading = true
            try {
                viewModel.getTaskById(id.toInt())
            } catch (e: Exception) {
                errorMessage = "Failed to load note: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    // Update local state when note loads
    LaunchedEffect(note) {
        note?.let {
            title = it.title
            content = it.description ?: ""
            backColor = it.color
        }
    }

    Box(modifier.fillMaxSize()) {

        when {
            isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            errorMessage != null -> Text(text = errorMessage.toString())
            else -> {
                EditNoteContent(
                    viewModel = viewModel,
                    navController = navController,
                    note = note,
                    title = title,
                    onTitleChange = { title = it },
                    content = content,
                    onContentChange = { content = it },
                    colorNew = { backColor = it },
                    onSave = {
                        val currentNote = note?.copy(
                            title = title,
                            description = content,
                            color = backColor
                        ) ?: TaskEntity(
                            title = title,
                            description = content,
                            color = backColor
                        )
                        viewModel.updateNote(currentNote)
                        navController.popBackStack()
                    },
                    onCancel = { navController.popBackStack() },
                    color = backColor ?: 0xFFEACEB,
                    delete = {
                        viewModel.deleteNote(note!!)
                        navController.navigateUp()
                    },
                    context = LocalContext.current
                )
            }
        }
    }
}

@Composable
private fun EditNoteContent(
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
    note: TaskEntity? = null,
    title: String = "",
    onTitleChange: (String) -> Unit = {},
    content: String = "",
    onContentChange: (String) -> Unit = {},
    onSave: () -> Unit = {},
    onCancel: () -> Unit = {},
    colorNew: (Long?) -> Unit = {},
    color: Long? = 0xFFEACEB8,
    delete: () -> Unit = {},
    context: Context
) {
    var undo by remember { mutableStateOf("") }
    var translatedTitle by remember { mutableStateOf(note?.title) }
    var translatedContent by remember { mutableStateOf(note?.description) }

    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(if (color != null) Color(color) else Color(0xFFEACEB8))
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    "Back",
                    Modifier.clickable { onCancel.invoke() },
                    fontSize = 18.sp,
                    color = titleColor,
                    fontWeight = FontWeight.W500
                )

                var drop by remember { mutableStateOf(false) }

                Box {
                    IconButton(onClick = { drop = true }) {
                        Icon(imageVector = Icons.Filled.MoreVert, null)
                    }
                    DropdownMenu(
                        expanded = drop,
                        onDismissRequest = { drop = false },
                        modifier = Modifier.padding(0.dp)
                    ) {
                        if (note != null) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Delete",
                                        Modifier.padding(0.dp), color = Color.Red
                                    )
                                },
                                onClick = {
                                    delete.invoke()
                                    drop = false
                                },
                                Modifier.padding(0.dp),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.Delete,
                                        null,
                                        tint = Color.Red
                                    )
                                }
                            )
                        }
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Share",
                                    Modifier.padding(0.dp)
                                )
                            },
                            onClick = {
                                drop = false
                                note?.let {
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            "${note.title}\n\n${note.description}"
                                        )
                                    }
                                    context.startActivity(
                                        Intent.createChooser(
                                            shareIntent,
                                            "Share Note"
                                        )
                                    )
                                }
                            },
                            Modifier.padding(0.dp),
                            leadingIcon = { Icon(imageVector = Icons.Filled.Share, null) }
                        )
                        DropdownMenuItem(text = { Text("Translate") }, onClick = {
                            drop = false
                            translator(title, "hi") {
                                translatedTitle = it
                            }
                            translator(content, "hi") {
                                translatedContent = it
                            }
                        })
                    }
                }
            }
            CustomTextField(
                value = translatedTitle?:title,
                onValueChange = {
                    onTitleChange(it)
                    translatedTitle = it
                },
                label = "Title (Required)",
                textStyle = TextStyle(
                    fontSize = 30.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.W300
                ),
                colorIt = TextFieldDefaults.colors(
                    unfocusedTextColor = textColor,
                    focusedTextColor = textColor,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                )
            )

            CustomTextField(
                value = translatedContent?: content,
                onValueChange = {
                    onContentChange(it)
                    translatedContent = it
                },
                label = "Content (Required)",
                modifier = Modifier.weight(1f),
                singleLine = false,
                textStyle = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.W300
                ),
                maxLines = Int.MAX_VALUE,
                colorIt = TextFieldDefaults.colors(
                    unfocusedTextColor = textColor,
                    focusedTextColor = textColor,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                )
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                OutlinedButton(
                    onClick = {
                        if (note == null) {
                            viewModel.addNote(
                                TaskEntity(
                                    title = title,
                                    description = content
                                )
                            )
                            navController.navigateUp()
                        } else onSave.invoke()
                    },
                    modifier = Modifier
                        .size(90.dp)
                        .clip(RoundedCornerShape(12)),
                    border = BorderStroke(1.dp, Color.White),
                    enabled = title.isNotEmpty() && content.isNotEmpty(),
                ) {
                    Text("Save", color = Color.White)
                }
            }

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf(0xFFA79277, 0xFFEACEB8, 0xFFB996A1).forEach { colorValue ->
                    Button(
                        onClick = { colorNew(colorValue) },
                        modifier = Modifier
                            .size(35.dp)
                            .clip(CircleShape),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(Color(colorValue)),
                        elevation = ButtonDefaults.buttonElevation(3.dp, 1.dp, 1.dp, 1.dp)
                    ) {}
                }
            }
        }
    }
}

@Composable
private fun ErrorMessage(
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onDismiss) {
            Text("Retry")
        }
    }
}
