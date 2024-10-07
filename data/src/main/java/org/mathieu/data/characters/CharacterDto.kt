package org.mathieu.data.characters

import com.google.gson.annotations.SerializedName

data class GetCharactersDto(
    val info: PagerInfo,
    val results: List<CharacterDto>
)


data class PagerInfo(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)



/**
 * Data Transfer Object (DTO) representing a character's information.
 * This class is used with Retrofit to map the JSON response of a character's endpoint.
 *
 * @property id The unique identifier of the character.
 * @property name The name of the character.
 * @property status The current status of the character (e.g., 'Alive', 'Dead', 'unknown').
 * @property species The species of the character.
 * @property type The type or subspecies of the character.
 * @property gender The gender of the character (e.g., 'Female', 'Male', 'Genderless', 'unknown').
 * @property origin The origin location of the character (name and link to the location).
 * @property location The last known location of the character (name and link to the location).
 * @property image The URL of the character's image (300x300px).
 * @property episodes A list of episode URLs in which this character appeared.
 * @property url The URL of the character's endpoint.
 * @property created The timestamp of when the character was created in the database.
 */
data class CharacterDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("species")
    val species: String,

    @SerializedName("type")
    val type: String,

    @SerializedName("gender")
    val gender: String,

    @SerializedName("origin")
    val origin: LocationDto,

    @SerializedName("location")
    val location: LocationDto,

    @SerializedName("image")
    val image: String,

    @SerializedName("episode")
    val episodes: List<String>,

    @SerializedName("url")
    val url: String,

    @SerializedName("created")
    val created: String
)

/**
 * Data Transfer Object (DTO) representing a location's information.
 * This class is used to store both the origin and last known location of the character.
 *
 * @property name The name of the location.
 * @property url The URL of the location's endpoint.
 */
data class LocationDto(
    @SerializedName("name")
    val name: String,

    @SerializedName("url")
    val url: String
)

fun CharacterDto.toEntity() = CharacterEntity(
    id = id,
    name = name,
    pictureUrl = image
)

