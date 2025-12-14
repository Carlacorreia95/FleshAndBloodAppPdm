package com.example.fleshandbloodapppdm.repositories

import com.example.fleshandbloodapppdm.models.Deck
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DeckRepository @Inject constructor(
    private val db: FirebaseFirestore
){
    fun fetchDecks() : Flow<ResultWrapper<List<Deck>>> = flow{
        try {
            emit (ResultWrapper.Loading())
            db.collection("decks")
                .whereArrayContains("owners", Firebase.auth.currentUser?.uid!!)
                .snapshotFlow()
                .collect {result ->
                    val decks = mutableListOf<Deck>()
                    for (document in result.documents){
                        val deck = document.toObject(Deck::class.java)
                        deck?.docId = document.id
                        deck?.let{
                            decks.add(deck)
                        }
                    }
                    emit(ResultWrapper.Success(decks.toList()))
                }

        }catch (e: Exception){
            emit(ResultWrapper.Error(e.message?:""))
        }
    }.flowOn(Dispatchers.IO)

    fun addDeck(deck: Deck): Flow<ResultWrapper<Unit>> = flow {
        try {
            emit(ResultWrapper.Loading())

            db.collection("decks")
                .add(deck)  // <- top-level "decks" collection
                .await()

            emit(ResultWrapper.Success(Unit))
        } catch (e: Exception) {
            emit(ResultWrapper.Error(e.message ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)

    fun updateDeckName(docId: String, newName: String) = flow {
        emit(ResultWrapper.Loading())
        try {
            db
                .collection("decks")
                .document(docId)
                .update("name", newName)
            emit(ResultWrapper.Success(Unit))
        } catch (e: Exception){
            emit(ResultWrapper.Error(e.message?:""))
        }
    }

    fun deleteDeck(docId: String) = flow {
        emit(ResultWrapper.Loading())
        try {
            db.collection("decks")
                .document(docId)
                .delete()
                .await() // needs kotlinx-coroutines-play-services
            emit(ResultWrapper.Success(Unit))
        } catch (e: Exception) {
            emit(ResultWrapper.Error(e.message ?: "Failed to delete deck"))
        }
    }
}