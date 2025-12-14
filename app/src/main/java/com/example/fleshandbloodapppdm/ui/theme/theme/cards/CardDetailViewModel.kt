package com.example.fleshandbloodapppdm.ui.theme.theme.cards

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.fleshandbloodapppdm.models.Card
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

data class CardDetailState(
    var name: String? = null,
    var qtd: Double? = null,
    var error: String? = null,
    var isLoading: Boolean? = null
)

class CardDetailViewModel : ViewModel() {

    var uiState = mutableStateOf(CardDetailState())
        private set

    var docId: String? = null

    private val db = Firebase.firestore

    fun setName(name: String) {
        uiState.value = uiState.value.copy(name = name)
    }

    fun setQtd(qtd: Double?) {
        uiState.value = uiState.value.copy(qtd = qtd)
    }

    fun fetchCard(deckId: String, docId: String) {
        this.docId = docId
        uiState.value = uiState.value.copy(isLoading = true, error = null)

        db.collection("decks")
            .document(deckId)
            .collection("cards")
            .document(docId)
            .get()
            .addOnSuccessListener { snap ->
                uiState.value = uiState.value.copy(
                    name = snap.getString("name"),
                    qtd = snap.getDouble("qtd"),
                    error = null,
                    isLoading = false
                )
            }
            .addOnFailureListener { e ->
                uiState.value = uiState.value.copy(
                    error = e.message ?: "Failed to load card",
                    isLoading = false
                )
            }
    }

    fun saveCard(
        deckId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val name = uiState.value.name
        val qtd = uiState.value.qtd

        if (name.isNullOrBlank()) {
            onError("Name is required")
            return
        }

        uiState.value = uiState.value.copy(isLoading = true, error = null)

        val card = Card(
            name = name,
            qtd = qtd
        )

        if (docId == null) {
            // CREATE
            db.collection("decks")
                .document(deckId)
                .collection("cards")
                .add(card)
                .addOnSuccessListener {
                    uiState.value = uiState.value.copy(isLoading = false, error = null)
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    val msg = e.message ?: "Failed to save card"
                    uiState.value = uiState.value.copy(isLoading = false, error = msg)
                    onError(msg)
                }
        } else {
            // UPDATE
            db.collection("decks")
                .document(deckId)
                .collection("cards")
                .document(docId!!)
                .set(card)
                .addOnSuccessListener {
                    uiState.value = uiState.value.copy(isLoading = false, error = null)
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    val msg = e.message ?: "Failed to update card"
                    uiState.value = uiState.value.copy(isLoading = false, error = msg)
                    onError(msg)
                }
        }

    }
}
