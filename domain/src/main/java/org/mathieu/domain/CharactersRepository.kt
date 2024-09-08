package org.mathieu.domain

interface CharactersRepository {
    suspend fun getAllCharacters(): List<Character>

    suspend fun getCharacterById(id: Int): Character
}