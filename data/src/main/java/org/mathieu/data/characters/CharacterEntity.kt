package org.mathieu.data.characters

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.mathieu.domain.characters.Character

@Entity
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val pictureUrl: String
)

fun CharacterEntity.toModel() = Character(
    id = id,
    name = name,
    pictureUrl = pictureUrl
)