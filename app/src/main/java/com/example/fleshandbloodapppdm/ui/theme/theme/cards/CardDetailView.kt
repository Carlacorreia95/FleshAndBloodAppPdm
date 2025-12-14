package com.example.fleshandbloodapppdm.ui.theme.theme.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fleshandbloodapppdm.ui.theme.theme.theme.FleshAndBloodAppPdmTheme

@Composable
fun CardDetailView(
    navController: NavController,
    docId: String? = null,
    deckId: String
) {
    val viewModel: CardDetailViewModel = viewModel()
    val uiState by viewModel.uiState

    LaunchedEffect(docId) {
        if (docId != null) {
            viewModel.fetchCard(deckId, docId)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextField(
            modifier = Modifier.padding(8.dp),
            value = uiState.name ?: "",
            onValueChange = { viewModel.setName(it) },
            label = { Text("product name") }
        )

        TextField(
            modifier = Modifier.padding(8.dp),
            value = uiState.qtd?.toString() ?: "",
            onValueChange = { text ->
                // safe parse
                val number = text.toDoubleOrNull()
                viewModel.setQtd(number)
            },
            label = { Text("Qtd") }
        )

        if (uiState.error != null) {
            Text(
                text = uiState.error ?: "",
                modifier = Modifier.padding(8.dp)
            )
        }

        Button(onClick = {
            viewModel.saveCard(
                deckId = deckId,
                onSuccess = {
                    navController.popBackStack()
                },
                onError = {

                }
            )
        }) {
            Text(
                "Add",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardDetailViewPreview() {
    FleshAndBloodAppPdmTheme {
        CardDetailView(
            navController = rememberNavController(),
            deckId = ""
        )
    }
}
