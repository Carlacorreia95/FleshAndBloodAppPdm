package com.example.fleshandbloodapppdm.models

class Deck (
    var docId  : String? = null,
    var name   : String? = null,
    var owners   : List<String>? = null,
){

    fun copyWithName(newName: String): Deck {
        return Deck(
            name = newName,
            owners = owners
        ).also {
            it.docId = docId
        }
    }
}