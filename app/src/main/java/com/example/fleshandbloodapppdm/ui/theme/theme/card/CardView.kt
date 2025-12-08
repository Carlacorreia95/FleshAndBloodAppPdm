package com.example.fleshandbloodapppdm.ui.theme.theme.card

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fleshandbloodapppdm.ui.theme.theme.theme.FleshAndBloodAppPdmTheme

@Composable
fun CardView(
    navController: NavController,
    modifier: Modifier,
    deckId: String
){
    val viewModel:CardViewModel = viewModel()
    val uiState by viewModel.uiState
    LaunchedEffect(Unit) {
        viewModel.fetchCards(deckId)
    }
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {

        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading == true) {
                CircularProgressIndicator()
            } else if (uiState.error != null) {
                Text(
                    uiState.error!!,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(
                        items = uiState.cards
                    ) { index, item ->
                        Row (
                            modifier = Modifier
                                .height(60.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically

                        ){
                            CardViewCell(
                                card = item,
                                onClick = {
                                    navController.navigate("cardDetail/$deckId/${item.docId}")
                                },
                                onCheckedChange = {
                                    viewModel.checkCard(
                                        docId = item.docId!!,
                                        isChecked = it
                                    )
                                }
                            )
                        }
                    }
                }
            }

        }
        Button(
            onClick = {
                navController.navigate("cardDetail/$deckId")
            },
            modifier = Modifier.padding(12.dp)
        ) {
            Text("Add")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeViewPreview() {
    FleshAndBloodAppPdmTheme {
        CardView(navController = rememberNavController(),
            modifier = Modifier,
            deckId = "")
    }
}

