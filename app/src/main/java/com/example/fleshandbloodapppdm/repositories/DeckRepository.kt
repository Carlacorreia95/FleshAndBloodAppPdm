package com.example.fleshandbloodapppdm.repositories

import com.example.fleshandbloodapppdm.models.Deck
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
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
                    var decks = mutableListOf<Deck>()
                    for (document in result?.documents ?: emptyList()){
                        var deck = document.toObject(Deck::class.java)
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
    fun addDeck(deck:Deck) : Flow<ResultWrapper<Unit>> = flow{
        try{
            emit(ResultWrapper.Loading())
            db.collection("Decks")
                .add(deck)
                .await()
            emit(ResultWrapper.Success(Unit))
        }catch (e:Exception){
            emit(ResultWrapper.Error(e.message?:""))
        }
    }.flowOn(Dispatchers.IO)
}