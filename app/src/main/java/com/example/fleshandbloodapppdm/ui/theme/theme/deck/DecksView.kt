package com.example.fleshandbloodapppdm.ui.theme.theme.deck

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fleshandbloodapppdm.ui.theme.theme.theme.FleshAndBloodAppPdmTheme
import androidx.compose.runtime.getValue
import com.example.fleshandbloodapppdm.models.Deck
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


@Composable
fun DecksView(
    navController: NavController,
) {
    val viewModel: DecksViewModel = hiltViewModel()
    val uiState by viewModel.uiState

    var showRenameDialog by remember { mutableStateOf(false) }
    var selectedDeck by remember { mutableStateOf<Deck?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var newDeckName by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.fetchDecks()
    }
    if (showRenameDialog && selectedDeck != null) {
        AlertDialog(
            onDismissRequest = {
                showRenameDialog = false
                selectedDeck = null
                newDeckName = ""
            },
            title = { Text("Rename deck") },
            text = {
                OutlinedTextField(
                    value = newDeckName,
                    onValueChange = { newDeckName = it },
                    label = { Text("Deck name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    enabled = newDeckName.trim().isNotEmpty(),
                    onClick = {
                        viewModel.renameDeck(
                            deck = selectedDeck!!,
                            newName = newDeckName.trim()
                        )
                        showRenameDialog = false
                        selectedDeck = null
                        newDeckName = ""
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showRenameDialog = false
                    selectedDeck = null
                    newDeckName = ""
                }) {
                    Text("Cancel")
                }
            }
        )
    }
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = {
                showAddDialog = false
                newDeckName = ""
            },
            title = { Text("New deck") },
            text = {
                OutlinedTextField(
                    value = newDeckName,
                    onValueChange = { newDeckName = it },
                    label = { Text("Deck name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val name = newDeckName.trim()
                        if (name.isNotEmpty()) {
                            viewModel.addDeck(name)
                            showAddDialog = false
                            newDeckName = ""
                        }
                    },
                    enabled = newDeckName.trim().isNotEmpty()
                ) {
                    Text("Create")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAddDialog = false
                        newDeckName = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    androidx.compose.material3.Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .padding(all = 16.dp),


                    ) {
                    Text("Logout")
                }
            }
        },
        floatingActionButton = {
            Button(onClick = { showAddDialog = true }) {
                Text("Add")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading == true -> {
                    CircularProgressIndicator()
                }
                uiState.error != null -> {
                    Text(
                        uiState.error!!,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        itemsIndexed(uiState.decks) { _, item ->
                            Row(
                                modifier = Modifier
                                    .height(60.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                DeckViewCell(
                                    modifier = Modifier.weight(1f),
                                    deck = item,
                                    onClick = {
                                        navController.navigate("cards/${item.docId}")
                                    },
                                    onLongClick = {
                                        selectedDeck = item
                                        newDeckName = item.name ?: ""
                                        showRenameDialog = true
                                    }
                                )

                                IconButton(
                                    onClick = { viewModel.deleteDeck(item) }
                                ) {
                                    Icon(
                                        Icons.Filled.Delete,
                                        contentDescription = "Delete deck"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeViewPreview() {
    FleshAndBloodAppPdmTheme {
        DecksView(navController = rememberNavController())
    }
}