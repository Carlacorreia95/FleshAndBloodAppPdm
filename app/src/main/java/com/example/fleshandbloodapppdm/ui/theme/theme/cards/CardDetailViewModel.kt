package com.example.fleshandbloodapppdm.ui.theme.theme.cards

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.fleshandbloodapppdm.models.Card
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

data class CardDetailState(
    var name : String? = null,
    var qtd : Double? = null,
    var error: String? = null,
    var isLoading: Boolean? = null
)
public class CardDetailViewModel : ViewModel() {

    var uiState = mutableStateOf(CardDetailState())
        private set

    var docId : String? = null

    fun setName(name : String) {
        uiState.value = uiState.value.copy(name = name)
    }

    fun setQtd(qtd : Double) {
        uiState.value = uiState.value.copy(qtd = qtd)
    }

    fun fetchCard( deckId : String,docId : String) {
        this.docId = docId
        uiState.value = uiState.value.copy(isLoading = true)
        val db = Firebase.firestore
        db
            .collection("decks")
            .document(deckId)
            .collection("cards")
            .document(docId)
            .get()
            .addOnSuccessListener {
                uiState.value = uiState.value.copy(
                    name = it.data?.get("name").toString(),
                    qtd = (it.data?.get("qtd")?:"0.0").toString().toDouble(),
                    error = null,
                    isLoading = false
                )
            }
    }

    fun createCard(deckId : String) {
        uiState.value = uiState.value.copy(isLoading = true)
        val db = Firebase.firestore
        if (docId == null) {

            db
                .collection("decks")
                .document(deckId)
                .collection("cards")
                .add(
                    Card(
                        name = uiState.value.name,
                        qtd = uiState.value.qtd
                    )
                ).addOnSuccessListener {

                }
        }else{
            db
                .collection("decks")
                .document(deckId)
                .collection("cards")
                .document(docId!!)
                .update(
                    mapOf(
                        "name" to uiState.value.name,
                        "qtd" to uiState.value.qtd)
                )

        }

    }

}


