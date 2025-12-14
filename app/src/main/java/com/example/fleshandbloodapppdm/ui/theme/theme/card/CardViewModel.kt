package com.example.fleshandbloodapppdm.ui.theme.theme.card

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.fleshandbloodapppdm.models.Card
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

data class CardState(
    var cards: List<Card> = emptyList(),
    var error: String? = null,
    var isLoading: Boolean? = null
)
public class CardViewModel : ViewModel() {

    var uiState = mutableStateOf(CardState())
        private set

    var deckId : String? = null

    val db = Firebase.firestore
    fun fetchCards(deckId : String) {
        this.deckId = deckId
        uiState.value = uiState.value.copy(isLoading = true)


        db
            .collection("decks")
            .document(deckId)
            .collection("cards")
            .addSnapshotListener { result, error ->
                if (error != null) {
                    uiState.value = uiState.value.copy(
                        error = error.message,
                        isLoading = false
                    )
                    return@addSnapshotListener
                }

                var cards = mutableListOf<Card>()
                for (document in result?.documents?:emptyList()) {
                    var card = document.toObject(Card::class.java)
                    card?.docId = document.id
                    card?.let {
                        cards.add(card)
                    }

                }
                uiState.value = uiState.value.copy(
                    cards = cards,
                    error = null,
                    isLoading = false
                )
            }

    }

    fun checkCard(docId: String, isChecked: Boolean) {

        db
            .collection("decks")
            .document(deckId!!)
            .collection("cards")
            .document(docId)
            .update(mapOf("checked" to isChecked))
    }

    fun deleteCard(docId: String) {
        db
            .collection("decks")
            .document(deckId!!)
            .collection("cards")
            .document(docId)
            .delete()
    }



}