package com.example.fleshandbloodapppdm.ui.theme.theme.deck

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fleshandbloodapppdm.models.Deck
import com.example.fleshandbloodapppdm.repositories.DeckRepository
import com.example.fleshandbloodapppdm.repositories.ResultWrapper
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

data class DecksState(
    var decks: List<Deck> = emptyList(),
    var error: String? = null,
    var isLoading: Boolean? = null
)

@HiltViewModel
class DecksViewModel @Inject constructor(
    val deckRepository: DeckRepository
)
    : ViewModel() {

    var uiState = mutableStateOf(DecksState())
        private set

    val db = Firebase.firestore
    fun fetchDecks() {
        deckRepository.fetchDecks().onEach {result ->
            when(result){
                is ResultWrapper.Success -> {
                    uiState.value = uiState.value.copy(
                        decks = result.data?: emptyList(),
                        error = null,
                        isLoading = false
                    )
                }
                is ResultWrapper.Loading -> {
                    uiState.value = uiState.value.copy(
                        isLoading = true
                    )
                }
                is ResultWrapper.Error -> {
                    uiState.value = uiState.value.copy(
                        error = result.message,
                        isLoading = false
                    )
                }
            }
        }.launchIn(viewModelScope)


    }

    fun addDeck(){

        val uid = Firebase.auth.currentUser?.uid!!
        val deck = Deck(name = "New Deck ${
            uiState.value.decks.size + 1
        }",owners = listOf(uid))

        deckRepository.addDeck(deck).onEach { result->
            when(result){
                is ResultWrapper.Success -> {
                    uiState.value = uiState.value.copy(
                        error = null,
                        isLoading = false
                    )
                }
                is ResultWrapper.Loading -> {
                    uiState.value = uiState.value.copy(
                        isLoading = true
                    )
                }
                is ResultWrapper.Error -> {
                    uiState.value = uiState.value.copy(
                        error = result.message,
                        isLoading = false
                    )
                }
            }
        }.launchIn(viewModelScope)


    }


}
