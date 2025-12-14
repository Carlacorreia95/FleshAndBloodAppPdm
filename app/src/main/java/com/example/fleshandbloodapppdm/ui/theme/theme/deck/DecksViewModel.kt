package com.example.fleshandbloodapppdm.ui.theme.theme.deck

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fleshandbloodapppdm.models.Deck
import com.example.fleshandbloodapppdm.repositories.DeckRepository
import com.example.fleshandbloodapppdm.repositories.ResultWrapper
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
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

    fun addDeck(name: String) {
        val uid = Firebase.auth.currentUser?.uid ?: return

        val deck = Deck(
            name = name.trim(),
            owners = listOf(uid)
        )

        deckRepository.addDeck(deck).onEach { result ->
            when (result) {
                is ResultWrapper.Success -> {
                    uiState.value = uiState.value.copy(
                        error = null,
                        isLoading = false
                    )
                }
                is ResultWrapper.Loading -> {
                    uiState.value = uiState.value.copy(isLoading = true)
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

    fun renameDeck(deck: Deck, newName: String) {
        val docId = deck.docId ?: return

        deckRepository.updateDeckName(docId, newName).onEach { result ->
            when (result) {
                is ResultWrapper.Success -> {
                    uiState.value = uiState.value.copy(
                        decks = uiState.value.decks.map {
                            if (it.docId == docId) it.copyWithName(newName) else it
                        },
                        error = null,
                        isLoading = false
                    )
                }
                is ResultWrapper.Loading -> {
                    uiState.value = uiState.value.copy(isLoading = true)
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


    fun deleteDeck(deck: Deck) {
        val docId = deck.docId ?: return

        deckRepository.deleteDeck(docId).onEach { result ->
            when (result) {
                is ResultWrapper.Success -> {
                    uiState.value = uiState.value.copy(
                        decks = uiState.value.decks.filterNot { it.docId == docId },
                        error = null,
                        isLoading = false
                    )
                }
                is ResultWrapper.Loading -> {
                    uiState.value = uiState.value.copy(isLoading = true)
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
