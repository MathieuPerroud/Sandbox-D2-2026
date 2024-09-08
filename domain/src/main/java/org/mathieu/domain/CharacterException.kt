package org.mathieu.domain

sealed interface CharacterException

data class CharacterNotFoundForId(val id: Int):
    CharacterException,
    Throwable(message = "Character cannot be fin for id: $id")
