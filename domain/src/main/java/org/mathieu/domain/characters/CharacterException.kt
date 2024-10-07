package org.mathieu.domain.characters

sealed interface CharacterException

data class CharacterNotFoundForId(val id: Int) :
    CharacterException,
    Throwable(message = "Character cannot be find for id: $id")

data class CharactersNotFound(private val exception: Exception) :
    CharacterException,
    Throwable(cause = exception, message = "Cannot find any character.")


data class CannotProvideUpdatedCharacters(private val exception: Exception) :
    CharacterException,
    Throwable(cause = exception, message = "Cannot retrieve a fresh version of characters list.")