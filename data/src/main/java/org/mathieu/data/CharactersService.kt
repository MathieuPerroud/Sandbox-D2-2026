package org.mathieu.data

import retrofit2.http.GET

interface CharactersService {
    @GET("character")
    suspend fun getCharacters(): GetCharactersDto
}