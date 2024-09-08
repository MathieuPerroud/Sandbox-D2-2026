package org.mathieu.data

import org.mathieu.domain.Character
import org.mathieu.domain.CharacterNotFoundForId
import org.mathieu.domain.CharactersRepository


class CharacterRepositoryImpl(
    private val charactersService: CharactersService
) : CharactersRepository {

    private var characters: List<Character> = emptyList()

    private fun getCharactersMapping(
        dto: CharacterDto
    ) = Character(
        id = dto.id,
        name = dto.name,
        pictureUrl = dto.image
    )


    override suspend fun getAllCharacters(): List<Character> {
        val charactersDto = charactersService.getCharacters()

        characters = charactersDto.results.map(::getCharactersMapping)

        return characters
    }

    override suspend fun getCharacterById(id: Int): Character {
        return characters.find {
            it.id == id
        } ?: throw CharacterNotFoundForId(id = id)
    }

}

