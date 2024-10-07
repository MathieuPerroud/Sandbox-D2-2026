package org.mathieu.data.characters

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharactersService {
    /**
     * Fetches a list of characters from the API.
     *
     * If the page parameter is not provided, it defaults to fetching the first page.
     *
     * @param page The page number to fetch. If null, the first page is fetched by default.
     * @return A paginated response containing a list of [CharacterDto] for the specified page.
     */
    @GET("character/")
    suspend fun getCharacters(@Query("page") page: Int?): GetCharactersDto


    /**
     * Fetches the details of a character with the given ID from the service.
     *
     * @param id The unique identifier of the character to retrieve.
     * @return The [CharacterDto] representing the details of the character.
     */
    @GET("character/{characterId}")
    suspend fun getCharacter(@Path("characterId") id: Int): CharacterDto?

}