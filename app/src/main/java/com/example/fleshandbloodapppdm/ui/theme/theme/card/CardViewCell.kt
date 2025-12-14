package com.example.fleshandbloodapppdm.ui.theme.theme.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fleshandbloodapppdm.models.Card
import com.example.fleshandbloodapppdm.ui.theme.theme.theme.FleshAndBloodAppPdmTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

@Composable
fun CardViewCell(
    modifier: Modifier = Modifier,
    card: Card,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card ( modifier = modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable{
            onClick()
        }){
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                card.name ?: "",
                modifier = Modifier.weight(1f),
                fontSize = 24.sp
            )
            Text(
                card.qtd.toString(),
                fontSize = 24.sp
            )
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete card"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductViewCellPreview() {
    FleshAndBloodAppPdmTheme {
        CardViewCell(
            card = Card(
                name = "Card 1",
                qtd = 1.0,
            ),
            onClick = {},
            onDelete = {}
        )
    }
}