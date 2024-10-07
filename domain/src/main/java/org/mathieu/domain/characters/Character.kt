package org.mathieu.domain.characters

import java.util.Date

data class Character(
    val id: Int,
    val name: String,
    val pictureUrl: String
)

//c
data class CharactersAndVersion(
    val list: List<Character>,
    val version: Version
)

sealed interface Version {
    data class Old(val lastDateUpdate: Date) : Version
    data object Latest : Version
}
